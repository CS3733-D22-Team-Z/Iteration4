package edu.wpi.cs3733.D22.teamZ.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.D22.teamZ.App;
import edu.wpi.cs3733.D22.teamZ.database.MedEquipReqDAOImpl;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipmentDeliveryRequest;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MedicalEquipmentRequestListController implements Initializable {

  // Each column on the main request list.
  @FXML private JFXTreeTableColumn<RequestRow, String> deviceColumn;
  @FXML private JFXTreeTableColumn<RequestRow, String> idColumn;
  @FXML private JFXTreeTableColumn<RequestRow, String> assigneeColumn;
  @FXML private JFXTreeTableColumn<RequestRow, String> statusColumn;

  // Table that lists all requests.
  @FXML private JFXTreeTableView requestTable;

  // Button that re-fetches requests and refreshes table.
  @FXML private JFXButton refreshButton;

  // Button that goes back to the default screen.
  @FXML private JFXButton backButton;

  // Buttons to select the sorting/filter parameters.
  @FXML private JFXButton assigneeButton;
  @FXML private JFXButton idButton;
  @FXML private JFXButton deviceButton;
  @FXML private JFXButton statusButton;

  // Drop-down box that selects which data type to filter by.
  @FXML private JFXComboBox<String> filterCBox;

  // Details Table
  @FXML public TableView<TableColumnItems> statusTable;
  @FXML private TableColumn<TableColumnItems, String> labelsColumn;
  @FXML private TableColumn<TableColumnItems, String> detailsColumn;

  private final String toHomepageURL = "views/Homepage.fxml";

  // List of identifiers for each
  private String[] identifiers = {
    "ID", "Device", "Assignee", "Handler", "Status", "Target Location"
  };

  // List of MedEquipReq that represents raw data
  private List<MedicalEquipmentDeliveryRequest> rawRequests;

  // List of RequestRows currently being displayed on the table
  private ObservableList<RequestRow> requests;

  // Database object
  private MedEquipReqDAOImpl database;

  public MedicalEquipmentRequestListController() {
    // Create new database object
    database = new MedEquipReqDAOImpl();

    // Grab data
    loadRequests();
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

    // Replace with lambda eventually
    // Set each column so that it displays the right value from each RequestRow
    idColumn.setCellValueFactory(
        new Callback<
            TreeTableColumn.CellDataFeatures<RequestRow, String>, ObservableValue<String>>() {
          @Override
          public ObservableValue<String> call(
              TreeTableColumn.CellDataFeatures<RequestRow, String> param) {
            return param.getValue().getValue().id;
          }
        });

    deviceColumn.setCellValueFactory(param -> param.getValue().getValue().device);
    assigneeColumn.setCellValueFactory(param -> param.getValue().getValue().assignee);
    statusColumn.setCellValueFactory(param -> param.getValue().getValue().status);

    requests = FXCollections.observableArrayList();

    // Add a selected listener
    requestTable
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldSelection, newSelection) -> {
              RecursiveTreeItem<RequestRow> sel = (RecursiveTreeItem) newSelection;
              System.out.println("Selected #" + sel.getValue().id.getValue());
              loadRow(sel.getValue().id.getValue());
            });
    requestTable.setShowRoot(false);

    // Initialize requests
    createRRList();
  }

  // Called whenever one of the filter buttons are clicked.
  public void filterClicked(ActionEvent event) {
    JFXButton buttonPressed = (JFXButton) event.getTarget();
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

  // Called whenever the back button is clicked.
  public void backClicked() throws IOException {
    Stage mainStage = (Stage) backButton.getScene().getWindow();
    Parent root = FXMLLoader.load(App.class.getResource(toHomepageURL));
    Scene scene = new Scene(root);
    mainStage.setScene(scene);
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

    // Set root's children to requests, and add root to table.
    final TreeItem<RequestRow> root =
        new RecursiveTreeItem<>(requests, RecursiveTreeObject::getChildren);
    requestTable.setRoot(root);
  }

  // Load a MedEquipReq into the Details row.
  public void loadRow(String MeqID) {
    // Clear out current details data
    statusTable.getItems().clear();

    // Retrieve the MedEquipReq with the given ID.
    MedicalEquipmentDeliveryRequest selectedReq = getRequestFromID(MeqID);

    // statusTable.getColumns().add(labelsColumn);
    // statusTable.getColumns().add(detailsColumn);

    labelsColumn.setCellValueFactory(new PropertyValueFactory<>("label"));
    detailsColumn.setCellValueFactory(new PropertyValueFactory<>("detail"));

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
    String label = null;
    String detail = null;

    public TableColumnItems(String label, String detail) {
      this.label = label;
      this.detail = detail;
    }

    public String getLabel() {
      return label;
    }

    public void setLabel(String label) {
      this.label = label;
    }

    public String getDetail() {
      return detail;
    }

    public void setDetail(String detail) {
      this.detail = detail;
    }
  }

  public void loadRequests() {
    rawRequests = database.getAllMedEquipReq();
  }

  public MedicalEquipmentDeliveryRequest getRequestFromID(String MeqID) {
    return database.getMedEquipReqByID(MeqID);
  }

  public void exportToCSV(ActionEvent actionEvent) {

    FileChooser fileChooser = new FileChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    fileChooser.setTitle("Enter a .csv file...");
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);

    File file = fileChooser.showSaveDialog(stage);
    database.exportToMedEquipReqCSV(file);
  }

  // Data structure to represent a row in the request list.
  // Does this belong here or in an entity?
  class RequestRow extends RecursiveTreeObject<RequestRow> {
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
