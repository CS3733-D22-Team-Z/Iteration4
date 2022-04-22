package edu.wpi.cs3733.D22.teamZ.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Alert that represents the warnings per floor */
public class DashAlert {
  // Contains the data for the warning
  HashMap<String, Integer> warningMap;
  String floor;

  public DashAlert(String floor) {
    warningMap = new HashMap<>();
    this.floor = floor;
  }

  /**
   * Places a new warning sentence into the alert
   *
   * @param formatter the string that will hold the formatting
   * @param newData the data to be placed in the string
   * @param constraint the minimum value of the data
   * @param isMin whether or not the constraint is a maximum or minimum
   */
  public void putWarningData(String formatter, int newData, int constraint, boolean isMin) {
    if ((isMin && newData >= constraint) || (!isMin && newData <= constraint)) {
      warningMap.put(formatter, newData);
    } else {
      warningMap.remove(formatter);
    }
  }

  public List<String> getWarnings() {
    List<String> formattedWarnings = new ArrayList<>();
    for (String formattingString : warningMap.keySet()) {
      int value = warningMap.get(formattingString);
      formattedWarnings.add(String.format(formattingString, value));
    }
    return formattedWarnings;
  }

  public String getFloor() {
    return floor;
  }
}
