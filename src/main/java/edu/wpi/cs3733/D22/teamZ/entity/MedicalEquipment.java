package edu.wpi.cs3733.D22.teamZ.entity;

public class MedicalEquipment {
  private String equipmentID;
  private String type;
  private String status;
  private Location currentLocation;

  // Constructors

  /**
   * Basic constructor for only MedicalEquipment with equipmentID
   *
   * @param equipmentID
   */
  public MedicalEquipment(String equipmentID) {
    this.equipmentID = equipmentID;
  }

  /**
   * Full constructor for MedicalEquipment
   *
   * @param equipmentID
   * @param type
   * @param status
   * @param currentLocation
   */
  public MedicalEquipment(
      String equipmentID, String type, String status, Location currentLocation) {
    this.equipmentID = equipmentID;
    this.type = type;
    this.status = status;
    this.currentLocation = currentLocation;
  }

  /**
   * Gives the itemID of an MedicalEquipment object
   *
   * @return itemID of MedicalEquipment object
   */
  public String getEquipmentID() {
    return equipmentID;
  }

  /**
   * Gives the availability of a MedicalEquipment object
   *
   * @return status of MedicalEquipment availability
   */
  public String getStatus() {
    return status;
  }

  /**
   * Sets the availability of a MedicalEquipment object
   *
   * @param status
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Gives the current location of the MedicalEquipment object
   *
   * @return Location object
   */
  public Location getCurrentLocation() {
    return currentLocation;
  }

  /**
   * Sets the current location of the MedicalEquipment object
   *
   * @param currentLocation
   */
  public void setCurrentLocation(Location currentLocation) {
    this.currentLocation = currentLocation;
  }

  /**
   * Gives the type of the MedicalEquipment object
   *
   * @return type of MedicalEquipment
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type of the MedicalEquipment object
   *
   * @param type
   */
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof MedicalEquipment) {
      MedicalEquipment objectEquipment = (MedicalEquipment) o;
      return (this.getEquipmentID().equals(objectEquipment.getEquipmentID()));
    } else {
      return false;
    }
  }
}
