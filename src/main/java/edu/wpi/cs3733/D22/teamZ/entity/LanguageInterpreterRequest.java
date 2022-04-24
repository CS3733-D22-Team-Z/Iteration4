package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;

public class LanguageInterpreterRequest extends ServiceRequest {
  private String patientID;
  private String patientName;
  private String language;

  private FacadeDAO facadeDAO = FacadeDAO.getInstance();

  public LanguageInterpreterRequest(
      String requestID,
      RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation,
      String patientName,
      String patientID,
      String language) {
    super(requestID, RequestType.LANG, status, issuer, handler, targetLocation);
    this.patientName = patientName;
    this.patientID = patientID;
    this.language = language;
  }

  public LanguageInterpreterRequest(
      String requestID,
      RequestStatus status,
      String issuer,
      String handler,
      String targetLocation,
      String patientName,
      String patientID,
      String language) {
    super(requestID, RequestType.LANG, status, issuer, handler, targetLocation);
    this.patientName = patientName;
    this.patientID = patientID;
    this.language = language;
  }

  // GetterFunctions
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

  public String getLanguage() {
    return this.language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }
}
