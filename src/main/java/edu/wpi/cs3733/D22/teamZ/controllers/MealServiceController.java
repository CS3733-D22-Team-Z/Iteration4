package edu.wpi.cs3733.D22.teamZ.controllers;

import static javafx.scene.paint.Color.*;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.*;
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

public class MealServiceController extends ServiceRequestController {

  @FXML public Label patientIDFormHeader;
  @FXML public Label patientNameFormHeader;
  @FXML public Label roomNumberFormHeader;
  @FXML public Label mealTimeFormHeader;
  @FXML public Label drinkChoiceFormHeader;
  @FXML public Label entreeChoiceFormHeader;
  @FXML public Label snackChoiceFormHeader;
  @FXML public Label allergiesFormHeader;
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

  List<MealItem> allMenuItems = new ArrayList<>();

  List<MealItem> breakfastItemsList = new ArrayList<>();
  List<MealItem> lunchItemsList = new ArrayList<>();
  List<MealItem> dinnerItemsList = new ArrayList<>();

  List<MealItem> drinkItemsList = new ArrayList<>();
  List<MealItem> entreeItemsList = new ArrayList<>();
  List<MealItem> snackItemsList = new ArrayList<>();

  private ObservableList<String> currReq = FXCollections.observableList(new ArrayList<>());

  private final String toMealServiceRequestListURL =
      "edu/wpi/cs3733/D22/teamZ/views/MealServiceRequestList.fxml";

  FacadeDAO instanceDAO = FacadeDAO.getInstance();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    menuName = "Meal Request";
    submitButton.setDisable(true);
    // System.out.println("Meal Request Submit button disabled");

    mealOptionDropDown.setDisable(true); // leave for time dependant -temp
    patientNameDropDown.setDisable(true); // dropdown for only unique names
    // patientNameFormHeader.setText("Patient Name");
    roomNumberDropDown.setDisable(true); // what about more than one patient per room? -disabled
    // roomNumberFormHeader.setText("Room Number");

    mealRequestIndicator.setText(""); // Clear base error message label

    locationList = instanceDAO.getAllLocationsByType("PATI");
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

      //      System.out.println("Patient ID: " + patient.getPatientID());
      //      System.out.println("Patient Name: " + patient.getName());
      //      System.out.println(
      //          "Patient Room: "
      //              + patient
      //                  .getLocation()
      //                  .getShortName()
      //                  .substring(patient.getLocation().getShortName().length() - 4));
    }

    // TODO: Remove duplicate room numbers
    // If two or more patients share a room, the first patient in the list will be assigned.
    //

    patientIDDropDown.setItems(FXCollections.observableArrayList(patientIDList));
    patientNameDropDown.setItems(FXCollections.observableArrayList(patientNameList));
    roomNumberDropDown.setItems(FXCollections.observableArrayList(roomNumberList));

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
      // System.out.println(temp);
    }

    List<String> roomList = new ArrayList<>();
    for (Location model : locationList) {
      roomList.add(model.getShortName().substring(model.getShortName().length() - 4));
    }
    //    roomNumberDropDown.setItems(FXCollections.observableArrayList(roomList));
    mealOptionDropDown.setItems(
        FXCollections.observableList(List.of("Breakfast", "Lunch", "Dinner")));
    //    orderStatusDropDown.setItems(
    //        FXCollections.observableArrayList("IN PROGRESS", "PROCESSING", "COMPLETED"));
    //    orderStatusDropDown.setValue("IN PROGRESS");
    //    currReqLabel.setText("");

    validateTime(); // Check the time and update meal options based on the hour

    loadFoodItems(); // add food items to time-category lists

    // Get all service requests.
    // Used later for correct ID numbering in order. TODO: Notice: Numbers randomized
    allServiceRequestList = instanceDAO.getAllServiceRequests();

    // Set initial fields cleared, not empty
    // Used to avoid errors from validateButton
    patientIDDropDown.setValue(null);
    patientNameDropDown.setValue(null);
    roomNumberDropDown.setValue(null);
    drinkOptionDropDown.setValue(null);
    entreeOptionDropDown.setValue(null);
    snackOptionDropDown.setValue(null);

    // Handle a food item chosen
    drinkOptionDropDown.setOnAction(event -> validateButton());
    entreeOptionDropDown.setOnAction(event -> validateButton());
    snackOptionDropDown.setOnAction(event -> validateButton());

    // Handle event when a drop-down selection changes
    patientIDDropDown.setOnAction(event -> updatePatientID());

    // Handle event when an allergy choice is changed
    dairyChoice.setOnAction(
        evt -> {
          if (dairyChoice.isSelected()) {
            patientAllergensList.add("Dairy");
          } else {
            patientAllergensList.remove("Dairy");
          }
          refreshOptionDropDowns();
        });
    wheatChoice.setOnAction(
        evt -> {
          if (wheatChoice.isSelected()) {
            patientAllergensList.add("Wheat");
          } else {
            patientAllergensList.remove("Wheat");
          }
          refreshOptionDropDowns();
        });
    eggChoice.setOnAction(
        evt -> {
          if (eggChoice.isSelected()) {
            patientAllergensList.add("Egg");
          } else {
            patientAllergensList.remove("Egg");
          }
          refreshOptionDropDowns();
        });
    peanutChoice.setOnAction(
        evt -> {
          if (peanutChoice.isSelected()) {
            patientAllergensList.add("Peanut");
          } else {
            patientAllergensList.remove("Peanut");
          }
          refreshOptionDropDowns();
        });
    treenutChoice.setOnAction(
        evt -> {
          if (treenutChoice.isSelected()) {
            patientAllergensList.add("Tree Nut");
          } else {
            patientAllergensList.remove("Tree Nut");
          }
          refreshOptionDropDowns();
        });
    soyChoice.setOnAction(
        evt -> {
          if (soyChoice.isSelected()) {
            patientAllergensList.add("Soy");

          } else {
            patientAllergensList.remove("Soy");
          }
          refreshOptionDropDowns();
        });
    fishChoice.setOnAction(
        evt -> {
          if (fishChoice.isSelected()) {
            patientAllergensList.add("Fish");
          } else {
            patientAllergensList.remove("Fish");
          }
          refreshOptionDropDowns();
        });
    shellfishChoice.setOnAction(
        evt -> {
          if (shellfishChoice.isSelected()) {
            patientAllergensList.add("Shellfish");
          } else {
            patientAllergensList.remove("Shellfish");
          }
          refreshOptionDropDowns();
        });

    refreshOptionDropDowns();
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
    // System.out.println("Submit Button Clicked");
    // System.out.println("Patient ID: " + patientIDDropDown.getSelectionModel().getSelectedItem());
    // System.out.println("Patient Name: " + patientNameDropDown.getValue());
    // System.out.println("Room Number: " + roomNumberDropDown.getValue());
    // System.out.println("Meal Type " + mealOptionDropDown.getValue());
    // System.out.println("Drink Option: " + drinkOptionDropDown.getValue());
    // System.out.println("Patient Allergens: " + patientAllergensList);

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
      // System.out.println("Meal Service Request: Submitted Successfully");
    } catch (Exception e) {
      // System.out.println("Meal Service Request: Submission failed");
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
    // System.out.println("Meal Request Submit button disabled");
  }

  /**
   * Reset fields to initial conditions.
   *
   * @param event
   * @throws IOException
   */
  @Override
  protected void onResetButtonClicked(ActionEvent event) throws IOException {
    // System.out.println("Reset Button Clicked");
    submitButton.setDisable(true);
    // System.out.println("Meal Request Submit button disabled");
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
    // System.out.println("Patient Allergen List Emptied");

    loadFoodItems(); // reset food lists
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
      // System.out.println("Error: Failed to load Meal Service Request List URL");
      e.printStackTrace();
      throw new IOException();
    }
  }

  /**
   * Show tooltips for different headers
   *
   * @param visible If the help button has been pressed.
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

  /**
   * Add pre-items to appropriate lists for use in the meal service controller. Call once. TODO: Add
   * MealItems to a CSV (low)
   */
  protected void loadFoodItems() {
    List<String> allDayTimeList = new ArrayList<>();
    allDayTimeList.add("Breakfast");
    allDayTimeList.add("Lunch");
    allDayTimeList.add("Dinner");
    List<String> breakfastTimeList = new ArrayList<>();
    breakfastTimeList.add("Breakfast");
    List<String> lunchTimeList = new ArrayList<>();
    lunchTimeList.add("Lunch");
    List<String> dinnerTimeList = new ArrayList<>();
    dinnerTimeList.add("Dinner");
    List<String> noneAllergyList = new ArrayList<>();

    List<String> dairyEggWheatAllergenList = new ArrayList<>();
    dairyEggWheatAllergenList.add("Dairy");
    dairyEggWheatAllergenList.add("Egg");
    dairyEggWheatAllergenList.add("Wheat");
    List<String> dairyEggAllergenList = new ArrayList<>();
    dairyEggAllergenList.add("Dairy");
    dairyEggAllergenList.add("Egg");
    List<String> dairyEggPeanutTreenutWheatAllergenList = new ArrayList<>();
    dairyEggPeanutTreenutWheatAllergenList.add("Dairy");
    dairyEggPeanutTreenutWheatAllergenList.add("Egg");
    dairyEggPeanutTreenutWheatAllergenList.add("Peanut");
    dairyEggPeanutTreenutWheatAllergenList.add("Tree Nut");
    dairyEggPeanutTreenutWheatAllergenList.add("Wheat");
    List<String> dairySoyWheatAllergenList = new ArrayList<>();
    dairySoyWheatAllergenList.add("Dairy");
    dairySoyWheatAllergenList.add("Soy");
    dairySoyWheatAllergenList.add("Wheat");
    List<String> soyWheatAllergenList = new ArrayList<>();
    soyWheatAllergenList.add("Soy");
    soyWheatAllergenList.add("Wheat");
    List<String> wheatAllergenList = new ArrayList<>();
    wheatAllergenList.add("Wheat");
    List<String> dairyWheatAllergenList = new ArrayList<>();
    dairyWheatAllergenList.add("Dairy");
    dairyWheatAllergenList.add("Wheat");
    List<String> eggSoyAllergenList = new ArrayList<>();
    eggSoyAllergenList.add("Egg");
    eggSoyAllergenList.add("Soy");
    List<String> peanutTreenutSoyShellfishAllergenList = new ArrayList<>();
    peanutTreenutSoyShellfishAllergenList.add("Peanut");
    peanutTreenutSoyShellfishAllergenList.add("Tree Nut");
    peanutTreenutSoyShellfishAllergenList.add("Soy");
    peanutTreenutSoyShellfishAllergenList.add("Shellfish");

    // All Day

    allMenuItems.add(new MealItem("Water", "Drink", allDayTimeList, noneAllergyList));
    allMenuItems.add(new MealItem("Coffee", "Drink", allDayTimeList, noneAllergyList));
    allMenuItems.add(new MealItem("Tea", "Drink", allDayTimeList, noneAllergyList));

    // Breakfast

    allMenuItems.add(new MealItem("Apple Juice", "Drink", breakfastTimeList, noneAllergyList));
    allMenuItems.add(new MealItem("Orange Juice", "Drink", breakfastTimeList, noneAllergyList));
    allMenuItems.add(new MealItem("Cranberry Juice", "Drink", breakfastTimeList, noneAllergyList));
    allMenuItems.add(
        new MealItem("Belgian Waffle", "Entree", breakfastTimeList, dairyEggWheatAllergenList));
    allMenuItems.add(new MealItem("Omelette", "Entree", breakfastTimeList, dairyEggAllergenList));
    allMenuItems.add(
        new MealItem(
            "Pancakes", "Entree", breakfastTimeList, dairyEggPeanutTreenutWheatAllergenList));
    allMenuItems.add(new MealItem("Apple Sauce", "Snack", breakfastTimeList, noneAllergyList));
    allMenuItems.add(
        new MealItem("Blueberry Muffin", "Snack", breakfastTimeList, dairyEggWheatAllergenList));

    List<String> fruitBowlCategoryList = new ArrayList<>(List.of("Breakfast", "Lunch"));
    allMenuItems.add(
        new MealItem("Fruit Bowl", "Snack", fruitBowlCategoryList, dairyEggWheatAllergenList));

    // Lunch

    allMenuItems.add(new MealItem("Caesar Salad", "Entree", lunchTimeList, noneAllergyList));
    allMenuItems.add(
        new MealItem("Cheeseburger", "Entree", lunchTimeList, dairySoyWheatAllergenList));
    allMenuItems.add(
        new MealItem(
            "Chicken Sandwich", "Entree", lunchTimeList, peanutTreenutSoyShellfishAllergenList));
    allMenuItems.add(new MealItem("Cornbread", "Snack", lunchTimeList, soyWheatAllergenList));

    allMenuItems.add(new MealItem("Pretzel", "Snack", lunchTimeList, wheatAllergenList));

    // Dinner

    allMenuItems.add(new MealItem("Coca Cola", "Drink", dinnerTimeList, noneAllergyList));
    allMenuItems.add(new MealItem("Sprite", "Drink", dinnerTimeList, noneAllergyList));
    allMenuItems.add(
        new MealItem("Cheese Pizza", "Entree", dinnerTimeList, dairyWheatAllergenList));
    allMenuItems.add(
        new MealItem("Chicken Parm", "Entree", dinnerTimeList, dairySoyWheatAllergenList));
    allMenuItems.add(
        new MealItem(
            "Spaghetti and Meatballs", "Entree", dinnerTimeList, dairyEggWheatAllergenList));
    allMenuItems.add(new MealItem("Brownie", "Snack", dinnerTimeList, eggSoyAllergenList));
    allMenuItems.add(
        new MealItem("Chocolate Chip Cookie", "Snack", dinnerTimeList, noneAllergyList));
    allMenuItems.add(new MealItem("Tiramisu", "Snack", dinnerTimeList, noneAllergyList));

    populateFoodLists();
  }

  /** */
  protected void populateFoodLists() {
    for (MealItem item : allMenuItems) {
      if (item.getCategory().equals("Drink")) {
        drinkItemsList.add(item);
      }
      if (item.getCategory().equals("Entree")) {
        entreeItemsList.add(item);
      }
      if (item.getCategory().equals("Snack")) {
        snackItemsList.add(item);
      }
      if (item.getTimeOfDayList().contains("Breakfast")) {
        breakfastItemsList.add(item);
      }
      if (item.getTimeOfDayList().contains("Lunch")) {
        lunchItemsList.add(item);
      }
      if (item.getTimeOfDayList().contains("Dinner")) {
        dinnerItemsList.add(item);
      }
    }
  }

  @FXML
  private void validateButton() {
    if (!(patientIDDropDown.getSelectionModel().getSelectedItem() == null)
        && !(patientNameDropDown.getSelectionModel().getSelectedItem() == null)
        && !(roomNumberDropDown.getSelectionModel().getSelectedItem() == null)
        && !(drinkOptionDropDown.getSelectionModel().getSelectedItem() == null)
        && !(entreeOptionDropDown.getSelectionModel().getSelectedItem() == null)
        && !(snackOptionDropDown.getSelectionModel().getSelectedItem() == null)) {
      submitButton.setDisable(false);
      // System.out.println("Meal Request Submit button enabled");
    } else {
      submitButton.setDisable(true);
      // System.out.println("Meal Request Submit button disabled");
    }

    //    validateTime();
  }

  /** Base meal options on the time of day by hour. */
  protected void validateTime() {

    // Time base on time zone
    //    TimeZone timeZone = TimeZone.getTimeZone("America/New_York");
    //    Calendar calendar = new GregorianCalendar(timeZone);
    //    int localHour = Calendar.HOUR_OF_DAY;
    //    System.out.println(localHour);

    // Local Time
    LocalDateTime date = LocalDateTime.now();
    int localHour = date.getHour();
    // System.out.println("Local Hour: " + localHour);

    if (localHour <= 11) {
      mealOptionDropDown.setValue("Breakfast");
    } else if (localHour <= 17) {
      mealOptionDropDown.setValue("Lunch");
    } else {
      mealOptionDropDown.setValue("Dinner");
    }

    refreshOptionDropDowns();
  }

  /** */
  protected void updateMealOptions(
      List<MealItem> entrees, List<MealItem> snacks, List<MealItem> drinks) {
    drinkOptionDropDown.setItems(
        FXCollections.observableArrayList(
            drinks.stream().map(MealItem::toString).collect(Collectors.toList())));
    entreeOptionDropDown.setItems(
        FXCollections.observableArrayList(
            entrees.stream().map(MealItem::toString).collect(Collectors.toList())));
    snackOptionDropDown.setItems(
        FXCollections.observableArrayList(
            snacks.stream().map(MealItem::toString).collect(Collectors.toList())));
  }

  /** */
  private void updatePatientID() {
    // System.out.println("Update 1: Patient ID ComboBox Selected");

    if (patientIDDropDown.getValue() != null) {
      //      System.out.println("Patient ID Value: " + patientIDDropDown.getValue());
      //      System.out.println("Patient Name: " +
      // patientNameList.indexOf(patientIDDropDown.getValue()));
      //      System.out.println(
      //          "Patient New Name: "
      //              + patientNameList.get(patientIDList.indexOf(patientIDDropDown.getValue())));

      String tempName = instanceDAO.getPatientByID(patientIDDropDown.getValue()).getName();
      patientNameDropDown.setValue(tempName);
      // System.out.println("TempName: " + tempName);

      String tempShortName =
          instanceDAO.getPatientByID(patientIDDropDown.getValue()).getLocation().getShortName();

      String tempRoomNumber = tempShortName.substring(tempShortName.length() - 4);
      roomNumberDropDown.setValue(tempRoomNumber);
      // System.out.println("tempRoomNumber: " + tempRoomNumber);
    }
  }

  private void updateAllergens() {
    // System.out.println("Updating allergens...");

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
        // System.out.println("Removed \"none\" from Patient Allergen list");
      }

    } else {
      // Re-add none if not present
      if (!patientAllergensList.contains("none")) {
        patientAllergensList.add("none");
      }
    }
  }

  /** */
  protected void refreshOptionDropDowns() {

    List<MealItem> drinks =
        allMenuItems.stream()
            .filter(
                item ->
                    item.getCategory().equals("Drink")
                        && item.getTimeOfDayList().contains(mealOptionDropDown.getValue()))
            .collect(Collectors.toList());
    List<MealItem> entrees =
        allMenuItems.stream()
            .filter(
                item ->
                    item.getCategory().equals("Entree")
                        && item.getTimeOfDayList().contains(mealOptionDropDown.getValue()))
            .collect(Collectors.toList());
    List<MealItem> snacks =
        allMenuItems.stream()
            .filter(
                item ->
                    item.getCategory().equals("Snack")
                        && item.getTimeOfDayList().contains(mealOptionDropDown.getValue()))
            .collect(Collectors.toList());

    List<List<MealItem>> allItems = new ArrayList<>(List.of(drinks, snacks, entrees));
    for (List<MealItem> list : allItems) {
      for (String allergen : patientAllergensList) {
        list.removeIf(item -> item.getAllergensList().contains(allergen));
      }
    }

    updateMealOptions(entrees, snacks, drinks);
    updateAllergens();
  }
}
