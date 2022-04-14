package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.LabServiceRequest;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class LabRequestServiceDAOImpl implements ILabRequestServiceDAO {

  static Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();
  // DatabaseConnection.getConnection();
  // private HashMap<String, LabServiceRequest> labRequests = new HashMap<>();
  private List<LabServiceRequest> labRequests = new ArrayList<>();
  private LabRequestControlCSV reqCSV;

  /**
   * Gets all lab service requests
   *
   * @return list of lab service requests
   * @throws SQLException
   */
  @Override
  public List<LabServiceRequest> getAllLabServiceRequests() {
    return labRequests;
  }

  /**
   * Get a LabServiceRequest with provided requestID
   *
   * @param requestID
   * @return LabServiceRequest object with given ID
   */
  @Override
  public LabServiceRequest getLabRequestByID(String requestID) {
    int i = 0;
    for (LabServiceRequest req : labRequests) {
      if (req.getRequestID().equals(requestID)) {
        return labRequests.get(i);
      }
      i++;
    }
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
    updateConnection();
    boolean val = false;
    if (addToDatabase(request)) {
      val = true;
      labRequests.add(request);
      // labRequests.put(request.getRequestID(), request);
    }
    return val;
  }

  /**
   * Updates an existing LabServiceRequest in database with new request
   *
   * @param req LabServiceRequest to be updated
   * @return True if successful, false otherwise
   */
  @Override
  public boolean updateLabRequest(LabServiceRequest req) {
    updateConnection();
    // TODO implement updateLabRequest
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
    updateConnection();
    try {
      PreparedStatement stmt =
          connection.prepareStatement("DELETE FROM LABREQUEST WHERE REQUESTID=?");
      stmt.setString(1, req.getRequestID());
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Failed to delete service request");
      return false;
    }
    labRequests.remove(req);
    // labRequests.remove(req.getRequestID());
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
    reqCSV.writeLabRequestCSV(getAllLabServiceRequests());
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
    updateConnection();
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

          labRequests.add(info);
          // labRequests.put(info.getRequestID(), info);
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

  /** Updates the connection */
  private void updateConnection() {
    connection = EnumDatabaseConnection.CONNECTION.getConnection();
  }

  /**
   * Inserts lab requests into database from given list
   *
   * @param list list of lab requests to be added
   * @return true if successful, false otherwise
   */
  public boolean addLabRequestFromList(List<LabServiceRequest> list) {
    updateConnection();
    boolean val = true;
    for (LabServiceRequest request : list) {
      if (!addToDatabase(request)) {
        val = false;
      }
    }
    return val;
  }

  /**
   * Contains the SQL command for inserting to database
   *
   * @param request request to be inserted
   * @return True if successful, false otherwise
   */
  private boolean addToDatabase(LabServiceRequest request) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement("INSERT INTO LABREQUEST (REQUESTID, LABTYPE) values (?, ?)");
      stmt.setString(1, request.getRequestID());
      stmt.setString(2, request.getLabType());

      stmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("add lab request statement failed");
      return false;
    }
    return true;
  }
}
