package edu.wpi.cs3733.D22.teamZ.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D22.teamZ.*;
import edu.wpi.cs3733.D22.teamZ.database.*;
import edu.wpi.cs3733.D22.teamZ.entity.*;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MedicalEquipmentRequestController implements IMenuAccess {
  private ILocationDAO locationDAO = new LocationDAOImpl();
  private IMedEquipReqDAO medicalEquipmentRequestDAO = new MedEquipReqDAOImpl();

  @FXML private JFXButton backButton;
  @FXML private JFXButton resetButton;
  @FXML private JFXButton submitButton;

  @FXML private Label header;
  @FXML private Label objectBodyText;
  @FXML private Label roomNumberLabel;
  @FXML private Label equipmentLabel;
  @FXML private TextField enterRoomNumber;
  @FXML private TextField enterFloorNumber;
  @FXML private ChoiceBox enterNodeType;
  @FXML private ChoiceBox equipmentDropDown;
  @FXML private Label errorSavingLabel;

  // URLs
  private final String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";
  private String toMedicalEquipmentRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentRequestList.fxml";

  // Lists
  private List<Location> locationList;
  private List<MedicalEquipmentDeliveryRequest> equipmentRequestList;

  private MenuController menu;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @FXML
  public void initialize() {
    locationList = locationDAO.getAllLocations();
    equipmentRequestList = medicalEquipmentRequestDAO.getAllMedEquipReq();

    for (Location model : locationList) {
      System.out.println(model.getNodeID());
    }

    equipmentDropDown.setItems(
        FXCollections.observableArrayList("Bed", "Recliner", "X-Ray", "Infusion Pump"));
    enterNodeType.setItems(
        FXCollections.observableArrayList(
            "DEPT", "EXIT", "HALL", "INFO", "LABS", "RETL", "SERV", "STAI", "ELEV", "BATH", "STOR",
            "PATI"));
    // //example
    errorSavingLabel.setVisible(false);
  }

  @FXML
  private void onBackButtonClicked(ActionEvent event) throws IOException {
    menu.load(toLandingPageURL);
  }

  @FXML
  private void onResetButtonClicked(ActionEvent event) throws IOException {
    enterRoomNumber.clear();
    enterFloorNumber.clear();
    enterNodeType.setValue(null);
    equipmentDropDown.setValue(null);
  }

  @FXML
  private void onSubmitButtonClicked(ActionEvent actionEvent) {
    // Debug
    System.out.println("Room Number: " + enterRoomNumber.getText());
    System.out.println("Floor Number: " + enterFloorNumber.getText());
    System.out.println("nodeType: " + enterNodeType.getValue());
    System.out.println("Equipment Selected: " + equipmentDropDown.getValue());

    String id;
    // Check for empty db and set first request (will appear as REQ1 in the db)

    if (equipmentRequestList.isEmpty()) {
      System.out.println("Equipment is empty");
      id = "REQ0";
    } else {
      MedicalEquipmentDeliveryRequest lastestReq =
          equipmentRequestList.get(equipmentRequestList.size() - 1);
      id = lastestReq.getRequestID();
    }
    // Create new REQID
    int num = 1 + Integer.parseInt(id.substring(id.lastIndexOf("Q") + 1));
    String requestID = "REQ" + num;

    // Create entities for submission
    String itemID = equipmentDropDown.getValue().toString();
    ServiceRequest.RequestStatus status = ServiceRequest.RequestStatus.PROCESSING;
    Employee issuer = new Employee("Pat" + num, "Pat", Employee.AccessType.ADMIN, "", "");
    Employee handler = new Employee("Jake" + num, "Jake", Employee.AccessType.ADMIN, "", "");

    String equipmentID = equipmentDropDown.getValue().toString();
    IMedicalEquipmentDAO medicalEquipmentDAO = new MedicalEquipmentDAOImpl();

    // Find available equipment, if there is one. Else return null
    equipmentID = medicalEquipmentDAO.getFirstAvailableEquipmentByType(equipmentID);

    if (equipmentID == null) {
      errorSavingLabel.setVisible(true);
      return;
    } else {

      // Update medical equipment table to show in use
      String nodeID =
          Location.createNodeID(
              enterNodeType.getValue().toString(),
              enterRoomNumber.getText(),
              enterFloorNumber.getText());
      Location targetLoc = locationDAO.getLocationByID(nodeID);

      MedicalEquipmentDeliveryRequest temp =
          new MedicalEquipmentDeliveryRequest(
              requestID, status, issuer, handler, equipmentID, targetLoc);

      medicalEquipmentRequestDAO.addMedEquipReq(temp);
    }
    errorSavingLabel.setVisible(false);
  }

  @FXML
  private void validateButton() {
    if (!enterRoomNumber.getText().trim().isEmpty()
        && !enterFloorNumber.getText().trim().isEmpty()
        && !enterNodeType.getSelectionModel().isEmpty()
        && !equipmentDropDown.getSelectionModel().isEmpty()) {
      submitButton.setDisable(false);
    }
  }

  public void onNavigateToMedicalRequestList() throws IOException {
    menu.selectMenu(3);
    menu.load(toMedicalEquipmentRequestURL);
  }
}
