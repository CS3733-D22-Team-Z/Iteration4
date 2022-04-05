package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipmentDeliveryRequest;
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
    try {
      PreparedStatement pstmt =
          connection.prepareStatement(
              "SELECT * FROM SERVICEREQUEST WHERE TYPE = 'MedicalEquipment'");
      ResultSet rset = pstmt.executeQuery();

      list.clear();

      while (rset.next()) {
        // get the result set
        String reqID = rset.getString("requestID");
        String status = rset.getString("status");
        String issuer = rset.getString("issuer");
        String handler = rset.getString("handler");
        String equipment = rset.getString("equipment");
        String currentLoc = rset.getString("currentLoc");
        String targetLoc = rset.getString("targetLoc");

        // make new temp to put into list
        MedicalEquipmentDeliveryRequest temp =
            new MedicalEquipmentDeliveryRequest(
                reqID, status, issuer, handler, equipment, currentLoc, targetLoc);

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
    MedicalEquipmentDeliveryRequest temp = new MedicalEquipmentDeliveryRequest();

    try {
      // query to get information of request by ID
      PreparedStatement pstmt =
          connection.prepareStatement("Select * From MEDEQUIPREQ WHERE REQUESTID = ?");
      pstmt.setString(1, id);
      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        // get the result set
        String reqID = rset.getString("requestID");
        String status = rset.getString("status");
        String issuer = rset.getString("issuer");
        String handler = rset.getString("handler");
        String equipment = rset.getString("equipment");
        String currentLoc = rset.getString("currentLoc");
        String targetLoc = rset.getString("targetLoc");

        temp.setRequestID(reqID);
        temp.setStatus(status);
        temp.setIssuer(issuer);
        temp.setHandler(handler);
        temp.setEquipment(equipment);
        temp.setCurrentLoc(currentLoc);
        temp.setTargetLoc(targetLoc);
      }
    } catch (SQLException e) {
      System.out.println("Unable to find request with given ID");
    }
    return temp;
  }

  public void addMedEquipReq(MedicalEquipmentDeliveryRequest req) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "INSERT INTO MEDEQUIPREQ (REQUESTID, STATUS, ISSUER, HANDLER, EQUIPMENT, CURRENTLOC, TARGETLOC) "
                  + "values (?, ?, ?, ?, ?, ?, ?)");
      stmt.setString(1, req.getRequestID());
      stmt.setString(2, req.getStatus());
      stmt.setString(3, req.getIssuer());
      stmt.setString(4, req.getHandler());
      stmt.setString(5, req.getEquipment());
      stmt.setString(6, req.getCurrentLoc());
      stmt.setString(7, req.getTargetLoc());

      stmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("Statement failed");
    }
    list.add(req);
  }

  public void updateMedEquipReq(MedicalEquipmentDeliveryRequest req) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "UPDATE MEDEQUIPREQ SET status =?, handler =? WHERE RequestID =?");
      stmt.setString(1, req.getStatus());
      stmt.setString(2, req.getHandler());
      stmt.setString(3, req.getRequestID());

      stmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("Update failed");
    }
    list.remove(req);
    list.add(req);
  }

  public void deleteMedEquipReq(MedicalEquipmentDeliveryRequest req) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement("DELETE FROM MEDEQUIPREQ WHERE RequestID=?");
      stmt.setString(1, req.getRequestID());
      stmt.execute();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("Deletion failed");
    }
    list.remove(req);
  }

  public boolean exportToMedEquipReqCSV() {
    File reqData = new File(System.getProperty("user.dir") + "\\MedEquipReq.csv");
    reqCSV = new MedEqReqControlCSV(reqData);

    reqCSV.writeMedReqCSV(getAllMedEquipReq());
    return true;
  }
}
