package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.LanguageInterpreterRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LanguageInterpreterRequestDAOImpl implements ILanguageInterpreterDAO {
  List<LanguageInterpreterRequest> languageInterpreterList;
  static Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();
  private LanguageInterpreterRequestControlCSV reqCSV;

  public LanguageInterpreterRequestDAOImpl() {
    updateConnection();
    languageInterpreterList = new ArrayList<>();
  }

  @Override
  public List<LanguageInterpreterRequest> getAllLanguageInterpreterServiceRequests()
      throws SQLException {
    return languageInterpreterList;
  }

  @Override
  public LanguageInterpreterRequest getLanguageInterpreterRequestByID(String requestID) {
    for (LanguageInterpreterRequest request : languageInterpreterList) {
      if (request.getRequestID().equals(requestID)) {
        return request;
      }
    }
    return null;
  }

  @Override
  public boolean addLanguageInterpreterRequest(LanguageInterpreterRequest request) {
    updateConnection();
    boolean val = false;
    if (addToDatabase(request)) {
      val = true;
      languageInterpreterList.add(request);
    }
    return val;
  }

  @Override
  public boolean updateLanguageInterpreterRequest(LanguageInterpreterRequest request) {
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
      for (LanguageInterpreterRequest req : languageInterpreterList) {
        if (req.equals(request)) {
          req.setHandler(request.getHandler());
          req.setStatus(ServiceRequest.RequestStatus.PROCESSING);
          return true;
        }
      }
      return true;
    } catch (SQLException e) {
      System.out.println("Language Intepreter update failed");
      return false;
    }
  }

  @Override
  public boolean deleteLanguageInterpreterRequest(LanguageInterpreterRequest request) {
    updateConnection();
    try {
      PreparedStatement stmt =
          connection.prepareStatement("DELETE FROM LANGUAGEINTERPRETERREQUEST WHERE REQUESTID=?");
      stmt.setString(1, request.getRequestID());
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Failed to delete service request");
      return false;
    }
    languageInterpreterList.remove(request);
    return true;
  }

  @Override
  public boolean exportToLanguageInterpreterRequestCSV(File data) {
    reqCSV = new LanguageInterpreterRequestControlCSV(data);

    try {
      reqCSV.writeLanguageInterpreterRequestCSV(languageInterpreterList);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  @Override
  public int importLanguageInterpreterRequestFromCSV(File data) {
    updateConnection();
    reqCSV = new LanguageInterpreterRequestControlCSV(data);

    int conflictCounter = 0;
    String temp = "";
    try {
      List<LanguageInterpreterRequest> tempLanguageInterpreter =
          reqCSV.readLanguageIntepreterRequestCSV();

      try {
        for (LanguageInterpreterRequest info : tempLanguageInterpreter) {
          PreparedStatement pstmt =
              connection.prepareStatement(
                  "INSERT INTO LANGUAGEINTERPRETERREQUEST (REQUESTID, PATIENTNAME, PATIENTID, LANGUAGE) "
                      + "values (?, ?, ?, ?)");
          temp = info.getRequestID();
          pstmt.setString(1, info.getRequestID());
          pstmt.setString(2, info.getPatientName());
          pstmt.setString(3, info.getPatientID());
          pstmt.setString(4, info.getLanguage());

          // insert it
          pstmt.executeUpdate();

          languageInterpreterList.add(info);
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
      System.out.println("Failed to populate Language Interpreter Request table");
    }
    return conflictCounter;
  }

  @Override
  public boolean addLanguageInterpreterFromList(List<LanguageInterpreterRequest> list) {
    updateConnection();
    boolean val = true;
    for (LanguageInterpreterRequest request : list) {
      if (!addToDatabase(request)) {
        val = false;
      }
    }
    return val;
  }

  private boolean addToDatabase(LanguageInterpreterRequest request) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "INSERT INTO LANGUAGEINTERPRETERREQUEST (REQUESTID, PATIENTNAME, PATIENTID, LANGUAGE) values (?, ?, ? ,?)");
      stmt.setString(1, request.getRequestID());
      stmt.setString(2, request.getPatientName());
      stmt.setString(3, request.getPatientID());
      stmt.setString(4, request.getLanguage());

      stmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("add language interpreter request failed");
      return false;
    }
    return true;
  }

  private void updateConnection() {
    connection = EnumDatabaseConnection.CONNECTION.getConnection();
  }
}
