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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class MealServiceController extends ServiceRequestController {

  @FXML private MFXTextField enterPatientName;
  @FXML private MFXTextField enterPatientID;
  @FXML private MFXTextField enterStaffAssigned;
  @FXML private ComboBox<String> roomNumberDropDown;
  @FXML private ComboBox<String> mealOptionDropDown;
  @FXML private ChoiceBox<String> orderStatusDropDown;
  @FXML private ListView<String> currentRequests;
  @FXML private Label mealRequestIndicator;

  // Lists
  private List<Location> locationList;
  private List<MealServiceRequest> mealRequestList = new ArrayList<>();
  private List<ServiceRequest> allServiceRequestList = new ArrayList<>();

  private List<String> breakfastDrinksList = new ArrayList<>();
  private List<String> breakfastEntreesList = new ArrayList<>();
  private List<String> breakfastSidesList = new ArrayList<>();
  private List<String> lunchDrinksList = new ArrayList<>();
  private List<String> lunchEntreesList = new ArrayList<>();
  private List<String> lunchSidesList = new ArrayList<>();
  private List<String> dinnerDrinksList = new ArrayList<>();
  private List<String> dinnerEntreesList = new ArrayList<>();
  private List<String> dinnerSidesList = new ArrayList<>();

  private ObservableList<String> currReq = FXCollections.observableList(new ArrayList<>());

  private final String toMealServiceRequestListURL =
      "edu/wpi/cs3733/D22/teamZ/views/MealServiceRequestList.fxml";

  FacadeDAO instanceDAO = FacadeDAO.getInstance();

  /** Updates the CurrentRequests ListView table in FXML */
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
    //    currentRequests.get
    currentRequests.refresh();
    allServiceRequestList = instanceDAO.getAllServiceRequests();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    menuName = "Meal Request";
    submitButton.setDisable(true);
    System.out.println("Meal Request Submit button disabled");

    //    locationList =
    //        instanceDAO.getAllLocations().stream()
    //            .filter(Location -> Location.getNodeType() == "PATI")
    //            .collect(Collectors.toList());
    locationList = instanceDAO.getALlLocationsByType("PATI");
    mealRequestList = instanceDAO.getAllMealServiceRequests();
    //        instanceDAO.getAllServiceRequests().stream()
    //            .filter(REQ -> REQ.getType() == ServiceRequest.RequestType.MEAL)
    //            .collect(Collectors.toList());
    //    allServiceRequestList = instanceDAO.

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
    //    orderStatusDropDown.setItems(
    //        FXCollections.observableArrayList("IN PROGRESS", "PROCESSING", "COMPLETED"));
    //    orderStatusDropDown.setValue("IN PROGRESS");
    //    currReqLabel.setText("");

    breakfastDrinksList.add(
        "none" + "Water" + "Coffee" + "Tea" + "Apple Juice" + "Orange Juice" + "Cranberry Juice");
    breakfastEntreesList.add("none" + "Belgium Waffle" + "Omlette" + "Pancakes");
    breakfastSidesList.add("none" + "Apple Sauce" + "Blueberry Muffin" + "Fruit Bowl");
    lunchDrinksList.add("none" + "Water" + "Coffee" + "Tea");
    lunchEntreesList.add("none" + "Caesar Salad" + "Cheeseburger" + "Chicken Sandwich");
    lunchSidesList.add("none" + "Corn Bread" + "Fruit Bowl" + "Pretzel");
    dinnerDrinksList.add("none" + "Water" + "Coffee" + "Tea" + "Coca Cola" + "Sprite");
    dinnerEntreesList.add("none" + "Cheese Pizza" + "Chicken Parmigiana" + "Spaghetti & Meatballs");
    dinnerSidesList.add("none" + "Brownie" + "Chocolate Chip Cookie" + "Tiramisu");

    allServiceRequestList = instanceDAO.getAllServiceRequests();

    roomNumberDropDown.setValue(null);
    mealOptionDropDown.setValue(null);

    roomNumberDropDown.setOnAction(event -> validateButton());
    mealOptionDropDown.setOnAction(event -> validateButton());
    //    orderStatusDropDown.setOnAction(event -> validateButton());

    //    updateCurrentMealRequestList();
  }

  @Override
  protected void onSubmitButtonClicked(ActionEvent event) throws SQLException {
    System.out.println("Submit Button Clicked");
    System.out.println("Patient Name: " + enterPatientName.getText().trim());
    System.out.println("Patient ID: " + enterPatientID.getText().trim());
    System.out.println("Room Number: " + roomNumberDropDown.getValue());
    System.out.println("Staff Assigned: " + enterStaffAssigned.getText().trim());
    System.out.println("Meal Option: " + mealOptionDropDown.getValue());
    //    System.out.println("Order Status: " + orderStatusDropDown.getValue());

    String id;

    allServiceRequestList = instanceDAO.getAllServiceRequests();

    if (allServiceRequestList.isEmpty()) {
      System.out.println("Meal is empty");
      id = "REQ0";
    } else {
      List<ServiceRequest> currentList = database.getAllServiceRequests();
      ServiceRequest lastestReq = currentList.get(currentList.size() - 1);
      id = lastestReq.getRequestID();
    }
    // Create new REQID
    int num = 1 + Integer.parseInt(id.substring(id.lastIndexOf("Q") + 1));
    String requestID = "REQ" + num;

    //    ServiceRequest newRequest = new ServiceRequest();

    // Create entities for submission
    String mealServiceOption = mealOptionDropDown.getValue().toString();
    ServiceRequest.RequestStatus status = ServiceRequest.RequestStatus.UNASSIGNED;
    Employee issuer = MenuController.getLoggedInUser();
    Employee handler = null;

    //     Update meal request table to show in use
    Patient patient = instanceDAO.getPatientByID(enterPatientID.getText());

    Location targetLocation = null;
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
            requestID, status, issuer, handler, targetLocation, patient, "1", "2", "3");

    instanceDAO.addMealServiceRequest(temp);
    //    mealRequestList.add(temp);

    //    instanceDAO.addServiceRequestFromList(mealRequestList);
    //    database.addServiceRequest(temp);

    //    updateCurrentMealRequestList();

    enterPatientName.clear();
    enterPatientID.clear();
    enterStaffAssigned.clear();
    roomNumberDropDown.setValue(null);
    mealOptionDropDown.setValue(null);
    submitButton.setDisable(true);
    System.out.println("Meal Request Submit button disabled");
  }

  @Override
  protected void onResetButtonClicked(ActionEvent event) throws IOException {
    System.out.println("Reset Button Clicked");
    submitButton.setDisable(true);
    System.out.println("Meal Request Submit button disabled");
    //    infoLabel.setText("");
    enterPatientName.clear();
    enterPatientID.clear();
    enterStaffAssigned.clear();
    roomNumberDropDown.setValue(null);
    mealOptionDropDown.setValue(null);
  }

  public void enterPatientName(ActionEvent event) {}

  public void enterPatientID(ActionEvent event) {}

  public void enterRoomNumber(ActionEvent event) {}

  public void chooseMealOption(MouseEvent mouseEvent) {}

  public void chooseOrderStatus(MouseEvent mouseEvent) {}

  public void enterStaffAssigned(ActionEvent event) {}

  public void validateButton() {
    if (!enterPatientName.getText().trim().isEmpty()
        && !enterPatientID.getText().trim().isEmpty()
        && !enterStaffAssigned.getText().trim().isEmpty()
        && !(roomNumberDropDown.getSelectionModel().getSelectedItem() == null)
        && !(mealOptionDropDown.getSelectionModel().getSelectedItem() == null)) {
      submitButton.setDisable(false);
      System.out.println("Meal Request Submit button enabled");
    } else {
      submitButton.setDisable(true);
      System.out.println("Meal Request Submit button disabled");
    }
  }

//  public void onMouseClick(MouseEvent mouseEvent) {
//    System.out.println("TESTING MOUSE CLICK");
//    System.out.println(currentRequests.getFocusModel().getFocusedIndex()); // number starting at 0
//    System.out.println(currentRequests.getFocusModel().getFocusedItem()); // string of selected
//    int orderIndex = currentRequests.getFocusModel().getFocusedIndex();
//
//    if (orderIndex > -1) {
//      ServiceRequest temp = mealRequestList.get(orderIndex);
//
//      //    enterPatientName.setText();
//      //    enterPatientID.setText();
//      //    enterStaffAssigned.setText();
//      roomNumberDropDown.setValue(temp.getTargetLocation().getShortName().substring(0, 4));
//      //    mealOptionDropDown.setValue()
//      orderStatusDropDown.setValue(temp.getStatus().toString());
//      submitButton.setDisable(
//          true); // TODO: Eventually set to false once all other fields are retrieved
//    } else {
//      System.out.println("OrderIndex: " + orderIndex);
//    }
//  }

  public void onNavigateToMealRequestList(ActionEvent event) throws IOException {
    try {
      menu.load(toMealServiceRequestListURL);
    } catch (IOException e) {
      System.out.println("Error: Failed to load Meal Service Request List URL");
      e.printStackTrace();
      throw new IOException();
    }
  }
}
