package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.ComputerServiceRequest;
import java.io.File;
import java.util.List;

public interface IComputerServiceRequestDAO {
  /**
   * Gets all computer service requests
   *
   * @return list of computer service requests
   */
  List<ComputerServiceRequest> getAllComputerServiceRequests();

  /**
   * Get a ComputerServiceRequest with provided requestID
   *
   * @param requestID ID to find
   * @return ComputerServiceRequest object with given ID
   */
  ComputerServiceRequest getComputerServiceRequestByID(String requestID);

  /**
   * Adds ComputerServiceRequest to the database
   *
   * @param request ComputerServiceRequest to be added
   * @return True if successful, false otherwise
   */
  boolean addComputerServiceRequest(ComputerServiceRequest request);

  /**
   * Updates an existing ComputerServiceRequest in database with new request
   *
   * @param request ComputerServiceRequest to be updated
   * @return True if successful, false otherwise
   */
  boolean updateComputerServiceRequest(ComputerServiceRequest request);

  /**
   * Deletes an ComputerServiceRequest from the database
   *
   * @param request ComputerServiceRequest to be deleted
   * @return True if successful, false otherwise
   */
  boolean deleteComputerServiceRequest(ComputerServiceRequest request);

  /**
   * Exports all ComputerServiceRequest in the database to specified file location of csv
   *
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  boolean exportToComputerServiceRequestCSV(File data);

  /**
   * Imports all ComputerServiceRequest in specified file location of csv into the database
   *
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  int importComputerServiceRequestFromCSV(File data);

  /**
   * Inserts ComputerServiceRequest into database from given list
   *
   * @param list list of ComputerServiceRequest to be added
   * @return true if successful, false otherwise
   */
  boolean addComputerServiceRequestFromList(List<ComputerServiceRequest> list);
}
