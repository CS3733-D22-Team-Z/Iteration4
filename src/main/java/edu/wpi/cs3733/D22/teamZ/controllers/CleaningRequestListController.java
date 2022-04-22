package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.CleaningRequest;
import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CleaningRequestListController implements Initializable, IMenuAccess {
  // Main table
  @FXML public TableView<CleaningRequest> tableContainer;
  @FXML private TableColumn<CleaningRequest, String> idCol;
  @FXML private TableColumn<CleaningRequest, String> typeCol;
  @FXML private TableColumn<CleaningRequest, Employee> issuerCol;
  @FXML private TableColumn<CleaningRequest, Employee> handlerCol;
  @FXML private TableColumn<CleaningRequest, ServiceRequest.RequestStatus> statusCol;
  @FXML private TableColumn<CleaningRequest, Location> locationCol;

  private final String toCleaningRequest = "views/CleaningRequest.fxml";

  protected MenuController menu;

  // Changes per every implementation
  // The name of the page that will be displayed in the bottom menu bar.
  protected String menuName;

  private ObservableList<CleaningRequest> requests;

  // Database object
  private FacadeDAO facadeDAO;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Cleaning Request List Page";
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    facadeDAO = FacadeDAO.getInstance();
    try {
      createTable();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /** creates table of all cleaning requests */
  public void createTable() throws SQLException {
    tableContainer.refresh();
    idCol.setCellValueFactory(new PropertyValueFactory<CleaningRequest, String>("requestID"));
    typeCol.setCellValueFactory(new PropertyValueFactory<CleaningRequest, String>("type"));
    issuerCol.setCellValueFactory(new PropertyValueFactory<CleaningRequest, Employee>("issuer"));
    handlerCol.setCellValueFactory(new PropertyValueFactory<CleaningRequest, Employee>("handler"));
    statusCol.setCellValueFactory(
        new PropertyValueFactory<CleaningRequest, ServiceRequest.RequestStatus>("status"));
    locationCol.setCellValueFactory(
        new PropertyValueFactory<CleaningRequest, Location>("targetLocation"));
    requests = FXCollections.observableList(facadeDAO.getAllCleaningRequests());
    tableContainer.setItems(requests);
  }

  public void refreshClicked(ActionEvent event) {
    try {
      createTable();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /** EXPORT to CSV button is clicked */
  public void exportToCSV(ActionEvent actionEvent) {
    FileChooser fileChooser = new FileChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    fileChooser.setTitle("Enter a .csv file...");
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);

    File defaultFile = facadeDAO.getDefaultCleaningReqCSVPath();
    if (defaultFile.isDirectory()) {
      fileChooser.setInitialDirectory(defaultFile);
    } else {
      fileChooser.setInitialDirectory(defaultFile.getParentFile());
      fileChooser.setInitialFileName(defaultFile.getName());
    }

    File file = fileChooser.showSaveDialog(stage);

    if (file != null) {
      facadeDAO.exportCleaningReqToCSV(file);
    }
  }
}
