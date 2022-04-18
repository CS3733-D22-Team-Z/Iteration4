package edu.wpi.cs3733.D22.teamZ.entity;

public class GiftServiceRequest extends ServiceRequest {

  private String patientName;
  private String patientID;
  private String giftType;

  public GiftServiceRequest(
      String requestID,
      RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation,
      String patientName,
      String patientID,
      String giftType) {
    super(requestID, RequestType.GIFT, status, issuer, handler, targetLocation);
    this.patientName = patientName;
    this.patientID = patientID;
    this.giftType = giftType;
  }

  public String getPatientName() {
    return patientName;
  }

  public void setPatientName(String patientName) {
    this.patientName = patientName;
  }

  public String getPatientID() {
    return patientID;
  }

  public void setPatientID(String patientID) {
    this.patientID = patientID;
  }

  public String getGiftType() {
    return giftType;
  }

  public void setGiftType(String giftType) {
    this.giftType = giftType;
  }
}
