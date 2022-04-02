package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Location;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDAOImpl implements ILocationDAO {

  List<Location> locations;
  private LocationControlCSV locCSV;

  static Connection connection = DatabaseConnection.getConnection();

  public LocationDAOImpl() {
    locations = new ArrayList<Location>();
  }

  /**
   * Gets all of the locations in the database
   *
   * @return List of locations
   */
  public List<Location> getAllLocations() {
    try {
      PreparedStatement pstmt = connection.prepareStatement("Select * From Location");
      ResultSet rset = pstmt.executeQuery();

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
   * Gets ONE lcoation from the database based on the provided nodeID
   *
   * @param nodeID
   * @return Location object with provided nodeID
   */
  public Location getLocationByID(String nodeID) { // implement
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
    locations.add(loc);
    return true;
  }

  /**
   * Updates a location in the database. Will automatically check if exists in database
   *
   * @param loc
   * @return True if successful, false if not
   */
  public boolean updateLocation(Location loc) {
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
  public boolean exportToLocationCSV() {

    File locData = new File(System.getProperty("user.dir") + "\\TowerLocations.csv");
    locCSV = new LocationControlCSV(locData);
    locCSV.writeLocCSV(getAllLocations());

    return true;
  }
}
