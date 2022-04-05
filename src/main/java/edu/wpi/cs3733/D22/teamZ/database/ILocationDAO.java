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
   * Gets ONE lcoation from the database based on the provided nodeID
   *
   * @param nodeID
   * @return Location object with provided nodeID
   */
  Location getLocationByID(String nodeID);

  /**
   * Adds a new location to database. Will automatically check if already in database
   *
   * @param loc
   * @return True if successful, false if not
   */
  boolean addLocation(Location loc);

  /**
   * Updates a location in the database. Will automatically check if exists in database
   *
   * @param loc
   * @return True if successful, false if not
   */
  boolean updateLocation(Location loc);

  /**
   * Deletes a location from database. Will automatically check if exists in database already
   *
   * @param loc
   * @return True if successful, false if not
   */
  boolean deleteLocation(Location loc);

  /**
   * Exports the Location table into a csv file to the working directory
   *
   * @return True if successful, false if not
   */
  boolean exportToLocationCSV(File locData);

  /**
   * Gets all locations on the given floor
   *
   * @param floor
   * @return list of locations
   */
  List<Location> getAllLocationsByFloor(String floor);
}
