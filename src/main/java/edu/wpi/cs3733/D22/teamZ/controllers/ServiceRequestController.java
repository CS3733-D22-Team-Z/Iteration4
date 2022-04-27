package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

/**
 * A class that all service request controllers will inherit from. Each Service Request will have...
 * *A function to set the menu for the class
 */
public abstract class ServiceRequestController implements Initializable, IMenuAccess {
  // A link to the side menu
  protected MenuController menu;

  // Changes per every implementation
  // The name of the page that will be displayed in the bottom menu bar.
  protected String menuName;

  // The facade DAO that will be used by each service request page
  protected FacadeDAO database;

  // Path that will be loaded when back button is pressed.
  protected String backButtonPath = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";

  // SVGs
  protected String helpIconSVG =
      "M36 64H44V56H36V64ZM40 0C17.92 0 0 17.92 0 40C0 62.08 17.92 80 40 80C62.08 80 80 62.08 80 40C80 17.92 62.08 0 40 0ZM40 72C22.36 72 8 57.64 8 40C8 22.36 22.36 8 40 8C57.64 8 72 22.36 72 40C72 57.64 57.64 72 40 72ZM40 16C31.16 16 24 23.16 24 32H32C32 27.6 35.6 24 40 24C44.4 24 48 27.6 48 32C48 40 36 39 36 52H44C44 43 56 42 56 32C56 23.16 48.84 16 40 16Z";

  // FXML objects every Service Request should have.
  @FXML protected MFXButton backButton;
  @FXML protected MFXButton submitButton;
  @FXML protected MFXButton resetButton;
  @FXML private Region helpGraphic;
  // @FXML ComboBox locationField;

  private String svgCSSLine = "-fx-background-color: %s";

  // Help Logic
  protected boolean isHelpOn = false;

  /** Constructor for ServiceRequestController class, which initializes the database. */
  public ServiceRequestController() {
    database = FacadeDAO.getInstance();
  }

  /**
   * Sets this controller's menu to the current MenuController class.
   *
   * @param menu the menu controller
   */
  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  public String getMenuName() {
    return menuName;
  }

  /**
   * Initialization method that every service request will implement
   *
   * @param location idk
   * @param resources idk
   */
  @Override
  public abstract void initialize(URL location, ResourceBundle resources);

  @FXML
  protected void onBackButtonClicked(ActionEvent event) throws IOException {
    menu.load(backButtonPath);
  }

  protected void initializeHelpGraphic() {
    // Initialize help Graphic
    SVGPath helpIcon = new SVGPath();
    helpIcon.setContent(helpIconSVG);
    helpGraphic.setShape(helpIcon);
    helpGraphic.setStyle(String.format(svgCSSLine, "#0062A9"));
  }

  @FXML
  protected abstract void onSubmitButtonClicked(ActionEvent event) throws SQLException;

  @FXML
  protected abstract void onResetButtonClicked(ActionEvent event) throws IOException;

  @FXML
  public void onHelpMenu() {
    if (isHelpOn) {
      helpGraphic.setStyle(null);
      helpGraphic.setStyle(String.format(svgCSSLine, "#0062A9"));
      highlightRequirements(false);
      isHelpOn = false;
    } else {
      helpGraphic.setStyle(null);
      helpGraphic.setStyle(String.format(svgCSSLine, "#f6bd38"));
      highlightRequirements(true);
      isHelpOn = true;
    }
  }

  protected abstract void highlightRequirements(boolean visible);

  protected void enableToolTipOnLabel(Label label, String text) {
    Tooltip tempTT = new Tooltip(text);
    tempTT.setShowDuration(new Duration(1000000));
    tempTT.setHideDelay(new Duration(0));
    label.setTooltip(tempTT);
  }
}
