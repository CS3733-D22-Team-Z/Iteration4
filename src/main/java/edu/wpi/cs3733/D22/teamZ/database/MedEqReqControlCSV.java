package edu.wpi.cs3733.D22.teamZ.database;

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

  protected void writeMedReqCSV(List<MedEquipReq> in) {
    writeCSV(objToData(in), headers);
  }

  protected List<MedEquipReq> readMedReqCSV() throws IOException {
    return dataToObj(readCSV());
  }

  private List<MedEquipReq> dataToObj(List<List<String>> data) {
    List<MedEquipReq> ret = new ArrayList<>();

    for (List<String> a : data) {
      ret.add(
          new MedEquipReq(a.get(0), a.get(1), a.get(2), a.get(3), a.get(4), a.get(5), a.get(6)));
    }
    return ret;
  }

  private List<List<String>> objToData(List<MedEquipReq> in) {
    List<List<String>> ret = new ArrayList<>();
    for (MedEquipReq a : in) {
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
