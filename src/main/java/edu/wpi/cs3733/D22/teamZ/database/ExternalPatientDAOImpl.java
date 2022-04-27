package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.ExternalPatientTransportationRequest;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExternalPatientDAOImpl implements IExternalPatientDAO {
  static Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();
  List<ExternalPatientTransportationRequest> requestList = new ArrayList<>();

  public ExternalPatientDAOImpl() {}

  /** Updates the connection */
  private void updateConnection() {
    connection = EnumDatabaseConnection.CONNECTION.getConnection();
  }

  /**
   * Adds a ExternalPatientTransportationRequest to the database
   *
   * @param request request to be added
   * @return True if successful, false otherwise
   */
  @Override
  public boolean addPatientTransportRequest(ExternalPatientTransportationRequest request) {
    updateConnection();
    boolean val = false;
    if (addToDatabase(request)) {
      val = true;
      requestList.add(request);
    }
    return val;
  }

  /**
   * Contains the SQL command for inserting to database
   *
   * @param request request to be inserted
   * @return True if successful, false otherwise
   */
  private boolean addToDatabase(ExternalPatientTransportationRequest request) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "INSERT INTO EXTERNALTRANSPORTREQUEST (REQUESTID,"
                  + " PATIENTID, PATIENTNAME, DESTINATION, DEPARTUREDATE) values (?, ?, ?, ?, ?)");
      stmt.setString(1, request.getRequestID());
      stmt.setString(2, request.getPatientID());
      stmt.setString(3, request.getPatientName());
      stmt.setString(4, request.getDestination());
      stmt.setDate(5, Date.valueOf(request.getDepartureDate()));

      stmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("add external patient transport request statement failed");
      return false;
    }
    return true;
  }
}
