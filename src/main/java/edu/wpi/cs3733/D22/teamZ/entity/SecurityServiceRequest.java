package edu.wpi.cs3733.D22.teamZ.entity;

public class SecurityServiceRequest extends ServiceRequest {
  public SecurityServiceRequest(
      String requestID,
      RequestType type,
      RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation) {
    super(requestID, type, status, issuer, handler, targetLocation);
  }

  public SecurityServiceRequest(
      String requestID,
      RequestType type,
      RequestStatus status,
      String issuer,
      String handler,
      String targetLocation) {
    super(requestID, type, status, issuer, handler, targetLocation);
  }
}
