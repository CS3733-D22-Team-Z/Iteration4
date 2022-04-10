package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class EmployeeController implements IMenuAccess, Initializable {

  private final String toHomePageURL = "edu/wpi/cs3733/D22/teamZ/views/HomePage.fxml";
  private MenuController menu;
  private List<Employee> employeeList;
  private ObservableList<Employee> data;

  @FXML private TableView<Employee> employeeTable;
  @FXML private TableColumn<Employee, String> IDColumn;
  @FXML private TableColumn<Employee, String> nameColumn;
  @FXML private TableColumn<Employee, Employee.AccessType> accessColumn;

  private FacadeDAO facadeDAO;

  public EmployeeController() {
    // Create new database object
    facadeDAO = new FacadeDAO();

    data = FXCollections.observableList(facadeDAO.getAllEmployees());
  }

  public void loadRequests() {
    employeeList = facadeDAO.getAllEmployees();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    IDColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeeID"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
    accessColumn.setCellValueFactory(
        new PropertyValueFactory<Employee, Employee.AccessType>("accessType"));

    employeeTable.setItems(data);
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  public void toHomePage(ActionEvent event) throws IOException {
    menu.load(toHomePageURL);
  }
}
