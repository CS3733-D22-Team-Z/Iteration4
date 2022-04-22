package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.LaundryServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LaundryServiceRequestControlCSV extends ControlCSV {
  private final String[] headers = {"requestID", "laundryStatus", "LaundryType"};

  public LaundryServiceRequestControlCSV(File path) {
    this.setDefaultPath(path);
  }

  public void writeLaundryServiceRequestCSV(List<LaundryServiceRequest> in) throws IOException {
    writeCSV(objToData(in), headers);
  }

  public List<LaundryServiceRequest> readLaundryServiceRequestCSV() throws IOException {
    return dataToObj(readCSV());
  }

  private List<LaundryServiceRequest> dataToObj(List<List<String>> in) {
    List<LaundryServiceRequest> laundryServiceRequests = new ArrayList<>();

    for (List<String> entry : in) {
      String requestID = entry.get(0);
      String laundryStatus = entry.get(1);
      String linenType = entry.get(2);

      ServiceRequest request = FacadeDAO.getInstance().getServiceRequestByID(requestID);

      laundryServiceRequests.add(
          new LaundryServiceRequest(
              requestID,
              request.getStatus(),
              request.getIssuer(),
              request.getHandler(),
              request.getTargetLocation(),
              LaundryServiceRequest.LaundryStatus.getRequestStatusByString(laundryStatus),
              linenType));
    }
    return laundryServiceRequests;
  }

  private List<List<String>> objToData(List<LaundryServiceRequest> in) {
    List<List<String>> data = new ArrayList<>();

    for (LaundryServiceRequest request : in) {
      List<String> entry =
          new ArrayList<>(
              List.of(
                  new String[] {
                    request.getRequestID(),
                    String.valueOf(request.getLaundryType()),
                    request.getLaundryType()
                  }));

      data.add(entry);
    }

    return data;
  }
}
