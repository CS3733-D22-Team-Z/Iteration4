package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicalEquipmentDAOImpl implements IMedicalEquipmentDAO {

  Connection connection = DatabaseConnection.getConnection();
  List<MedicalEquipment> medicalEquipmentsList;

  /** Constructor for MedicalEquipmentDAOImpl */
  public MedicalEquipmentDAOImpl() {
    medicalEquipmentsList = new ArrayList<>();
  }

  /**
   * Gets all of the MedicalEquipment objects in the database
   *
   * @return list of all MedicalEquipment objects
   */
  @Override
  public List<MedicalEquipment> getAllMedicalEquipment() {
    try {
      PreparedStatement pstmt = connection.prepareStatement("Select * From MEDICALEQUIPMENT");
      ResultSet rset = pstmt.executeQuery();

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
   * Finds and gives the MedicalEquipment object from given itemID
   *
   * @param itemID
   * @return MedicalEquipment object with given itemID
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
   * Adds new MedicalEquipment object to the database
   *
   * @param equipment
   * @return True if successful, false if rejected
   */
  @Override
  public boolean addMedicalEquipment(MedicalEquipment equipment) {
    try {
      PreparedStatement pstmt =
          connection.prepareStatement(
              ""
                  + "INSERT INTO MEDICALEQUIPMENT (itemID, type, status, currentLocation)"
                  + "values (?, ?, ?, ?)");
      pstmt.setString(1, equipment.getItemID());
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
   * Updates the MedicalEquipment object within database with given MedicalEquipment object
   *
   * @param equipment
   * @return True if successful, false if not
   */
  @Override
  public boolean updateMedicalEquipment(MedicalEquipment equipment) {
    MedicalEquipment oldEquipment;
    try {
      oldEquipment = getMedicalEquipmentByID(equipment.getItemID());
      PreparedStatement pstmt =
          connection.prepareStatement(
              "" + "UPDATE MEDICALEQUIPMENT SET status = ?, currentLocation = ? WHERE itemID = ?");
      pstmt.setString(1, equipment.getStatus());
      pstmt.setString(2, equipment.getCurrentLocation().getNodeID());
      pstmt.setString(3, equipment.getItemID());

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
   * Deletes MedicalEquipment object from the database
   *
   * @param equipment
   * @return True if successful, false if not
   */
  @Override
  public boolean deleteMedicalEquipment(MedicalEquipment equipment) {
    try {
      PreparedStatement pstmt =
          connection.prepareStatement("" + "DELETE FROM MEDICALEQUIPMENT WHERE itemID = ?");
      pstmt.setString(1, equipment.getItemID());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Failed to delete from database");
      return false;
    }
    medicalEquipmentsList.remove(equipment);
    return true;
  }

  /**
   * Exports the MedicalEquipment database to a CSV
   *
   * @return True if successful, false if not
   */
  @Override
  public boolean exportToMedicalEquipmentCSV() {
    return false;
  }
}
