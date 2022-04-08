package edu.wpi.cs3733.D22.teamZ.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MealServiceController implements IMenuAccess {
  @FXML private Button backButton;
  @FXML private Button mealButton;

  private String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";

  private MenuController menu;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @FXML
  private void toLandingPage() throws IOException {
    menu.load(toLandingPageURL);
  }
}
