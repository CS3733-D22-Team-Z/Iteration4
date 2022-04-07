package edu.wpi.cs3733.D22.teamZ.entity;

import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class MapLabel {

  private Location location;
  private List<MedicalEquipment> equip;
  private List<Employee> employee;
  private Label label = new Label();
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

  private void locationLabel() {
    // stylize label icon
    Image locationImg = new Image("edu/wpi/cs3733/D22/teamZ/images/location.png");
    ImageView locationIcon = new ImageView(locationImg);

    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(5.0);
    dropShadow.setOffsetX(3.0);
    dropShadow.setOffsetY(3.0);
    dropShadow.setColor(Color.GRAY);

    // create the label
    label.setEffect(dropShadow);
    label.setGraphic(locationIcon);

    label
        .focusedProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (!newValue) {
                label.setScaleX(1);
                label.setScaleY(1);
                // returnOnClick();
              } else {
                label.setScaleX(2);
                label.setScaleY(2);
              }
            });

    label.setOnMouseClicked(
        evt -> {
          label.requestFocus();
        });
    // place label at correct coords
    label.relocate(location.getXcoord() - 8, location.getYcoord() - 10);
  }

  public boolean isOnFloor(String floor) {
    return location.getFloor().equalsIgnoreCase(floor);
  }

  public Label getLabel() {
    return this.label;
  }

  public Location getLocation() {
    return location;
  }

  public static class mapLabelBuilder {
    private Location location = null;
    private List<MedicalEquipment> equip = null;
    private List<Employee> employee = null;

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

    public MapLabel build() {
      return new MapLabel(this);
    }
  }
}
