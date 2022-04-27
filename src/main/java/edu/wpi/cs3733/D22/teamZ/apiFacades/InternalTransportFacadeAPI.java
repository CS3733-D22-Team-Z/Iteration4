package edu.wpi.cs3733.D22.teamZ.apiFacades;

import edu.wpi.cs3733.D22.teamB.api.*;
import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import edu.wpi.cs3733.D22.teamZ.entity.InternalTransportRequest;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import java.io.IOException;
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
  private final API api;
  private final DatabaseController apiDatabase;
  private final InternalTransportAPIConverter apiConverter;

  public static InternalTransportFacadeAPI getInstance() {
    return instance;
  }

  private InternalTransportFacadeAPI() {
    this.api = new API();
    this.apiDatabase = new DatabaseController();
    this.apiConverter = new InternalTransportAPIConverter();

    List<edu.wpi.cs3733.D22.teamB.api.Location> bLocs = apiDatabase.listLocations();
    for (edu.wpi.cs3733.D22.teamB.api.Location bLoc : bLocs) {
      apiDatabase.delete(bLoc);
    }

    List<edu.wpi.cs3733.D22.teamB.api.IPTEmployee> bEmps = apiDatabase.listEmployees();
    for (edu.wpi.cs3733.D22.teamB.api.IPTEmployee bEmp : bEmps) {
      apiDatabase.delete(bEmp);
    }
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

  // Request Methods
  public List<InternalTransportRequest> getAllInternalTransportRequests() {
    List<Request> apiRequests = apiDatabase.listRequests();
    List<InternalTransportRequest> requests =
        apiRequests.stream().map(apiConverter::convertFromAPIRequest).collect(Collectors.toList());
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

  // Location Methods
  public boolean addLocation(Location loc) {
    int val = apiDatabase.add(new TeamBLocation(loc));
    return (val == 0);
  }

  public boolean updateLocation(Location loc) {
    int val = apiDatabase.update(new TeamBLocation(loc));
    return (val == 0);
  }

  public boolean deleteLocation(Location loc) {
    int val = apiDatabase.delete(new TeamBLocation(loc));
    return (val == 0);
  }

  // Employee Methods
  public boolean addEmployee(Employee emp) {
    int val = apiDatabase.add(apiConverter.convertToAPIEmployee(emp));
    return (val == 0);
  }

  public boolean updateEmployee(Employee emp) {
    int val = apiDatabase.update(apiConverter.convertToAPIEmployee(emp));
    return (val == 0);
  }

  public boolean deleteEmployee(Employee emp) {
    int val = apiDatabase.delete(apiConverter.convertToAPIEmployee(emp));
    return (val == 0);
  }
}
