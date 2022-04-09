package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.LabServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LabRequestServiceDAOImpl implements ILabRequestServiceDAO {

  static Connection connection = DatabaseConnection.getConnection();
  List<LabServiceRequest> returnList = new ArrayList<>();

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
              "Select LABRESULT.ITEMID, SERVICEREQUEST.STATUS, ISSUERID, HANDLERID, TARGETLOCATIONID, LABRESULT.TYPE\n"
                  + "FROM SERVICEREQUEST,LABRESULT where  SERVICEREQUEST.REQUESTID = LABRESULT.ITEMID");
      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        String itemID = rset.getString("ITEMID");
        String status = rset.getString("STATUS");
        String issuer = rset.getString("ISSUERID");
        String handler = rset.getString("HANDLERID");
        String targetLoc = rset.getString("TARGETLOCATIONID");
        String type = rset.getString("TYPE");

        temp =
            new LabServiceRequest(
                itemID,
                ServiceRequest.RequestStatus.getRequestStatusByString(status),
                issuer,
                handler,
                targetLoc,
                type);
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
          connection.prepareStatement("SELECT * FROM LABRESULT WHERE 'ITEMID' = ?");
      pstmt.setString(1, requestID);
      ResultSet rset = pstmt.executeQuery();

      rset.next();
      // get the result set
      if (rset.getString("itemId") == null) {
        return null;
      }
      String itemId = rset.getString("itemId");
      String labType = rset.getString("type");
      ServiceRequest request = requestDAO.getServiceRequestByID(itemId);

      return new LabServiceRequest(
          itemId,
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
              "INSERT INTO LABRESULT (ITEMID, TYPE, STATUS, CURRENTLOCATION) values (?, ?, ?, ?)");
      stmt.setString(1, request.getRequestID());
      stmt.setString(2, request.getLabType());
      stmt.setString(3, request.getStatus().toString());
      stmt.setString(4, request.getTargetLocation().getNodeID());

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
      PreparedStatement stmt = connection.prepareStatement("DELETE FROM LABRESULT WHERE ITEMID=?");
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
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  @Override
  public boolean exportToLabRequestCSV(File data) {
    return false;
  }

  /**
   * Imports all LabServiceRequests in specified file location of csv into the database
   *
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  @Override
  public int importLabRequestFromCSV(File data) {
    return 0;
  }
}
