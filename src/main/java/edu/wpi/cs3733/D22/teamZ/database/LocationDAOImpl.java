package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Location;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDAOImpl implements ILocationDAO {

  private List<Location> locations;
  private LocationControlCSV locCSV;

  private static Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();
  // DatabaseConnection.getConnection();

  public LocationDAOImpl() {
    updateConnection();
    locations = new ArrayList<Location>();
  }

  /**
   * Gets all of the locations in the database
   *
   * @return List of locations
   */
  public List<Location> getAllLocations() {
    updateConnection();
    try {
      PreparedStatement pstmt = connection.prepareStatement("Select * From Location");
      ResultSet rset = pstmt.executeQuery();

      locations.clear();

      while (rset.next()) {
        String nodeID = rset.getString("nodeID");
        int xcoord = rset.getInt("xcoord");
        int ycoord = rset.getInt("ycoord");
        String floor = rset.getString("floor");
        String building = rset.getString("building");
        String nodeType = rset.getString("nodeType");
        String longName = rset.getString("longName");
        String shortName = rset.getString("shortName");
        Location loc =
            new Location(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName);
        if (!locations.contains(loc)) {
          locations.add(loc);
        }
      }
    } catch (SQLException e) {
      System.out.println("Failed to get all Locations");
    }
    return locations;
  }

  /**
   * Gets all the nodeIDs for the locations in database
   *
   * @return list of nodeIDs
   */
  public List<String> getAllLocationNodeIDs() {
    updateConnection();
    List<String> list = new ArrayList<>();
    try {
      PreparedStatement pstmt = connection.prepareStatement("Select NODEID From Location");
      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        list.add(rset.getString("NODEID"));
      }

    } catch (SQLException e) {
      System.out.println("Failed to get nodeIDs from database");
    }
    return list;
  }

  /**
   * Gets ONE lcoation from the database based on the provided nodeID
   *
   * @param nodeID
   * @return Location object with provided nodeID
   */
  public Location getLocationByID(String nodeID) {
    updateConnection();
    Location loc = new Location();
    try {
      PreparedStatement pstmt =
          connection.prepareStatement("Select * From LOCATION WHERE NODEID = ?");
      pstmt.setString(1, nodeID);
      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        int xcoord = rset.getInt("xcoord");
        int ycoord = rset.getInt("ycoord");
        String floor = rset.getString("floor");
        String building = rset.getString("building");
        String nodeType = rset.getString("nodeType");
        String longName = rset.getString("longName");
        String shortName = rset.getString("shortName");
        loc.setNodeID(nodeID);
        loc.setXcoord(xcoord);
        loc.setYcoord(ycoord);
        loc.setFloor(floor);
        loc.setBuilding(building);
        loc.setNodeType(nodeType);
        loc.setLongName(longName);
        loc.setShortName(shortName);
      }
    } catch (SQLException e) {
      System.out.println("Unable to find location");
    }
    return loc;
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
      locations.add(loc);
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
    locations.remove(getLocationByID(loc.getNodeID()));
    locations.add(loc);
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
    locations.remove(loc);
    return true;
  }

  /**
   * Exports the Location table into a csv file to the working directory
   *
   * @return True if successful, false if not
   */
  public boolean exportToLocationCSV(File locData) {
    updateConnection();

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
          connection.prepareStatement("Select * from LOCATION WHERE FLOOR = ?");
      pstmt.setString(1, floor);

      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        Location tempLoc = new Location();
        tempLoc.setNodeID(rset.getString("nodeID"));
        tempLoc.setXcoord(rset.getInt("xcoord"));
        tempLoc.setYcoord(rset.getInt("ycoord"));
        tempLoc.setFloor(floor);
        tempLoc.setBuilding(rset.getString("building"));
        tempLoc.setNodeType(rset.getString("nodeType"));
        tempLoc.setLongName(rset.getString("longName"));
        tempLoc.setShortName(rset.getString("shortName"));

        temp.add(tempLoc);
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
          connection.prepareStatement("Select * from LOCATION WHERE NODETYPE = ?");
      pstmt.setString(1, type);

      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        Location tempLoc = new Location();
        tempLoc.setNodeID(rset.getString("nodeID"));
        tempLoc.setXcoord(rset.getInt("xcoord"));
        tempLoc.setYcoord(rset.getInt("ycoord"));
        tempLoc.setFloor(rset.getString("floor"));
        tempLoc.setBuilding(rset.getString("building"));
        tempLoc.setNodeType(rset.getString("nodeType"));
        tempLoc.setLongName(rset.getString("longName"));
        tempLoc.setShortName(rset.getString("shortName"));

        tempList.add(tempLoc);
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
