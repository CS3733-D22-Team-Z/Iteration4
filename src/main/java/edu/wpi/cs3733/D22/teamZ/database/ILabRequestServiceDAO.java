package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.LabServiceRequest;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public interface ILabRequestServiceDAO {
  /**
   * Gets all lab service requests
   *
   * @return list of lab service requests
   * @throws SQLException
   */
  List<LabServiceRequest> getAllLabServiceRequests() throws SQLException;

  /**
   * Get a LabServiceRequest with provided requestID
   *
   * @param requestID
   * @return LabServiceRequest object with given ID
   */
  LabServiceRequest getLabRequestByID(String requestID);

  /**
   * Adds a LabServiceRequest to the database
   *
   * @param request LabServiceRequest to be added
   * @return True if successful, false otherwise
   */
  boolean addLabRequest(LabServiceRequest request);

  /**
   * Updates an existing LabServiceRequest in database with new request
   *
   * @param request LabServiceRequest to be updated
   * @return True if successful, false otherwise
   */
  boolean updateLabRequest(LabServiceRequest request);

  /**
   * Deletes a LabServiceRequest from the database
   *
   * @param request LabServiceRequest to be deleted
   * @return True if successful, false otherwise
   */
  boolean deleteLabRequest(LabServiceRequest request);

  /**
   * Exports all LabServiceRequests in the database to specified file location of csv
   *
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  boolean exportToLabRequestCSV(File data);

  /**
   * Imports all LabServiceRequests in specified file location of csv into the database
   *
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  int importLabRequestFromCSV(File data);

  /**
   * Inserts lab requests into database from given list
   *
   * @param list list of lab requests to be added
   * @return true if successful, false otherwise
   */
  boolean addLabRequestFromList(List<LabServiceRequest> list);

}
