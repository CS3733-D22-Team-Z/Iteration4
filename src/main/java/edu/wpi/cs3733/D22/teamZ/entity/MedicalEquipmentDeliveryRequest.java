package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;

public class MedicalEquipmentDeliveryRequest extends ServiceRequest {

  private String equipmentID;

  FacadeDAO facadeDAO = new FacadeDAO();

  public MedicalEquipmentDeliveryRequest(
      String requestID,
      ServiceRequest.RequestStatus status,
      Employee issuer,
      Employee handler,
      String equipmentID,
      Location targetLoc) {
    super(requestID, RequestType.MEDEQUIP, status, issuer, handler, targetLoc);
    this.equipmentID = equipmentID;
  }

  public MedicalEquipmentDeliveryRequest(
      String requestID,
      ServiceRequest.RequestStatus status,
      String issuer,
      String handler,
      String equipmentID,
      String targetLoc) {
    super(requestID, RequestType.MEDEQUIP, status, issuer, handler, targetLoc);
    this.equipmentID = equipmentID;
  }

  public String getEquipmentID() {
    return equipmentID;
  }

  public void setEquipmentID(String equipmentID) {
    this.equipmentID = equipmentID;
  }
}
