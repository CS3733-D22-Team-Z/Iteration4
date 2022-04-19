package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.EquipmentPurchaseRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EquipmentPurchaseRequestControlCSV extends ControlCSV {
  private final String[] headers = {"requestID", "equipmentTypeStr", "paymentMethodStr"};

  public EquipmentPurchaseRequestControlCSV(File path) {
    this.setDefaultPath(path);
  }

  public void writeEquipmentPurchaseRequestCSV(List<EquipmentPurchaseRequest> in)
      throws IOException {
    writeCSV(objToData(in), headers);
  }

  public List<EquipmentPurchaseRequest> readEquipmentPurchaseRequestCSV() throws IOException {
    return dataToObj(readCSV());
  }

  private List<EquipmentPurchaseRequest> dataToObj(List<List<String>> in) {
    List<EquipmentPurchaseRequest> equipmentPurchaseRequests = new ArrayList<>();

    for (List<String> entry : in) {
      String requestID = entry.get(0);
      String equipmentTypeStr = entry.get(1);
      String paymentMethodStr = entry.get(2);

      ServiceRequest request = FacadeDAO.getInstance().getServiceRequestByID(requestID);

      equipmentPurchaseRequests.add(
          new EquipmentPurchaseRequest(
              requestID,
              request.getStatus(),
              request.getIssuer(),
              request.getHandler(),
              request.getTargetLocation(),
              equipmentTypeStr,
              paymentMethodStr));
    }
    return equipmentPurchaseRequests;
  }

  private List<List<String>> objToData(List<EquipmentPurchaseRequest> in) {
    List<List<String>> data = new ArrayList<>();

    for (EquipmentPurchaseRequest request : in) {
      List<String> entry =
          new ArrayList<>(
              List.of(
                  new String[] {
                    request.getRequestID(), request.getEquipmentType(), request.getPaymentMethod()
                  }));

      data.add(entry);
    }

    return data;
  }
}
