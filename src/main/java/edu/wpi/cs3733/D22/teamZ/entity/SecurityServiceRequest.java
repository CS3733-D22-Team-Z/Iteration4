package edu.wpi.cs3733.D22.teamZ.entity;

import lombok.Getter;


public class SecurityServiceRequest extends ServiceRequest {
  @Getter
  private String urgency;
@Getter private String reason;
  public SecurityServiceRequest(
      String requestID,
      RequestType type,
      RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation, String urgency, String reason) {
    super(requestID, type, status, issuer, handler, targetLocation);
    this.urgency = urgency;
    this.reason = reason;
  }

  public SecurityServiceRequest(
      String requestID,
      RequestType type,
      RequestStatus status,
      String issuer,
      String handler,
      String targetLocation, String urgency, String reason) {
    super(requestID, type, status, issuer, handler, targetLocation);
    this.urgency = urgency;
    this.reason = reason;
  }
}
