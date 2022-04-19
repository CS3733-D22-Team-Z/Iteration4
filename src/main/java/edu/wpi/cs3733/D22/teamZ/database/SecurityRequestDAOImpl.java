package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.SecurityServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SecurityRequestDAOImpl implements ISecurityRequestDAO {
  private List<SecurityServiceRequest> requestList;
  static Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();
  private SecurityRequestControlCSV reqCSV;

  @Override
  public List<SecurityServiceRequest> getAllSecurityServiceRequests() {
    return requestList;
  }

  @Override
  public SecurityServiceRequest getSecurityServiceRequestByID(String requestID) {
    for (SecurityServiceRequest req : requestList) {
      if (req.getRequestID().equals(requestID)) {
        return req;
      }
    }
    return null;
  }

  @Override
  public boolean addSecurityServiceRequest(SecurityServiceRequest request) {
    updateConnection();
    boolean val = false;
    if (addToDatabase(request)) {
      val = true;
      requestList.add(request);
      // labRequests.put(request.getRequestID(), request);
    }
    return val;
  }

  @Override
  public boolean updateSecurityServiceRequest(SecurityServiceRequest request) {
    updateConnection();
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "UPDATE SERVICEREQUEST SET status =?, handlerID =? WHERE RequestID =?");
      stmt.setString(1, request.getStatus().toString());
      stmt.setString(2, request.getHandler().getEmployeeID());
      stmt.setString(3, request.getRequestID());

      stmt.executeUpdate();
      connection.commit();
      for (SecurityServiceRequest req : requestList) {
        if (req.equals(request)) {
          req.setHandler(request.getHandler());
          req.setStatus(ServiceRequest.RequestStatus.PROCESSING);
          return true;
        }
      }
      return true;
    } catch (SQLException e) {
      System.out.println("Equipment Purchase Request update failed");
      return false;
    }
  }

  @Override
  public boolean deleteSecurityServiceRequest(SecurityServiceRequest request) {
    updateConnection();
    try {
      PreparedStatement stmt =
          connection.prepareStatement("DELETE FROM SECURITYREQUEST WHERE REQUESTID=?");
      stmt.setString(1, request.getRequestID());
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Failed to delete service request");
      return false;
    }
    requestList.remove(request);
    return true;
  }

  @Override
  public boolean exportToSecurityServiceRequestCSV(File data) {
    reqCSV = new SecurityRequestControlCSV(data);

    try {
      reqCSV.writeSecurityServiceRequestCSV(requestList);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  @Override
  public int importSecurityServiceRequestFromCSV(File data) {
    updateConnection();
    reqCSV = new SecurityRequestControlCSV(data);

    int conflictCounter = 0;
    String temp = "";
    try {
      List<SecurityServiceRequest> tempSecurityRequests = reqCSV.readSecurityServiceRequestCSV();

      try {
        for (SecurityServiceRequest info : tempSecurityRequests) {
          PreparedStatement pstmt =
              connection.prepareStatement(
                  "INSERT INTO SECURITYREQUEST (REQUESTID, URGENCY, REASON) "
                      + "values (?, ?, ?)");
          temp = info.getRequestID();
          pstmt.setString(1, info.getRequestID());
          pstmt.setString(2, info.getUrgency());
          pstmt.setString(3, info.getReason());

          // insert it
          pstmt.executeUpdate();
          requestList.add(info);
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
      System.out.println("Failed to import Equipment Equipment Request table");
    }
    return conflictCounter;
  }

  @Override
  public boolean addSecurityServiceRequestFromList(List<SecurityServiceRequest> list) {
    updateConnection();
    boolean val = true;
    for (SecurityServiceRequest request : list) {
      if (!addToDatabase(request)) {
        val = false;
      }
    }
    return val;
  }

  /** Updates the connection */
  private void updateConnection() {
    connection = EnumDatabaseConnection.CONNECTION.getConnection();
  }

  private boolean addToDatabase(SecurityServiceRequest request) {
    try {
      PreparedStatement pstmt =
          connection.prepareStatement(
              "INSERT INTO SECURITYREQUEST (REQUESTID, URGENCY, REASON) " + "values (?, ?, ?)");
      pstmt.setString(1, request.getRequestID());
      pstmt.setString(2, request.getUrgency());
      pstmt.setString(3, request.getReason());

      pstmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("add security request statement failed");
      return false;
    }
    return true;
  }
}
