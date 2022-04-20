package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.LaundryServiceRequest;
import java.io.File;
import java.util.List;

public interface ILaundryServiceDAO {
  /**
   * Gets all equipment purchase requests
   *
   * @return list of equipment purchase requests
   */
  List<LaundryServiceRequest> getAllLaundryServiceRequests();

  /**
   * Get a EquipmentPurchaseRequest with provided requestID
   *
   * @param requestID ID to find
   * @return EquipmentPurchaseRequest object with given ID
   */
  LaundryServiceRequest getLaundryRequestById(String requestID);

  /**
   * Adds EquipmentPurchaseRequest to the database
   *
   * @param request EquipmentPurchaseRequest to be added
   * @return True if successful, false otherwise
   */
  boolean addLaundryServiceRequest(LaundryServiceRequest request);

  /**
   * Updates an existing EquipmentPurchaseRequest in database with new request
   *
   * @param request EquipmentPurchaseRequest to be updated
   * @return True if successful, false otherwise
   */
  boolean updateLaundryServiceRequest(LaundryServiceRequest request);

  /**
   * Deletes an EquipmentPurchaseRequest from the database
   *
   * @param request EquipmentPurchaseRequest to be deleted
   * @return True if successful, false otherwise
   */
  boolean deleteLaundryServiceRequest(LaundryServiceRequest request);

  /**
   * Exports all EquipmentPurchaseRequest in the database to specified file location of csv
   *
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  boolean exportToLaundryServiceRequestCSV(File data);

  /**
   * Imports all EquipmentPurchaseRequests in specified file location of csv into the database
   *
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  int importLaundryServiceRequestFromCSV(File data);

  /**
   * Inserts lab requests into database from given list
   *
   * @param list list of lab requests to be added
   * @return true if successful, false otherwise
   */
  boolean addLaundryServiceRequestFromList(List<LaundryServiceRequest> list);
}
