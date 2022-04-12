package edu.wpi.cs3733.D22.teamZ.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class UpperFloorsDashboardController implements IMenuAccess {
  @FXML private Button lowerLevelButton;

  private final String toLowerLevel = "edu/wpi/cs3733/D22/teamZ/views/LowerLevelsDashboard.fxml";

  private MenuController menu;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @FXML
  public void toLowerLevelDashboard(ActionEvent event) throws IOException {
    menu.load(toLowerLevel);
  }
}
