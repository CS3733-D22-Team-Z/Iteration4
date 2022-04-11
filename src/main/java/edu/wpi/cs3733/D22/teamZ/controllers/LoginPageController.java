package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginPageController implements Initializable {
  @FXML private TextField usernameField;
  @FXML private TextField passwordField;
  @FXML private Label errorLabel;

  private FacadeDAO facadeDAO;
  private String toHomepageURL = "edu/wpi/cs3733/D22/teamZ/views/Menu.fxml";

  /**
   * Initalizes the employee database for the controller
   *
   * @param location
   * @param resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    facadeDAO = new FacadeDAO();
  }

  /**
   * Reads information from the username and password fields, and determines whether it is a valid
   * login or not. If so, it loads the success screen. If not, it switches to the error state.
   */
  @FXML
  private void loginButtonPressed(ActionEvent event) {

    if (usernameField.getText().equals("admin") && passwordField.getText().equals("admin")) {
      try {
        loadSuccessScreen(usernameField.getText(), event);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      enterErrorState();
    }
  }

  public void enterErrorState() {
    usernameField.setStyle("-fx-border-color: #FF4343");
    passwordField.setStyle("-fx-border-color: #FF4343");
    errorLabel.setVisible(true);
  }

  public void enterNormalState() {
    usernameField.setStyle("-fx-border-color: #0075ff");
    passwordField.setStyle("-fx-border-color: #0075ff");
    errorLabel.setVisible(false);
  }

  public void loadSuccessScreen(String username, ActionEvent event) throws IOException {
    // Load the default FXML file and set that scene to the main stage.
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getClassLoader().getResource(toHomepageURL));
    Parent root = loader.load();
    Scene scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    HomepageController thisController = loader.getController();
    // thisController.setWelcomeMessage(username);
  }

  @FXML
  public void skipButtonPressed(ActionEvent event) throws IOException {
    // Load the default FXML file and set that scene to the main stage.
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getClassLoader().getResource(toHomepageURL));
    Parent root = loader.load();
    Scene scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    HomepageController thisController = loader.getController();
    // thisController.setWelcomeMessage("Skipped");
  }
}
