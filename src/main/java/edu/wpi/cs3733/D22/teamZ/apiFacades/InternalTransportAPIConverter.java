package edu.wpi.cs3733.D22.teamZ.apiFacades;

import edu.wpi.cs3733.D22.teamB.api.IPTEmployee;
import edu.wpi.cs3733.D22.teamB.api.Request;
import edu.wpi.cs3733.D22.teamZ.apiFacades.InternalTransportFacadeAPI.TeamBLocation;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import edu.wpi.cs3733.D22.teamZ.entity.InternalTransportRequest;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest.RequestStatus;

class InternalTransportAPIConverter {

  // ---------- Location Converters ----------
  public void updateLocation(TeamBLocation apiLoc, Location loc) {
    apiLoc.setxCoord(loc.getXcoord());
    apiLoc.setyCoord(loc.getYcoord());
    apiLoc.setFloor(loc.getFloor());
    apiLoc.setBuilding(loc.getBuilding());
    apiLoc.setNodeType(loc.getNodeType());
    apiLoc.setLongName(loc.getLongName());
    apiLoc.setShortName(loc.getShortName());
  }

  Location convertFromAPILocation(edu.wpi.cs3733.D22.teamB.api.Location apiLoc) {
    return FacadeDAO.getInstance().getLocationByID(apiLoc.getNodeID());
  }

  // ---------- Employee Converters ----------
  IPTEmployee convertToAPIEmployee(Employee emp) {
    return new IPTEmployee(emp.getEmployeeID(), "", emp.getName(), "N/A", "N/A");
  }

  // ---------- Request Converters ----------
  InternalTransportRequest convertFromAPIRequest(Request apiRequest) {
    Employee issuer =
        new Employee("Unknown", "Unknown", Employee.AccessType.NURSE, "Unknown", "Unknown");
    Employee handler = FacadeDAO.getInstance().getEmployeeByID(apiRequest.getEmployeeID());
    return new InternalTransportRequest(
        apiRequest.getRequestID(),
        getStatusFromAPIRequest(apiRequest),
        issuer,
        handler,
        convertFromAPILocation(apiRequest.getFinishLocation()),
        apiRequest.getInformation());
  }

  Request convertToAPIRequest(InternalTransportRequest request) {
    String handlerID = (request.getHandler() == null) ? "" : request.getHandler().getEmployeeID();
    return new Request(
        request.getRequestID(),
        handlerID,
        null,
        null,
        request.getStatus().toString(),
        0,
        request.getInformation(),
        null,
        null,
        null);
  }

  void updateAPIRequestWithServiceRequest(Request apiRequest, InternalTransportRequest request) {
    String handlerID = (request.getHandler() == null) ? "" : request.getHandler().getEmployeeID();
    apiRequest.setEmployeeID(handlerID);
    apiRequest.setInformation(request.getInformation());
    apiRequest.setStatus(request.getStatus().toString());
  }

  private RequestStatus getStatusFromAPIRequest(Request apiRequest) {
    switch (apiRequest.getStatus()) {
      case "Pending":
      case "UNASSIGNED":
        return RequestStatus.UNASSIGNED;
      case "In-Progress":
      case "PROCESSING":
        return RequestStatus.PROCESSING;
      case "Completed":
      case "DONE":
        return RequestStatus.DONE;
      default:
        return null;
    }
  }
}
