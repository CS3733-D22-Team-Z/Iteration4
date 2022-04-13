package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ServiceRequestPageController implements Initializable, IMenuAccess {
  // Button that re-fetches requests and refreshes table.
  @FXML private MFXButton refresh;

  // Buttons to select the sorting/filter parameters.
  @FXML private MFXButton assigneeFilter;
  @FXML private MFXButton idFilter;
  @FXML private MFXButton deviceFilter;
  @FXML private MFXButton statusFilter;
  @FXML private MFXButton setEmpButton;

  // Drop-down box that selects which data type to filter by.
  @FXML private ChoiceBox<String> filterBox;
  @FXML private ChoiceBox<String> employeeBox;

  // Main table
  @FXML public TableView<ServiceRequest> tableContainer;
  @FXML private TableColumn<ServiceRequest, String> idCol;
  @FXML private TableColumn<ServiceRequest, ServiceRequest.RequestType> typeCol;
  @FXML private TableColumn<ServiceRequest, Employee> assigneeCol;
  @FXML private TableColumn<ServiceRequest, ServiceRequest.RequestStatus> statusCol;

  private final String toHomepageURL = "views/Homepage.fxml";

  private MenuController menu;

  // List of RequestRows currently being displayed on the table
  private ObservableList<ServiceRequest> requests;

  // Database object
  private FacadeDAO facadeDAO;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    facadeDAO = FacadeDAO.getInstance();
    // Create labels for field values

    // Fill the filter box with test data
    filterBox.getItems().addAll("Test 1", "Test 2", "Test 3");
    List<Employee> employees = facadeDAO.getAllEmployees();
    for (int i = 0; i < employees.size(); i++) {
      employeeBox.getItems().add(employees.get(i).getEmployeeID());
    }

    createTable();
  }

  public void createTable() {
    tableContainer.getItems().clear();
    idCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, String>("requestID"));
    typeCol.setCellValueFactory(
        new PropertyValueFactory<ServiceRequest, ServiceRequest.RequestType>("type"));
    assigneeCol.setCellValueFactory(new PropertyValueFactory<ServiceRequest, Employee>("handler"));
    statusCol.setCellValueFactory(
        new PropertyValueFactory<ServiceRequest, ServiceRequest.RequestStatus>("status"));
    requests = FXCollections.observableList(facadeDAO.getAllServiceRequests());
    tableContainer.setItems(requests);
  }

  // Called whenever one of the filter buttons are clicked.
  public void filterClicked(ActionEvent event) {
    MFXButton buttonPressed = (MFXButton) event.getTarget();
    System.out.println(buttonPressed.getText());
  }

  // Called whenever the refresh button is clicked.
  public void refreshClicked(ActionEvent event) {
    // System.out.println(refresh.getText());
    // employeeBox.getItems().clear();

    // Reload table
    createTable();
  }

  // Called whenever the filter select was set?
  public void filterSet(ActionEvent event) {
    System.out.println(filterBox.getSelectionModel().getSelectedItem());
  }

  public void setEmployee(ActionEvent actionEvent) {
    if (tableContainer.getSelectionModel().getSelectedItem() == null
        || employeeBox.getValue() == null) {
      System.out.println("nope");
    } else {
      ServiceRequest handler = tableContainer.getSelectionModel().getSelectedItem();
      handler.setHandler(facadeDAO.getEmployeeByID(employeeBox.getValue()));
      facadeDAO.updateServiceRequest(handler);
      createTable();
    }
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
}
