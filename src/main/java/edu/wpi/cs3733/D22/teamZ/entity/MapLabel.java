package edu.wpi.cs3733.D22.teamZ.entity;

import java.util.List;
import javafx.scene.control.Label;

public class MapLabel extends Label {

  private Location location;
  private List<MedicalEquipment> equip;
  private List<Employee> employee;
  // etc

  public MapLabel(mapLabelBuilder b) {
    this.location = b.location;
    this.employee = b.employee;
    this.equip = b.equip;
    createLabel();
  }

  private void createLabel() {
    locationLabel();
  }

  private void locationLabel() {}

  public boolean isOnFloor(String floor) {
    return location.getFloor().equalsIgnoreCase(floor);
  }

  public Location getLocation() {
    return location;
  }

  public static class mapLabelBuilder {
    private Location location = null;
    private List<MedicalEquipment> equip = null;
    private List<Employee> employee = null;
    private List<ServiceRequest> reqs = null;

    public mapLabelBuilder location(Location loc) {
      location = loc;
      return this;
    }

    public mapLabelBuilder equipment(List<MedicalEquipment> equip) {
      this.equip = equip;
      return this;
    }

    public mapLabelBuilder employees(List<Employee> employ) {
      this.employee = employ;
      return this;
    }
    public mapLabelBuilder requests(List<ServiceRequest> reqs) {
      this.reqs = reqs;
      return this;
    }

    public MapLabel build() {
      return new MapLabel(this);
    }
  }
}
