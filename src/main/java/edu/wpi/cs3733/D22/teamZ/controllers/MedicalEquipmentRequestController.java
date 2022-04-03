package edu.wpi.cs3733.D22.teamZ.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D22.teamZ.*;
import edu.wpi.cs3733.D22.teamZ.database.ILocationDAO;
import edu.wpi.cs3733.D22.teamZ.database.IMedEquipReqDAO;
import edu.wpi.cs3733.D22.teamZ.database.LocationDAOImpl;
import edu.wpi.cs3733.D22.teamZ.database.MedEquipReqDAOImpl;
import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipmentDeliveryRequest;
import java.io.IOException;
import java.util.List;

import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MedicalEquipmentRequestController {
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
  @FXML private TextField enterNodeType;
  @FXML private ChoiceBox equipmentDropDown;

  // URLs
  private final String toLandingPageURL = "views/LandingPage.fxml";

  // Lists
  private List<Location> locationList;
  private List<MedicalEquipmentDeliveryRequest> equipmentRequestList;

  @FXML
  public void initialize() {
    locationList = locationDAO.getAllLocations();
    equipmentRequestList = medicalEquipmentRequestDAO.getAllMedEquipReq();

    for (Location model : locationList) {
      System.out.println(model.getNodeID());
    }

    equipmentDropDown.setItems(FXCollections.observableArrayList("Bed", "IV", "Pillow"));
    // //example
  }

  @FXML
  private void onBackButtonClicked(ActionEvent event) throws IOException {
    Stage mainStage = (Stage) backButton.getScene().getWindow();
    Parent root = FXMLLoader.load(App.class.getResource(toLandingPageURL));
    Scene scene = new Scene(root);
    mainStage.setScene(scene);
  }

  @FXML
  private void onResetButtonClicked(ActionEvent event) throws IOException {
    enterRoomNumber.clear();
    enterFloorNumber.clear();
    enterNodeType.clear();
    equipmentDropDown.setValue(null);
  }

  @FXML
  private void onSubmitButtonClicked(ActionEvent actionEvent) {
    System.out.println("Room Number: " + enterRoomNumber.getText());
    System.out.println("Floor Number: " + enterFloorNumber.getText());
    System.out.println("nodeType: " + enterNodeType.getText());
    System.out.println("Equipment Selected: " + equipmentDropDown.getValue());

    MedicalEquipmentDeliveryRequest lastestReq = equipmentRequestList.get(equipmentRequestList.size() - 1);
    String id = lastestReq.getRequestID();
    int num = 1 + Integer.parseInt(id.substring(id.lastIndexOf("Q") + 1));

    String requestID = "REQ" + num;
    String itemID = equipmentDropDown.getValue().toString();
    ServiceRequest.RequestStatus status = ServiceRequest.RequestStatus.PROCESSING;
    Employee issuer = new Employee(
            "Pat"+num,
            "Pat",
            Employee.AccessType.ADMIN,
            "",
            ""
    );
    Employee handler = new Employee(
            "Jake"+num,
            "Jake",
            Employee.AccessType.ADMIN,
            "",
            ""
    );

    //TODO add method to pick a free MedicalEquipment from the table of this type
    String equipmentID = equipmentDropDown.getValue().toString();

    String nodeID = Location.createNodeID(
            enterNodeType.getText(),
            enterRoomNumber.getText(),
            enterFloorNumber.getText()
    );
    Location targetLoc = locationDAO.getLocationByID(nodeID);

    MedicalEquipmentDeliveryRequest temp = new MedicalEquipmentDeliveryRequest(
            requestID, status, issuer, handler, equipmentID, targetLoc
    );

    medicalEquipmentRequestDAO.addMedEquipReq(temp);
  }

  @FXML
  private void validateButton() {
    if (!enterRoomNumber.getText().trim().isEmpty()
        && !enterFloorNumber.getText().trim().isEmpty()
        && !enterNodeType.getText().trim().isEmpty()
        && !equipmentDropDown.getSelectionModel().isEmpty()) {
      submitButton.setDisable(false);
    }
  }
}
