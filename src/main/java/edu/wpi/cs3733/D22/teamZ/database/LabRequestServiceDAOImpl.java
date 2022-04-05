package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.LabServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LabRequestServiceDAOImpl implements ILabRequestServiceDAO {

  static Connection connection = DatabaseConnection.getConnection();

  @Override
  public List<LabServiceRequest> getAllLabServiceRequests() {
    IServiceRequestDAO requestDAO = new ServiceRequestDAOImpl();
    List<List<String>> labResultList = new ArrayList<>();

    LabServiceRequest temp;
    List<LabServiceRequest> returnList = new ArrayList<>();

    try {
      PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM LABRESULT");
      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        // get the result set
        labResultList.add(
            Arrays.asList(
                rset.getString("itemId"),
                rset.getString("type"),
                rset.getString("currentLocation")));
      }
      for (List<String> serviceRequestList : labResultList) {
        String itemId = serviceRequestList.get(0);
        String labType = serviceRequestList.get(1);
        String currentLocation = serviceRequestList.get(2);
        ServiceRequest request = requestDAO.getServiceRequestByID(itemId);

        // make new temp to put into list
        temp =
            new LabServiceRequest(
                itemId,
                request.getStatus(),
                request.getIssuer(),
                request.getHandler(),
                request.getTargetLocation(),
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

  @Override
  public LabServiceRequest getLabRequestByID(String requestID) {
    IServiceRequestDAO requestDAO = new ServiceRequestDAOImpl();
    List<List<String>> labResultList = new ArrayList<>();

    LabServiceRequest temp;
    List<LabServiceRequest> returnList = new ArrayList<>();

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
      stmt.setString(2, request.getStatus().toString());
      stmt.setString(2, request.getTargetLocation().getShortName());

      stmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("Statement failed");
      return false;
    }
    return true;
  }

  @Override
  public void updateLabRequest(LabServiceRequest req) {}

  @Override
  public void deleteLabRequest(LabServiceRequest req) {}

  @Override
  public boolean exportToLabRequestCSV() {
    return false;
  }
}
