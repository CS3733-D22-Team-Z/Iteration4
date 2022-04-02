package edu.wpi.cs3733.D22.teamZ.entity;

public class ComputerServiceRequest {
  private String employeeName;
  private int employeeID;
  private String operatingSystem;
  private String status;
  private String staffAssigned;
  private String problemDescription;

  public ComputerServiceRequest() {}

  public ComputerServiceRequest(
      String employeeName,
      int employeeID,
      String operatingSystem,
      String status,
      String staffAssigned,
      String problemDescription) {
    this.employeeName = employeeName;
    this.employeeID = employeeID;
    this.operatingSystem = operatingSystem;
    this.status = status;
    this.staffAssigned = staffAssigned;
    this.problemDescription = problemDescription;
  }

  // GetterFunctions
  public String getEmployeeName() {
    return employeeName;
  }

  public int getEmployeeID() {
    return employeeID;
  }

  public String getOperatingSystem() {
    return operatingSystem;
  }

  public String getStatus() {
    return status;
  }

  public String getStaffAssigned() {
    return staffAssigned;
  }

  public String getProblemDescription() {
    return problemDescription;
  }

  // SetterFunctions
  public void setEmployeeName(String employeeName) {
    this.employeeName = employeeName;
  }

  public void setEmployeeID(int employeeID) {
    this.employeeID = employeeID;
  }

  public void setOperatingSystem(String operatingSystem) {
    this.operatingSystem = operatingSystem;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setStaffAssigned(String staffAssigned) {
    this.staffAssigned = staffAssigned;
  }

  public void setProblemDescription(String problemDescription) {
    this.problemDescription = problemDescription;
  }
}
