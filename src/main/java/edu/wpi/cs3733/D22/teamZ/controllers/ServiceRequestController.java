package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.File;
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

public class ServiceRequestController implements Initializable, IMenuAccess {
  // Button that re-fetches requests and refreshes table.
  @FXML private MFXButton refreshButton;

  // Buttons to select the sorting/filter parameters.
  @FXML private MFXButton assigneeButton;
  @FXML private MFXButton idButton;
  @FXML private MFXButton deviceButton;
  @FXML private MFXButton statusButton;

  // Drop-down box that selects which data type to filter by.
  @FXML private ChoiceBox<String> filterCBox;

  // Details Table
  @FXML public TableView<ServiceRequestController.TableColumnItems> statusTable;
  @FXML private TableColumn<ServiceRequestController.TableColumnItems, String> labelsColumn;
  @FXML private TableColumn<ServiceRequestController.TableColumnItems, String> detailsColumn;

  // Main table
  @FXML public TableView<ServiceRequestController.RequestRow> tableContainer;
  @FXML private TableColumn<ServiceRequestController.RequestRow, String> idColumn;
  @FXML private TableColumn<ServiceRequestController.RequestRow, String> typeColumn;
  @FXML private TableColumn<ServiceRequestController.RequestRow, String> assigneeColumn;
  @FXML private TableColumn<ServiceRequestController.RequestRow, String> statusColumn;

  private final String toHomepageURL = "views/Homepage.fxml";

  // List of identifiers for each
  private String[] identifiers = {"ID", "Type", "Assignee", "Handler", "Status", "Target Location"};

  private MenuController menu;

  // List of ServiceReq that represents raw data
  private List<ServiceRequest> rawRequests;

  // List of RequestRows currently being displayed on the table
  private ObservableList<ServiceRequestController.RequestRow> requests;

  // Database object
  private FacadeDAO facadeDAO;

  public ServiceRequestController() {
    // Create new database object
    facadeDAO = FacadeDAO.getInstance();

    // Grab data
    loadRequests();
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Create labels for field values
    for (int i = 0; i < identifiers.length; i++) {
      Label ID = new Label();
      ID.setText(identifiers[i]);
    }

    // Fill the filter box with test data
    filterCBox.getItems().addAll("Test 1", "Test 2", "Test 3");

    // Setup details window
    labelsColumn.setCellValueFactory(tRow -> tRow.getValue().label);
    labelsColumn.setResizable(false);
    labelsColumn.setReorderable(false);

    detailsColumn.setCellValueFactory(tRow -> tRow.getValue().detail);
    detailsColumn.setResizable(false);
    detailsColumn.setReorderable(false);

    // Setup main list
    idColumn.setCellValueFactory(rRow -> rRow.getValue().id);
    idColumn.setResizable(false);
    idColumn.setReorderable(false);

    typeColumn.setCellValueFactory(rRow -> rRow.getValue().type);
    typeColumn.setResizable(false);
    typeColumn.setReorderable(false);

    assigneeColumn.setCellValueFactory(rRow -> rRow.getValue().assignee);
    assigneeColumn.setResizable(false);
    assigneeColumn.setReorderable(false);

    statusColumn.setCellValueFactory(rRow -> rRow.getValue().status);
    statusColumn.setResizable(false);
    statusColumn.setReorderable(false);

    tableContainer
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldVal, newVal) ->
                loadRow(tableContainer.getSelectionModel().getSelectedItem().id.get()));

    // Initialize requests
    requests = FXCollections.observableArrayList();
    createRRList();
  }

  // Called whenever one of the filter buttons are clicked.
  public void filterClicked(ActionEvent event) {
    MFXButton buttonPressed = (MFXButton) event.getTarget();
    System.out.println(buttonPressed.getText());
  }

  // Called whenever the refresh button is clicked.
  public void refreshClicked(ActionEvent event) {
    System.out.println(refreshButton.getText());

    // Reload requests
    loadRequests();

    // Reload table
    createRRList();
  }

  // Called whenever the filter select was set?
  public void filterSet(ActionEvent event) {
    System.out.println(filterCBox.getSelectionModel().getSelectedItem());
  }

  public void createRRList() {
    // Clear old requests
    requests.clear();

    // Iterate through each ServiceReq in entity and create RequestRow for each
    for (ServiceRequest serviceRequest : rawRequests) {
      requests.add(
          new ServiceRequestController.RequestRow(
              serviceRequest.getRequestID(),
              serviceRequest.getType().toString(),
              serviceRequest.getIssuer().getName(),
              serviceRequest.getStatus().toString()));
    }

    tableContainer.setItems(requests);
  }

  // Load a SErviceReq into the Details row.
  public void loadRow(String reqID) {
    // Clear out current details data
    statusTable.getItems().clear();

    // Retrieve the ServiceReq with the given ID.
    ServiceRequest selectedReq = getRequestFromID(reqID);

    // statusTable.getColumns().add(labelsColumn);
    // statusTable.getColumns().add(detailsColumn);

    statusTable
        .getItems()
        .add(new ServiceRequestController.TableColumnItems("ID", selectedReq.getRequestID()));
    statusTable
        .getItems()
        .add(
            new ServiceRequestController.TableColumnItems(
                "Type", selectedReq.getType().toString()));
    statusTable
        .getItems()
        .add(
            new ServiceRequestController.TableColumnItems(
                "Status", selectedReq.getStatus().toString()));
    statusTable
        .getItems()
        .add(
            new ServiceRequestController.TableColumnItems(
                "Issuer", selectedReq.getIssuer().getName()));
    statusTable
        .getItems()
        .add(
            new ServiceRequestController.TableColumnItems(
                "Handler", selectedReq.getHandler().getName()));
    statusTable
        .getItems()
        .add(
            new ServiceRequestController.TableColumnItems(
                "Destination", selectedReq.getTargetLocation().getLongName()));
  }

  public class TableColumnItems {
    SimpleStringProperty label = null;
    SimpleStringProperty detail = null;

    public TableColumnItems(String label, String detail) {
      this.label = new SimpleStringProperty(label);
      this.detail = new SimpleStringProperty(detail);
    }
  }

  public void loadRequests() {
    rawRequests = facadeDAO.getAllServiceRequests();
  }

  public ServiceRequest getRequestFromID(String reqID) {
    return facadeDAO.getServiceRequestByID(reqID);
  }

  public void exportToCSV(ActionEvent actionEvent) {

    FileChooser fileChooser = new FileChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    fileChooser.setTitle("Enter a .csv file...");
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);

    File file = fileChooser.showSaveDialog(stage);
    facadeDAO.exportServiceRequestsToCSV(file);
  }

  // Data structure to represent a row in the request list.
  // Does this belong here or in an entity?
  class RequestRow {
    SimpleStringProperty id;
    SimpleStringProperty type;
    SimpleStringProperty assignee;
    SimpleStringProperty status;

    public RequestRow(String newId, String newType, String newAssignee, String newStatus) {
      id = new SimpleStringProperty(newId);
      type = new SimpleStringProperty(newType);
      assignee = new SimpleStringProperty(newAssignee);
      status = new SimpleStringProperty(newStatus);
    }
  }
}
