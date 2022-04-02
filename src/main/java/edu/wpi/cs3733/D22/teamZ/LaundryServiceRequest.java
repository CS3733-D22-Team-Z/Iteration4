package edu.wpi.cs3733.D22.teamZ;

public class LaundryServiceRequest {
  private String patientName;
  private int patientID;
  private String roomNumber;
  private String linenType;
  private String status;
  private String staffAssigned;

  public LaundryServiceRequest() {}

  public LaundryServiceRequest(
      String patientName,
      int patientID,
      String roomNumber,
      String linenType,
      String status,
      String staffAssigned) {
    this.patientName = patientName;
    this.patientID = patientID;
    this.roomNumber = roomNumber;
    this.linenType = linenType;
    this.status = status;
    this.staffAssigned = staffAssigned;
  }

  // GetterFunctions
  public String getPatientName() {
    return this.patientName;
  }

  public int getPatientID() {
    return this.patientID;
  }

  public String getRoomNumber() {
    return this.roomNumber;
  }

  public String getLinenType() {
    return this.linenType;
  }

  public String getStatus() {
    return status;
  }

  public String getStaffAssigned() {
    return this.staffAssigned;
  }

  // SetterFunctions
  public void setPatientName(String patientName) {
    this.patientName = patientName;
  }

  public void setPatientID(int patientID) {
    this.patientID = patientID;
  }

  public void setRoomNumber(String roomNumber) {
    this.roomNumber = roomNumber;
  }

  public void setLinenType(String linenType) {
    this.linenType = linenType;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setStaffAssigned(String staffAssigned) {
    this.staffAssigned = staffAssigned;
  }
}
