package edu.wpi.cs3733.D22.teamZ.controllers;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
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
  @FXML Cylinder cPodB;
  @FXML Cylinder cPodC;
  @FXML Box bedParkU;
  @FXML Box bedParkD;
  @FXML PerspectiveCamera camera;
  @FXML Box dirtyPod;
  @FXML PointLight light;

  @FXML
  private void initialize() {
    setFloor(320, 25);
    setColors();
    group3.setTranslateX(WIDTH / 2);
    group3.setTranslateY(HEIGHT / 2);
    group3.getTransforms().add(new Rotate(10, Rotate.Z_AXIS));

    light.setTranslateY(HEIGHT / 2 - 300);
    light.setTranslateX(WIDTH / 2);
    light.setTranslateZ(-250);
  }

  private void setColors() {
    PhongMaterial pmClean = new PhongMaterial(Color.GREEN);
    PhongMaterial pmDirty = new PhongMaterial(Color.RED);
    PhongMaterial pmHigher = new PhongMaterial(Color.rgb(179, 165, 141, 1));

    podA3.setMaterial(pmHigher);
    podB3.setMaterial(pmHigher);
    podC3.setMaterial(pmHigher);
    podD3.setMaterial(pmHigher);
    base3.setMaterial(pmHigher);
    cPodB.setMaterial(pmClean);
    cPodC.setMaterial(pmClean);
    bedParkU.setMaterial(pmDirty);
    bedParkD.setMaterial(pmDirty);
    dirtyPod.setMaterial(pmDirty);
  }

  private void setFloor(double angle, double yDisplacement) {
    base3.setTranslateX(0);
    base3.setTranslateY(0);
    podA3.setTranslateX(BASEW / 2);
    podA3.setTranslateY(-BASEH / 2);
    podB3.setTranslateX(-BASEW / 2);
    podB3.setTranslateY(-BASEH / 2);
    podC3.setTranslateX(-BASEW / 2);
    podC3.setTranslateY(BASEH / 2);
    podD3.setTranslateX(BASEW / 2);
    podD3.setTranslateY(BASEH / 2);

    podA3.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podB3.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podC3.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podD3.getTransforms().add(new Rotate(90, Rotate.X_AXIS));

    cPodB.setTranslateX(-BASEW / 2);
    cPodB.setTranslateY(-BASEH / 2 - yDisplacement);
    cPodB.setTranslateZ(15);
    cPodB.getTransforms().add(new Rotate(90, Rotate.X_AXIS));

    cPodC.setTranslateX(-BASEW / 2);
    cPodC.setTranslateY(BASEH / 2 - yDisplacement);
    cPodC.setTranslateZ(15);
    cPodC.getTransforms().add(new Rotate(90, Rotate.X_AXIS));

    bedParkD.setTranslateX(0);
    bedParkD.setTranslateY(75 - yDisplacement);
    bedParkD.setTranslateZ(15);

    bedParkU.setTranslateX(0);
    bedParkU.setTranslateY(-75 - yDisplacement + 10);
    bedParkU.setTranslateZ(15);

    dirtyPod.setTranslateX(-75);
    dirtyPod.setTranslateY(0 - yDisplacement);
    dirtyPod.setTranslateZ(15);

    group3.getTransforms().add(new Rotate(angle, Rotate.X_AXIS));
    group3.getTransforms().add(new Rotate(0, Rotate.Y_AXIS));
  }
}
