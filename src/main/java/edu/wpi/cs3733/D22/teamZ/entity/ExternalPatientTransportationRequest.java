package edu.wpi.cs3733.D22.teamZ.entity;

import java.time.LocalDate;

public class ExternalPatientTransportationRequest extends ServiceRequest {
  private String patientName;
  private String patientID;
  private String destination;
  private LocalDate departureDate;

  public ExternalPatientTransportationRequest(
      String requestID,
      RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation,
      String patientID,
      String patientName,
      String destination,
      LocalDate departureDate) {
    super(requestID, RequestType.EXTERNAL, status, issuer, handler, targetLocation);
    this.patientID = patientID;
    this.patientName = patientName;
    this.destination = destination;
    this.departureDate = departureDate;
  }

  public ExternalPatientTransportationRequest(
      String requestID,
      RequestStatus status,
      String issuer,
      String handler,
      String targetLocation,
      String patientID,
      String patientName,
      String destination,
      LocalDate departureDate) {
    super(requestID, RequestType.EXTERNAL, status, issuer, handler, targetLocation);
    this.patientID = patientID;
    this.patientName = patientName;
    this.destination = destination;
    this.departureDate = departureDate;
  }

  public LocalDate getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(LocalDate departureDate) {
    this.departureDate = departureDate;
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

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }
}
