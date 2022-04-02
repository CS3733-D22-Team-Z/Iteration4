package edu.wpi.cs3733.D22.teamZ.database;

import java.util.List;

public interface ILocationDAO {
  List<Location> getAllLocations();

  Location getLocationByID(String nodeID);

  void addLocation(Location loc);

  void updateLocation(Location loc);

  void deleteLocation(Location loc);

  boolean exportToLocationCSV();
}
