package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class EquipmentPurchaseRequestController extends ServiceRequestController
    implements IMenuAccess, Initializable {

  @FXML public Label equipmentTypeLabel;
  @FXML public Label paymentMethodLabel;
  @FXML private MFXButton equipmentPurchaseRequestListButton;
  @FXML private Label currentRequestsLabel;
  @FXML private ChoiceBox equipmentTypeChoiceBox;
  @FXML private ChoiceBox paymentMethodChoiceBox;
  @FXML private Label errorSavingLabel;
  @FXML private Label successfulSubmitLabel;
  @FXML private MFXButton submitButton;

  private String toPurchaseListURL =
      "edu/wpi/cs3733/D22/teamZ/views/EquipmentPurchaseRequestList.fxml";

  @Override
  public void initialize(URL location, ResourceBundle resources) {

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
    successfulSubmitLabel.setText("Successfully Submitted Payment!");
    initializeHelpGraphic();
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

    UniqueID id = new UniqueID();
    String requestID = id.generateID("BUYEQ");
    // Create entities for submission

    ServiceRequest.RequestStatus status = ServiceRequest.RequestStatus.UNASSIGNED;
    Employee issuer = MenuController.getLoggedInUser();
    Employee handler = null;
    LocalDateTime opened = LocalDateTime.now();
    LocalDateTime closed = null;

    EquipmentPurchaseRequest temp =
        new EquipmentPurchaseRequest(
            requestID,
            status,
            issuer,
            handler,
            FacadeDAO.getInstance().getLocationByID("zSTOR00101"),
            opened,
            closed,
            equipmentTypeChoiceBox.getSelectionModel().getSelectedItem().toString(),
            paymentMethodChoiceBox.getSelectionModel().getSelectedItem().toString());

    if (FacadeDAO.getInstance().addEquipmentPurchaseRequest(temp)) {
      this.clearFields();
      successfulSubmitLabel.setVisible(true);
    }
  }

  @FXML
  private void toEquipmentPurchaseRequestList(ActionEvent event) throws IOException {
    menu.load(toPurchaseListURL);
  }

  @FXML
  protected void onResetButtonClicked(ActionEvent event) {
    equipmentTypeChoiceBox.setValue(null);
    paymentMethodChoiceBox.setValue(null);
    successfulSubmitLabel.setVisible(false);
  }

  @Override
  protected void highlightRequirements(boolean visible) {
    if (visible) {
      equipmentTypeLabel.getStyleClass().clear();
      equipmentTypeLabel.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(equipmentTypeLabel, "Select equipment type\nto purchase");

      paymentMethodLabel.getStyleClass().clear();
      paymentMethodLabel.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(paymentMethodLabel, "Select payment method\nfor use on purchase");
    } else {
      equipmentTypeLabel.getStyleClass().clear();
      equipmentTypeLabel.getStyleClass().add("form-header");
      equipmentTypeLabel.setTooltip(null);

      paymentMethodLabel.getStyleClass().clear();
      paymentMethodLabel.getStyleClass().add("form-header");
      paymentMethodLabel.setTooltip(null);
    }
  }

  @FXML
  public void clearFields() {
    equipmentTypeChoiceBox.setValue(null);
    paymentMethodChoiceBox.setValue(null);
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Medical Equipment Purchase Request";
  }
}
