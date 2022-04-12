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

  // FXML objects every Service Request should have.
  @FXML protected MFXButton backButton;
  @FXML protected MFXButton submitButton;
  @FXML protected MFXButton resetButton;
  // @FXML ComboBox locationField;

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

  @FXML
  protected abstract void onSubmitButtonClicked(ActionEvent event) throws SQLException;

  @FXML
  protected abstract void onResetButtonClicked(ActionEvent event) throws IOException;
}
