package edu.wpi.cs3733.D22.teamZ.entity;

import javafx.scene.control.Label;

public class HospitalCode {
  private Location location;
  private String codeType;
  private Label label;

  public HospitalCode(Location location, String codeType, Label label) {
    this.location = location;
    this.codeType = codeType;
    this.label = label;
  }

  public Label getLabel() {
    return this.label;
  }

  public Location getLocation() {
    return this.location;
  }

  public String getCodeType() {
    return this.codeType;
  }
}
