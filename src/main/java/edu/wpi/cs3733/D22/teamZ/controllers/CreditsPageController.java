package edu.wpi.cs3733.D22.teamZ.controllers;

public class CreditsPageController implements IMenuAccess {

  private MenuController menu;
  private final String toAPILandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/APILandingPage.fxml";

  private final String grey = "#0067B1";
  private final String svgCSSLine = "-fx-background-color: %s";

  private final String externalTransportCSSPath =
      "edu/wpi/cs3733/D22/teamZ/styles/ServiceRequestDefault.css";

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Credits Page landing";
  }

  public void initialize() {}
}
