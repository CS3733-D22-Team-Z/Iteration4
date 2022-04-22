package edu.wpi.cs3733.D22.teamZ.controllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipmentDeliveryRequest;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MedicalEquipmentRequestListController implements Initializable, IMenuAccess {
  // Button that re-fetches requests and refreshes table.
  @FXML private MFXButton refreshButton;

  // Buttons to select the sorting/filter parameters.
  @FXML private MFXButton assigneeButton;
  @FXML private MFXButton idButton;
  @FXML private MFXButton deviceButton;
  @FXML private MFXButton statusButton;

  // Selector button stuff
  private MFXButton lastButtonPressed;
  private Map<String, String> prevCSS;

  // Drop-down box that selects which data type to filter by.
  @FXML private ComboBox<String> filterCBox;

  // Details Table
  @FXML public TableView<TableColumnItems> statusTable;
  @FXML private TableColumn<TableColumnItems, String> labelsColumn;
  @FXML private TableColumn<TableColumnItems, String> detailsColumn;

  // Main table
  @FXML public TableView<RequestRow> tableContainer;
  @FXML private TableColumn<RequestRow, String> idColumn;
  @FXML private TableColumn<RequestRow, String> deviceColumn;
  @FXML private TableColumn<RequestRow, String> assigneeColumn;
  @FXML private TableColumn<RequestRow, String> statusColumn;

  private final String toHomepageURL = "views/Homepage.fxml";

  // List of identifiers for each
  private final String[] identifiers = {
    "ID", "Device", "Assignee", "Handler", "Status", "Target Location"
  };

  private MenuController menu;

  // List of MedEquipReq that represents raw data
  private List<MedicalEquipmentDeliveryRequest> rawRequests;

  // List of RequestRows currently being displayed on the table
  private ObservableList<RequestRow> requests;
  private ObservableList<RequestRow> dispRequests;
  private String filter = "";

  // Database object
  private final FacadeDAO facadeDAO;

  public MedicalEquipmentRequestListController() {
    // Create new database object
    facadeDAO = FacadeDAO.getInstance();

    // Grab data
    loadRequests();

    // Setup CSS map
    prevCSS =
        Map.of(
            "ID",
            "-fx-background-radius: 5 0 0 5; ",
            "Device",
            "-fx-border-width: 0 1 0 1; -fx-border-color: #D2D2D2; ",
            "Status",
            "-fx-border-width: 0 1 0 0; -fx-border-color: #D2D2D2; ",
            "Handler",
            "-fx-background-radius: 0 5 5 0; ");
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Medical Equipment Request List";
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Create labels for field values
    for (String identifier : identifiers) {
      Label ID = new Label();
      ID.setText(identifier);
    }

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
    int width = 380 / 4;

    idColumn.setCellValueFactory(rRow -> rRow.getValue().id);
    idColumn.setPrefWidth(width);
    idColumn.setResizable(false);
    idColumn.setReorderable(false);

    deviceColumn.setCellValueFactory(rRow -> rRow.getValue().device);
    deviceColumn.setPrefWidth(width);
    deviceColumn.setResizable(false);
    deviceColumn.setReorderable(false);

    assigneeColumn.setCellValueFactory(rRow -> rRow.getValue().assignee);
    assigneeColumn.setPrefWidth(width);
    assigneeColumn.setResizable(false);
    assigneeColumn.setReorderable(false);

    statusColumn.setCellValueFactory(rRow -> rRow.getValue().status);
    statusColumn.setPrefWidth(width);
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
    if (lastButtonPressed != null)
      lastButtonPressed.setStyle(prevCSS.get(lastButtonPressed.getText()));
    filterCBox.getItems().clear();
    if (lastButtonPressed == null || !lastButtonPressed.equals(event.getTarget())) {
      lastButtonPressed = (MFXButton) event.getTarget();
      filter = lastButtonPressed.getText();
      lastButtonPressed.setStyle(
          prevCSS.get(lastButtonPressed.getText())
              + "-fx-background-color: #0075ff; -fx-text-fill: #FFFFFF");

      // Filter buttons
      Comparator<RequestRow> comparator =
          Comparator.comparing(r -> r.retrievePropertyFromType(filter));
      FXCollections.sort(requests, comparator);

      // Setup filter box
      Set<String> allFilterOptions = new HashSet<>();
      for (RequestRow row : requests) {
        allFilterOptions.add(row.retrievePropertyFromType(filter));
      }
      allFilterOptions.add("None");
      filterCBox.getItems().addAll(allFilterOptions);
      filterCBox.getSelectionModel().select("None");
    } else {
      tableContainer.setItems(requests);
      lastButtonPressed = null;
    }
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
  public void filterSet() {
    String filterOption = filterCBox.getSelectionModel().getSelectedItem();
    FilteredList<RequestRow> fList =
        requests.filtered(
            new Predicate<RequestRow>() {
              @Override
              public boolean test(RequestRow requestRow) {
                if (filterOption == null || filterOption.equals("None")) return true;
                return requestRow.retrievePropertyFromType(filter).equals(filterOption);
              }
            });

    tableContainer.setItems(fList);
  }

  public void createRRList() {
    // Clear old requests
    requests.clear();

    // Iterate through each MedEquipReq in entity and create RequestRow for each
    for (MedicalEquipmentDeliveryRequest medicalEquipmentRequest : rawRequests) {
      String handlerName =
          (medicalEquipmentRequest.getHandler() != null)
              ? medicalEquipmentRequest.getHandler().getName()
              : "null";
      requests.add(
          new RequestRow(
              medicalEquipmentRequest.getRequestID(),
              medicalEquipmentRequest.getEquipmentID(),
              handlerName,
              medicalEquipmentRequest.getStatus().toString()));
    }

    tableContainer.setItems(requests);
  }

  // Load a MedEquipReq into the Details row.
  public void loadRow(String MeqID) {
    // Clear out current details data
    statusTable.getItems().clear();

    // Retrieve the MedEquipReq with the given ID.
    MedicalEquipmentDeliveryRequest selectedReq = getRequestFromID(MeqID);

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

  public static class TableColumnItems {
    SimpleStringProperty label;
    SimpleStringProperty detail;

    public TableColumnItems(String label, String detail) {
      this.label = new SimpleStringProperty(label);
      this.detail = new SimpleStringProperty(detail);
    }
  }

  public void loadRequests() {
    rawRequests = FacadeDAO.getInstance().getAllMedicalEquipmentRequest();
  }

  public MedicalEquipmentDeliveryRequest getRequestFromID(String MeqID) {
    return FacadeDAO.getInstance().getMedicalEquipmentRequestByID(MeqID);
  }

  public void exportToCSV(ActionEvent actionEvent) {

    FileChooser fileChooser = new FileChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    fileChooser.setTitle("Enter a .csv file...");
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);

    File defaultFile = facadeDAO.getDefaultMedEquipReqCSVPath();
    if (defaultFile.isDirectory()) {
      fileChooser.setInitialDirectory(defaultFile);
    } else {
      fileChooser.setInitialDirectory(defaultFile.getParentFile());
      fileChooser.setInitialFileName(defaultFile.getName());
    }

    File file = fileChooser.showSaveDialog(stage);
    facadeDAO.exportMedicalEquipmentRequestsToCSV(file);
  }

  // Data structure to represent a row in the request list.
  // Does this belong here or in an entity?
  static class RequestRow {
    SimpleStringProperty id;
    SimpleStringProperty device;
    SimpleStringProperty assignee;
    SimpleStringProperty status;

    public RequestRow(String newId, String newDevice, String newAssignee, String newStatus) {
      id = new SimpleStringProperty(newId);
      device = new SimpleStringProperty(newDevice);
      assignee = new SimpleStringProperty(newAssignee);
      status = new SimpleStringProperty(newStatus);
    }

    /**
     * Gets the property from the String identifier
     *
     * @param type the type of property to be retrieved
     * @return the value of the property
     */
    public String retrievePropertyFromType(String type) {
      switch (type) {
        case "ID":
          return id.get();
        case "Device":
          return device.get();
        case "Handler":
          return assignee.get();
        case "Status":
          return status.get();
        default:
          return "";
      }
    }
  }
}
