package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.EquipmentPurchaseRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipmentPurchaseDAOImpl implements IEquipmentPurchaseDAO {

  private List<EquipmentPurchaseRequest> requestList = new ArrayList<>();
  static Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();
  private EquipmentPurchaseRequestControlCSV reqCSV;

  @Override
  public List<EquipmentPurchaseRequest> getAllEquipmentPurchaseRequests() {
    return requestList;
  }

  @Override
  public EquipmentPurchaseRequest getEquipmentPurchaseRequestByID(String requestID) {
    for (EquipmentPurchaseRequest req : requestList) {
      if (req.getRequestID().equals(requestID)) {
        return req;
      }
    }
    return null;
  }

  @Override
  public boolean addEquipmentPurchaseRequest(EquipmentPurchaseRequest request) {
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
  public boolean updateEquipmentPurchaseRequest(EquipmentPurchaseRequest request) {
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
      for (EquipmentPurchaseRequest req : requestList) {
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
  public boolean deleteEquipmentPurchaseRequest(EquipmentPurchaseRequest request) {
    updateConnection();
    try {
      PreparedStatement stmt =
          connection.prepareStatement("DELETE FROM EQUIPMENTPURCHASE WHERE REQUESTID=?");
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
  public boolean exportToEquipmentPurchaseRequestCSV(File data) {
    reqCSV = new EquipmentPurchaseRequestControlCSV(data);

    try {
      reqCSV.writeEquipmentPurchaseRequestCSV(requestList);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  @Override
  public int importEquipmentPurchaseRequestFromCSV(File data) {
    updateConnection();
    reqCSV = new EquipmentPurchaseRequestControlCSV(data);

    int conflictCounter = 0;
    String temp = "";
    try {
      List<EquipmentPurchaseRequest> tempPurchaseRequest = reqCSV.readEquipmentPurchaseRequestCSV();

      try {
        for (EquipmentPurchaseRequest info : tempPurchaseRequest) {
          PreparedStatement pstmt =
              connection.prepareStatement(
                  "INSERT INTO EQUIPMENTPURCHASE (REQUESTID, EQUIPMENTTYPE, PAYMENTMETHOD) "
                      + "values (?, ?, ?)");
          temp = info.getRequestID();
          pstmt.setString(1, info.getRequestID());
          pstmt.setString(2, info.getEquipmentType());
          pstmt.setString(3, info.getPaymentMethod());

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
  public boolean addEquipmentPurchaseRequestFromList(List<EquipmentPurchaseRequest> list) {
    updateConnection();
    boolean val = true;
    for (EquipmentPurchaseRequest request : list) {
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
  private boolean addToDatabase(EquipmentPurchaseRequest request) {
    try {
      PreparedStatement pstmt =
          connection.prepareStatement(
              "INSERT INTO EQUIPMENTPURCHASE (REQUESTID, EQUIPMENTTYPE, PAYMENTMETHOD) "
                  + "values (?, ?, ?)");
      pstmt.setString(1, request.getRequestID());
      pstmt.setString(2, request.getEquipmentType());
      pstmt.setString(3, request.getPaymentMethod());

      pstmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("add equipment purchase request statement failed");
      return false;
    }
    return true;
  }
  /**
   * Returns the default path for an Equipment Purchase Request csv file to be saved
   *
   * @return The default path for a service request csv file to be saved
   */
  File getDefaultEquipmentPurchaseRequestCSVPath() {
    return reqCSV.getDefaultPath();
  }
}
