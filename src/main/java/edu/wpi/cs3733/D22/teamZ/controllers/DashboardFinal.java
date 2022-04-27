package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.observers.DashboardBedAlertObserver;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;

public class DashboardFinal implements IMenuAccess {

  private final String dashboardAlert =
      "M11 15h2v2h-2v-2zm0-8h2v6h-2V7zm.99-5C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8z";
  private final String FloorDetailsURL =
      "edu.wpi.cs3733.D22.teamZ.controllers.FloorDetailsController";

  private MenuController menu;
  private String menuName;
  private final double MAXWIDTH = 659;

  @FXML Rectangle bedGreen5;
  @FXML Rectangle bedGreen4;
  @FXML Rectangle bedGreen3;
  @FXML Rectangle bedGreen2;
  @FXML Rectangle bedGreen1;
  @FXML Rectangle bedGreenLL1;
  @FXML Rectangle bedGreenLL2;
  @FXML Rectangle bedRed5;
  @FXML Rectangle bedRed4;
  @FXML Rectangle bedRed3;
  @FXML Rectangle bedRed2;
  @FXML Rectangle bedRed1;
  @FXML Rectangle bedRedLL1;
  @FXML Rectangle bedRedLL2;
  @FXML Rectangle ipumpGreen5;
  @FXML Rectangle ipumpGreen4;
  @FXML Rectangle ipumpGreen3;
  @FXML Rectangle ipumpGreen2;
  @FXML Rectangle ipumpGreen1;
  @FXML Rectangle ipumpGreenLL1;
  @FXML Rectangle ipumpGreenLL2;
  @FXML Rectangle ipumpRed5;
  @FXML Rectangle ipumpRed4;
  @FXML Rectangle ipumpRed3;
  @FXML Rectangle ipumpRed2;
  @FXML Rectangle ipumpRed1;
  @FXML Rectangle ipumpRedLL1;
  @FXML Rectangle ipumpRedLL2;
  @FXML Label cleanBeds5Label;
  @FXML Label cleanBeds4Label;
  @FXML Label cleanBeds3Label;
  @FXML Label cleanBeds2Label;
  @FXML Label cleanBeds1Label;
  @FXML Label cleanBedsLL1Label;
  @FXML Label cleanBedsLL2Label;
  @FXML Label dirtyBeds5Label;
  @FXML Label dirtyBeds4Label;
  @FXML Label dirtyBeds3Label;
  @FXML Label dirtyBeds2Label;
  @FXML Label dirtyBeds1Label;
  @FXML Label dirtyBedsLL1Label;
  @FXML Label dirtyBedsLL2Label;
  @FXML Label cleanIPumps5Label;
  @FXML Label cleanIPumps4Label;
  @FXML Label cleanIPumps3Label;
  @FXML Label cleanIPumps2Label;
  @FXML Label cleanIPumps1Label;
  @FXML Label cleanIPumpsLL1Label;
  @FXML Label cleanIPumpsLL2Label;
  @FXML Label dirtyIPumps5Label;
  @FXML Label dirtyIPumps4Label;
  @FXML Label dirtyIPumps3Label;
  @FXML Label dirtyIPumps2Label;
  @FXML Label dirtyIPumps1Label;
  @FXML Label dirtyIPumpsLL1Label;
  @FXML Label dirtyIPumpsLL2Label;
  @FXML Region error5Region;
  @FXML Region error4Region;
  @FXML Region error3Region;
  @FXML Region error2Region;
  @FXML Region error1Region;
  @FXML Region errorLL1Region;
  @FXML Region errorLL2Region;
  @FXML private VBox floor5Container;
  @FXML private VBox floor4Container;
  @FXML private VBox floor3Container;
  @FXML private VBox floor2Container;
  @FXML private VBox floor1Container;
  @FXML private VBox LL1Container;
  @FXML private VBox LL2Container;

  @FXML
  private void initialize() {
    bedGreen5.setWidth(countCleanBeds("5"));
    bedGreen4.setWidth(countCleanBeds("4"));
    bedGreen3.setWidth(countCleanBeds("3"));
    bedGreen2.setWidth(countCleanBeds("2"));
    bedGreen1.setWidth(countCleanBeds("1"));
    bedGreenLL1.setWidth(countCleanBeds("L1"));
    bedGreenLL2.setWidth(countCleanBeds("L2"));
    ipumpGreen5.setWidth(countCleanIPumps("5"));
    ipumpGreen4.setWidth(countCleanIPumps("4"));
    ipumpGreen3.setWidth(countCleanIPumps("3"));
    ipumpGreen2.setWidth(countCleanIPumps("2"));
    ipumpGreen1.setWidth(countCleanIPumps("1"));
    ipumpGreenLL1.setWidth(countCleanIPumps("LL1"));
    ipumpGreenLL2.setWidth(countCleanIPumps("LL2"));
    setLabels();
    noBeds("5", bedRed5);
    noBeds("4", bedRed4);
    noBeds("3", bedRed3);
    noBeds("2", bedRed2);
    noBeds("1", bedRed1);
    noBeds("L1", bedRedLL1);
    noBeds("L2", bedRedLL2);
    noIPumps("5", ipumpRed5);
    noIPumps("4", ipumpRed4);
    noIPumps("3", ipumpRed3);
    noIPumps("2", ipumpRed2);
    noIPumps("1", ipumpRed1);
    noIPumps("L1", ipumpRedLL1);
    noIPumps("L2", ipumpRedLL2);

    SVGPath Icon = new SVGPath();
    Icon.setContent(dashboardAlert);
    List<Region> dashRegions =
        List.of(
            error5Region,
            error4Region,
            error3Region,
            error2Region,
            error1Region,
            errorLL1Region,
            errorLL2Region);
    for (Region dashRegion : dashRegions) {
      dashRegion.setShape(Icon);
      dashRegion.setStyle("-fx-background-color: #ff8800;");
    }

    setupDropdown(floor5Container, "5");
    setupDropdown(floor4Container, "4");
    setupDropdown(floor3Container, "3");
    setupDropdown(floor2Container, "2");
    setupDropdown(floor3Container, "1");
    setupDropdown(LL1Container, "L1");
    setupDropdown(LL2Container, "L2");
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return menuName;
  }

  private double countCleanBeds(String floor) {
    double clean = FacadeDAO.getInstance().countCleanBedsByFloor(floor);
    double dirty = FacadeDAO.getInstance().countDirtyBedsByFloor(floor);
    double total = clean + dirty;
    double cleanWidth = (clean * MAXWIDTH) / total;
    return cleanWidth;
  }

  private double countCleanIPumps(String floor) {
    double clean = FacadeDAO.getInstance().countCleanIPumpsByFloor(floor);
    double dirty = FacadeDAO.getInstance().countDirtyIPumpsByFloor(floor);
    double total = clean + dirty;
    double cleanWidth = (clean * MAXWIDTH) / total;
    return cleanWidth;
  }

  private void noBeds(String floor, Rectangle rec) {
    if (FacadeDAO.getInstance().countDirtyBedsByFloor(floor) == 0
        && FacadeDAO.getInstance().countCleanBedsByFloor(floor) == 0) {
      rec.setStyle("-fx-fill: #7B7B7B; -fx-stroke: #7B7B7B;");
    }
  }

  private void noIPumps(String floor, Rectangle rec) {
    if (FacadeDAO.getInstance().countDirtyIPumpsByFloor(floor) == 0
        && FacadeDAO.getInstance().countCleanIPumpsByFloor(floor) == 0) {
      rec.setStyle("-fx-fill: #7B7B7B; -fx-stroke: #7B7B7B;");
    }
  }

  private void setLabels() {
    // Label showing clean beds
    cleanBeds5Label.setText(Integer.toString(FacadeDAO.getInstance().countCleanBedsByFloor("5")));
    cleanBeds4Label.setText(Integer.toString(FacadeDAO.getInstance().countCleanBedsByFloor("4")));
    cleanBeds3Label.setText(Integer.toString(FacadeDAO.getInstance().countCleanBedsByFloor("3")));
    cleanBeds2Label.setText(Integer.toString(FacadeDAO.getInstance().countCleanBedsByFloor("2")));
    cleanBeds1Label.setText(Integer.toString(FacadeDAO.getInstance().countCleanBedsByFloor("1")));
    cleanBedsLL1Label.setText(
        Integer.toString(FacadeDAO.getInstance().countCleanBedsByFloor("L1")));
    cleanBedsLL2Label.setText(
        Integer.toString(FacadeDAO.getInstance().countCleanBedsByFloor("L2")));
    // Label showing dirty beds
    dirtyBeds5Label.setText(Integer.toString(FacadeDAO.getInstance().countDirtyBedsByFloor("5")));
    dirtyBeds4Label.setText(Integer.toString(FacadeDAO.getInstance().countDirtyBedsByFloor("4")));
    dirtyBeds3Label.setText(Integer.toString(FacadeDAO.getInstance().countDirtyBedsByFloor("3")));
    dirtyBeds2Label.setText(Integer.toString(FacadeDAO.getInstance().countDirtyBedsByFloor("2")));
    dirtyBeds1Label.setText(Integer.toString(FacadeDAO.getInstance().countDirtyBedsByFloor("1")));
    dirtyBedsLL1Label.setText(
        Integer.toString(FacadeDAO.getInstance().countDirtyBedsByFloor("L1")));
    dirtyBedsLL2Label.setText(
        Integer.toString(FacadeDAO.getInstance().countDirtyBedsByFloor("L2")));
    // Label showing clean pumps
    cleanIPumps5Label.setText(
        Integer.toString(FacadeDAO.getInstance().countCleanIPumpsByFloor("5")));
    cleanIPumps4Label.setText(
        Integer.toString(FacadeDAO.getInstance().countCleanIPumpsByFloor("4")));
    cleanIPumps3Label.setText(
        Integer.toString(FacadeDAO.getInstance().countCleanIPumpsByFloor("3")));
    cleanIPumps2Label.setText(
        Integer.toString(FacadeDAO.getInstance().countCleanIPumpsByFloor("2")));
    cleanIPumps1Label.setText(
        Integer.toString(FacadeDAO.getInstance().countCleanIPumpsByFloor("1")));
    cleanIPumpsLL1Label.setText(
        Integer.toString(FacadeDAO.getInstance().countCleanIPumpsByFloor("LL1")));
    cleanIPumpsLL2Label.setText(
        Integer.toString(FacadeDAO.getInstance().countCleanIPumpsByFloor("LL2")));
    // Label showing dirty pumps
    dirtyIPumps5Label.setText(
        Integer.toString(FacadeDAO.getInstance().countDirtyIPumpsByFloor("5")));
    dirtyIPumps4Label.setText(
        Integer.toString(FacadeDAO.getInstance().countDirtyIPumpsByFloor("4")));
    dirtyIPumps3Label.setText(
        Integer.toString(FacadeDAO.getInstance().countDirtyIPumpsByFloor("3")));
    dirtyIPumps2Label.setText(
        Integer.toString(FacadeDAO.getInstance().countDirtyIPumpsByFloor("2")));
    dirtyIPumps1Label.setText(
        Integer.toString(FacadeDAO.getInstance().countDirtyIPumpsByFloor("1")));
    dirtyIPumpsLL1Label.setText(
        Integer.toString(FacadeDAO.getInstance().countDirtyIPumpsByFloor("LL1")));
    dirtyIPumpsLL2Label.setText(
        Integer.toString(FacadeDAO.getInstance().countDirtyIPumpsByFloor("LL2")));

    // Create observers for each dirty location
    List<Location> dirtyTest =
        List.of(
            FacadeDAO.getInstance().getLocationByID("zSTOR00305"),
            FacadeDAO.getInstance().getLocationByID("zSTOR00303"),
            FacadeDAO.getInstance().getLocationByID("zSTOR00403"),
            FacadeDAO.getInstance().getLocationByID("zSTOR00304"),
            FacadeDAO.getInstance().getLocationByID("zSTOR00404"));

    for (Location dirtyLocation : dirtyTest) {
      new DashboardBedAlertObserver(dirtyLocation, this);
    }

    List<Location> dirtyPumpLocations =
        List.of(
            FacadeDAO.getInstance().getLocationByID("zDIRT00103"),
            FacadeDAO.getInstance().getLocationByID("zDIRT00104"),
            FacadeDAO.getInstance().getLocationByID("zDIRT00105"));

    for (Location dirtyLocation : dirtyPumpLocations) {
      new DashboardBedAlertObserver(dirtyLocation, this);
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
      new DashboardBedAlertObserver(cleanLocation, this);
    }
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

  /*public void updateBedAlert(String floor, int dirtyBeds, int dirtyPumps, int cleanPumps) {
    // Get alert for this floor
    DashAlert floorAlert = alerts.get(floor);
    floorAlert.putWarningData(dirtyBedMsg, dirtyBeds, 6, true);
    floorAlert.putWarningData(dirtyPumpMsg, dirtyPumps, 15, true);
    floorAlert.putWarningData(cleanPumpMsg, cleanPumps, 5, false);
  }*/

  public void floorAlert(String floor) {
    if (floor.equals("5")) {
      error5Region.setVisible(true);
    } else if (floor.equals("4")) {
      error4Region.setVisible(true);
    } else if (floor.equals("3")) {
      error3Region.setVisible(true);
    } else if (floor.equals("2")) {
      error2Region.setVisible(true);
    } else if (floor.equals("1")) {
      error1Region.setVisible(true);
    } else if (floor.equals("L1")) {
      errorLL1Region.setVisible(true);
    } else if (floor.equals("L2")) {
      errorLL2Region.setVisible(true);
    }
  }

  private void setupDropdown(VBox dropdownBox, String floor) {
    Pane topComponents = (Pane) dropdownBox.getChildren().get(0);
    Pane bottomComponents = (Pane) dropdownBox.getChildren().get(1);

    MFXButton dropdownbutton =
        (MFXButton) topComponents.getChildren().get(topComponents.getChildren().size() - 1);
    bottomComponents
        .visibleProperty()
        .addListener(
            listener -> bottomComponents.setPrefHeight(bottomComponents.isVisible() ? 200 : 0));
    dropdownbutton.setOnAction(event -> bottomComponents.setVisible(!bottomComponents.isVisible()));
  }

  private void onClickDropdown(){

  }

}
