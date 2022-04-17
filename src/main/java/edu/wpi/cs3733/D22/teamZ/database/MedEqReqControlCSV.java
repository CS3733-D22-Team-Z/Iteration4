package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipmentDeliveryRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MedEqReqControlCSV extends ControlCSV {
  private final String[] headers = {"requestID", "itemID"};
  private FacadeDAO dao = FacadeDAO.getInstance();

  public MedEqReqControlCSV(File path) {
    this.setDefaultPath(path);
  }

  protected void writeMedReqCSV(List<MedicalEquipmentDeliveryRequest> in) throws IOException {
    IMedicalEquipmentDAO medicalEquipmentDAO = new MedicalEquipmentDAOImpl();
    IServiceRequestDAO requestDAO = new ServiceRequestDAOImpl();

    writeCSV(objToData(in), headers);
    // TODO this doesnt make sense
    // dao.exportToServiceRequestCSV();
  }

  protected List<MedicalEquipmentDeliveryRequest> readMedReqCSV() throws IOException {
    return dataToObj(readCSV());
  }

  private List<MedicalEquipmentDeliveryRequest> dataToObj(List<List<String>> data) {
    List<MedicalEquipmentDeliveryRequest> ret = new ArrayList<>();

    for (List<String> a : data) {
      String requestID = a.get(0);
      String equipmentID = a.get(1);

      ServiceRequest request = dao.getServiceRequestByID(requestID);
      if (request.getHandler() == null) {
        ret.add(
            new MedicalEquipmentDeliveryRequest(
                requestID,
                request.getStatus(),
                request.getIssuer(),
                null,
                equipmentID,
                request.getTargetLocation()));
      } else {
        ret.add(
            new MedicalEquipmentDeliveryRequest(
                requestID,
                request.getStatus(),
                request.getIssuer(),
                request.getHandler(),
                equipmentID,
                request.getTargetLocation()));
      }
    }
    return ret;
  }

  private List<List<String>> objToData(List<MedicalEquipmentDeliveryRequest> in) {
    List<List<String>> ret = new ArrayList<>();
    for (MedicalEquipmentDeliveryRequest a : in) {
      List<String> entry =
          new ArrayList<>(List.of(new String[] {a.getRequestID(), a.getEquipmentID()}));
      ret.add(entry);
    }
    return ret;
  }
}
