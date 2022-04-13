package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipmentDeliveryRequest;
import java.io.File;
import java.util.List;

public interface IMedEquipReqDAO {
  /**
   * Gets all MedicalEquipmentRequests from database
   *
   * @return list of MedicalEquipmentRequests
   */
  List<MedicalEquipmentDeliveryRequest> getAllMedEquipReq();

  /**
   * Gets a MedicalEquipmentRequest of the given requestID
   *
   * @param requestID ID of request to be fetched
   * @return MedicalEquipmentRequest of given ID, null if not found
   */
  MedicalEquipmentDeliveryRequest getMedEquipReqByID(String requestID);

  /**
   * Adds a MedicalEquipmentRequest to the database
   *
   * @param req MedicalEquipmentRequest to be added
   * @return True if successful, false otherwise
   */
  boolean addMedEquipReq(MedicalEquipmentDeliveryRequest req);

  /**
   * Updates an existing MedicalEquipmentRequest in the database with the given request
   *
   * @param req MedicalEquipmentRequest with updated information
   * @return True if successful, false otherwise
   */
  boolean updateMedEquipReq(MedicalEquipmentDeliveryRequest req);

  /**
   * Deletes MedicalEquipmentRequest from database
   *
   * @param req MedicalEquipmentRequest to be deleted
   * @return True if successful, false otherwise
   */
  boolean deleteMedEquipReq(MedicalEquipmentDeliveryRequest req);

  /**
   * Exports the MedicalEquipmentRequest database to specified file location for csv
   *
   * @param data file location for csv
   * @return True if successful, false otherwise
   */
  boolean exportToMedEquipReqCSV(File data);

  /**
   * Imports MedicalEquipmentRequests into database from specified file location for csv
   *
   * @param data file location of csv
   * @return number of conflicts during import
   */
  int importMedEquipReqFromCSV(File data);

  /**
   * Adds MedicalEquipmentDeliveryRequest into database from list
   *
   * @param list Request to be added
   * @return True if successful, false otherwise
   */
  boolean addMedicalEquipReqFromList(List<MedicalEquipmentDeliveryRequest> list);
}
