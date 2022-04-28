package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.ComputerServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ComputerRequestControlCSV extends ControlCSV {
  private final String[] headers = {"requestID", "operatingSystem", "problemDesc"};

  public ComputerRequestControlCSV(File path) {
    this.setDefaultPath(path);
  }

  public void writeComputerServiceRequestCSV(List<ComputerServiceRequest> in) throws IOException {
    writeCSV(objToData(in), headers);
  }

  public List<ComputerServiceRequest> readComputerServiceRequestCSV() throws IOException {
    return dataToObj(readCSV());
  }

  private List<ComputerServiceRequest> dataToObj(List<List<String>> in) {
    List<ComputerServiceRequest> computerRequests = new ArrayList<>();

    for (List<String> entry : in) {
      String requestID = entry.get(0);
      String operatingSystemStr = entry.get(1);
      String problemDescStr = entry.get(2);

      ServiceRequest request = FacadeDAO.getInstance().getServiceRequestByID(requestID);

      computerRequests.add(
          new ComputerServiceRequest(
              requestID,
              request.getStatus(),
              request.getIssuer(),
              request.getHandler(),
              request.getTargetLocation(),
              request.getOpened(),
              request.getClosed(),
              operatingSystemStr,
              problemDescStr));
    }
    return computerRequests;
  }

  private List<List<String>> objToData(List<ComputerServiceRequest> in) {
    List<List<String>> data = new ArrayList<>();

    for (ComputerServiceRequest request : in) {
      List<String> entry =
          new ArrayList<>(
              List.of(
                  new String[] {
                    request.getRequestID(),
                    request.getOperatingSystem(),
                    request.getProblemDescription()
                  }));

      data.add(entry);
    }

    return data;
  }
}
