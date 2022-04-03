package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Patient;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientDAOImpl implements IPatientDAO{
    List<Patient> patients;
    private PatientControlCSV patCSV;

    static Connection connection = DatabaseConnection.getConnection();

    public PatientDAOImpl() {
        patients = new ArrayList<Patient>();
    }

    /**
     * Gets all of the patients in the database
     *
     * @return List of patients
     */
    public List<Patient> getAllPatients() {
        try {
            PreparedStatement pstmt = connection.prepareStatement("Select * From PATIENTS");
            ResultSet rset = pstmt.executeQuery();

            while (rset.next()) {
                String patientID = rset.getString("patientID");
                String name = rset.getString("name");
                String location = rset.getString("location");
                Patient pat = new Patient(patientID, name, location);
                if (!patients.contains(pat)) {
                    patients.add(pat);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to get all Patients");
        }
        return patients;
    }

    /**
     * Gets ONE patients from the database based on the provided patientID
     *
     * @param patientID
     * @return Patient object with provided patientID
     */
    public Patient getPatientByID(String patientID) {
        Patient pat = new Patient();
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("Select * From PATIENTS WHERE PATIENTID = ?");
            pstmt.setString(1, patientID);
            ResultSet rset = pstmt.executeQuery();

            while (rset.next()) {
                String name = rset.getString("name");
                String location = rset.getString("location");
                pat.setPatientID(patientID);
                pat.setName(name);
                pat.setLocation(location);
            }
        } catch (SQLException e) {
            System.out.println("Unable to find patient");
        }
        return pat;
    }

    /**
     * Adds a new patient to database. Will automatically check if already in database
     *
     * @param pat
     * @return True if successful, false if not
     */
    public boolean addPatient(Patient pat) {
        try {
            PreparedStatement stmt =
                    connection.prepareStatement(
                            "INSERT INTO PATIENT (PATIENTID, NAME, LOCATION)"
                                    + "values (?, ?, ?)");
            stmt.setString(1, pat.getPatientID());
            stmt.setString(2, pat.getName());
            stmt.setObject(3, pat.getLocation());

            stmt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Statement failed");
            return false;
        }
        patients.add(pat);
        return true;
    }

    /**
     * Updates a patients in the database. Will automatically check if exists in database
     *
     * @param pat
     * @return True if successful, false if not
     */
    public boolean updatePatient(Patient pat) {
        try {
            PreparedStatement stmt =
                    connection.prepareStatement(
                            "UPDATE PATIENT SET NAME=?, LOCATION =? WHERE PATIENTID =?");
            stmt.setString(1, pat.getName());
            stmt.setString(2, pat.getLocation());
            stmt.setString(3, pat.getPatientID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Statement failed");
            return false;
        }
        patients.remove(getPatientByID(pat.getPatientID()));
        patients.add(pat);
        return true;
    }

    /**
     * Deletes a patient from database. Will automatically check if exists in database already
     *
     * @param pat
     * @return True if successful, false if not
     */
    public boolean deletePatient(Patient pat) {
        try {
            PreparedStatement stmt =
                    connection.prepareStatement("DELETE FROM PATIENT WHERE PATIENTID=?");
            stmt.setString(1, pat.getPatientID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Statement failed");
            return false;
        }
        patients.remove(pat);
        return true;
    }

    /**
     * Exports the patient table into a csv file to the working directory
     *
     * @return True if successful, false if not
     */
    public boolean exportToPatientCSV() {

        File patData = new File(System.getProperty("user.dir") + "\\patient.csv");
        patCSV = new PatientControlCSV(patData);
        patCSV.writePatCSV(getAllPatients());

        return true;
    }
}
