package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipmentDeliveryRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MedEqReqControlCSV extends ControlCSV {
  private String[] headers = {
    "RequestID", "status", "issuer", "handler", "equipment", "currentLoc", "targetLoc"
  };

  public MedEqReqControlCSV(File path) {
    this.setPath(path);
  }

  protected void writeMedReqCSV(List<MedicalEquipmentDeliveryRequest> in) {
    writeCSV(objToData(in), headers);
  }

  protected List<MedicalEquipmentDeliveryRequest> readMedReqCSV() throws IOException {
    return dataToObj(readCSV());
  }

  private List<MedicalEquipmentDeliveryRequest> dataToObj(List<List<String>> data) {
    List<MedicalEquipmentDeliveryRequest> ret = new ArrayList<>();

    for (List<String> a : data) {
      ret.add(
          new MedicalEquipmentDeliveryRequest(
              a.get(0), a.get(1), a.get(2), a.get(3), a.get(4), a.get(5), a.get(6)));
    }
    return ret;
  }

  private List<List<String>> objToData(List<MedicalEquipmentDeliveryRequest> in) {
    List<List<String>> ret = new ArrayList<>();
    for (MedicalEquipmentDeliveryRequest a : in) {
      List<String> entry =
          new ArrayList<>(
              List.of(
                  new String[] {
                    a.getRequestID(),
                    a.getStatus(),
                    a.getIssuer(),
                    a.getHandler(),
                    a.getEquipment(),
                    a.getCurrentLoc(),
                    a.getTargetLoc()
                  }));
      ret.add(entry);
    }
    return ret;
  }
}
