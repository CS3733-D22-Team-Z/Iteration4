package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Patient;
import java.io.File;
import java.util.List;

public interface IPatientDAO {
  /**
   * Gets all of the patients in the database
   *
   * @return List of patients
   */
  List<Patient> getAllPatients();

  /**
   * Gets ONE patient from the database based on the provided patientID
   *
   * @param patientID
   * @return Patient object with provided patientID
   */
  Patient getPatientByID(String patientID);

  /**
   * Adds a new Patient to database. Will automatically check if already in database
   *
   * @param pat
   * @return True if successful, false if not
   */
  boolean addPatient(Patient pat);

  /**
   * Updates a patient in the database. Will automatically check if exists in database
   *
   * @param pat
   * @return True if successful, false if not
   */
  boolean updatePatient(Patient pat);

  /**
   * Deletes a patient from database. Will automatically check if exists in database
   *
   * @param pat
   * @return True if successful, false if not
   */
  boolean deletePatient(Patient pat);

  /**
   * Exports the patient table into a csv file to the working directory
   *
   * @return True if successful, false if not
   */
  boolean exportToPatientCSV(File data);

  /**
   * Imports Patients to database from a specified file location for the csv
   *
   * @param patientData file location for csv
   * @return number of conflicts when importing
   */
  int importPatientsFromCSV(File patientData);
}
