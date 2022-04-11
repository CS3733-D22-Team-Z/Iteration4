package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipmentDeliveryRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class MedEquipReqDAOImpl implements IMedEquipReqDAO {
  private List<MedicalEquipmentDeliveryRequest> list;
  private MedEqReqControlCSV reqCSV;

  static Connection connection = DatabaseConnection.getConnection();

  public MedEquipReqDAOImpl() {
    list = new ArrayList<>();
  }

  /**
   * Gets all MedicalEquipmentRequests from database
   *
   * @return list of MedicalEquipmentRequests
   */
  public List<MedicalEquipmentDeliveryRequest> getAllMedEquipReq() {
    try {
      PreparedStatement pstmt =
          connection.prepareStatement(
              "Select SERVICEREQUEST.REQUESTID, SERVICEREQUEST.STATUS, MEDEQUIPREQ.EQUIPMENTID, ISSUERID, HANDLERID, TARGETLOCATIONID\n"
                  + "FROM SERVICEREQUEST,MEDEQUIPREQ WHERE SERVICEREQUEST.REQUESTID = MEDEQUIPREQ.REQUESTID");
      ResultSet rset = pstmt.executeQuery();

      list.clear();

      while (rset.next()) {
        String requestID = rset.getString("REQUESTID");
        String status = rset.getString("STATUS");
        String equipmentID = rset.getString("EQUIPMENTID");
        String issuer = rset.getString("ISSUERID");
        String handler = rset.getString("HANDLERID");
        String targetLoc = rset.getString("TARGETLOCATIONID");

        // make new temp to put into list
        MedicalEquipmentDeliveryRequest temp =
            new MedicalEquipmentDeliveryRequest(
                requestID,
                ServiceRequest.RequestStatus.getRequestStatusByString(status),
                issuer,
                handler,
                equipmentID,
                targetLoc);

        // if not in the list already, add it
        if (!list.contains(temp)) {
          list.add(temp);
        }
      }
    } catch (SQLException e) {
      System.out.println("Unable to get all Medical Equipment Requests");
    }
    return list;
  }

  /**
   * Gets a MedicalEquipmentRequest of the given requestID
   *
   * @param id ID of request to be fetched
   * @return MedicalEquipmentRequest of given ID, null if not found
   */
  public MedicalEquipmentDeliveryRequest getMedEquipReqByID(String id) {
    try {
      // query to get information of request by ID
      PreparedStatement pstmt =
          connection.prepareStatement(
              "Select SERVICEREQUEST.REQUESTID, "
                  + "SERVICEREQUEST.STATUS, MEDEQUIPREQ.EQUIPMENTID, ISSUERID,"
                  + " HANDLERID, TARGETLOCATIONID FROM SERVICEREQUEST,MEDEQUIPREQ "
                  + "WHERE SERVICEREQUEST.REQUESTID = MEDEQUIPREQ.REQUESTID AND MEDEQUIPREQ.REQUESTID = ?");
      pstmt.setString(1, id);
      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        String requestID = rset.getString("REQUESTID");
        String status = rset.getString("STATUS");
        String equipmentID = rset.getString("EQUIPMENTID");
        String issuer = rset.getString("ISSUERID");
        String handler = rset.getString("HANDLERID");
        String targetLoc = rset.getString("TARGETLOCATIONID");

        // make new temp to put into list
        MedicalEquipmentDeliveryRequest temp =
            new MedicalEquipmentDeliveryRequest(
                requestID,
                ServiceRequest.RequestStatus.getRequestStatusByString(status),
                issuer,
                handler,
                equipmentID,
                targetLoc);
        return temp;
      }
    } catch (SQLException e) {
      System.out.println("Unable to find request with given ID");
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
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "INSERT INTO MEDEQUIPREQ (REQUESTID, EQUIPMENTID) values (?, ?)");
      stmt.setString(1, request.getRequestID());
      stmt.setString(2, request.getEquipmentID());

      stmt.executeUpdate();
      connection.commit();
      list.add(request);
      return true;
    } catch (SQLException e) {
      System.out.println("Statement failed");
      return false;
    }
  }

  /**
   * Updates an existing MedicalEquipmentRequest in the database with the given request
   *
   * @param request MedicalEquipmentRequest with updated information
   * @return True if successful, false otherwise
   */
  public boolean updateMedEquipReq(MedicalEquipmentDeliveryRequest request) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "UPDATE MEDEQUIPREQ SET status =?, handler =? WHERE RequestID =?");
      stmt.setString(1, request.getStatus().toString());
      stmt.setString(2, request.getHandler().toString());
      stmt.setString(3, request.getRequestID());

      stmt.executeUpdate();
      connection.commit();
      list.remove(request);
      list.add(request);
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
    try {
      PreparedStatement stmt =
          connection.prepareStatement("DELETE FROM MEDEQUIPREQ WHERE RequestID=?");
      stmt.setString(1, request.getRequestID());
      stmt.execute();
      connection.commit();
      list.remove(request);
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
    // reqData = new File(System.getProperty("user.dir") + "\\MedEquipReq.csv");
    reqCSV = new MedEqReqControlCSV(reqData);

    reqCSV.writeMedReqCSV(getAllMedEquipReq());
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
    reqCSV = new MedEqReqControlCSV(data);
    int conflictCounter = 0;
    String temp = "";
    try {
      List<MedicalEquipmentDeliveryRequest> tempMedicalEquipment = reqCSV.readMedReqCSV();

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
}
