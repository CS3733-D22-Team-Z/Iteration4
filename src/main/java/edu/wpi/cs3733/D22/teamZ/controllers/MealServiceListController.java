package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MealServiceListController implements Initializable, IMenuAccess {

  // Button that re-fetches requests and refreshes table.
  @FXML private MFXButton refreshButton;

  // Buttons to select the sorting/filter parameters.
  @FXML private MFXButton idButton;
  @FXML private MFXButton patientButton;
  @FXML private MFXButton statusButton;
  @FXML private MFXButton assigneeButton;

  // Drop-down box that selects which data type to filter by.
  @FXML private ChoiceBox<String> filterCBox;

  // Details Table
  @FXML public TableView<TableColumnItems> statusTable;
  @FXML private TableColumn<TableColumnItems, String> labelsColumn;
  @FXML private TableColumn<TableColumnItems, String> detailsColumn;

  // Main table
  @FXML public TableView<RequestRow> tableContainer;
  @FXML private TableColumn<RequestRow, String> idColumn;
  @FXML private TableColumn<RequestRow, String> statusColumn;
  @FXML private TableColumn<RequestRow, String> locationColumn;
  @FXML private TableColumn<RequestRow, String> drinkColumn;
  @FXML private TableColumn<RequestRow, String> entreeColumn;
  @FXML private TableColumn<RequestRow, String> snackColumn;
  //  @FXML private TableColumn<RequestRow, String> assigneeColumn;

  private final String toHomepageURL = "views/Homepage.fxml";
  private final String toMealServiceRequestURL = "edu/wpi/cs3733/D22/teamZ/views/MealService.fxml";

  // List of identifiers for each
  private final String[] identifiers = {
    "ID",
    "PatientID",
    "Assignee",
    "Handler",
    "Status",
    "Target Location",
    "Drink",
    "Entree",
    "Snack"
  };

  //  requestID,patientID,drink,entree,side
  //  String requestID,
  //  ServiceRequest.RequestType type,
  //  ServiceRequest.RequestStatus status,
  //  Employee issuer,
  //  Employee handler,
  //  Location targetLocation

  private MenuController menu;

  // List of MealServReq that represents raw data
  private List<MealServiceRequest> rawRequests;

  // List of RequestRows currently being displayed on the table
  private ObservableList<RequestRow> requests;

  // Database object
  private final FacadeDAO facadeDAO;

  /** */
  public MealServiceListController() {
    // Create new database object
    facadeDAO = FacadeDAO.getInstance();

    // Grab data
    loadRequests();
  }

  /** @param menu */
  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  /** @return */
  @Override
  public String getMenuName() {
    return "Meal Service Request List";
  }

  /**
   * @param location
   * @param resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Create labels for field values
    for (String identifier : identifiers) {
      Label ID = new Label();
      ID.setText(identifier);
    }

    // Fill the filter box with test data
    filterCBox.getItems().addAll("Test 1", "Test 2", "Test 3");

    // Setup details window
    int sWidth = 186 / 2;
    labelsColumn.setCellValueFactory(tRow -> tRow.getValue().label);
    labelsColumn.setPrefWidth(sWidth);
    labelsColumn.setResizable(false);
    labelsColumn.setReorderable(false);

    detailsColumn.setCellValueFactory(tRow -> tRow.getValue().detail);
    detailsColumn.setPrefWidth(sWidth);
    detailsColumn.setResizable(false);
    detailsColumn.setReorderable(false);

    // Setup main list

    // idColumn;
    // statusColumn;
    // locationColumn;
    // drinkColumn;
    // entreeColumn;
    // snackColumn;
    int width = 380 / 6;

    idColumn.setCellValueFactory(rRow -> rRow.getValue().id);
    idColumn.setPrefWidth(width);
    idColumn.setResizable(false);
    idColumn.setReorderable(false);

    statusColumn.setCellValueFactory(rRow -> rRow.getValue().status);
    statusColumn.setPrefWidth(width);
    statusColumn.setResizable(false);
    statusColumn.setReorderable(false);

    locationColumn.setCellValueFactory(rRow -> rRow.getValue().location);
    locationColumn.setPrefWidth(width);
    locationColumn.setResizable(false);
    locationColumn.setReorderable(false);

    drinkColumn.setCellValueFactory(rRow -> rRow.getValue().drink);
    drinkColumn.setPrefWidth(width);
    drinkColumn.setResizable(false);
    drinkColumn.setReorderable(false);

    entreeColumn.setCellValueFactory(rRow -> rRow.getValue().entree);
    entreeColumn.setPrefWidth(width);
    entreeColumn.setResizable(false);
    entreeColumn.setReorderable(false);

    snackColumn.setCellValueFactory(rRow -> rRow.getValue().snack);
    snackColumn.setPrefWidth(width);
    snackColumn.setResizable(false);
    snackColumn.setReorderable(false);

    tableContainer
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldVal, newVal) ->
                loadRow(tableContainer.getSelectionModel().getSelectedItem().id.get()));

    // Initialize requests
    requests = FXCollections.observableArrayList();

    loadRequests();
    createRRList();
  }

  // Called whenever one of the filter buttons are clicked.
  /** @param event */
  public void filterClicked(ActionEvent event) {
    MFXButton buttonPressed = (MFXButton) event.getTarget();
    System.out.println(buttonPressed.getText());
  }

  // Called whenever the refresh button is clicked.
  /** @param event */
  public void refreshClicked(ActionEvent event) {
    System.out.println(refreshButton.getText());

    // Reload requests
    loadRequests();

    // Reload table
    createRRList();
  }

  // Called whenever the filter select was set?
  /** @param event */
  public void filterSet(ActionEvent event) {
    System.out.println(filterCBox.getSelectionModel().getSelectedItem());
  }

  /** */
  public void createRRList() {
    // Clear old requests
    requests.clear();

    // Iterate through each MealServReq in entity and create RequestRow for each
    for (MealServiceRequest mealServiceRequest : rawRequests) {
      String handlerName =
          (mealServiceRequest.getHandler() != null)
              ? mealServiceRequest.getHandler().getName()
              : "null";
      requests.add(
          new RequestRow(
              mealServiceRequest.getRequestID(),
              mealServiceRequest.getStatus().toString(),
              mealServiceRequest.getTargetLocation().getShortName(),
              mealServiceRequest.getDrink(),
              mealServiceRequest.getEntree(),
              mealServiceRequest.getSide()));
    }

    /*// Set root's children to requests, and add root to table.
    final TreeItem<RequestRow> root =
        new RecursiveTreeItem<>(requests, RecursiveTreeObject::getChildren);
    requestTable.setRoot(root);*/

    tableContainer.setItems(requests);
  }

  // Load a MealServReq into the Details row.
  /** @param MealID */
  public void loadRow(String MealID) {
    // Clear out current details data
    statusTable.refresh();

    // Retrieve the MedEquipReq with the given ID.
    MealServiceRequest selectedReq = getRequestFromID(MealID);

    // statusTable.getColumns().add(labelsColumn);
    // statusTable.getColumns().add(detailsColumn);
    String handlerName =
        (selectedReq.getHandler() != null) ? selectedReq.getHandler().getName() : "null";

    statusTable.getItems().add(new TableColumnItems("ID", selectedReq.getRequestID()));
    statusTable.getItems().add(new TableColumnItems("Type", selectedReq.getType().toString()));
    statusTable.getItems().add(new TableColumnItems("Status", selectedReq.getStatus().toString()));
    statusTable.getItems().add(new TableColumnItems("Issuer", selectedReq.getIssuer().getName()));
    statusTable.getItems().add(new TableColumnItems("Handler", handlerName));
    statusTable
        .getItems()
        .add(new TableColumnItems("Destination", selectedReq.getTargetLocation().getLongName()));
  }

  /**
   * Navigate back to Meal Service Request
   *
   * @param event
   * @throws IOException
   */
  public void onBackButtonClicked(ActionEvent event) throws IOException {
    try {
      menu.load(toMealServiceRequestURL);
    } catch (IOException e) {
      System.out.println("Error: Failed to load Meal Service Request List URL");
      e.printStackTrace();
      throw new IOException();
    }
  }

  /** */
  public static class TableColumnItems {
    SimpleStringProperty label;
    SimpleStringProperty detail;

    public TableColumnItems(String label, String detail) {
      this.label = new SimpleStringProperty(label);
      this.detail = new SimpleStringProperty(detail);
    }
  }

  /** */
  public void loadRequests() {
    rawRequests = FacadeDAO.getInstance().getAllMealServiceRequests();
  }

  /**
   * @param MealID
   * @return
   */
  public MealServiceRequest getRequestFromID(String MealID) {
    return FacadeDAO.getInstance().getMealServiceRequestByID(MealID);
  }

  /** @param actionEvent */
  public void exportToCSV(ActionEvent actionEvent) {

    FileChooser fileChooser = new FileChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    fileChooser.setTitle("Enter a .csv file...");
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);

    File defaultFile = facadeDAO.getDefaultMealServReqCSVPath();
    if (defaultFile.isDirectory()) {
      fileChooser.setInitialDirectory(defaultFile);
    } else {
      fileChooser.setInitialDirectory(defaultFile.getParentFile());
      fileChooser.setInitialFileName(defaultFile.getName());
    }

    File file = fileChooser.showSaveDialog(stage);
    facadeDAO.exportMealServiceRequestsToCSV(file);
  }

  // Data structure to represent a row in the request list.
  // Does this belong here or in an entity?
  /** */
  static class RequestRow {
    SimpleStringProperty id;
    SimpleStringProperty status;
    SimpleStringProperty location;
    SimpleStringProperty drink;
    SimpleStringProperty entree;
    SimpleStringProperty snack;

    public RequestRow(
        String newId,
        String newStatus,
        String newLocation,
        String newDrink,
        String newEntree,
        String newSnack) {
      id = new SimpleStringProperty(newId);
      status = new SimpleStringProperty(newStatus);
      location = new SimpleStringProperty(newLocation);
      drink = new SimpleStringProperty(newDrink);
      entree = new SimpleStringProperty(newEntree);
      snack = new SimpleStringProperty(newSnack);
    }
  }
}