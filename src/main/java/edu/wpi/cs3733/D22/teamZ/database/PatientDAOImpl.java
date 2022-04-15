package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Patient;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class PatientDAOImpl implements IPatientDAO {
  List<Patient> patients;
  private PatientControlCSV patCSV;

  static Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();
  // DatabaseConnection.getConnection();

  public PatientDAOImpl() {
    updateConnection();
    patients = new ArrayList<Patient>();
  }

  /**
   * Gets all of the patients in the database
   *
   * @return List of patients
   */
  public List<Patient> getAllPatients() {
    updateConnection();
    /*try {
      PreparedStatement pstmt = connection.prepareStatement("Select * From PATIENTS");
      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        String patientID = rset.getString("patientID");
        String name = rset.getString("name");
        Location location = new Location(rset.getString("location"));
        Patient pat = new Patient(patientID, name, location);
        if (!patients.contains(pat)) {
          patients.add(pat);
        }
      }
    } catch (SQLException e) {
      System.out.println("Failed to get all Patients");
    }*/
    return patients;
  }

  /**
   * Gets ONE patients from the database based on the provided patientID
   *
   * @param patientID
   * @return Patient object with provided patientID
   */
  public Patient getPatientByID(String patientID) {
    updateConnection();
    /*Patient pat = new Patient();
    try {
      PreparedStatement pstmt =
          connection.prepareStatement("Select * From PATIENTS WHERE PATIENTID = ?");
      pstmt.setString(1, patientID);
      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        String name = rset.getString("name");
        Location location = new Location(rset.getString("location"));
        pat.setPatientID(patientID);
        pat.setName(name);
        pat.setLocation(location);
      }
    } catch (SQLException e) {
      System.out.println("Unable to find patient");
    }*/
    for (Patient patient : patients) {
      if (patient.getPatientID().equals(patientID)) {
        return patient;
      }
    }
    return null;
  }

  /**
   * Adds a new patient to database. Will automatically check if already in database
   *
   * @param pat
   * @return True if successful, false if not
   */
  public boolean addPatient(Patient pat) {
    updateConnection();
    boolean val = false;
    if (addToDatabase(pat)) {
      val = true;
      patients.add(pat);
    }
    return val;
  }

  /**
   * Updates a patients in the database. Will automatically check if exists in database
   *
   * @param pat
   * @return True if successful, false if not
   */
  public boolean updatePatient(Patient pat) {
    updateConnection();
    try {
      PreparedStatement stmt =
          connection.prepareStatement("UPDATE PATIENTS SET NAME=?, LOCATION =? WHERE PATIENTID =?");
      stmt.setString(1, pat.getName());
      stmt.setString(2, pat.getLocation().getNodeID());
      stmt.setString(3, pat.getPatientID());

      stmt.executeUpdate();
      for (Patient patient : patients) {
        if (patient.equals(pat)) {
          patient.setName(pat.getName());
          patient.setLocation(pat.getLocation());
          return true;
        }
      }
    } catch (SQLException e) {
      System.out.println("Statement failed");
      return false;
    }
    // patients.remove(getPatientByID(pat.getPatientID()));
    // patients.add(pat);
    return true;
  }

  /**
   * Deletes a patient from database. Will automatically check if exists in database already
   *
   * @param pat
   * @return True if successful, false if not
   */
  public boolean deletePatient(Patient pat) {
    updateConnection();
    try {
      PreparedStatement stmt =
          connection.prepareStatement("DELETE FROM PATIENTS WHERE PATIENTID=?");
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
   * @param data file location of patient csv
   * @return True if successful, false if not
   */
  public boolean exportToPatientCSV(File data) {
    updateConnection();

    data = new File(System.getProperty("user.dir") + "\\patient.csv");
    patCSV = new PatientControlCSV(data);
    patCSV.writePatCSV(patients);

    return true;
  }

  /**
   * Imports Patients to database from a specified file location for the csv
   *
   * @param patientData file location for csv
   * @return number of conflicts when importing
   */
  @Override
  public int importPatientsFromCSV(File patientData) {
    updateConnection();
    patientData = new File(System.getProperty("user.dir") + "\\employee.csv");
    patCSV = new PatientControlCSV(patientData);
    int conflictCounter = 0;
    try {
      List<Patient> tempPatient = patCSV.readPatCSV();
      String temp = "";
      try {
        for (Patient info : tempPatient) {
          PreparedStatement pstmt =
              connection.prepareStatement(
                  "INSERT INTO PATIENTS (PATIENTID, NAME, LOCATION) " + "values (?, ?, ?)");
          temp = info.getPatientID();
          pstmt.setString(1, info.getPatientID());
          pstmt.setString(2, info.getName());
          pstmt.setString(3, info.getLocation().toString());

          // insert it
          pstmt.executeUpdate();
        }
      } catch (SQLException e) {
        conflictCounter++;
        System.out.println(
            "Found "
                + conflictCounter
                + " conflicts. "
                + temp
                + " might be in a room that does not exist.");
      }
    } catch (IOException e) {
      System.out.println("Failed to insert into patient table");
      e.printStackTrace();
    }
    return conflictCounter;
  }

  /** Updates the connection */
  private void updateConnection() {
    connection = EnumDatabaseConnection.CONNECTION.getConnection();
  }

  /**
   * Insert patients into database from given list
   *
   * @param list list of patients that need to be added
   * @return True if successful, false otherwise
   */
  public boolean addPatientFromList(List<Patient> list) {
    updateConnection();
    boolean val = true;
    for (Patient patient : list) {
      if (!addToDatabase(patient)) {
        val = false;
      }
    }
    return val;
  }

  /**
   * Contains SQL command for inserting data
   *
   * @param pat patient to be inserted
   * @return True if successful, false otherwise
   */
  private boolean addToDatabase(Patient pat) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "INSERT INTO PATIENTS (PATIENTID, NAME, LOCATION)" + "values (?, ?, ?)");
      stmt.setString(1, pat.getPatientID());
      stmt.setString(2, pat.getName());
      stmt.setObject(3, pat.getLocation());

      stmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("Statement failed");
      return false;
    }
    return true;
  }
}
