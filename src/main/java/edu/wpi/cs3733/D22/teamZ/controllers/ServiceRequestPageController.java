package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import java.io.File;
import java.net.URL;
import java.util.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ServiceRequestPageController implements Initializable, IMenuAccess {
  // Button that re-fetches requests and refreshes table.
  @FXML private MFXButton refresh;

  @FXML private TabPane tabs;
  @FXML private Tab all;
  @FXML private Tab outstanding;

  // Buttons to select the sorting/filter parameters.
  @FXML private MFXButton setEmpButton;

  @FXML private MFXRectangleToggleNode issuerSelect;
  @FXML private MFXRectangleToggleNode handlerSelect;

  // Drop-down box that selects which data type to filter by.
  @FXML private ChoiceBox<String> filterBox;
  @FXML private ChoiceBox<String> employeeBox;

  // Main table
  @FXML public TableView<ServiceRequest> tableContainer;
  @FXML private TableColumn<ServiceRequest, String> idCol;
  @FXML private TableColumn<ServiceRequest, ServiceRequest.RequestType> typeCol;
  @FXML private TableColumn<ServiceRequest, Employee> assignedCol;
  @FXML private TableColumn<ServiceRequest, ServiceRequest.RequestStatus> statusCol;

  @FXML public TableView<ServiceRequest> outstandingTable;
  @FXML private TableColumn<ServiceRequest, String> idColO;
  @FXML private TableColumn<ServiceRequest, ServiceRequest.RequestType> typeColO;
  @FXML private TableColumn<ServiceRequest, Employee> assignedColO;
  @FXML private TableColumn<ServiceRequest, ServiceRequest.RequestStatus> statusColO;

  private final String toHomepageURL = "views/Homepage.fxml";

  protected MenuController menu;

  // Changes per every implementation
  // The name of the page that will be displayed in the bottom menu bar.
  protected String menuName;

  // List of RequestRows currently being displayed on the table
  private ObservableList<ServiceRequest> requests;
  private ObservableList<ServiceRequest> outstandingRequests;

  // Database object
  private FacadeDAO facadeDAO;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Service Request Page";
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    facadeDAO = FacadeDAO.getInstance();
    // Create labels for field values

    // Fill the filter box with test data
    List<Employee> employees = facadeDAO.getAllEmployees();
    for (int i = 0; i < employees.size(); i++) {
      employeeBox.getItems().add(employees.get(i).getEmployeeID());
      filterBox.getItems().add(employees.get(i).getEmployeeID());
    }

    createTable();
    createOutstandingTable();

    tabs.getSelectionModel()
        .selectedItemProperty()
        .addListener(
            new ChangeListener<Tab>() {

              @Override
              public void changed(
                  ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
                issuerSelect.setSelected(false);
                handlerSelect.setSelected(false);
                createTable();
                createOutstandingTable();
                filterBox.getSelectionModel().clearSelection();
              }
            });
  }

  public void createTable() {
    tableContainer.getItems().clear();
    idCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, String>("requestID"));
    typeCol.setCellValueFactory(
        new PropertyValueFactory<ServiceRequest, ServiceRequest.RequestType>("type"));
    assignedCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, Employee>("handler"));
    statusCol.setCellValueFactory(
        new PropertyValueFactory<ServiceRequest, ServiceRequest.RequestStatus>("status"));
    requests = FXCollections.observableList(facadeDAO.getAllServiceRequests());
    tableContainer.setItems(requests);
  }

  public void createOutstandingTable() {
    outstandingTable.getItems().clear();
    idColO.setCellValueFactory(new PropertyValueFactory<ServiceRequest, String>("requestID"));
    typeColO.setCellValueFactory(
        new PropertyValueFactory<ServiceRequest, ServiceRequest.RequestType>("type"));
    assignedColO.setCellValueFactory(new PropertyValueFactory<ServiceRequest, Employee>("handler"));
    statusColO.setCellValueFactory(
        new PropertyValueFactory<ServiceRequest, ServiceRequest.RequestStatus>("status"));
    outstandingRequests =
        FXCollections.observableList(
            facadeDAO.getServiceRequestsByStatus(ServiceRequest.RequestStatus.PROCESSING));
    outstandingRequests.addAll(
        FXCollections.observableList(
            facadeDAO.getServiceRequestsByStatus(ServiceRequest.RequestStatus.UNASSIGNED)));
    outstandingTable.setItems(outstandingRequests);
  }

  // Called whenever the refresh button is clicked.
  public void refreshClicked(ActionEvent event) {
    // System.out.println(refresh.getText());
    // employeeBox.getItems().clear();

    // Reload table
    createTable();
  }

  public void filterList(ActionEvent event) {
    if (filterBox.getSelectionModel().getSelectedItem() == null) {
      createTable();
      createOutstandingTable();
      return;
    }
    if (issuerSelect.isSelected()) {
      if (all.isSelected()) {
        requests = FXCollections.observableList(facadeDAO.getAllServiceRequests());
        filter(
            requests,
            false,
            facadeDAO.getEmployeeByID(filterBox.getSelectionModel().getSelectedItem()));
      } else {
        outstandingRequests =
            FXCollections.observableList(
                facadeDAO.getServiceRequestsByStatus(ServiceRequest.RequestStatus.PROCESSING));
        outstandingRequests.addAll(
            FXCollections.observableList(
                facadeDAO.getServiceRequestsByStatus(ServiceRequest.RequestStatus.UNASSIGNED)));
        filter(
            outstandingRequests,
            false,
            facadeDAO.getEmployeeByID(filterBox.getSelectionModel().getSelectedItem()));
      }
    } else if (handlerSelect.isSelected()) {
      if (all.isSelected()) {
        requests = FXCollections.observableList(facadeDAO.getAllServiceRequests());
        filter(
            requests,
            true,
            facadeDAO.getEmployeeByID(filterBox.getSelectionModel().getSelectedItem()));
      } else {
        outstandingRequests =
            FXCollections.observableList(
                facadeDAO.getServiceRequestsByStatus(ServiceRequest.RequestStatus.PROCESSING));
        outstandingRequests.addAll(
            FXCollections.observableList(
                facadeDAO.getServiceRequestsByStatus(ServiceRequest.RequestStatus.UNASSIGNED)));
        filter(
            outstandingRequests,
            true,
            facadeDAO.getEmployeeByID(filterBox.getSelectionModel().getSelectedItem()));
      }
    } else {
      createTable();
      createOutstandingTable();
    }
  }

  public void filter(ObservableList<ServiceRequest> reqs, Boolean handler, Employee emp) {
    ObservableList<ServiceRequest> filteredRequests = FXCollections.observableArrayList();

    for (ServiceRequest req : reqs) {
      if (handler) {
        if (req.getHandler().equals(emp)) {
          filteredRequests.add(req);
        }
      } else {
        if (req.getIssuer().equals(emp)) {
          filteredRequests.add(req);
        }
      }
    }

    if (all.isSelected()) {
      tableContainer.getItems().clear();
      tableContainer.setItems(filteredRequests);
    } else {
      outstandingTable.getItems().clear();
      outstandingTable.setItems(filteredRequests);
    }
  }

  public void setEmployee(ActionEvent actionEvent) {
    if (tableContainer.getSelectionModel().getSelectedItem() == null
        || employeeBox.getValue() == null) {
      System.out.println("nope");
    } else {
      ServiceRequest handler = tableContainer.getSelectionModel().getSelectedItem();
      handler.setHandler(facadeDAO.getEmployeeByID(employeeBox.getValue()));
      handler.setStatus(ServiceRequest.RequestStatus.getRequestStatusByString("PROCESSING"));
      facadeDAO.updateServiceRequest(handler);
      createTable();
    }
  }

  public void issuerFilter(ActionEvent actionEvent) {
    handlerSelect.setSelected(false);
    createTable();
    createOutstandingTable();
    filterBox.getSelectionModel().clearSelection();
  }

  public void handlerFilter(ActionEvent actionEvent) {
    issuerSelect.setSelected(false);
    createTable();
    createOutstandingTable();
    filterBox.getSelectionModel().clearSelection();
  }

  /*public void clearASRItems(Event event) {
    handlerSelect.setSelected(false);
    issuerSelect.setSelected(false);
    createTable();
    createOutstandingTable();
    filterBox.getSelectionModel().clearSelection();
  }

  public void clearOSRItems(Event event) {
    handlerSelect.setSelected(false);
    issuerSelect.setSelected(false);
    createTable();
    createOutstandingTable();
    filterBox.getSelectionModel().clearSelection();
  }*/

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
}
