package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipmentDeliveryRequest;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
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

public class DontUseThisController implements Initializable, IMenuAccess {
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
  @FXML public TableView<TableColumnItems> statusTable;
  @FXML private TableColumn<TableColumnItems, String> labelsColumn;
  @FXML private TableColumn<TableColumnItems, String> detailsColumn;

  // Main table
  @FXML public MFXTableView<RequestRow> tableContainer;
  /*@FXML private TableColumn<RequestRow, String> idColumn;
  @FXML private TableColumn<RequestRow, String> deviceColumn;
  @FXML private TableColumn<RequestRow, String> assigneeColumn;
  @FXML private TableColumn<RequestRow, String> statusColumn;*/

  private final String toHomepageURL = "views/Homepage.fxml";

  // List of identifiers for each
  private String[] identifiers = {
    "ID", "Device", "Assignee", "Handler", "Status", "Target Location"
  };

  private MenuController menu;

  // List of MedEquipReq that represents raw data
  private List<MedicalEquipmentDeliveryRequest> rawRequests;

  // List of RequestRows currently being displayed on the table
  private ObservableList<RequestRow> requests;

  // Database object
  private FacadeDAO facadeDAO;

  public DontUseThisController() {
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
  public String getMenuName() {
    return "Medical Equipment Request List";
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
    labelsColumn.setMinWidth(statusTable.getPrefWidth() / 2);
    labelsColumn.setResizable(false);
    labelsColumn.setReorderable(false);

    detailsColumn.setCellValueFactory(tRow -> tRow.getValue().detail);
    detailsColumn.setMinWidth(statusTable.getPrefWidth() / 2);
    detailsColumn.setResizable(false);
    detailsColumn.setReorderable(false);

    /* Setup main list
    idColumn.setCellValueFactory(rRow -> rRow.getValue().id);
    idColumn.setPrefWidth(tableContainer.getPrefWidth() / 4);
    idColumn.setResizable(false);
    idColumn.setReorderable(false);

    deviceColumn.setCellValueFactory(rRow -> rRow.getValue().device);
    deviceColumn.setPrefWidth(tableContainer.getPrefWidth() / 4);
    deviceColumn.setResizable(false);
    deviceColumn.setReorderable(false);

    assigneeColumn.setCellValueFactory(rRow -> rRow.getValue().assignee);
    assigneeColumn.setPrefWidth(tableContainer.getPrefWidth() / 4);
    assigneeColumn.setResizable(false);
    assigneeColumn.setReorderable(false);

    statusColumn.setCellValueFactory(rRow -> rRow.getValue().status);
    statusColumn.setPrefWidth(tableContainer.getPrefWidth() / 4);
    statusColumn.setResizable(false);
    statusColumn.setReorderable(false);

    tableContainer
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldVal, newVal) ->
                loadRow(tableContainer.getSelectionModel().getSelectedItem().id.get())); */

    int width = (int) (380 / 4);

    MFXTableColumn<RequestRow> idColumn = new MFXTableColumn<>("ID");
    idColumn.setRowCellFactory(
        row ->
            new MFXTableRowCell<>(requestRow -> requestRow.id.get()) {
              {
                setMinWidth(width);
                setMaxWidth(width);
                onMouseClickedProperty()
                    .set(
                        (event) -> {
                          loadRow(
                              tableContainer.getSelectionModel().getSelection().get(0).id.get());
                        });
              }
            });
    idColumn.setMinWidth(width);
    idColumn.setMaxWidth(width);
    // idColumn.setStyle(String.format("-fx-max-width: %d;", (int) (tableContainer.getWidth() /
    // 4)));

    MFXTableColumn<RequestRow> deviceColumn = new MFXTableColumn<>("Device");
    deviceColumn.setRowCellFactory(
        row ->
            new MFXTableRowCell<>(requestRow -> requestRow.device.get()) {
              {
                setMinWidth(width);
                setMaxWidth(width);
                onMouseClickedProperty()
                    .set(
                        (event) -> {
                          ObservableMap map = tableContainer.getSelectionModel().getSelection();
                          for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
                            Integer key = (Integer) it.next();
                            loadRow(
                                tableContainer
                                    .getSelectionModel()
                                    .getSelection()
                                    .get(key)
                                    .id
                                    .get());
                          }
                        });
              }
            });
    // deviceColumn.setStyle(
    //    String.format("-fx-max-width: %d;", (int) (tableContainer.getWidth() / 4)));
    deviceColumn.setMinWidth(width);
    deviceColumn.setMaxWidth(width);

    MFXTableColumn<RequestRow> assigneeColumn = new MFXTableColumn<>("ID");
    assigneeColumn.setRowCellFactory(
        row ->
            new MFXTableRowCell<>(requestRow -> requestRow.assignee.get()) {
              {
                setMinWidth(width);
                setMaxWidth(width);
                onMouseClickedProperty()
                    .set(
                        (event) -> {
                          loadRow(
                              tableContainer.getSelectionModel().getSelection().get(0).id.get());
                        });
              }
            });
    // assigneeColumn.setStyle(
    //    String.format("-fx-max-width: %d;", (int) (tableContainer.getWidth() / 4)));
    assigneeColumn.setMinWidth(width);
    assigneeColumn.setMaxWidth(width);

    MFXTableColumn<RequestRow> statusColumn = new MFXTableColumn<>("Status");
    statusColumn.setRowCellFactory(
        row ->
            new MFXTableRowCell<>(requestRow -> requestRow.status.get()) {
              {
                setMinWidth(width);
                setMaxWidth(width);
                onMouseClickedProperty()
                    .set(
                        (event) -> {
                          loadRow(
                              tableContainer.getSelectionModel().getSelection().get(0).id.get());
                        });
              }
            });
    // statusColumn.setStyle(
    //    String.format("-fx-max-width: %d;", (int) (tableContainer.getWidth() / 4)));
    statusColumn.setMinWidth(width);
    statusColumn.setMaxWidth(width);

    tableContainer.getTableColumns().addAll(idColumn, deviceColumn, assigneeColumn, statusColumn);

    // Initialize requests
    requests = FXCollections.observableArrayList();
    createRRList();

    // tableContainer.autosizeColumnsOnInitialization();
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

    // Iterate through each MedEquipReq in entity and create RequestRow for each
    for (MedicalEquipmentDeliveryRequest medicalEquipmentRequest : rawRequests) {
      requests.add(
          new RequestRow(
              medicalEquipmentRequest.getRequestID(),
              medicalEquipmentRequest.getEquipmentID(),
              medicalEquipmentRequest.getIssuer().getName(),
              medicalEquipmentRequest.getStatus().toString()));
    }

    /*// Set root's children to requests, and add root to table.
    final TreeItem<RequestRow> root =
        new RecursiveTreeItem<>(requests, RecursiveTreeObject::getChildren);
    requestTable.setRoot(root);*/

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

    statusTable.getItems().add(new TableColumnItems("ID", selectedReq.getRequestID()));
    statusTable.getItems().add(new TableColumnItems("Type", selectedReq.getType().toString()));
    statusTable.getItems().add(new TableColumnItems("Status", selectedReq.getStatus().toString()));
    statusTable.getItems().add(new TableColumnItems("Issuer", selectedReq.getIssuer().getName()));
    statusTable.getItems().add(new TableColumnItems("Handler", selectedReq.getHandler().getName()));
    statusTable
        .getItems()
        .add(new TableColumnItems("Destination", selectedReq.getTargetLocation().getLongName()));
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
    rawRequests = facadeDAO.getAllMedicalEquipmentRequest();
  }

  public MedicalEquipmentDeliveryRequest getRequestFromID(String MeqID) {
    return facadeDAO.getMedicalEquipmentRequestByID(MeqID);
  }

  public void exportToCSV(ActionEvent actionEvent) {

    FileChooser fileChooser = new FileChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    fileChooser.setTitle("Enter a .csv file...");
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);

    File file = fileChooser.showSaveDialog(stage);
    facadeDAO.exportMedicalEquipmentRequestsToCSV(file);
  }

  // Data structure to represent a row in the request list.
  // Does this belong here or in an entity?
  class RequestRow {
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
  }
}
