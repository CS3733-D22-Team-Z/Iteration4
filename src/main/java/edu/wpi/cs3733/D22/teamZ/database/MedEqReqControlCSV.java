package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipmentDeliveryRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MedEqReqControlCSV extends ControlCSV {
  private String[] headers = {"requestID", "itemID"};

  public MedEqReqControlCSV(File path) {
    this.setPath(path);
  }

  protected void writeMedReqCSV(List<MedicalEquipmentDeliveryRequest> in) {
    IMedicalEquipmentDAO medicalEquipmentDAO = new MedicalEquipmentDAOImpl();
    IServiceRequestDAO requestDAO = new ServiceRequestDAOImpl();

    writeCSV(objToData(in), headers);
    // TODO this doesnt make sense
    requestDAO.writeServiceRequestsToCSV();
  }

  protected List<MedicalEquipmentDeliveryRequest> readMedReqCSV() throws IOException {
    return dataToObj(readCSV());
  }

  private List<MedicalEquipmentDeliveryRequest> dataToObj(List<List<String>> data) {
    List<MedicalEquipmentDeliveryRequest> ret = new ArrayList<>();
    IServiceRequestDAO requestDAO = new ServiceRequestDAOImpl();
    IEmployeeDAO employeeDAO = new EmployeeDAOImpl();
    ILocationDAO locationDAO = new LocationDAOImpl();

    for (List<String> a : data) {
      String requestID = a.get(0);
      String equipmentID = a.get(1);

      ServiceRequest request = requestDAO.getServiceRequestByID(requestID);

      ret.add(
          new MedicalEquipmentDeliveryRequest(
              requestID,
              request.getStatus(),
              request.getIssuer(),
              request.getHandler(),
              equipmentID,
              request.getTargetLocation()));
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
