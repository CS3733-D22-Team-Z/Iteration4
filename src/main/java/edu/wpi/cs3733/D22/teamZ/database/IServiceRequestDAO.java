package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
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

  /** Writes the current database to a .csv file */
  void writeServiceRequestsToCSV();
}
