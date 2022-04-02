package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MealServiceController {
  @FXML private Button backButton;
  @FXML private Button mealButton;

  @FXML
  void backToDashboard() throws IOException {
    Stage primaryStage = (Stage) backButton.getScene().getWindow();
    Parent root = FXMLLoader.load(App.class.getResource("views/LandingPage.fxml"));
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
  }
}
