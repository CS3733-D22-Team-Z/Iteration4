package edu.wpi.cs3733.D22.teamZ.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D22.teamZ.App;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MealServiceExitConfirmationController {

  public int exitState = 0; // BAD

  @FXML private JFXButton backButton;
  @FXML private JFXButton leaveButton;

  private String toMealServiceURL = "views/MealService.fxml";
  //    private String toMealServiceURL = "views/LandingPage.fxml";
  //  private String toMealServiceRequestListURL = "views/MealServiceRequestList.fxml";
  //  private String toMealServiceRequestListURL = "views/MealServiceRequestList.fxml";

  // private String toLocationsURL = "edu/wpi/cs3733/D22/teamZ/views/Location.fxml";
  //    private String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";
  //    private String toMedicalEquipmentRequestURL =
  //            "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentRequestList.fxml";
  //    private String toHomeURL = "edu/wpi/cs3733/D22/teamZ/views/Homepage.fxml";
  //    private String toMealServiceRequestListURL =
  //            "edu/wpi/cs3733/D22/teamZ/views/MealServiceRequestList.fxml";

  @FXML
  public void initialize() {
    // submit status indicator
    //        submitStatusIndicator.setStyle(
    //                "-fx-text-fill: #7b7b7b; -fx-translate-x: 126; -fx-translate-y: 10");
    //        submitStatusIndicator.setText("Awaiting submission...");
  }

  @FXML
  private void onBackButtonClicked(ActionEvent event) throws IOException {
    exitState = -1; // bad
    System.out.println("navigating to Meal Service Request List page from landing page");
    Stage primaryStage = (Stage) backButton.getScene().getWindow();
    Parent root = FXMLLoader.load(App.class.getResource(toMealServiceURL));
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
  }

  @FXML
  public void onSubmitButtonClicked() {
    exitState = 1; // bad
    //        System.out.println("navigating to " +  +  " from landing page");
  }

  //    @FXML
  //    private void toLocations(ActionEvent event) throws IOException {
  //        System.out.println("navigating to locations from home");
  //        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(toLocationsURL));
  //        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
  //        Scene scene = new Scene(root);
  //        stage.setScene(scene);
  //        stage.show();
  //    }
  //
  //    @FXML
  //    private void toLandingPage(ActionEvent event) throws IOException {
  //        System.out.println("navigating to landing page from home");
  //        Parent root =
  // FXMLLoader.load(getClass().getClassLoader().getResource(toLandingPageURL));
  //        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
  //        Scene scene = new Scene(root);
  //        primaryStage.setScene(scene);
  //        primaryStage.show();
  //    }
  //
  //    @FXML
  //    private void toMedicalEquipmentRequest(ActionEvent event) throws IOException {
  //        System.out.println("navigating to Medical Equipment Request page from home");
  //        Parent root =
  //
  // FXMLLoader.load(getClass().getClassLoader().getResource(toMedicalEquipmentRequestURL));
  //        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
  //        Scene scene = new Scene(root);
  //        stage.setScene(scene);
  //        stage.show();
  //    }
  //
  //    @FXML
  //    private void toHome(ActionEvent event) throws IOException {
  //        System.out.println("navigating to home using home button on sidebar");
  //        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(toHomeURL));
  //        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
  //        Scene scene = new Scene(root);
  //        stage.setScene(scene);
  //        stage.show();
  //    }
  //
  //    @FXML
  //    private void toExit(ActionEvent event) {
  //        System.out.println("exit the app using exit button bottom left");
  //        Stage stage = (Stage) exitButton.getScene().getWindow();
  //        stage.close();
  //    }
}

//    //  @FXML
//    //  public void onListButtonClicked(ActionEvent event) throws IOException {
//    //    System.out.println("navigating to all meal service request list, from meal service");
//    //    Stage primaryStage = (Stage) listButton.getScene().getWindow();
//    //    Parent root =
//    //
// FXMLLoader.load(getClass().getClassLoader().getResource(toMealServiceRequestListURL));
//    //    Scene scene = new Scene(root);
//    //    primaryStage.setScene(scene);
//    //  }
//
//    @FXML
//    private void onListButtonClicked(ActionEvent event) throws IOException {
//        System.out.println("navigating to all meal service request list, from meal service");
//        Parent root =
//
// FXMLLoader.load(getClass().getClassLoader().getResource(toMealServiceRequestListURL));
//        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        Scene scene = new Scene(root);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    /** For confirmation exit page */
//    @FXML
//    public void toHome(ActionEvent event) {
//        System.out.println("navigating to meal service exit confirmation popup, from meal
// service");
//    }
//
//    @FXML
//    public void toLocations(ActionEvent event) {
//        System.out.println("navigating to all meal service request list, from meal service");
//    }
//
//    @FXML
//    public void toLandingPage(ActionEvent event) {
//        System.out.println("navigating to all meal service request list, from meal service");
//    }
//
//    @FXML
//    public void toMedicalEquipmentRequest(ActionEvent event) {
//        System.out.println("navigating to all meal service request list, from meal service");
//    }
//
//    @FXML
//    public void toExit(ActionEvent event) {
//        System.out.println("navigating to all meal service request list, from meal service");
//    }
// }
