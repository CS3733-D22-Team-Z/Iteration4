package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import java.io.IOException;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;

public class DashboardFinal implements IMenuAccess {

  private final String dashboardAlert =
      "M11 15h2v2h-2v-2zm0-8h2v6h-2V7zm.99-5C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8z";

  private MenuController menu;
  private String menuName;
  private final double MAXWIDTH = 749;

  @FXML Rectangle bedGreen5;
  @FXML Rectangle bedGreen4;
  @FXML Rectangle bedGreen3;
  @FXML Rectangle bedGreen2;
  @FXML Rectangle bedGreen1;
  @FXML Rectangle bedGreenLL1;
  @FXML Rectangle bedGreenLL2;
  @FXML Rectangle ipumpGreen5;
  @FXML Rectangle ipumpGreen4;
  @FXML Rectangle ipumpGreen3;
  @FXML Rectangle ipumpGreen2;
  @FXML Rectangle ipumpGreen1;
  @FXML Rectangle ipumpGreenLL1;
  @FXML Rectangle ipumpGreenLL2;
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
  @FXML Region error5;
  @FXML Region error4;
  @FXML Region error3;
  @FXML Region error2;
  @FXML Region error1;
  @FXML Region errorLL1;
  @FXML Region errorLL2;

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

    SVGPath Icon = new SVGPath();
    Icon.setContent(dashboardAlert);
    List<Region> dashRegions = List.of(error5, error4, error3, error2, error1, errorLL1, errorLL2);
    for (Region dashRegion : dashRegions) {
      dashRegion.setShape(Icon);
      dashRegion.setStyle("-fx-background-color: #ff8800;");
    }
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
  }

  @FXML
  public void toFloor5(ActionEvent actionEvent) {}

  @FXML
  public void toFloor4(ActionEvent actionEvent) {}

  @FXML
  public void toFloor3(ActionEvent actionEvent) throws IOException {
    FloorDetailsController floorDets =
        (FloorDetailsController) menu.load("edu/wpi/cs3733/D22/teamZ/views/FloorDetails.fxml");
    floorDets.setFloor("3");
  }

  @FXML
  public void toFloor2(ActionEvent actionEvent) {}

  @FXML
  public void toFloor1(ActionEvent actionEvent) {}

  @FXML
  public void toLowerLevel1(ActionEvent actionEvent) {}

  @FXML
  public void toLowerLevel2(ActionEvent actionEvent) {}

  // Drop down for warnings
  static class DropdownRow {
    // SimpleString Property id;
    SimpleStringProperty type;
    SimpleStringProperty status;
    SimpleStringProperty location;

    public DropdownRow(String type, String status, String location) {
      // this.id = new SimpleStringProperty(is);
      this.type = new SimpleStringProperty(type);
      this.status = new SimpleStringProperty(status);
      this.location = new SimpleStringProperty(location);
    }
  }
}
