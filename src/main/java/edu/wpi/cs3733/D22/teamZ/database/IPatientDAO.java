package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Patient;
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
    boolean addPateint(Patient pat);

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
    boolean exportToPatientCSV();
}
