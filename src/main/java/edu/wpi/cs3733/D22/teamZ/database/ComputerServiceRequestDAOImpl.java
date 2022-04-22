package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.ComputerServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComputerServiceRequestDAOImpl implements IComputerServiceRequestDAO {
  private List<ComputerServiceRequest> requestList = new ArrayList<>();
  private ComputerRequestControlCSV reqCSV;
  static Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();

  @Override
  public List<ComputerServiceRequest> getAllComputerServiceRequests() {
    return requestList;
  }

  @Override
  public ComputerServiceRequest getComputerServiceRequestByID(String requestID) {
    for (ComputerServiceRequest req : requestList) {
      if (req.getRequestID().equals(requestID)) {
        return req;
      }
    }
    return null;
  }

  @Override
  public boolean addComputerServiceRequest(ComputerServiceRequest request) {
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
  public boolean updateComputerServiceRequest(ComputerServiceRequest request) {
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
      for (ComputerServiceRequest req : requestList) {
        if (req.equals(request)) {
          req.setHandler(request.getHandler());
          req.setStatus(ServiceRequest.RequestStatus.PROCESSING);
          return true;
        }
      }
      return true;
    } catch (SQLException e) {
      System.out.println("Computer Service Request update failed");
      return false;
    }
  }

  @Override
  public boolean deleteComputerServiceRequest(ComputerServiceRequest request) {
    updateConnection();
    try {
      PreparedStatement stmt =
          connection.prepareStatement("DELETE FROM COMPUTERREQUEST WHERE REQUESTID=?");
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
  public boolean exportToComputerServiceRequestCSV(File data) {
    reqCSV = new ComputerRequestControlCSV(data);

    try {
      reqCSV.writeComputerServiceRequestCSV(requestList);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  @Override
  public int importComputerServiceRequestFromCSV(File data) {
    updateConnection();
    reqCSV = new ComputerRequestControlCSV(data);

    int conflictCounter = 0;
    String temp = "";
    try {
      List<ComputerServiceRequest> tempPurchaseRequest = reqCSV.readComputerServiceRequestCSV();

      try {
        for (ComputerServiceRequest info : tempPurchaseRequest) {
          PreparedStatement pstmt =
              connection.prepareStatement(
                  "INSERT INTO COMPUTERREQUEST (REQUESTID, OPERATINGSYSTEM, PROBLEMDESC) "
                      + "values (?, ?, ?)");
          temp = info.getRequestID();
          pstmt.setString(1, info.getRequestID());
          pstmt.setString(2, info.getOperatingSystem());
          pstmt.setString(3, info.getProblemDescription());

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
  public boolean addComputerServiceRequestFromList(List<ComputerServiceRequest> list) {
    updateConnection();
    boolean val = true;
    for (ComputerServiceRequest request : list) {
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

  /**
   * Contains the SQL command for inserting to database
   *
   * @param request request to be inserted
   * @return True if successful, false otherwise
   */
  private boolean addToDatabase(ComputerServiceRequest request) {
    try {
      PreparedStatement pstmt =
          connection.prepareStatement(
              "INSERT INTO COMPUTERREQUEST (REQUESTID, OPERATINGSYSTEM, PROBLEMDESC) "
                  + "values (?, ?, ?)");
      pstmt.setString(1, request.getRequestID());
      pstmt.setString(2, request.getOperatingSystem());
      pstmt.setString(3, request.getProblemDescription());

      pstmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("add computer service request statement failed");
      return false;
    }
    return true;
  }

  /**
   * Returns the default path for a computer service request csv file to be saved
   *
   * @return The default path for a computer service request csv file to be saved
   */
  File getDefaultComputerServiceRequestCSVPath() {
    return reqCSV.getDefaultPath();
  }
}
