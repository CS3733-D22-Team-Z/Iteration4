package edu.wpi.cs3733.D22.teamZ.entity;

import java.time.LocalDateTime;

public class LabServiceRequest extends ServiceRequest {

  private final String labType;

  public LabServiceRequest(
      String requestID,
      RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation,
      String labType,
      LocalDateTime opened,
      LocalDateTime closed) {
    super(requestID, RequestType.LABS, status, issuer, handler, targetLocation, opened, closed);
    this.labType = labType;
  }

  public LabServiceRequest(
      String requestID,
      RequestStatus status,
      String issuer,
      String handler,
      String targetLocation,
      String labType,
      String opened,
      String closed) {
    super(requestID, RequestType.LABS, status, issuer, handler, targetLocation, opened, closed);
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
