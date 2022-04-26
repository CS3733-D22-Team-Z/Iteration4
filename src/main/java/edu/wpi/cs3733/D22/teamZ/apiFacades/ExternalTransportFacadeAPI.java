package edu.wpi.cs3733.D22.teamZ.apiFacades;

import edu.wpi.cs3733.D22.teamZ.api.API;
import edu.wpi.cs3733.D22.teamZ.api.entity.ExternalTransportRequest;
import edu.wpi.cs3733.D22.teamZ.api.entity.RequestStatus;
import edu.wpi.cs3733.D22.teamZ.api.exception.ServiceException;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.ExternalPatientTransportationRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.util.ArrayList;
import java.util.List;

public class ExternalTransportFacadeAPI {

  private static final ExternalTransportFacadeAPI instance = new ExternalTransportFacadeAPI();
  private final API api;

  public static ExternalTransportFacadeAPI getInstance() {
    return instance;
  }

  private ExternalTransportFacadeAPI() {
    api = new API();
  }

  private final FacadeDAO dao = FacadeDAO.getInstance();

  public void run(String cssPath) {
    try {
      api.run(0, 0, 450, 800, cssPath, "", "");
    } catch (ServiceException e) {
      System.out.println("Error accessing External Patient API");
      System.out.println(e.getMessage());
      // e.printStackTrace();
    }
  }

  public List<ExternalPatientTransportationRequest> getAllExternalTransportRequests() {
    List<ExternalTransportRequest> list = api.getAllExternalTransportRequests();
    List<ExternalPatientTransportationRequest> returnList = new ArrayList<>();
    for (ExternalTransportRequest req : list) {
      ExternalPatientTransportationRequest temp =
          new ExternalPatientTransportationRequest(
              req.getRequestID(),
              ServiceRequest.RequestStatus.valueOf(String.valueOf(req.getStatus())),
              req.getIssuerID(),
              req.getHandlerID(),
              dao.getPatientByID(req.getPatientID()).getLocation().getNodeID(),
              req.getPatientID(),
              dao.getPatientByID(req.getPatientID()).getName(),
              req.getTransportDestination(),
              req.getDepartureDate());
      returnList.add(temp);
    }
    return returnList;
  }

  public ExternalTransportRequest getExternalTransportRequestbyID(String Id) {
    ExternalTransportRequest req = getExternalTransportRequestbyID(Id);
    ExternalPatientTransportationRequest temp =
        new ExternalPatientTransportationRequest(
            req.getRequestID(),
            ServiceRequest.RequestStatus.valueOf(String.valueOf(req.getStatus())),
            req.getIssuerID(),
            req.getHandlerID(),
            dao.getPatientByID(req.getPatientID()).getLocation().getNodeID(),
            req.getPatientID(),
            dao.getPatientByID(req.getPatientID()).getName(),
            req.getTransportDestination(),
            req.getDepartureDate());
    return req;
  }

  public boolean updateExternalTransportRequest(ExternalPatientTransportationRequest req) {
    return api.updateExternalTransportRequest(
        new ExternalTransportRequest(
            req.getRequestID(),
            RequestStatus.valueOf(String.valueOf(req.getStatus())),
            req.getIssuer().getEmployeeID(),
            req.getHandler().getEmployeeID(),
            req.getPatientID(),
            req.getDestination(),
            req.getDepartureDate(),
            api.getExternalTransportRequestByID(req.getRequestID()).getTransportMethod()));
  }

  public boolean deleteExternalTransportRequest(ExternalPatientTransportationRequest req) {
    return api.deleteExternalTransportRequest(req.getRequestID());
  }

  public boolean addExternalTransportRequest(ExternalPatientTransportationRequest req) {
    return api.addExternalTransportRequest(
        new ExternalTransportRequest(
            req.getRequestID(),
            RequestStatus.valueOf(String.valueOf(req.getStatus())),
            req.getIssuer().getEmployeeID(),
            req.getHandler().getEmployeeID(),
            req.getPatientID(),
            req.getDestination(),
            req.getDepartureDate(),
            api.getExternalTransportRequestByID(req.getRequestID()).getTransportMethod()));
  }

  public void addAllExternalTransportRequest(List<ExternalPatientTransportationRequest> listReq) {
    for (ExternalPatientTransportationRequest req : listReq) {
      addExternalTransportRequest(req);
    }
  }
}
