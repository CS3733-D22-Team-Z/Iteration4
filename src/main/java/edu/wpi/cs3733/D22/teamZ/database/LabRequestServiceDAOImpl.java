package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.LabServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipmentDeliveryRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LabRequestServiceDAOImpl implements ILabRequestServiceDAO {

  static Connection connection = DatabaseConnection.getConnection();
  List<LabServiceRequest> returnList = new ArrayList<>();
  private LabRequestControlCSV reqCSV;

  /**
   * Gets all lab service requests
   *
   * @return list of lab service requests
   * @throws SQLException
   */
  @Override
  public List<LabServiceRequest> getAllLabServiceRequests() {
    LabServiceRequest temp;
    returnList.clear();

    try {
      PreparedStatement pstmt =
          connection.prepareStatement(
              "Select LABREQUEST.REQUESTID, SERVICEREQUEST.STATUS, ISSUERID, HANDLERID, TARGETLOCATIONID, LABRESULT.TYPE\n"
                  + "FROM SERVICEREQUEST,LABRESULT where  SERVICEREQUEST.REQUESTID = LABREQUEST.REQUESTID");
      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        String requestID = rset.getString("REQUEST");
        String status = rset.getString("STATUS");
        String issuer = rset.getString("ISSUERID");
        String handler = rset.getString("HANDLERID");
        String targetLoc = rset.getString("TARGETLOCATIONID");
        String labType = rset.getString("LABTYPE");

        temp =
            new LabServiceRequest(
                requestID,
                ServiceRequest.RequestStatus.getRequestStatusByString(status),
                issuer,
                handler,
                targetLoc,
                labType);
        // Avoid duplicates
        if (!returnList.contains(temp)) {
          returnList.add(temp);
        }
      }
    } catch (SQLException e) {
      System.out.println("Get All Labs Failed");
      return null;
    }
    return returnList;
  }

  /**
   * Get a LabServiceRequest with provided requestID
   *
   * @param requestID
   * @return LabServiceRequest object with given ID
   */
  @Override
  public LabServiceRequest getLabRequestByID(String requestID) {
    IServiceRequestDAO requestDAO = new ServiceRequestDAOImpl();

    try {
      PreparedStatement pstmt =
          connection.prepareStatement("SELECT * FROM LABREQUEST WHERE 'REQUESTID' = ?");
      pstmt.setString(1, requestID);
      ResultSet rset = pstmt.executeQuery();

      rset.next();
      // get the result set
      if (rset.getString("requestId") == null) {
        return null;
      }
      String requestId = rset.getString("requestId");
      String labType = rset.getString("labType");
      ServiceRequest request = requestDAO.getServiceRequestByID(requestId);

      return new LabServiceRequest(
          requestId,
          request.getStatus(),
          request.getIssuer(),
          request.getHandler(),
          request.getTargetLocation(),
          labType);
    } catch (SQLException e) {
      System.out.println("Get Lab Request by ID Failed");
    }
    // Error
    return null;
  }

  /**
   * Adds a LabServiceRequest to the database
   *
   * @param request LabServiceRequest to be added
   * @return True if successful, false otherwise
   */
  @Override
  public boolean addLabRequest(LabServiceRequest request) {

    IServiceRequestDAO requestDAO = new ServiceRequestDAOImpl();
    requestDAO.addServiceRequest(request);

    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "INSERT INTO LABREQUEST (REQUESTID, LABTYPE) values (?, ?)");
      stmt.setString(1, request.getRequestID());
      stmt.setString(2, request.getLabType());

      stmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("Statement failed");
      return false;
    }
    return true;
  }

  /**
   * Updates an existing LabServiceRequest in database with new request
   *
   * @param req LabServiceRequest to be updated
   * @return True if successful, false otherwise
   */
  @Override
  public boolean updateLabRequest(LabServiceRequest req) {
    //TODO implement updateLabRequest
    return false;
  }

  /**
   * Deletes a LabServiceRequest from the database
   *
   * @param req LabServiceRequest to be deleted
   * @return True if successful, false otherwise
   */
  @Override
  public boolean deleteLabRequest(LabServiceRequest req) {
    try {
      PreparedStatement stmt = connection.prepareStatement("DELETE FROM LABREQUEST WHERE REQUESTID=?");
      stmt.setString(1, req.getRequestID());
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Failed to delete service request");
      return false;
    }
    returnList.remove(req);
    return true;
  }

  /**
   * Exports all LabServiceRequests in the database to specified file location of csv
   *
   * @param reqData file location of csv
   * @return True if successful, false otherwise
   */
  @Override
  public boolean exportToLabRequestCSV(File reqData) {
    reqCSV = new LabRequestControlCSV(reqData);

    reqCSV.writeLabRequestCSV(returnList);
    return true;
  }

  /**
   * Imports all LabServiceRequests in specified file location of csv into the database
   *
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  @Override
  public int importLabRequestFromCSV(File data) {
    reqCSV = new LabRequestControlCSV(data);
    int conflictCounter = 0;
    String temp = "";
    try {
      List<LabServiceRequest> tempLabRequest = reqCSV.readLabRequestCSV();

      try {
        for (LabServiceRequest info : tempLabRequest) {
          PreparedStatement pstmt =
                  connection.prepareStatement(
                          "INSERT INTO LABREQUEST (REQUESTID, LABTYPE) " + "values (?, ?)");
          temp = info.getRequestID();
          pstmt.setString(1, info.getRequestID());
          pstmt.setString(2, info.getLabType());

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
                + " is already in the table or does not exist.");
      }
    } catch (IOException e) {
      System.out.println("Failed to populate Lab Equipment Request table");
    }
    return conflictCounter;
  }
}
