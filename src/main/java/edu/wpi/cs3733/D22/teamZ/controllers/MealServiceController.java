package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.*;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class MealServiceController extends ServiceRequestController {

  @FXML private MFXTextField enterPatientName;
  @FXML private MFXTextField enterPatientID;
  @FXML private MFXTextField enterStaffAssigned;
  @FXML private ChoiceBox<String> roomNumberDropDown;
  @FXML private ChoiceBox<String> mealOptionDropDown;
  @FXML private ChoiceBox<String> orderStatusDropDown;
  @FXML private ListView<String> currentRequests;
  @FXML private Label infoLabel;

  // Lists
  private List<Location> locationList;
  private List<ServiceRequest> mealRequestList = new ArrayList<>();

  private ObservableList<String> currReq = FXCollections.observableList(new ArrayList<>());

  FacadeDAO instanceDAO = FacadeDAO.getInstance();

  private void updateCurrentMealRequestList() {
    String requestCon = null;
    currReq.clear();
    currentRequests.getItems().removeAll();
    for (ServiceRequest model : mealRequestList) {
      System.out.println(model.getRequestID());

      requestCon =
          model.getRequestID()
              + " "
              + model.getStatus()
              + " "
              + model.getTargetLocation().getNodeID();
      currReq.add(requestCon);
    }
    currentRequests.setItems(currReq);
    currentRequests.refresh();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    menuName = "Meal Request";

    //    locationList =
    //        instanceDAO.getAllLocations().stream()
    //            .filter(Location -> Location.getNodeType() == "PATI")
    //            .collect(Collectors.toList());
    locationList = instanceDAO.getALlLocationsByType("PATI");
    mealRequestList =
        instanceDAO.getAllServiceRequests().stream()
            .filter(REQ -> REQ.getType() == ServiceRequest.RequestType.MEAL)
            .collect(Collectors.toList());

    String temp = null;
    for (Location model : locationList) {
      // nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName
      temp =
          model.getNodeID()
              + " "
              + model.getXcoord()
              + " "
              + model.getYcoord()
              + " "
              + model.getFloor()
              + " "
              + model.getBuilding()
              + " "
              + model.getNodeType()
              + " "
              + model.getLongName()
              + " "
              + model.getShortName();
      System.out.println(temp);
    }

    List<String> roomList = new ArrayList<>();
    for (Location model : locationList) {
      roomList.add(model.getShortName().substring(model.getShortName().length() - 4));
    }
    roomNumberDropDown.setItems(FXCollections.observableArrayList(roomList));
    mealOptionDropDown.setItems(FXCollections.observableArrayList("BREAKFAST", "LUNCH", "DINNER"));
    orderStatusDropDown.setItems(
        FXCollections.observableArrayList("IN PROGRESS", "PROCESSING", "COMPLETED"));
    orderStatusDropDown.setValue("IN PROGRESS");
    //    currReqLabel.setText("");

    updateCurrentMealRequestList();
  }

  @Override
  protected void onSubmitButtonClicked(ActionEvent event) throws SQLException {
    System.out.println("Submit Button Clicked");
    System.out.println("Patient Name: " + enterPatientName.getText().trim());
    System.out.println("Patient ID: " + enterPatientID.getText().trim());
    System.out.println("Room Number: " + roomNumberDropDown.getValue());
    System.out.println("Staff Assigned: " + enterStaffAssigned.getText().trim());
    System.out.println("Meal Option: " + mealOptionDropDown.getValue());
    System.out.println("Order Status: " + orderStatusDropDown.getValue());

    String id;

    if (mealRequestList.isEmpty()) {
      System.out.println("Meal is empty");
      id = "REQ0";
    } else {
      //      List<ServiceRequest> currentList = instanceDAO.getAllServiceRequests();
      ServiceRequest lastestReq = mealRequestList.get(mealRequestList.size() - 1);
      id = lastestReq.getRequestID();
    }
    // Create new REQID
    int num = 1 + Integer.parseInt(id.substring(id.lastIndexOf("Q") + 1));
    String requestID = "REQ" + num;

    //    ServiceRequest newRequest = new ServiceRequest();

    // Create entities for submission
    String mealServiceOption = mealOptionDropDown.getValue().toString();
    ServiceRequest.RequestStatus status = ServiceRequest.RequestStatus.PROCESSING;
    Employee issuer = new Employee("Pat" + num, "Pat", Employee.AccessType.ADMIN, "", "");
    Employee handler = new Employee("Jake" + num, "Jake", Employee.AccessType.ADMIN, "", "");

    //     Update meal request table to show in use
    Location targetLocation = new Location();
    Patient patient =
        new Patient(
            enterPatientID.getText().trim(), enterPatientName.getText().trim(), targetLocation);
    for (Location model : locationList) {
      if (model
          .getShortName()
          .substring(model.getShortName().length() - 4)
          .equals(roomNumberDropDown.getValue())) {
        targetLocation = instanceDAO.getLocationByID(model.getNodeID());
      }
    }

    MealServiceRequest temp =
        new MealServiceRequest(
            requestID, status, issuer, handler, targetLocation, patient, mealServiceOption);
    temp.setTargetLocation(targetLocation);
    temp.setPatient(patient);
    temp.setMealServiceOption(mealServiceOption);

    mealRequestList.add(temp);

    instanceDAO.addServiceRequestFromList(mealRequestList);

    updateCurrentMealRequestList();

    enterPatientName.clear();
    enterPatientID.clear();
    enterStaffAssigned.clear();
    roomNumberDropDown.setValue(null);
    mealOptionDropDown.setValue(null);
    orderStatusDropDown.setValue("IN PROGRESS");
  }

  @Override
  protected void onResetButtonClicked(ActionEvent event) throws IOException {
    System.out.println("Reset Button Clicked");
    infoLabel.setText("");
    enterPatientName.clear();
    enterPatientID.clear();
    enterStaffAssigned.clear();
    roomNumberDropDown.setValue(null);
    mealOptionDropDown.setValue(null);
    orderStatusDropDown.setValue("IN PROGRESS");
  }

  public void enterPatientName(ActionEvent event) {}

  public void enterPatientID(ActionEvent event) {}

  public void enterRoomNumber(ActionEvent event) {}

  public void chooseMealOption(MouseEvent mouseEvent) {}

  public void chooseOrderStatus(MouseEvent mouseEvent) {}

  public void enterStaffAssigned(ActionEvent event) {}

  public void validateButton(KeyEvent keyEvent) {
    if (!enterPatientName.getText().trim().isEmpty()
        && !enterPatientID.getText().trim().isEmpty()
        && !enterStaffAssigned.getText().trim().isEmpty()
        && !roomNumberDropDown.getValue().isEmpty()
        && !mealOptionDropDown.getValue().isEmpty()
        && !orderStatusDropDown.getValue().isEmpty()) {
      submitButton.setDisable(false);
      System.out.println("Meal Request Submit button enabled");
    } else {
      submitButton.setDisable(true);
      System.out.println("Meal Request Submit button disabled");
    }
  }

  public void onMouseClick(MouseEvent mouseEvent) {
    System.out.println("TESTING MOUSE CLICK");
    System.out.println(currentRequests.getFocusModel().getFocusedIndex()); // number starting at 0
    System.out.println(currentRequests.getFocusModel().getFocusedItem()); // string of selected
  }
}
