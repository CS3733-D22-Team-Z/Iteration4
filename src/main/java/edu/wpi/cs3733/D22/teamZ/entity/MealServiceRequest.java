package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import java.time.LocalDateTime;

public class MealServiceRequest extends ServiceRequest {
  private Patient patient;
  private String drink;
  private String entree;
  private String snack;
  private String allergen;

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
      String snack,
      String allergen) {
    super(requestID, RequestType.MEALSERV, status, issuer, handler, targetLocation, opened, closed);
    this.patient = patient;
    this.drink = drink;
    this.entree = entree;
    this.snack = snack;
    this.allergen = allergen;
  }

  //  public MealServiceRequest(
  //      String requestID,
  //      ServiceRequest.RequestStatus status,
  //      Employee issuer,
  //      Employee handler,
  //      Location targetLocation,
  //      LocalDateTime opened,
  //      LocalDateTime closed,
  //      Patient patient,
  //      String drink,
  //      String entree,
  //      String snack,
  //      String meal,
  //      String allergen) {
  //    super(requestID, RequestType.MEALSERV, status, issuer, handler, targetLocation, opened,
  // closed);
  //    this.patient = patient;
  //    this.drink = drink;
  //    this.entree = entree;
  //    this.snack = snack;
  //    this.meal = drink+entree+snack;
  //    this.allergen = allergen;
  //  }

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
      String snack,
      String allergen) {
    super(requestID, RequestType.MEALSERV, status, issuer, handler, targetLocation, opened, closed);
    this.patient = facadeDAO.getPatientByID(patient);
    this.drink = drink;
    this.entree = entree;
    this.snack = snack;
    this.allergen = allergen;
  }

  // Getter Functions
  public Patient getPatient() {
    return this.patient;
  }

  public String getDrink() {
    return drink;
  }

  public String getEntree() {
    return entree;
  }

  public String getSnack() {
    return snack;
  }

  public String getAllergen() {
    return allergen;
  }

  // Setter Functions
  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public void setDrink(String drink) {
    this.drink = drink;
  }

  public void setEntree(String entree) {
    this.entree = entree;
  }

  public void setSnack(String side) {
    this.snack = snack;
  }

  public void setAllergen(String allergen) {
    this.allergen = allergen;
  }
}
