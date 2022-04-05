package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipmentDeliveryRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedEquipReqDAOImpl implements IMedEquipReqDAO {
  private List<MedicalEquipmentDeliveryRequest> list;
  private MedEqReqControlCSV reqCSV;

  static Connection connection = DatabaseConnection.getConnection();

  public MedEquipReqDAOImpl() {
    list = new ArrayList<>();
  }

  public List<MedicalEquipmentDeliveryRequest> getAllMedEquipReq() {
    IServiceRequestDAO requestDAO = new ServiceRequestDAOImpl();

    try {
      PreparedStatement pstmt =
          connection.prepareStatement("SELECT * FROM SERVICEREQUEST WHERE TYPE = 'MEDEQUIP'");
      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        // get the result set
        String requestID = rset.getString("requestID");
        String equipmentID = rset.getString("equipmentID");

        ServiceRequest request = requestDAO.getServiceRequestByID(requestID);

        // make new temp to put into list
        MedicalEquipmentDeliveryRequest temp =
            new MedicalEquipmentDeliveryRequest(
                requestID,
                request.getStatus(),
                request.getIssuer(),
                request.getHandler(),
                equipmentID,
                request.getTargetLocation());

        // if not in the list already, add it
        if (!list.contains(temp)) {
          list.add(temp);
        }
      }
    } catch (SQLException e) {
      System.out.println("Unable to get all Medical Equipment Requests");
    }
    return list;
  }

  public MedicalEquipmentDeliveryRequest getMedEquipReqByID(String id) {
    IServiceRequestDAO requestDAO = new ServiceRequestDAOImpl();

    MedicalEquipmentDeliveryRequest deliveryRequest = null;

    try {
      // query to get information of request by ID
      PreparedStatement pstmt =
          connection.prepareStatement("Select * From MEDEQUIPREQ WHERE REQUESTID = ?");
      pstmt.setString(1, id);
      ResultSet rset = pstmt.executeQuery();

      // get the result set
      String requestID = rset.getString("requestID");
      String equipmentID = rset.getString("equipmentID");

      ServiceRequest request = requestDAO.getServiceRequestByID(requestID);

      deliveryRequest =
          new MedicalEquipmentDeliveryRequest(
              requestID,
              request.getStatus(),
              request.getIssuer(),
              request.getHandler(),
              equipmentID,
              request.getTargetLocation());

    } catch (SQLException e) {
      System.out.println("Unable to find request with given ID");
    }

    return deliveryRequest;
  }

  public void addMedEquipReq(MedicalEquipmentDeliveryRequest request) {
    IServiceRequestDAO requestDAO = new ServiceRequestDAOImpl();
    requestDAO.addServiceRequest(request);

    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "INSERT INTO MEDEQUIPREQ (REQUESTID, EQUIPMENTID) values (?, ?)");
      stmt.setString(1, request.getRequestID());
      stmt.setString(2, request.getEquipmentID());

      stmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("Statement failed");
    }
    list.add(request);
  }

  public void updateMedEquipReq(MedicalEquipmentDeliveryRequest request) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "UPDATE MEDEQUIPREQ SET status =?, handler =? WHERE RequestID =?");
      stmt.setString(1, request.getStatus().toString());
      stmt.setString(2, request.getHandler().toString());
      stmt.setString(3, request.getRequestID());

      stmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("Update failed");
    }
    list.remove(request);
    list.add(request);
  }

  public void deleteMedEquipReq(MedicalEquipmentDeliveryRequest request) {
    IServiceRequestDAO requestDAO = new ServiceRequestDAOImpl();

    try {
      PreparedStatement stmt =
          connection.prepareStatement("DELETE FROM MEDEQUIPREQ WHERE RequestID=?");
      stmt.setString(1, request.getRequestID());
      stmt.execute();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("Failed to delete from MEDEQUIPREQ table");
    }
    list.remove(request);

    requestDAO.deleteServiceRequest(request);
  }

  public boolean exportToMedEquipReqCSV(File reqData) {
    // reqData = new File(System.getProperty("user.dir") + "\\MedEquipReq.csv");
    reqCSV = new MedEqReqControlCSV(reqData);

    reqCSV.writeMedReqCSV(getAllMedEquipReq());
    return true;
  }
}
