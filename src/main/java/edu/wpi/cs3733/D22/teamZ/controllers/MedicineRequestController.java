package edu.wpi.cs3733.D22.teamZ.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MedicineRequestController implements IMenuAccess {
  @FXML private Label label;

  private final String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";

  private MenuController menu;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @FXML
  private void backToDashboard(ActionEvent event) throws IOException {
    menu.load(toLandingPageURL);
  }
}
