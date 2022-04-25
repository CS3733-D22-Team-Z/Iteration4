package edu.wpi.cs3733.D22.teamZ.entity;

import java.time.LocalDateTime;

public class CleaningRequest extends ServiceRequest {
  private final String type;

  public CleaningRequest(
      String requestID,
      RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation,
      LocalDateTime opened,
      LocalDateTime closed,
      String type) {
    super(requestID, RequestType.CLEANING, status, issuer, handler, targetLocation, opened, closed);
    this.type = type;
  }

  public String getCleaningType() {
    return type;
  }
}
