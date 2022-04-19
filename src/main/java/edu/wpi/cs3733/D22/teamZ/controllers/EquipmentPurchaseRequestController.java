package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class EquipmentPurchaseRequestController extends ServiceRequestController {

  @FXML private ChoiceBox equipmentTypeChoiceBox;
  @FXML private ChoiceBox paymentMethodChoiceBox;
  @FXML private Label errorSavingLabel;
  @FXML private Label successfulSubmitLabel;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    menuName = "Medical Equipment Purchase Request";

    equipmentTypeChoiceBox.setItems(
        FXCollections.observableArrayList("Bed", "Infusion Pump", "X-Ray", "Recliner"));
    paymentMethodChoiceBox.setItems(FXCollections.observableArrayList("Credit Card", "Debit Card"));

    equipmentTypeChoiceBox.setValue(null);
    paymentMethodChoiceBox.setValue(null);

    errorSavingLabel.setVisible(false);
    submitButton.setDisable(true);
    successfulSubmitLabel.setVisible(false);

    equipmentTypeChoiceBox.setOnAction(event -> validateButton());
    paymentMethodChoiceBox.setOnAction(event -> validateButton());
  }

  @FXML
  public void validateButton() {
    if (!equipmentTypeChoiceBox.getSelectionModel().isEmpty()
        && !paymentMethodChoiceBox.getSelectionModel().isEmpty()) {
      submitButton.setDisable(false);
      System.out.println("Equipment Purchase Request Submit Button enabled");
    } else {
      submitButton.setDisable(true);
      System.out.println("Equipment Purchase Request Submit Button disabled");
    }
  }

  @FXML
  public void onSubmitButtonClicked(ActionEvent event) {
    List<ServiceRequest> serviceRequestList = FacadeDAO.getInstance().getAllServiceRequests();
    int id = 0;
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

    EquipmentPurchaseRequest temp =
        new EquipmentPurchaseRequest(
            requestID,
            status,
            issuer,
            handler,
            FacadeDAO.getInstance().getLocationByID("zSTOR00101"),
            equipmentTypeChoiceBox.getSelectionModel().getSelectedItem().toString(),
            paymentMethodChoiceBox.getSelectionModel().getSelectedItem().toString());

    if (FacadeDAO.getInstance().addEquipmentPurchaseRequest(temp)) {
      this.clearFields();
      successfulSubmitLabel.setVisible(true);
    }
  }

  @FXML
  private void toEquipmentPurchaseRequestList(ActionEvent event) {}

  @FXML
  protected void onResetButtonClicked(ActionEvent event) {
    equipmentTypeChoiceBox.setValue(null);
    paymentMethodChoiceBox.setValue(null);
    successfulSubmitLabel.setVisible(false);
  }

  @FXML
  public void clearFields() {
    equipmentTypeChoiceBox.setValue(null);
    paymentMethodChoiceBox.setValue(null);
  }
}