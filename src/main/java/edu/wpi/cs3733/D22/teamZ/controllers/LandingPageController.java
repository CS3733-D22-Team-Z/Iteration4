package edu.wpi.cs3733.D22.teamZ.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LandingPageController {

  @FXML private Button exitButton; // ??????

  @FXML
  public void navMedicalEquipment(ActionEvent event) throws IOException {
    System.out.println("navigating to medical from default");
    Parent root =
        FXMLLoader.load(
            getClass().getClassLoader().getResource("views/MedicalEquipmentDelivery.fxml"));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  public void navLab(ActionEvent event) throws IOException {
    System.out.println("navigating to lab from default");
    Parent root =
        FXMLLoader.load(getClass().getClassLoader().getResource("views/LabServiceRequest.fxml"));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  public void navMedicine(ActionEvent event) throws IOException {
    System.out.println("navigating to medicine from default");
    Parent root =
        FXMLLoader.load(getClass().getClassLoader().getResource("views/MedicineRequest.fxml"));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  public void navMeal(ActionEvent event) throws IOException {
    System.out.println("navigating to meal from default");
    Parent root =
        FXMLLoader.load(getClass().getClassLoader().getResource("views/Meal Service.fxml"));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  public void navLanguage(ActionEvent event) throws IOException {
    System.out.println("navigating to language from default");
    Parent root =
        FXMLLoader.load(getClass().getClassLoader().getResource("views/LanguageInterpreter.fxml"));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  public void navLaundry(ActionEvent event) throws IOException {
    System.out.println("navigating to laundry from default");
    Parent root =
        FXMLLoader.load(getClass().getClassLoader().getResource("views/LaundryService.fxml"));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  public void navComputer(ActionEvent event) throws IOException {
    System.out.println("navigating to computer from default");
    Parent root =
        FXMLLoader.load(
            getClass().getClassLoader().getResource("views/ComputerServiceRequest.fxml"));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  public void toLocations(ActionEvent event) throws IOException {
    System.out.println("navigating to location from home");
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/Location.fxml"));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  public void toLandingPage(ActionEvent event) throws IOException {

    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/LandingPage.fxml"));
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public void toMedicalEquipmentRequest(ActionEvent event) throws IOException {
    System.out.println("navigating to location from home");
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/MERL.fxml"));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  public void toHome(ActionEvent event) throws IOException {
    System.out.println("navigating to location from home");
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/Homepage.fxml"));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  public void toExit(ActionEvent event) {
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
  }
}
