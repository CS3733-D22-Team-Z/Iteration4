package edu.wpi.cs3733.D22.teamZ.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MenuController {
  @FXML Pane contentPane;
  @FXML Button exitButton;

  ClassLoader rscLoader;

  private String toLocationsURL = "edu/wpi/cs3733/D22/teamZ/views/Location.fxml";
  private String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";
  private String toMedicalEquipmentRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentRequestList.fxml";
  private String toHomeURL = "edu/wpi/cs3733/D22/teamZ/views/Homepage.fxml";

  public MenuController() {
    rscLoader = getClass().getClassLoader();
  }

  /**
   * Loads the given FXML file into the content pane.
   *
   * @param path the path to the FXML file
   * @return the controller associated with the FXML
   */
  public IMenuAccess load(String path) throws IOException {
    contentPane.getChildren().clear();
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(rscLoader.getResource(path));
    Node root = loader.load();
    contentPane.getChildren().add(root);
    IMenuAccess cont = loader.getController();
    cont.setMenuController(this);
    return cont;
  }

  @FXML
  public void toHome() throws IOException {
    load(toHomeURL);
  }

  @FXML
  public void toLocations() throws IOException {
    load(toLocationsURL);
  }

  @FXML
  public void toLandingPage() throws IOException {
    load(toLandingPageURL);
  }

  @FXML
  public void toMedicalEquipmentRequest() throws IOException {
    load(toMedicalEquipmentRequestURL);
  }

  @FXML
  public void toExit() {
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
  }
}
