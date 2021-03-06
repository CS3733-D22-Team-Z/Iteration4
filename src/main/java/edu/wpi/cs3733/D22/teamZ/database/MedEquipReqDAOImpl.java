package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipmentDeliveryRequest;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class MedEquipReqDAOImpl implements IMedEquipReqDAO {
  private final MedEqReqControlCSV reqCSV;
  private final List<MedicalEquipmentDeliveryRequest> medicalEquipmentRequests;

  static Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();
  // DatabaseConnection.getConnection();

  public MedEquipReqDAOImpl() {
    updateConnection();
    // medicalEquipmentRequests = new HashMap<>();
    medicalEquipmentRequests = new ArrayList<>();

    File reqData =
        new File(
            System.getProperty("user.dir")
                + System.getProperty("file.separator")
                + "MedEquipReq.csv");
    this.reqCSV = new MedEqReqControlCSV(reqData);
  }

  /**
   * Gets all MedicalEquipmentRequests from database
   *
   * @return list of MedicalEquipmentRequests
   */
  public List<MedicalEquipmentDeliveryRequest> getAllMedEquipReq() {
    // ArrayList<MedicalEquipmentDeliveryRequest> medRequestList = new
    // ArrayList<>(medicalEquipmentRequests.values());
    return medicalEquipmentRequests;
  }

  /**
   * Gets a MedicalEquipmentRequest of the given requestID
   *
   * @param id ID of request to be fetched
   * @return MedicalEquipmentRequest of given ID, null if not found
   */
  public MedicalEquipmentDeliveryRequest getMedEquipReqByID(String id) {
    for (MedicalEquipmentDeliveryRequest req : medicalEquipmentRequests) {
      if (req.getRequestID().equals(id)) {
        return req;
      }
    }
    return null;
  }

  /**
   * Adds a MedicalEquipmentRequest to the database
   *
   * @param request MedicalEquipmentRequest to be added
   * @return True if successful, false otherwise
   */
  public boolean addMedEquipReq(MedicalEquipmentDeliveryRequest request) {
    updateConnection();
    boolean val = false;
    if (addToDatabase(request)) {
      val = true;
      medicalEquipmentRequests.add(request);
      // medicalEquipmentRequests.put(request.getEquipmentID(), request);
    }
    return val;
  }

  /**
   * Updates an existing MedicalEquipmentRequest in the database with the given request
   *
   * @param request MedicalEquipmentRequest with updated information
   * @return True if successful, false otherwise
   */
  public boolean updateMedEquipReq(MedicalEquipmentDeliveryRequest request) {
    updateConnection();
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "UPDATE SERVICEREQUEST SET status =?, handlerID =?, closed =? WHERE RequestID =?");
      stmt.setString(1, request.getStatus().toString());
      stmt.setString(2, request.getHandler().getEmployeeID());
      if (request.getClosed() == null) {
        stmt.setString(3, null);
      } else {
        stmt.setString(3, request.getClosed().toString());
      }
      stmt.setString(4, request.getRequestID());

      stmt.executeUpdate();
      connection.commit();
      for (MedicalEquipmentDeliveryRequest req : medicalEquipmentRequests) {
        if (req.equals(request)) {
          req.setStatus(request.getStatus());
          req.setHandler(request.getHandler());
          req.setClosed(request.getClosed());
          return true;
        }
      }
      // medicalEquipmentRequests.remove(request);
      // medicalEquipmentRequests.add(request);
      // medicalEquipmentRequests.remove(request.getRequestID());
      // medicalEquipmentRequests.put(request.getRequestID(), request);
      return true;
    } catch (SQLException e) {
      System.out.println("Update failed");
      return false;
    }
  }

  /**
   * Deletes MedicalEquipmentRequest from database
   *
   * @param request MedicalEquipmentRequest to be deleted
   * @return True if successful, false otherwise
   */
  public boolean deleteMedEquipReq(MedicalEquipmentDeliveryRequest request) {
    updateConnection();
    try {
      PreparedStatement stmt =
          connection.prepareStatement("DELETE FROM MEDEQUIPREQ WHERE RequestID=?");
      stmt.setString(1, request.getRequestID());
      stmt.execute();
      connection.commit();
      medicalEquipmentRequests.remove(request);
      // medicalEquipmentRequests.remove(request.getRequestID());
      return true;
    } catch (SQLException e) {
      System.out.println("Failed to delete from MEDEQUIPREQ table");
      return false;
    }
  }

  /**
   * Exports the MedicalEquipmentRequest database to specified file location for csv
   *
   * @param reqData file location for csv
   * @return True if successful, false otherwise
   */
  @Override
  public boolean exportToMedEquipReqCSV(File reqData) {
    updateConnection();

    try {
      reqCSV.writeMedReqCSV(getAllMedEquipReq(), reqData);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * Imports MedicalEquipmentRequests into database from specified file location for csv
   *
   * @param data file location of csv
   * @return number of conflicts during import
   */
  @Override
  public int importMedEquipReqFromCSV(File data) {
    updateConnection();

    int conflictCounter = 0;
    String temp = "";
    try {
      List<MedicalEquipmentDeliveryRequest> tempMedicalEquipment = reqCSV.readMedReqCSV(data);

      try {
        for (MedicalEquipmentDeliveryRequest info : tempMedicalEquipment) {
          PreparedStatement pstmt =
              connection.prepareStatement(
                  "INSERT INTO MEDEQUIPREQ (REQUESTID, EQUIPMENTID) " + "values (?, ?)");
          temp = info.getRequestID();
          pstmt.setString(1, info.getRequestID());
          pstmt.setString(2, info.getEquipmentID());

          // insert it
          pstmt.executeUpdate();
          medicalEquipmentRequests.add(info);
          // medicalEquipmentRequests.put(info.getRequestID(), info);
        }
      } catch (SQLException e) {
        conflictCounter++;
        System.out.println(
            "Found "
                + conflictCounter
                + " conflicts. "
                + temp
                + " has equipment/request IDs that do not exist.");
      }
    } catch (IOException e) {
      System.out.println("Failed to populate Medical Equipment Request table");
    }
    return conflictCounter;
  }

  /**
   * Returns the default path that medical equipment delivery request csv files are printed to
   *
   * @return The default path that medical equipment delivery request csv files are printed to
   */
  File getDefaultMedEquipReqCSVPath() {
    return reqCSV.getDefaultPath();
  }

  /** Updates the connection */
  private void updateConnection() {
    connection = EnumDatabaseConnection.CONNECTION.getConnection();
  }

  /**
   * Adds MedicalEquipmentDeliveryRequest into database from list
   *
   * @param list list of medical equipment delivery requests to be added
   * @return True if successful, false otherwise
   */
  public boolean addMedicalEquipReqFromList(List<MedicalEquipmentDeliveryRequest> list) {
    updateConnection();
    boolean val = true;
    for (MedicalEquipmentDeliveryRequest request : list) {
      if (!addToDatabase(request)) {
        val = false;
      }
    }
    return val;
  }

  /**
   * Contains SQL command for inserting MedicalEquipmentDeliveryRequests into database
   *
   * @param request The medical equipment delivery request to be added
   * @return True if successful, false otherwise
   */
  private boolean addToDatabase(MedicalEquipmentDeliveryRequest request) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "INSERT INTO MEDEQUIPREQ (REQUESTID, EQUIPMENTID) values (?, ?)");
      stmt.setString(1, request.getRequestID());
      stmt.setString(2, request.getEquipmentID());

      stmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("Statement failed");
      return false;
    }
    return true;
  }
}
