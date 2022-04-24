package edu.wpi.cs3733.D22.teamZ.apiFacades;

import edu.wpi.cs3733.D22.teamB.api.API;
import edu.wpi.cs3733.D22.teamB.api.DatabaseController;
import edu.wpi.cs3733.D22.teamB.api.ServiceException;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class InternalTransportFacadeAPI {

  // Wraps teamB's Location class in a unique class name
  private static class TeamBLocation extends edu.wpi.cs3733.D22.teamB.api.Location {
    public TeamBLocation(
        String nodeID,
        int xcoord,
        int ycoord,
        String floor,
        String building,
        String nodeType,
        String longName,
        String shortName) {
      super(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName);
    }
  }

  private static final InternalTransportFacadeAPI instance = new InternalTransportFacadeAPI();
  private final FacadeDAO facadeDAO = FacadeDAO.getInstance();
  private final API api;
  private final DatabaseController apiDatabase;

  public static InternalTransportFacadeAPI getInstance() {
    return instance;
  }

  private InternalTransportFacadeAPI() {
    api = new API();
    apiDatabase = new DatabaseController();
    try {
      apiDatabase.reset();
    } catch (ServiceException e) {
      System.out.println("Failed to clear database");
    }

    List<Location> locations = facadeDAO.getAllLocations();
    List<TeamBLocation> bLocations =
        locations.stream()
            .map(InternalTransportFacadeAPI::convertFromLocation)
            .collect(Collectors.toList());
    for (TeamBLocation bLoc : bLocations) {
      apiDatabase.add(bLoc);
    }
  }

  public void run(String cssPath) {
    try {
      api.run(0, 0, 450, 800, cssPath, "", "");
    } catch (ServiceException e) {
      System.out.println("Error accessing External Patient API");
      // System.out.println(e.getMessage());
      // e.printStackTrace();
    } catch (IOException ex) {
      System.out.println("weird");
    }
  }

  private static Location convertToLocation(TeamBLocation bLoc) {
    return FacadeDAO.getInstance().getLocationByID(bLoc.getNodeID());
  }

  private static TeamBLocation convertFromLocation(Location loc) {
    return new TeamBLocation(
        loc.getNodeID(),
        loc.getXcoord(),
        loc.getYcoord(),
        loc.getFloor(),
        loc.getBuilding(),
        loc.getNodeType(),
        loc.getLongName(),
        loc.getShortName());
  }
}
