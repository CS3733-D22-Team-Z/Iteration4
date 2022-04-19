package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.CleaningRequest;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public interface ICleaningRequestDAO {
  /**
   * Gets all cleaning service requests
   *
   * @return list of cleaning service requests
   * @throws SQLException
   */
  List<CleaningRequest> getAllCleaningServiceRequests() throws SQLException;

  /**
   * Get a CleaningServiceRequest with provided requestID
   *
   * @param requestID
   * @return CleaningRequest object with given ID
   */
  CleaningRequest getCleaningRequestByID(String requestID);

  /**
   * Adds a LabServiceRequest to the database
   *
   * @param request CleaningRequest to be added
   * @return True if successful, false otherwise
   */
  boolean addCleaningRequest(CleaningRequest request);

  /**
   * Updates an existing CleaningRequest in database with new request
   *
   * @param request CleaningRequest to be updated
   * @return True if successful, false otherwise
   */
  boolean updateCleaningRequest(CleaningRequest request);

  /**
   * Deletes a CleaningRequest from the database
   *
   * @param request CleaningRequest to be deleted
   * @return True if successful, false otherwise
   */
  boolean deleteCleaningRequest(CleaningRequest request);

  /**
   * Exports all CleaningRequests in the database to specified file location of csv
   *
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  boolean exportToCleaningRequestCSV(File data);

  /**
   * Imports all CleaningServiceRequests in specified file location of csv into the database
   *
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  int importCleaningRequestFromCSV(File data);

  /**
   * Inserts cleaning requests into database from given list
   *
   * @param list list of cleaning requests to be added
   * @return true if successful, false otherwise
   */
  boolean addCleaningRequestFromList(List<CleaningRequest> list);
}
