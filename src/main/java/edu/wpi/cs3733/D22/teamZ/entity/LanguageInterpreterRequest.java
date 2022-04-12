package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;

public class LanguageInterpreterRequest extends ServiceRequest {
  private Patient patient;
  private String language;

  private FacadeDAO facadeDAO = FacadeDAO.getInstance();

  public LanguageInterpreterRequest(
      String requestID,
      RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation,
      Patient patient,
      String language) {
    super(requestID, RequestType.LANG, status, issuer, handler, targetLocation);
    this.patient = patient;
    this.language = language;
  }

  public LanguageInterpreterRequest(
      String requestID,
      RequestStatus status,
      String issuer,
      String handler,
      String targetLocation,
      String patient,
      String language) {
    super(requestID, RequestType.LANG, status, issuer, handler, targetLocation);
    this.patient = facadeDAO.getPatientByID(patient);
    this.language = language;
  }

  // GetterFunctions
  public Patient getPatient() {
    return patient;
  }

  public String getLanguage() {
    return language;
  }

  // SetterFunctions
  public void setPatientID(Patient patient) {
    this.patient = patient;
  }

  public void setLanguage(String language) {
    this.language = language;
  }
}
