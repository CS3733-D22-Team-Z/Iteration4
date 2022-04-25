package edu.wpi.cs3733.D22.teamZ.controllers;

import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

public class ThreeDModelController extends Application {

  private final int WIDTH = 800;
  private final int HEIGHT = 600;
  private final int BASEW = 100;
  private final int BASEH = 100;

  private final String to3DFloor3URL = "edu/wpi/cs3733/D22/teamZ/views/Floor33D.fxml";

  @FXML PerspectiveCamera camera;
  @FXML Box baseL2;
  @FXML Group groupL2;
  @FXML Box baseL1;
  @FXML Group groupL1;
  @FXML Box base1;
  @FXML Box base1a;
  @FXML Cylinder podA1;
  @FXML Cylinder podB1;
  @FXML Cylinder podC1;
  @FXML Cylinder podD1;
  @FXML Cylinder lobby1;
  @FXML Group group1;
  @FXML Box base2;
  @FXML Box base2a;
  @FXML Cylinder podA2;
  @FXML Cylinder podB2;
  @FXML Cylinder podC2;
  @FXML Cylinder podD2;
  @FXML Cylinder lobby2;
  @FXML Group group2;
  @FXML Box base3;
  @FXML Cylinder podA3;
  @FXML Cylinder podB3;
  @FXML Cylinder podC3;
  @FXML Cylinder podD3;
  @FXML Group group3;
  @FXML Box base4;
  @FXML Cylinder podA4;
  @FXML Cylinder podB4;
  @FXML Cylinder podC4;
  @FXML Cylinder podD4;
  @FXML Group group4;
  @FXML Box base5;
  @FXML Cylinder podA5;
  @FXML Cylinder podB5;
  @FXML Cylinder podC5;
  @FXML Cylinder podD5;
  @FXML Group group5;
  @FXML Group group;

  private Transform t = new Rotate();
  double speed = 0.2;
  private final double ANGLE = 278;

  @FXML
  private void initialize() {

    setColor();

    //    box.getTransforms().add(new Rotate(30, Rotate.Y_AXIS));
    //    box.getTransforms().add(new Rotate(300, Rotate.X_AXIS));

    setLowerL2Floor(90, ANGLE);
    setLowerL1Floor(60, ANGLE);
    setFirstFloor(30, ANGLE);
    setSecondFloor(0, ANGLE);
    setThirdFloor(-30, ANGLE);
    setFourthFloor(-60, ANGLE);
    setFifthFloor(-90, ANGLE);

    group.setTranslateX(WIDTH / 2);
    group.setTranslateY(HEIGHT / 2);

    group.setRotationAxis(Rotate.Y_AXIS);
    camera.setRotationAxis(Rotate.Y_AXIS);

    prepareAnimation();

    /*camera.getTransforms().add(new Rotate(-35, Rotate.X_AXIS));
    camera.getTransforms().add(new Translate(0, 0, 10));*/

    /*group.setOnKeyPressed(
    event -> {
      switch (event.getCode()) {
        case W:
          group.translateZProperty().set(group.getTranslateZ() + 10);
          break;
        case S:
          group.translateZProperty().set(group.getTranslateZ() - 10);
          break;
        case Q:
          t = t.createConcatenation(new Rotate(10, Rotate.X_AXIS));
          group.getTransforms().clear();
          group.getTransforms().addAll(t);
          break;
        case E:
          t = t.createConcatenation(new Rotate(-10, Rotate.X_AXIS));
          group.getTransforms().clear();
          group.getTransforms().addAll(t);
          break;
        case Z:
          t = t.createConcatenation(new Rotate(10, Rotate.Z_AXIS));
          group.getTransforms().clear();
          group.getTransforms().addAll(t);
          break;
        case X:
          t = t.createConcatenation(new Rotate(-10, Rotate.Z_AXIS));
          group.getTransforms().clear();
          group.getTransforms().addAll(t);
          break;
      }
    });*/
  }

  private void setColor() {
    PhongMaterial pmHigher = new PhongMaterial(Color.rgb(179, 165, 141, 1));
    PhongMaterial pmLobbyFront = new PhongMaterial(Color.rgb(155, 138, 130, 1));
    PhongMaterial pmLower = new PhongMaterial(Color.rgb(102, 73, 58, 1));
    PhongMaterial pmLobbyBack = new PhongMaterial(Color.rgb(161, 194, 204, 1));

    baseL1.setMaterial(pmLower);
    baseL2.setMaterial(pmLower);

    base1a.setMaterial(pmLobbyFront);
    base2a.setMaterial(pmLobbyFront);

    base1.setMaterial(pmHigher);
    base2.setMaterial(pmHigher);
    base3.setMaterial(pmHigher);
    base4.setMaterial(pmHigher);
    base5.setMaterial(pmHigher);

    podA1.setMaterial(pmHigher);
    podA2.setMaterial(pmHigher);
    podA3.setMaterial(pmHigher);
    podA4.setMaterial(pmHigher);
    podA5.setMaterial(pmHigher);

    podB1.setMaterial(pmHigher);
    podB2.setMaterial(pmHigher);
    podB3.setMaterial(pmHigher);
    podB4.setMaterial(pmHigher);
    podB5.setMaterial(pmHigher);

    podC1.setMaterial(pmHigher);
    podC2.setMaterial(pmHigher);
    podC3.setMaterial(pmHigher);
    podC4.setMaterial(pmHigher);
    podC5.setMaterial(pmHigher);

    podD1.setMaterial(pmHigher);
    podD2.setMaterial(pmHigher);
    podD3.setMaterial(pmHigher);
    podD4.setMaterial(pmHigher);
    podD5.setMaterial(pmHigher);

    lobby1.setMaterial(pmLobbyBack);
    lobby2.setMaterial(pmLobbyBack);
  }

  private void setFirstFloor(double displacement, double angle) {
    base1.setTranslateX(0);
    base1.setTranslateY(0);
    podA1.setTranslateX(-BASEW / 2);
    podA1.setTranslateY(-BASEH / 2);
    podB1.setTranslateX(-BASEW / 2);
    podB1.setTranslateY(BASEH / 2);
    podC1.setTranslateX(BASEW / 2);
    podC1.setTranslateY(-BASEH / 2);
    podD1.setTranslateX(BASEW / 2);
    podD1.setTranslateY(BASEH / 2);

    group1.setTranslateX(0);
    group1.setTranslateY(displacement);

    podA1.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podB1.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podC1.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podD1.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    lobby1.getTransforms().add(new Rotate(90, Rotate.X_AXIS));

    group1.getTransforms().add(new Rotate(angle, Rotate.X_AXIS));
    group1.getTransforms().add(new Rotate(0, Rotate.Y_AXIS));
  }

  private void setSecondFloor(double displacement, double angle) {
    base2.setTranslateX(0);
    base2.setTranslateY(0);
    podA2.setTranslateX(-BASEW / 2);
    podA2.setTranslateY(-BASEH / 2);
    podB2.setTranslateX(-BASEW / 2);
    podB2.setTranslateY(BASEH / 2);
    podC2.setTranslateX(BASEW / 2);
    podC2.setTranslateY(-BASEH / 2);
    podD2.setTranslateX(BASEW / 2);
    podD2.setTranslateY(BASEH / 2);

    group2.setTranslateX(0);
    group2.setTranslateY(displacement);

    podA2.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podB2.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podC2.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podD2.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    lobby2.getTransforms().add(new Rotate(90, Rotate.X_AXIS));

    group2.getTransforms().add(new Rotate(angle, Rotate.X_AXIS));
    group2.getTransforms().add(new Rotate(0, Rotate.Y_AXIS));
  }

  private void setThirdFloor(double displacement, double angle) {
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

    group3.setTranslateX(0);
    group3.setTranslateY(displacement);

    podA3.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podB3.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podC3.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podD3.getTransforms().add(new Rotate(90, Rotate.X_AXIS));

    group3.getTransforms().add(new Rotate(angle, Rotate.X_AXIS));
    group3.getTransforms().add(new Rotate(0, Rotate.Y_AXIS));
  }

  private void setFourthFloor(double displacement, double angle) {
    base4.setTranslateX(0);
    base4.setTranslateY(0);
    podA4.setTranslateX(-BASEW / 2);
    podA4.setTranslateY(-BASEH / 2);
    podB4.setTranslateX(-BASEW / 2);
    podB4.setTranslateY(BASEH / 2);
    podC4.setTranslateX(BASEW / 2);
    podC4.setTranslateY(-BASEH / 2);
    podD4.setTranslateX(BASEW / 2);
    podD4.setTranslateY(BASEH / 2);

    group4.setTranslateX(0);
    group4.setTranslateY(displacement);

    podA4.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podB4.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podC4.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podD4.getTransforms().add(new Rotate(90, Rotate.X_AXIS));

    group4.getTransforms().add(new Rotate(angle, Rotate.X_AXIS));
    group4.getTransforms().add(new Rotate(0, Rotate.Y_AXIS));
  }

  private void setFifthFloor(double displacement, double angle) {
    base5.setTranslateX(0);
    base5.setTranslateY(0);
    podA5.setTranslateX(-BASEW / 2);
    podA5.setTranslateY(-BASEH / 2);
    podB5.setTranslateX(-BASEW / 2);
    podB5.setTranslateY(BASEH / 2);
    podC5.setTranslateX(BASEW / 2);
    podC5.setTranslateY(-BASEH / 2);
    podD5.setTranslateX(BASEW / 2);
    podD5.setTranslateY(BASEH / 2);

    group5.setTranslateX(0);
    group5.setTranslateY(displacement);

    podA5.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podB5.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podC5.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podD5.getTransforms().add(new Rotate(90, Rotate.X_AXIS));

    group5.getTransforms().add(new Rotate(angle, Rotate.X_AXIS));
    group5.getTransforms().add(new Rotate(0, Rotate.Y_AXIS));
  }

  private void setLowerL1Floor(double displacement, double angle) {
    baseL1.setTranslateX(0);
    baseL1.setTranslateY(0);

    groupL1.setTranslateX(0);
    groupL1.setTranslateY(displacement);

    groupL1.getTransforms().add(new Rotate(angle, Rotate.X_AXIS));
    groupL1.getTransforms().add(new Rotate(0, Rotate.Y_AXIS));
  }

  private void setLowerL2Floor(double displacement, double angle) {
    baseL2.setTranslateX(0);
    baseL2.setTranslateY(0);

    groupL2.setTranslateX(0);
    groupL2.setTranslateY(displacement);

    groupL2.getTransforms().add(new Rotate(angle, Rotate.X_AXIS));
    groupL2.getTransforms().add(new Rotate(0, Rotate.Y_AXIS));
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root =
        FXMLLoader.load(
            getClass()
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamZ/views/ThreeDModel.fxml"));

    /*
    SmartGroup group = new SmartGroup();

    Box base = new Box(50, 50, 20);
    Cylinder podA = new Cylinder(20, 20);
    Cylinder podB = new Cylinder(20, 20);
    Cylinder podC = new Cylinder(20, 20);
    Cylinder podD = new Cylinder(20, 20);

    podA.setTranslateX(-25);
    podA.setTranslateY(-25);
    podB.setTranslateX(-25);
    podB.setTranslateY(25);
    podC.setTranslateX(25);
    podC.setTranslateY(-25);
    podD.setTranslateX(25);
    podD.setTranslateY(25);

    group.getChildren().add(base);
    group.getChildren().add(podA);
    group.getChildren().add(podB);
    group.getChildren().add(podC);
    group.getChildren().add(podD);

    root.getChildrenUnmodifiable().add(group);

    PerspectiveCamera camera = new PerspectiveCamera(true);
    camera.setTranslateZ(-80);

    root.getChildrenUnmodifiable().add(camera);*/

    Scene scene = new Scene(root);
    scene.setFill(Color.SILVER);
    scene.setOnKeyPressed(
        new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
            switch (event.getCode()) {
              case DIGIT3:
                try {
                  Parent floor3 =
                      FXMLLoader.load(getClass().getClassLoader().getResource(to3DFloor3URL));
                  primaryStage.setScene(new Scene(floor3));
                  primaryStage.show();
                } catch (IOException e) {
                  e.printStackTrace();
                }
                break;
              case S:
                group.translateZProperty().set(group.getTranslateZ() - 10);
                break;
            }
          }
        });
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    /* TODO: Make decision about parsing command line arguments. */
    launch(args);
  }

  private void prepareAnimation() {
    AnimationTimer timer =
        new AnimationTimer() {
          @Override
          public void handle(long l) {
            if (group.getRotate() >= 30) {
              speed *= -1;
            } else if (group.getRotate() <= -30) {
              speed *= -1;
            }
            group.rotateProperty().set(group.getRotate() + speed);
          }
        };
    timer.start();
  }

  @FXML
  public void toFloor3Clicked() throws IOException {
    FXMLLoader.load(getClass().getClassLoader().getResource(to3DFloor3URL));
  }
}
