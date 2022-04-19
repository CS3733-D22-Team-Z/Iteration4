package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.util.List;

public interface IServiceRequestDAO {
  /**
   * Returns all the service requests currently stored
   *
   * @return A List of all ServiceRequest objects in the database
   */
  List<ServiceRequest> getAllServiceRequests();

  /**
   * Returns a single ServiceRequest object that is stored in the database and has the id that is
   * provided
   *
   * @param serviceRequestID The id of the service request with the given id in the database
   * @return A ServiceRequest object representing the request in the database with the given
   *     serviceRequestID
   */
  ServiceRequest getServiceRequestByID(String serviceRequestID);

  /**
   * Returns a list of ServiceRequest objects stored in the database that are located in the given
   * target location
   *
   * @param target The location to search
   * @return A list of service requests that are located in the given location
   */
  List<ServiceRequest> getServiceRequestsByLocation(Location target);

  /**
   * Adds the given ServiceRequest object to the database
   *
   * @param request The request to be added
   * @return true if successfully added, false otherwise
   */
  boolean addServiceRequest(ServiceRequest request);

  /**
   * Takes a ServiceRequest object and deletes the respective one from the database with the same
   * requestID
   *
   * @param request The request to be deleted
   * @return true if the deletion was successful, false otherwise
   */
  boolean deleteServiceRequest(ServiceRequest request);

  /**
   * Update a ServiceRequest object in database and list of service requests
   *
   * @param request ServiceRequest object that stores updated information
   * @return True if success, false otherwise
   */
  boolean updateServiceRequest(ServiceRequest request);

  /**
   * Writes the current database to a .csv file
   *
   * @return True if successful, false otherwise
   */
  boolean exportToServiceRequestCSV();

  /**
   * Writes the current database to a .csv file
   *
   * @param path The file path the csv will be written to
   * @return True if successful, false otherwise
   */
  boolean exportToServiceRequestCSV(File path);

  /**
   * Import ServiceRequest to database from a specified file location for csv
   *
   * @param serviceRequestData file location for csv
   * @return number of conflicts when importing
   */
  int importServiceRequestsFromCSV(File serviceRequestData);

  /**
   * Insert service requests into the database from the given list
   *
   * @param list list of service requests to be added
   * @return True if successful, false otherwise
   */
  boolean addServiceRequestFromList(List<ServiceRequest> list);
}
