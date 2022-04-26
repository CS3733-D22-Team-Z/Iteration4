package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.controllers.subControllers.MapController;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import edu.wpi.cs3733.D22.teamZ.helpers.LabelMethod;
import edu.wpi.cs3733.D22.teamZ.helpers.PopupLoader;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

/** Controller for FloorDetails.fxml, which displays information about each floor. */
public class FloorDetailsController implements IMenuAccess, Initializable {
  @FXML private AnchorPane mapContainer;

  @FXML private CategoryAxis x;
  @FXML private NumberAxis y;
  @FXML private BarChart<?, ?> barChart;

  @FXML private TableView<DropdownRow> medicalEquipmentTable;
  @FXML private TableColumn<DropdownRow, String> typeColumn;
  @FXML private TableColumn<DropdownRow, String> locationColumn;
  @FXML private TableColumn<DropdownRow, String> statusColumn;

  private String floor;
  private List<Location> floorLocations;
  private List<MedicalEquipment> floorEquipment;

  private MenuController menu;
  private FacadeDAO dao;

  // Map stuff
  private ScrollPane map;
  private MapController mapController;
  private String statusFilter = "";

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

    floor = "3"; // TODO Idk how we're setting the floor its not being done rn
    makeChart();
  }

  public void makeChart() {
    XYChart.Series clean = new XYChart.Series();
    clean.setName("clean");
    int cleanBeds = FacadeDAO.getInstance().countCleanBedsByFloor(floor);
    int cleanIpump = FacadeDAO.getInstance().countCleanIPumpsByFloor(floor);
    int cleanRecliner = FacadeDAO.getInstance().countCleanReclinersByFloor(floor);
    int cleanXray = FacadeDAO.getInstance().countCleanXRaysByFloor(floor);
    clean.getData().add(new XYChart.Data("Bed", cleanBeds));
    clean.getData().add(new XYChart.Data("IPump", cleanIpump));
    clean.getData().add(new XYChart.Data("Recliner", cleanRecliner));
    clean.getData().add(new XYChart.Data("X-Ray", cleanXray));

    XYChart.Series dirty = new XYChart.Series();
    dirty.setName("dirty");
    int dirtyBeds = FacadeDAO.getInstance().countDirtyBedsByFloor(floor);
    int dirtyIpump = FacadeDAO.getInstance().countDirtyIPumpsByFloor(floor);
    int dirtyRecliner = FacadeDAO.getInstance().countDirtyReclinersByFloor(floor);
    int dirtyXray = FacadeDAO.getInstance().countDirtyXRaysByFloor(floor);
    dirty.getData().add(new XYChart.Data("Bed", dirtyBeds));
    dirty.getData().add(new XYChart.Data("IPump", dirtyIpump));
    dirty.getData().add(new XYChart.Data("Recliner", dirtyRecliner));
    dirty.getData().add(new XYChart.Data("X-Ray", dirtyXray));

    XYChart.Series inUse = new XYChart.Series();
    inUse.setName("in use");
    int inUseBeds = FacadeDAO.getInstance().countInUseBedsByFloor(floor);
    int inUseIpump = FacadeDAO.getInstance().countInUseIPumpsByFloor(floor);
    int inUseRecliner = FacadeDAO.getInstance().countInUseReclinersByFloor(floor);
    int inUseXray = FacadeDAO.getInstance().countInUseXRaysByFloor(floor);
    inUse.getData().add(new XYChart.Data("Bed", inUseBeds));
    inUse.getData().add(new XYChart.Data("IPump", inUseIpump));
    inUse.getData().add(new XYChart.Data("Recliner", inUseRecliner));
    inUse.getData().add(new XYChart.Data("X-Ray", inUseXray));

    barChart.getData().addAll(dirty, inUse, clean);
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return String.format("Floor %s", floor);
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
    List<Location> relevantLocations =
        floorLocations.stream()
            .filter(
                loc ->
                    loc.getEquipmentList().size() > 0
                        && (loc.getEquipmentList().stream()
                                .filter(
                                    medicalEquipment ->
                                        statusFilter.isEmpty()
                                            || medicalEquipment.getType().equals(statusFilter))
                                .count()
                            > 0))
            .collect(Collectors.toList());
    // Load in labels
    LabelMethod coloredLoader =
        (label) -> {
          // Count number of clean, in use, and dirty equipment
          int clean = 0;
          int dirty = 0;
          int inuse = 0;
          for (MedicalEquipment equipment : label.getLocation().getEquipmentList()) {
            switch (equipment.getStatus()) {
              case CLEAN:
                clean++;
                break;
              case DIRTY:
                dirty++;
                break;
              case INUSE:
                inuse++;
                break;
            }
          }

          // Get maximum
          int max = Math.max(clean, Math.max(dirty, inuse));
          String locImg;

          if (max == clean) {
            locImg = "locationGreen";
          } else if (max == dirty) {
            locImg = "location";
          } else {
            locImg = "locationYellow";
          }

          MapController.loadImage(locImg).call(label);
        };
    mapController.setLabels(relevantLocations, relevantLocations, false, coloredLoader);
  }

  /** Loads floor equipment into equipment table * */
  public void loadEquipmentTable() {
    List<MedicalEquipment> necessaryEquipment = floorEquipment;
    ObservableList<DropdownRow> tableRows = FXCollections.observableArrayList();
    for (MedicalEquipment equipment : necessaryEquipment)
      tableRows.add(
          new DropdownRow(
              equipment.getType(),
              equipment.getStatus().toString(),
              equipment.getCurrentLocation().getLongName()));

    // Idk why it says redundant
    //noinspection unchecked
    typeColumn.setReorderable(false);
    typeColumn.setCellValueFactory(dRow -> dRow.getValue().type);
    //noinspection unchecked
    locationColumn.setReorderable(false);
    locationColumn.setCellValueFactory(dRow -> dRow.getValue().location);
    //noinspection unchecked
    statusColumn.setReorderable(false);
    statusColumn.setCellValueFactory(dRow -> dRow.getValue().status);

    medicalEquipmentTable.setItems(tableRows);
  }

  @FXML
  private void allButtonPressed() {
    statusFilter = "";
    loadMap();
  }

  @FXML
  private void bedsButtonPressed() {
    statusFilter = "Bed";
    loadMap();
  }

  @FXML
  private void pumpsButtonPressed() {
    statusFilter = "IPumps";
    loadMap();
  }

  @FXML
  private void reclinerButtonPressed() {
    statusFilter = "Recliner";
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
}
