package edu.wpi.cs3733.D22.teamZ.entity;

public class MealServiceRequest {
  private String patientName;
  private int patientID;
  private String roomNumber;
  private String mealServiceOption;
  private String status;
  private String staffAssigned;

  public MealServiceRequest() {}

  public MealServiceRequest(
      String patientName,
      int patientID,
      String roomNumber,
      String mealServiceOption,
      String status,
      String staffAssigned) {
    this.patientName = patientName;
    this.patientID = patientID;
    this.roomNumber = roomNumber;
    this.mealServiceOption = mealServiceOption;
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

  public String getMealServiceOption() {
    return this.mealServiceOption;
  }

  public String getStatus() {
    return status;
  }

  public String getStaffAssigned() {
    return this.staffAssigned;
  }

  // SetterFunction
  public void setPatientName(String patientName) {
    this.patientName = patientName;
  }

  public void setPatientID(int patientID) {
    this.patientID = patientID;
  }

  public void setRoomNumber(String roomNumber) {
    this.roomNumber = roomNumber;
  }

  public void setMealServiceOption(String mealServiceOption) {
    this.mealServiceOption = mealServiceOption;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setStaffAssigned(String staffAssigned) {
    this.staffAssigned = staffAssigned;
  }
}
