package edu.wpi.cs3733.D22.teamZ.entity;

public class MedicalEquipment {
  private String equipmentID;
  private String type;
  private MedicalEquipment.EquipmentStatus status;
  private Location currentLocation;

  // Constructors

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
    this.status = EquipmentStatus.getRequestStatusByString(status);
    this.currentLocation = currentLocation;
    if (!this.currentLocation.getEquipmentList().contains(this)) {
      this.currentLocation.addEquipmentToList(this);
    }
  }

  public enum EquipmentStatus {
    CLEAN("CLEAN"),
    DIRTY("DIRTY"),
    CLEANING("CLEANING"),
    INUSE("INUSE");

    private final String statusStr;

    EquipmentStatus(String statusStr) {
      this.statusStr = statusStr;
    }

    /**
     * Converts this RequestStatus into a String
     *
     * @return A String representing this RequestStatus
     */
    public String toString() {
      return this.statusStr;
    }

    /**
     * Returns a RequestStatus based on the String provided
     *
     * @param statusStr The String used to base the RequestStatus on
     * @return The RequestStatus associated with the String provided or null if no RequestStatus is
     *     found
     */
    public static MedicalEquipment.EquipmentStatus getRequestStatusByString(String statusStr) {
      switch (statusStr) {
        case "CLEAN":
          return CLEAN;
        case "DIRTY":
          return DIRTY;
        case "CLEANING":
          return CLEANING;
        case "INUSE":
          return INUSE;
        default:
          System.out.println("No EquipmentStatus found for the string: |" + statusStr + "|");
          return null;
      }
    }
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
  public EquipmentStatus getStatus() {
    return status;
  }

  /**
   * Sets the availability of a MedicalEquipment object
   *
   * @param status
   */
  public void setStatus(EquipmentStatus status) {
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
    if (this.currentLocation != null) {
      this.getCurrentLocation().removeEquipmentFromList(this);
    }
    this.currentLocation = currentLocation;
    if (!currentLocation.getEquipmentList().contains(this)) {
      currentLocation.addEquipmentToList(this);
    }
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
