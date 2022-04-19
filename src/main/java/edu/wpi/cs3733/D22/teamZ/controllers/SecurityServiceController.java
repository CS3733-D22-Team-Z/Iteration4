package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.SecurityServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class SecurityServiceController extends ServiceRequestController {
  public ChoiceBox<String> urgencyBox;
  public MFXTextField reasonTextField;
  public MFXTextField nodeIdField;
  public Label errorLabel;
  public MFXButton submitButton;
  FacadeDAO facadeDAO = FacadeDAO.getInstance();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    urgencyBox.getItems().addAll("Low", "Medium", "High", "Emergency");
    urgencyBox.getSelectionModel().select("Low");
    errorLabel.setVisible(false);

    menuName = "Security Request";
  }

  @Override
  protected void onSubmitButtonClicked(ActionEvent event) throws SQLException {
    List<ServiceRequest> servReqs = facadeDAO.getAllServiceRequests();

    ServiceRequest last = servReqs.get(servReqs.size() - 1);

    String id = last.getRequestID();
    int idNum = Integer.parseInt(id.substring(id.lastIndexOf("Q") + 1));

    String genID = "REQ" + (idNum + 1);
    Location tryGet = facadeDAO.getLocationByID(nodeIdField.getText());

    if (!tryGet.getNodeID().equals("")) {
      SecurityServiceRequest req =
          new SecurityServiceRequest(
              genID,
              ServiceRequest.RequestType.SECURITY,
              ServiceRequest.RequestStatus.UNASSIGNED,
              MenuController.getLoggedInUser(),
              null,
              tryGet, urgencyBox.getSelectionModel().getSelectedItem(), reasonTextField.getText());
      //    facadeDAO.add(req);
      //TODO: DAO implementation
      errorLabel.setVisible(false);
    } else {
      errorLabel.setVisible(true);
    }
  }

  @Override
  protected void onResetButtonClicked(ActionEvent event) throws IOException {
    urgencyBox.getSelectionModel().select("Low");
    reasonTextField.setText("");
    errorLabel.setVisible(false);
  }

  public void onNavigateToRequestList(ActionEvent actionEvent) throws IOException {
    menu.selectMenu(1);
    menu.load("edu/wpi/cs3733/D22/teamZ/views/ServiceRequest.fxml");
  }
}
