package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.DashboardEquipment;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import edu.wpi.cs3733.D22.teamZ.observers.MedicalEquipmentObserver;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class UpperFloorsDashboardController implements IMenuAccess, MedicalEquipmentObserver {

  private FacadeDAO dao = FacadeDAO.getInstance();
  private MenuController menu;
  private String menuName;

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

  @FXML private Label LL2Label;
  @FXML private Label LL1Label;
  @FXML private Label F1Label;
  @FXML private Label F2Label;
  @FXML private Label F3Label;
  @FXML private Label F4Label;
  @FXML private Label F5Label;

  private final String goBack = "edu/wpi/cs3733/D22/teamZ/views/Homepage.fxml";

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
    menu.load(goBack);
  }

  @FXML
  public void initialize() {
    dao.getAllMedicalEquipment().get(0).registerObserver(this);
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
  }

  private void createBarUP5Dirty() {
    floor5Dirty.setStyle("-fx-accent: red;");
    // Set default values
    double dirty = 0;
    double total = 0;
    // For each medical equipment on the floor, add one to total and add to dirty if it's dirty
    for (MedicalEquipment me : dao.getAllMedicalEquipmentByFloor("5")) {
      total++;
      if (me.getStatus().equals(MedicalEquipment.EquipmentStatus.DIRTY)) dirty++;
    }
    // Set progress
    double dirtyValue = dirty / total;
    floor5Dirty.setProgress(dirtyValue);
    F5Label.setText((int) dirty + " / " + (int) total);
  }
  // None of the equipment has "Clean" as the status, so nothing shows up under the clean progress
  // bar
  private void createBarUP5Clean() {
    floor5Clean.setStyle("-fx-accent: green;");
    // Set default values
    double clean = 0;
    double total = 0;
    // For each medical equipment on the floor, add one to total and add to clean if it's clean
    for (MedicalEquipment me : dao.getAllMedicalEquipmentByFloor("5")) {
      total++;
      if (me.getStatus().equals(MedicalEquipment.EquipmentStatus.CLEAN)) clean++;
    }
    // Set progress
    double cleanValue = clean / total;
    floor5Clean.setProgress(cleanValue);
  }

  private void createBarUP4Dirty() {
    floor4Dirty.setStyle("-fx-accent: red;");
    // Set default values
    double dirty = 0;
    double total = 0;
    // For each medical equipment on the floor, add one to total and add to dirty if it's dirty
    for (MedicalEquipment me : dao.getAllMedicalEquipmentByFloor("4")) {
      total++;
      if (me.getStatus().equals(MedicalEquipment.EquipmentStatus.DIRTY)) dirty++;
    }
    // Set progress
    double dirtyValue = dirty / total;
    floor4Dirty.setProgress(dirtyValue);
    F4Label.setText((int) dirty + " / " + (int) total);
  }

  private void createBarUP4Clean() {
    floor4Clean.setStyle("-fx-accent: green;");
    // Set default values
    double clean = 0;
    double total = 0;
    // For each medical equipment on the floor, add one to total and add to clean if it's clean
    for (MedicalEquipment me : dao.getAllMedicalEquipmentByFloor("4")) {
      total++;
      if (me.getStatus().equals(MedicalEquipment.EquipmentStatus.CLEAN)) clean++;
    }
    // Set progress
    double cleanValue = clean / total;
    floor4Clean.setProgress(cleanValue);
  }

  private void createBarUP3Dirty() {
    floor3Dirty.setStyle("-fx-accent: red;");
    // Set default values
    double dirty = 0;
    double total = 0;
    // For each medical equipment on the floor, add one to total and add to dirty if it's dirty
    for (MedicalEquipment me : dao.getAllMedicalEquipmentByFloor("3")) {
      total++;
      if (me.getStatus().equals(MedicalEquipment.EquipmentStatus.DIRTY)) dirty++;
    }
    // Set progress
    floor3Dirty.setProgress(dirty / total);
    F3Label.setText((int) dirty + " / " + (int) total);
  }

  private void createBarUP3Clean() {
    floor3Clean.setStyle("-fx-accent: green;");
    // Set default values
    double clean = 0;
    double total = 0;
    // For each medical equipment on the floor, add one to total and add to clean if it's clean
    for (MedicalEquipment me : dao.getAllMedicalEquipmentByFloor("3")) {
      total++;
      if (me.getStatus().equals(MedicalEquipment.EquipmentStatus.CLEAN)) clean++;
    }
    // Set progress
    floor3Clean.setProgress(clean / total);
  }

  private void createBarUP2Dirty() {
    floor2Dirty.setStyle("-fx-accent: red;");
    // Set default values
    double dirty = 0;
    double total = 0;
    // For each medical equipment on the floor, add one to total and add to dirty if it's dirty
    for (MedicalEquipment me : dao.getAllMedicalEquipmentByFloor("2")) {
      total++;
      if (me.getStatus().equals(MedicalEquipment.EquipmentStatus.DIRTY)) dirty++;
    }
    // Set progress
    floor2Dirty.setProgress(dirty / total);
    F2Label.setText((int) dirty + " / " + (int) total);
  }

  private void createBarUP2Clean() {
    floor2Clean.setStyle("-fx-accent: green;");
    // Set default values
    double clean = 0;
    double total = 0;
    // For each medical equipment on the floor, add one to total and add to clean if it's clean
    for (MedicalEquipment me : dao.getAllMedicalEquipmentByFloor("2")) {
      total++;
      if (me.getStatus().equals(MedicalEquipment.EquipmentStatus.CLEAN)) clean++;
    }
    // Set progress
    floor2Clean.setProgress(clean / total);
  }

  private void createBarUP1Dirty() {
    floor1Dirty.setStyle("-fx-accent: red;");
    // Set default values
    double dirty = 0;
    double total = 0;
    // For each medical equipment on the floor, add one to total and add to dirty if it's dirty
    for (MedicalEquipment me : dao.getAllMedicalEquipmentByFloor("1")) {
      total++;
      if (me.getStatus().equals(MedicalEquipment.EquipmentStatus.DIRTY)) dirty++;
    }
    // Set progress
    floor1Dirty.setProgress(dirty / total);
    F1Label.setText((int) dirty + " / " + (int) total);
  }

  private void createBarUP1Clean() {
    floor1Clean.setStyle("-fx-accent: green;");
    // Set default values
    double clean = 0;
    double total = 0;
    // For each medical equipment on the floor, add one to total and add to clean if it's clean
    for (MedicalEquipment me : dao.getAllMedicalEquipmentByFloor("1")) {
      total++;
      if (me.getStatus().equals(MedicalEquipment.EquipmentStatus.CLEAN)) clean++;
    }
    // Set progress
    floor1Clean.setProgress(clean / total);
  }

  private void createBarLL1Dirty() {
    lowerLevel1Dirty.setStyle("-fx-accent: red;");
    // Set default values
    double dirty = 0;
    double total = 0;
    // For each medical equipment on the floor, add one to total and add to dirty if it's dirty
    for (MedicalEquipment me : dao.getAllMedicalEquipmentByFloor("LL1")) {
      total++;
      if (me.getStatus().equals(MedicalEquipment.EquipmentStatus.DIRTY)) dirty++;
    }
    // Set progress
    lowerLevel1Dirty.setProgress(dirty / total);
    LL1Label.setText((int) dirty + " / " + (int) total);
  }

  private void createBarLL1Clean() {
    lowerLevel1Clean.setStyle("-fx-accent: green;");
    // Set default values
    double clean = 0;
    double total = 0;
    // For each medical equipment on the floor, add one to total and add to clean if it's clean
    for (MedicalEquipment me : dao.getAllMedicalEquipmentByFloor("LL1")) {
      total++;
      if (me.getStatus().equals(MedicalEquipment.EquipmentStatus.CLEAN)) clean++;
    }
    // Set progress
    lowerLevel1Clean.setProgress(clean / total);
  }

  private void createBarLL2Dirty() {
    lowerLevel2Dirty.setStyle("-fx-accent: red;");
    // Set default values
    double dirty = 0;
    double total = 0;
    // For each medical equipment on the floor, add one to total and add to dirty if it's dirty
    for (MedicalEquipment me : dao.getAllMedicalEquipmentByFloor("LL2")) {
      total++;
      if (me.getStatus().equals(MedicalEquipment.EquipmentStatus.DIRTY)) dirty++;
    }
    // Set progress
    lowerLevel2Dirty.setProgress(dirty / total);
    LL2Label.setText((int) dirty + " / " + (int) total);
  }

  private void createBarLL2Clean() {
    lowerLevel2Clean.setStyle("-fx-accent: green;");
    // Set default values
    double clean = 0;
    double total = 0;
    // For each medical equipment on the floor, add one to total and add to clean if it's clean
    for (MedicalEquipment me : dao.getAllMedicalEquipmentByFloor("LL2")) {
      total++;
      if (me.getStatus().equals(MedicalEquipment.EquipmentStatus.CLEAN)) clean++;
    }
    // Set progress
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

  @Override
  public void update(MedicalEquipment equipment) {
    initialize();
  }
}
