package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements IEmployeeDAO {
  List<Employee> employees;
  private EmployeeControlCSV empCSV;

  static Connection connection = DatabaseConnection.getConnection();

  public EmployeeDAOImpl() {
    employees = new ArrayList<Employee>();
  }

  /**
   * Gets all of the employees in the database
   *
   * @return List of employees
   */
  public List<Employee> getAllEmployees() {
    try {
      PreparedStatement pstmt = connection.prepareStatement("Select * From EMPLOYEES");
      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        String employeeID = rset.getString("employeeID");
        String name = rset.getString("name");
        Employee.AccessType accessType = Employee.AccessType.valueOf(rset.getString("accessType"));
        String username = rset.getString("username");
        String password = rset.getString("password");
        Employee emp = new Employee(employeeID, name, accessType, username, password);
        if (!employees.contains(emp)) {
          employees.add(emp);
        }
      }
    } catch (SQLException e) {
      System.out.println("Failed to get all Employees");
      e.printStackTrace();
    }
    return employees;
  }

  /**
   * Gets ONE employee from the database based on the provided employeeID
   *
   * @param employeeID
   * @return Employee object with provided employeeID
   */
  public Employee getEmployeeByID(String employeeID) {
    Employee emp = new Employee();
    try {
      PreparedStatement pstmt =
          connection.prepareStatement("Select * From EMPLOYEES WHERE EMPLOYEEID = ?");
      pstmt.setString(1, employeeID);
      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        String name = rset.getString("name");
        Employee.AccessType accessType = Employee.AccessType.valueOf(rset.getString("accessType"));
        String username = rset.getString("username");
        String password = rset.getString("password");
        emp.setName(name);
        emp.setAccesstype(accessType);
        emp.setUsername(username);
        emp.setPassword(password);
      }
    } catch (SQLException e) {
      System.out.println("Unable to find employee");
      e.printStackTrace();
    }
    return emp;
  }

  /**
   * Gets ONE employee from the database based on the provided username
   *
   * @param employeeUsername
   * @return Employee object with provided employeeID
   */
  public Employee getEmployeeByUsername(String employeeUsername) {
    Employee emp = new Employee();
    try {
      PreparedStatement pstmt =
          connection.prepareStatement("Select * From EMPLOYEES WHERE USERNAME = ?");
      pstmt.setString(1, employeeUsername);
      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        String name = rset.getString("name");
        Employee.AccessType accessType = Employee.AccessType.valueOf(rset.getString("accessType"));
        String username = rset.getString("username");
        String password = rset.getString("password");
        emp.setName(name);
        emp.setAccesstype(accessType);
        emp.setUsername(username);
        emp.setPassword(password);
      }
    } catch (SQLException e) {
      System.out.println("Unable to find employee");
      e.printStackTrace();
    }
    return emp;
  }

  /**
   * Adds a new employee to database. Will automatically check if already in database
   *
   * @param emp
   * @return True if successful, false if not
   */
  public boolean addEmployee(Employee emp) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "INSERT INTO EMPLOYEES (EMPLOYEEID, NAME, ACCESSTYPE, USERNAME, PASSWORD)"
                  + "values (?, ?, ?, ?, ?)");
      stmt.setString(1, emp.getEmployeeID());
      stmt.setString(2, emp.getName());
      stmt.setString(3, emp.getAccesstype().accessTypeToString());
      stmt.setString(4, emp.getUsername());
      stmt.setString(5, emp.getPassword());

      stmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("Statement failed");
      e.printStackTrace();
      return false;
    }
    employees.add(emp);
    return true;
  }

  /**
   * Updates a employee in the database. Will automatically check if exists in database
   *
   * @param emp
   * @return True if successful, false if not
   */
  public boolean updateEmployee(Employee emp) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "UPDATE EMPLOYEES SET NAME=?, ACCESSTYPE =? WHERE EMPLOYEEID =?");
      stmt.setString(1, emp.getName());
      stmt.setObject(2, emp.getAccesstype());
      stmt.setString(3, emp.getEmployeeID());

      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Statement failed");
      e.printStackTrace();
      return false;
    }
    employees.remove(getEmployeeByID(emp.getEmployeeID()));
    employees.add(emp);
    return true;
  }

  /**
   * Deletes a location from database. Will automatically check if exists in database already
   *
   * @param emp
   * @return True if successful, false if not
   */
  public boolean deleteEmployee(Employee emp) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement("DELETE FROM EMPLOYEES WHERE EmployeeID=?");
      stmt.setString(1, emp.getEmployeeID());
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Statement failed");
      e.printStackTrace();
      return false;
    }
    employees.remove(emp);
    return true;
  }

  /**
   * Exports the Location table into a csv file to the working directory
   *
   * @return True if successful, false if not
   */
  public boolean exportToEmployeeCSV(File empData) {

    empData = new File(System.getProperty("user.dir") + "\\employee.csv");
    empCSV = new EmployeeControlCSV(empData);
    empCSV.writeEmployeeCSV(getAllEmployees());

    return true;
  }

  /**
   * Imports the Employee csv into the Employee table
   *
   * @param employeeData file location of employee csv
   * @return number of conflicts when inserting
   */
  @Override
  public int importEmployeesFromCSV(File employeeData) {
    employeeData = new File(System.getProperty("user.dir") + "\\employee.csv");
    empCSV = new EmployeeControlCSV(employeeData);
    int conflictCounter = 0;
    try {
      List<Employee> tempEmployee = empCSV.readEmployeeCSV();

      try {
        for (Employee info : tempEmployee) {
          PreparedStatement pstmt =
              connection.prepareStatement(
                  "INSERT INTO EMPLOYEES (EMPLOYEEID, NAME, ACCESSTYPE, USERNAME, PASSWORD) "
                      + "values (?, ?, ?, ?, ?)");
          pstmt.setString(1, info.getEmployeeID());
          pstmt.setString(2, info.getName());
          pstmt.setString(3, info.getAccesstype().toString());
          pstmt.setString(4, info.getUsername());
          pstmt.setString(5, info.getPassword());

          // insert it
          pstmt.executeUpdate();
        }
      } catch (SQLException e) {
        conflictCounter++;
        System.out.println("Found " + conflictCounter + " conflicts.");
      }
    } catch (IOException e) {
      System.out.println("Failed to insert into Employee table");
      e.printStackTrace();
    }
    return conflictCounter;
  }
}
