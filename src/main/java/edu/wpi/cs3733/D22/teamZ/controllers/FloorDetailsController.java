package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.controllers.subControllers.MapController;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import edu.wpi.cs3733.D22.teamZ.helpers.LabelMethod;
import edu.wpi.cs3733.D22.teamZ.helpers.PopupLoader;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/** Controller for FloorDetails.fxml, which displays information about each floor. */
public class FloorDetailsController implements IMenuAccess, Initializable {
  @FXML private AnchorPane mapContainer;
  @FXML private TableView<DropdownRow> medicalEquipmentTable;
  @FXML private TableColumn<DropdownRow, String> typeColumn;
  @FXML private TableColumn<DropdownRow, String> locationColumn;
  @FXML private TableColumn<DropdownRow, String> statusColumn;
  @FXML private MFXButton noneMapButton;
  @FXML private MFXButton bedsMapButton;
  @FXML private MFXButton pumpsMapButton;
  @FXML private MFXButton reclinerMapButton;
  @FXML private MFXButton xRayMapButton;

  private String floor;
  private List<Location> floorLocations;
  private List<MedicalEquipment> floorEquipment;

  private MenuController menu;
  private FacadeDAO dao;

  // Map stuff
  private final String buttonColorSelect = "-fx-background-color: %s; -fx-text-fill: #FFFFFF";
  private ScrollPane map;
  private MapController mapController;
  private String equipmentFilter = "";
  private MFXButton prevButton;
  private Map<String, String> equipmentColors =
      Map.of(
          "",
          "noneLocation",
          "Bed",
          "bedLocation",
          "IPumps",
          "pumpLocation",
          "Recliner",
          "reclinerLocation",
          "XRay",
          "xRayLocation");

  // Colorse
  List<String> colors = List.of("#0075FF", "#FF79DA", "#79FFF8", "#FF800B", "#B479FF");

  public FloorDetailsController() {
    dao = FacadeDAO.getInstance();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Load new map
    List<Object> popupResults = null;
    try {
      popupResults = PopupLoader.loadPopup("Map", mapContainer);
    } catch (IOException e) {
      e.printStackTrace();
    }
    map = (ScrollPane) popupResults.get(0);
    mapController = (MapController) popupResults.get(1);

    // Configure
    map.setPrefWidth(mapContainer.getPrefWidth());
    map.setPrefHeight(mapContainer.getPrefHeight());
    mapContainer.setStyle("-fx-background-color: transparent");

    prevButton = noneMapButton;

    // Equipment List
    typeColumn.setReorderable(false);
    typeColumn.setCellValueFactory(dRow -> dRow.getValue().type);
    typeColumn.setCellFactory((param) -> new SpecialCell());

    locationColumn.setReorderable(false);
    locationColumn.setCellValueFactory(dRow -> dRow.getValue().location);
    locationColumn.setCellFactory((param) -> new SpecialCell());

    statusColumn.setReorderable(false);
    statusColumn.setCellValueFactory(dRow -> dRow.getValue().status);
    statusColumn.setCellFactory((param) -> new SpecialCell());
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Floor Details";
  }

  /**
   * Sets the floor of the page, and loads data from that floor into the page.
   *
   * @param floor
   */
  public void setFloor(String floor) throws IOException {
    this.floor = floor;
    load();
  }

  /**
   * Main loading function. Initiates loading of overview, map, service requests, and medical
   * equipment.
   */
  public void load() throws IOException {
    floorLocations = dao.getAllLocationsByFloor(floor);
    floorEquipment = dao.getAllMedicalEquipmentByFloor(floor);

    loadMap();
    loadEquipmentTable();
  }

  /** Loads the map for the given floor. */
  public void loadMap() {
    mapController.setFloor(floor);
    // Only want locations with equipment that adhere to filter.
    //    List<Location> relevantLocations =
    //        floorLocations.stream()
    //            .filter(
    //                loc ->
    //                    loc.getEquipmentList().stream().filter(equipment ->
    // equipment.getType().equals(equipmentFilter)).count() > 0)
    //            .collect(Collectors.toList());
    LabelMethod colorStuff =
        (label) -> {
          if (label.getLocation().getEquipmentList().stream()
                  .filter(equip -> equip.getType().equals(equipmentFilter))
                  .count()
              == 0) {
            MapController.loadImage(equipmentColors.get("")).call(label);
          } else {
            MapController.loadImage(equipmentColors.get(equipmentFilter)).call(label);
          }
        };
    mapController.setLabels(floorLocations, floorLocations, false, colorStuff);
  }

  /** Loads floor equipment into equipment table * */
  public void loadEquipmentTable() {
    List<MedicalEquipment> necessaryEquipment =
        floorEquipment.stream()
            .filter(equipment -> !equipment.getType().equals("INUSE"))
            .collect(Collectors.toList());
    ObservableList<DropdownRow> tableRows = FXCollections.observableArrayList();
    for (MedicalEquipment equipment : necessaryEquipment)
      tableRows.add(
          new DropdownRow(
              equipment.getType(),
              equipment.getStatus().toString(),
              equipment.getCurrentLocation().getLongName()));

    // Idk why it says redundant

    medicalEquipmentTable.setItems(tableRows);
  }

  @FXML
  private void noneButtonPressed() {
    equipmentFilter = "";
    prevButton.setStyle("");
    noneMapButton.setStyle(String.format(buttonColorSelect, colors.get(0)));
    prevButton = noneMapButton;
    loadMap();
  }

  @FXML
  private void bedsButtonPressed() {
    equipmentFilter = "Bed";
    prevButton.setStyle("");
    bedsMapButton.setStyle(String.format(buttonColorSelect, colors.get(1)));
    prevButton = bedsMapButton;
    loadMap();
  }

  @FXML
  private void pumpsButtonPressed() {
    equipmentFilter = "IPumps";
    prevButton.setStyle("");
    pumpsMapButton.setStyle(String.format(buttonColorSelect, colors.get(2)));
    prevButton = pumpsMapButton;
    loadMap();
  }

  @FXML
  private void reclinerButtonPressed() {
    equipmentFilter = "Recliner";
    prevButton.setStyle("");
    reclinerMapButton.setStyle(String.format(buttonColorSelect, colors.get(3)));
    prevButton = reclinerMapButton;
    loadMap();
  }

  @FXML
  private void xrayButtonPressed() {
    equipmentFilter = "XRay";
    prevButton.setStyle("");
    xRayMapButton.setStyle(String.format(buttonColorSelect, colors.get(4)));
    prevButton = xRayMapButton;
    loadMap();
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
  /*.setCellFactory(param -> {
    TableCell<DropdownRow, String> cell = new TableCell<>();
    Text text = new Text();
    cell.setGraphic(text);
    cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
    text.wrappingWidthProperty().bind(cell.widthProperty());
    text.textProperty().bind(cell.itemProperty());
    return cell;
  });*/
  static class SpecialCell extends TableCell<DropdownRow, String> {
    public SpecialCell() {
      Text text = new Text();
      setGraphic(text);
      setPrefHeight(Control.USE_COMPUTED_SIZE);
      text.wrappingWidthProperty().bind(widthProperty());
      text.setTextAlignment(TextAlignment.CENTER);
      text.textProperty().bind(itemProperty());
    }
  }
}
