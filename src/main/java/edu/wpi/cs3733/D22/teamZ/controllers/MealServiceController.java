package edu.wpi.cs3733.D22.teamZ.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D22.teamZ.*;
import edu.wpi.cs3733.D22.teamZ.App;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

  @FXML private Label header;
  @FXML private Label objectBodyText;
  @FXML private Label roomNumberLabel;
  @FXML private Label equipmentLabel;
  @FXML private Label setSubmitStatus;
  @FXML private TextField enterRoomNumber;
  @FXML private TextField enterPatientName;
  @FXML private TextField enterPatientID;
  @FXML private TextField enterStaffAssigned;
  @FXML private ChoiceBox mealSelectionDropDown;
  @FXML private ChoiceBox mealRequestStatusDropDown;

  //  private ObservableList<Location> locationList;
  //  private List<Location> locationList;

  private String toLandingPageURL = "views/LandingPage.fxml";

  @FXML
  public void initialize() {
    //    //    locationList = locationDAO.getAllLocations(); //locationDAO file not included yet

    //    //    locationList = locationDAO.getAllLocations();

    //    for (Location model : locationList) {
    //      System.out.println(model.getNodeID());
    //    }

    // TODO: Creat a meal database
    mealSelectionDropDown.setItems(
        FXCollections.observableArrayList("Breakfast", "Lunch", "Dinner"));
    mealRequestStatusDropDown.setItems(
        FXCollections.observableArrayList(
            "Creating", "Order Placed", "Preparing", "Ready", "Served"));

    mealSelectionDropDown.setValue("Breakfast");
    mealRequestStatusDropDown.setValue("Creating");
    submitButton.setDisableVisualFocus(true);
    //    submitButton.setDisable(true);
  }

  @FXML
  private void onBackButtonClicked(ActionEvent event) throws IOException {
    Stage primaryStage = (Stage) backButton.getScene().getWindow();
    Parent root = FXMLLoader.load(App.class.getResource(toLandingPageURL));
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
  }

  public void validateButton() {
    if (!enterPatientName.getText().trim().isEmpty()
        && !enterPatientID.getText().trim().isEmpty()
        && !enterRoomNumber.getText().trim().isEmpty()
        && !enterStaffAssigned.getText().trim().isEmpty()
        && !mealSelectionDropDown.getSelectionModel().isEmpty()
        && !mealRequestStatusDropDown.getSelectionModel().isEmpty()) {
      //      submitButton.setDisable(false);
      submitButton.setDisableVisualFocus(false);
    } else {
      //      submitButton.setDisable(true);
      submitButton.setDisableVisualFocus(true);
    }
  }

  public void onSubmitButtonClicked() {
    System.out.println("Patient Name: " + enterPatientName.getText());
    System.out.println("Patient ID: " + enterPatientID.getText());
    System.out.println("Room Number: " + enterRoomNumber.getText());
    //    System.out.println("Floor Number: " + enterFloorNumber.getText());
    //    System.out.println("nodeType: " + enterNodeType.getText());
    System.out.println("Staff Assigned: " + enterStaffAssigned.getText());
    System.out.println("Meal Selected: " + mealSelectionDropDown.getValue());
    System.out.println("Meal Status Selected: " + mealRequestStatusDropDown.getValue());
  }

  public void onResetButtonClicked(ActionEvent event) throws IOException {
    enterPatientName.clear();
    enterPatientID.clear();
    enterRoomNumber.clear();
    enterStaffAssigned.clear();
    mealSelectionDropDown.setValue("Breakfast");
    mealRequestStatusDropDown.setValue("Creating");
  }
}
