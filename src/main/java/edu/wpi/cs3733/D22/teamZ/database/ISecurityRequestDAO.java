package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.SecurityServiceRequest;
import java.io.File;
import java.util.List;

public interface ISecurityRequestDAO {
  /**
   * Gets all security requests
   *
   * @return list of security requests
   */
  List<SecurityServiceRequest> getAllSecurityServiceRequests();

  /**
   * Get a SecurityServiceRequest with provided requestID
   *
   * @param requestID ID to find
   * @return SecurityServiceRequest object with given ID
   */
  SecurityServiceRequest getSecurityServiceRequestByID(String requestID);

  /**
   * Adds SecurityServiceRequest to the database
   *
   * @param request SecurityServiceRequest to be added
   * @return True if successful, false otherwise
   */
  boolean addSecurityServiceRequest(SecurityServiceRequest request);

  /**
   * Updates an existing SecurityServiceRequest in database with new request
   *
   * @param request SecurityServiceRequest to be updated
   * @return True if successful, false otherwise
   */
  boolean updateSecurityServiceRequest(SecurityServiceRequest request);

  /**
   * Deletes an SecurityServiceRequest from the database
   *
   * @param request SecurityServiceRequest to be deleted
   * @return True if successful, false otherwise
   */
  boolean deleteSecurityServiceRequest(SecurityServiceRequest request);

  /**
   * Exports all SecurityServiceRequest in the database to specified file location of csv
   *
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  boolean exportToSecurityServiceRequestCSV(File data);

  /**
   * Imports all SecurityServiceRequest in specified file location of csv into the database
   *
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  int importSecurityServiceRequestFromCSV(File data);

  /**
   * Inserts SecurityServiceRequest into database from given list
   *
   * @param list list of SecurityServiceRequest to be added
   * @return true if successful, false otherwise
   */
  boolean addSecurityServiceRequestFromList(List<SecurityServiceRequest> list);
}
