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

    mealOptionDropDown.setDisable(true); // leave for time dependant -temp
    patientNameDropDown.setDisable(true); // dropdown for only unique names
    patientNameFormHeader.setText("Patient Name");
    roomNumberDropDown.setDisable(true); // what about more than one patient per room? -disabled
    roomNumberFormHeader.setText("Room Number");

    mealRequestIndicator.setText(""); // Clear base error message label

    validateTime(); // Check the time and update meal options based on the hour

    //    locationList =
    //        instanceDAO.getAllLocations().stream()
    //            .filter(Location -> Location.getNodeType() == "PATI")
    //            .collect(Collectors.toList());
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

      System.out.println("Patient ID: " + patient.getPatientID());
      System.out.println("Patient Name: " + patient.getName());
      System.out.println(
          "Patient Room: "
              + patient
                  .getLocation()
                  .getShortName()
                  .substring(patient.getLocation().getShortName().length() - 4));
    }

    // TODO: Remove duplicate room numbers
    // If two or more patients share a room, the first patient in the list will be assigned.
    //

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

    /**
     * Food Item Reference for Allergens TODO: Make food items as objects with attributes. Either
     * here or new Class and/or new CSV /w DAO/Impl. TODO: Food Item Attributes: ID, Name, Category
     * (Drink/Entree/Snack), Time of Day (Breakfast/Lunch/Dinner), TODO: Food Item Att. Cont.: Day
     * of Week, Any Modifiers (Vegan/No Meat), Calories, Allergens
     *
     * <p>TODO: Refine food items to be more inclusive for allergen-free
     *
     * <p>Item | Allergens (Out of Dairy/Egg/Peanut/Tree Nut/Soy/Wheat/Fish/Shellfish)
     *
     * <p>Sample Items:
     *
     * <p>--Breakfast: -Drink: Water .................... none Coffee ................... none Tea
     * ...................... none Apple Juice .............. none Orange Juice ............. none
     * Cranberry Juice .......... none -Entree: Belgium Waffle ........... Egg,Dairy,Wheat Omelette
     * .................. Egg,Dairy Pancakes ................. Egg,Dairy,Peanut,Tree Nut,Wheat
     * -Side: Apple Sauce .............. none Blueberry Muffin ......... Egg,Dairy,Wheat,Gluten
     * Fruit Bowl ............... none
     *
     * <p>--Lunch: -Drink: Water .................... none Coffee ................... none Tea
     * ...................... none -Entree: Caesar Salad ............. none Cheeseburger
     * ............. Dairy,Soy,Wheat Chicken Sandwich ......... Peanuts,Shellfish,Soy,Tree Nut
     * -Side: Corn Bread ............... Soy,Wheat Fruit Bowl ............... none Pretzel
     * .................. Wheat
     *
     * <p>--Dinner: -Drink: Water .................... none Coffee ................... none Tea
     * ...................... none Coca Cola ................ none Sprite ................... none
     * -Entree: Cheese Pizza ............. Dairy, Wheat Chicken Parmigiana ....... Dairy, Soy, Wheat
     * Spaghetti & Meatballs .... Egg, Dairy, Wheat -Side: Brownie .................. Egg,Soy
     * Chocolate Chip Cookie .... none Tiramisu ................. Egg,Dairy,Wheat
     */
    //resetMealTimeLists(); // clear and add "none" to time-category lists
    loadFoodItems(); // add food items to time-category lists

    // Before MealItem was an object:

    //    breakfastDrinksList.addAll(
    //        Arrays.asList(
    //            "none", "Water", "Coffee", "Tea", "Apple_Juice", "Orange_Juice",
    // "Cranberry_Juice"));
    //    breakfastEntreesList.addAll(Arrays.asList("none", "Belgium_Waffle", "Omelette",
    // "Pancakes"));
    //    breakfastSnackList.addAll(
    //        Arrays.asList("none", "Apple_Sauce", "Blueberry_Muffin", "Fruit_Bowl"));
    //    lunchDrinksList.addAll(Arrays.asList("none", "Water", "Coffee", "Tea"));
    //    lunchEntreesList.addAll(
    //        Arrays.asList("none", "Caesar_Salad", "Cheeseburger", "Chicken_Sandwich"));
    //    lunchSnackList.addAll(Arrays.asList("none", "Corn_Bread", "Fruit_Bowl", "Pretzel"));
    //    dinnerDrinksList.addAll(Arrays.asList("none", "Water", "Coffee", "Tea", "Coca_Cola",
    // "Sprite"));
    //    dinnerEntreesList.addAll(
    //        Arrays.asList("none", "Cheese_Pizza", "Chicken_Parmigiana", "Spaghetti_&_Meatballs"));
    //    dinnerSnackList.addAll(Arrays.asList("none", "Brownie", "Chocolate_Chip_Cookie",
    // "Tiramisu"));

    // Get all service requests.
    // Used later for correct ID numbering in order. TODO: Notice: Numbers randomized
    allServiceRequestList = instanceDAO.getAllServiceRequests();

    // Set initial fields cleared, not empty
    // Used to avoid errors from validateButton
    patientIDDropDown.setValue(null);
    patientNameDropDown.setValue(null);
    roomNumberDropDown.setValue(null);
    mealOptionDropDown.setValue(null);
    drinkOptionDropDown.setValue(null);
    entreeOptionDropDown.setValue(null);
    snackOptionDropDown.setValue(null);

    // Handle a food item chosen
    //    patientIDDropDown.setOnAction(event -> validateButton());
    //    patientNameDropDown.setOnAction(event -> validateButton());
    //    roomNumberDropDown.setOnAction(event -> validateButton());
    mealOptionDropDown.setOnAction(event -> validateButton());
    drinkOptionDropDown.setOnAction(event -> validateButton());
    entreeOptionDropDown.setOnAction(event -> validateButton());
    snackOptionDropDown.setOnAction(event -> validateButton());

    // TODO: Fix recursion
    // Handle event when a drop-down selection changes
    patientIDDropDown.setOnAction(event -> updatePatientID());
    patientNameDropDown.setOnAction(event -> updatePatientName());
    roomNumberDropDown.setOnAction(event -> updatePatientRoom());

    // Handle event when an allergy choice is changed
    dairyChoice.setOnAction(event -> updateDairyChoice());
    eggChoice.setOnAction(event -> updateEggChoice());
    peanutChoice.setOnAction(event -> updatePeanutChoice());
    treenutChoice.setOnAction(event -> updateTreenutChoice());
    soyChoice.setOnAction(event -> updateSoyChoice());
    wheatChoice.setOnAction(event -> updateWheatChoice());
    fishChoice.setOnAction(event -> updateFishChoice());
    shellfishChoice.setOnAction(event -> updateShellfishChoice());

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

    //    updatePatientID();   // Commented out to stop recursion
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
      System.out.println("Error: Failed to load Meal Service Request List URL");
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

  /** Clear and add "none" to time-category MenuItem lists */
 /* protected void resetMealTimeLists() {
    breakfastDrinksList.clear();
    breakfastEntreesList.clear();
    breakfastSnackList.clear();
    lunchDrinksList.clear();
    lunchEntreesList.clear();
    lunchSnackList.clear();
    dinnerDrinksList.clear();
    dinnerEntreesList.clear();
    dinnerSnackList.clear();

    breakfastDrinksList.add("none");
    breakfastEntreesList.add("none");
    breakfastSnackList.add("none");
    lunchDrinksList.add("none");
    lunchEntreesList.add("none");
    lunchSnackList.add("none");
    dinnerDrinksList.add("none");
    dinnerEntreesList.add("none");
    dinnerSnackList.add("none");
  }*/

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
    noneAllergyList.add("none");

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

    MealItem Coffee = new MealItem();
    Coffee.setName("Coffee");
    Coffee.setCategory("Drink");
    Coffee.setTimeOfDayList(allDayTimeList);
    Coffee.setAllergensList(noneAllergyList);
    allMenuItems.add(Coffee);

    MealItem Tea = new MealItem();
    Tea.setName("Tea");
    Tea.setCategory("Drink");
    Tea.setTimeOfDayList(allDayTimeList);
    Tea.setAllergensList(noneAllergyList);
    allMenuItems.add(Tea);

    // Breakfast

    MealItem AppleJuice = new MealItem();
    AppleJuice.setName("Apple Juice");
    AppleJuice.setCategory("Drink");
    AppleJuice.setTimeOfDayList(allDayTimeList);
    AppleJuice.setAllergensList(noneAllergyList);
    allMenuItems.add(AppleJuice);

    MealItem OrangeJuice = new MealItem();
    OrangeJuice.setName("Orange Juice");
    OrangeJuice.setCategory("Drink");
    OrangeJuice.setTimeOfDayList(breakfastTimeList);
    OrangeJuice.setAllergensList(noneAllergyList);
    allMenuItems.add(OrangeJuice);

    MealItem CranberryJuice = new MealItem();
    CranberryJuice.setName("Cranberry Juice");
    CranberryJuice.setCategory("Drink");
    CranberryJuice.setTimeOfDayList(breakfastTimeList);
    CranberryJuice.setAllergensList(noneAllergyList);
    allMenuItems.add(CranberryJuice);

    allMenuItems.add(new MealItem("Belgian Waffle", "Entree", breakfastTimeList, dairyEggWheatAllergenList));

    MealItem Omelette = new MealItem();
    Omelette.setName("Omelette");
    Omelette.setCategory("Entree");
    Omelette.setTimeOfDayList(breakfastTimeList);
    Omelette.setAllergensList(dairyEggAllergenList);
    allMenuItems.add(Omelette);

    MealItem Pancakes = new MealItem();
    Pancakes.setName("Pancakes");
    Pancakes.setCategory("Entree");
    Pancakes.setTimeOfDayList(breakfastTimeList);
    Pancakes.setAllergensList(dairyEggPeanutTreenutWheatAllergenList);
    allMenuItems.add(Pancakes);

    MealItem AppleSauce = new MealItem();
    AppleSauce.setName("Apple Sauce");
    AppleSauce.setCategory("Snack");
    AppleSauce.setTimeOfDayList(breakfastTimeList);
    AppleSauce.setAllergensList(noneAllergyList);
    allMenuItems.add(AppleSauce);

    MealItem BlueberryMuffin = new MealItem();
    BlueberryMuffin.setName("Blueberry Muffin");
    BlueberryMuffin.setCategory("Snack");
    BlueberryMuffin.setTimeOfDayList(breakfastTimeList);
    BlueberryMuffin.setAllergensList(dairyEggWheatAllergenList);
    allMenuItems.add(BlueberryMuffin);

    MealItem FruitBowl = new MealItem();
    FruitBowl.setName("Fruit Bowl");
    FruitBowl.setCategory("Snack");
    List<String> fruitBowlCategoryList = new ArrayList<>();
    fruitBowlCategoryList.add("Breakfast");
    fruitBowlCategoryList.add("Lunch");
    FruitBowl.setTimeOfDayList(fruitBowlCategoryList);
    FruitBowl.setAllergensList(noneAllergyList);
    allMenuItems.add(FruitBowl);

    // Lunch

    MealItem CaesarSalad = new MealItem();
    CaesarSalad.setName("Caesar Salad");
    CaesarSalad.setCategory("Snack");
    CaesarSalad.setTimeOfDayList(lunchTimeList);
    CaesarSalad.setAllergensList(noneAllergyList);
    allMenuItems.add(CaesarSalad);

    MealItem Cheeseburger = new MealItem();
    Cheeseburger.setName("Cheeseburger");
    Cheeseburger.setCategory("Entree");
    Cheeseburger.setTimeOfDayList(lunchTimeList);
    Cheeseburger.setAllergensList(dairySoyWheatAllergenList);
    allMenuItems.add(Cheeseburger);

    MealItem ChickenSandwich = new MealItem();
    ChickenSandwich.setName("Chicken Sandwich");
    ChickenSandwich.setCategory("Entree");
    ChickenSandwich.setTimeOfDayList(lunchTimeList);
    ChickenSandwich.setAllergensList(peanutTreenutSoyShellfishAllergenList);
    allMenuItems.add(ChickenSandwich);

    MealItem CornBread = new MealItem();
    CornBread.setName("Corn Bread");
    CornBread.setCategory("Snack");
    CornBread.setTimeOfDayList(lunchTimeList);
    CornBread.setAllergensList(soyWheatAllergenList);
    allMenuItems.add(CornBread);

    // Fruit bowl added

    MealItem Pretzel = new MealItem();
    Pretzel.setName("Pretzel");
    Pretzel.setCategory("Snack");
    Pretzel.setTimeOfDayList(lunchTimeList);
    Pretzel.setAllergensList(wheatAllergenList);
    allMenuItems.add(Pretzel);

    MealItem CocaCola = new MealItem();
    CocaCola.setName("Coca Cola");
    CocaCola.setCategory("Drink");
    CocaCola.setTimeOfDayList(dinnerTimeList);
    CocaCola.setAllergensList(noneAllergyList);
    allMenuItems.add(CocaCola);

    MealItem Sprite = new MealItem();
    Sprite.setName("Sprite");
    Sprite.setCategory("Drink");
    Sprite.setTimeOfDayList(dinnerTimeList);
    Sprite.setAllergensList(noneAllergyList);
    allMenuItems.add(Sprite);

    MealItem CheesePizza = new MealItem();
    CheesePizza.setName("Cheese Pizza");
    CheesePizza.setCategory("Entree");
    CheesePizza.setTimeOfDayList(dinnerTimeList);
    CheesePizza.setAllergensList(dairyWheatAllergenList);
    allMenuItems.add(CheesePizza);

    MealItem ChickenParm = new MealItem();
    ChickenParm.setName("Chicken Parm");
    ChickenParm.setCategory("Entree");
    ChickenParm.setTimeOfDayList(dinnerTimeList);
    ChickenParm.setAllergensList(dairySoyWheatAllergenList);
    allMenuItems.add(ChickenParm);

    MealItem SpaghettiAndMeatballs = new MealItem();
    SpaghettiAndMeatballs.setName("Spaghetti and Meatballs");
    SpaghettiAndMeatballs.setCategory("Entree");
    SpaghettiAndMeatballs.setTimeOfDayList(dinnerTimeList);
    SpaghettiAndMeatballs.setAllergensList(dairyEggWheatAllergenList);
    allMenuItems.add(SpaghettiAndMeatballs);

    MealItem Brownie = new MealItem();
    Brownie.setName("Brownie");
    Brownie.setCategory("Snack");
    Brownie.setTimeOfDayList(dinnerTimeList);
    Brownie.setAllergensList(eggSoyAllergenList);
    allMenuItems.add(Brownie);

    MealItem ChocolateChipCookie = new MealItem();
    ChocolateChipCookie.setName("Chocolate Chip Cookie");
    ChocolateChipCookie.setCategory("Snack");
    ChocolateChipCookie.setTimeOfDayList(dinnerTimeList);
    ChocolateChipCookie.setAllergensList(noneAllergyList);
    allMenuItems.add(ChocolateChipCookie);

    MealItem Tiramisu = new MealItem();
    Tiramisu.setName("Tiramisu");
    Tiramisu.setCategory("Snack");
    Tiramisu.setTimeOfDayList(dinnerTimeList);
    Tiramisu.setAllergensList(dairyEggWheatAllergenList);
    allMenuItems.add(Tiramisu);

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

    for (MealItem item : breakfastItemsList) {
      if (item.getCategory().equals("Drink")) {
        breakfastDrinksList.add(item.getName());
      }
      if (item.getCategory().equals("Entree")) {
        breakfastEntreesList.add(item.getName());
      }
      if (item.getCategory().equals("Snack")) {
        breakfastSnackList.add(item.getName());
      }
    }
    for (MealItem item : lunchItemsList) {
      if (item.getCategory().equals("Drink")) {
        lunchDrinksList.add(item.getName());
      }
      if (item.getCategory().equals("Entree")) {
        lunchEntreesList.add(item.getName());
      }
      if (item.getCategory().equals("Snack")) {
        lunchSnackList.add(item.getName());
      }
    }
    for (MealItem item : dinnerItemsList) {
      if (item.getCategory().equals("Drink")) {
        dinnerDrinksList.add(item.getName());
      }
      if (item.getCategory().equals("Entree")) {
        dinnerEntreesList.add(item.getName());
      }
      if (item.getCategory().equals("Snack")) {
        dinnerSnackList.add(item.getName());
      }
    }
  }

  public void enterPatientName(ActionEvent event) {}

  public void enterPatientID(ActionEvent event) {}

  public void enterRoomNumber(ActionEvent event) {}

  public void chooseMealOption(MouseEvent mouseEvent) {}

  public void chooseOrderStatus(MouseEvent mouseEvent) {}

  public void enterStaffAssigned(ActionEvent event) {}

  @FXML
  private void validateButton() {
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
  protected void validateTime() {
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
    } else if (localHour > 11 && localHour <= 17) {
      isMorning = false;
      isDay = true;
      isNight = false;
      mealOptionDropDown.setValue("LUNCH");
    } else {
      isMorning = false;
      isDay = false;
      isNight = true;
      mealOptionDropDown.setValue("DINNER");
    }

    drinkOptionList = allMenuItems.stream().filter(item -> item.getCategory().equals("Drink") && item.getTimeOfDayList().contains(mealOptionDropDown.getValue())).map(MealItem::toString).collect(Collectors.toList());
    entreeOptionList = allMenuItems.stream().filter(item -> item.getCategory().equals("Entree") && item.getTimeOfDayList().contains(mealOptionDropDown.getValue())).map(MealItem::toString).collect(Collectors.toList());
    snackOptionList = allMenuItems.stream().filter(item -> item.getCategory().equals("Snack") && item.getTimeOfDayList().contains(mealOptionDropDown.getValue())).map(MealItem::toString).collect(Collectors.toList());

    drinkOptionDropDown.setItems(FXCollections.observableArrayList(drinkOptionList));
    entreeOptionDropDown.setItems(FXCollections.observableArrayList(entreeOptionList));
    snackOptionDropDown.setItems(FXCollections.observableArrayList(snackOptionList));
  }

  /** */
  protected void updateMealOptions() {
    validateTime();
  }

  /** */
  private void updatePatientID() {
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
  private void updatePatientName() {
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
  private void updatePatientRoom() {
    System.out.println("Update 3: Patient Room ComboBox Selected");
    if (roomNumberDropDown.getValue() != null) {

      // Works based on index. Breaks if the three drop-downs don't match in length
      // TODO: Decide to remove, disable, or refactor to work with Set. Reconsider
      patientIDDropDown.setValue(
          patientIDList.get(roomNumberList.indexOf(roomNumberDropDown.getValue())));
      patientNameDropDown.setValue(
          patientNameList.get(roomNumberList.indexOf(roomNumberDropDown.getValue())));
    }
  }

  private void updateAllergens() {
    System.out.println("Updating allergens...");

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
    String filter = "Dairy";
    if (dairyChoice.isSelected()) {
      System.out.println(filter + " selected");
      if (!patientAllergensList.contains(filter)) {
        patientAllergensList.add(filter);
      }
      for (MealItem item : breakfastItemsList) {
        if (item.getAllergensList().contains(filter)) {
          if (breakfastDrinksList.contains(item.getName())) {
            breakfastDrinksList.remove(item.getName());
          }
          if (breakfastEntreesList.contains(item.getName())) {
            breakfastEntreesList.remove(item.getName());
          }
          if (breakfastSnackList.contains(item.getName())) {
            breakfastSnackList.remove(item.getName());
          }
        }
      }
      for (MealItem item : lunchItemsList) {
        if (item.getAllergensList().contains(filter)) {
          if (lunchDrinksList.contains(item.getName())) {
            lunchDrinksList.remove(item.getName());
          }
          if (lunchEntreesList.contains(item.getName())) {
            lunchEntreesList.remove(item.getName());
          }
          if (lunchSnackList.contains(item.getName())) {
            lunchSnackList.remove(item.getName());
          }
        }
      }
      for (MealItem item : dinnerItemsList) {
        if (item.getAllergensList().contains(filter)) {
          if (dinnerDrinksList.contains(item.getName())) {
            dinnerDrinksList.remove(item.getName());
          }
          if (dinnerEntreesList.contains(item.getName())) {
            dinnerEntreesList.remove(item.getName());
          }
          if (dinnerSnackList.contains(item.getName())) {
            dinnerSnackList.remove(item.getName());
          }
        }
      }
    } else {
      System.out.println(filter + " unselected");
      if (patientAllergensList.contains(filter)) {
        patientAllergensList.remove(filter);
      }
      for (MealItem item : breakfastItemsList) {
        if (item.getAllergensList().contains(filter)) {
          if (!breakfastDrinksList.contains(item.getName())) {
            breakfastDrinksList.add(item.getName());
          }
          if (!breakfastEntreesList.contains(item.getName())) {
            breakfastEntreesList.add(item.getName());
          }
          if (!breakfastSnackList.contains(item.getName())) {
            breakfastSnackList.add(item.getName());
          }
        }
      }
      for (MealItem item : lunchItemsList) {
        if (item.getAllergensList().contains(filter)) {
          if (!lunchDrinksList.contains(item.getName())) {
            lunchDrinksList.add(item.getName());
          }
          if (!lunchEntreesList.contains(item.getName())) {
            lunchEntreesList.add(item.getName());
          }
          if (!lunchSnackList.contains(item.getName())) {
            lunchSnackList.add(item.getName());
          }
        }
      }
      for (MealItem item : dinnerItemsList) {
        if (item.getAllergensList().contains(filter)) {
          if (!dinnerDrinksList.contains(item.getName())) {
            dinnerDrinksList.add(item.getName());
          }
          if (!dinnerEntreesList.contains(item.getName())) {
            dinnerEntreesList.add(item.getName());
          }
          if (!dinnerSnackList.contains(item.getName())) {
            dinnerSnackList.add(item.getName());
          }
        }
      }
    }

    updateAllergens();
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
      System.out.println("Egg unselected");
      if (patientAllergensList.contains("Egg")) {
        patientAllergensList.remove("Egg");
      }
    }

    updateAllergens();
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
      System.out.println("Peanut selected");
      if (patientAllergensList.contains("Peanut")) {
        patientAllergensList.remove("Peanut");
      }
    }

    updateAllergens();
  }

  /** */
  private void updateTreenutChoice() {
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

    updateAllergens();
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

    updateAllergens();
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

    updateAllergens();
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

    updateAllergens();
  }

  /** */
  private void updateShellfishChoice() {
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

    updateAllergens();
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



  protected void updateAllergenChoice(String filter) {
    List<List<MealItem>> allItems = new ArrayList<>();

    allItems.add(breakfastItemsList);
    allItems.add(lunchItemsList);
    allItems.add(dinnerItemsList);

    for (List<MealItem> list : allItems) {
      list.removeIf(item -> item.getAllergensList().contains(filter));
    }
  }
}
