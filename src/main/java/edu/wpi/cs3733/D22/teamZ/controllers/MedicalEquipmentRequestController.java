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
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;

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
  @FXML private Region backRegion;

  // URLs
  private String toMedicalEquipmentRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentRequestList.fxml";

  // Lists
  private List<Location> locationList;
  private List<MedicalEquipmentDeliveryRequest> equipmentRequestList;
  private String backSVG =
      "M 13.83 19 C 13.6806 19.0005 13.533 18.9675 13.398 18.9035 C 13.263 18.8395 13.1441 18.746 13.05 18.63 L 8.22 12.63 C 8.07291 12.4511 7.99251 12.2266 7.99251 11.995 C 7.99251 11.7634 8.07291 11.5389 8.22 11.36 L 13.22 5.36 C 13.3897 5.15578 13.6336 5.02736 13.8981 5.00298 C 14.1625 4.9786 14.4258 5.06026 14.63 5.23 C 14.8342 5.39974 14.9626 5.64365 14.987 5.90808 C 15.0114 6.1725 14.9297 6.43578 14.76 6.64 L 10.29 12 L 14.61 17.36 C 14.7323 17.5068 14.81 17.6855 14.8338 17.8751 C 14.8577 18.0646 14.8268 18.257 14.7447 18.4296 C 14.6627 18.6021 14.5329 18.7475 14.3708 18.8486 C 14.2087 18.9497 14.021 19.0022 13.83 19 Z";
  private String white = "FFFFFF";
  private String svgCSSLine = "-fx-background-color: %s";

  @FXML
  public void initialize(URL location, ResourceBundle resources) {

    SVGPath icon = new SVGPath();
    icon.setContent(backSVG);
    backRegion.setShape(icon);
    backRegion.setStyle(String.format(svgCSSLine, white));

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
      errorSavingLabel.setVisible(true);
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
      System.out.println("Medical Equipment Request Submit Button disabled");
    }
  }

  public void onNavigateToMedicalRequestList() throws IOException {
    menu.selectMenu(3);
    menu.load(toMedicalEquipmentRequestURL);
  }
}
