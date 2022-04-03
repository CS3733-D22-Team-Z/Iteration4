package edu.wpi.cs3733.D22.teamZ.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.cs3733.D22.teamZ.App;
import edu.wpi.cs3733.D22.teamZ.database.MedEquipReqDAOImpl;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipmentDeliveryRequest;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MealServiceRequestListController implements Initializable {

  // Each column on the main request list.
  @FXML private JFXTreeTableColumn<RequestRow, String> typeColumn;
  @FXML private JFXTreeTableColumn<RequestRow, String> idColumn;
  @FXML private JFXTreeTableColumn<RequestRow, String> locationColumn;
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

  // Lists that display details about a selection.
  @FXML private JFXListView<Label> identifierList;
  @FXML private JFXListView dataList;

  private final String toHomepageURL = "views/Homepage.fxml";

  // List of identifiers for each
  private String[] identifiers = {
    "ID", "Device", "Assignee", "Handler", "Status", "Current Location", "Target Location"
  };

  // List of MedEquipReq that represents raw data
  private List<MedicalEquipmentDeliveryRequest> rawRequests;

  // List of RequestRows currently being displayed on the table
  private ObservableList<RequestRow> requests;

  // Database object
  private MedEquipReqDAOImpl database;

  public MealServiceRequestListController() {
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
      identifierList.getItems().add(ID);
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

    //    deviceColumn.setCellValueFactory(param -> param.getValue().getValue().device);
    //    assigneeColumn.setCellValueFactory(param -> param.getValue().getValue().assignee);
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
    for (MedicalEquipmentDeliveryRequest MERequest : rawRequests) {
      requests.add(
          new RequestRow(
              MERequest.getRequestID(),
              MERequest.getEquipment(),
              MERequest.getIssuer(),
              MERequest.getStatus()));
    }

    // Set root's children to requests, and add root to table.
    final TreeItem<RequestRow> root =
        new RecursiveTreeItem<>(requests, RecursiveTreeObject::getChildren);
    requestTable.setRoot(root);
  }

  // Load a MedEquipReq into the Details row.
  public void loadRow(String MeqID) {
    // Clear out current details data
    dataList.getItems().clear();

    // Retrieve the MedEquipReq with the given ID.
    MedicalEquipmentDeliveryRequest selectedReq = getRequestFromID(MeqID);

    // "ID", "Device", "Assignee", "Handler", "Status", "Current Location", "Target Location"
    // Add and fill labels with relevant information.
    for (int i = 0; i < identifiers.length; i++) {
      Label data = new Label();
      switch (i) {
        case 0:
          data.setText(selectedReq.getRequestID());
          break;
        case 1:
          data.setText(selectedReq.getEquipment());
          break;
        case 2:
          data.setText(selectedReq.getIssuer());
          break;
        case 3:
          data.setText(selectedReq.getHandler());
          break;
        case 4:
          data.setText(selectedReq.getStatus());
          break;
        case 5:
          data.setText(selectedReq.getCurrentLoc());
          break;
        case 6:
          data.setText(selectedReq.getTargetLoc());
          break;
      }

      // Add label to dataList
      dataList.getItems().add(data);
    }
  }

  public void loadRequests() {
    rawRequests = database.getAllMedEquipReq();
  }

  public MedicalEquipmentDeliveryRequest getRequestFromID(String MeqID) {
    return database.getMedEquipReqByID(MeqID);
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
