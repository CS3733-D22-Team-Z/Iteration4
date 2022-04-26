package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import java.time.LocalDateTime;

public class MealServiceRequest extends ServiceRequest {
  private Patient patient;
  private String drink;
  private String entree;
  private String side;

  private FacadeDAO facadeDAO = FacadeDAO.getInstance();

  public MealServiceRequest(
      String requestID,
      ServiceRequest.RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation,
      LocalDateTime opened,
      LocalDateTime closed,
      Patient patient,
      String drink,
      String entree,
      String side) {
    super(requestID, RequestType.MEALSERV, status, issuer, handler, targetLocation, opened, closed);
    this.patient = patient;
    this.drink = drink;
    this.entree = entree;
    this.side = side;
  }

  public MealServiceRequest(
      String requestID,
      ServiceRequest.RequestStatus status,
      String issuer,
      String handler,
      String targetLocation,
      String opened,
      String closed,
      String patient,
      String drink,
      String entree,
      String side) {
    super(requestID, RequestType.MEALSERV, status, issuer, handler, targetLocation, opened, closed);
    this.patient = facadeDAO.getPatientByID(patient);
    this.drink = drink;
    this.entree = entree;
    this.side = side;
  }

  // GetterFunctions
  public Patient getPatient() {
    return this.patient;
  }

  public String getDrink() {
    return drink;
  }

  public String getEntree() {
    return entree;
  }

  public String getSide() {
    return side;
  }

  // SetterFunction
  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public void setDrink(String drink) {
    this.drink = drink;
  }

  public void setEntree(String entree) {
    this.entree = entree;
  }

  public void setSide(String side) {
    this.side = side;
  }
}
