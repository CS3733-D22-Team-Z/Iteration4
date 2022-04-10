package edu.wpi.cs3733.D22.teamZ.entity;

public class LabServiceRequest extends ServiceRequest {

  private final String labType;

  public LabServiceRequest(
      String requestID,
      RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation,
      String labType) {
    super(requestID, RequestType.LABS, status, issuer, handler, targetLocation);
    this.labType = labType;
  }

  public LabServiceRequest(
      String requestID,
      RequestStatus status,
      String issuer,
      String handler,
      String targetLocation,
      String labType) {
    super(requestID, RequestType.LABS, status, issuer, handler, targetLocation);
    this.labType = labType;
  }

  /**
   * Gets the lab type for this lab request
   *
   * @return Lab type
   */
  public String getLabType() {
    return labType;
  }
}
