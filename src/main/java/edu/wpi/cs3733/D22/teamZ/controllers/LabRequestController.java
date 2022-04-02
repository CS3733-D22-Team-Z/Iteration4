package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.App;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LabRequestController {
  @FXML private Button backButton;

  private final String toDashboardURL = "views/LandingPage.fxml";

  @FXML
  public void backToDashboard(ActionEvent event) throws IOException {
    // Load the default FXML file and set that scene to the main stage.
    Stage mainStage = (Stage) backButton.getScene().getWindow();
    Parent root = FXMLLoader.load(App.class.getResource(toDashboardURL));
    Scene scene = new Scene(root);
    mainStage.setScene(scene);
  }
}
