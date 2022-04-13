package edu.wpi.cs3733.D22.teamZ.entity;

public class DashboardEquipment {
  private String equipmentID;
  private String locationNodeID;
  private String locationNodeType;

  public DashboardEquipment(MedicalEquipment medicalEquipment) {
    this.equipmentID = medicalEquipment.getEquipmentID();
    this.locationNodeID = medicalEquipment.getCurrentLocation().getNodeID();
    setLocationNodeType(medicalEquipment.getCurrentLocation().getNodeType());
  }

  /**
   * gets equipmentID
   *
   * @return equipmentID string
   */
  public String getEquipmentID() {
    return equipmentID;
  }

  /**
   * sets equipmentID
   *
   * @param equipmentID
   */
  public void setEquipmentID(String equipmentID) {
    this.equipmentID = equipmentID;
  }

  /**
   * gets locationNodeID
   *
   * @return locationNodeID string
   */
  public String getLocationNodeID() {
    return locationNodeID;
  }

  /**
   * sets locationNodeID
   *
   * @param locationNodeID
   */
  public void setLocationNodeID(String locationNodeID) {
    this.locationNodeID = locationNodeID;
  }

  /**
   * gets locationNodeType
   *
   * @return getLocationNodeTypeString
   */
  public String getLocationNodeType() {
    return locationNodeType;
  }

  /**
   * sets locationNodeType
   *
   * @param locationNodeType
   */
  public void setLocationNodeType(String locationNodeType) {
    switch (locationNodeType) {
      case "PATI":
        this.locationNodeType = "POD";
        break;
      case "STOR":
        this.locationNodeType = "CLEAN";
        break;
      case "DIRT":
        this.locationNodeType = "DIRTY";
        break;
      default:
        this.locationNodeType = locationNodeType;
        break;
    }
  }
}
