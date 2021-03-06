package edu.wpi.cs3733.D22.teamZ.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.util.Locale;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class HomepageController implements IMenuAccess {
  @FXML private Label label;
  @FXML private Button exitButton;
  @FXML private Label welcomeMessage;
  @FXML private MFXButton upperFloorsDashboardButton;

  @FXML private MFXButton mapButton;
  @FXML private MFXButton servicesButton;
  @FXML private MFXButton cctvButton;
  @FXML private MFXButton employeesButton;
  @FXML private MFXButton listButton;

  @FXML private SVGPath dashboardSVG;
  @FXML private SVGPath mapSVG;
  @FXML private SVGPath servicesSVG;
  @FXML private SVGPath cctvSVG;
  @FXML private SVGPath employeesSVG;
  @FXML private SVGPath listSVG;

  private MenuController menu;

  private String toLocationsURL = "edu/wpi/cs3733/D22/teamZ/views/Location.fxml";
  private String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";
  private String toMedicalEquipmentRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentRequestList.fxml";
  private String toUpperFloorsDashboardURL = "edu/wpi/cs3733/D22/teamZ/views/DashboardFinal.fxml";
  private String toEmployeeURL = "edu/wpi/cs3733/D22/teamZ/views/Employee.fxml";
  private String toServerSwitchURL = "edu/wpi/cs3733/D22/teamZ/views/ServerSwitcher.fxml";
  private final String toServiceRequestURL = "edu/wpi/cs3733/D22/teamZ/views/ServiceRequest.fxml";
  private String toGameURL = "edu/wpi/cs3733/D22/teamZ/views/Game.fxml";
  private String toSimulatorURL = "edu/wpi/cs3733/D22/teamZ/views/Simulator.fxml";
  private final String toCCTV = "edu/wpi/cs3733/D22/teamZ/views/CCTVPreview.fxml";
  private final String toCredits = "edu/wpi/cs3733/D22/teamZ/views/CreditsPage.fxml";

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
    name = name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
    setWelcomeMessage(name);

    mapButton.setOnMouseEntered((e) -> changeColor(mapSVG));
    mapButton.setOnMouseExited((e) -> mapSVG.setFill(Color.WHITE));

    servicesButton.setOnMouseEntered((e) -> changeColor(servicesSVG));
    servicesButton.setOnMouseExited((e) -> servicesSVG.setFill(Color.WHITE));

    cctvButton.setOnMouseEntered((e) -> changeColor(cctvSVG));
    cctvButton.setOnMouseExited((e) -> cctvSVG.setFill(Color.WHITE));

    employeesButton.setOnMouseEntered((e) -> changeColor(employeesSVG));
    employeesButton.setOnMouseExited((e) -> employeesSVG.setFill(Color.WHITE));

    listButton.setOnMouseEntered((e) -> changeColor(listSVG));
    listButton.setOnMouseExited((e) -> listSVG.setFill(Color.WHITE));

    upperFloorsDashboardButton.setOnMouseEntered((e) -> changeColor(dashboardSVG));
    upperFloorsDashboardButton.setOnMouseExited((e) -> dashboardSVG.setFill(Color.WHITE));
  }

  public void changeColor(SVGPath svg) {
    Color color = Color.web("0067B1");
    svg.setFill(color);
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
    menu.selectMenu(3);
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
  private void toSimulation() throws IOException {
    menu.load(toSimulatorURL);
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

  /**
   * Opens panel for CCTV viewer
   *
   * @param event
   * @throws IOException
   */
  public void toCCTV(ActionEvent event) throws IOException {
    menu.load(toCCTV);
  }

  public void toCredits(ActionEvent event) throws IOException {
    menu.load(toCredits);
  }
}
// Link to location, Landing, & Medical Equipment Page
// Test the exit button
// Fix wrap text method
