package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.GiftServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GiftServiceRequestControlCSV extends ControlCSV {
  private FacadeDAO requestDAO = FacadeDAO.getInstance();
  private final String[] headers = {"requestID", "patientName", "patientID", "giftType"};

  public GiftServiceRequestControlCSV(File path) {
    this.setDefaultPath(path);
  }

  public void writeGiftServiceRequestCSV(List<GiftServiceRequest> in) throws IOException {
    writeCSV(objToData(in), headers);
  }

  public List<GiftServiceRequest> readGiftServiceRequestCSV() throws IOException {
    return dataToObj(readCSV());
  }

  private List<GiftServiceRequest> dataToObj(List<List<String>> in) {
    List<GiftServiceRequest> giftRequestList = new ArrayList<>();

    for (List<String> entry : in) {
      String requestID = entry.get(0);
      String patientName = entry.get(1);
      String patientID = entry.get(2);
      String giftType = entry.get(3);

      ServiceRequest request = requestDAO.getServiceRequestByID(requestID);

      giftRequestList.add(
          new GiftServiceRequest(
              requestID,
              request.getStatus(),
              request.getIssuer(),
              request.getHandler(),
              request.getTargetLocation(),
              patientName,
              patientID,
              giftType));
    }

    return giftRequestList;
  }

  private List<List<String>> objToData(List<GiftServiceRequest> in) {
    List<List<String>> data = new ArrayList<>();

    for (GiftServiceRequest request : in) {
      List<String> entry =
          new ArrayList<>(
              List.of(
                  new String[] {
                    request.getRequestID(),
                    request.getPatientName(),
                    request.getPatientID(),
                    request.getGiftType()
                  }));

      data.add(entry);
    }

    return data;
  }
}
