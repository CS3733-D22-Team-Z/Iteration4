package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.entity.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;

public class LabRequestController extends ServiceRequestController {

  @FXML private MFXButton navigateToLabRequests;
  @FXML private ChoiceBox<String> labTypeChoiceBox;
  @FXML private TextField patientNameField;
  @FXML private TextField patientIdField;
  @FXML private Label patientNameLabel;
  @FXML private Label patientIdLabel;
  @FXML private Label errorSavingLabel;
  @FXML private Label successfulSubmitLabel;
  @FXML private Rectangle warningBackground;

  private final String toLabServiceRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/LabRequestList.fxml";

  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    menuName = "Lab Request";

    labTypeChoiceBox.setItems(
        FXCollections.observableArrayList(
            "Blood Sample", "Urine Sample", "X-Ray", "CAT Scan", "MRI"));
    FXCollections.observableArrayList("Blood Sample", "Urine Sample", "X-Ray", "CAT Scan", "MRI");
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
  private void toLabServiceRequestList(ActionEvent event) throws IOException {
    menu.load(toLabServiceRequestURL);
  }

  @FXML
  protected void onSubmitButtonClicked(ActionEvent event) throws SQLException {
    // IServiceRequestDAO serviceRequestDAO = new ServiceRequestDAOImpl();
    List<ServiceRequest> serviceRequestList = database.getAllServiceRequests();
    int id;
    // Check for empty db and set first request (will appear as REQ1 in the db)

    if (serviceRequestList.isEmpty()) {
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

    ServiceRequest.RequestStatus status = ServiceRequest.RequestStatus.UNASSIGNED;
    Employee issuer = MenuController.getLoggedInUser();
    Employee handler = null;

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

    database.addLabServiceRequest(temp);
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
  protected void onResetButtonClicked(ActionEvent event) {
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
