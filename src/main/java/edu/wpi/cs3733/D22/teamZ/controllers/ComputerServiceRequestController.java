package edu.wpi.cs3733.D22.teamZ.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ComputerServiceRequestController implements IMenuAccess {
  @FXML private Button backButton;
  @FXML private Button submitButton;
  @FXML private TextField roomNumberField;
  @FXML private TextArea descriptionArea;

  private final String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";

  private MenuController menu;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @FXML
  void backToDashboard() throws IOException {
    menu.load(toLandingPageURL);
  }
}
