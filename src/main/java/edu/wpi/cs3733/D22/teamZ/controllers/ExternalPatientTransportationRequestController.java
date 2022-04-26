package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import edu.wpi.cs3733.D22.teamZ.entity.ExternalPatientTransportationRequest;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

public class ExternalPatientTransportationRequestController extends ServiceRequestController
    implements IMenuAccess {
  @FXML private Button submitButton;
  @FXML private Button resetButton;
  @FXML private MFXTextField patientNameField;
  @FXML private MFXTextField patientIDField;
  @FXML private MFXTextField destinationField;
  // @FXML private MFXTextField departureTimeField;
  @FXML private MFXDatePicker departureDateField;
  @FXML private Label successfulSubmitLabel;
  @FXML private Label errorSavingLabel;
  @FXML private Rectangle warningBackground;

  private final String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";

  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    menuName = "External Patient Transportation Request";
    errorSavingLabel.setVisible(false);
    successfulSubmitLabel.setVisible(false);
    warningBackground.setVisible(false);
    submitButton.setDisable(false);
  }

  @FXML
  protected void onSubmitButtonClicked(ActionEvent event) throws SQLException {
    FacadeDAO dao = FacadeDAO.getInstance();
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

    Employee issuer = MenuController.getLoggedInUser();
    Employee handler = null;
    Location tempLoc = dao.getLocationByID("zEXIT00101");
    String patientName = patientNameField.getText();
    String patientID = patientIDField.getText();
    String destination = destinationField.getText();
    LocalDate departureDate = departureDateField.getValue();
    ExternalPatientTransportationRequest temp =
        new ExternalPatientTransportationRequest(
            requestID,
            ServiceRequest.RequestStatus.UNASSIGNED,
            issuer,
            handler,
            tempLoc,
            patientID,
            patientName,
            destination,
            departureDate);
    if (dao.addExternalPatientTransport(temp)) {
      System.out.println("successful addition of patient transport request");
    } else {
      System.out.println("failed addition of patient transport request");
    }
  }

  @FXML
  protected void onResetButtonClicked(ActionEvent event) throws IOException {
    patientNameField.clear();
    patientIDField.clear();
    destinationField.clear();
    departureDateField.setValue(null);
    // departureTimeField.clear();
    departureDateField.setValue(null);
    successfulSubmitLabel.setVisible(false);
    warningBackground.setVisible(false);
  }

  @FXML
  protected void validateButton() {
    if (!patientNameField.getText().trim().isEmpty()
        && !patientIDField.getText().trim().isEmpty()
        && !destinationField.getText().trim().isEmpty()
        // && !departureTimeField.getText().trim().isEmpty()
        && !departureDateField.getText().trim().isEmpty()) {
      submitButton.setDisable(false);
    } else {
      submitButton.setDisable(true);
      errorSavingLabel.setVisible(true);
    }
  }
}
