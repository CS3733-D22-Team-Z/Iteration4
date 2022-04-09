package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServiceRequestControlCSV extends ControlCSV {

  private final LocationDAOImpl locationDAO = new LocationDAOImpl();

  private final String[] headers = {"requestID", "type", "status", "issuer", "handler", "targetLocation"};

  public ServiceRequestControlCSV(File path) {
    this.setPath(path);
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
      ret.add(
          new ServiceRequest(
              a.get(0),
              ServiceRequest.RequestType.getRequestTypeByString(a.get(1)),
              ServiceRequest.RequestStatus.getRequestStatusByString(a.get(2)),
              // change later
              null,
              null,
              locationDAO.getLocationByID(a.get(5))));
    }
    return ret;
  }

  protected List<List<String>> objToData(List<ServiceRequest> in) {
    List<List<String>> ret = new ArrayList<>();

    for (ServiceRequest a : in) {
      List<String> entry =
          new ArrayList<>(
              List.of(
                  new String[] {
                    a.getRequestID(),
                    a.getType().toString(),
                    a.getStatus().toString(),
                    // change later
                    "null",
                    "null",
                    a.getTargetLocation().getNodeID()
                  }));
      ret.add(entry);
    }
    return ret;
  }
}
