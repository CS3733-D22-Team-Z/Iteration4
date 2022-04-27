package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.entity.*;
import io.github.palexdev.materialfx.controls.MFXTextField;
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
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;

public class CleaningRequestController extends ServiceRequestController {
  @FXML private Label successfulSubmitLabel;
  @FXML private Label header;
  @FXML private Label objectBodyText;
  @FXML private Label roomNumberLabel;
  @FXML private Label equipmentLabel;
  @FXML private MFXTextField enterRoomNumber;
  @FXML private MFXTextField enterFloorNumber;
  @FXML private MFXTextField enterRequest;
  @FXML private ChoiceBox<String> nodeTypeDropDown;
  @FXML private Label errorSavingLabel;
  @FXML private Region backRegion;

  // URLs
  /*private String toMedicalEquipmentRequestURL =
  "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentRequestList.fxml";*/

  // Lists
  private List<Location> locationList;
  private List<CleaningRequest> cleaningList;
  private String backSVG =
      "M 13.83 19 C 13.6806 19.0005 13.533 18.9675 13.398 18.9035 C 13.263 18.8395 13.1441 18.746 13.05 18.63 L 8.22 12.63 C 8.07291 12.4511 7.99251 12.2266 7.99251 11.995 C 7.99251 11.7634 8.07291 11.5389 8.22 11.36 L 13.22 5.36 C 13.3897 5.15578 13.6336 5.02736 13.8981 5.00298 C 14.1625 4.9786 14.4258 5.06026 14.63 5.23 C 14.8342 5.39974 14.9626 5.64365 14.987 5.90808 C 15.0114 6.1725 14.9297 6.43578 14.76 6.64 L 10.29 12 L 14.61 17.36 C 14.7323 17.5068 14.81 17.6855 14.8338 17.8751 C 14.8577 18.0646 14.8268 18.257 14.7447 18.4296 C 14.6627 18.6021 14.5329 18.7475 14.3708 18.8486 C 14.2087 18.9497 14.021 19.0022 13.83 19 Z";
  private String white = "FFFFFF";
  private String svgCSSLine = "-fx-background-color: %s";

  private String toList = "edu/wpi/cs3733/D22/teamZ/views/CleaningRequestList.fxml";

  @FXML
  public void initialize(URL location, ResourceBundle resources) {

    SVGPath icon = new SVGPath();
    icon.setContent(backSVG);
    backRegion.setShape(icon);
    backRegion.setStyle(String.format(svgCSSLine, white));

    menuName = "Cleaning Request";

    locationList = database.getAllLocations();
    try {
      cleaningList = database.getAllCleaningRequests();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    for (Location model : locationList) {
      System.out.println(model.getNodeID());
    }

    nodeTypeDropDown.setItems(
        FXCollections.observableArrayList(
            "DEPT", "EXIT", "HALL", "INFO", "LABS", "RETL", "SERV", "STAI", "ELEV", "BATH", "STOR",
            "PATI"));
    // //example
    nodeTypeDropDown.getSelectionModel().select(0);
    System.out.println(
        "ChoiceBox 1 value" + nodeTypeDropDown.getSelectionModel().getSelectedItem().isEmpty());
    errorSavingLabel.setVisible(false);
    successfulSubmitLabel.setVisible(false);
  }

  @FXML
  protected void onResetButtonClicked(ActionEvent event) throws IOException {
    enterRoomNumber.clear();
    enterFloorNumber.clear();
    enterRequest.clear();
    submitButton.setDisable(true);
    nodeTypeDropDown.getSelectionModel().select(0);
    validateButton();
    errorSavingLabel.setVisible(false);
    successfulSubmitLabel.setVisible(false);
  }

  @FXML
  protected void onSubmitButtonClicked(ActionEvent actionEvent) {
    // Debug
    System.out.println("Room Number: " + enterRoomNumber.getText());
    System.out.println("Floor Number: " + enterFloorNumber.getText());
    System.out.println("nodeType: " + nodeTypeDropDown.getValue());
    System.out.println("Request: " + enterRequest.getText());

    UniqueID id = new UniqueID();
    String requestID = id.generateID("CLEAN");

    // Create entities for submission
    ServiceRequest.RequestStatus status = ServiceRequest.RequestStatus.UNASSIGNED;
    Employee issuer = MenuController.getLoggedInUser();
    Employee handler = null;
    LocalDateTime opened = LocalDateTime.now();
    LocalDateTime closed = null;

    String request = enterRequest.getText();

    // Update medical equipment table to show in use
    String nodeID =
        Location.createNodeID(
            nodeTypeDropDown.getValue().toString(),
            enterRoomNumber.getText(),
            enterFloorNumber.getText());
    Location targetLoc = database.getLocationByID(nodeID);
    if (targetLoc.getNodeID() == null) {
      errorSavingLabel.setVisible(true);
    } else {
      CleaningRequest temp =
          new CleaningRequest(
              requestID, status, issuer, handler, targetLoc, opened, closed, request);

      database.addCleaningRequest(temp);

      errorSavingLabel.setVisible(false);
      successfulSubmitLabel.setVisible(true);
    }
  }

  @FXML
  private void validateButton() {
    if (!enterRoomNumber.getText().trim().isEmpty()
        && !enterFloorNumber.getText().trim().isEmpty()
        && !nodeTypeDropDown.getSelectionModel().getSelectedItem().isEmpty()
        && !enterRequest.getText().trim().isEmpty()) {
      submitButton.setDisable(false);
    } else {
      submitButton.setDisable(true);
      System.out.println("Cleaning Request Submit Button disabled");
    }
  }

  @FXML
  protected void toList(ActionEvent actionEvent) throws IOException {
    menu.load(toList);
  }
}
