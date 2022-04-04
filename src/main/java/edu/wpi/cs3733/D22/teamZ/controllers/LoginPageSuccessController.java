package edu.wpi.cs3733.D22.teamZ.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D22.teamZ.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LoginPageSuccessController {
  @FXML private Label welcomeMessage;
  @FXML private JFXButton returnButton;

  private String toLandingPageURL = "views/LandingPage.fxml";

  public void setWelcomeMessage(String name) {
    welcomeMessage.setText(String.format(welcomeMessage.getText(), name));
  }

  @FXML
  private void returnButtonClicked() throws IOException {
    // Load the default FXML file and set that scene to the main stage.
    Stage mainStage = (Stage) returnButton.getScene().getWindow();
    Parent root = FXMLLoader.load(App.class.getResource(toLandingPageURL));
    Scene scene = new Scene(root);
    mainStage.setScene(scene);
  }
}
