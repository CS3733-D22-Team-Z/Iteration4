package edu.wpi.cs3733.D22.teamZ.controllers;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.DashAlert;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.observers.Dashboard3DAlertObserver;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.SVGPath;
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
  @FXML Region errorRegion5;
  @FXML Region errorRegion4;
  @FXML Region errorRegion3;
  @FXML Region errorRegion2;
  @FXML Region errorRegion1;
  @FXML Region errorRegionLL1;
  @FXML Region errorRegionLL2;
  @FXML private MFXButton errorButton5;
  @FXML private MFXButton errorButton4;
  @FXML private MFXButton errorButton3;
  @FXML private MFXButton errorButton2;
  @FXML private MFXButton errorButton1;
  @FXML private MFXButton errorButtonLL1;
  @FXML private MFXButton errorButtonLL2;
  @FXML private HBox errorHolder;
  @FXML private Pane errorPane5;
  @FXML private Pane errorPane4;
  @FXML private Pane errorPane3;
  @FXML private Pane errorPane2;
  @FXML private Pane errorPane1;
  @FXML private Pane errorPaneLL1;
  @FXML private Pane errorPaneLL2;

  private final String dashboardAlert =
      "M11 15h2v2h-2v-2zm0-8h2v6h-2V7zm.99-5C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8z";

  private final String dirtyBedMsg = "There are %d dirty beds on this floor.";
  private final String dirtyPumpMsg = "There are %d dirty pumps on this floor.";
  private final String cleanPumpMsg = "There are only %d clean pumps on this floor.";

  private final String to2DDashboardURL = "edu/wpi/cs3733/D22/teamZ/views/DashboardFinal.fxml";

  private HashMap<String, DashAlert> alerts;

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
      /*PhongMaterial material = new PhongMaterial();
      material.setDiffuseMap(
          new Image(getClass().getClassLoader().getResourceAsStream(arrayURL[i])));
      view.setMaterial(material);*/
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
    alerts = new HashMap<>();
    List<String> floors = List.of("5", "4", "3", "2", "1", "LL1", "LL2");
    for (String floor : floors) {
      alerts.put(floor, new DashAlert(floor));
    }

    SVGPath Icon = new SVGPath();
    Icon.setContent(dashboardAlert);
    List<Region> dashRegions =
        List.of(
            errorRegion5,
            errorRegion4,
            errorRegion3,
            errorRegion2,
            errorRegion1,
            errorRegionLL1,
            errorRegionLL2);
    for (Region dashRegion : dashRegions) {
      dashRegion.setShape(Icon);
      dashRegion.setStyle("-fx-background-color: #ff8800;");
    }

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

    // Create observers for each dirty location
    List<Location> dirtyTest =
        List.of(
            FacadeDAO.getInstance().getLocationByID("zSTOR00305"),
            FacadeDAO.getInstance().getLocationByID("zSTOR00303"),
            FacadeDAO.getInstance().getLocationByID("zSTOR00403"),
            FacadeDAO.getInstance().getLocationByID("zSTOR00304"),
            FacadeDAO.getInstance().getLocationByID("zSTOR00404"));

    for (Location dirtyLocation : dirtyTest) {
      new Dashboard3DAlertObserver(dirtyLocation, this);
    }

    List<Location> dirtyPumpLocations =
        List.of(
            FacadeDAO.getInstance().getLocationByID("zDIRT00103"),
            FacadeDAO.getInstance().getLocationByID("zDIRT00104"),
            FacadeDAO.getInstance().getLocationByID("zDIRT00105"));

    for (Location dirtyLocation : dirtyPumpLocations) {
      new Dashboard3DAlertObserver(dirtyLocation, this);
    }

    List<Location> cleanPumpLocations =
        List.of(
            FacadeDAO.getInstance().getLocationByID("zSTOR00103"),
            FacadeDAO.getInstance().getLocationByID("zSTOR00203"),
            FacadeDAO.getInstance().getLocationByID("zSTOR00104"),
            FacadeDAO.getInstance().getLocationByID("zSTOR00204"),
            FacadeDAO.getInstance().getLocationByID("zSTOR00105"),
            FacadeDAO.getInstance().getLocationByID("zSTOR00205"));

    for (Location cleanLocation : cleanPumpLocations) {
      new Dashboard3DAlertObserver(cleanLocation, this);
    }
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

  public void updateBedAlert(String floor, int dirtyBeds, int dirtyPumps, int cleanPumps) {
    // Get alert for this floor
    DashAlert floorAlert = alerts.get(floor);
    floorAlert.putWarningData(dirtyBedMsg, dirtyBeds, 6, true);
    floorAlert.putWarningData(dirtyPumpMsg, dirtyPumps, 10, true);
    floorAlert.putWarningData(cleanPumpMsg, cleanPumps, 5, false);
  }

  public void floorAlert(String floor) {
    if (floor.equals("5")) {
      errorRegion5.setVisible(true);
      errorButton5.setDisable(false);
    } else if (floor.equals("4")) {
      errorRegion4.setVisible(true);
      errorButton4.setDisable(false);
    } else if (floor.equals("3")) {
      errorRegion3.setVisible(true);
      errorButton3.setDisable(false);
    } else if (floor.equals("2")) {
      errorRegion2.setVisible(true);
      errorButton2.setDisable(false);
    } else if (floor.equals("1")) {
      errorRegion1.setVisible(true);
      errorButton1.setDisable(false);
    } else if (floor.equals("L1")) {
      errorRegionLL1.setVisible(true);
      errorButtonLL1.setDisable(false);
    } else if (floor.equals("L2")) {
      errorRegionLL2.setVisible(true);
      errorButtonLL2.setDisable(false);
    }
  }

  public void openPane5(ActionEvent event) {
    VBox panes = (VBox) errorHolder.getChildren().get(1);
    ObservableList<Node> panesList = panes.getChildren();
    for (Node node : panesList) {
      if (!errorPane5.equals((Pane) node)) {
        node.setVisible(false);
      }
    }
    String floor = ("5");
    setLabels(errorPane5, floor);
  }

  public void openPane4(ActionEvent event) {
    VBox panes = (VBox) errorHolder.getChildren().get(1);
    ObservableList<Node> panesList = panes.getChildren();
    for (Node node : panesList) {
      if (!errorPane4.equals((Pane) node)) {
        node.setVisible(false);
      }
    }
    String floor = ("4");
    setLabels(errorPane4, floor);
  }

  public void openPane3(ActionEvent event) {
    VBox panes = (VBox) errorHolder.getChildren().get(1);
    ObservableList<Node> panesList = panes.getChildren();
    for (Node node : panesList) {
      if (!errorPane3.equals((Pane) node)) {
        node.setVisible(false);
      }
    }
    String floor = ("3");
    setLabels(errorPane3, floor);
  }

  public void openPane2(ActionEvent event) {
    VBox panes = (VBox) errorHolder.getChildren().get(1);
    ObservableList<Node> panesList = panes.getChildren();
    for (Node node : panesList) {
      if (!errorPane2.equals((Pane) node)) {
        node.setVisible(false);
      }
    }
    String floor = ("2");
    setLabels(errorPane2, floor);
  }

  public void openPane1(ActionEvent event) {
    VBox panes = (VBox) errorHolder.getChildren().get(1);
    ObservableList<Node> panesList = panes.getChildren();
    for (Node node : panesList) {
      if (!errorPane1.equals((Pane) node)) {
        node.setVisible(false);
      }
    }
    String floor = ("1");
    setLabels(errorPane1, floor);
  }

  public void openPaneLL1(ActionEvent event) {
    VBox panes = (VBox) errorHolder.getChildren().get(1);
    ObservableList<Node> panesList = panes.getChildren();
    for (Node node : panesList) {
      if (!errorPaneLL1.equals((Pane) node)) {
        node.setVisible(false);
      }
    }
    String floor = ("L1");
    setLabels(errorPaneLL1, floor);
  }

  public void openPaneLL2(ActionEvent event) {
    VBox panes = (VBox) errorHolder.getChildren().get(1);
    ObservableList<Node> panesList = panes.getChildren();
    for (Node node : panesList) {
      if (!errorPaneLL2.equals((Pane) node)) {
        node.setVisible(false);
      }
    }
    String floor = ("L2");
    setLabels(errorPaneLL2, floor);
  }

  private void setLabels(Pane node, String floor) {
    node.setVisible(!node.isVisible());

    VBox labelContainer = (VBox) root.lookup("#warningContainer" + floor);
    labelContainer.getChildren().clear();

    // Get floor alert
    DashAlert floorAlert = alerts.get(floor);

    // Load labels into warning
    for (String message : floorAlert.getWarnings()) {
      Label infoLabel = new Label();
      infoLabel.setText(message);
      infoLabel.getStyleClass().add("object-body");
      labelContainer.getChildren().add(infoLabel);
      infoLabel.setWrapText(true);
    }
  }

  public void to2DDashboard(ActionEvent event) throws IOException {
    menu.load(to2DDashboardURL);
  }
}
