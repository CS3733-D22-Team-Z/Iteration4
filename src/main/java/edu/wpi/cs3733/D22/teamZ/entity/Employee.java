package edu.wpi.cs3733.D22.teamZ.entity;

public class Employee {
  private String employeeID;
  private String name;
  private AccessType accesstype;
  private String username;
  private String password;

  public enum AccessType {
    ADMIN("ADMIN"),
    DOCTOR("DOCTOR"),
    NURSE("NURSE");

    private String typeStr;

    AccessType(String typeStr) {
      this.typeStr = typeStr;
    }

    public String accessTypeToString() {
      return this.typeStr;
    }

    public static AccessType getRequestTypeByString(String typeStr) {
      switch (typeStr) {
        case "ADMIN":
          return ADMIN;
        case "DOCTOR":
          return DOCTOR;
        case "NURSE":
          return NURSE;
        default:
          return null;
      }
    }
  }

  public Employee() {}

  public Employee(
      String employeeID, String name, AccessType accessType, String username, String password) {
    this.employeeID = employeeID;
    this.name = name;
    this.accesstype = accessType;
    this.username = username;
    this.password = password;
  }

  public String getEmployeeID() {
    return employeeID;
  }

  public void setEmployeeID(String employeeID) {
    this.employeeID = employeeID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public AccessType getAccesstype() {
    return accesstype;
  }

  public void setAccesstype(AccessType accesstype) {
    this.accesstype = accesstype;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
