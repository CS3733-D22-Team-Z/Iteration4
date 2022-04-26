package edu.wpi.cs3733.D22.teamZ.controllers;

import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.*;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.*;
import java.util.List;
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

  @FXML public Label patientIDFormHeader;
  @FXML public Label patientNameFormHeader;
  @FXML public Label roomNumberFormHeader;
  @FXML public Label mealTimeFormHeader;
  @FXML public Label drinkChoiceFormHeader;
  @FXML public Label entreeChoiceFormHeader;
  @FXML public Label snackChoiceFormHeader;
  @FXML public Label allergiesFormHeader;
  @FXML private MFXTextField enterPatientName;
  @FXML private MFXTextField enterPatientID;
  @FXML private MFXTextField enterStaffAssigned;
  @FXML private ChoiceBox<String> orderStatusDropDown;
  @FXML private ListView<String> currentRequests;
  @FXML private Label mealRequestIndicator;
  @FXML private ComboBox<String> patientIDDropDown;
  @FXML private ComboBox<String> patientNameDropDown;
  @FXML private ComboBox<String> roomNumberDropDown;
  @FXML private ComboBox<String> mealOptionDropDown;
  @FXML private ComboBox<String> drinkOptionDropDown;
  @FXML private ComboBox<String> entreeOptionDropDown;
  @FXML private ComboBox<String> snackOptionDropDown;

  // Lists. Hi!
  private List<Location> locationList;
  private List<Patient> patientList;
  private List<String> patientIDList = new ArrayList<>();
  private List<String> patientNameList = new ArrayList<>();
  private List<String> roomNumberList = new ArrayList<>();
  private List<MealServiceRequest> mealRequestList = new ArrayList<>();
  private List<ServiceRequest> allServiceRequestList = new ArrayList<>();

  private List<String> drinkOptionList = new ArrayList<>();
  private List<String> entreeOptionList = new ArrayList<>();
  private List<String> snackOptionList = new ArrayList<>();

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

    mealRequestIndicator.setText("");

    validateTime();

    //    locationList =
    //        instanceDAO.getAllLocations().stream()
    //            .filter(Location -> Location.getNodeType() == "PATI")
    //            .collect(Collectors.toList());
    locationList = instanceDAO.getALlLocationsByType("PATI"); // TODO: FIX CAPITAL L IN ALl
    mealRequestList = instanceDAO.getAllMealServiceRequests();
    patientList = instanceDAO.getAllPatients();

    for (Patient patient : patientList) {
      patientIDList.add(patient.getPatientID());
      patientNameList.add(patient.getName());
      roomNumberList.add(
          patient
              .getLocation()
              .getShortName()
              .substring(patient.getLocation().getShortName().length() - 4));

      System.out.println("Patient ID: " + patient.getPatientID());
      System.out.println("Patient Name: " + patient.getName());
      System.out.println(
          "Patient Room: "
              + patient
                  .getLocation()
                  .getShortName()
                  .substring(patient.getLocation().getShortName().length() - 4));
    }

    patientIDDropDown.setItems(FXCollections.observableArrayList(patientIDList));
    patientNameDropDown.setItems(FXCollections.observableArrayList(patientNameList));
    roomNumberDropDown.setItems(FXCollections.observableArrayList(roomNumberList));
    //    patientNameList = instanceDAO.getAllPatients().stream().filter(PAT -> PAT.getPatientID()
    // == );

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
    //    roomNumberDropDown.setItems(FXCollections.observableArrayList(roomList));
    mealOptionDropDown.setItems(FXCollections.observableArrayList("BREAKFAST", "LUNCH", "DINNER"));
    //    orderStatusDropDown.setItems(
    //        FXCollections.observableArrayList("IN PROGRESS", "PROCESSING", "COMPLETED"));
    //    orderStatusDropDown.setValue("IN PROGRESS");
    //    currReqLabel.setText("");

    breakfastDrinksList.addAll(
        Arrays.asList(
            "none", "Water", "Coffee", "Tea", "Apple_Juice", "Orange_Juice", "Cranberry_Juice"));
    breakfastEntreesList.addAll(Arrays.asList("none", "Belgium_Waffle", "Omlette", "Pancakes"));
    breakfastSidesList.addAll(
        Arrays.asList("none", "Apple_Sauce", "Blueberry_Muffin", "Fruit_Bowl"));
    lunchDrinksList.addAll(Arrays.asList("none", "Water", "Coffee", "Tea"));
    lunchEntreesList.addAll(
        Arrays.asList("none", "Caesar_Salad", "Cheeseburger", "Chicken_Sandwich"));
    lunchSidesList.addAll(Arrays.asList("none", "Corn_Bread", "Fruit_Bowl", "Pretzel"));
    dinnerDrinksList.addAll(Arrays.asList("none", "Water", "Coffee", "Tea", "Coca_Cola", "Sprite"));
    dinnerEntreesList.addAll(
        Arrays.asList("none", "Cheese_Pizza", "Chicken_Parmigiana", "Spaghetti_&_Meatballs"));
    dinnerSidesList.addAll(Arrays.asList("none", "Brownie", "Chocolate_Chip_Cookie", "Tiramisu"));

    allServiceRequestList = instanceDAO.getAllServiceRequests();

    patientIDDropDown.setValue(null);
    patientNameDropDown.setValue(null);
    roomNumberDropDown.setValue(null);
    mealOptionDropDown.setValue(null);
    drinkOptionDropDown.setValue(null);
    entreeOptionDropDown.setValue(null);
    snackOptionDropDown.setValue(null);

    //    patientIDDropDown.setOnAction(event -> validateButton());
    //    patientNameDropDown.setOnAction(event -> validateButton());
    //    roomNumberDropDown.setOnAction(event -> validateButton());
    mealOptionDropDown.setOnAction(event -> validateButton());
    drinkOptionDropDown.setOnAction(event -> validateButton());
    entreeOptionDropDown.setOnAction(event -> validateButton());
    snackOptionDropDown.setOnAction(event -> validateButton());

    patientIDDropDown.setOnAction(event -> updatePatientID());
    patientNameDropDown.setOnAction(event -> updatePatientName());
    roomNumberDropDown.setOnAction(event -> updatePatientRoom());

    //    orderStatusDropDown.setOnAction(event -> validateButton());

    //    updateCurrentMealRequestList();

    updateMealOptions(); // update drink, entrée, snack/dessert options based on hour
    mealOptionDropDown.setDisable(true);

    //    updatePatientID();
    //    updatePatientName();
    //    updatePatientRoom();
    initializeHelpGraphic();
  }

  @Override
  protected void onSubmitButtonClicked(ActionEvent event) throws SQLException {
    System.out.println("Submit Button Clicked");
    System.out.println("Patient ID: " + patientIDDropDown.getSelectionModel().getSelectedItem());
    System.out.println("Patient Name: " + patientNameDropDown.getValue());
    System.out.println("Room Number: " + roomNumberDropDown.getValue());
    System.out.println("Meal Type " + mealOptionDropDown.getValue());
    System.out.println("Drink Option: " + drinkOptionDropDown.getValue());

    allServiceRequestList = instanceDAO.getAllServiceRequests();

    UniqueID id = new UniqueID();
    String requestID = id.generateID("MEAL");

    //    ServiceRequest newRequest = new ServiceRequest();

    // Create entities for submission
    ServiceRequest.RequestStatus status = ServiceRequest.RequestStatus.UNASSIGNED;
    Employee issuer = MenuController.getLoggedInUser();
    Employee handler = null; // Service request assigned to null

    Location targetLocation = null;
    for (Location model : locationList) {
      if (model
          .getShortName()
          .substring(model.getShortName().length() - 4)
          .equals(roomNumberDropDown.getValue())) {
        targetLocation = instanceDAO.getLocationByID(model.getNodeID());
      }
    }

    LocalDateTime opened = LocalDateTime.now();
    LocalDateTime closed = null;

    //
    Patient patient = instanceDAO.getPatientByID(patientIDDropDown.getValue());

    String drinkOptionSelected = drinkOptionDropDown.getValue();
    String entreeOptionSelected = entreeOptionDropDown.getValue();
    String snackOptionSelected = snackOptionDropDown.getValue();

    MealServiceRequest temp =
        new MealServiceRequest(
            requestID,
            status,
            issuer,
            handler,
            targetLocation,
            opened,
            closed,
            patient,
            drinkOptionSelected,
            entreeOptionSelected,
            snackOptionSelected);

    try {
      instanceDAO.addMealServiceRequest(temp);
      mealRequestIndicator.setText("Submitted Successfully");
      mealRequestIndicator.setTextFill(GREEN);
      System.out.println("Meal Service Request: Submitted Successfully");
    } catch (Exception e) {
      System.out.println("Meal Service Request: Submission failed");
      mealRequestIndicator.setText("Submission Failed");
      mealRequestIndicator.setTextFill(RED);
      e.printStackTrace();
    }

    //    mealRequestList.add(temp);

    //    instanceDAO.addServiceRequestFromList(mealRequestList);
    //    database.addServiceRequest(temp);

    //    updateCurrentMealRequestList();

    patientIDDropDown.setValue(null);
    patientNameDropDown.setValue(null);
    roomNumberDropDown.setValue(null);
    drinkOptionDropDown.setValue(null);
    entreeOptionDropDown.setValue(null);
    snackOptionDropDown.setValue(null);
    submitButton.setDisable(true);
    System.out.println("Meal Request Submit button disabled");
  }

  @Override
  protected void onResetButtonClicked(ActionEvent event) throws IOException {
    System.out.println("Reset Button Clicked");
    submitButton.setDisable(true);
    System.out.println("Meal Request Submit button disabled");
    mealRequestIndicator.setText("Form Reset");
    mealRequestIndicator.setStyle("-fx-text-fill: #7B7B7B");
    patientIDDropDown.setValue(null);
    patientNameDropDown.setValue(null);
    roomNumberDropDown.setValue(null);
    drinkOptionDropDown.setValue(null);
    entreeOptionDropDown.setValue(null);
    snackOptionDropDown.setValue(null);
    validateTime();
  }

  @Override
  protected void highlightRequirements(boolean visible) {
    if (visible) {
      roomNumberFormHeader.getStyleClass().clear();
      roomNumberFormHeader.getStyleClass().add("form-header-help");
      patientIDFormHeader.getStyleClass().clear();
      patientIDFormHeader.getStyleClass().add("form-header-help");
      patientNameFormHeader.getStyleClass().clear();
      patientNameFormHeader.getStyleClass().add("form-header-help");
      roomNumberFormHeader.getStyleClass().clear();
      roomNumberFormHeader.getStyleClass().add("form-header-help");
      mealTimeFormHeader.getStyleClass().clear();
      mealTimeFormHeader.getStyleClass().add("form-header-help");
      drinkChoiceFormHeader.getStyleClass().clear();
      drinkChoiceFormHeader.getStyleClass().add("form-header-help");
      entreeChoiceFormHeader.getStyleClass().clear();
      entreeChoiceFormHeader.getStyleClass().add("form-header-help");
      snackChoiceFormHeader.getStyleClass().clear();
      snackChoiceFormHeader.getStyleClass().add("form-header-help");
      allergiesFormHeader.getStyleClass().clear();
      allergiesFormHeader.getStyleClass().add("form-header-help");
    } else {
      roomNumberFormHeader.getStyleClass().clear();
      roomNumberFormHeader.getStyleClass().add("form-header");
      patientIDFormHeader.getStyleClass().clear();
      patientIDFormHeader.getStyleClass().add("form-header");
      patientNameFormHeader.getStyleClass().clear();
      patientNameFormHeader.getStyleClass().add("form-header");
      roomNumberFormHeader.getStyleClass().clear();
      roomNumberFormHeader.getStyleClass().add("form-header");
      mealTimeFormHeader.getStyleClass().clear();
      mealTimeFormHeader.getStyleClass().add("form-header");
      drinkChoiceFormHeader.getStyleClass().clear();
      drinkChoiceFormHeader.getStyleClass().add("form-header");
      entreeChoiceFormHeader.getStyleClass().clear();
      entreeChoiceFormHeader.getStyleClass().add("form-header");
      snackChoiceFormHeader.getStyleClass().clear();
      snackChoiceFormHeader.getStyleClass().add("form-header");
      allergiesFormHeader.getStyleClass().clear();
      allergiesFormHeader.getStyleClass().add("form-header");
    }
  }

  public void enterPatientName(ActionEvent event) {}

  public void enterPatientID(ActionEvent event) {}

  public void enterRoomNumber(ActionEvent event) {}

  public void chooseMealOption(MouseEvent mouseEvent) {}

  public void chooseOrderStatus(MouseEvent mouseEvent) {}

  public void enterStaffAssigned(ActionEvent event) {}

  public void validateButton() {
    if (!(patientIDDropDown.getSelectionModel().getSelectedItem() == null)
        && !(patientNameDropDown.getSelectionModel().getSelectedItem() == null)
        && !(roomNumberDropDown.getSelectionModel().getSelectedItem() == null)
        && !(drinkOptionDropDown.getSelectionModel().getSelectedItem() == null)
        && !(entreeOptionDropDown.getSelectionModel().getSelectedItem() == null)
        && !(snackOptionDropDown.getSelectionModel().getSelectedItem() == null)) {
      submitButton.setDisable(false);
      System.out.println("Meal Request Submit button enabled");
    } else {
      submitButton.setDisable(true);
      System.out.println("Meal Request Submit button disabled");
    }

    //    validateTime();
  }

  /** Base meal options on the time of day by hour. */
  public void validateTime() {
    boolean isMorning = false;
    boolean isDay = false;
    boolean isNight = false;

    // Time base on time zone
    //    TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
    //    Calendar calendar = new GregorianCalendar(timeZone);
    //    int localHour = Calendar.HOUR_OF_DAY;
    //    System.out.println(localHour);

    // Local Time
    LocalDateTime date = LocalDateTime.now();
    int localHour = date.getHour();
    System.out.println("Local Hour: " + localHour);

    if (localHour >= 0 && localHour <= 11) {
      isMorning = true;
      isDay = false;
      isNight = false;
      mealOptionDropDown.setValue("BREAKFAST");
      drinkOptionList = breakfastDrinksList;
      entreeOptionList = breakfastEntreesList;
      snackOptionList = breakfastSidesList;
    } else if (localHour > 11 && localHour < 5) {
      isMorning = false;
      isDay = true;
      isNight = false;
      mealOptionDropDown.setValue("LUNCH");
      drinkOptionList = lunchDrinksList;
      entreeOptionList = lunchEntreesList;
      snackOptionList = lunchSidesList;
    } else {
      isMorning = false;
      isDay = false;
      isNight = true;
      mealOptionDropDown.setValue("DINNER");
      drinkOptionList = dinnerDrinksList;
      entreeOptionList = dinnerEntreesList;
      snackOptionList = dinnerSidesList;
    }

    drinkOptionDropDown.setItems(FXCollections.observableArrayList(drinkOptionList));
    entreeOptionDropDown.setItems(FXCollections.observableArrayList(entreeOptionList));
    snackOptionDropDown.setItems(FXCollections.observableArrayList(snackOptionList));
  }

  /** */
  public void updateMealOptions() {
    validateTime();
  }

  /** */
  public void updatePatientID() {
    System.out.println("Update 1: Patient ID ComboBox Selected");

    if (patientIDDropDown.getValue() != null) {
      System.out.println("Patient ID Value: " + patientIDDropDown.getValue());
      System.out.println("Patient Name: " + patientNameList.indexOf(patientIDDropDown.getValue()));
      System.out.println(
          "Patient New Name: "
              + patientNameList.get(patientIDList.indexOf(patientIDDropDown.getValue())));
      patientNameDropDown.setValue(
          patientNameList.get(patientIDList.indexOf(patientIDDropDown.getValue())));
      roomNumberDropDown.setValue(
          roomNumberList.get(patientIDList.indexOf(patientIDDropDown.getValue())));
    }
  }

  /** */
  public void updatePatientName() {
    System.out.println("Update 2: Patient Name ComboBox Selected");
    if (patientNameDropDown.getValue() != null) {
      patientIDDropDown.setValue(
          patientIDList.get(patientNameList.indexOf(patientNameDropDown.getValue())));
      roomNumberDropDown.setValue(
          roomNumberList.get(patientNameList.indexOf(patientNameDropDown.getValue())));
    }
  }

  /** */
  public void updatePatientRoom() {
    System.out.println("Update 3: Patient Room ComboBox Selected");
    if (roomNumberDropDown.getValue() != null) {
      patientIDDropDown.setValue(
          patientIDList.get(roomNumberList.indexOf(roomNumberDropDown.getValue())));
      patientNameDropDown.setValue(
          patientNameList.get(roomNumberList.indexOf(roomNumberDropDown.getValue())));
    }
  }

  //  public void onMouseClick(MouseEvent mouseEvent) {
  //    System.out.println("TESTING MOUSE CLICK");
  //    System.out.println(currentRequests.getFocusModel().getFocusedIndex()); // number starting at
  // 0
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

  /**
   * Navigate to table of Meal Service Requests
   *
   * @param event
   * @throws IOException
   */
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
