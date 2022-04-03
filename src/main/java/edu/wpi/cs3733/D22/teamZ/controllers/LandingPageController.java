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

  private final String toMedicalEquipmentDeliveryURL =
      "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentDelivery.fxml";
  private final String toLabRequestURL = "edu/wpi/cs3733/D22/teamZ/views/LabServiceRequest.fxml";
  private final String toMedicineRequestURL = "edu/wpi/cs3733/D22/teamZ/views/MedicineRequest.fxml";
  private final String toMealRequestsURL = "edu/wpi/cs3733/D22/teamZ/views/MealService.fxml";
  private final String toLanguageInterpreterURL =
      "edu/wpi/cs3733/D22/teamZ/views/LanguageInterpreter.fxml";
  private final String toLaundryServiceURL = "edu/wpi/cs3733/D22/teamZ/views/LaundryService.fxml";
  private final String toComputerServiceRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/ComputerServiceRequest.fxml";
  private final String toLocationsURL = "edu/wpi/cs3733/D22/teamZ/views/Location.fxml";
  private final String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";
  private final String toMedicalEquipmentRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentRequestList.fxml";
  private final String toMealServiceRequestListURL =
      "edu/wpi/cs3733/D22/teamZ/views/MealServiceRequestList.fxml";
  private final String toHomeURL = "edu/wpi/cs3733/D22/teamZ/views/Homepage.fxml";

  @FXML
  private void navMedicalEquipment(ActionEvent event) throws IOException {
    System.out.println("navigating to medical equipment delivery from landing page");
    Parent root =
        FXMLLoader.load(getClass().getClassLoader().getResource(toMedicalEquipmentDeliveryURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  private void navLab(ActionEvent event) throws IOException {
    System.out.println("navigating to lab requests from landing page");
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(toLabRequestURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  private void navMedicine(ActionEvent event) throws IOException {
    System.out.println("navigating to medicine from landing page");
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(toMedicineRequestURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  private void navMeal(ActionEvent event) throws IOException {
    System.out.println("navigating to meal from landing page");
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(toMealRequestsURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  private void navLanguage(ActionEvent event) throws IOException {
    System.out.println("navigating to language from landing page");
    Parent root =
        FXMLLoader.load(getClass().getClassLoader().getResource(toLanguageInterpreterURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  private void navLaundry(ActionEvent event) throws IOException {
    System.out.println("navigating to laundry from landing page");
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(toLaundryServiceURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  private void navComputer(ActionEvent event) throws IOException {
    System.out.println("navigating to computer from landing page");
    Parent root =
        FXMLLoader.load(getClass().getClassLoader().getResource(toComputerServiceRequestURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  private void toLocations(ActionEvent event) throws IOException {
    System.out.println("navigating to location from landing page");
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(toLocationsURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  private void toLandingPage(ActionEvent event) throws IOException {
    System.out.println("navigating to landing page from landing page");
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(toLandingPageURL));
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  @FXML
  private void toMedicalEquipmentRequest(ActionEvent event) throws IOException {
    System.out.println("navigating to Medical Equipment Request page from landing page");
    Parent root =
        FXMLLoader.load(getClass().getClassLoader().getResource(toMedicalEquipmentRequestURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  public void toMealServiceRequest(ActionEvent event) throws IOException {
    System.out.println("navigating to Meal Service Request List page from landing page");
    Parent root =
        FXMLLoader.load(getClass().getClassLoader().getResource(toMealServiceRequestListURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  private void toHome(ActionEvent event) throws IOException {
    System.out.println("navigating to home using home button on sidebar");
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(toHomeURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  private void toExit(ActionEvent event) {
    System.out.println("exit the app using exit button bottom left");
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
  }
}
