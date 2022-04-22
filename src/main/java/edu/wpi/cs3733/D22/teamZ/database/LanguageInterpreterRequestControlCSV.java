package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LanguageInterpreterRequestControlCSV extends ControlCSV {
  private FacadeDAO requestDAO = FacadeDAO.getInstance();
  private final String[] headers = {"requestID", "patientName", "patientID", "language"};

  public LanguageInterpreterRequestControlCSV(File path) {
    this.setDefaultPath(path);
  }

  public void writeLanguageInterpreterRequestCSV(List<LanguageInterpreterRequest> in)
      throws IOException {
    writeCSV(objToData(in), headers);
  }

  public List<LanguageInterpreterRequest> readLanguageIntepreterRequestCSV() throws IOException {
    return dataToObj(readCSV());
  }

  private List<LanguageInterpreterRequest> dataToObj(List<List<String>> in) {
    List<LanguageInterpreterRequest> languageInterpreterRequestList = new ArrayList<>();

    for (List<String> entry : in) {
      String requestID = entry.get(0);
      String patientName = entry.get(1);
      String patientID = entry.get(2);
      String language = entry.get(3);

      ServiceRequest request = requestDAO.getServiceRequestByID(requestID);
      languageInterpreterRequestList.add(
          new LanguageInterpreterRequest(
              requestID,
              request.getStatus(),
              request.getIssuer(),
              request.getHandler(),
              request.getTargetLocation(),
              patientName,
              patientID,
              language));
    }

    return languageInterpreterRequestList;
  }

  private List<List<String>> objToData(List<LanguageInterpreterRequest> in) {
    List<List<String>> data = new ArrayList<>();

    for (LanguageInterpreterRequest request : in) {
      List<String> entry =
          new ArrayList<>(
              List.of(
                  new String[] {
                    request.getRequestID(),
                    request.getPatientName(),
                    request.getPatientID(),
                    request.getLanguage()
                  }));

      data.add(entry);
    }

    return data;
  }
}
