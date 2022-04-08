package edu.wpi.cs3733.D22.teamZ.controllers;

import java.io.IOException;

public class LaundryServiceController implements IMenuAccess {

  private final String toLandingPage = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";

  private MenuController menu;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  public void navDefault(javafx.event.ActionEvent actionEvent) throws IOException {
    menu.load(toLandingPage);
  }
}
