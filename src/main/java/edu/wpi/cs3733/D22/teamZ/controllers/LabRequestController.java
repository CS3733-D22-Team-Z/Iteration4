package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

public class LabRequestController extends ServiceRequestController {

  @FXML private MFXButton navigateToLabRequests;
  @FXML private ChoiceBox<String> labTypeChoiceBox;
  @FXML private TextField patientNameField;
  @FXML private TextField patientIdField;
  @FXML private Label patientNameLabel;
  @FXML private Label patientIdLabel;
  @FXML private Label labTypeLabel;
  @FXML private Label errorSavingLabel;
  @FXML private Label successfulSubmitLabel;
  @FXML private Region backRegion;

  private final String toLabServiceRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/LabRequestList.fxml";

  private String backSVG =
      "M 13.83 19 C 13.6806 19.0005 13.533 18.9675 13.398 18.9035 C 13.263 18.8395 13.1441 18.746 13.05 18.63 L 8.22 12.63 C 8.07291 12.4511 7.99251 12.2266 7.99251 11.995 C 7.99251 11.7634 8.07291 11.5389 8.22 11.36 L 13.22 5.36 C 13.3897 5.15578 13.6336 5.02736 13.8981 5.00298 C 14.1625 4.9786 14.4258 5.06026 14.63 5.23 C 14.8342 5.39974 14.9626 5.64365 14.987 5.90808 C 15.0114 6.1725 14.9297 6.43578 14.76 6.64 L 10.29 12 L 14.61 17.36 C 14.7323 17.5068 14.81 17.6855 14.8338 17.8751 C 14.8577 18.0646 14.8268 18.257 14.7447 18.4296 C 14.6627 18.6021 14.5329 18.7475 14.3708 18.8486 C 14.2087 18.9497 14.021 19.0022 13.83 19 Z";
  private String white = "FFFFFF";
  private String svgCSSLine = "-fx-background-color: %s";

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
    initializeHelpGraphic();
  }

  @FXML
  private void validateButton() {
    if (!patientNameField.getText().trim().isEmpty()
        && !patientIdField.getText().trim().isEmpty()
        && !labTypeChoiceBox.getSelectionModel().isEmpty()) {
      submitButton.setDisable(false);
      System.out.println("Lab Request Submit Button enabled");
    } else {
      submitButton.setDisable(true);
      System.out.println("Lab Request Submit Button disabled");
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

    UniqueID id = new UniqueID();
    String requestID = id.generateID("LAB");

    // Create entities for submission

    ServiceRequest.RequestStatus status = ServiceRequest.RequestStatus.UNASSIGNED;
    Employee issuer = MenuController.getLoggedInUser();
    Employee handler = null;
    LocalDateTime opened = LocalDateTime.now();
    LocalDateTime closed = null;

    LabServiceRequest temp =
        new LabServiceRequest(
            requestID,
            status,
            issuer,
            handler,
            FacadeDAO.getInstance().getLocationByID("zLABS00101"),
            labTypeChoiceBox.getSelectionModel().getSelectedItem(),
            opened,
            closed);

    database.addLabServiceRequest(temp);
    this.clearFields();
    successfulSubmitLabel.setVisible(true);
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
  }

  @Override
  protected void highlightRequirements(boolean visible) {
    if (visible) {
      patientNameLabel.getStyleClass().clear();
      patientNameLabel.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(
          patientNameLabel, "Enter name of patient that\nwill be receiving the lab test");

      patientIdLabel.getStyleClass().clear();
      patientIdLabel.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(
          patientIdLabel, "Enter ID of patient that\nwill be receiving the lab test");

      labTypeLabel.getStyleClass().clear();
      labTypeLabel.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(labTypeLabel, "Select type of lab test\nthat patient will receive");

    } else {
      patientNameLabel.getStyleClass().clear();
      patientNameLabel.getStyleClass().add("form-header");
      patientNameLabel.setTooltip(null);

      patientIdLabel.getStyleClass().clear();
      patientIdLabel.getStyleClass().add("form-header");
      patientIdLabel.setTooltip(null);

      labTypeLabel.getStyleClass().clear();
      labTypeLabel.getStyleClass().add("form-header");
      labTypeLabel.setTooltip(null);
    }
  }

  @FXML
  public void clearFields() {
    patientIdField.clear();
    patientNameField.clear();
    labTypeChoiceBox.setValue(null);
  }
}
