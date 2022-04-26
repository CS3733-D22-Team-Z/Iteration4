package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
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
  @FXML private MFXButton finishRequestButton;

  // Labels
  @FXML private Label errorLabel;

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

  @FXML private Label textEmp;

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

    Employee.AccessType accessType = MenuController.getLoggedInUser().getAccesstype();
    if (!accessType.equals(Employee.AccessType.ADMIN)) {
      textEmp.setVisible(false);
      setEmpButton.setVisible(false);
      employeeBox.setVisible(false);
    }

    // Fill the filter box with test data
    List<Employee> employees = facadeDAO.getAllEmployees();
    filterBox.getItems().add(MenuController.getLoggedInUser().getEmployeeID() + ": (you)");
    for (int i = 0; i < employees.size(); i++) {
      employeeBox
          .getItems()
          .add(employees.get(i).getEmployeeID() + ": " + employees.get(i).getName());
      if (employees.get(i) != MenuController.getLoggedInUser()) {
        filterBox
            .getItems()
            .add(employees.get(i).getEmployeeID() + ": " + employees.get(i).getName());
      }
    }
    errorLabel.setVisible(false);
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

  /** creates table of all service requests */
  public void createTable() {
    tableContainer.refresh();
    idCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, String>("requestID"));
    typeCol.setCellValueFactory(
        new PropertyValueFactory<ServiceRequest, ServiceRequest.RequestType>("type"));
    assignedCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, Employee>("handler"));
    statusCol.setCellValueFactory(
        new PropertyValueFactory<ServiceRequest, ServiceRequest.RequestStatus>("status"));
    requests = FXCollections.observableList(facadeDAO.getAllServiceRequests());
    tableContainer.setItems(requests);
  }

  /** creates table of all outstanding service requests */
  public void createOutstandingTable() {
    outstandingTable.refresh();
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

  /** when refrsh button is clicked */
  // Called whenever the refresh button is clicked.
  public void refreshClicked(ActionEvent event) {
    // System.out.println(refresh.getText());
    // employeeBox.getItems().clear();

    // Reload table
    createTable();
    createOutstandingTable();
    issuerSelect.setSelected(false);
    handlerSelect.setSelected(false);
    filterBox.getSelectionModel().clearSelection();
    employeeBox.getSelectionModel().clearSelection();
  }

  /**
   * called when an employee is selected from drop down list determins which table we are on, which
   * employee is seleceted and which toggle button is selected
   */
  public void filterList(ActionEvent event) {
    if (filterBox.getSelectionModel().getSelectedItem() == null) {
      createTable();
      createOutstandingTable();
      return;
    }
    int end = filterBox.getSelectionModel().getSelectedItem().indexOf(":");
    if (issuerSelect.isSelected()) {
      if (all.isSelected()) {
        requests = FXCollections.observableList(facadeDAO.getAllServiceRequests());
        filter(
            requests,
            false,
            facadeDAO.getEmployeeByID(
                filterBox.getSelectionModel().getSelectedItem().substring(0, end)));
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
            facadeDAO.getEmployeeByID(
                filterBox.getSelectionModel().getSelectedItem().substring(0, end)));
      }
    } else if (handlerSelect.isSelected()) {
      if (all.isSelected()) {
        requests = FXCollections.observableList(facadeDAO.getAllServiceRequests());
        filter(
            requests,
            true,
            facadeDAO.getEmployeeByID(
                filterBox.getSelectionModel().getSelectedItem().substring(0, end)));
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
            facadeDAO.getEmployeeByID(
                filterBox.getSelectionModel().getSelectedItem().substring(0, end)));
      }
    } else {
      createTable();
      createOutstandingTable();
    }
  }

  /** helper function for filterList filters the lists and refreshes the table */
  public void filter(ObservableList<ServiceRequest> reqs, Boolean handler, Employee emp) {
    ObservableList<ServiceRequest> filteredRequests = FXCollections.observableArrayList();

    for (ServiceRequest req : reqs) {
      if (handler) {
        if (req.getHandler() != null && req.getHandler().equals(emp)) {
          filteredRequests.add(req);
        }
      } else {
        if (req.getIssuer().equals(emp)) {
          filteredRequests.add(req);
        }
      }
    }

    if (all.isSelected()) {
      tableContainer.refresh();
      tableContainer.setItems(filteredRequests);
    } else {
      outstandingTable.refresh();
      outstandingTable.setItems(filteredRequests);
    }
  }

  /** when set employee button is clicked sets employee to service request */
  public void setEmployee(ActionEvent actionEvent) {
    if (!(tableContainer.getSelectionModel().getSelectedItem() == null
        || employeeBox.getValue() == null)) {
      int end = employeeBox.getSelectionModel().getSelectedItem().indexOf(":");
      ServiceRequest handler = tableContainer.getSelectionModel().getSelectedItem();
      handler.setHandler(
          facadeDAO.getEmployeeByID(
              employeeBox.getSelectionModel().getSelectedItem().substring(0, end)));
      handler.setStatus(ServiceRequest.RequestStatus.getRequestStatusByString("PROCESSING"));
      facadeDAO.updateServiceRequest(handler);
      createTable();
      errorLabel.setVisible(false);
      createOutstandingTable();
      issuerSelect.setSelected(false);
      handlerSelect.setSelected(false);
      filterBox.getSelectionModel().clearSelection();
      employeeBox.getSelectionModel().clearSelection();
    } else {
      errorLabel.setVisible(true);
    }
  }

  /** when set employee button is clicked sets employee to service request */
  public void finishRequest(ActionEvent actionEvent) {
    if (!(tableContainer.getSelectionModel().getSelectedItem() == null)) {
      ServiceRequest req = tableContainer.getSelectionModel().getSelectedItem();
      if (!req.getStatus().equals(ServiceRequest.RequestStatus.PROCESSING)) {
        return;
      }
      req.setStatus(ServiceRequest.RequestStatus.getRequestStatusByString("DONE"));
      req.setClosed(LocalDateTime.now());
      facadeDAO.updateServiceRequest(req);
      createTable();
      createOutstandingTable();
      issuerSelect.setSelected(false);
      handlerSelect.setSelected(false);
      filterBox.getSelectionModel().clearSelection();
      employeeBox.getSelectionModel().clearSelection();
    } else {
      errorLabel.setVisible(true);
    }
  }

  /**
   * when issuer toggle button is clicked deselects handler button and disables any previous filter
   */
  public void issuerFilter(ActionEvent actionEvent) {
    handlerSelect.setSelected(false);
    createTable();
    createOutstandingTable();
    filterBox.getSelectionModel().clearSelection();
  }

  /**
   * when handler toggle button is clicked deselects issuer button and disables any previous filter
   */
  public void handlerFilter(ActionEvent actionEvent) {
    issuerSelect.setSelected(false);
    createTable();
    createOutstandingTable();
    filterBox.getSelectionModel().clearSelection();
  }

  /** EXPORT to CSV button is clicked */
  public void exportToCSV(ActionEvent actionEvent) {
    FileChooser fileChooser = new FileChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    fileChooser.setTitle("Enter a .csv file...");
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);

    File defaultFile = facadeDAO.getDefaultServiceRequestCSVPath();
    if (defaultFile.isDirectory()) {
      fileChooser.setInitialDirectory(defaultFile);
    } else {
      fileChooser.setInitialDirectory(defaultFile.getParentFile());
      fileChooser.setInitialFileName(defaultFile.getName());
    }

    File file = fileChooser.showSaveDialog(stage);

    if (file != null) {
      facadeDAO.exportServiceRequestsToCSV(file);
    }
  }
}
