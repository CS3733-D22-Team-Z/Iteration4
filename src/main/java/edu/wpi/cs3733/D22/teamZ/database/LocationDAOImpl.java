package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Location;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class LocationDAOImpl implements ILocationDAO {

  private final List<Location> locationsList;
  private final LocationControlCSV locCSV;
  private final EdgesControlCSV edgeCSV;

  private static Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();

  public LocationDAOImpl() {
    updateConnection();
    locationsList = new ArrayList<>();

    File locData = new File(System.getProperty("user.dir") + "\\TowerLocations.csv");
    File edgeData = new File(System.getProperty("user.dir") + "\\PathEdges.csv");

    locCSV = new LocationControlCSV(locData);
    edgeCSV = new EdgesControlCSV(edgeData);
  }

  /**
   * Gets all the locations in the database
   *
   * @return List of locations
   */
  public List<Location> getAllLocations() {
    // List<Location> locationList = new ArrayList<>(locations.values());
    return locationsList;
  }

  /**
   * Gets all the nodeIDs for the locations in database
   *
   * @return list of nodeIDs
   */
  public List<String> getAllLocationNodeIDs() {
    List<String> nodeIDList = new ArrayList<>();
    for (Location loc : locationsList) {
      nodeIDList.add(loc.getNodeID());
    }
    return nodeIDList;
  }

  /**
   * Gets the location from the database that has the provided nodeID
   *
   * @param nodeID The id of the location node being searched for
   * @return Location object with provided nodeID, null if there is no location with that ID
   */
  public Location getLocationByID(String nodeID) {
    for (Location loc : locationsList) {
      if (loc.getNodeID().equals(nodeID)) {
        return loc;
      }
    }
    return null;
  }
  /**
   * Adds a new location to database. Will automatically check if already in database
   *
   * @param loc The location to be added
   * @return True if successful, false if not
   */
  public boolean addLocation(Location loc) {
    updateConnection();
    boolean val = false;
    if (addToDatabase(loc)) {
      val = true;
      locationsList.add(loc);
    }
    return val;
  }

  /**
   * Updates a location in the database. Will automatically check if exists in database
   *
   * @param loc The location to be updated
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
    locationsList.remove(loc);
    locationsList.add(loc);
    // locations.remove(loc.getNodeID());
    // locations.put(loc.getNodeID(), loc);
    return true;
  }

  /**
   * Deletes a location from database. Will automatically check if exists in database already
   *
   * @param loc The location to be deleted
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
    locationsList.remove(loc);
    // locations.remove(loc.getNodeID());
    return true;
  }

  /**
   * Returns the default path for a location csv file to be saved
   *
   * @return The default path for a location csv file to be saved
   */
  File getDefaultLocationCSVPath() {
    return locCSV.getDefaultPath();
  }

  /**
   * Exports the Locations table into a CSV at the given path
   *
   * @param locData The path the CSV will be written to
   * @return True if success, false otherwise
   */
  @Override
  public boolean exportToLocationCSV(File locData) {
    // File locData = new File(System.getProperty("user.dir") + "\\TowerLocations.csv");
    updateConnection();

    try {
      locCSV.writeLocCSV(getAllLocations(), locData);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  @Override
  public boolean exportToLocationCSV() {
    updateConnection();

    try {
      locCSV.writeLocCSV(getAllLocations());
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * Gets all locations on the given floor
   *
   * @param floor The floor the locations will be on
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
        int i = 0;
        for (Location loc : locationsList) {
          if (loc.getNodeID().equals(nodeID)) {
            temp.add(locationsList.get(i));
          }
          i++;
        }
        // temp.add(locations.get(nodeID));
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
  public List<Location> getAllLocationsByType(String type) {
    updateConnection();
    List<Location> tempList = new ArrayList<>();
    try {
      PreparedStatement pstmt =
          connection.prepareStatement("Select nodeID from LOCATION WHERE NODETYPE = ?");
      pstmt.setString(1, type);

      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        String nodeID = rset.getString("nodeID");
        for (Location loc : locationsList) {
          if (loc.getNodeID().equals(nodeID)) {
            tempList.add(loc);
          }
        }
        // tempList.add(locations.get(nodeID));
      }
    } catch (SQLException e) {
      System.out.println("Failed to get locations");
    }
    return tempList;
  }

  /**
   * Imports data from CSV into location database
   *
   * @param locData The file location that contains a CSV of location data
   * @return number of times there are conflicts when trying to import
   */
  @Override
  public int importLocationFromCSV(File locData) {
    updateConnection();

    int numberConflicts = 0;
    try {
      List<Location> tempLoc = locCSV.readLocCSV(locData);

      List<String> newLocations = new ArrayList<>();

      for (Location loc : tempLoc) {
        newLocations.add(loc.getNodeID());
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

          Location temp = new Location(id);
          locationsList.remove(temp);
          // locations.remove(id);
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

          locationsList.add(newInfo);
          // locations.put(newInfo.getNodeID(), newInfo);
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

          locationsList.remove(newInfo);
          locationsList.add(newInfo);
          // locations.remove(newInfo.getNodeID());
          // locations.put(newInfo.getNodeID(), newInfo);
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

  /**
   * Get the nodeID of a dirty location on the given floor
   *
   * @param floor floor to be inspected
   * @return nodeID of the dirty location
   */
  public String getDirtyNodeIDbyFloor(String floor) {
    updateConnection();
    try {
      PreparedStatement pstmt =
          connection.prepareStatement(
              "" + "SELECT NODEID FROM LOCATION WHERE FLOOR = ? AND NODETYPE = 'DIRT'");
      pstmt.setString(1, floor);
      ResultSet rset = pstmt.executeQuery();
      while (rset.next()) {
        if (!rset.getString("NODEID").equals("")) {
          return rset.getString("NODEID");
        }
        return "zDIRT00103";
      }
    } catch (SQLException e) {
      System.out.println("Failed to get dirty location nodeID from floor");
      e.printStackTrace();
    }
    return "zDIRT00103";
  }

  /**
   * Get the nodeID of a clean storage location on the given floor
   *
   * @param floor floor to be inspected
   * @return nodeID of the clean location
   */
  public String getCleanNodeIDbyFloor(String floor) {
    updateConnection();
    try {
      PreparedStatement pstmt =
          connection.prepareStatement(
              "" + "SELECT NODEID FROM LOCATION WHERE FLOOR = ? AND NODETYPE = 'CLEAN'");
      pstmt.setString(1, floor);
      ResultSet rset = pstmt.executeQuery();

      int number = (int) Math.random() % 4;

      while (rset.next()) {
        if (!rset.getString("NODEID").equals("")) {
          return rset.getString("NODEID");
        } else {
          return "zSTOR00103";
        }
      }
    } catch (SQLException e) {
      System.out.println("Failed to get clean location nodeID from floor");
      e.printStackTrace();
    }
    return "zSTOR00103";
  }

  /**
   * Get the nodeID of a random bed park on the given floor
   *
   * @param floor floor to be inspected
   * @return nodeID of the bed park; default is OR Bed Park
   */
  public String getRandomBedParkNodeIDByFloor(String floor) {
    String thirdFloorBed[] = {"zSTOR00403", "zSTOR00403"};
    String fourthFloorBed[] = {"zSTOR00304", "zSTOR00404"};
    String fifthFloorBed[] = {"zSTOR00305"};
    int num = 0;
    switch (floor) {
      case ("3"):
        num = ((int) Math.random()) % thirdFloorBed.length;
        return thirdFloorBed[num];
      case ("4"):
        num = ((int) Math.random()) % fourthFloorBed.length;
        return fourthFloorBed[num];
      case ("5"):
        num = ((int) Math.random()) % fifthFloorBed.length;
        return fourthFloorBed[num];
      case ("L1"):
        return "zSTOR001L1";
      case ("1"):
        return "zSTOR00101";
      default:
        return "zSTOR00101";
    }
  }
}
