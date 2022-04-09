package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;

import java.io.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ControlCSV<T> {

  private final Class<T> givenClass;
  private String[] headers;
  private List<Method> readMethods;
  private List<Method> writeMethods;

  private File defaultPath;

  public static <U> ControlCSV<U> makeReadClass(Class<U> classType) {
    return new ControlCSV<>(classType);
  }

  private ControlCSV(Class<T> givenClass) {
    this.givenClass = givenClass;
    this.defaultPath = new File("");
    determineHeaders();
    determineWriteMethods();
  }

  public void setDefaultPath(File path) {
    this.defaultPath = path;
  }

  private boolean isGivenClassServiceRequest() {
    Class<?> serviceRequestClass = ServiceRequest.class;
    return serviceRequestClass.isAssignableFrom(this.givenClass);
  }

  private void determineHeaders() {
    ArrayList<String> list = new ArrayList<>();

    if(isGivenClassServiceRequest()) {
      list.add("requestID");
    }

    Field[] fields = givenClass.getDeclaredFields();
    List<Field> fieldsList = new ArrayList<>(List.of(fields));
    List<String> fieldNames =
            fieldsList.stream().map(Field::getName).collect(Collectors.toList());

    list.addAll(fieldNames);

    headers = list.toArray(new String[0]);
  }

  private void determineWriteMethods() {
    //writeMethods
    List<Method> writeMethods = new ArrayList<>();

    Field[] fields = givenClass.getDeclaredFields();
    List<Field> fieldsList = new ArrayList<>(List.of(fields));
    List<String> fieldNames =
            fieldsList.stream().map(Field::getName).collect(Collectors.toList());

    //Turn first letter of field name capital and put 'get' at the front
    List<String> supposedMethodNames =
            fieldNames.stream().map(
                    str -> "get" + str.substring(0, 1).toUpperCase() + str.substring(1)
            ).collect(Collectors.toList());

    for(String methodName : supposedMethodNames) {
      try {
        Method method = givenClass.getMethod(methodName);
        writeMethods.add(method);
      } catch(NoSuchMethodException e) {
        //TODO catch exception properly
        throw new RuntimeException("The method |"+methodName+"| could not be found in the class |"+givenClass.getName()+"|");
      }
    }
  }

  public void writeCSV(List<T> list) {
    writeDataToCSV(objectsToData(list), this.defaultPath);
  }

  public void writeCSV(File path, List<T> list) {
    writeDataToCSV(objectsToData(list), path);
  }

  public List<T> readCSV() throws IOException {
    return dataToObjects(readDataFromCSV(this.defaultPath));
  }

  private List<T> dataToObjects(List<List<String>> readDataFromCSV) {
    return null; //TODO implement dataToObjects function
  }

  public List<T> readCSV(File path) throws IOException {
    return dataToObjects(readDataFromCSV(path));
  }

  private List<List<String>> objectsToData(List<T> objectList) {
    List<List<String>> dataList = new ArrayList<>();

    for(T object : objectList) {
      List<String> fieldList = new ArrayList<>();

      for(Method method : writeMethods) {
        try {
          String fieldStr = method.invoke(object).toString();
          fieldList.add(fieldStr);
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          e.printStackTrace();
        }
      }

      dataList.add(fieldList);
    }

    return dataList;
  }

  private void writeDataToCSV(List<List<String>> in, File path) {
    String fLine = String.join(",", headers);
    FileWriter file;
    try {
      file = new FileWriter(path);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    try {
      file.write(fLine + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }

    for (List<String> a : in) {

      String line = String.join(",", a);

      line += "\n";

      try {
        file.write(line);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    try {
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private List<List<String>> readDataFromCSV(File path) throws IOException {
    FileReader temp;
    try {
      temp = new FileReader(path);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
    BufferedReader in = new BufferedReader(temp);

    try {
      in.readLine(); //just reads past the headers
    } catch (IOException e) {
      e.printStackTrace();
    }

    List<List<String>> mid = new ArrayList<>();

    String line;
    while ((line = in.readLine()) != null) {
      String[] split = line.split(",");
      mid.add(List.of(split));
    }
    in.close();

    return mid;
  }
}
