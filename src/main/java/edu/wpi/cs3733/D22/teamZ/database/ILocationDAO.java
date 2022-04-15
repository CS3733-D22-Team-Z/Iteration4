package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Location;
import java.io.File;
import java.util.List;

public interface ILocationDAO {
  /**
   * Gets all of the locations in the database
   *
   * @return List of locations
   */
  List<Location> getAllLocations();

  /**
   * Gets all the nodeIDs for the locations in database
   *
   * @return list of nodeIDs
   */
  List<String> getAllLocationNodeIDs();

  /**
   * Gets ONE lcoation from the database based on the provided nodeID
   *
   * @param nodeID The id of the location to be searched for
   * @return Location object with provided nodeID
   */
  Location getLocationByID(String nodeID);

  /**
   * Adds a new location to database. Will automatically check if already in database
   *
   * @param loc The location to be added
   * @return True if successful, false if not
   */
  boolean addLocation(Location loc);

  /**
   * Updates a location in the database. Will automatically check if exists in database
   *
   * @param loc The location to be updated
   * @return True if successful, false if not
   */
  boolean updateLocation(Location loc);

  /**
   * Deletes a location from database. Will automatically check if exists in database already
   *
   * @param loc The location to be deleted
   * @return True if successful, false if not
   */
  boolean deleteLocation(Location loc);

  /**
   * Exports the Location table into a csv file to the working directory
   *
   * @return True if successful, false if not
   */
  boolean exportToLocationCSV();

  /**
   * Exports the Location table into a CSV at the given path
   *
   * @param locData The path the CSV will be written to
   * @return True if successful, false otherwise
   */
  boolean exportToLocationCSV(File locData);

  /**
   * Gets all locations on the given floor
   *
   * @param floor The floor to be searched
   * @return list of locations
   */
  List<Location> getAllLocationsByFloor(String floor);

  /**
   * Gets all locations of the given type
   *
   * @param type type of location
   * @return list of locations of the given type
   */
  List<Location> getALlLocationsByType(String type);

  /**
   * Imports data from CSV into location database
   *
   * @param locData The file path to the csv file that will be imported from
   * @return True if successful, false if not
   */
  int importLocationFromCSV(File locData);

  /**
   * Insert locations into the database from given list
   *
   * @param list list of locations to be added
   * @return true if successful, false otherwise
   */
  boolean addLocationFromList(List<Location> list);
}
