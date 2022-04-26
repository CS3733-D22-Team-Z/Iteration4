package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;

public class DashboardFinal implements IMenuAccess {

  private MenuController menu;
  private String menuName;
  private final double MAXWIDTH = 749;

  @FXML Rectangle bedGreen5;
  @FXML Rectangle ipumpGreen5;

  @FXML
  private void initialize() {
    bedGreen5.setWidth(countCleanBeds("5"));
    ipumpGreen5.setWidth(countCleanIPumps("5"));
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

  @FXML
  public void toFloor5(ActionEvent actionEvent) {}

  @FXML
  public void toFloor4(ActionEvent actionEvent) {}

  @FXML
  public void toFloor3(ActionEvent actionEvent) {}

  @FXML
  public void toFloor2(ActionEvent actionEvent) {}

  @FXML
  public void toFloor1(ActionEvent actionEvent) {}

  @FXML
  public void toLowerLevel1(ActionEvent actionEvent) {}

  @FXML
  public void toLowerLevel2(ActionEvent actionEvent) {}
}
