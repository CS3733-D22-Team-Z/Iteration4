package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.EquipmentPurchaseRequest;
import java.io.File;
import java.util.List;

public interface IEquipmentPurchaseDAO {
  /**
   * Gets all equipment purchase requests
   *
   * @return list of equipment purchase requests
   */
  List<EquipmentPurchaseRequest> getAllEquipmentPurchaseRequests();

  /**
   * Get a EquipmentPurchaseRequest with provided requestID
   *
   * @param requestID ID to find
   * @return EquipmentPurchaseRequest object with given ID
   */
  EquipmentPurchaseRequest getEquipmentPurchaseRequestByID(String requestID);

  /**
   * Adds EquipmentPurchaseRequest to the database
   *
   * @param request EquipmentPurchaseRequest to be added
   * @return True if successful, false otherwise
   */
  boolean addEquipmentPurchaseRequest(EquipmentPurchaseRequest request);

  /**
   * Updates an existing EquipmentPurchaseRequest in database with new request
   *
   * @param request EquipmentPurchaseRequest to be updated
   * @return True if successful, false otherwise
   */
  boolean updateEquipmentPurchaseRequest(EquipmentPurchaseRequest request);

  /**
   * Deletes an EquipmentPurchaseRequest from the database
   *
   * @param request EquipmentPurchaseRequest to be deleted
   * @return True if successful, false otherwise
   */
  boolean deleteEquipmentPurchaseRequest(EquipmentPurchaseRequest request);

  /**
   * Exports all EquipmentPurchaseRequest in the database to specified file location of csv
   *
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  boolean exportToEquipmentPurchaseRequestCSV(File data);

  /**
   * Imports all EquipmentPurchaseRequests in specified file location of csv into the database
   *
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  int importEquipmentPurchaseRequestFromCSV(File data);

  /**
   * Inserts lab requests into database from given list
   *
   * @param list list of lab requests to be added
   * @return true if successful, false otherwise
   */
  boolean addEquipmentPurchaseRequestFromList(List<EquipmentPurchaseRequest> list);
}
