package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServiceRequestControlCSV extends ControlCSV {

  private final FacadeDAO facadeDAO = FacadeDAO.getInstance();

  private final String[] headers = {
    "requestID", "type", "status", "issuer", "handler", "targetLocation"
  };

  public ServiceRequestControlCSV(File path) {
    this.setDefaultPath(path);
  }

  protected void writeServiceRequestCSV(List<ServiceRequest> in) {
    writeCSV(objToData(in), headers);
  }

  protected List<ServiceRequest> readServiceRequestCSV() throws IOException {
    return dataToObj(readCSV());
  }

  private List<ServiceRequest> dataToObj(List<List<String>> data) {
    List<ServiceRequest> ret = new ArrayList<>();
    for (List<String> a : data) {
      if (a.get(4).equals("null")) {
        ret.add(
            new ServiceRequest(
                a.get(0),
                ServiceRequest.RequestType.getRequestTypeByString(a.get(1)),
                ServiceRequest.RequestStatus.getRequestStatusByString(a.get(2)),
                facadeDAO.getEmployeeByID(a.get(3)),
                null,
                facadeDAO.getLocationByID(a.get(5))));
      } else {
        ret.add(
            new ServiceRequest(
                a.get(0),
                ServiceRequest.RequestType.getRequestTypeByString(a.get(1)),
                ServiceRequest.RequestStatus.getRequestStatusByString(a.get(2)),
                facadeDAO.getEmployeeByID(a.get(3)),
                facadeDAO.getEmployeeByID(a.get(4)),
                facadeDAO.getLocationByID(a.get(5))));
      }
    }
    return ret;
  }

  protected List<List<String>> objToData(List<ServiceRequest> in) {
    List<List<String>> ret = new ArrayList<>();

    for (ServiceRequest a : in) {
      if (a.getHandler() == null) {
        List<String> entry =
            new ArrayList<>(
                List.of(
                    new String[] {
                      a.getRequestID(),
                      a.getType().toString(),
                      a.getStatus().toString(),
                      a.getIssuer().getEmployeeID(),
                      null,
                      a.getTargetLocation().getNodeID()
                    }));
        ret.add(entry);
      } else {
        List<String> entry =
            new ArrayList<>(
                List.of(
                    new String[] {
                      a.getRequestID(),
                      a.getType().toString(),
                      a.getStatus().toString(),
                      a.getIssuer().getEmployeeID(),
                      a.getHandler().getEmployeeID(),
                      a.getTargetLocation().getNodeID()
                    }));
        ret.add(entry);
      }
    }
    return ret;
  }
}
