package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.LaundryServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LaundryServiceDAOImpl implements ILaundryServiceDAO {

  private List<LaundryServiceRequest> requestList = new ArrayList<>();
  static Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();
  private LaundryServiceRequestControlCSV reqCSV;
  // TODO CSV

  public LaundryServiceDAOImpl() {
    updateConnection();
    // medicalEquipmentRequests = new HashMap<>();
    requestList = new ArrayList<>();

    File reqData =
        new File(
            System.getProperty("user.dir")
                + System.getProperty("file.separator")
                + "LaundryServiceRequest.csv");
    this.reqCSV = new LaundryServiceRequestControlCSV(reqData);
  }

  @Override
  public List<LaundryServiceRequest> getAllLaundryServiceRequests() {
    return requestList;
  }

  @Override
  public LaundryServiceRequest getLaundryRequestById(String requestID) {
    for (LaundryServiceRequest req : requestList) {
      if (req.getRequestID().equals(requestID)) {
        return req;
      }
    }
    return null;
  }

  @Override
  public boolean addLaundryServiceRequest(LaundryServiceRequest request) {
    updateConnection();

    if (addToDatabase(request)) {
      requestList.add(request);
      return true;
    }
    return false;
  }

  @Override
  public boolean updateLaundryServiceRequest(LaundryServiceRequest request) {
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
      for (LaundryServiceRequest req : requestList) {
        if (req.equals(request)) {
          req.setHandler(request.getHandler());
          req.setStatus(ServiceRequest.RequestStatus.PROCESSING);
          return true;
        }
      }
      return true;
    } catch (SQLException e) {
      System.out.println("Laundry Service update failed");
      return false;
    }
  }

  @Override
  public boolean deleteLaundryServiceRequest(LaundryServiceRequest request) {
    updateConnection();
    try {
      PreparedStatement stmt =
          connection.prepareStatement("DELETE FROM LAUNDRYREQUEST WHERE REQUESTID=?");
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
  public boolean exportToLaundryServiceRequestCSV(File data) {
    reqCSV = new LaundryServiceRequestControlCSV(data);

    try {
      reqCSV.writeLaundryServiceRequestCSV(requestList);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  @Override
  public int importLaundryServiceRequestFromCSV(File data) {
    updateConnection();
    reqCSV = new LaundryServiceRequestControlCSV(data);

    int conflictCounter = 0;
    String temp = "";
    try {
      List<LaundryServiceRequest> tempPurchaseRequest = reqCSV.readLaundryServiceRequestCSV();

      try {
        for (LaundryServiceRequest info : tempPurchaseRequest) {
          PreparedStatement pstmt =
              connection.prepareStatement(
                  "INSERT INTO LAUNDRYREQUEST (REQUESTID, LAUNDRYSTATUS, LAUNDRYTYPE) "
                      + "values (?, ?, ?)");
          temp = info.getRequestID();
          pstmt.setString(1, info.getRequestID());
          pstmt.setString(2, info.getLaundryStatus().toString());
          pstmt.setString(3, info.getLaundryType());

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
      System.out.println("Failed to import Laundry Service Request table");
    }
    return conflictCounter;
  }

  @Override
  public boolean addLaundryServiceRequestFromList(List<LaundryServiceRequest> list) {
    updateConnection();
    boolean val = true;
    for (LaundryServiceRequest request : list) {
      if (!addToDatabase(request)) {
        val = false;
      }
    }
    return val;
  }

  /** Updates connection to Database */
  private void updateConnection() {
    connection = EnumDatabaseConnection.CONNECTION.getConnection();
  }

  /**
   * Contains the SQL command for inserting to database
   *
   * @param request request to be inserted
   * @return True if successful, false otherwise
   */
  private boolean addToDatabase(LaundryServiceRequest request) {
    try {
      PreparedStatement pstmt =
          connection.prepareStatement(
              "INSERT INTO LAUNDRYREQUEST (REQUESTID, LAUNDRYSTATUS, LAUNDRYTYPE) "
                  + "values (?, ?, ?)");
      pstmt.setString(1, request.getRequestID());
      pstmt.setString(2, String.valueOf(request.getLaundryStatus()));
      pstmt.setString(3, request.getLaundryType());

      pstmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("add laundry service request statement failed");
      return false;
    }
    return true;
  }

  /**
   * Returns the default path that medical equipment delivery request csv files are printed to
   *
   * @return The default path that medical equipment delivery request csv files are printed to
   */
  File getDefaultLaundryServiceRequestCSVPath() {
    return reqCSV.getDefaultPath();
  }
}
