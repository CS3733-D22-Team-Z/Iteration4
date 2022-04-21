package edu.wpi.cs3733.D22.teamZ.entity;

import java.util.ArrayList;
import java.util.List;

/** Alert that represents the warnings per floor */
public class DashAlert {
  List<String> warningBase;
  List<Integer> warningData;
  String floor;

  public DashAlert(String floor) {
    warningBase = new ArrayList<>();
    warningBase.add("There are %d dirty beds on this floor.");
    warningBase.add("There are %d dirty infusion pumps on this floor.");
    warningBase.add("There are %d clean infusion pumps on this floor.");
    warningData = new ArrayList<>();
    warningData.add(0);
    warningData.add(0);
    warningData.add(0);
    this.floor = floor;
  }

  public void addWarning() {
    // warningBase.add(warning);
    warningData.add(0);
  }

  public void setWarningData(int idx, int newData) {
    warningData.set(idx, newData);
  }

  public List<String> getWarnings() {
    List<String> formattedWarnings = new ArrayList<>();
    for (int i = 0; i < warningBase.size(); i++) {
      formattedWarnings.add(String.format(warningBase.get(i), warningData.get(i)));
    }
    return formattedWarnings;
  }

  public String getFloor() {
    return floor;
  }

  public int getIndexOfFormattedString(String string) {
    for (String val : warningBase) {
      if (val.equals(string)) {
        return warningBase.indexOf(val);
      }
    }
    return 0;
  }
}
