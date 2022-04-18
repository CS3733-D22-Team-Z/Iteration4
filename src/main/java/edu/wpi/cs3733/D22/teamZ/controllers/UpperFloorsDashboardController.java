package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.DashboardEquipment;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class UpperFloorsDashboardController implements IMenuAccess {

  private FacadeDAO dao = FacadeDAO.getInstance();
  private MenuController menu;
  private String menuName;

  @FXML private Button lowerLevelButton;

  private ObservableList<DashboardEquipment> data;
  @FXML private AnchorPane root;
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
  @FXML private ProgressBar floor5Dirty;
  @FXML private ProgressBar floor5Clean;
  @FXML private ProgressBar floor4Dirty;
  @FXML private ProgressBar floor4Clean;
  @FXML private ProgressBar floor3Dirty;
  @FXML private ProgressBar floor3Clean;
  @FXML private ProgressBar floor2Dirty;
  @FXML private ProgressBar floor2Clean;
  @FXML private ProgressBar floor1Dirty;
  @FXML private ProgressBar floor1Clean;
  @FXML private ProgressBar lowerLevel1Dirty;
  @FXML private ProgressBar lowerLevel1Clean;
  @FXML private ProgressBar lowerLevel2Dirty;
  @FXML private ProgressBar lowerLevel2Clean;
  @FXML private MFXButton floor5Button;
  @FXML private MFXButton floor4Button;
  @FXML private MFXButton floor3Button;
  @FXML private MFXButton floor2Button;
  @FXML private MFXButton floor1Button;
  @FXML private MFXButton lowerLevel1Button;
  @FXML private MFXButton lowerLevel2Button;

  private FacadeDAO database;

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
    database = FacadeDAO.getInstance();
    createTableUP1();
    createTableUP2();
    createTableUP3();
    createTableUP4();
    createTableUP5();
    createBarLL2Dirty();
    createBarLL2Clean();
    createBarLL1Dirty();
    createBarLL1Clean();
    createBarUP1Dirty();
    createBarUP1Clean();
    createBarUP2Dirty();
    createBarUP2Clean();
    createBarUP3Dirty();
    createBarUP3Clean();
    createBarUP4Dirty();
    createBarUP4Clean();
    createBarUP5Dirty();
    createBarUP5Clean();

    /*// Create a new observer for each existing medical equipment
    DirtyBedObserver dashObserver = new DirtyBedObserver(this);
    dashObserver.setSubjects(dao.getAllMedicalEquipment());
    // dao.addMedEquipObserver(dashObserver);

    // When the root is removed (its parent is changed), then remove all observers.
    root.parentProperty().addListener(parent -> dashObserver.removeSubjects());*/
  }

  private void createBarUP5Dirty() {
    floor5Dirty.setStyle("-fx-accent: red;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("5").size();
    double dirty = FacadeDAO.getInstance().countDirtyEquipmentByFloor("5");
    double dirtyValue = dirty / total;
    floor5Dirty.setProgress(dirtyValue);
  }
  // None of the equipment has "Clean" as the status, so nothing shows up under the clean progress
  // bar
  private void createBarUP5Clean() {
    floor5Clean.setStyle("-fx-accent: green;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("5").size();
    double clean = FacadeDAO.getInstance().countCleanEquipmentByFloor("5");
    double cleanValue = clean / total;
    floor5Clean.setProgress(cleanValue);
  }

  private void createBarUP4Dirty() {
    floor4Dirty.setStyle("-fx-accent: red;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("4").size();
    double dirty = FacadeDAO.getInstance().countDirtyEquipmentByFloor("4");
    double dirtyValue = dirty / total;
    floor4Clean.setProgress(dirtyValue);
  }

  private void createBarUP4Clean() {
    floor4Clean.setStyle("-fx-accent: green;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("4").size();
    double clean = FacadeDAO.getInstance().countCleanEquipmentByFloor("4");
    double cleanValue = clean / total;
    floor4Clean.setProgress(cleanValue);
  }

  private void createBarUP3Dirty() {
    floor3Dirty.setStyle("-fx-accent: red;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("3").size();
    double dirty = FacadeDAO.getInstance().countDirtyEquipmentByFloor("3");
    floor3Dirty.setProgress(dirty / total);
  }

  private void createBarUP3Clean() {
    floor3Clean.setStyle("-fx-accent: green;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("3").size();
    double clean = FacadeDAO.getInstance().countCleanEquipmentByFloor("3");
    floor3Clean.setProgress(clean / total);
  }

  private void createBarUP2Dirty() {
    floor2Dirty.setStyle("-fx-accent: red;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("2").size();
    double dirty = FacadeDAO.getInstance().countDirtyEquipmentByFloor("2");
    floor2Dirty.setProgress(dirty / total);
  }

  private void createBarUP2Clean() {
    floor2Clean.setStyle("-fx-accent: green;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("2").size();
    double clean = FacadeDAO.getInstance().countCleanEquipmentByFloor("2");
    floor2Clean.setProgress(clean / total);
  }

  private void createBarUP1Dirty() {
    floor1Dirty.setStyle("-fx-accent: red;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("1").size();
    double dirty = FacadeDAO.getInstance().countDirtyEquipmentByFloor("1");
    floor1Dirty.setProgress(dirty / total);
  }

  private void createBarUP1Clean() {
    floor1Clean.setStyle("-fx-accent: green;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("1").size();
    double clean = FacadeDAO.getInstance().countCleanEquipmentByFloor("1");
    floor1Clean.setProgress(clean / total);
  }

  private void createBarLL1Dirty() {
    lowerLevel1Dirty.setStyle("-fx-accent: red;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("LL1").size();
    double dirty = FacadeDAO.getInstance().countDirtyEquipmentByFloor("LL1");
    lowerLevel1Dirty.setProgress(dirty / total);
  }

  private void createBarLL1Clean() {
    lowerLevel1Clean.setStyle("-fx-accent: green;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("LL1").size();
    double clean = FacadeDAO.getInstance().countCleanEquipmentByFloor("LL1");
    lowerLevel1Clean.setProgress(clean / total);
  }

  private void createBarLL2Dirty() {
    lowerLevel2Dirty.setStyle("-fx-accent: red;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("LL2").size();
    double dirty = FacadeDAO.getInstance().countDirtyEquipmentByFloor("LL2");
    lowerLevel2Dirty.setProgress(dirty / total);
  }

  private void createBarLL2Clean() {
    lowerLevel2Clean.setStyle("-fx-accent: green;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("LL2").size();
    double clean = FacadeDAO.getInstance().countCleanEquipmentByFloor("LL2");
    lowerLevel2Clean.setProgress(clean / total);
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

  public void toFloor5(ActionEvent actionEvent) {}

  public void toFloor4(ActionEvent actionEvent) {}

  public void toFloor3(ActionEvent actionEvent) {}

  public void toFloor2(ActionEvent actionEvent) {}

  public void toFloor1(ActionEvent actionEvent) {}

  public void toLowerLevel1(ActionEvent actionEvent) {}

  public void toLowerLevel2(ActionEvent actionEvent) {}
}
