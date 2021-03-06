package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.MealServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MealServReqControlCSV extends ControlCSV {
  private final String[] headers = {"requestID", "patientID"};
  private FacadeDAO dao = FacadeDAO.getInstance();

  public MealServReqControlCSV(File path) {
    this.setDefaultPath(path);
  }

  protected void writeMealReqCSV(List<MealServiceRequest> in) throws IOException {
    IMealServiceRequestDAO mealServiceRequestDAO = new MealServiceRequestDAOImpl();
    IServiceRequestDAO requestDAO = new ServiceRequestDAOImpl();

    writeCSV(objToData(in), headers);
    // TODO this doesnt make sense
    // dao.exportToServiceRequestCSV();
  }

  protected List<MealServiceRequest> readMealServReqCSV() throws IOException {
    return dataToObj(readCSV());
  }

  private List<MealServiceRequest> dataToObj(List<List<String>> data) {
    List<MealServiceRequest> ret = new ArrayList<>();

    for (List<String> a : data) {
      String requestID = a.get(0);
      String patientID = a.get(1);
      String drink = a.get(2);
      String entree = a.get(3);
      String side = a.get(4);
      String allergen = a.get(5);

      ServiceRequest request = dao.getServiceRequestByID(requestID);
      if (request.getHandler() == null) {
        ret.add(
            new MealServiceRequest(
                requestID,
                request.getStatus(),
                request.getIssuer(),
                null,
                request.getTargetLocation(),
                request.getOpened(),
                request.getClosed(),
                dao.getPatientByID(patientID),
                drink,
                entree,
                side,
                allergen));
      } else {
        ret.add(
            new MealServiceRequest(
                requestID,
                request.getStatus(),
                request.getIssuer(),
                request.getHandler(),
                request.getTargetLocation(),
                request.getOpened(),
                request.getClosed(),
                dao.getPatientByID(patientID),
                drink,
                entree,
                side,
                allergen));
      }
    }
    return ret;
  }

  private List<List<String>> objToData(List<MealServiceRequest> in) {
    List<List<String>> ret = new ArrayList<>();
    for (MealServiceRequest a : in) {
      List<String> entry =
          new ArrayList<>(List.of(new String[] {a.getRequestID(), a.getPatient().getPatientID()}));
      ret.add(entry);
    }
    return ret;
  }
}
