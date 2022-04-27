package edu.wpi.cs3733.D22.teamZ.entity;

import lombok.Getter;
import lombok.Setter;

public class InternalTransportRequest extends ServiceRequest {
  @Getter @Setter private String information;

  public InternalTransportRequest(
      String requestID,
      RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation,
      String information) {
    super(requestID, RequestType.INTERNAL, status, issuer, handler, targetLocation, null, null);
    this.information = information;
  }
}
