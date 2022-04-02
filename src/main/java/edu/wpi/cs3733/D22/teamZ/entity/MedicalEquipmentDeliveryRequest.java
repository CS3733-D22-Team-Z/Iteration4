package edu.wpi.cs3733.D22.teamZ.entity;

public class MedicalEquipmentDeliveryRequest {
  private String requestID;
  private String status;
  private String issuer;
  private String handler;
  private String equipment;
  private String currentLoc;
  private String targetLoc;

  public MedicalEquipmentDeliveryRequest() {}

  public MedicalEquipmentDeliveryRequest(String requestID) {
    this.requestID = requestID;
  }

  public MedicalEquipmentDeliveryRequest(
      String requestID,
      String status,
      String issuer,
      String handler,
      String equipment,
      String currentLoc,
      String targetLoc) {
    this.requestID = requestID;
    this.status = status;
    this.issuer = issuer;
    this.handler = handler;
    this.equipment = equipment;
    this.currentLoc = currentLoc;
    this.targetLoc = targetLoc;
  }

  public String getRequestID() {
    return requestID;
  }

  public String getStatus() {
    return status;
  }

  public String getIssuer() {
    return issuer;
  }

  public String getHandler() {
    return handler;
  }

  public String getEquipment() {
    return equipment;
  }

  public String getCurrentLoc() {
    return currentLoc;
  }

  public String getTargetLoc() {
    return targetLoc;
  }

  public void setRequestID(String requestID) {
    this.requestID = requestID;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }

  public void setHandler(String handler) {
    this.handler = handler;
  }

  public void setEquipment(String equipment) {
    this.equipment = equipment;
  }

  public void setCurrentLoc(String currentLoc) {
    this.currentLoc = currentLoc;
  }

  public void setTargetLoc(String targetLoc) {
    this.targetLoc = targetLoc;
  }
}
