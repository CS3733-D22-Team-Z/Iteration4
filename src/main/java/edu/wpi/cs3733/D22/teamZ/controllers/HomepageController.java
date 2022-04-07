package edu.wpi.cs3733.D22.teamZ.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HomepageController implements IMenuAccess {
  @FXML private Label label;
  @FXML private Button exitButton; // ??????
  @FXML private Label welcomeMessage;

  private MenuController menu;

  private String toLocationsURL = "edu/wpi/cs3733/D22/teamZ/views/Location.fxml";
  private String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";
  private String toMedicalEquipmentRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentRequestList.fxml";

  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @FXML
  private void toLocations() throws IOException {
    System.out.println("navigating to locations from home");
    menu.load(toLocationsURL);
  }

  @FXML
  private void toLandingPage() throws IOException {
    System.out.println("navigating to landing page from home");
    menu.load(toLandingPageURL);
  }

  @FXML
  private void toMedicalEquipmentRequest() throws IOException {
    System.out.println("navigating to Medical Equipment Request page from home");
    menu.load(toMedicalEquipmentRequestURL);
  }

  /**
   * Updates the welcome message with the username of who logged in
   *
   * @param name username
   */
  public void setWelcomeMessage(String name) {
    welcomeMessage.setText(String.format(welcomeMessage.getText(), name));
  }
}
// Link to location, Landing, & Medical Equipment Page
// Test the exit button
// Fix wrap text method
