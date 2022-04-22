package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class EmployeeDAOImpl implements IEmployeeDAO {
  private final EmployeeControlCSV empCSV;
  private final List<Employee> employeeList;

  static Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();
  // DatabaseConnection.getConnection();

  public EmployeeDAOImpl() {
    employeeList = new ArrayList<>();
    updateConnection();

    File empData =
        new File(
            System.getProperty("user.dir")
                + System.getProperty("file.separator")
                + "Employees.csv");
    this.empCSV = new EmployeeControlCSV(empData);
  }

  /**
   * Gets all of the employees in the database
   *
   * @return List of employees
   */
  public List<Employee> getAllEmployees() {
    return employeeList;
  }

  /**
   * Gets ONE employee from the database based on the provided employeeID
   *
   * @param employeeID The id of the employee to be searched for
   * @return Employee object with provided employeeID
   */
  public Employee getEmployeeByID(String employeeID) {
    for (Employee emp : employeeList) {
      if (emp.getEmployeeID().equals(employeeID)) {
        return emp;
      }
    }
    return null;
  }

  /**
   * Gets ONE employee from the database based on the provided username
   *
   * @param employeeUsername The username of the employee to be searched for
   * @return Employee object with provided employeeID
   */
  public Employee getEmployeeByUsername(String employeeUsername) {
    updateConnection();
    // Employee employee = null;
    try {
      PreparedStatement pstmt =
          connection.prepareStatement("Select employeeID From EMPLOYEES WHERE USERNAME = ?");
      pstmt.setString(1, employeeUsername);
      ResultSet rset = pstmt.executeQuery();

      if (rset.next()) {
        String employeeID = rset.getString("employeeID");
        for (Employee emp : employeeList) {
          if (emp.getEmployeeID().equals(employeeID)) {
            return emp;
          }
        }
      }
      rset.close();
      pstmt.close();
    } catch (SQLException e) {
      System.out.println("Unable to find employee");
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Adds a new employee to database. Will automatically check if already in database
   *
   * @param emp The employee to be added
   * @return True if successful, false if not
   */
  public boolean addEmployee(Employee emp) {
    updateConnection();
    boolean val = false;
    if (addToDatabase(emp)) {
      val = true;
      employeeList.add(emp);
    }
    return val;
  }

  /**
   * Updates an employee in the database. Will automatically check if exists in database
   *
   * @param emp The employee to be updated
   * @return True if successful, false if not
   */
  public boolean updateEmployee(Employee emp) {
    updateConnection();
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "UPDATE EMPLOYEES SET NAME=?, ACCESSTYPE =? WHERE EMPLOYEEID =?");
      stmt.setString(1, emp.getName());
      stmt.setObject(2, emp.getAccesstype());
      stmt.setString(3, emp.getEmployeeID());

      stmt.executeUpdate();
      stmt.close();
      connection.commit();
      for (Employee employee : employeeList) {
        if (employee.equals(emp)) {
          employee.setName(emp.getName());
          employee.setAccesstype(emp.getAccesstype());
        }
      }
    } catch (SQLException e) {
      System.out.println("Statement failed");
      e.printStackTrace();
      return false;
    }
    // employees.remove(id);
    // employees.put(id, emp);
    return true;
  }

  /**
   * Deletes a location from database. Will automatically check if exists in database already
   *
   * @param emp The employee to be deleted
   * @return True if successful, false if not
   */
  public boolean deleteEmployee(Employee emp) {
    updateConnection();
    try {
      PreparedStatement stmt =
          connection.prepareStatement("DELETE FROM EMPLOYEES WHERE EmployeeID=?");
      stmt.setString(1, emp.getEmployeeID());
      stmt.executeUpdate();
      stmt.close();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("Statement failed");
      e.printStackTrace();
      return false;
    }
    employeeList.remove(emp);
    // employees.remove(emp.getEmployeeID());
    return true;
  }

  /**
   * Exports the Location table into a csv file to the working directory
   *
   * @return True if successful, false if not
   */
  public boolean exportToEmployeeCSV(File empData) {
    try {
      empCSV.writeEmployeeCSV(getAllEmployees(), empData);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

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
    updateConnection();

    int conflictCounter = 0;
    try {
      List<Employee> tempEmployee = empCSV.readEmployeeCSV(employeeData);

      for (Employee info : tempEmployee) {
        try {
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
          pstmt.close();
          connection.commit();
          employeeList.add(info);
          // employees.put(info.getEmployeeID(), info);
        } catch (SQLException e) {
          conflictCounter++;
          System.out.println("Found " + conflictCounter + " conflicts.");
        }
      }

    } catch (IOException e) {
      System.out.println("Failed to insert into Employee table");
      e.printStackTrace();
    }
    return conflictCounter;
  }

  /**
   * Returns the default path for an employee csv file to be saved
   *
   * @return The default path for an employee csv file to be saved
   */
  File getDefaultEmployeeCSVPath() {
    return empCSV.getDefaultPath();
  }

  /** Updates the connection */
  private void updateConnection() {
    connection = EnumDatabaseConnection.CONNECTION.getConnection();
  }

  /**
   * Inserts employees from a list
   *
   * @param list list of employees to be added
   * @return true if successful, false otherwise
   */
  public boolean addEmployeeFromList(List<Employee> list) {
    updateConnection();
    boolean val = true;
    for (Employee info : list) {
      if (!addToDatabase(info)) {
        val = false;
      }
    }
    return val;
  }

  /**
   * Contains the SQL command to insert into DB
   *
   * @return True if successful, false otherwise
   */
  private boolean addToDatabase(Employee emp) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "INSERT INTO EMPLOYEES (EMPLOYEEID, NAME, ACCESSTYPE, USERNAME, PASSWORD)"
                  + "values (?, ?, ?, ?, ?)");
      stmt.setString(1, emp.getEmployeeID());
      stmt.setString(2, emp.getName());
      stmt.setString(3, emp.getAccesstype().toString());
      stmt.setString(4, emp.getUsername());
      stmt.setString(5, emp.getPassword());

      stmt.executeUpdate();
      stmt.close();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("Statement failed");
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
