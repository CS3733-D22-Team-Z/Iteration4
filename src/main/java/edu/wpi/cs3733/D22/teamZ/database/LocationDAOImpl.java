package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Location;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class LocationDAOImpl implements ILocationDAO {

  private HashMap<String, Location> locations;
  private LocationControlCSV locCSV;

  private static Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();

  public LocationDAOImpl() {
    updateConnection();
    locations = new HashMap<String, Location>();
  }

  /**
   * Gets all the locations in the database
   *
   * @return List of locations
   */
  public List<Location> getAllLocations() {
    List<Location> locationList = new ArrayList<>(locations.values());
    return locationList;
  }

  /**
   * Gets all the nodeIDs for the locations in database
   *
   * @return list of nodeIDs
   */
  public List<String> getAllLocationNodeIDs() {
    List<String> nodeIDList = new ArrayList<>(locations.keySet());
    return nodeIDList;
  }

  /**
   * Gets the location from the database that has the provided nodeID
   *
   * @param nodeID
   * @return Location object with provided nodeID, null if there is no location with that ID
   */
  public Location getLocationByID(String nodeID) {
    return locations.get(nodeID);
  }
  /**
   * Adds a new location to database. Will automatically check if already in database
   *
   * @param loc
   * @return True if successful, false if not
   */
  public boolean addLocation(Location loc) {
    updateConnection();
    boolean val = false;
    if (addToDatabase(loc)) {
      val = true;
      locations.put(loc.getNodeID(), loc);
    }
    return val;
  }

  /**
   * Updates a location in the database. Will automatically check if exists in database
   *
   * @param loc
   * @return True if successful, false if not
   */
  public boolean updateLocation(Location loc) {
    updateConnection();
    try {
      PreparedStatement stmt =
          connection.prepareStatement("UPDATE Location SET floor=?, nodeTYPE =? WHERE nodeID =?");
      stmt.setString(1, loc.getFloor());
      stmt.setString(2, loc.getNodeType());
      stmt.setString(3, loc.getNodeID());

      stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Statement failed");
      return false;
    }
    locations.remove(loc.getNodeID());
    locations.put(loc.getNodeID(), loc);
    return true;
  }

  /**
   * Deletes a location from database. Will automatically check if exists in database already
   *
   * @param loc
   * @return True if successful, false if not
   */
  public boolean deleteLocation(Location loc) {
    updateConnection();
    try {
      PreparedStatement stmt3 = connection.prepareStatement("DELETE FROM Location WHERE Nodeid=?");
      stmt3.setString(1, loc.getNodeID());
      stmt3.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Statement failed");
      return false;
    }
    locations.remove(loc.getNodeID());
    return true;
  }

  /**
   * Exports the Location table into a csv file to the working directory
   *
   * @return True if successful, false if not
   */
  public boolean exportToLocationCSV(File locData) {
    // File locData = new File(System.getProperty("user.dir") + "\\TowerLocations.csv");
    locCSV = new LocationControlCSV(locData);
    locCSV.writeLocCSV(getAllLocations());

    return true;
  }

  /**
   * Gets all locations on the given floor
   *
   * @param floor
   * @return list of locations
   */
  @Override
  public List<Location> getAllLocationsByFloor(String floor) {
    updateConnection();
    List<Location> temp = new ArrayList<>();
    try {
      PreparedStatement pstmt =
          connection.prepareStatement("Select nodeID from LOCATION WHERE FLOOR = ?");
      pstmt.setString(1, floor);

      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        String nodeID = rset.getString("nodeID");
        temp.add(locations.get(nodeID));
      }
    } catch (SQLException e) {
      System.out.println("Failed to get locations");
    }
    return temp;
  }

  /**
   * Gets all locations of the given type
   *
   * @param type type of location
   * @return list of locations of given type
   */
  @Override
  public List<Location> getALlLocationsByType(String type) {
    updateConnection();
    List<Location> tempList = new ArrayList<>();
    try {
      PreparedStatement pstmt =
          connection.prepareStatement("Select nodeID from LOCATION WHERE NODETYPE = ?");
      pstmt.setString(1, type);

      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        String nodeID = rset.getString("nodeID");
        tempList.add(locations.get(nodeID));
      }
    } catch (SQLException e) {
      System.out.println("Failed to get locations");
    }
    return tempList;
  }

  /**
   * Imports data from CSV into location database
   *
   * @param locData
   * @return number of times there are conflicts when trying to import
   */
  @Override
  public int importLocationFromCSV(File locData) {
    updateConnection();

    int numberConflicts = 0;
    try {
      locCSV = new LocationControlCSV(locData);
      List<Location> tempLoc = locCSV.readLocCSV();

      List<String> newLocations = new ArrayList<>();

      for (int i = 0; i < tempLoc.size(); i++) {
        newLocations.add(tempLoc.get(i).getNodeID());
      }

      List<String> currentNodeIDs = getAllLocationNodeIDs();

      currentNodeIDs.removeAll(newLocations);

      // TODO if you found something that cannot be deleted, abort completely

      for (String id : currentNodeIDs) {
        // delete from database
        try {
          PreparedStatement pstmt =
              connection.prepareStatement("DELETE from LOCATION WHERE NODEID = ?");
          pstmt.setString(1, id);
          pstmt.executeUpdate();

          locations.remove(id);
        } catch (SQLException e) {
          numberConflicts++;
          System.out.println(
              numberConflicts
                  + " found. "
                  + id
                  + " cannot delete from database since"
                  + " some stuff might still be in it.");
        }
      }

      for (Location newInfo : tempLoc) {
        Location temp = getLocationByID(newInfo.getNodeID());
        // if new: insert
        if (temp.getNodeID() == null) {
          PreparedStatement pstmt =
              connection.prepareStatement(
                  "INSERT INTO Location (nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName) values (?, ?, ?, ?, ?, ?, ?, ?)");
          pstmt.setString(1, newInfo.getNodeID());
          pstmt.setInt(2, newInfo.getXcoord());
          pstmt.setInt(3, newInfo.getYcoord());
          pstmt.setString(4, newInfo.getFloor());
          pstmt.setString(5, newInfo.getBuilding());
          pstmt.setString(6, newInfo.getNodeType());
          pstmt.setString(7, newInfo.getLongName());
          pstmt.setString(8, newInfo.getShortName());

          // insert it
          pstmt.executeUpdate();

          locations.put(newInfo.getNodeID(), newInfo);
        }
        // if already exists: update
        else {
          PreparedStatement pstmt =
              connection.prepareStatement(
                  "UPDATE  LOCATION SET "
                      + "XCOORD = ?,"
                      + "YCOORD = ?,"
                      + "FLOOR = ?,"
                      + "BUILDING = ?,"
                      + "NODETYPE = ?,"
                      + "LONGNAME = ?,"
                      + "SHORTNAME = ?"
                      + "WHERE NODEID = ?");
          pstmt.setInt(1, newInfo.getXcoord());
          pstmt.setInt(2, newInfo.getYcoord());
          pstmt.setString(3, newInfo.getFloor());
          pstmt.setString(4, newInfo.getBuilding());
          pstmt.setString(5, newInfo.getNodeType());
          pstmt.setString(6, newInfo.getLongName());
          pstmt.setString(7, newInfo.getShortName());
          pstmt.setString(8, newInfo.getNodeID());

          // update it
          pstmt.executeUpdate();

          locations.remove(newInfo.getNodeID());
          locations.put(newInfo.getNodeID(), newInfo);
        }
      }
    } catch (SQLException e) {
      System.out.println("Failed to load LOCATION data");
      return -1;
    } catch (IOException e) {
      System.out.println("Failed to read CSV");
      return -1;
    }
    return numberConflicts;
  }

  /** Updates the connection */
  private void updateConnection() {
    connection = EnumDatabaseConnection.CONNECTION.getConnection();
  }

  /**
   * Insert locations into the database from given list
   *
   * @param list list of locations to be added
   * @return true if successful, false otherwise
   */
  public boolean addLocationFromList(List<Location> list) {
    updateConnection();
    boolean val = true;
    for (Location loc : list) {
      if (!addToDatabase(loc)) {
        val = false;
      }
    }
    return val;
  }

  /**
   * Contains the SQL command for inserting to database
   *
   * @param loc location to be inserted
   * @return True if successful, false otherwise
   */
  private boolean addToDatabase(Location loc) {
    try {
      PreparedStatement stmt =
          connection.prepareStatement(
              "INSERT INTO Location (NODEID, XCOORD, YCOORD, FLOOR, BUILDING, NODETYPE, LONGNAME, SHORTNAME)"
                  + "values (?, ?, ?, ?, ?, ?, ?, ?)");
      stmt.setString(1, loc.getNodeID());
      stmt.setInt(2, loc.getXcoord());
      stmt.setInt(3, loc.getYcoord());
      stmt.setString(4, loc.getFloor());
      stmt.setString(5, loc.getBuilding());
      stmt.setString(6, loc.getNodeType());
      stmt.setString(7, loc.getLongName());
      stmt.setString(8, loc.getShortName());

      stmt.executeUpdate();
      connection.commit();
    } catch (SQLException e) {
      System.out.println("Statement failed");
      return false;
    }
    return true;
  }
}
