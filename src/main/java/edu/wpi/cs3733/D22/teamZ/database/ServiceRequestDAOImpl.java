package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class ServiceRequestDAOImpl implements IServiceRequestDAO {
  private List<ServiceRequest> serviceRequestList;
  private ServiceRequestControlCSV csvController;

  private static IEmployeeDAO employeeDAO = new EmployeeDAOImpl();
  private static ILocationDAO locationDAO = new LocationDAOImpl();

  private static Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();
  // DatabaseConnection.getConnection();

  public ServiceRequestDAOImpl() {
    updateConnection();
    File serviceRequestCSV = new File(System.getProperty("user.dir") + "\\ServiceRequest.csv");
    csvController = new ServiceRequestControlCSV(serviceRequestCSV);

    serviceRequestList = new ArrayList<ServiceRequest>();

    try {
      PreparedStatement pstmt = connection.prepareStatement("Select * From SERVICEREQUEST");
      ResultSet rset = pstmt.executeQuery();

      // Read through all of the ResultSet before calling another DAO to prevent conflicts
      List<List<String>> data = new ArrayList<>();
      while (rset.next()) {
        List<String> fields = new ArrayList<>();
        fields.add(rset.getString("requestID"));
        fields.add(rset.getString("type"));
        fields.add(rset.getString("status"));
        fields.add(rset.getString("issuerID"));
        fields.add(rset.getString("handlerID"));
        fields.add(rset.getString("targetLocationID"));

        data.add(fields);
      }

      for (List<String> fields : data) {
        String requestID = fields.get(0);
        ServiceRequest.RequestType type =
            ServiceRequest.RequestType.getRequestTypeByString(fields.get(1));
        ServiceRequest.RequestStatus status =
            ServiceRequest.RequestStatus.getRequestStatusByString(fields.get(2));
        Employee issuer = employeeDAO.getEmployeeByID(fields.get(3));
        Employee handler = employeeDAO.getEmployeeByID(fields.get(4));
        Location targetLocation = locationDAO.getLocationByID(fields.get(5));

        serviceRequestList.add(
            new ServiceRequest(requestID, type, status, issuer, handler, targetLocation));
      }
    } catch (SQLException e) {
      System.out.println("Something went wrong with the database connection.");
      e.printStackTrace();
    }
  }

  /**
   * Returns all the service requests currently stored
   *
   * @return A List of all ServiceRequest objects in the database
   */
  @Override
  public List<ServiceRequest> getAllServiceRequests() {
    updateConnection();
    return this.serviceRequestList;
  }

  /**
   * Returns a single ServiceRequest object that is stored in the database and has the id that is
   * provided
   *
   * @param serviceRequestID The id of the service request with the given id in the database
   * @return A ServiceRequest object representing the request in the database with the given
   *     serviceRequestID
   */
  @Override
  public ServiceRequest getServiceRequestByID(String serviceRequestID) {
    updateConnection();
    ServiceRequest request = null;

    try {
      PreparedStatement pstmt =
          connection.prepareStatement("Select * From SERVICEREQUEST WHERE requestID = ?");
      pstmt.setString(1, serviceRequestID);
      ResultSet rset = pstmt.executeQuery();

      if (rset.next()) {
        request = serviceRequestFromResultSet(rset);
      }
    } catch (SQLException e) {
      System.out.println("Something went wrong with the database connection.");
      e.printStackTrace();
    }

    return request;
  }

  private ServiceRequest serviceRequestFromResultSet(ResultSet rset) throws SQLException {
    updateConnection();
    String requestID = rset.getString("requestID");
    String typeStr = rset.getString("type");
    String statusStr = rset.getString("status");
    String issuerID = rset.getString("issuerID");
    String handlerID = rset.getString("handlerID");
    String targetLocationID = rset.getString("targetLocationID");

    ServiceRequest.RequestType type = ServiceRequest.RequestType.getRequestTypeByString(typeStr);
    ServiceRequest.RequestStatus status =
        ServiceRequest.RequestStatus.getRequestStatusByString(statusStr);
    Employee issuer = employeeDAO.getEmployeeByID(issuerID);
    Employee handler = employeeDAO.getEmployeeByID(handlerID);
    Location targetLocation = locationDAO.getLocationByID(targetLocationID);

    return new ServiceRequest(requestID, type, status, issuer, handler, targetLocation);
  }

  /**
   * Adds the given ServiceRequest object to the database if there is not already one with the same
   * serviceID
   *
   * @param request The request to be added
   * @return true if successfully added, false otherwise
   */
  @Override
  public boolean addServiceRequest(ServiceRequest request) {
    updateConnection();
    boolean val = false;
    if (addToDatabase(request)) {
      val = true;
      serviceRequestList.add(request);
    }
    return val;
  }

  /**
   * Takes a ServiceRequest object and deletes the respective one from the database with the same
   * requestID
   *
   * @param request The request to be deleted
   * @return true if the deletion was successful, false otherwise
   */
  @Override
  public boolean deleteServiceRequest(ServiceRequest request) {
    updateConnection();
    try {
      PreparedStatement stmt =
          connection.prepareStatement("DELETE FROM SERVICEREQUEST WHERE requestID=?");
      stmt.setString(1, request.getRequestID());
      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Failed to delete service request");
      return false;
    }
    serviceRequestList.remove(request);
    return true;
  }

  /**
   * Update a ServiceRequest object in database and list of service requests
   *
   * @param request ServiceRequest object that stores updated information
   * @returnTrue if success, false otherwise
   */
  @Override
  public boolean updateServiceRequest(ServiceRequest request) {
    updateConnection();
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "UPDATE SERVICEREQUEST SET status =?, HANDLERID =? WHERE RequestID =?");
      stmt.setString(1, request.getStatus().toString());
      stmt.setString(2, request.getHandler().toString());
      stmt.setString(3, request.getRequestID());

      stmt.executeUpdate();
      connection.commit();
      serviceRequestList.remove(request);
      serviceRequestList.add(request);
      return true;
    } catch (SQLException e) {
      System.out.println("Update failed");
      return false;
    }
  }

  /** Writes the current database to a .csv file */
  @Override
  public void exportToServiceRequestCSV() {
    updateConnection();
    csvController.writeServiceRequestCSV(serviceRequestList);
  }

  /**
   * Import ServiceRequest to database from a specified file location for csv
   *
   * @param serviceRequestData file location for csv
   * @return number of conflicts when importing
   */
  @Override
  public int importServiceRequestsFromCSV(File serviceRequestData) {
    updateConnection();
    serviceRequestData = new File(System.getProperty("user.dir") + "\\employee.csv");
    csvController = new ServiceRequestControlCSV(serviceRequestData);
    int conflictCounter = 0;
    try {
      List<ServiceRequest> tempServiceRequest = csvController.readServiceRequestCSV();
      String temp = "";

      try {
        for (ServiceRequest info : tempServiceRequest) {
          PreparedStatement pstmt =
              connection.prepareStatement(
                  "INSERT INTO SERVICEREQUEST (REQUESTID, TYPE, STATUS, ISSUERID, HANDLERID, TARGETLOCATIONID) "
                      + "values (?, ?, ?, ?, ?, ?)");
          temp = info.getRequestID();
          pstmt.setString(1, info.getRequestID());
          pstmt.setString(2, info.getType().toString());
          pstmt.setString(3, info.getStatus().toString());
          pstmt.setString(4, info.getIssuer().toString());
          pstmt.setString(5, info.getHandler().toString());
          pstmt.setString(6, info.getTargetLocation().toString());

          // insert it
          pstmt.executeUpdate();
        }
      } catch (SQLException e) {
        conflictCounter++;
        System.out.println(
            "Found "
                + conflictCounter
                + " conflicts. "
                + temp
                + " might have a location that does not exist");
      }
    } catch (IOException e) {
      System.out.println("Failed to insert into Employee table");
      e.printStackTrace();
    }
    return conflictCounter;
  }

  /** Updates the connection */
  private void updateConnection() {
    connection = EnumDatabaseConnection.CONNECTION.getConnection();
  }

  /**
   * Insert service requests into the database from the given list
   *
   * @param list list of service requests to be added
   * @return True if successful, false otherwise
   */
  public boolean addServiceRequestFromList(List<ServiceRequest> list) {
    updateConnection();
    boolean val = true;
    for (ServiceRequest request : list) {
      if (!addToDatabase(request)) {
        val = false;
      }
    }
    return val;
  }

  /**
   * Contains SQL command for inserting into database
   *
   * @param request service request to be added
   * @return True if successful, false otherwise
   */
  private boolean addToDatabase(ServiceRequest request) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "INSERT INTO SERVICEREQUEST"
                  + "(requestID, type, status, issuerID, handlerID, targetLocationID)"
                  + "values (?, ?, ?, ?, ?, ?)");
      stmt.setString(1, request.getRequestID());
      stmt.setString(2, request.getType().toString());
      stmt.setString(3, request.getStatus().toString());
      stmt.setString(4, request.getIssuer().getEmployeeID());
      stmt.setString(5, request.getHandler().getEmployeeID());
      stmt.setString(6, request.getTargetLocation().getNodeID());

      stmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("Statement failed");
      return false;
    }
    return true;
  }
}
