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
    warningData = new ArrayList<>();
    this.floor = floor;
  }

  public void addWarning(int idx, String warning) {
    warningBase.add(warning);
    warningData.add(1);
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
}
