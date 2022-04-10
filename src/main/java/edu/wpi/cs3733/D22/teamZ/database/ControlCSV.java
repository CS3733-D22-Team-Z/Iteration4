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

  // --------------- Constructor/Factory ---------------

  /**
   * Creates a ControlCSV object that is geared toward handling data from
   * the given class and returns it.
   * @param classType The class that the returned ControlCSV object will
   *                  be able to read.
   * @param <U> The entity class that this object will record data for.
   * @return A new ControlCSV class
   */
  public static <U> ControlCSV<U> makeControlCSV(Class<U> classType) {
    return new ControlCSV<>(classType);
  }

  /**
   * Initializes this ControlCSV class to handle data from the given class
   * @param givenClass The class that this object would be geared to handle
   *                   data from
   */
  private ControlCSV(Class<T> givenClass) {
    this.givenClass = givenClass;
    this.defaultPath = new File("");
    determineHeaders();
    determineWriteMethods();
  }

  // --------------- Public Methods ---------------

  /**
   * Sets the default File for this object
   * @param path The File that will be set as the default
   */
  public void setDefaultPath(File path) {
    this.defaultPath = path;
  }

  /**
   * Attempts to write the given list of objects to the default File set
   * @param list The list of objects that represent the data
   *    that will be written.
   */
  public void writeCSV(List<T> list) {
    writeDataToCSV(objectsToData(list), this.defaultPath);
  }

  /**
   * Attempts to write the given list of objects to the given CSV File.
   * @param path The File that represents the CSV that will be written to.
   * @param list The list of objects that represent the data
   *             that will be written.
   */
  public void writeCSV(File path, List<T> list) {
    writeDataToCSV(objectsToData(list), path);
  }

  /**
   * Attempts to read the File saved as the default and return the data contained
   * as a list of objects.
   * @return A list of objects representing the data contained within the CSV
   * @throws IOException If an I/O error occurs
   */
  public List<T> readCSV() throws IOException {
    return dataToObjects(readDataFromCSV(this.defaultPath));
  }

  /**
   * Attempts to read the given File as a CSV and return the data contained
   * as a list of objects.
   * @param path The File that represents the CSV that will be read.
   * @return A list of objects representing the data contained within the CSV
   * @throws IOException If an I/O error occurs
   */
  public List<T> readCSV(File path) throws IOException {
    return dataToObjects(readDataFromCSV(path));
  }

  // --------------- Private Methods ---------------

  /**
   * Determines what headers the csv will contain
   */
  private void determineHeaders() {
    ArrayList<String> list = new ArrayList<>();

    if (isGivenClassServiceRequestChild()) {
      list.add("requestID");
    }

    Field[] fields = givenClass.getDeclaredFields();
    List<Field> fieldsList = new ArrayList<>(List.of(fields));
    List<String> fieldNames = fieldsList.stream().map(Field::getName).collect(Collectors.toList());

    list.addAll(fieldNames);

    headers = list.toArray(new String[0]);
  }

  /**
   * Determines which methods to call in what order when writing to a CSV
   */
  private void determineWriteMethods() {
    // writeMethods
    List<Method> writeMethods = new ArrayList<>();

    Field[] fields = givenClass.getDeclaredFields();
    List<Field> fieldsList = new ArrayList<>(List.of(fields));
    List<String> fieldNames = fieldsList.stream().map(Field::getName).collect(Collectors.toList());

    // Turn first letter of field name capital and put 'get' at the front
    List<String> supposedMethodNames =
        fieldNames.stream()
            .map(str -> "get" + str.substring(0, 1).toUpperCase() + str.substring(1))
            .collect(Collectors.toList());

    for (String methodName : supposedMethodNames) {
      try {
        Method method = givenClass.getMethod(methodName);
        writeMethods.add(method);
      } catch (NoSuchMethodException e) {
        // TODO catch exception properly
        throw new RuntimeException(
            "The method |"
                + methodName
                + "| could not be found in the class |"
                + givenClass.getName()
                + "|");
      }
    }
  }

  /**
   * Takes in a List<List<String>> that represents the data that should be written.
   * In the outer List object, each list it contains represents one row of info
   * to be written. In each inner List object, each string contains the data to
   * be written to a single column
   * @param in The List<List<String>> that holds the data to be written.
   * @param path The File that represents the CSV that will be written to.
   */
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

  /**
   * Stores each row of the CSV read in a list. Each row is also represented
   * by a list where each string corresponds to the entry in one of the columns
   * @param path The File that represents the CSV that will be read.
   * @return A List<List<String>> that represents all the data in the CSV file
   * @throws IOException If an I/O error occurs
   */
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
      in.readLine(); // just reads past the headers
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

  /**
   * Turns every list of strings that represents an object's fields into that object.
   *
   * @param dataList the list that contains lists of strings which each represent an object T's
   *     fields.
   * @return A List of objects whose fields contain the information represented by the given lists
   *     of strings
   */
  private List<T> dataToObjects(List<List<String>> dataList) {
    return null; // TODO implement dataToObjects function
  }

  /**
   * Turns every object in the list into a list of strings that represents that object's fields
   *
   * @param objectList the list of objects that will be converted to field strings
   * @return a list containing each list of strings that represent the original objects
   */
  private List<List<String>> objectsToData(List<T> objectList) {
    List<List<String>> dataList = new ArrayList<>();

    for (T object : objectList) {
      List<String> fieldList = new ArrayList<>();

      for (Method method : writeMethods) {
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

  /**
   * Determines whether the class given when constructing this object is a subclass of the
   * ServiceRequest class
   *
   * @return true if the given class is a subclass of ServiceRequest, false otherwise
   */
  private boolean isGivenClassServiceRequestChild() {
    Class<?> serviceRequestClass = ServiceRequest.class;
    boolean isCastable = serviceRequestClass.isAssignableFrom(this.givenClass);
    boolean isClass = serviceRequestClass == givenClass;
    return isCastable && !isClass;
  }
}
