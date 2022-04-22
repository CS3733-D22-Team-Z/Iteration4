package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EmployeeController implements IMenuAccess, Initializable {

  private final String toHomePageURL = "edu/wpi/cs3733/D22/teamZ/views/Homepage.fxml";
  private ObservableList<Employee> data;

  @FXML private MFXButton editEmp;
  @FXML private MFXButton deleteEmp;
  @FXML private MFXButton addSubmit;
  @FXML private TableView<Employee> employeeTable;
  @FXML private TableColumn<Employee, String> IDColumn;
  @FXML private TableColumn<Employee, String> nameColumn;
  @FXML private TableColumn<Employee, Employee.AccessType> accessColumn;
  @FXML private TableColumn<Employee, String> usernameColumn;
  @FXML private Pane addEmployeePane;
  @FXML private MFXTextField addEmployeeName;
  @FXML private MFXTextField addEmployeeUsername;
  @FXML private ChoiceBox addEmployeeAccessType;
  @FXML private Text fillFields;
  @FXML private Text editFields;

  private MenuController menu;
  private FacadeDAO facadeDAO;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    facadeDAO = FacadeDAO.getInstance();
    editEmp.setDisable(true);
    deleteEmp.setDisable(true);
    addEmployeePane.setVisible(false);
    fillFields.setVisible(false);

    createTable();
    addEmployeeAccessType.getItems().addAll("ADMIN", "DOCTOR", "NURSE");
  }

  /** Populate the table with the current database */
  public void createTable() {
    IDColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeeID"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
    accessColumn.setCellValueFactory(
        new PropertyValueFactory<Employee, Employee.AccessType>("accesstype"));
    usernameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("username"));
    data = FXCollections.observableList(facadeDAO.getAllEmployees());
    employeeTable.setItems(data);
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  public String getMenuName() {
    return "Employee";
  }

  public void toHomePage(ActionEvent event) throws IOException {
    menu.load(toHomePageURL);
  }

  /**
   * set everything to how it starts when you first open the page
   *
   * @param actionEvent the event trigger
   */
  public void refresh(ActionEvent actionEvent) {
    editEmp.setDisable(true);
    deleteEmp.setDisable(true);
    createTable();
  }

  /**
   * Export to CSV via file director
   *
   * @param actionEvent the button event that triggers this method
   */
  public void exportToCSV(ActionEvent actionEvent) {
    FileChooser fileChooser = new FileChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    fileChooser.setTitle("Enter a .csv file...");
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);

    File defaultFile = facadeDAO.getDefaultEmployeeCSVPath();
    if (defaultFile.isDirectory()) {
      fileChooser.setInitialDirectory(defaultFile);
    } else {
      fileChooser.setInitialDirectory(defaultFile.getParentFile());
      fileChooser.setInitialFileName(defaultFile.getName());
    }

    File file = fileChooser.showSaveDialog(stage);

    if (file != null) {
      facadeDAO.exportEmployeesToCSV(file);
      createTable();
    }
  }

  /**
   * Import from CSV via file director
   *
   * @param actionEvent The button event that triggers this method
   */
  public void importFromCSV(ActionEvent actionEvent) {
    FileChooser fileChooser = new FileChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    fileChooser.setTitle("Enter a .csv file...");
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);

    File defaultFile = facadeDAO.getDefaultEmployeeCSVPath();
    if (defaultFile.isDirectory()) {
      fileChooser.setInitialDirectory(defaultFile);
    } else {
      fileChooser.setInitialDirectory(defaultFile.getParentFile());
      fileChooser.setInitialFileName(defaultFile.getName());
    }

    File file = fileChooser.showOpenDialog(stage);

    if (file != null) {
      facadeDAO.importEmployeesFromCSV(file);
      createTable();
    }
  }

  /**
   * modify popup for Adding New Employee
   *
   * @param actionEvent The button event that triggers this method
   */
  public void addEmployee(ActionEvent actionEvent) {
    addEmployeeName.clear();
    addEmployeeUsername.clear();
    addEmployeeAccessType.setValue(null);
    fillFields.setVisible(false);
    editFields.setText("Add Employee");
    addEmployeePane.setVisible(true);
  }

  /**
   * submit button for both adding new employee and editing employee
   *
   * @param actionEvent The event that triggers this method
   */
  public void submitNewEmployee(ActionEvent actionEvent) {
    if (!addEmployeeName.getText().equals("")
        && !addEmployeeUsername.getText().equals("")
        && !(addEmployeeAccessType.getValue() == null)) {
      if (editFields.getText().equals("Add Employee")) {
        newID(addEmployeeAccessType.getValue().toString());
      } else {
        String employeeID = employeeTable.getSelectionModel().getSelectedItem().getEmployeeID();
        facadeDAO.deleteEmployee(employeeTable.getSelectionModel().getSelectedItem());
        submitEmployee(employeeID);
        addEmployeePane.setVisible(false);
        editEmp.setDisable(true);
        deleteEmp.setDisable(true);
      }
      createTable();
      editEmp.setDisable(true);
      deleteEmp.setDisable(true);
    } else {
      fillFields.setVisible(true);
    }
  }

  /**
   * submitting an employee to a database
   *
   * @param employeeID the employee's id
   * @return boolean true if successfully submits a new employee
   */
  public boolean submitEmployee(String employeeID) {
    Employee temp = new Employee();
    temp.setName(addEmployeeName.getText());
    temp.setUsername(addEmployeeUsername.getText());
    temp.setAccesstype(
        Employee.AccessType.getRequestTypeByString(addEmployeeAccessType.getValue().toString()));
    temp.setEmployeeID(employeeID);
    temp.setPassword("password");
    if (facadeDAO.addEmployee(temp)) {
      createTable();
      addEmployeeName.clear();
      addEmployeeUsername.clear();
      addEmployeeAccessType.setValue(null);
      fillFields.setVisible(false);
      return true;
    }
    return false;
  }

  /**
   * New employee ID that doesn't already exist
   *
   * @param accessType the access type for the employee that will eventually be created
   */
  public void newID(String accessType) {
    String ID = accessType.toLowerCase();
    Random rand = new Random();
    int int_random = rand.nextInt(9) + 1;
    ID += int_random;
    while (!submitEmployee(ID)) {
      int_random = rand.nextInt(10);
      ID += int_random;
    }
  }

  /**
   * clear fields
   *
   * @param actionEvent The event that triggers this method
   */
  public void clearNewEmployee(ActionEvent actionEvent) {
    addEmployeeName.clear();
    addEmployeeUsername.clear();
    addEmployeeAccessType.setValue(null);
  }

  /**
   * Sets edit employee pop up with fields filled in
   *
   * @param actionEvent The button event that triggers this method
   */
  public void editEmployee(ActionEvent actionEvent) {
    editFields.setText("Edit Location");
    Employee temp = employeeTable.getSelectionModel().getSelectedItem();
    addEmployeeName.setText(temp.getName());
    addEmployeeUsername.setText(temp.getUsername());
    addEmployeeAccessType.setValue(temp.getAccesstype());
    fillFields.setVisible(false);
    addEmployeePane.setVisible(true);
  }

  /**
   * deletes employee and refreshes table
   *
   * @param actionEvent The event that triggers this method
   */
  public void deleteEmployee(ActionEvent actionEvent) {
    facadeDAO.deleteEmployee(employeeTable.getSelectionModel().getSelectedItem());
    createTable();
  }

  /**
   * Buttons are enabled when an employee is selected
   *
   * @param mouseEvent The mouse event that triggers this method
   */
  public void buttonsAppear(MouseEvent mouseEvent) {
    editEmp.setDisable(false);
    deleteEmp.setDisable(false);
  }

  /**
   * close popup
   *
   * @param mouseEvent The mouse event that triggers this method
   */
  public void closeAddEmployee(MouseEvent mouseEvent) {
    addEmployeePane.setVisible(false);
    addEmployeeName.clear();
    addEmployeeUsername.clear();
    addEmployeeAccessType.setValue(null);
    fillFields.setVisible(false);
  }
}
