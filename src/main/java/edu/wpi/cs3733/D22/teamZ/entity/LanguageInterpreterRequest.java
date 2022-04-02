package edu.wpi.cs3733.D22.teamZ.entity;

public class LanguageInterpreterRequest {
  private String patientName;
  private String patientID;
  private String roomNumber;
  private String language;
  private String status;
  private String staffAssigned;

  public LanguageInterpreterRequest() {}

  public LanguageInterpreterRequest(
      String patientName,
      String patientID,
      String roomNumber,
      String language,
      String status,
      String staffAssigned) {
    this.patientName = patientName;
    this.patientID = patientID;
    this.roomNumber = roomNumber;
    this.language = language;
    this.status = status;
    this.staffAssigned = staffAssigned;
  }

  // GetterFunctions
  public String getPatientName() {
    return patientName;
  }

  public String getPatientID() {
    return patientID;
  }

  public String getRoomNumber() {
    return roomNumber;
  }

  public String getLanguage() {
    return language;
  }

  public String getStatus() {
    return status;
  }

  public String getStaffAssigned() {
    return staffAssigned;
  }

  // SetterFunctions
  public void setPatientName(String patientName) {
    this.patientName = patientName;
  }

  public void setPatientID(String patientID) {
    this.patientID = patientID;
  }

  public void setRoomNumber(String roomNumber) {
    this.roomNumber = roomNumber;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setStaffAssigned(String staffAssigned) {
    this.staffAssigned = staffAssigned;
  }
}
