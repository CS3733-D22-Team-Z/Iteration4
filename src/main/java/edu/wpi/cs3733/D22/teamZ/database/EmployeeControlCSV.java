package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeControlCSV extends ControlCSV {
  private String[] headers = {"EmployeeID", "name", "accessType", "username", "password"};

  public EmployeeControlCSV(File path) {
    this.setPath(path);
  }

  protected void writeEmployeeCSV(List<Employee> in) {
    writeCSV(objToData(in), headers);
  }

  protected List<Employee> readEmployeeCSV() throws IOException {
    return dataToObj(readCSV());
  }

  private List<Employee> dataToObj(List<List<String>> data) {
    List<Employee> ret = new ArrayList<>();
    for (List<String> a : data) {
      ret.add(
          new Employee(
              a.get(0),
              a.get(1),
              Employee.AccessType.getRequestTypeByString(a.get(2)),
              a.get(3),
              a.get(4)));
    }
    return ret;
  }

  protected List<List<String>> objToData(List<Employee> in) {
    List<List<String>> ret = new ArrayList<>();

    for (Employee a : in) {
      List<String> entry =
          new ArrayList<>(
              List.of(
                  new String[] {
                    a.getEmployeeID(),
                    a.getName(),
                    a.getAccesstype().accessTypeToString(),
                    a.getUsername(),
                    a.getPassword()
                  }));
      ret.add(entry);
    }
    return ret;
  }
}
