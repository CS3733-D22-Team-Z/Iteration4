package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class MedicalEquipmentDAOImpl implements IMedicalEquipmentDAO {

  Connection connection = DatabaseConnection.getConnection();
  List<MedicalEquipment> medicalEquipmentsList;

  MedicalEquipmentControlCSV medicalEquipmentControlCSV;

  /** Constructor for MedicalEquipmentDAOImpl */
  public MedicalEquipmentDAOImpl() {
    medicalEquipmentsList = new ArrayList<>();
  }

  /**
   * Get all MedicalEquipment in the database
   *
   * @return list of MedicalEquipment
   */
  @Override
  public List<MedicalEquipment> getAllMedicalEquipment() {
    try {
      PreparedStatement pstmt = connection.prepareStatement("Select * From MEDICALEQUIPMENT");
      ResultSet rset = pstmt.executeQuery();

      medicalEquipmentsList.clear();

      while (rset.next()) {
        String itemID = rset.getString("itemID");
        String type = rset.getString("type");
        String status = rset.getString("status");
        String locationNodeID = rset.getString("currentLocation");
        LocationDAOImpl tempDAO = new LocationDAOImpl();
        Location tempLocation = tempDAO.getLocationByID(locationNodeID);
        MedicalEquipment medicalEquipment =
            new MedicalEquipment(itemID, type, status, tempLocation);
        if (!medicalEquipmentsList.contains(medicalEquipment)) {
          medicalEquipmentsList.add(medicalEquipment);
        }
      }
    } catch (SQLException e) {
      System.out.println("Failed to get all Medical Equipment");
    }
    return medicalEquipmentsList;
  }

  /**
   * Get MedicalEquipment with the given ID
   *
   * @param itemID ID of MedicalEquipment to be fetched
   * @return MedicalEquipment with the given ID
   */
  @Override
  public MedicalEquipment getMedicalEquipmentByID(String itemID) {
    MedicalEquipment medicalEquipment = new MedicalEquipment(itemID);
    try {
      PreparedStatement pstmt =
          connection.prepareStatement("Select * From MEDICALEQUIPMENT WHERE itemID = ?");
      pstmt.setString(1, itemID);
      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        String type = rset.getString("type");
        String status = rset.getString("status");
        String locationNodeID = rset.getString("currentLocation");
        LocationDAOImpl tempDAO = new LocationDAOImpl();
        Location tempLocation = tempDAO.getLocationByID(locationNodeID);
        medicalEquipment.setStatus(status);
        medicalEquipment.setType(type);
        medicalEquipment.setCurrentLocation(tempLocation);
      }
    } catch (SQLException e) {
      System.out.println("Failed to get the Medical Equipment");
    }
    return medicalEquipment;
  }

  /**
   * Get the first avalable equipment with the given equipment type
   *
   * @param equipment type of equipment
   * @return equipmentID of the first available equipment of the given type
   */
  @Override
  public String getFirstAvailableEquipmentByType(String equipment) {

    ILocationDAO locationDAO = new LocationDAOImpl();

    try {
      PreparedStatement pstmt =
          connection.prepareStatement(
              "Select * From MEDICALEQUIPMENT WHERE TYPE = ? AND STATUS = 'Available'");
      pstmt.setString(1, equipment);
      ResultSet rset = pstmt.executeQuery();

      rset.next();
      String temp = rset.getString("ITEMID");
      rset.close();
      if (temp != null) {
        pstmt =
            connection.prepareStatement(
                "UPDATE MEDICALEQUIPMENT SET STATUS = 'In-Use' WHERE ITEMID = ?");
        pstmt.setString(1, temp);
        pstmt.executeUpdate();
        return temp;
      }

    } catch (SQLException e) {
      System.out.println("Failed to get the Medical Equipment");
    }
    // Only returns if no equipments of type are available
    return null;
  }

  /**
   * Gets all MedicalEquipment in a given location
   *
   * @param location Location to extract MedicalEquipment inside
   * @return list of MedicalEquipment in the given location
   */
  @Override
  public List<MedicalEquipment> getAllMedicalEquipmentByLocation(Location location) {
    List<MedicalEquipment> medicalEquipmentLocationList = new ArrayList<>();
    try {
      PreparedStatement pstnt =
          connection.prepareStatement("select * from MEDICALEQUIPMENT where currentLocation = ?");
      pstnt.setString(1, location.getNodeID());
      ResultSet rset = pstnt.executeQuery();
      while (rset.next()) {
        String tempItemID = rset.getString("ITEMID");
        String tempType = rset.getString("TYPE");
        String tempStatus = rset.getString("STATUS");
        String tempCurrentLocation = rset.getString("CURRENTLOCATION");
        LocationDAOImpl tempDAO = new LocationDAOImpl();
        Location tempLocation = tempDAO.getLocationByID(tempCurrentLocation);
        MedicalEquipment tempMedicalEquipment =
            new MedicalEquipment(tempItemID, tempType, tempStatus, tempLocation);
        medicalEquipmentLocationList.add(tempMedicalEquipment);
      }
    } catch (SQLException e) {
      System.out.println("failed to get medical equipment by location");
      for (int i = 0; i < medicalEquipmentLocationList.size(); i++) {
        medicalEquipmentLocationList.remove(i);
      }
    }
    return medicalEquipmentLocationList;
  }

  /**
   * Adds MedicalEquipment to the database
   *
   * @param equipment MedicalEquipment to be added
   * @return True if successful, false otherwise
   */
  @Override
  public boolean addMedicalEquipment(MedicalEquipment equipment) {
    try {
      PreparedStatement pstmt =
          connection.prepareStatement(
              ""
                  + "INSERT INTO MEDICALEQUIPMENT (itemID, type, status, currentLocation)"
                  + "values (?, ?, ?, ?)");
      pstmt.setString(1, equipment.getEquipmentID());
      pstmt.setString(2, equipment.getType());
      pstmt.setString(3, equipment.getStatus());
      pstmt.setString(4, equipment.getCurrentLocation().getNodeID());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Failed to insert new MedicalEquipment");
      return false;
    }
    medicalEquipmentsList.add(equipment);
    return true;
  }

  /**
   * Updates existing MedicalEquipment in the database with an updated MedicalEquipment
   *
   * @param equipment Updated MedicalEquipment
   * @return True if successful, false otherwise
   */
  @Override
  public boolean updateMedicalEquipment(MedicalEquipment equipment) {
    MedicalEquipment oldEquipment;
    try {
      oldEquipment = getMedicalEquipmentByID(equipment.getEquipmentID());
      PreparedStatement pstmt =
          connection.prepareStatement(
              "" + "UPDATE MEDICALEQUIPMENT SET status = ?, currentLocation = ? WHERE itemID = ?");
      pstmt.setString(1, equipment.getStatus());
      pstmt.setString(2, equipment.getCurrentLocation().getNodeID());
      pstmt.setString(3, equipment.getEquipmentID());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Failed to update MedicalEquipment");
      return false;
    }
    medicalEquipmentsList.remove(oldEquipment);
    medicalEquipmentsList.add(equipment);
    return true;
  }

  /**
   * Deletes MedicalEquipment in the database
   *
   * @param equipment MedicalEquipment to be deleted
   * @return True if successful, false otherwise
   */
  @Override
  public boolean deleteMedicalEquipment(MedicalEquipment equipment) {
    try {
      PreparedStatement pstmt =
          connection.prepareStatement("" + "DELETE FROM MEDICALEQUIPMENT WHERE itemID = ?");
      pstmt.setString(1, equipment.getEquipmentID());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Failed to delete from database");
      return false;
    }
    medicalEquipmentsList.remove(equipment);
    return true;
  }

  /**
   * Exports the MedicalEquipment in the database to the specified file location of csv
   *
   * @param equipmentData File location of csv
   * @return True if successful, false otherwise
   */
  @Override
  public boolean exportToMedicalEquipmentCSV(File equipmentData) {
    medicalEquipmentControlCSV = new MedicalEquipmentControlCSV(equipmentData);
    medicalEquipmentControlCSV.writeMedicalEquipmentCSV(medicalEquipmentsList);
    return true;
  }

  /**
   * Imports the MedicalEquipment into the database from specified file location for csv
   *
   * @param equipmentData file location for csv
   * @return number of conflicts when importing
   */
  @Override
  public int importMedicalEquipmentFromCSV(File equipmentData) {
    medicalEquipmentControlCSV = new MedicalEquipmentControlCSV(equipmentData);
    int conflictCounter = 0;
    String temp = "";
    try {
      List<MedicalEquipment> tempMedicalEquipment =
          medicalEquipmentControlCSV.readMedicalEquipmentCSV();

      try {
        for (MedicalEquipment info : tempMedicalEquipment) {
          PreparedStatement pstmt =
              connection.prepareStatement(
                  "INSERT INTO MEDICALEQUIPMENT (ITEMID, TYPE, STATUS, CURRENTLOCATION) "
                      + "values (?, ?, ?, ?)");
          temp = info.getEquipmentID();
          pstmt.setString(1, info.getEquipmentID());
          pstmt.setString(2, info.getType());
          pstmt.setString(3, info.getStatus());
          pstmt.setString(4, info.getCurrentLocation().getNodeID());

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
                + " is in location that does not exist.");
      }
    } catch (IOException e) {
      System.out.println("Failed to populate MedicalEquipment table");
    }
    return conflictCounter;
  }
}
