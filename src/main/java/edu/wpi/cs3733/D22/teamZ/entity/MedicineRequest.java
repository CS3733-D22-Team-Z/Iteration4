package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import java.time.LocalDateTime;

public class MedicineRequest extends ServiceRequest {
  private Patient patient;
  private String medicine;

  private FacadeDAO facadeDAO = FacadeDAO.getInstance();

  public MedicineRequest(
      String requestID,
      RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation,
      LocalDateTime opened,
      LocalDateTime closed,
      Patient patient,
      String medicine) {
    super(requestID, RequestType.MEDIC, status, issuer, handler, targetLocation, opened, closed);
    this.patient = patient;
    this.medicine = medicine;
  }

  public MedicineRequest(
      String requestID,
      RequestStatus status,
      String issuer,
      String handler,
      String targetLocation,
      String opened,
      String closed,
      String patient,
      String medicine) {
    super(requestID, RequestType.MEDIC, status, issuer, handler, targetLocation, opened, closed);
    this.patient = facadeDAO.getPatientByID(patient);
    this.medicine = medicine;
  }

  // GetterFunctions
  public Patient getPatient() {
    return this.patient;
  }

  public String getMedicine() {
    return this.medicine;
  }

  // SetterFunctions
  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public void setMedicine(String medicine) {
    this.medicine = medicine;
  }
}
