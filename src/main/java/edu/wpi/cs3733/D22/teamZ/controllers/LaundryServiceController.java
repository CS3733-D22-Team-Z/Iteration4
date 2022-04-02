package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.App;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LaundryServiceController {

  private final String toLandingPage = "views/LandingPage.fxml";

  public void navDefault(javafx.event.ActionEvent actionEvent) throws IOException {
    Parent root = FXMLLoader.load(App.class.getResource(toLandingPage));
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
}
