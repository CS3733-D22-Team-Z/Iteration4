package edu.wpi.cs3733.D22.teamZ.entity;

public class CleaningRequest extends ServiceRequest {
  private final String type;

  public CleaningRequest(
      String requestID,
      RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation,
      String type) {
    super(requestID, RequestType.CLEANING, status, issuer, handler, targetLocation);
    this.type = type;
  }

  public String getCleaningType() {
    return type;
  }
}
