package edu.wpi.cs3733.D22.teamZ.database;

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
  private final List<ServiceRequest> serviceRequestList;
  private final ServiceRequestControlCSV csvController;

  private static Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();
  // DatabaseConnection.getConnection();

  public ServiceRequestDAOImpl() {
    updateConnection();
    serviceRequestList = new ArrayList<>();

    File serviceRequestData =
        new File(
            System.getProperty("user.dir")
                + System.getProperty("file.separator")
                + "ServiceRequest.csv");

    csvController = new ServiceRequestControlCSV(serviceRequestData);
  }

  /**
   * Returns all the service requests currently stored
   *
   * @return A List of all ServiceRequest objects in the database
   */
  @Override
  public List<ServiceRequest> getAllServiceRequests() {
    // ArrayList<ServiceRequest> requestsList = new ArrayList<>(serviceRequests.values());
    return serviceRequestList;
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
    int i = 0;
    for (ServiceRequest req : serviceRequestList) {
      if (req.getRequestID().equals(serviceRequestID)) {
        return serviceRequestList.get(i);
      }
      i++;
    }
    return null;
  }

  /**
   * Returns a list of ServiceRequest objects stored in the database that are located in the given
   * target location
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
          connection.prepareStatement(
              "SELECT requestID FROM SERVICEREQUEST WHERE targetLocationID=?");
      stmt.setString(1, target.getNodeID());
      ResultSet rset = stmt.executeQuery();

      while (rset.next()) {
        String requestID = rset.getString("requestID");
        int i = 0;
        for (ServiceRequest req : serviceRequestList) {
          if (req.getRequestID().equals(requestID)) {
            searchedRequests.add(serviceRequestList.get(i));
          }
          i++;
        }
        // searchedRequests.add(serviceRequests.get(requestID));
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
   * @return True if success, false otherwise
   */
  @Override
  public boolean updateServiceRequest(ServiceRequest request) {
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
      // cannot simply delete then edit
      for (ServiceRequest req : serviceRequestList) {
        if (req.equals(request)) {
          req.setStatus(request.getStatus());
          req.setHandler(request.getHandler());
          req.setClosed(request.getClosed());
          return true;
        }
      }
      // serviceRequestList.remove(request);
      // serviceRequestList.add(request);
      //      serviceRequests.remove(request.getRequestID());
      //      serviceRequests.put(request.getRequestID(), request);
      return true;
    } catch (SQLException e) {
      System.out.println("Update failed");
      return false;
    }
  }

  /**
   * Returns the default path for a service request csv file to be saved
   *
   * @return The default path for a service request csv file to be saved
   */
  File getDefaultServiceRequestCSVPath() {
    return csvController.getDefaultPath();
  }

  /**
   * Writes the current database to a .csv file
   *
   * @return True if successful, false otherwise
   */
  @Override
  public boolean exportToServiceRequestCSV() {
    updateConnection();
    try {
      csvController.writeServiceRequestCSV(serviceRequestList);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * Writes the current database to a .csv file
   *
   * @param path The file path the csv will be written to
   * @return True if successful, false otherwise
   */
  @Override
  public boolean exportToServiceRequestCSV(File path) {
    updateConnection();
    try {
      csvController.writeServiceRequestCSV(serviceRequestList, path);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
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

    int conflictCounter = 0;
    try {
      List<ServiceRequest> tempServiceRequest =
          csvController.readServiceRequestCSV(serviceRequestData);
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
          serviceRequestList.add(info);
          // serviceRequests.put(info.getRequestID(), info);
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
                  + "(requestID, type, status, issuerID, handlerID, targetLocationID, opened, closed)"
                  + "values (?, ?, ?, ?, ?, ?, ?, ?)");
      stmt.setString(1, request.getRequestID());
      stmt.setString(2, request.getType().toString());
      stmt.setString(3, request.getStatus().toString());
      stmt.setString(4, request.getIssuer().getEmployeeID());
      if (request.getHandler() == null) {
        stmt.setString(5, null);
      } else {
        stmt.setString(5, request.getHandler().getEmployeeID());
      }
      stmt.setString(6, request.getTargetLocation().getNodeID());
      if (request.getOpened() == null) { // TODO delete since should always have start
        stmt.setString(7, null);
      } else {
        stmt.setString(7, request.getOpened().toString());
      }
      if (request.getClosed() == null) {
        stmt.setString(8, null);
      } else {
        stmt.setString(8, request.getClosed().toString());
      }

      stmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("Failed to add service request");
      return false;
    }
    return true;
  }

  /**
   * Gets the ServiceRequests in the given locations
   *
   * @param status
   * @return ServiceRequests of that status
   */
  public List<ServiceRequest> getServiceRequestsByStatus(ServiceRequest.RequestStatus status) {
    updateConnection();
    List<ServiceRequest> listServiceRequest = new ArrayList<>();
    String stat = status.toString();
    try {
      PreparedStatement pstmt =
          connection.prepareStatement("Select * From SERVICEREQUEST WHERE STATUS = ?");
      pstmt.setString(1, stat);
      ResultSet rset = pstmt.executeQuery();
      while (rset.next()) {
        String requestID = rset.getString("requestID");
        String typeStr = rset.getString("type");
        String statusStr = rset.getString("status");
        String issuerID = rset.getString("issuerID");
        String handlerID = rset.getString("handlerID");
        String targetLocationID = rset.getString("targetLocationID");
        String opened = rset.getString("opened");
        String closed = rset.getString("closed");

        listServiceRequest.add(
            new ServiceRequest(
                requestID,
                ServiceRequest.RequestType.getRequestTypeByString(typeStr),
                ServiceRequest.RequestStatus.getRequestStatusByString(statusStr),
                issuerID,
                handlerID,
                targetLocationID,
                opened,
                closed));
      }
    } catch (SQLException e) {
      System.out.println("Failed to get ServiceRequest by status");
      e.printStackTrace();
    }
    return listServiceRequest;
  }
}
