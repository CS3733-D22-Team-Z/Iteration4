package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.CleaningRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CleaningReqControlCSV extends ControlCSV {
  private final String[] headers = {"requestID", "type"};
  private final FacadeDAO dao = FacadeDAO.getInstance();

  public CleaningReqControlCSV(File path) {
    this.setDefaultPath(path);
  }

  protected void writeCleanReqCSV(List<CleaningRequest> in) throws IOException {
    writeCSV(objToData(in), headers);
  }

  protected void writeCleanReqCSV(List<CleaningRequest> in, File path) throws IOException {
    writeCSV(objToData(in), path, headers);
  }

  protected List<CleaningRequest> readCleanReqCSV() throws IOException {
    return dataToObj(readCSV());
  }

  protected List<CleaningRequest> readCleanReqCSV(File path) throws IOException {
    return dataToObj(readCSV(path));
  }

  private List<CleaningRequest> dataToObj(List<List<String>> data) {
    List<CleaningRequest> ret = new ArrayList<>();

    for (List<String> a : data) {
      String requestID = a.get(0);
      String type = a.get(1);

      ServiceRequest request = dao.getServiceRequestByID(requestID);
      if (request.getHandler() == null) {
        ret.add(
            new CleaningRequest(
                requestID,
                request.getStatus(),
                request.getIssuer(),
                null,
                request.getTargetLocation(),
                request.getOpened(),
                request.getClosed(),
                type));
      } else {
        ret.add(
            new CleaningRequest(
                requestID,
                request.getStatus(),
                request.getIssuer(),
                request.getHandler(),
                request.getTargetLocation(),
                request.getOpened(),
                request.getClosed(),
                type));
      }
    }
    return ret;
  }

  private List<List<String>> objToData(List<CleaningRequest> in) {
    List<List<String>> ret = new ArrayList<>();
    for (CleaningRequest a : in) {
      List<String> entry =
          new ArrayList<>(List.of(new String[] {a.getRequestID(), a.getCleaningType()}));
      ret.add(entry);
    }
    return ret;
  }
}
