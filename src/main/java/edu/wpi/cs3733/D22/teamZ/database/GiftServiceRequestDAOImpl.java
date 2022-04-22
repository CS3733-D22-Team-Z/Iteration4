package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.GiftServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GiftServiceRequestDAOImpl implements IGiftServiceRequestDAO {

  List<GiftServiceRequest> giftList;
  static Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();
  private GiftServiceRequestControlCSV reqCSV;

  public GiftServiceRequestDAOImpl() {
    updateConnection();
    giftList = new ArrayList<>();

    File giftData = new File(System.getProperty("user.dir") + "\\giftRequest.csv");
    reqCSV = new GiftServiceRequestControlCSV(giftData);
  }

  @Override
  public List<GiftServiceRequest> getAllGiftServiceRequests() {
    return giftList;
  }

  @Override
  public GiftServiceRequest getGiftRequestByID(String requestID) {
    for (GiftServiceRequest request : giftList) {
      if (request.getRequestID().equals(requestID)) {
        return request;
      }
    }
    return null;
  }

  File getDefaultGiftServiceCSVPath() {
    return reqCSV.getDefaultPath();
  }

  @Override
  public boolean addGiftRequest(GiftServiceRequest request) {
    updateConnection();
    boolean val = false;
    if (addToDatabase(request)) {
      val = true;
      giftList.add(request);
    }
    return val;
  }

  @Override
  public boolean updateGiftRequest(GiftServiceRequest request) {
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
      for (GiftServiceRequest req : giftList) {
        if (req.equals(request)) {
          req.setHandler(request.getHandler());
          req.setStatus(ServiceRequest.RequestStatus.PROCESSING);
          return true;
        }
      }
      return true;
    } catch (SQLException e) {
      System.out.println("Gift service update failed");
      return false;
    }
  }

  @Override
  public boolean deleteGiftRequest(GiftServiceRequest request) {
    updateConnection();
    try {
      PreparedStatement stmt =
          connection.prepareStatement("DELETE FROM GIFTSERVICEREQUEST WHERE REQUESTID=?");
      stmt.setString(1, request.getRequestID());
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Failed to delete service request");
      return false;
    }
    giftList.remove(request);
    return true;
  }

  @Override
  public boolean exportToGiftRequestCSV(File data) {
    reqCSV = new GiftServiceRequestControlCSV(data);

    try {
      reqCSV.writeGiftServiceRequestCSV(giftList);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  @Override
  public int importGiftRequestFromCSV(File data) {
    updateConnection();
    reqCSV = new GiftServiceRequestControlCSV(data);

    int conflictCounter = 0;
    String temp = "";
    try {
      List<GiftServiceRequest> tempGiftRequest = reqCSV.readGiftServiceRequestCSV();

      try {
        for (GiftServiceRequest info : tempGiftRequest) {
          PreparedStatement pstmt =
              connection.prepareStatement(
                  "INSERT INTO GIFTSERVICEREQUEST (REQUESTID, PATIENTNAME, PATIENTID, GIFTTYPE) "
                      + "values (?, ?, ?, ?)");
          temp = info.getRequestID();
          pstmt.setString(1, info.getRequestID());
          pstmt.setString(2, info.getPatientName());
          pstmt.setString(3, info.getPatientID());
          pstmt.setString(4, info.getGiftType());

          // insert it
          pstmt.executeUpdate();

          giftList.add(info);
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
      System.out.println("Failed to populate Gift Service Request table");
    }
    return conflictCounter;
  }

  @Override
  public boolean addGiftRequestFromList(List<GiftServiceRequest> list) {
    updateConnection();
    boolean val = true;
    for (GiftServiceRequest request : list) {
      if (!addToDatabase(request)) {
        val = false;
      }
    }
    return val;
  }

  private boolean addToDatabase(GiftServiceRequest request) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "INSERT INTO GIFTSERVICEREQUEST (REQUESTID, PATIENTNAME, PATIENTID, GIFTTYPE) values (?, ?, ? ,?)");
      stmt.setString(1, request.getRequestID());
      stmt.setString(2, request.getPatientName());
      stmt.setString(3, request.getPatientID());
      stmt.setString(4, request.getGiftType());

      stmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("add gift service request failed");
      return false;
    }
    return true;
  }

  private void updateConnection() {
    connection = EnumDatabaseConnection.CONNECTION.getConnection();
  }
}
