package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.CleaningRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CleaningRequestDAOImpl implements ICleaningRequestDAO {
  static Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();
  // DatabaseConnection.getConnection();
  private final List<CleaningRequest> cleaningRequests;
  private CleaningReqControlCSV reqCSV;

  public CleaningRequestDAOImpl() {
    updateConnection();
    // medicalEquipmentRequests = new HashMap<>();
    cleaningRequests = new ArrayList<>();

    File reqData =
        new File(
            System.getProperty("user.dir")
                + System.getProperty("file.separator")
                + "CleaningReq.csv");
    this.reqCSV = new CleaningReqControlCSV(reqData);
  }

  /**
   * Gets all cleaning service requests
   *
   * @return list of cleaning service requests
   */
  @Override
  public List<CleaningRequest> getAllCleaningServiceRequests() {
    return cleaningRequests;
  }

  /**
   * Get a CleaningRequest with provided requestID
   *
   * @param requestID The id of the request to be search for
   * @return CleaningRequest object with given ID
   */
  @Override
  public CleaningRequest getCleaningRequestByID(String requestID) {
    for (CleaningRequest req : cleaningRequests) {
      if (req.getRequestID().equals(requestID)) {
        return req;
      }
    }
    return null;
  }

  /**
   * Adds a CleaningRequest to the database
   *
   * @param request CleaningRequest to be added
   * @return True if successful, false otherwise
   */
  @Override
  public boolean addCleaningRequest(CleaningRequest request) {
    updateConnection();
    boolean val = false;
    if (addToDatabase(request)) {
      val = true;
      cleaningRequests.add(request);
    }
    return val;
  }

  /**
   * Updates an existing CleaningRequest in database with new request
   *
   * @param request CleaningRequest to be updated
   * @return True if successful, false otherwise
   */
  @Override
  public boolean updateCleaningRequest(CleaningRequest request) {
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
      for (CleaningRequest req : cleaningRequests) {
        if (req.equals(request)) {
          req.setStatus(request.getStatus());
          req.setHandler(request.getHandler());
          req.setClosed(request.getClosed());
          return true;
        }
      }
      return true;
    } catch (SQLException e) {
      System.out.println("Cleaning service update failed");
      return false;
    }
  }

  /**
   * Deletes a CleaningRequest from the database
   *
   * @param req CleaningRequest to be deleted
   * @return True if successful, false otherwise
   */
  @Override
  public boolean deleteCleaningRequest(CleaningRequest req) {
    updateConnection();
    try {
      PreparedStatement stmt =
          connection.prepareStatement("DELETE FROM CLEANINGREQUEST WHERE REQUESTID=?");
      stmt.setString(1, req.getRequestID());
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Failed to delete service request");
      return false;
    }
    cleaningRequests.remove(req);
    return true;
  }

  /**
   * Exports all CleaningRequests in the database to specified file location of csv
   *
   * @param reqData file location of csv
   * @return True if successful, false otherwise
   */
  @Override
  public boolean exportToCleaningRequestCSV(File reqData) {
    reqCSV = new CleaningReqControlCSV(reqData);

    try {
      reqCSV.writeCleanReqCSV(cleaningRequests);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * Imports all CleaningServiceRequests in specified file location of csv into the database
   *
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  @Override
  public int importCleaningRequestFromCSV(File data) {
    updateConnection();
    reqCSV = new CleaningReqControlCSV(data);

    int conflictCounter = 0;
    String temp = "";
    try {
      List<CleaningRequest> tempCleaningRequest = reqCSV.readCleanReqCSV();

      try {
        for (CleaningRequest info : tempCleaningRequest) {
          PreparedStatement pstmt =
              connection.prepareStatement(
                  "INSERT INTO CLEANINGREQUEST (REQUESTID, TYPE) " + "values (?, ?)");
          temp = info.getRequestID();
          pstmt.setString(1, info.getRequestID());
          pstmt.setString(2, info.getCleaningType());

          // insert it
          pstmt.executeUpdate();

          cleaningRequests.add(info);
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
      System.out.println("Failed to populate Cleaning Equipment Request table");
    }
    return conflictCounter;
  }

  File getDefaultCleaningReqCSVPath() {
    return reqCSV.getDefaultPath();
  }

  /** Updates the connection */
  private void updateConnection() {
    connection = EnumDatabaseConnection.CONNECTION.getConnection();
  }

  /**
   * Inserts cleaning requests into database from given list
   *
   * @param list list of cleaning requests to be added
   * @return true if successful, false otherwise
   */
  public boolean addCleaningRequestFromList(List<CleaningRequest> list) {
    updateConnection();
    boolean val = true;
    for (CleaningRequest request : list) {
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
  private boolean addToDatabase(CleaningRequest request) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "INSERT INTO CLEANINGREQUEST (REQUESTID, TYPE) values (?, ?)");
      stmt.setString(1, request.getRequestID());
      stmt.setString(2, request.getCleaningType());

      stmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("add cleaning request statement failed");
      return false;
    }
    return true;
  }
}
