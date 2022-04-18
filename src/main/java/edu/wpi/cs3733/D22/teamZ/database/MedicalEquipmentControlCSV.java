package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MedicalEquipmentControlCSV extends ControlCSV {

  private static final FacadeDAO dao = FacadeDAO.getInstance();

  private final String[] headers = {"itemID", "type", "status", "currentLocation"};

  public MedicalEquipmentControlCSV(File path) {
    this.setDefaultPath(path);
  }

  protected void writeMedicalEquipmentCSV(List<MedicalEquipment> in) throws IOException {
    writeCSV(objToData(in), headers);
  }

  protected void writeMedicalEquipmentCSV(List<MedicalEquipment> in, File path) throws IOException {
    writeCSV(objToData(in), path, headers);
  }

  protected List<MedicalEquipment> readMedicalEquipmentCSV(File path) throws IOException {
    return dataToObj(readCSV(path));
  }

  protected List<MedicalEquipment> readMedicalEquipmentCSV() throws IOException {
    return dataToObj(readCSV());
  }

  private List<MedicalEquipment> dataToObj(List<List<String>> data) {
    List<MedicalEquipment> ret = new ArrayList<>();
    for (List<String> a : data) {
      ret.add(new MedicalEquipment(a.get(0), a.get(1), a.get(2), dao.getLocationByID(a.get(3))));
    }
    return ret;
  }

  protected List<List<String>> objToData(List<MedicalEquipment> in) {
    List<List<String>> ret = new ArrayList<>();

    for (MedicalEquipment a : in) {
      List<String> entry =
          new ArrayList<>(
              List.of(
                  new String[] {
                    a.getEquipmentID(),
                    a.getType(),
                    a.getStatus(),
                    a.getCurrentLocation().getNodeID(),
                  }));
      ret.add(entry);
    }
    return ret;
  }
}
