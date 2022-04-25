package edu.wpi.cs3733.D22.teamZ.controllers;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

public class Floor33DControllers {
  private final double WIDTH = 800;
  private final double HEIGHT = 600;
  private final int BASEW = 200;
  private final int BASEH = 200;

  @FXML Group group3;
  @FXML Cylinder podA3;
  @FXML Cylinder podB3;
  @FXML Cylinder podC3;
  @FXML Cylinder podD3;
  @FXML Box base3;

  @FXML
  private void initialize() {
    setFloor(320);
    group3.setTranslateX(WIDTH / 2);
    group3.setTranslateY(HEIGHT / 2);
    group3.getTransforms().add(new Rotate(10, Rotate.Z_AXIS));
  }

  private void setFloor(double angle) {
    base3.setTranslateX(0);
    base3.setTranslateY(0);
    podA3.setTranslateX(-BASEW / 2);
    podA3.setTranslateY(-BASEH / 2);
    podB3.setTranslateX(-BASEW / 2);
    podB3.setTranslateY(BASEH / 2);
    podC3.setTranslateX(BASEW / 2);
    podC3.setTranslateY(-BASEH / 2);
    podD3.setTranslateX(BASEW / 2);
    podD3.setTranslateY(BASEH / 2);

    podA3.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podB3.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podC3.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podD3.getTransforms().add(new Rotate(90, Rotate.X_AXIS));

    group3.getTransforms().add(new Rotate(angle, Rotate.X_AXIS));
    group3.getTransforms().add(new Rotate(0, Rotate.Y_AXIS));
  }
}
