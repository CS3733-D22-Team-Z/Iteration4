package edu.wpi.cs3733.D22.teamZ.controllers;

import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;

public class ExternalPatientTransportationRequestController extends ServiceRequestController
    implements IMenuAccess {
  @FXML private Button backButton;
  @FXML private Button submitButton;
  @FXML private Button resetButton;
  @FXML private MFXTextField patientNameField;
  @FXML private MFXTextField patientIDField;
  @FXML private MFXTextField destinationField;
  @FXML private MFXTextField departureTimeField;
  @FXML private MFXDatePicker departureDateField;
  @FXML private Label successfulSubmitLabel;
  @FXML private Label errorSavingLabel;
  @FXML private Rectangle warningBackground;

  protected MenuController menu;

  private final String toHomepageURL = "edu/wpi/cs3733/D22/teamZ/views/Homepage.fxml";

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    menuName = "External Patient Transportation Request";
    errorSavingLabel.setVisible(false);
    successfulSubmitLabel.setVisible(false);
    warningBackground.setVisible(false);
    submitButton.setDisable(false);
  }

  @Override
  protected void onSubmitButtonClicked(ActionEvent event) throws SQLException {

    departureDateField.getValue();
  }

  @Override
  protected void onResetButtonClicked(ActionEvent event) throws IOException {
    patientNameField.clear();
    patientIDField.clear();
    destinationField.clear();
    departureTimeField.clear();
    departureDateField.setValue(null);
    successfulSubmitLabel.setVisible(false);
    warningBackground.setVisible(false);
  }

  @FXML
  protected void validateButton() {
    if (!patientNameField.getText().trim().isEmpty()
        && !patientIDField.getText().trim().isEmpty()
        && !destinationField.getText().trim().isEmpty()
        && !departureTimeField.getText().trim().isEmpty()
        && !departureDateField.getText().trim().isEmpty()) {
      submitButton.setDisable(false);
    }
  }
}
