package edu.wpi.cs3733.D22.teamZ.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D22.teamZ.*;
import edu.wpi.cs3733.D22.teamZ.App;
import java.io.IOException;
import java.util.Calendar;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MealServiceController {
  //  private ILocationDAO locationDAO = new LocationDAOImpl();

  @FXML private JFXButton backButton;
  @FXML private JFXButton submitButton;
  @FXML private JFXButton resetButton;
  @FXML private JFXButton allMealRequestsButton;
  @FXML private JFXButton listButton; // temp

  @FXML private Label header;
  @FXML private Label objectBodyText;
  @FXML private Label roomNumberLabel;
  @FXML private Label equipmentLabel;
  @FXML private Label setSubmitStatus;
  @FXML private Label submitStatusIndicator;
  @FXML private TextField enterPatientName;
  @FXML private TextField enterPatientID;
  @FXML private TextField enterStaffAssigned;
  @FXML private ChoiceBox patientRoomDropDown;
  @FXML private ChoiceBox mealSelectionDropDown;
  @FXML private ChoiceBox mealRequestStatusDropDown;

  //  private ObservableList<Location> locationList;
  //  private List<Location> locationList;

  private String toLandingPageURL = "views/LandingPage.fxml";
  //  private String toMealServiceRequestListURL = "views/MealServiceRequestList.fxml";
  private String toMealServiceRequestListURL =
      "edu/wpi/cs3733/D22/teamZ/views/MealServiceRequestList.fxml";

  @FXML
  public void initialize() {
    //    //    locationList = locationDAO.getAllLocations(); //locationDAO file not included yet

    //    //    locationList = locationDAO.getAllLocations();

    //    for (Location model : locationList) {
    //      System.out.println(model.getNodeID());
    //    }

    // Patient Room (short name)
    patientRoomDropDown.setItems(
        FXCollections.observableArrayList(
            "PR3B31", "PR3B32", "PR3B33", "PR3B34", "PR3B35", "PR3B36", "PR3B37", "PR3B38",
            "PR3B39", "PR3B40", "PR3C51", "PR3C52", "PR3C53", "PR3C54", "PR3C55", "PR3C56",
            "PR3C57", "PR3C58", "PR3C59", "PR3C60"));

    // TODO: Creat a meal database
    mealSelectionDropDown.setItems(
        FXCollections.observableArrayList("Combo 1", "Combo 2", "Combo 3"));
    mealRequestStatusDropDown.setItems(
        FXCollections.observableArrayList(
            "Creating", "Order Placed", "Preparing", "Ready", "Served"));

    mealSelectionDropDown.setValue("Combo 1");
    mealRequestStatusDropDown.setValue("Creating");
    mealRequestStatusDropDown.setDisable(true);
    enterStaffAssigned.setText("Kitchen Staff");
    enterStaffAssigned.setDisable(true);

    //    submitButton.setDisableVisualFocus(true);
    submitButton.setDisable(true);
  }

  @FXML
  private void onBackButtonClicked(ActionEvent event) throws IOException {
    Stage primaryStage = (Stage) backButton.getScene().getWindow();
    Parent root = FXMLLoader.load(App.class.getResource(toLandingPageURL));
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
  }

  //  @FXML
  //  public void toAllMealRequests(ActionEvent event) throws IOException {
  //    Stage primaryStage = (Stage) allMealRequestsButton.getScene().getWindow();
  //    Parent root = FXMLLoader.load(App.class.getResource(toMealServiceRequestListURL));
  //    Scene scene = new Scene(root);
  //    primaryStage.setScene(scene);
  //  }

  @FXML
  public void toAllMealRequestList(ActionEvent event) throws IOException {
    System.out.println("navigating to Meal Service Request List page from landing page");
    Parent root =
        FXMLLoader.load(getClass().getClassLoader().getResource(toMealServiceRequestListURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  public void validateButton() {
    if (!enterPatientName.getText().trim().isEmpty()
        && !enterPatientID.getText().trim().isEmpty()
        && !enterStaffAssigned.getText().trim().isEmpty()
        && !patientRoomDropDown.getSelectionModel().isEmpty()
        && !mealSelectionDropDown.getSelectionModel().isEmpty()
        && !mealRequestStatusDropDown.getSelectionModel().isEmpty()) {
      submitButton.setDisable(false);
      //      submitButton.setDisableVisualFocus(false);
    } else {
      submitButton.setDisable(true);
      //      submitButton.setDisableVisualFocus(true);
    }
  }

  public void onSubmitButtonClicked() {
    Calendar rightNow = Calendar.getInstance();
    int hour = rightNow.get(Calendar.HOUR_OF_DAY);
    int minute = rightNow.get(Calendar.MINUTE);

    System.out.println("Time of Request: " + hour + ":" + minute);
    System.out.println("Patient Name: " + enterPatientName.getText());
    System.out.println("Patient ID: " + enterPatientID.getText());
    System.out.println("Room Number: " + patientRoomDropDown.getValue());
    //    System.out.println("Floor Number: " + enterFloorNumber.getText());
    //    System.out.println("nodeType: " + enterNodeType.getText());
    System.out.println("Staff Assigned: " + enterStaffAssigned.getText());
    System.out.println("Meal Selected: " + mealSelectionDropDown.getValue());
    System.out.println("Meal Status Selected: " + mealRequestStatusDropDown.getValue());

    // simple test
    if (minute % 2 == 0) {
      // if submit is successful
      submitStatusIndicator.setStyle("-fx-text-fill: green");
      submitStatusIndicator.setText(
          "Success at " + String.format("%02d", hour) + ":" + String.format("%02d", minute));
    } else {
      // if submit fails
      submitStatusIndicator.setStyle("-fx-text-fill: red");
      submitStatusIndicator.setText(
          "Failed at " + String.format("%02d", hour) + ":" + String.format("%02d", minute));
    }

    //    try {
    //
    //    } catch () {
    //
    //    }
  }

  public void onResetButtonClicked(ActionEvent event) throws IOException {
    enterPatientName.clear();
    enterPatientID.clear();
    enterStaffAssigned.clear();
    patientRoomDropDown.setValue(null);
    mealSelectionDropDown.setValue("Combo 1");
    mealRequestStatusDropDown.setValue("Creating");
    submitStatusIndicator.setStyle("-fx-text-fill: #7b7b7b");
    submitStatusIndicator.setText("Form Cleared");
  }

  @FXML
  public void onListButtonClicked(ActionEvent actionEvent) throws IOException {
    Stage primaryStage = (Stage) listButton.getScene().getWindow();
    Parent root = FXMLLoader.load(App.class.getResource(toMealServiceRequestListURL));
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
  }
}
