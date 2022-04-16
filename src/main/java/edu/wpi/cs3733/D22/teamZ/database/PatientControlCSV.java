package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Patient;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PatientControlCSV extends ControlCSV {
  private final String[] headers = {"PatientID", "name", "location"};
  private final FacadeDAO dao = FacadeDAO.getInstance();

  public PatientControlCSV(File path) {
    this.setDefaultPath(path);
  }

  protected void writePatCSV(List<Patient> in) throws IOException {
    writeCSV(objToData(in), headers);
  }

  protected List<Patient> readPatCSV() throws IOException {
    return dataToObj(readCSV());
  }

  private List<Patient> dataToObj(List<List<String>> data) {
    List<Patient> ret = new ArrayList<>();
    for (List<String> a : data) {
      ret.add(new Patient(a.get(0), a.get(1), dao.getLocationByID(a.get(2))));
    }
    return ret;
  }

  protected List<List<String>> objToData(List<Patient> in) {
    List<List<String>> ret = new ArrayList<>();

    for (Patient a : in) {
      List<String> entry =
          new ArrayList<>(
              List.of(new String[] {a.getPatientID(), a.getName(), a.getLocation().getNodeID()}));
      ret.add(entry);
    }
    return ret;
  }
}
