package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.DashAlert;
import edu.wpi.cs3733.D22.teamZ.entity.DashboardEquipment;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import edu.wpi.cs3733.D22.teamZ.helpers.PopupLoader;
import edu.wpi.cs3733.D22.teamZ.observers.DashboardBedAlertObserver;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;

public class UpperFloorsDashboardController implements IMenuAccess {

  private final String dashboardAlert =
      "M11 15h2v2h-2v-2zm0-8h2v6h-2V7zm.99-5C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8z";

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
  @FXML private Region dashRegion5;
  @FXML private Region dashRegion4;
  @FXML private Region dashRegion3;
  @FXML private Region dashRegion2;
  @FXML private Region dashRegion1;
  @FXML private Region dashRegionLL1;
  @FXML private Region dashRegionLL2;
  @FXML private Label floor5DirtyLabel;
  @FXML private Label floor5CleanLabel;
  @FXML private Label floor4DirtyLabel;
  @FXML private Label floor4CleanLabel;
  @FXML private Label floor3DirtyLabel;
  @FXML private Label floor3CleanLabel;
  @FXML private Label floor2DirtyLabel;
  @FXML private Label floor2CleanLabel;
  @FXML private Label floor1DirtyLabel;
  @FXML private Label floor1CleanLabel;
  @FXML private Label lowerLevel1DirtyLabel;
  @FXML private Label lowerLevel1CleanLabel;
  @FXML private Label lowerLevel2DirtyLabel;
  @FXML private Label lowerLevel2CleanLabel;
  @FXML private VBox floor5Container;
  @FXML private VBox floor4Container;
  @FXML private VBox floor3Container;
  @FXML private VBox floor2Container;
  @FXML private VBox floor1Container;
  @FXML private VBox LL1Container;
  @FXML private VBox LL2Container;
  @FXML private Label floor5TotalLabel;
  @FXML private Label floor4TotalLabel;
  @FXML private Label floor3TotalLabel;
  @FXML private Label floor2TotalLabel;
  @FXML private Label floor1TotalLabel;
  @FXML private Label lowerLevel1TotalLabel;
  @FXML private Label lowerLevel2TotalLabel;

  private FacadeDAO database;

  private Pane currentPopup = null;

  private List<DashAlert> alerts;

  // private final String toLowerLevel = "edu/wpi/cs3733/D22/teamZ/views/LowerLevelsDashboard.fxml";
  private final String toLocationURL = "edu/wpi/cs3733/D22/teamZ/views/Location.fxml";

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Dashboard";
  }

  @FXML
  public void initialize() throws IOException {
    database = FacadeDAO.getInstance();
    alerts = new ArrayList<>();
    List<String> floors = List.of("5", "4", "3", "2", "1", "LL1", "LL2");
    for (String floor : floors) {
      alerts.add(new DashAlert(floor));
    }
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

    SVGPath Icon = new SVGPath();
    Icon.setContent(dashboardAlert);
    List<Region> dashRegions =
        List.of(
            dashRegion1,
            dashRegion2,
            dashRegion3,
            dashRegion4,
            dashRegion5,
            dashRegionLL1,
            dashRegionLL2);
    for (Region dashRegion : dashRegions) {
      dashRegion.setShape(Icon);
      dashRegion.setStyle("-fx-background-color: #FF4343;");
    }

    // Create observers for each dirty location
    List<Location> dirtyTest =
        List.of(
            dao.getLocationByID("zSTOR00305"),
            dao.getLocationByID("zSTOR00303"),
            dao.getLocationByID("zSTOR00403"),
            dao.getLocationByID("zSTOR00304"),
            dao.getLocationByID("zSTOR00404"));

    for (Location dirtyLocation : dirtyTest) {
      new DashboardBedAlertObserver(dirtyLocation, this);
    }

    List<Location> dirtyPumpLocations =
        List.of(
            dao.getLocationByID("zDIRT00103"),
            dao.getLocationByID("zDIRT00104"),
            dao.getLocationByID("zDIRT00105"));

    for (Location dirtyLocation : dirtyPumpLocations) {
      new DashboardBedAlertObserver(dirtyLocation, this);
    }

    List<Location> cleanPumpLocations =
        List.of(
            dao.getLocationByID("zSTOR00103"),
            dao.getLocationByID("zSTOR00203"),
            dao.getLocationByID("zSTOR00104"),
            dao.getLocationByID("zSTOR00204"),
            dao.getLocationByID("zSTOR00105"),
            dao.getLocationByID("zSTOR00205"));

    for (Location cleanLocation : cleanPumpLocations) {
      new DashboardBedAlertObserver(cleanLocation, this);
    }

    setupDropdown(floor5Container, "5");
    setupDropdown(floor4Container, "4");
    setupDropdown(floor3Container, "3");
    setupDropdown(floor2Container, "2");
    setupDropdown(floor1Container, "1");
    setupDropdown(LL1Container, "LL1");
    setupDropdown(LL2Container, "LL2");

    // root.addEventFilter(MouseEvent.ANY, e -> System.out.println(e));
    List<Region> dashList =
        List.of(
            dashRegion5,
            dashRegion4,
            dashRegion3,
            dashRegion2,
            dashRegion1,
            dashRegionLL1,
            dashRegionLL2);
    for (Region dashRegion : dashList) {
      dashRegion.setOnMouseEntered(
          (event) -> {
            try {
              alertHovered(dashRegion);
            } catch (IOException e) {
              e.printStackTrace();
            }
          });
    }
  }

  private void createBarUP5Dirty() {
    floor5Dirty.setStyle("-fx-accent: red;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("5").size();
    double dirty = FacadeDAO.getInstance().countDirtyEquipmentByFloor("5");
    double dirtyValue = dirty / total;
    floor5Dirty.setProgress(dirtyValue);
    floor5DirtyLabel.setText(Integer.toString((int) dirty));
    floor5TotalLabel.setText(Integer.toString((int) total));
  }
  // None of the equipment has "Clean" as the status, so nothing shows up under the clean progress
  // bar
  private void createBarUP5Clean() {
    floor5Clean.setStyle("-fx-accent: green;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("5").size();
    double clean = FacadeDAO.getInstance().countCleanEquipmentByFloor("5");
    double cleanValue = clean / total;
    floor5Clean.setProgress(cleanValue);
    floor5CleanLabel.setText(Integer.toString((int) clean));
  }

  private void createBarUP4Dirty() {
    floor4Dirty.setStyle("-fx-accent: red;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("4").size();
    double dirty = FacadeDAO.getInstance().countDirtyEquipmentByFloor("4");
    double dirtyValue = dirty / total;
    floor4Dirty.setProgress(dirtyValue);
    floor4DirtyLabel.setText(Integer.toString((int) dirty));
    floor4TotalLabel.setText(Integer.toString((int) total));
  }

  private void createBarUP4Clean() {
    floor4Clean.setStyle("-fx-accent: green;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("4").size();
    double clean = FacadeDAO.getInstance().countCleanEquipmentByFloor("4");
    double cleanValue = clean / total;
    floor4Clean.setProgress(cleanValue);
    floor4CleanLabel.setText(Integer.toString((int) clean));
  }

  private void createBarUP3Dirty() {
    floor3Dirty.setStyle("-fx-accent: red;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("3").size();
    double dirty = FacadeDAO.getInstance().countDirtyEquipmentByFloor("3");
    floor3Dirty.setProgress(dirty / total);
    floor3DirtyLabel.setText(Integer.toString((int) dirty));
    floor3TotalLabel.setText(Integer.toString((int) total));
  }

  private void createBarUP3Clean() {
    floor3Clean.setStyle("-fx-accent: green;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("3").size();
    double clean = FacadeDAO.getInstance().countCleanEquipmentByFloor("3");
    floor3Clean.setProgress(clean / total);
    floor3CleanLabel.setText(Integer.toString((int) clean));
  }

  private void createBarUP2Dirty() {
    floor2Dirty.setStyle("-fx-accent: red;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("2").size();
    double dirty = FacadeDAO.getInstance().countDirtyEquipmentByFloor("2");
    floor2Dirty.setProgress(dirty / total);
    floor2DirtyLabel.setText(Integer.toString((int) dirty));
    floor2TotalLabel.setText(Integer.toString((int) total));
  }

  private void createBarUP2Clean() {
    floor2Clean.setStyle("-fx-accent: green;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("2").size();
    double clean = FacadeDAO.getInstance().countCleanEquipmentByFloor("2");
    floor2Clean.setProgress(clean / total);
    floor2CleanLabel.setText(Integer.toString((int) clean));
  }

  private void createBarUP1Dirty() {
    floor1Dirty.setStyle("-fx-accent: red;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("1").size();
    double dirty = FacadeDAO.getInstance().countDirtyEquipmentByFloor("1");
    floor1Dirty.setProgress(dirty / total);
    floor1DirtyLabel.setText(Integer.toString((int) dirty));
    floor1TotalLabel.setText(Integer.toString((int) total));
  }

  private void createBarUP1Clean() {
    floor1Clean.setStyle("-fx-accent: green;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("1").size();
    double clean = FacadeDAO.getInstance().countCleanEquipmentByFloor("1");
    floor1Clean.setProgress(clean / total);
    floor1CleanLabel.setText(Integer.toString((int) clean));
  }

  private void createBarLL1Dirty() {
    lowerLevel1Dirty.setStyle("-fx-accent: red;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("LL1").size();
    double dirty = FacadeDAO.getInstance().countDirtyEquipmentByFloor("LL1");
    lowerLevel1Dirty.setProgress(dirty / total);
    lowerLevel1DirtyLabel.setText(Integer.toString((int) dirty));
    lowerLevel1TotalLabel.setText(Integer.toString((int) total));
  }

  private void createBarLL1Clean() {
    lowerLevel1Clean.setStyle("-fx-accent: green;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("LL1").size();
    double clean = FacadeDAO.getInstance().countCleanEquipmentByFloor("LL1");
    lowerLevel1Clean.setProgress(clean / total);
    lowerLevel1CleanLabel.setText(Integer.toString((int) clean));
  }

  private void createBarLL2Dirty() {
    lowerLevel2Dirty.setStyle("-fx-accent: red;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("LL2").size();
    double dirty = FacadeDAO.getInstance().countDirtyEquipmentByFloor("LL2");
    lowerLevel2Dirty.setProgress(dirty / total);
    lowerLevel2DirtyLabel.setText(Integer.toString((int) dirty));
    lowerLevel2TotalLabel.setText(Integer.toString((int) total));
  }

  private void createBarLL2Clean() {
    lowerLevel2Clean.setStyle("-fx-accent: green;");
    double total = FacadeDAO.getInstance().getAllMedicalEquipmentByFloor("LL2").size();
    double clean = FacadeDAO.getInstance().countCleanEquipmentByFloor("LL2");
    lowerLevel2Clean.setProgress(clean / total);
    lowerLevel2CleanLabel.setText(Integer.toString((int) clean));
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

  /**
   * Sets up the given dropdown pane
   *
   * @param dropdownPane the Pane that holds the contents
   * @param floor the floor the dropdown represents
   */
  private void setupDropdown(VBox dropdownPane, String floor) {
    // Setup button
    FlowPane topComponents = (FlowPane) dropdownPane.getChildren().get(0);
    TableView<DropdownRow> table = (TableView) dropdownPane.getChildren().get(1);
    MFXButton dropDownButton =
        (MFXButton) topComponents.getChildren().get(topComponents.getChildren().size() - 1);
    table
        .visibleProperty()
        .addListener(listener -> table.setPrefHeight(table.isVisible() ? 200 : 0));
    dropDownButton.setOnAction(event -> table.setVisible(!table.isVisible()));

    // Add data to table
    List<MedicalEquipment> floorEquipment = dao.getAllMedicalEquipmentByFloor(floor);

    // Filter
    List<MedicalEquipment> necessaryEquipment =
        floorEquipment.stream()
            .filter(
                equip ->
                    equip.getCurrentLocation().getNodeType().equals("DIRT")
                        || equip.getCurrentLocation().getNodeType().equals("STOR"))
            .collect(Collectors.toList());

    ObservableList<DropdownRow> tableRows = FXCollections.observableArrayList();
    for (MedicalEquipment equipment : necessaryEquipment)
      tableRows.add(
          new DropdownRow(
              equipment.getType(),
              equipment.getStatus().toString(),
              equipment.getCurrentLocation().getLongName()));

    // Idk why it says redundant
    //noinspection unchecked
    TableColumn<DropdownRow, String> col1 =
        (TableColumn<DropdownRow, String>) table.getColumns().get(0);
    col1.setReorderable(false);
    col1.setCellValueFactory(dRow -> dRow.getValue().type);
    //noinspection unchecked
    TableColumn<DropdownRow, String> col2 =
        (TableColumn<DropdownRow, String>) table.getColumns().get(1);
    col2.setReorderable(false);
    col2.setCellValueFactory(dRow -> dRow.getValue().location);
    //noinspection unchecked
    TableColumn<DropdownRow, String> col3 =
        (TableColumn<DropdownRow, String>) table.getColumns().get(2);
    col3.setReorderable(false);
    col3.setCellValueFactory(dRow -> dRow.getValue().status);
    col3.setCellFactory(tableCol -> new DropdownCell<>());

    table.setItems(tableRows);
  }

  public void toFloor5(ActionEvent actionEvent) throws IOException {
    LocationListController mapListController = (LocationListController) menu.load(toLocationURL);
    mapListController.changeToFloor("5");
  }

  public void toFloor4(ActionEvent actionEvent) throws IOException {
    LocationListController mapListController = (LocationListController) menu.load(toLocationURL);
    mapListController.changeToFloor("4");
  }

  public void toFloor3(ActionEvent actionEvent) throws IOException {
    LocationListController mapListController = (LocationListController) menu.load(toLocationURL);
    mapListController.changeToFloor("3");
  }

  public void toFloor2(ActionEvent actionEvent) throws IOException {
    LocationListController mapListController = (LocationListController) menu.load(toLocationURL);
    mapListController.changeToFloor("2");
  }

  public void toFloor1(ActionEvent actionEvent) throws IOException {
    LocationListController mapListController = (LocationListController) menu.load(toLocationURL);
    mapListController.changeToFloor("1");
  }

  public void toLowerLevel1(ActionEvent actionEvent) throws IOException {
    LocationListController mapListController = (LocationListController) menu.load(toLocationURL);
    mapListController.changeToFloor("L1");
  }

  public void toLowerLevel2(ActionEvent actionEvent) throws IOException {
    LocationListController mapListController = (LocationListController) menu.load(toLocationURL);
    mapListController.changeToFloor("L2");
  }

  public void updateBedAlert(String floor, int dirtyBeds, int dirtyPumps, int cleanPumps) {
    // Idk.
    DashAlert floorAlert =
        alerts.stream()
            .filter(alert -> alert.getFloor().equals(floor))
            .collect(Collectors.toList())
            .get(0);
    // floorAlert.addWarning(0, "There are %d dirty beds on this floor.");
    floorAlert.setWarningData(0, dirtyBeds);
    // floorAlert.addWarning(1, "There are %d dirty pumps on this floor.");
    floorAlert.setWarningData(1, dirtyPumps);
    // floorAlert.addWarning(2, "There are %d clean pumps on this floor.");
    floorAlert.setWarningData(2, cleanPumps);
  }

  public void floorAlert(String floor) {
    if (floor.equals("5")) {
      dashRegion5.setVisible(true);
    } else if (floor.equals("4")) {
      dashRegion4.setVisible(true);
    } else if (floor.equals("3")) {
      dashRegion3.setVisible(true);
    } else if (floor.equals("2")) {
      dashRegion2.setVisible(true);
    } else if (floor.equals("1")) {
      dashRegion1.setVisible(true);
    } else if (floor.equals("L1")) {
      dashRegionLL1.setVisible(true);
    } else if (floor.equals("L2")) {
      dashRegionLL2.setVisible(true);
    }
  }

  /**
   * Ran whenever the mouse hovers over
   *
   * @param alert the dashRegion for the alert
   */
  @FXML
  private void alertHovered(Region alert) throws IOException {
    // Only run if alert is active
    if (alert.isVisible()) {
      // Count dirty equipment on floor
      String floor = alert.getId().substring(10);
      List<MedicalEquipment> equipment = dao.getAllMedicalEquipmentByFloor(floor);
      equipment =
          equipment.stream()
              .filter(equip -> equip.getStatus().equals(MedicalEquipment.EquipmentStatus.DIRTY))
              .collect(Collectors.toList());

      // Create a popup window at alert
      Bounds boundsInScene = root.sceneToLocal(alert.localToScene(alert.getBoundsInLocal()));
      if (currentPopup != null) root.getChildren().remove(currentPopup);
      int add = 80;
      if (List.of("LL1", "LL2", "1").contains(floor)) add = -80;
      currentPopup =
          PopupLoader.loadPopup(
              "WarningMessage",
              root,
              (int) boundsInScene.getCenterX(),
              (int) boundsInScene.getCenterY() + add);

      // Get floor alert
      DashAlert floorAlert =
          alerts.stream()
              .filter(alertEnt -> alertEnt.getFloor().equals(floor))
              .collect(Collectors.toList())
              .get(0);

      for (String message : floorAlert.getWarnings()) {
        Label infoLabel = new Label();
        infoLabel.setText(message);
        VBox labelContainer = (VBox) currentPopup.lookup("#warningContainer");
        labelContainer.getChildren().add(infoLabel);
        infoLabel.setWrapText(true);
      }
    }
  }

  /** Runs when the mouse leaves the alert region */
  @FXML
  private void alertExit() {
    if (currentPopup != null) {
      // Remove from root & set to null
      root.getChildren().remove(currentPopup);
      currentPopup = null;
    }
  }

  static class DropdownRow {
    // SimpleStringProperty id;
    SimpleStringProperty type;
    SimpleStringProperty status;
    SimpleStringProperty location;

    public DropdownRow(String type, String status, String location) {
      // this.id = new SimpleStringProperty(id);
      this.type = new SimpleStringProperty(type);
      this.status = new SimpleStringProperty(status);
      this.location = new SimpleStringProperty(location);
    }
  }

  static class DropdownCell<DropdownRow, String> extends TableCell<DropdownRow, String> {
    @Override
    protected void updateItem(String item, boolean empty) {
      super.updateItem(item, empty);

      if (!empty) {
        setText((java.lang.String) item);
        if (item.equals("CLEAN")) {
          this.setStyle("-fx-text-fill: #09BA10;");
        } else {
          this.setStyle("-fx-text-fill: #E00303;");
        }
      }
    }
  }
}
