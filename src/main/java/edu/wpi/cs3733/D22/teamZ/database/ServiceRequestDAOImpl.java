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
import java.util.HashMap;
import java.util.List;

class ServiceRequestDAOImpl implements IServiceRequestDAO {
  private HashMap<String, ServiceRequest> serviceRequests;
  private ServiceRequestControlCSV csvController;

  private static Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();
  // DatabaseConnection.getConnection();

  public ServiceRequestDAOImpl() {
    updateConnection();
    serviceRequests = new HashMap<>();
  }

  /**
   * Returns all the service requests currently stored
   *
   * @return A List of all ServiceRequest objects in the database
   */
  @Override
  public List<ServiceRequest> getAllServiceRequests() {
    ArrayList<ServiceRequest> requestsList = new ArrayList<>(serviceRequests.values());
    return requestsList;
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
    return serviceRequests.get(serviceRequestID);
  }

  /**
   * Returns a list of ServiceRequest objects stored in the database that are located
   * in the given target location
   *
   * @param target The location to search
   * @return A list of service requests that are located in the given location
   */
  @Override
  public List<ServiceRequest> getServiceRequestsByLocation(Location target) {
    updateConnection();

    ArrayList<ServiceRequest> searchedRequests = new ArrayList<>();
    try {
      PreparedStatement stmt =
              connection.prepareStatement("SELECT requestID FROM SERVICEREQUEST WHERE targetLocationID=?");
      stmt.setString(1, target.getNodeID());
      ResultSet rset = stmt.executeQuery();

      while(rset.next()) {
        String requestID = rset.getString("requestID");
        searchedRequests.add(serviceRequests.get(requestID));
      }

    } catch (SQLException e) {
      System.out.println("Failed to query service request table");
      return searchedRequests;
    }

    return searchedRequests;
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
      serviceRequests.put(request.getRequestID(), request);
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
    serviceRequests.remove(request.getRequestID());
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
      stmt.setString(2, request.getHandler().getEmployeeID());
      stmt.setString(3, request.getRequestID());

      stmt.executeUpdate();
      connection.commit();
      serviceRequests.remove(request.getRequestID());
      serviceRequests.put(request.getRequestID(), request);
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
    csvController.writeServiceRequestCSV(getAllServiceRequests());
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
    //serviceRequestData = new File(System.getProperty("user.dir") + "\\employee.csv");
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

          serviceRequests.put(info.getRequestID(), info);
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

  /**
   * Gets the ServiceRequests in the given locations
   *
   * @param loc location of service requests
   * @return ServiceRequest at that location
   */
  public List<ServiceRequest> getServiceRequestsByLocation(Location loc) {
    updateConnection();
    List<ServiceRequest> listServiceRequest = new ArrayList<>();
    try {
      PreparedStatement pstmt =
          connection.prepareStatement("Select * From SERVICEREQUEST WHERE TARGETLOCATIONID = ?");
      pstmt.setString(1, loc.getNodeID());
      ResultSet rset = pstmt.executeQuery();
      while (rset.next()) {
        String requestID = rset.getString("requestID");
        String typeStr = rset.getString("type");
        String statusStr = rset.getString("status");
        String issuerID = rset.getString("issuerID");
        String handlerID = rset.getString("handlerID");
        String targetLocationID = rset.getString("targetLocationID");

        listServiceRequest.add(
            new ServiceRequest(
                requestID,
                ServiceRequest.RequestType.getRequestTypeByString(typeStr),
                ServiceRequest.RequestStatus.getRequestStatusByString(statusStr),
                issuerID,
                handlerID,
                targetLocationID));
      }
    } catch (SQLException e) {
      System.out.println("Failed to get ServiceRequest by location");
      e.printStackTrace();
    }
    return listServiceRequest;
  }
}
