package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;

public class MealServiceRequest extends ServiceRequest {
  private Patient patient;
  private String mealServiceOption;

  private FacadeDAO facadeDAO = FacadeDAO.getInstance();

  public MealServiceRequest(
      String requestID,
      ServiceRequest.RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation,
      Patient patient,
      String mealServiceOption) {
    super(requestID, ServiceRequest.RequestType.MEAL, status, issuer, handler, targetLocation);
    this.patient = patient;
    this.mealServiceOption = mealServiceOption;
  }

  public MealServiceRequest(
      String requestID,
      ServiceRequest.RequestStatus status,
      String issuer,
      String handler,
      String targetLocation,
      String patient,
      String mealServiceOption) {
    super(requestID, ServiceRequest.RequestType.MEAL, status, issuer, handler, targetLocation);
    this.patient = facadeDAO.getPatientByID(patient);
    this.mealServiceOption = mealServiceOption;
  }

  // GetterFunctions
  public Patient getPatient() {
    return this.patient;
  }

  public String getMealServiceOption() {
    return this.mealServiceOption;
  }

  // SetterFunction
  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public void setMealServiceOption(String mealServiceOption) {
    this.mealServiceOption = mealServiceOption;
  }
}
