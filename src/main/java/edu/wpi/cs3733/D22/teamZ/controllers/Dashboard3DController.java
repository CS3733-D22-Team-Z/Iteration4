package edu.wpi.cs3733.D22.teamZ.controllers;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;

public class Dashboard3DController implements IMenuAccess, Initializable {

  @FXML private AnchorPane root;
  @FXML private Group floor3;
  @FXML private Group floor4;
  @FXML private Group floor5;
  @FXML private Group floor2;
  @FXML private Group floor1;
  @FXML private Group lowerLevel1;
  @FXML private Group lowerLevel2;
  @FXML private Group model;
  @FXML private PerspectiveCamera camera;
  @FXML private PointLight lightUp;
  @FXML private PointLight lightRight;
  @FXML private PointLight lightLeft;
  @FXML private SubScene subScene;

  private final double WIDTH = 1920.0;
  private final double HEIGHT = 1020.0;

  private MenuController menu;

  private void loadModelFloor1(URL url, Group floor) {

    ObjModelImporter importer = new ObjModelImporter();
    importer.read(url);

    String[] arrayURL = {
      "edu/wpi/cs3733/D22/teamZ/images/floor1Base.png",
      "edu/wpi/cs3733/D22/teamZ/images/baseTowerMaterial.png",
      "edu/wpi/cs3733/D22/teamZ/images/doorMaterial.png",
      "edu/wpi/cs3733/D22/teamZ/images/WingMatTower.png",
      "edu/wpi/cs3733/D22/teamZ/images/WingMatTower.png",
      "edu/wpi/cs3733/D22/teamZ/images/WingMatTower.png",
      "edu/wpi/cs3733/D22/teamZ/images/WingMatTower.png",
    };

    int i = 0;
    for (MeshView view : importer.getImport()) {
      PhongMaterial material = new PhongMaterial();
      material.setDiffuseMap(
          new Image(getClass().getClassLoader().getResourceAsStream(arrayURL[i])));
      view.setMaterial(material);
      view.setScaleX(50);
      view.setScaleY(50);
      view.setScaleZ(50);
      switch (i) {
        case 0:
          break;
        case 1:
          break;
        case 2:
          view.setTranslateX(130);
          view.setTranslateZ(100);
          view.setTranslateZ(-200);
          break;
        case 3:
          view.setTranslateX(100);
          view.setTranslateZ(100);
          break;
        case 4:
          view.setTranslateX(-100);
          view.setTranslateZ(100);
          break;
        case 5:
          view.setTranslateX(100);
          view.setTranslateZ(-100);
          break;
        case 6:
          view.setTranslateX(-100);
          view.setTranslateZ(-100);
          break;
        default:
          break;
      }
      floor.getChildren().add(view);
      i++;
    }

    importer.close();
  }

  private void loadModelFloor2(URL url, Group floor) {

    ObjModelImporter importer = new ObjModelImporter();
    importer.read(url);

    String[] arrayURL = {
      "edu/wpi/cs3733/D22/teamZ/images/floor2Base.png",
      "edu/wpi/cs3733/D22/teamZ/images/baseTowerMaterial.png",
      "edu/wpi/cs3733/D22/teamZ/images/WingMatTower.png",
      "edu/wpi/cs3733/D22/teamZ/images/WingMatTower.png",
      "edu/wpi/cs3733/D22/teamZ/images/WingMatTower.png",
      "edu/wpi/cs3733/D22/teamZ/images/WingMatTower.png",
      "edu/wpi/cs3733/D22/teamZ/images/doorMaterial.png"
    };

    int i = 0;
    for (MeshView view : importer.getImport()) {
      PhongMaterial material = new PhongMaterial();
      material.setDiffuseMap(
          new Image(getClass().getClassLoader().getResourceAsStream(arrayURL[i])));
      view.setMaterial(material);
      view.setScaleX(50);
      view.setScaleY(50);
      view.setScaleZ(50);
      switch (i) {
        case 0:
          break;
        case 1:
          break;
        case 2:
          view.setTranslateX(100);
          view.setTranslateZ(100);
          break;
        case 3:
          view.setTranslateX(-100);
          view.setTranslateZ(100);
          break;
        case 4:
          view.setTranslateX(100);
          view.setTranslateZ(-100);
          break;
        case 5:
          view.setTranslateX(-100);
          view.setTranslateZ(-100);
          break;
        case 6:
          view.setTranslateX(130);
          view.setTranslateZ(100);
          view.setTranslateZ(-200);
          break;
        default:
          break;
      }
      floor.getChildren().add(view);
      i++;
    }

    importer.close();
  }

  private void loadModelFloor3Up(URL url, Group floor) {

    ObjModelImporter importer = new ObjModelImporter();
    importer.read(url);

    String[] arrayURL = {
      "edu/wpi/cs3733/D22/teamZ/images/baseTowerMaterial.png",
      "edu/wpi/cs3733/D22/teamZ/images/WingMatTower.png",
      "edu/wpi/cs3733/D22/teamZ/images/WingMatTower.png",
      "edu/wpi/cs3733/D22/teamZ/images/WingMatTower.png",
      "edu/wpi/cs3733/D22/teamZ/images/WingMatTower.png"
    };

    int i = 0;
    for (MeshView view : importer.getImport()) {
      PhongMaterial material = new PhongMaterial();
      material.setDiffuseMap(
          new Image(getClass().getClassLoader().getResourceAsStream(arrayURL[i])));
      view.setMaterial(material);
      view.setScaleX(10);
      view.setScaleY(10);
      view.setScaleZ(10);
      switch (i) {
        case 0:
          view.setTranslateX(100);
          view.setTranslateZ(100);
          break;
        case 1:
          view.setTranslateX(100);
          view.setTranslateZ(-100);
          break;
        case 2:
          view.setTranslateX(-100);
          view.setTranslateZ(-100);
          break;
        case 3:
          view.setTranslateX(-100);
          view.setTranslateZ(100);
          break;
        default:
          break;
      }

      floor.getChildren().add(view);
      i++;
    }
    importer.close();
  }

  private void loadModelFloorLL1(URL url, Group floor) {

    ObjModelImporter importer = new ObjModelImporter();
    importer.read(url);

    String[] arrayURL = {"edu/wpi/cs3733/D22/teamZ/images/lowerLevelMaterial.png"};

    int i = 0;
    for (MeshView view : importer.getImport()) {
      PhongMaterial material = new PhongMaterial();
      material.setDiffuseMap(
          new Image(getClass().getClassLoader().getResourceAsStream(arrayURL[i])));
      view.setMaterial(material);
      view.setScaleX(50);
      view.setScaleY(50);
      view.setScaleZ(50);
      floor.getChildren().add(view);
      i++;
    }
    importer.close();
  }

  private void prepareAnimation() {
    AnimationTimer timer =
        new AnimationTimer() {
          @Override
          public void handle(long l) {
            model.rotateProperty().set(model.getRotate() + 0.2);
          }
        };
    timer.start();
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "3D Dashboard";
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    loadModelFloor3Up(
        getClass().getClassLoader().getResource("edu/wpi/cs3733/D22/teamZ/models/floor3Up.obj"),
        floor3);
    loadModelFloor3Up(
        getClass().getClassLoader().getResource("edu/wpi/cs3733/D22/teamZ/models/floor3Up.obj"),
        floor4);
    loadModelFloor3Up(
        getClass().getClassLoader().getResource("edu/wpi/cs3733/D22/teamZ/models/floor3Up.obj"),
        floor5);

    loadModelFloor2(
        getClass().getClassLoader().getResource("edu/wpi/cs3733/D22/teamZ/models/floor2.obj"),
        floor2);

    loadModelFloor1(
        getClass().getClassLoader().getResource("edu/wpi/cs3733/D22/teamZ/models/floor1.obj"),
        floor1);

    loadModelFloorLL1(
        getClass().getClassLoader().getResource("edu/wpi/cs3733/D22/teamZ/models/lowerLevels.obj"),
        lowerLevel1);

    loadModelFloorLL1(
        getClass().getClassLoader().getResource("edu/wpi/cs3733/D22/teamZ/models/lowerLevels.obj"),
        lowerLevel2);

    floor3.setTranslateY(-90);
    floor3.setTranslateX(0);

    floor4.setTranslateY(-180);
    floor4.setTranslateX(0);

    floor5.setTranslateY(-270);
    floor5.setTranslateX(0);

    floor2.setTranslateY(0);
    floor2.setTranslateX(0);

    floor1.setTranslateY(90);
    floor1.setTranslateX(0);

    lowerLevel1.setTranslateY(180);
    lowerLevel2.setTranslateX(0);

    lowerLevel2.setTranslateY(270);
    lowerLevel2.setTranslateX(0);

    lightUp.setTranslateY(HEIGHT / 2 - 600);
    lightUp.setTranslateX(WIDTH / 2);
    lightUp.setTranslateZ(-800);
    lightUp.getTransforms().add(new Rotate(180, Rotate.X_AXIS));
    lightRight.setTranslateY(HEIGHT / 2);
    lightRight.setTranslateX(WIDTH / 2 + 400);
    lightUp.setTranslateZ(-800);
    lightRight.getTransforms().add(new Rotate(90, Rotate.Z_AXIS));
    lightLeft.setTranslateY(HEIGHT / 2);
    lightLeft.setTranslateX(WIDTH / 2 - 400);
    lightUp.setTranslateZ(-800);
    lightLeft.getTransforms().add(new Rotate(-90, Rotate.Z_AXIS));

    // model.getTransforms().add(new Rotate(-10, Rotate.X_AXIS));
    model.setRotationAxis(Rotate.Y_AXIS);

    model.setTranslateX(WIDTH / 2);
    model.setTranslateY(HEIGHT / 2);

    prepareAnimation();
  }

  private AnchorPane getRoot() {
    return root;
  }

  @FXML
  public void toFloor5(ActionEvent actionEvent) throws IOException {
    FloorDetailsController floorDets =
        (FloorDetailsController) menu.load("edu/wpi/cs3733/D22/teamZ/views/FloorDetails.fxml");
    floorDets.setFloor("5");
  }

  @FXML
  public void toFloor4(ActionEvent actionEvent) throws IOException {
    FloorDetailsController floorDets =
        (FloorDetailsController) menu.load("edu/wpi/cs3733/D22/teamZ/views/FloorDetails.fxml");
    floorDets.setFloor("4");
  }

  @FXML
  public void toFloor3(ActionEvent actionEvent) throws IOException {
    FloorDetailsController floorDets =
        (FloorDetailsController) menu.load("edu/wpi/cs3733/D22/teamZ/views/FloorDetails.fxml");
    floorDets.setFloor("3");
  }

  @FXML
  public void toFloor2(ActionEvent actionEvent) throws IOException {
    FloorDetailsController floorDets =
        (FloorDetailsController) menu.load("edu/wpi/cs3733/D22/teamZ/views/FloorDetails.fxml");
    floorDets.setFloor("2");
  }

  @FXML
  public void toFloor1(ActionEvent actionEvent) throws IOException {
    FloorDetailsController floorDets =
        (FloorDetailsController) menu.load("edu/wpi/cs3733/D22/teamZ/views/FloorDetails.fxml");
    floorDets.setFloor("1");
  }

  @FXML
  public void toLowerLevel1(ActionEvent actionEvent) throws IOException {
    FloorDetailsController floorDets =
        (FloorDetailsController) menu.load("edu/wpi/cs3733/D22/teamZ/views/FloorDetails.fxml");
    floorDets.setFloor("L1");
  }

  @FXML
  public void toLowerLevel2(ActionEvent actionEvent) throws IOException {
    FloorDetailsController floorDets =
        (FloorDetailsController) menu.load("edu/wpi/cs3733/D22/teamZ/views/FloorDetails.fxml");
    floorDets.setFloor("L2");
  }
}
