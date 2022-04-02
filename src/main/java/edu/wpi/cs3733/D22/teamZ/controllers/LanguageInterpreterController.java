package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.App;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LanguageInterpreterController {

  // navDefault: navigates back to default page when back button is pressed
  @FXML
  public void navDefault(ActionEvent event) throws IOException {
    System.out.println("navigating to default from language interpreter");
    Parent root = FXMLLoader.load(App.class.getResource("views/LandingPage.fxml"));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
}
