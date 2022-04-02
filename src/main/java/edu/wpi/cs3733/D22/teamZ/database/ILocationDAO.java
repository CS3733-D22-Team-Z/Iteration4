package edu.wpi.cs3733.D22.teamZ.database;

import java.util.List;

public interface ILocationDAO {
  List<Location> getAllLocations();

  Location getLocationByID(String nodeID);

  boolean addLocation(Location loc);

  boolean updateLocation(Location loc);

  boolean deleteLocation(Location loc);

  boolean exportToLocationCSV();
}
