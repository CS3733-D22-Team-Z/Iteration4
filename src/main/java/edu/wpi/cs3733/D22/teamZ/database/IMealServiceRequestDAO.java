package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.MealServiceRequest;
import java.io.File;
import java.util.List;

public interface IMealServiceRequestDAO {
  /**
   * Gets all MealServiceRequests from database
   *
   * @return list of MedicalEquipmentRequests
   */
  List<MealServiceRequest> getAllMealServiceReq();

  /**
   * Gets a MealServiceRequest of the given requestID
   *
   * @param requestID ID of request to be fetched
   * @return MealServiceRequest of given ID, null if not found
   */
  MealServiceRequest getMealServReqByID(String requestID);

  /**
   * Adds a MealServiceRequest to the database
   *
   * @param req MealServiceRequest to be added
   * @return True if successful, false otherwise
   */
  boolean addMealServReq(MealServiceRequest req);

  /**
   * Updates an existing MealServiceRequest in the database with the given request
   *
   * @param req MealServiceRequest with updated information
   * @return True if successful, false otherwise
   */
  boolean updateMealServReq(MealServiceRequest req);

  /**
   * Deletes MealServiceRequest from database
   *
   * @param req MealServiceRequest to be deleted
   * @return True if successful, false otherwise
   */
  boolean deleteMealServReq(MealServiceRequest req);

  /**
   * Exports the MealServiceRequest database to specified file location for csv
   *
   * @param data file location for csv
   * @return True if successful, false otherwise
   */
  boolean exportToMealServReqCSV(File data);

  /**
   * Imports MealServiceRequest into database from specified file location for csv
   *
   * @param data file location of csv
   * @return number of conflicts during import
   */
  int importMealServReqFromCSV(File data);

  /**
   * Adds MealServiceRequest into database from list
   *
   * @param list Request to be added
   * @return True if successful, false otherwise
   */
  boolean addMealServReqFromList(List<MealServiceRequest> list);
}
