package edu.wpi.cs3733.D22.teamZ.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LanguageInterpreterController implements IMenuAccess {

  private final String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";

  private MenuController menu;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  // navDefault: navigates back to default page when back button is pressed
  @FXML
  public void navDefault(ActionEvent event) throws IOException {
    System.out.println("navigating to default from language interpreter");
    menu.load(toLandingPageURL);
  }
}
