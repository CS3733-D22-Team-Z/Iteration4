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

  /** Writes the current database to a .csv file */
  void exportToServiceRequestCSV();

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

  /**
   * Gets the ServiceRequests in the given locations
   *
   * @param loc location of service requests
   * @return ServiceRequest at that location
   */
  List<ServiceRequest> getServiceRequestsByLocation(Location loc);
}
