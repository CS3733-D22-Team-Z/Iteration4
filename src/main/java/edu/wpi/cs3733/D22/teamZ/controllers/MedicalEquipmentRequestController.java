package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.entity.*;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class MedicalEquipmentRequestController extends ServiceRequestController {
  @FXML private Label header;
  @FXML private Label objectBodyText;
  @FXML private Label roomNumberLabel;
  @FXML private Label equipmentLabel;
  @FXML private MFXTextField enterRoomNumber;
  @FXML private MFXTextField enterFloorNumber;
  @FXML private ChoiceBox<String> nodeTypeDropDown;
  @FXML private ChoiceBox<String> equipmentDropDown;
  @FXML private Label errorSavingLabel;

  // URLs
  private String toMedicalEquipmentRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentRequestList.fxml";

  // Lists
  private List<Location> locationList;
  private List<MedicalEquipmentDeliveryRequest> equipmentRequestList;

  @FXML
  public void initialize(URL location, ResourceBundle resources) {

    menuName = "Medical Equipment Request";

    locationList = database.getAllLocations();
    equipmentRequestList = database.getAllMedicalEquipmentRequest();

    for (Location model : locationList) {
      System.out.println(model.getNodeID());
    }

    equipmentDropDown.setItems(
        FXCollections.observableArrayList("Bed", "Recliner", "X-Ray", "Infusion Pump"));
    nodeTypeDropDown.setItems(
        FXCollections.observableArrayList(
            "DEPT", "EXIT", "HALL", "INFO", "LABS", "RETL", "SERV", "STAI", "ELEV", "BATH", "STOR",
            "PATI"));
    // //example
    nodeTypeDropDown.getSelectionModel().select(0);
    equipmentDropDown.getSelectionModel().select(0);
    System.out.println(
        "ChoiceBox 1 value" + nodeTypeDropDown.getSelectionModel().getSelectedItem().isEmpty());
    errorSavingLabel.setVisible(false);
  }

  @FXML
  protected void onResetButtonClicked(ActionEvent event) throws IOException {
    enterRoomNumber.clear();
    enterFloorNumber.clear();
    submitButton.setDisable(true);
    nodeTypeDropDown.setValue(null);
    equipmentDropDown.setValue(null);
    nodeTypeDropDown.getSelectionModel().select(0);
    equipmentDropDown.getSelectionModel().select(0);
    //    nodeTypeDropDown.setValue(null);
    //    equipmentDropDown.setValue(null);
    validateButton();
  }

  @FXML
  protected void onSubmitButtonClicked(ActionEvent actionEvent) {
    // Debug
    System.out.println("Room Number: " + enterRoomNumber.getText());
    System.out.println("Floor Number: " + enterFloorNumber.getText());
    System.out.println("nodeType: " + nodeTypeDropDown.getValue());
    System.out.println("Equipment Selected: " + equipmentDropDown.getValue());

    String id;
    // Check for empty db and set first request (will appear as REQ1 in the db)

    if (equipmentRequestList.isEmpty()) {
      System.out.println("Equipment is empty");
      id = "REQ0";
    } else {
      List<ServiceRequest> currentList = database.getAllServiceRequests();
      ServiceRequest lastestReq = currentList.get(currentList.size() - 1);
      id = lastestReq.getRequestID();
    }
    // Create new REQID
    int num = 1 + Integer.parseInt(id.substring(id.lastIndexOf("Q") + 1));
    String requestID = "REQ" + num;

    // Create entities for submission
    String itemID = equipmentDropDown.getValue().toString();
    ServiceRequest.RequestStatus status = ServiceRequest.RequestStatus.UNASSIGNED;
    Employee issuer = MenuController.getLoggedInUser();
    Employee handler = null;

    String equipmentID = equipmentDropDown.getValue().toString();

    // Find available equipment, if there is one. Else return null
    equipmentID = database.getFirstAvailableEquipmentByType(equipmentID);

    if (equipmentID == null) {
      errorSavingLabel.setVisible(true);
      return;
    } else {

      // Update medical equipment table to show in use
      String nodeID =
          Location.createNodeID(
              nodeTypeDropDown.getValue().toString(),
              enterRoomNumber.getText(),
              enterFloorNumber.getText());
      Location targetLoc = database.getLocationByID(nodeID);

      MedicalEquipmentDeliveryRequest temp =
          new MedicalEquipmentDeliveryRequest(
              requestID, status, issuer, handler, equipmentID, targetLoc);

      database.addMedicalEquipmentRequest(temp);
    }
    errorSavingLabel.setVisible(false);
  }

  @FXML
  private void validateButton() {
    if (!enterRoomNumber.getText().trim().isEmpty()
        && !enterFloorNumber.getText().trim().isEmpty()
        && !nodeTypeDropDown.getSelectionModel().getSelectedItem().isEmpty()
        && !equipmentDropDown.getSelectionModel().getSelectedItem().isEmpty()) {
      submitButton.setDisable(false);
    } else {
      submitButton.setDisable(true);
      errorSavingLabel.setVisible(true);
      System.out.println("Medical Equipment Request Submit Button disabled");
    }
  }

  public void onNavigateToMedicalRequestList() throws IOException {
    menu.selectMenu(3);
    menu.load(toMedicalEquipmentRequestURL);
  }
}
