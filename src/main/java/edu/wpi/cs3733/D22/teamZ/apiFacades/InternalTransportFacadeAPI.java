package edu.wpi.cs3733.D22.teamZ.apiFacades;

import edu.wpi.cs3733.D22.teamB.api.API;
import edu.wpi.cs3733.D22.teamB.api.DatabaseController;
import edu.wpi.cs3733.D22.teamB.api.Request;
import edu.wpi.cs3733.D22.teamB.api.ServiceException;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.InternalTransportRequest;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class InternalTransportFacadeAPI {

  // Wraps teamB's Location class in a unique class name
  static class TeamBLocation extends edu.wpi.cs3733.D22.teamB.api.Location {
    public TeamBLocation(Location loc) {
      super(
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

  private static final InternalTransportFacadeAPI instance = new InternalTransportFacadeAPI();
  private final FacadeDAO facadeDAO = FacadeDAO.getInstance();
  private final API api;
  private final DatabaseController apiDatabase;
  private final InternalTransportAPIConverter apiConverter;
  private final HashMap<String, TeamBLocation> apiLocationMap;

  public static InternalTransportFacadeAPI getInstance() {
    return instance;
  }

  private InternalTransportFacadeAPI() {
    this.api = new API();
    this.apiDatabase = new DatabaseController();
    try {
      apiDatabase.reset();
    } catch (ServiceException e) {
      System.out.println("Failed to clear database");
    }

    List<Location> locations = facadeDAO.getAllLocations();
    this.apiLocationMap = new HashMap<>();
    for (Location loc : locations) {
      TeamBLocation bLoc = new TeamBLocation(loc);
      apiLocationMap.put(loc.getNodeID(), bLoc);
      apiDatabase.add(bLoc);
    }

    apiConverter = new InternalTransportAPIConverter();
  }

  public void run(String cssPath) {
    try {
      api.run(0, 0, 800, 450, cssPath, "", "");
    } catch (ServiceException e) {
      System.out.println("Error accessing Internal Patient API");
      e.printStackTrace();
    } catch (IOException ex) {
      System.out.println("Internal Patient API IOException");
      ex.printStackTrace();
    }
  }

  // ---------- Database interaction methods ----------

  public List<InternalTransportRequest> getAllInternalTransportRequests() {
    List<Request> apiRequests = apiDatabase.listRequests();
    List<InternalTransportRequest> requests =
        apiRequests.stream()
            .map(e -> apiConverter.convertFromAPIRequest(e))
            .collect(Collectors.toList());
    return requests;
  }

  public InternalTransportRequest getInternalTransportRequestByID(String id) {
    Request apiRequest = apiDatabase.getRequestByID(id);
    return apiConverter.convertFromAPIRequest(apiRequest);
  }

  public boolean addInternalTransportRequest(InternalTransportRequest request) {
    int val = apiDatabase.add(apiConverter.convertToAPIRequest(request));
    return (val == 0);
  }

  public boolean updateInternalTransportRequest(InternalTransportRequest request) {
    Request apiRequest = apiDatabase.getRequestByID(request.getRequestID());
    if (apiRequest == null) {
      return false;
    }

    apiConverter.updateAPIRequestWithServiceRequest(apiRequest, request);
    int val = apiDatabase.update(apiRequest);

    return (val == 0);
  }

  public boolean deleteInternalTransportRequest(InternalTransportRequest request) {
    Request apiRequest = apiDatabase.getRequestByID(request.getRequestID());
    if (apiRequest == null) {
      return false;
    }
    int val = apiDatabase.delete(apiRequest);

    return (val == 0);
  }

  public boolean exportToInternalTransportRequestCSV() {
    // TODO implement exportToInternalTransportRequestCSV method
    return false;
  }

  public int importInternalTransportRequestsFromCSV() {
    // TODO implement importInternalTransportRequestsFromCSV method
    return -1;
  }
}
