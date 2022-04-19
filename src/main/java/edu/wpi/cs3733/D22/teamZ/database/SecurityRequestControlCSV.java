package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.SecurityServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SecurityRequestControlCSV extends ControlCSV {
  private final String[] headers = {"requestID", "urgency", "reason"};

  public SecurityRequestControlCSV(File path) {
    this.setDefaultPath(path);
  }

  public void writeSecurityServiceRequestCSV(List<SecurityServiceRequest> in) throws IOException {
    writeCSV(objToData(in), headers);
  }

  public List<SecurityServiceRequest> readSecurityServiceRequestCSV() throws IOException {
    return dataToObj(readCSV());
  }

  private List<SecurityServiceRequest> dataToObj(List<List<String>> in) {
    List<SecurityServiceRequest> securityRequests = new ArrayList<>();

    for (List<String> entry : in) {
      String requestID = entry.get(0);
      String urgency = entry.get(1);
      String reason = entry.get(2);

      ServiceRequest request = FacadeDAO.getInstance().getServiceRequestByID(requestID);

      securityRequests.add(
          new SecurityServiceRequest(
              requestID,
              request.getStatus(),
              request.getIssuer(),
              request.getHandler(),
              request.getTargetLocation(),
              urgency,
              reason));
    }
    return securityRequests;
  }

  private List<List<String>> objToData(List<SecurityServiceRequest> in) {
    List<List<String>> data = new ArrayList<>();

    for (SecurityServiceRequest request : in) {
      List<String> entry =
          new ArrayList<>(
              List.of(
                  new String[] {
                    request.getRequestID(), request.getUrgency(), request.getReason()
                  }));

      data.add(entry);
    }

    return data;
  }
}
