package edu.wpi.cs3733.D22.teamZ.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class SimulatorController implements IMenuAccess, Initializable {
  private MenuController menu;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Simulator";
  }

  @FXML
  public void initialize(URL location, ResourceBundle resources) {}
}
