package edu.wpi.cs3733.D22.teamZ.controllers;

import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.*;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
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
  @FXML private RadioButton dairyChoice;
  @FXML private RadioButton eggChoice;
  @FXML private RadioButton peanutChoice;
  @FXML private RadioButton treenutChoice;
  @FXML private RadioButton soyChoice;
  @FXML private RadioButton wheatChoice;
  @FXML private RadioButton fishChoice;
  @FXML private RadioButton shellfishChoice;
  @FXML final ToggleGroup radioGroup = new ToggleGroup();

  // Lists
  private List<Location> locationList;
  private List<Patient> patientList;
  private List<String> patientIDList = new ArrayList<>(); // Set or Map may be better
  private List<String> patientNameList = new ArrayList<>(); // Set or Map may be better
  private List<String> roomNumberList = new ArrayList<>(); // Set or Map may be better
  private Set<String> patientIDSet = new HashSet<>();
  private Set<String> patientNameSet = new HashSet<>();
  private Set<String> roomNumberSet = new HashSet<>();
  private List<MealServiceRequest> mealRequestList = new ArrayList<>();
  private List<ServiceRequest> allServiceRequestList = new ArrayList<>();
  private List<String> patientAllergensList = new ArrayList<>();

  private List<String> drinkOptionList = new ArrayList<>();
  private List<String> entreeOptionList = new ArrayList<>();
  private List<String> snackOptionList = new ArrayList<>();

  private List<String> breakfastDrinksList = new ArrayList<>();
  private List<String> breakfastEntreesList = new ArrayList<>();
  private List<String> breakfastSnackList = new ArrayList<>();
  private List<String> lunchDrinksList = new ArrayList<>();
  private List<String> lunchEntreesList = new ArrayList<>();
  private List<String> lunchSnackList = new ArrayList<>();
  private List<String> dinnerDrinksList = new ArrayList<>();
  private List<String> dinnerEntreesList = new ArrayList<>();
  private List<String> dinnerSnackList = new ArrayList<>();

  private ObservableList<String> currReq = FXCollections.observableList(new ArrayList<>());

  private final String toMealServiceRequestListURL =
      "edu/wpi/cs3733/D22/teamZ/views/MealServiceRequestList.fxml";
  private final String toMealServiceFoodOptionsURL =
      "edu/wpi/cs3733/D22/teamZ/views/MealServiceFoodOptions.fxml";

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

    // Remove duplicate room numbers
    // If two or more patients share a room, the first patient in the list will be assigned.
    //    for ()

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
            "none", "Water", "Coffee", "Tea", "Apple Juice", "Orange Juice", "Cranberry Juice"));
    breakfastEntreesList.addAll(Arrays.asList("none", "Belgium Waffle", "Omelette", "Pancakes"));
    breakfastSnackList.addAll(
        Arrays.asList("none", "Apple Sauce", "Blueberry Muffin", "Fruit Bowl"));
    lunchDrinksList.addAll(Arrays.asList("none", "Water", "Coffee", "Tea"));
    lunchEntreesList.addAll(
        Arrays.asList("none", "Caesar Salad", "Cheeseburger", "Chicken Sandwich"));
    lunchSnackList.addAll(Arrays.asList("none", "Corn Bread", "Fruit Bowl", "Pretzel"));
    dinnerDrinksList.addAll(Arrays.asList("none", "Water", "Coffee", "Tea", "Coca Cola", "Sprite"));
    dinnerEntreesList.addAll(
        Arrays.asList("none", "Cheese Pizza", "Chicken Parmigiana", "Spaghetti & Meatballs"));
    dinnerSnackList.addAll(Arrays.asList("none", "Brownie", "Chocolate hip Cookie", "Tiramisu"));

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

    //
    patientIDDropDown.setOnAction(event -> updatePatientID());
    patientNameDropDown.setOnAction(event -> updatePatientName());
    roomNumberDropDown.setOnAction(event -> updatePatientRoom());

    //
    dairyChoice.setOnAction(event -> updateAllergens());
    eggChoice.setOnAction(event -> updateAllergens());
    peanutChoice.setOnAction(event -> updateAllergens());
    treenutChoice.setOnAction(event -> updateAllergens());
    soyChoice.setOnAction(event -> updateAllergens());
    wheatChoice.setOnAction(event -> updateAllergens());
    fishChoice.setOnAction(event -> updateAllergens());
    shellfishChoice.setOnAction(event -> updateAllergens());

    //    radioGroup
    //        .selectedToggleProperty()
    //        .addListener(
    //            (observable, oldValue, newValue) -> {
    //              RadioButton selectedButton = (RadioButton) newValue;
    //              updateAllergens();
    //              //              mode = selectedButton.getText();
    //              //
    // changeToFloor(changeFloor.getSelectionModel().getSelectedItem());
    //            });

    //    orderStatusDropDown.setOnAction(event -> validateButton());

    //    updateCurrentMealRequestList();

    updateMealOptions(); // update drink, entrée, snack/dessert options based on hour
    mealOptionDropDown.setDisable(true);

    //    updatePatientID();
    //    updatePatientName();
    //    updatePatientRoom();
    initializeHelpGraphic();
  }

  /**
   * Submit meal service request to DAO
   *
   * @param event
   * @throws SQLException
   */
  @Override
  protected void onSubmitButtonClicked(ActionEvent event) throws SQLException {
    System.out.println("Submit Button Clicked");
    System.out.println("Patient ID: " + patientIDDropDown.getSelectionModel().getSelectedItem());
    System.out.println("Patient Name: " + patientNameDropDown.getValue());
    System.out.println("Room Number: " + roomNumberDropDown.getValue());
    System.out.println("Meal Type " + mealOptionDropDown.getValue());
    System.out.println("Drink Option: " + drinkOptionDropDown.getValue());
    System.out.println("Patient Allergens: " + patientAllergensList);

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
    String patientAllergens = patientAllergensList.toString();

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
            snackOptionSelected,
            patientAllergens);

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
    dairyChoice.setSelected(false);
    eggChoice.setSelected(false);
    dairyChoice.setSelected(false);
    eggChoice.setSelected(false);
    peanutChoice.setSelected(false);
    treenutChoice.setSelected(false);
    soyChoice.setSelected(false);
    wheatChoice.setSelected(false);
    fishChoice.setSelected(false);
    shellfishChoice.setSelected(false);
    System.out.println("Clear fields");
    submitButton.setDisable(true);
    System.out.println("Meal Request Submit button disabled");
  }

  /**
   * Reset fields to initial conditions.
   *
   * @param event
   * @throws IOException
   */
  @Override
  protected void onResetButtonClicked(ActionEvent event) throws IOException {
    System.out.println("Reset Button Clicked");
    submitButton.setDisable(true);
    System.out.println("Meal Request Submit button disabled");
    mealRequestIndicator.setText("Form Reset");
    mealRequestIndicator.setStyle("-fx-text-fill: #7B7B7B");
    // Clear dropdown values
    patientIDDropDown.setValue(null);
    patientNameDropDown.setValue(null);
    roomNumberDropDown.setValue(null);
    drinkOptionDropDown.setValue(null);
    entreeOptionDropDown.setValue(null);
    snackOptionDropDown.setValue(null);
    // Reset radio buttons
    dairyChoice.setSelected(false);
    eggChoice.setSelected(false);
    peanutChoice.setSelected(false);
    treenutChoice.setSelected(false);
    soyChoice.setSelected(false);
    wheatChoice.setSelected(false);
    fishChoice.setSelected(false);
    shellfishChoice.setSelected(false);
    // Remove allergens from meal request
    patientAllergensList.clear();
    System.out.println("Patient Allergen List Emptied");

    validateTime();
  }

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

  /**
   * Navigate to add/edit/remove food options. Admin only.
   *
   * @param event
   */
  public void onNavigateToMealOptions(ActionEvent event) throws IOException {
    try {
      menu.load(toMealServiceFoodOptionsURL);
    } catch (IOException e) {
      System.out.println("Error: Failed to load Meal Service Food Options URL");
      e.printStackTrace();
      throw new IOException();
    }
  }

  /**
   * Show tooltips for different headers
   *
   * @param visible
   */
  @Override
  protected void highlightRequirements(boolean visible) {
    if (visible) {
      roomNumberFormHeader.getStyleClass().clear();
      roomNumberFormHeader.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(roomNumberFormHeader, "Enter room number that\nmeal is delivered to");

      patientIDFormHeader.getStyleClass().clear();
      patientIDFormHeader.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(patientIDFormHeader, "Enter ID of patient that\nmeal is requested for");

      patientNameFormHeader.getStyleClass().clear();
      patientNameFormHeader.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(
          patientNameFormHeader, "Enter name of patient that\nmeal is requested for");

      mealTimeFormHeader.getStyleClass().clear();
      mealTimeFormHeader.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(mealTimeFormHeader, "Select meal time that\nmeal is delivered on");

      drinkChoiceFormHeader.getStyleClass().clear();
      drinkChoiceFormHeader.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(drinkChoiceFormHeader, "Select drink for meal delivery");

      entreeChoiceFormHeader.getStyleClass().clear();
      entreeChoiceFormHeader.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(entreeChoiceFormHeader, "Select entree for meal delivery");

      snackChoiceFormHeader.getStyleClass().clear();
      snackChoiceFormHeader.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(snackChoiceFormHeader, "Select snack for meal delivery");

      allergiesFormHeader.getStyleClass().clear();
      allergiesFormHeader.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(allergiesFormHeader, "Select all allergies\nthat patient has");
    } else {
      roomNumberFormHeader.getStyleClass().clear();
      roomNumberFormHeader.getStyleClass().add("form-header");
      roomNumberFormHeader.setTooltip(null);

      patientIDFormHeader.getStyleClass().clear();
      patientIDFormHeader.getStyleClass().add("form-header");
      patientIDFormHeader.setTooltip(null);

      patientNameFormHeader.getStyleClass().clear();
      patientNameFormHeader.getStyleClass().add("form-header");
      patientNameFormHeader.setTooltip(null);

      mealTimeFormHeader.getStyleClass().clear();
      mealTimeFormHeader.getStyleClass().add("form-header");
      mealTimeFormHeader.setTooltip(null);

      drinkChoiceFormHeader.getStyleClass().clear();
      drinkChoiceFormHeader.getStyleClass().add("form-header");
      drinkChoiceFormHeader.setTooltip(null);

      entreeChoiceFormHeader.getStyleClass().clear();
      entreeChoiceFormHeader.getStyleClass().add("form-header");
      entreeChoiceFormHeader.setTooltip(null);

      snackChoiceFormHeader.getStyleClass().clear();
      snackChoiceFormHeader.getStyleClass().add("form-header");
      snackChoiceFormHeader.setTooltip(null);

      allergiesFormHeader.getStyleClass().clear();
      allergiesFormHeader.getStyleClass().add("form-header");
      allergiesFormHeader.setTooltip(null);
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
      snackOptionList = breakfastSnackList;
    } else if (localHour > 11 && localHour < 5) {
      isMorning = false;
      isDay = true;
      isNight = false;
      mealOptionDropDown.setValue("LUNCH");
      drinkOptionList = lunchDrinksList;
      entreeOptionList = lunchEntreesList;
      snackOptionList = lunchSnackList;
    } else {
      isMorning = false;
      isDay = false;
      isNight = true;
      mealOptionDropDown.setValue("DINNER");
      drinkOptionList = dinnerDrinksList;
      entreeOptionList = dinnerEntreesList;
      snackOptionList = dinnerSnackList;
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

      String tempName = instanceDAO.getPatientByID(patientIDDropDown.getValue()).getName();
      patientNameDropDown.setValue(tempName);
      System.out.println("TempName: " + tempName);

      String tempShortName =
          instanceDAO.getPatientByID(patientIDDropDown.getValue()).getLocation().getShortName();

      String tempRoomNumber = tempShortName.substring(tempShortName.length() - 4);
      roomNumberDropDown.setValue(tempRoomNumber);
      System.out.println("tempRoomNumber: " + tempRoomNumber);

      //      patientNameDropDown.setValue(
      //          patientNameList.get(patientIDList.indexOf(patientIDDropDown.getValue())));
      //      roomNumberDropDown.setValue(
      //          roomNumberList.get(patientIDList.indexOf(patientIDDropDown.getValue())));
    }
  }

  /** */
  public void updatePatientName() {
    System.out.println("Update 2: Patient Name ComboBox Selected");
    if (patientNameDropDown.getValue() != null) {
      List<Patient> l =
          patientList.stream()
              .filter(s -> patientNameDropDown.getValue().equals(s))
              .collect(Collectors.toList());

      String tempShortName = l.get(0).getLocation().getShortName();

      String tempPatientName = l.get(0).getName();
      String tempPatientID = l.get(0).getPatientID();
      String tempRoomNumber =
          l.get(0).getLocation().getShortName().substring(tempShortName.length() - 4);

      System.out.println("Name Chosen: " + patientIDDropDown.getValue());
      System.out.println("Temp Name: " + tempPatientName);
      patientIDDropDown.setValue(tempPatientID);
      System.out.println("tempPatientID: " + tempPatientID);
      roomNumberDropDown.setValue(tempRoomNumber);
      System.out.println("tempRoomNumber: " + tempRoomNumber);

      //      String tempPatientID =
      //          instanceDAO
      //
      // .getPatientByID(instanceDAO.getAllPatients().contains(instanceDAO.getAllPatients().get().getName()))
      //      List<Patient> pat = instanceDAO.getAllPatients();

      //      Stream peters = userList.stream().filter(p -> p.user.name.equals("Peter"))

      //      String tempRoomNumber =
      //          instanceDAO
      //              .getPatientByID(patientIDDropDown.getValue())
      //              .getLocation()
      //              .getShortName()
      //              .substring(
      //                  instanceDAO
      //                      .getPatientByID(patientIDDropDown.getValue())
      //                      .getLocation()
      //                      .getShortName()
      //                      .length()
      //                      - 4);
      //      roomNumberDropDown.setValue(tempRoomNumber);
      //      System.out.println("tempRoomNumber: " + tempRoomNumber);

      //      patientIDDropDown.setValue(
      //          patientIDList.get(patientNameList.indexOf(patientNameDropDown.getValue())));
      //      roomNumberDropDown.setValue(
      //          roomNumberList.get(patientNameList.indexOf(patientNameDropDown.getValue())));
    }
  }

  /** */
  public void updatePatientRoom() {
    System.out.println("Update 3: Patient Room ComboBox Selected");
    if (roomNumberDropDown.getValue() != null) {

      //      patientNameList.indexOf()

      patientIDDropDown.setValue(
          patientIDList.get(roomNumberList.indexOf(roomNumberDropDown.getValue())));
      patientNameDropDown.setValue(
          patientNameList.get(roomNumberList.indexOf(roomNumberDropDown.getValue())));
    }
  }

  public void updateAllergens() {
    System.out.println("Updating allergens...");

    boolean stateDairy;
    boolean stateEgg;
    boolean statePeanut;
    boolean statetreenut;
    boolean stateSoy;
    boolean stateWheat;
    boolean stateFish;
    boolean stateShellfish;

    if (dairyChoice.isSelected()
        || eggChoice.isSelected()
        || peanutChoice.isSelected()
        || treenutChoice.isSelected()
        || soyChoice.isSelected()
        || wheatChoice.isSelected()
        || fishChoice.isSelected()
        || shellfishChoice.isSelected()) {
      // Remove none if exists
      if (patientAllergensList.contains("none")) {
        patientAllergensList.remove("none");
        System.out.println("Removed \"none\" from Patient Allergen list");
      }
      updateDairyChoice();
      updateEggChoice();
      updatePeanutChoice();
      updateTreenut();
      updateSoyChoice();
      updateWheatChoice();
      updateFishChoice();
      updateShellfish();
    } else {
      // Re-add none if not present
      if (!patientAllergensList.contains("none")) {
        patientAllergensList.add("none");
      }
    }
  }

  /** */
  private void updateDairyChoice() {
    // Add/Remove Dairy from patient list of allergens
    if (dairyChoice.isSelected()) {
      System.out.println("Dairy selected");
      if (!patientAllergensList.contains("Dairy")) {
        patientAllergensList.add("Dairy");
      }
    } else {
      if (patientAllergensList.contains("Dairy")) {
        patientAllergensList.remove("Dairy");
      }
    }
  }

  /** */
  private void updateEggChoice() {
    // Add/Remove Egg from patient list of allergens
    if (eggChoice.isSelected()) {
      System.out.println("Egg selected");
      if (!patientAllergensList.contains("Egg")) {
        patientAllergensList.add("Egg");
      }
    } else {
      if (patientAllergensList.contains("Egg")) {
        patientAllergensList.remove("Egg");
      }
    }
  }

  /** */
  private void updatePeanutChoice() {
    // Add/Remove Peanut from patient list of allergens
    if (peanutChoice.isSelected()) {
      System.out.println("Peanut selected");
      if (!patientAllergensList.contains("Peanut")) {
        patientAllergensList.add("Peanut");
      }
    } else {
      if (patientAllergensList.contains("Peanut")) {
        patientAllergensList.remove("Peanut");
      }
    }
  }

  /** */
  private void updateTreenut() {
    // Add/Remove Tree Nut from patient list of allergens
    if (treenutChoice.isSelected()) {
      System.out.println("Tree Nut selected");
      if (!patientAllergensList.contains("Tree_Nut")) {
        patientAllergensList.add("Tree_Nut");
      }
    } else {
      if (patientAllergensList.contains("Tree_Nut")) {
        patientAllergensList.remove("Tree_Nut");
      }
    }
  }

  /** */
  private void updateSoyChoice() {
    // Add/Remove Soy from patient list of allergens
    if (soyChoice.isSelected()) {
      System.out.println("Soy selected");
      if (!patientAllergensList.contains("Soy")) {
        patientAllergensList.add("Soy");
      }
    } else {
      if (patientAllergensList.contains("Soy")) {
        patientAllergensList.remove("Soy");
      }
    }
  }

  /** */
  private void updateWheatChoice() {
    // Add/Remove Wheat from patient list of allergens
    if (wheatChoice.isSelected()) {
      System.out.println("Wheat selected");
      if (!patientAllergensList.contains("Wheat")) {
        patientAllergensList.add("Wheat");
      }
    } else {
      if (patientAllergensList.contains("Wheat")) {
        patientAllergensList.remove("Wheat");
      }
    }
  }

  /** */
  private void updateFishChoice() {
    // Add/Remove Fish from patient list of allergens
    if (fishChoice.isSelected()) {
      System.out.println("Fish selected");
      if (!patientAllergensList.contains("Fish")) {
        patientAllergensList.add("Fish");
      }
    } else {
      if (patientAllergensList.contains("Fish")) {
        patientAllergensList.remove("Fish");
      }
    }
  }

  /** */
  private void updateShellfish() {
    // Add/Remove Shellfish from patient list of allergens
    if (shellfishChoice.isSelected()) {
      System.out.println("Shellfish selected");
      if (!patientAllergensList.contains("Shellfish")) {
        patientAllergensList.add("Shellfish");
      }
    } else {
      if (patientAllergensList.contains("Shellfish")) {
        patientAllergensList.remove("Shellfish");
      }
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
}
