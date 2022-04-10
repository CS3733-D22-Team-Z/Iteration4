package edu.wpi.cs3733.D22.teamZ.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.database.IServiceRequestDAO;
import edu.wpi.cs3733.D22.teamZ.database.ServiceRequestDAOImpl;
import edu.wpi.cs3733.D22.teamZ.entity.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;

public class LabRequestController implements IMenuAccess {

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

  private final String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";
  private final String toLabServiceRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/LabRequestList.fxml";

  private FacadeDAO facadeDAO;

  private MenuController menu;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @FXML
  public void initialize() {

    facadeDAO = new FacadeDAO();

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
  public void onBackButtonClicked(ActionEvent event) throws IOException {
    menu.load(toLandingPageURL);
  }

  @FXML
  private void toLabServiceRequestList(ActionEvent event) throws IOException {
    menu.load(toLabServiceRequestURL);
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

    facadeDAO.addLabServiceRequest(temp);
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
