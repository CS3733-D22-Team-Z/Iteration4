package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.SecurityServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.UniqueID;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class SecurityServiceController extends ServiceRequestController {
  public ChoiceBox<String> urgencyBox;
  public MFXTextField reasonTextField;
  public MFXTextField nodeIdField;
  public Label errorLabel;
  public MFXButton submitButton;
  @FXML public Label urgencyFormHeader;
  @FXML public Label securityReasonFormHeader;
  @FXML public Label roomNumberFormHeader;
  FacadeDAO facadeDAO = FacadeDAO.getInstance();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    urgencyBox.getItems().addAll("Low", "Medium", "High", "Emergency");
    urgencyBox.getSelectionModel().select("Low");
    errorLabel.setVisible(false);

    menuName = "Security Request";
    initializeHelpGraphic();
  }

  @Override
  protected void onSubmitButtonClicked(ActionEvent event) throws SQLException {
    List<ServiceRequest> servReqs = facadeDAO.getAllServiceRequests();

    ServiceRequest last = servReqs.get(servReqs.size() - 1);

    UniqueID id = new UniqueID();
    String requestID = id.generateID("SECR");

    Location tryGet = facadeDAO.getLocationByID(nodeIdField.getText());

    LocalDateTime opened = LocalDateTime.now();
    LocalDateTime closed = null;

    if (tryGet != null && tryGet.getNodeID() != null) {
      SecurityServiceRequest req =
          new SecurityServiceRequest(
              requestID,
              ServiceRequest.RequestStatus.UNASSIGNED,
              MenuController.getLoggedInUser(),
              null,
              tryGet,
              opened,
              closed,
              urgencyBox.getSelectionModel().getSelectedItem(),
              reasonTextField
                  .getText()
                  .substring(0, Math.min(39, reasonTextField.getText().length())));
      facadeDAO.addSecurityServiceRequest(req);
      reasonTextField.setText("");
      nodeIdField.setText("");
      urgencyBox.getSelectionModel().select("Low");
      errorLabel.setVisible(false);
    } else {
      errorLabel.setVisible(true);
    }
  }

  @Override
  protected void onResetButtonClicked(ActionEvent event) throws IOException {
    urgencyBox.getSelectionModel().select("Low");
    reasonTextField.setText("");
    nodeIdField.setText("");
    errorLabel.setVisible(false);
  }

  @Override
  protected void highlightRequirements(boolean visible) {
    if (visible) {
      urgencyFormHeader.getStyleClass().clear();
      urgencyFormHeader.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(urgencyFormHeader, "Select the level\nof urgency of issue");

      securityReasonFormHeader.getStyleClass().clear();
      securityReasonFormHeader.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(securityReasonFormHeader, "Describe reason for\nsecurity request");

      roomNumberFormHeader.getStyleClass().clear();
      roomNumberFormHeader.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(roomNumberFormHeader, "Enter room number that\nsecurity is needed");

    } else {
      urgencyFormHeader.getStyleClass().clear();
      urgencyFormHeader.getStyleClass().add("form-header");
      urgencyFormHeader.setTooltip(null);

      securityReasonFormHeader.getStyleClass().clear();
      securityReasonFormHeader.getStyleClass().add("form-header");
      securityReasonFormHeader.setTooltip(null);

      roomNumberFormHeader.getStyleClass().clear();
      roomNumberFormHeader.getStyleClass().add("form-header");
      roomNumberFormHeader.setTooltip(null);
    }
  }

  public void onNavigateToRequestList(ActionEvent actionEvent) throws IOException {
    menu.selectMenu(1);
    menu.load("edu/wpi/cs3733/D22/teamZ/views/SecurityServiceRequestList.fxml");
  }
}
