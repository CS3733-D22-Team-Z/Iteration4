package edu.wpi.cs3733.D22.teamZ.entity;

import java.time.LocalDateTime;
import lombok.Getter;

public class SecurityServiceRequest extends ServiceRequest {
  @Getter private String urgency;
  @Getter private String reason;

  public SecurityServiceRequest(
      String requestID,
      RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation,
      LocalDateTime opened,
      LocalDateTime closed,
      String urgency,
      String reason) {
    super(requestID, RequestType.SECURITY, status, issuer, handler, targetLocation, opened, closed);
    this.urgency = urgency;
    this.reason = reason;
  }

  public SecurityServiceRequest(
      String requestID,
      RequestType type,
      RequestStatus status,
      String issuer,
      String handler,
      String targetLocation,
      String opened,
      String closed,
      String urgency,
      String reason) {
    super(requestID, type, status, issuer, handler, targetLocation, opened, closed);
    this.urgency = urgency;
    this.reason = reason;
  }
}
