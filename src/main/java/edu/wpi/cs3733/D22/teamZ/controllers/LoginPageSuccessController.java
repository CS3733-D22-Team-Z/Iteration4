package edu.wpi.cs3733.D22.teamZ.controllers;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LoginPageSuccessController {
  @FXML private Label welcomeMessage;
  @FXML private JFXButton continueButton;

  private final String toHomePageURL = "edu/wpi/cs3733/D22/teamZ/views/Homepage.fxml";

  public void setWelcomeMessage(String name) {
    welcomeMessage.setText(String.format(welcomeMessage.getText(), name));
  }

  @FXML
  private void continueButtonClicked() throws IOException {
    // Load the default FXML file and set that scene to the main stage.
    Stage mainStage = (Stage) continueButton.getScene().getWindow();
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(toHomePageURL));
    Scene scene = new Scene(root);
    mainStage.setScene(scene);
  }
}
