package edu.wpi.cs3733.D22.teamZ.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D22.teamZ.App;
import edu.wpi.cs3733.D22.teamZ.database.ILabRequestServiceDAO;
import edu.wpi.cs3733.D22.teamZ.database.LabRequestServiceDAOImpl;
import edu.wpi.cs3733.D22.teamZ.entity.*;
import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LabRequestController {

  @FXML private Button backButton;
  @FXML private JFXButton navigateToLabRequests;
  @FXML private JFXButton submitButton;
  @FXML private JFXButton resetButton;
  @FXML private ChoiceBox<String> labTypeChoiceBox;
  @FXML private TextField patientNameField;
  @FXML private TextField patientIdField;
  @FXML private Label patientNameLabel;
  @FXML private Label patientIdLabel;
  @FXML private Label errorSavingLabel;

  private final String toDashboardURL = "views/LandingPage.fxml";
  ILabRequestServiceDAO labRequestServiceDAO;

  @FXML
  public void initialize() {

    labRequestServiceDAO = new LabRequestServiceDAOImpl();

    labTypeChoiceBox.setItems(
        FXCollections.observableArrayList(
            "Blood Sample", "Urine Sample", "X-Ray", "CAT Scan", "MRI"));
    errorSavingLabel.setVisible(false);
    submitButton.setDisable(true);
  }

  @FXML
  private void validateButton() {
    if (!patientNameField.getText().trim().isEmpty()
        && !patientIdField.getText().trim().isEmpty()
        && !labTypeChoiceBox.getSelectionModel().isEmpty()) {
      submitButton.setDisable(false);
    }
  }

  @FXML
  public void onBackButtonClicked(ActionEvent event) throws IOException {
    // Load the default FXML file and set that scene to the main stage.
    Stage mainStage = (Stage) backButton.getScene().getWindow();
    Parent root = FXMLLoader.load(App.class.getResource(toDashboardURL));
    Scene scene = new Scene(root);
    mainStage.setScene(scene);
  }

  @FXML
  public void onSubmitButtonClicked(ActionEvent event) throws SQLException {

    int id;
    // Check for empty db and set first request (will appear as REQ1 in the db)

    if (labRequestServiceDAO.getAllLabServiceRequests().isEmpty()) {
      System.out.println("Equipment is empty");
      id = 0;
    } else {
      id = labRequestServiceDAO.getAllLabServiceRequests().size() - 1;
    }
    // Create new REQID
    String requestID = "REQ" + ++id;

    // Create entities for submission

    ServiceRequest.RequestStatus status = ServiceRequest.RequestStatus.PROCESSING;
    Employee issuer = new Employee("Pat" + id, "Pat", Employee.AccessType.ADMIN, "", "");
    Employee handler = new Employee("Jake" + id, "Jake", Employee.AccessType.ADMIN, "", "");

    LabServiceRequest temp =
        new LabServiceRequest(
            requestID,
            status,
            issuer,
            handler,
            new Location(),
            labTypeChoiceBox.getSelectionModel().getSelectedItem());

    labRequestServiceDAO.addLabRequest(temp);
  }

  @FXML
  public void onNavigateToLabRequests(ActionEvent event) {
    // Todo implement new page
    /*
    Stage mainStage = (Stage) backButton.getScene().getWindow();
    Parent root = FXMLLoader.load(App.class.getResource("views/MedicalEquipmentRequestList.fxml"));
    Scene scene = new Scene(root);
    mainStage.setScene(scene);
     */
  }

  @FXML
  private void onResetButtonClicked(ActionEvent event) throws IOException {
    patientIdField.clear();
    patientNameField.clear();
    labTypeChoiceBox.setValue(null);
  }
}
