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

  public Location getLocation() {
    return location;
  }

  public String getCodeType() {
    return codeType;
  }

  public Label getLabel() {
    return label;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public void setCodeType(String codeType) {
    this.codeType = codeType;
  }

  public void setLabel(Label label) {
    this.label = label;
  }
}
