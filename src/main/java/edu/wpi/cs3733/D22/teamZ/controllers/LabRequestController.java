package edu.wpi.cs3733.D22.teamZ.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D22.teamZ.App;
import edu.wpi.cs3733.D22.teamZ.database.ILabRequestServiceDAO;
import edu.wpi.cs3733.D22.teamZ.database.IServiceRequestDAO;
import edu.wpi.cs3733.D22.teamZ.database.LabRequestServiceDAOImpl;
import edu.wpi.cs3733.D22.teamZ.database.ServiceRequestDAOImpl;
import edu.wpi.cs3733.D22.teamZ.entity.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
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
  @FXML private Label successfulSubmitLabel;
  @FXML private Rectangle warningBackground;

  private final String toDashboardURL = "views/LandingPage.fxml";
  private final String toLabServiceRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/LabRequestList.fxml";

  ILabRequestServiceDAO labRequestServiceDAO;

  @FXML
  public void initialize() {

    labRequestServiceDAO = new LabRequestServiceDAOImpl();

    labTypeChoiceBox.setItems(
        FXCollections.observableArrayList(
            "Blood Sample", "Urine Sample", "X-Ray", "CAT Scan", "MRI"));
        FXCollections.observableArrayList(
            "Blood Sample", "Urine Sample", "X-Ray", "CAT Scan", "MRI");
    errorSavingLabel.setVisible(false);
    submitButton.setDisable(true);
    successfulSubmitLabel.setVisible(false);
    warningBackground.setVisible(false);
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
  private void toLabServiceRequestList(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(toLabServiceRequestURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @FXML
  public void onSubmitButtonClicked(ActionEvent event) throws SQLException {
    IServiceRequestDAO serviceRequestDAO = new ServiceRequestDAOImpl();
    List<ServiceRequest> serviceRequestList = serviceRequestDAO.getAllServiceRequests();
    int id;
    // Check for empty db and set first request (will appear as REQ1 in the db)

    if (serviceRequestDAO.getAllServiceRequests().isEmpty()) {
      System.out.println("There are no service requests");
      id = 0;
    } else {
      ServiceRequest tempService = serviceRequestList.get(serviceRequestList.size() - 1);
      id =
          Integer.parseInt(
              tempService
                  .getRequestID()
                  .substring(tempService.getRequestID().lastIndexOf("Q") + 1));
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
            new Location(
                "zLABS00101",
                717,
                609,
                "1",
                "Tower",
                "LABS",
                "Obstetrics Admitting",
                "Obs Admitting"),
            labTypeChoiceBox.getSelectionModel().getSelectedItem());

    labRequestServiceDAO.addLabRequest(temp);
    this.clearFields();
    successfulSubmitLabel.setVisible(true);
    warningBackground.setVisible(true);
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
    successfulSubmitLabel.setVisible(false);
    warningBackground.setVisible(false);
  }

  @FXML
  public void clearFields() {
    patientIdField.clear();
    patientNameField.clear();
    labTypeChoiceBox.setValue(null);
  }
}
