package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.DashboardEquipment;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class UpperFloorsDashboardController implements IMenuAccess {

  private FacadeDAO dao = FacadeDAO.getInstance();
  private MenuController menu;

  @FXML private Button lowerLevelButton;

  private ObservableList<DashboardEquipment> data;
  @FXML private TableView upperFloor1Table;
  @FXML private TableView upperFloor2Table;
  @FXML private TableView upperFloor3Table;
  @FXML private TableView upperFloor4Table;
  @FXML private TableView upperFloor5Table;
  @FXML private TableColumn idColumnUP5;
  @FXML private TableColumn locationColumnUP5;
  @FXML private TableColumn idColumnUP1;
  @FXML private TableColumn locationColumnUP1;
  @FXML private TableColumn idColumnUP2;
  @FXML private TableColumn locationColumnUP2;
  @FXML private TableColumn idColumnUP3;
  @FXML private TableColumn locationColumnUP3;
  @FXML private TableColumn idColumnUP4;
  @FXML private TableColumn locationColumnUP4;

  private final String toLowerLevel = "edu/wpi/cs3733/D22/teamZ/views/LowerLevelsDashboard.fxml";

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Dashboard";
  }

  @FXML
  public void toLowerLevelDashboard(ActionEvent event) throws IOException {
    menu.load(toLowerLevel);
  }

  @FXML
  public void initialize() {
    createTableUP1();
    createTableUP2();
    createTableUP3();
    createTableUP4();
    createTableUP5();
  }

  public void createTableUP1() {
    upperFloor1Table.getItems().clear();
    idColumnUP1.setCellValueFactory(
        (new PropertyValueFactory<DashboardEquipment, String>("equipmentID")));
    locationColumnUP1.setCellValueFactory(
        new PropertyValueFactory<DashboardEquipment, String>("locationNodeType"));
    createGenericTable("1");
    upperFloor1Table.setItems(data);
  }

  public void createTableUP2() {
    upperFloor2Table.getItems().clear();
    idColumnUP2.setCellValueFactory(
        (new PropertyValueFactory<DashboardEquipment, String>("equipmentID")));
    locationColumnUP2.setCellValueFactory(
        new PropertyValueFactory<DashboardEquipment, String>("locationNodeType"));
    createGenericTable("2");
    upperFloor2Table.setItems(data);
  }

  public void createTableUP3() {
    upperFloor3Table.getItems().clear();
    idColumnUP3.setCellValueFactory(
        (new PropertyValueFactory<DashboardEquipment, String>("equipmentID")));
    locationColumnUP3.setCellValueFactory(
        new PropertyValueFactory<DashboardEquipment, String>("locationNodeType"));
    createGenericTable("3");
    upperFloor3Table.setItems(data);
  }

  public void createTableUP4() {
    upperFloor4Table.getItems().clear();
    idColumnUP4.setCellValueFactory(
        (new PropertyValueFactory<DashboardEquipment, String>("equipmentID")));
    locationColumnUP4.setCellValueFactory(
        new PropertyValueFactory<DashboardEquipment, String>("locationNodeType"));
    createGenericTable("4");
    upperFloor4Table.setItems(data);
  }

  public void createTableUP5() {
    upperFloor5Table.getItems().clear();
    idColumnUP5.setCellValueFactory(
        (new PropertyValueFactory<DashboardEquipment, String>("equipmentID")));
    locationColumnUP5.setCellValueFactory(
        new PropertyValueFactory<DashboardEquipment, String>("locationNodeType"));
    createGenericTable("5");
    upperFloor5Table.setItems(data);
  }

  public void createGenericTable(String floor) {
    List<DashboardEquipment> dashboardEquipmentList = new ArrayList<>();
    for (MedicalEquipment tempMedEquip : dao.getAllMedicalEquipmentByFloor(floor)) {
      dashboardEquipmentList.add(new DashboardEquipment(tempMedEquip));
    }
    data = FXCollections.observableList(dashboardEquipmentList);
  }
}
