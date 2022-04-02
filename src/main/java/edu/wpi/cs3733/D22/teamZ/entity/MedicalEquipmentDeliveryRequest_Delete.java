package edu.wpi.cs3733.D22.teamZ.entity;

public class MedicalEquipmentDeliveryRequest_Delete {
  private String roomNumber;
  private String equipment;
  private String status;
  private String staffAssigned;

  public MedicalEquipmentDeliveryRequest_Delete() {}

  public MedicalEquipmentDeliveryRequest_Delete(
      String roomNumber, String equipment, String status, String staffAssigned) {
    this.roomNumber = roomNumber;
    this.equipment = equipment;
    this.status = status;
    this.staffAssigned = staffAssigned;
  }

  // GetterFunction
  public String getRoomNumber() {
    return this.roomNumber;
  }

  public String getEquipment() {
    return this.equipment;
  }

  public String getStatus() {
    return status;
  }

  public String getStaffAssigned() {
    return this.staffAssigned;
  }

  // SetterFunction
  public void setRoomNumber(String roomNumber) {
    this.roomNumber = roomNumber;
  }

  public void setEquipment(String equipment) {
    this.equipment = equipment;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setStaffAssigned(String staffAssigned) {
    this.staffAssigned = staffAssigned;
  }
}
