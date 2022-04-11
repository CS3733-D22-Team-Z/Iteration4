package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;

public class LaundryServiceRequest extends ServiceRequest {
  private Patient patient;
  private String linenType;

  private FacadeDAO facadeDAO = new FacadeDAO();

  public LaundryServiceRequest(
          String requestID,
          ServiceRequest.RequestStatus status,
          Employee issuer,
          Employee handler,
          Location targetLocation,
          Patient patient,
          String linenType) {
    super(requestID, ServiceRequest.RequestType.LAUNDRY, status, issuer, handler, targetLocation);
    this.linenType = linenType;
    this.patient = patient;
  }

  public LaundryServiceRequest(
          String requestID,
          ServiceRequest.RequestStatus status,
          String issuer,
          String handler,
          String targetLocation,
          String patient,
          String linenType) {
    super(requestID, ServiceRequest.RequestType.LAUNDRY, status, issuer, handler, targetLocation);
    this.linenType = linenType;
    this.patient = facadeDAO.getPatientByID(patient);
  }

  // GetterFunctions
  public Patient getPatient() {
    return this.patient;
  }

  public String getLinenType() {
    return this.linenType;
  }

  // SetterFunctions
  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public void setLinenType(String linenType) {
    this.linenType = linenType;
  }
}