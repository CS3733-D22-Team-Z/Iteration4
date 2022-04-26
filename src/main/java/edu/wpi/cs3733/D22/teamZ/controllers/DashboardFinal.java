package edu.wpi.cs3733.D22.teamZ.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;

public class DashboardFinal implements IMenuAccess {

  private MenuController menu;
  private String menuName;

  @FXML Rectangle bedGreen5;
  @FXML Rectangle ipumpGreen5;

  @FXML
  private void initialize() {
    bedGreen5.setWidth(749);
    ipumpGreen5.setWidth(749);
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return menuName;
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
