package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.MealServiceRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class MealServiceRequestDAOImpl implements IMealServiceRequestDAO {

  Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();
  // DatabaseConnection.getConnection();
  List<MealServiceRequest> mealServiceRequestsList;

  MealServReqControlCSV mealServiceRequestCSV;

  /** Constructor for MealServiceRequestDAOImpl */
  public MealServiceRequestDAOImpl() {
    updateConnection();
    mealServiceRequestsList = new ArrayList<>();
  }

  /**
   * Get all MealServiceRequests in the database
   *
   * @return list of MealServiceRequests
   */
  @Override
  public List<MealServiceRequest> getAllMealServiceReq() {
    updateConnection();
    /*try {
      PreparedStatement pstmt = connection.prepareStatement("Select * From MEDICALEQUIPMENT");
      ResultSet rset = pstmt.executeQuery();

      medicalEquipmentsList.clear();

      while (rset.next()) {
        String itemID = rset.getString("equipmentID");
        String type = rset.getString("type");
        String status = rset.getString("status");
        String locationNodeID = rset.getString("currentLocation");
        MedicalEquipment medicalEquipment =
            new MedicalEquipment(
                itemID, type, status, FacadeDAO.getInstance().getLocationByID(locationNodeID));
        if (!medicalEquipmentsList.contains(medicalEquipment)) {
          medicalEquipmentsList.add(medicalEquipment);
        }
      }
    } catch (SQLException e) {
      System.out.println("Failed to get all Medical Equipment");
    }*/
    return mealServiceRequestsList;
  }

  /**
   * Get MealServiceRequest with the given ID
   *
   * @param itemID ID of MedicalEquipment to be fetched
   * @return MealServiceRequest with the given ID
   */
  @Override
  public MealServiceRequest getMealServReqByID(String itemID) {
    updateConnection();
    /*MedicalEquipment medicalEquipment = new MedicalEquipment(itemID);
    try {
      PreparedStatement pstmt =
          connection.prepareStatement("Select * From MEDICALEQUIPMENT WHERE EQUIPMENTID = ?");
      pstmt.setString(1, itemID);
      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        String type = rset.getString("type");
        String status = rset.getString("status");
        String locationNodeID = rset.getString("currentLocation");
        medicalEquipment.setStatus(status);
        medicalEquipment.setType(type);
        medicalEquipment.setCurrentLocation(
            FacadeDAO.getInstance().getLocationByID(locationNodeID));
      }
    } catch (SQLException e) {
      System.out.println("Failed to get the Medical Equipment");
    }*/
    for (MealServiceRequest mealServiceReq : mealServiceRequestsList) {
      if (mealServiceReq.getRequestID().equals(itemID)) {
        return mealServiceReq;
      }
    }
    return null;
  }

  /**
   * Get the first available equipment with the given equipment type
   *
   * @param equipment type of equipment
   * @return equipmentID of the first available equipment of the given type
   */
  //  @Override
  //  public String getFirstAvailableEquipmentByType(String equipment) {
  //    updateConnection();
  //
  //    try {
  //      PreparedStatement pstmt =
  //          connection.prepareStatement(
  //              "Select * From MEDICALEQUIPMENT WHERE TYPE = ? AND STATUS = 'Available'");
  //      pstmt.setString(1, equipment);
  //      ResultSet rset = pstmt.executeQuery();
  //
  //      rset.next();
  //      String temp = rset.getString("EQUIPMENTID");
  //      rset.close();
  //      if (temp != null) {
  //        pstmt =
  //            connection.prepareStatement(
  //                "UPDATE MEDICALEQUIPMENT SET STATUS = 'In-Use' WHERE EQUIPMENTID = ?");
  //        pstmt.setString(1, temp);
  //        pstmt.executeUpdate();
  //        return temp;
  //      }
  //
  //    } catch (SQLException e) {
  //      System.out.println("Failed to get the Medical Equipment");
  //    }
  //    // Only returns if no equipments of type are available
  //    return null;
  //  }
  //
  //  /**
  //   * Gets all MedicalEquipment in a given location
  //   *
  //   * @param location Location to extract MedicalEquipment inside
  //   * @return list of MedicalEquipment in the given location
  //   */
  //  @Override
  //  public List<MedicalEquipment> getAllMedicalEquipmentByLocation(Location location) {
  //    updateConnection();
  //    List<MedicalEquipment> medicalEquipmentLocationList = new ArrayList<>();
  //    try {
  //      PreparedStatement pstnt =
  //          connection.prepareStatement("select * from MEDICALEQUIPMENT where currentLocation =
  // ?");
  //      pstnt.setString(1, location.getNodeID());
  //      ResultSet rset = pstnt.executeQuery();
  //      while (rset.next()) {
  //        String tempItemID = rset.getString("EQUIPMENTID");
  //        for (MedicalEquipment medicalEquipment : medicalEquipmentsList) {
  //          if (medicalEquipment.getEquipmentID().equals(tempItemID)) {
  //            medicalEquipmentLocationList.add(medicalEquipment);
  //          }
  //        }
  //      }
  //    } catch (SQLException e) {
  //      System.out.println("failed to get medical equipment by location");
  //      medicalEquipmentLocationList.clear();
  //    }
  //    return medicalEquipmentLocationList;
  //  }

  /**
   * Adds MealServiceRequest to the database
   *
   * @param request MealServiceRequest to be added
   * @return True if successful, false otherwise
   */
  @Override
  public boolean addMealServReq(MealServiceRequest request) {
    updateConnection();
    boolean val = false;
    if (addToDatabase(request)) {
      val = true;
      mealServiceRequestsList.add(request);
    }
    return val;
  }

  /**
   * Updates existing MealServiceRequest in the database with an updated entity object
   *
   * @param request Updated MealServiceRequest
   * @return True if successful, false otherwise
   */
  @Override
  public boolean updateMealServReq(MealServiceRequest request) {
    updateConnection();
    try {
      PreparedStatement pstmt =
          connection.prepareStatement(
              "" + "UPDATE MEALSERVICEREQUEST SET PATIENTID = ? WHERE REQUESTID = ?");
      pstmt.setString(1, request.getPatient().getPatientID());
      pstmt.setString(2, request.getRequestID());
      pstmt.executeUpdate();
      for (MealServiceRequest mealServiceRequest : mealServiceRequestsList) {
        if (mealServiceRequest.equals(request)) {
          mealServiceRequest.setPatient(request.getPatient());
          // mealServiceRequest.setCurrentLocation(equipment.getCurrentLocation());
          return true;
        }
      }
      return true;
    } catch (SQLException e) {
      System.out.println("Failed to update Meal Service Request");
      return false;
    }
  }

  /**
   * Deletes MealServiceRequest in the database
   *
   * @param request MealServiceRequest to be deleted
   * @return True if successful, false otherwise
   */
  @Override
  public boolean deleteMealServReq(MealServiceRequest request) {
    updateConnection();
    try {
      PreparedStatement pstmt =
          connection.prepareStatement("" + "DELETE FROM MEALSERVICEREQUEST WHERE REQUESTID = ?");
      pstmt.setString(1, request.getRequestID());

      pstmt.executeUpdate();
      mealServiceRequestsList.remove(request);
    } catch (SQLException e) {
      System.out.println("Failed to delete from database");
      return false;
    }
    return true;
  }

  /**
   * Exports the MealServiceRequest in the database to the specified file location of csv
   *
   * @param requestData File location of csv
   * @return True if successful, false otherwise
   */
  @Override
  public boolean exportToMealServReqCSV(File requestData) {
    updateConnection();
    mealServiceRequestCSV = new MealServReqControlCSV(requestData);
    try {
      mealServiceRequestCSV.writeMealReqCSV(mealServiceRequestsList);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * Imports the Meal Service Request into the database from specified file location for csv
   *
   * @param requestData file location for csv
   * @return number of conflicts when importing
   */
  @Override
  public int importMealServReqFromCSV(File requestData) {
    updateConnection();
    mealServiceRequestCSV = new MealServReqControlCSV(requestData);
    int conflictCounter = 0;
    String temp = "";
    try {
      List<MealServiceRequest> tempRequests = mealServiceRequestCSV.readMealServReqCSV();

      try {
        for (MealServiceRequest info : tempRequests) {
          PreparedStatement pstmt =
              connection.prepareStatement(
                  "INSERT INTO MEALSERVICEREQUEST (REQUESTID, PATIENTID) " + "values (?, ?)");
          temp = info.getRequestID();
          pstmt.setString(1, info.getRequestID());
          pstmt.setString(2, info.getPatient().getPatientID());

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
      System.out.println("Failed to populate MealServiceRequest table");
    }
    return conflictCounter;
  }

  /** Updates the connection */
  private void updateConnection() {
    connection = EnumDatabaseConnection.CONNECTION.getConnection();
  }

  /**
   * Insert Meal Service Request into database from list
   *
   * @param list list of meal service requests to be added
   * @return True if successful, false otherwise
   */
  public boolean addMealServReqFromList(List<MealServiceRequest> list) {
    updateConnection();
    boolean val = true;
    for (MealServiceRequest info : list) {
      if (!addToDatabase(info)) {
        val = false;
      }
    }
    return val;
  }

  /**
   * Contains SQL command to insert MealServiceRequest to database
   *
   * @param request Meal Service Request to be added
   * @return True if successful, false otherwise
   */
  private boolean addToDatabase(MealServiceRequest request) {
    try {
      PreparedStatement pstmt =
          connection.prepareStatement(
              ""
                  + "INSERT INTO MEALSERVICEREQUEST (REQUESTID, PATIENTID, DRINK, ENTREE, SIDE)"
                  + "values (?, ?, ?, ?, ?)");
      pstmt.setString(1, request.getRequestID());
      pstmt.setString(2, request.getPatient().getPatientID());
      pstmt.setString(3, request.getDrink());
      pstmt.setString(4, request.getEntree());
      pstmt.setString(5, request.getSide());

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Failed to insert new Meal Service Request");
      e.printStackTrace();
      return false;
    }
    return true;
  }

  //  /**
  //   * Get all Meal Service Requests in given floor
  //   *
  //   * @param floor floor to be searched
  //   * @return list of meal requests for given floor
  //   */
  //  public List<MealServiceRequest> getAllMealServiceRequestsByFloor(String floor) {
  //    updateConnection();
  //    List<MealServiceRequest> tempMealRequestList = new ArrayList<>();
  //    try {
  //      PreparedStatement pstmt =
  //          connection.prepareStatement(
  //              "SELECT * FROM MEDICALEQUIPMENT, LOCATION WHERE CURRENTLOCATION = NODEID AND FLOOR
  // = ?");
  //      pstmt.setString(1, floor);
  //      ResultSet rset = pstmt.executeQuery();
  //      while (rset.next()) {
  //        String id = rset.getString("EQUIPMENTID");
  //        String type = rset.getString("TYPE");
  //        String status = rset.getString("STATUS");
  //        String currentLocation = rset.getString("CURRENTLOCATION");
  //        MedicalEquipment tempMedEquip =
  //            new MedicalEquipment(
  //                id, type, status, FacadeDAO.getInstance().getLocationByID(currentLocation));
  //        tempMedEquipList.add(tempMedEquip);
  //      }
  //    } catch (SQLException e) {
  //      System.out.println("Failed medical equipment by floor");
  //      e.printStackTrace();
  //    }
  //    return tempMedEquipList;
  //  }
}