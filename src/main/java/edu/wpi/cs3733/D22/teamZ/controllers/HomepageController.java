package edu.wpi.cs3733.D22.teamZ.controllers;

import java.io.IOException;
import java.util.Locale;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HomepageController implements IMenuAccess {
  @FXML private Label label;
  @FXML private Button exitButton;
  @FXML private Label welcomeMessage;
  @FXML private Button upperFloorsDashboardButton;

  private MenuController menu;

  private String toLocationsURL = "edu/wpi/cs3733/D22/teamZ/views/Location.fxml";
  private String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";
  private String toMedicalEquipmentRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentRequestList.fxml";
  private String toUpperFloorsDashboardURL =
      "edu/wpi/cs3733/D22/teamZ/views/UpperFloorsDashboard.fxml";
  private String toEmployeeURL = "edu/wpi/cs3733/D22/teamZ/views/Employee.fxml";
  private String toServerSwitchURL = "edu/wpi/cs3733/D22/teamZ/views/ServerSwitcher.fxml";
  private final String toServiceRequestURL = "edu/wpi/cs3733/D22/teamZ/views/ServiceRequest.fxml";
  private String toGameURL = "edu/wpi/cs3733/D22/teamZ/views/Game.fxml";

  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Home";
  }

  @FXML
  public void initialize() {
    String name = MenuController.getLoggedInUser().getName();
    name =
        name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
    setWelcomeMessage(name);
  }

  @FXML
  private void toLocations() throws IOException {
    System.out.println("navigating to locations from home");
    menu.selectMenu(1);
    menu.load(toLocationsURL);
  }

  @FXML
  private void toGame() throws IOException {
    System.out.println("navigating to game from home");
    menu.selectMenu(1);
    menu.load(toGameURL);
  }

  @FXML
  private void toServiceRequests() throws IOException {
    System.out.println("navigating to servReq from landing page");
    menu.load(toServiceRequestURL);
  }

  @FXML
  private void toLandingPage() throws IOException {
    System.out.println("navigating to landing page from home");
    menu.selectMenu(2);
    menu.load(toLandingPageURL);
  }

  @FXML
  private void toMedicalEquipmentRequest() throws IOException {
    System.out.println("navigating to Medical Equipment Request page from home");
    menu.selectMenu(3);
    menu.load(toMedicalEquipmentRequestURL);
  }

  @FXML
  private void toUpperFloorsDashboard() throws IOException {
    menu.load(toUpperFloorsDashboardURL);
  }

  @FXML
  private void toEmployeePage() throws IOException {
    System.out.println("navigating to employee page from home");
    menu.load(toEmployeeURL);
  }

  /**
   * Updates the welcome message with the username of who logged in
   *
   * @param name username
   */
  public void setWelcomeMessage(String name) {
    welcomeMessage.setText(
        String.format("Hello %s, Welcome to the Brigham and Women's Hospital App", name));
  }

  /**
   * Opens panel for switching database servers
   *
   * @param event
   */
  @FXML
  public void SwitchServerClicked(ActionEvent event) throws IOException {
    menu.load(toServerSwitchURL);
  }
}
// Link to location, Landing, & Medical Equipment Page
// Test the exit button
// Fix wrap text method
