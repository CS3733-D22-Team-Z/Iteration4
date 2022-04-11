package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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

  private final String toHomePageURL = "edu/wpi/cs3733/D22/teamZ/views/HomePage.fxml";
  private ObservableList<Employee> data;

  @FXML private MFXButton editEmp;
  @FXML private MFXButton deleteEmp;
  @FXML private MFXButton addSubmit;
  @FXML private TableView<Employee> employeeTable;
  @FXML private TableColumn<Employee, String> IDColumn;
  @FXML private TableColumn<Employee, String> nameColumn;
  @FXML private TableColumn<Employee, Employee.AccessType> accessColumn;
  @FXML private Pane addEmployeePane;
  @FXML private MFXTextField addEmployeeName;
  @FXML private MFXTextField addEmployeeID;
  @FXML private ChoiceBox addEmployeeAccessType;
  @FXML private Text fillFields;
  @FXML private Text editFields;

  private MenuController menu;
  private FacadeDAO facadeDAO;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    facadeDAO = new FacadeDAO();
    editEmp.setDisable(true);
    deleteEmp.setDisable(true);
    addEmployeePane.setVisible(false);
    fillFields.setVisible(false);

    createTable();
    addEmployeeAccessType.getItems().addAll("ADMIN", "DOCTOR", "NURSE");
  }

  public void createTable() {
    employeeTable.getItems().clear();
    IDColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("employeeID"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<Employee, String>("name"));
    accessColumn.setCellValueFactory(
        new PropertyValueFactory<Employee, Employee.AccessType>("accesstype"));
    data = FXCollections.observableList(facadeDAO.getAllEmployees());
    employeeTable.setItems(data);
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  public void toHomePage(ActionEvent event) throws IOException {
    menu.load(toHomePageURL);
  }

  public void refresh(ActionEvent actionEvent) {
    editEmp.setDisable(true);
    deleteEmp.setDisable(true);
    createTable();
  }

  public void exportToCSV(ActionEvent actionEvent) {
    FileChooser fileChooser = new FileChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    fileChooser.setTitle("Enter a .csv file...");
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);

    File file = fileChooser.showSaveDialog(stage);
    facadeDAO.exportEmployeesToCSV(file);
    createTable();
  }

  public void addEmployee(ActionEvent actionEvent) throws IOException {
    addEmployeeName.clear();
    addEmployeeID.clear();
    addEmployeeAccessType.setValue(null);
    fillFields.setVisible(false);
    editFields.setText("Add Employee");
    addEmployeePane.setVisible(true);
  }

  public void submitNewEmployee(ActionEvent actionEvent) throws IOException {
    if (!addEmployeeName.getText().equals("")
        && !addEmployeeID.getText().equals("")
        && !(addEmployeeAccessType.getValue() == null)) {
      if (editFields.getText().equals("Add Employee")) {
        addEmployee();
      } else {
        facadeDAO.deleteEmployee(employeeTable.getSelectionModel().getSelectedItem());
        addEmployee();
        addEmployeePane.setVisible(false);
        editEmp.setDisable(true);
        deleteEmp.setDisable(true);
      }
      createTable();
    } else {
      fillFields.setVisible(true);
    }
  }

  public boolean addEmployee() {
    Employee temp = new Employee();
    temp.setName(addEmployeeName.getText());
    temp.setEmployeeID(addEmployeeID.getText());
    temp.setAccesstype(
        Employee.AccessType.getRequestTypeByString(addEmployeeAccessType.getValue().toString()));
    temp.setUsername(addEmployeeID.getText());
    temp.setPassword("password");
    if (facadeDAO.addEmployee(temp)) {
      createTable();
      addEmployeeName.clear();
      addEmployeeID.clear();
      addEmployeeAccessType.setValue(null);
      fillFields.setVisible(false);
      return true;
    }
    return false;
  }

  public void clearNewEmployee(ActionEvent actionEvent) throws IOException {
    addEmployeeName.clear();
    addEmployeeID.clear();
    addEmployeeAccessType.setValue(null);
  }

  public void editEmployee(ActionEvent actionEvent) throws IOException {
    editFields.setText("Edit Location");
    Employee temp = employeeTable.getSelectionModel().getSelectedItem();
    addEmployeeName.setText(temp.getName());
    addEmployeeID.setText(temp.getEmployeeID());
    addEmployeeAccessType.setValue(temp.getAccesstype());
    fillFields.setVisible(false);
    addEmployeePane.setVisible(true);
  }

  public void deleteEmployee(ActionEvent actionEvent) {
    facadeDAO.deleteEmployee(employeeTable.getSelectionModel().getSelectedItem());
    createTable();
  }

  public void buttonsAppear(MouseEvent mouseEvent) {
    editEmp.setDisable(false);
    deleteEmp.setDisable(false);
  }

  public void closeAddEmployee(MouseEvent mouseEvent) {
    addEmployeePane.setVisible(false);
    addEmployeeName.clear();
    addEmployeeID.clear();
    addEmployeeAccessType.setValue(null);
    fillFields.setVisible(false);
  }
}
