package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import java.io.File;
import java.util.List;

public interface IMedicalEquipmentDAO {
  /**
   * Get all MedicalEquipment in the database
   *
   * @return list of MedicalEquipment
   */
  List<MedicalEquipment> getAllMedicalEquipment();

  /**
   * Get MedicalEquipment with the given ID
   *
   * @param itemID ID of MedicalEquipment to be fetched
   * @return MedicalEquipment with the given ID
   */
  MedicalEquipment getMedicalEquipmentByID(String itemID);

  /**
   * Gets all MedicalEquipment in a given location
   *
   * @param location Location to extract MedicalEquipment inside
   * @return list of MedicalEquipment in the given location
   */
  List<MedicalEquipment> getAllMedicalEquipmentByLocation(Location location);

  /**
   * Get the first avalable equipment with the given equipment type
   *
   * @param equipment type of equipment
   * @return equipmentID of the first available equipment of the given type
   */
  String getFirstAvailableEquipmentByType(String equipment);

  /**
   * Adds MedicalEquipment to the database
   *
   * @param equipment MedicalEquipment to be added
   * @return True if successful, false otherwise
   */
  boolean addMedicalEquipment(MedicalEquipment equipment);

  /**
   * Updates existing MedicalEquipment in the database with an updated MedicalEquipment
   *
   * @param equipment Updated MedicalEquipment
   * @return True if successful, false otherwise
   */
  boolean updateMedicalEquipment(MedicalEquipment equipment);

  /**
   * Deletes MedicalEquipment in the database
   *
   * @param equipment MedicalEquipment to be deleted
   * @return True if successful, false otherwise
   */
  boolean deleteMedicalEquipment(MedicalEquipment equipment);

  /**
   * Exports the MedicalEquipment in the database to the specified file location of csv
   *
   * @param equipmentData File location of csv
   * @return True if successful, false otherwise
   */
  boolean exportToMedicalEquipmentCSV(File equipmentData);

  /**
   * Imports the MedicalEquipment into the database from specified file location for csv
   *
   * @param equipmentData file location for csv
   * @return number of conflicts when importing
   */
  int importMedicalEquipmentFromCSV(File equipmentData);

  /**
   * Insert Medical Equipment into database from list
   *
   * @param list list of medical equipment to be added
   * @return True if successful, false otherwise
   */
  boolean addMedicalEquipmentFromList(List<MedicalEquipment> list);

  /**
   * Get all Medical Equipment in given floor
   *
   * @param floor floor to be searched
   * @return list of medical equipment for given floor
   */
  List<MedicalEquipment> getAllMedicalEquipmentByFloor(String floor);

  /**
   * Get dirty equipment for the specified floor
   *
   * @param floor floor to be searched
   * @return number of dirty equipment
   */
  int countDirtyEquipmentByFloor(String floor);

  /**
   * Get clean equipment for the specified floor
   *
   * @param floor floor to be searched
   * @return number of clean equipment
   */
  int countCleanEquipmentByFloor(String floor);
}
