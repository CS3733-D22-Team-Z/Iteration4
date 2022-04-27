package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.controllers.subControllers.MapController;
import edu.wpi.cs3733.D22.teamZ.controllers.subControllers.SingleServiceRequestController;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MapLabel;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import edu.wpi.cs3733.D22.teamZ.helpers.LabelMethod;
import edu.wpi.cs3733.D22.teamZ.helpers.PopupLoader;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/** Controller for FloorDetails.fxml, which displays information about each floor. */
public class FloorDetailsController implements IMenuAccess, Initializable {
  private static String xSvg = "M5.25 1.75L1.75 5.25, M1.75 1.75L5.25 5.25";
  private static String checkSvg = "M5 1L2.25 4L1 2.63636";
  private static String timeSvg =
      "M3 5.5C4.38071 5.5 5.5 4.38071 5.5 3C5.5 1.61929 4.38071 0.5 3 0.5C1.61929 0.5 0.5 1.61929 0.5 3C0.5 4.38071 1.61929 5.5 3 5.5Z M3 1.5V3L4 3.5";

  @FXML private Label floorTitle;

  @FXML private CategoryAxis x;
  @FXML private NumberAxis y;
  @FXML private BarChart<?, ?> barChart;

  @FXML private AnchorPane mapContainer;
  @FXML private MFXButton noneMapButton;
  @FXML private MFXButton bedsMapButton;
  @FXML private MFXButton pumpsMapButton;
  @FXML private MFXButton reclinerMapButton;
  @FXML private MFXButton xRayMapButton;

  @FXML private VBox equipmentListBed;
  @FXML private VBox equipmentListInfusion;
  @FXML private VBox equipmentListRecliner;
  @FXML private VBox equipmentListXRay;

  @FXML private VBox timeContainer;

  private String floor;
  private List<Location> floorLocations;
  private List<MedicalEquipment> floorEquipment;
  private List<ServiceRequest> floorServices;

  private MenuController menu;
  private FacadeDAO dao;

  // Map stuff
  private final String buttonColorSelect = "-fx-background-color: %s; -fx-text-fill: #FFFFFF";
  private ScrollPane map;
  private MapController mapController;
  private String equipmentFilter = "";
  private MFXButton prevButton;
  private MapLabel lastLabel;
  private boolean cooling = false;
  private Map<String, String> locationColorMap =
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

  // Equipment
  private Map<String, String> equipFormatStrings =
      Map.of(
          "Bed",
          "Beds: %d",
          "IPumps",
          "Infusion Pumps: %d",
          "Recliner",
          "Recliners: %d",
          "XRay",
          "XRay Machines: %d");

  // Service Requests
  DateTimeFormatter hourFormat = DateTimeFormatter.ofPattern("hh:mm a");
  private MFXButton lastActiveButton;

  // Colorse
  static List<String> equipmentColors =
      List.of("#0075FF", "#FF79DA", "#00ADA4", "#FF800B", "#B479FF");
  static Map<String, String> statusColors =
      Map.of("DIRTY", "#FF4343", "INUSE", "#E1BD00", "CLEAN", "#00CF15", "CLEANING", "#0075FF");

  private String toDashboard = "edu/wpi/cs3733/D22/teamZ/views/DashboardFinal.fxml";
  private String toServiceRequestProperties =
      "edu/wpi/cs3733/D22/teamZ/views/popups/SingleServiceRequest.fxml";
  private final String toLocationProperties =
      "edu/wpi/cs3733/D22/teamZ/views/LocationProperties.fxml";

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
    mapController.setScale(0.8);
    map.setHvalue(0.4);
    map.setVvalue(0.2);
    mapController.setLabelClickedMethod(
        (label) -> {
          if (label.equals(lastLabel) && !cooling) {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource(toLocationProperties));
            Parent root = null;
            try {
              root = loader.load();
            } catch (IOException e) {
              e.printStackTrace();
            }

            Scene window = new Scene(root);
            stage.setScene(window);
            stage.show();

            LocationPropertiesController controller = loader.getController();
            controller.setLocation(label.getLocation());
            cooling = true;
            PopupLoader.delay(3000, () -> cooling = false);
          }
          lastLabel = label;
          PopupLoader.delay(1000, () -> lastLabel = null);
        });

    prevButton = noneMapButton;
  }

  public void makeChart() {
    XYChart.Series clean = new XYChart.Series();
    clean.setName("Clean");
    int cleanBeds = FacadeDAO.getInstance().countCleanBedsByFloor(floor);
    int cleanIpump = FacadeDAO.getInstance().countCleanIPumpsByFloor(floor);
    int cleanRecliner = FacadeDAO.getInstance().countCleanReclinersByFloor(floor);
    int cleanXray = FacadeDAO.getInstance().countCleanXRaysByFloor(floor);
    clean.getData().add(new XYChart.Data("Bed", cleanBeds));
    clean.getData().add(new XYChart.Data("IPump", cleanIpump));
    clean.getData().add(new XYChart.Data("Recliner", cleanRecliner));
    clean.getData().add(new XYChart.Data("X-Ray", cleanXray));

    XYChart.Series dirty = new XYChart.Series();
    dirty.setName("Dirty");
    int dirtyBeds = FacadeDAO.getInstance().countDirtyBedsByFloor(floor);
    int dirtyIpump = FacadeDAO.getInstance().countDirtyIPumpsByFloor(floor);
    int dirtyRecliner = FacadeDAO.getInstance().countDirtyReclinersByFloor(floor);
    int dirtyXray = FacadeDAO.getInstance().countDirtyXRaysByFloor(floor);
    dirty.getData().add(new XYChart.Data("Bed", dirtyBeds));
    dirty.getData().add(new XYChart.Data("IPump", dirtyIpump));
    dirty.getData().add(new XYChart.Data("Recliner", dirtyRecliner));
    dirty.getData().add(new XYChart.Data("X-Ray", dirtyXray));

    XYChart.Series inUse = new XYChart.Series();
    inUse.setName("In-Use");
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
    return "Floor Details";
  }

  /**
   * Sets the floor of the page, and loads data from that floor into the page.
   *
   * @param floor
   */
  public void setFloor(String floor) throws IOException {
    this.floor = floor;
    floorTitle.setText("Floor " + floor);
    load();
  }

  @FXML
  private void backButtonClicked() throws IOException {
    menu.load(toDashboard);
  }

  /**
   * Main loading function. Initiates loading of overview, map, service requests, and medical
   * equipment.
   */
  public void load() throws IOException {
    floorLocations = dao.getAllLocationsByFloor(floor);
    floorEquipment = dao.getAllMedicalEquipmentByFloor(floor);
    floorServices =
        dao.getAllServiceRequests().stream()
            .filter(req -> req.getTargetLocation().getFloor().equals(floor))
            .collect(Collectors.toList());

    loadMap();
    makeChart();
    setupEquipment(equipmentListBed, "Bed");
    setupEquipment(equipmentListInfusion, "IPumps");
    setupEquipment(equipmentListRecliner, "Recliner");
    setupEquipment(equipmentListXRay, "XRay");
    loadServiceRequests(floorServices);
  }

  /** Loads the map for the given floor. */
  public void loadMap() {
    mapController.setFloor(floor);
    LabelMethod colorStuff =
        (label) -> {
          if (label.getLocation().getEquipmentList().stream()
                  .filter(equip -> equip.getType().equals(equipmentFilter))
                  .count()
              == 0) {
            MapController.loadImage(locationColorMap.get("")).call(label);
          } else {
            MapController.loadImage(locationColorMap.get(equipmentFilter)).call(label);
          }
        };
    mapController.setLabels(floorLocations, floorLocations, false, colorStuff);
  }

  /**
   * Sets up an equipment container VBox
   *
   * @param container the container to be set up
   * @param equipmentType the type of equipment corresponding to the container
   */
  public void setupEquipment(VBox container, String equipmentType) {
    // Get equipment
    List<MedicalEquipment> thisEquipment =
        floorEquipment.stream()
            .filter(equip -> equip.getType().equals(equipmentType))
            .collect(Collectors.toList());

    // Set label
    Label contLabel = (Label) container.lookup("#numLabel");
    contLabel.setText(Integer.toString(thisEquipment.size()));

    // Set dropdown function
    MFXButton dropdown = (MFXButton) contLabel.getGraphic();
    final boolean[] active = {false};
    dropdown.setOnAction(
        (event) -> {
          if (lastActiveButton != dropdown && lastActiveButton != null) {
            lastActiveButton.fire();
          }
          if (active[0]) {
            // Remove table
            container.getChildren().remove(1);
            contLabel.getGraphic().setRotate(0);
          } else {
            TableView<DropdownRow> thisTable = loadEquipmentTable(thisEquipment);
            container.getChildren().add(thisTable);
            contLabel.getGraphic().setRotate(180);
          }
          active[0] = !active[0];
          if (active[0]) lastActiveButton = dropdown;
          else lastActiveButton = null;
        });
  }

  /**
   * Loads floor equipment into equipment table
   *
   * @param data the data to be placed in the table
   * @return a TableView with data filled in from type *
   */
  public TableView<DropdownRow> loadEquipmentTable(List<MedicalEquipment> data) {
    // First create tableview
    TableView<DropdownRow> table = new TableView<>();
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    table.getStyleClass().add("dropdown-table-view");
    table.setPrefHeight(50 * data.size() + 40);

    // Create columns
    TableColumn<DropdownRow, String> locCol = new TableColumn<>();
    locCol.setSortable(false);
    locCol.setReorderable(false);
    locCol.setCellFactory((param) -> new SpecialCell());
    locCol.setCellValueFactory(dRow -> dRow.getValue().location);
    locCol.setText("Location");

    TableColumn<DropdownRow, String> statCol = new TableColumn<>();
    statCol.setSortable(false);
    statCol.setReorderable(false);
    statCol.setCellFactory((param) -> new SpecialCell());
    statCol.setCellValueFactory(dRow -> dRow.getValue().status);
    statCol.setText("Status");

    table.getColumns().addAll(locCol, statCol);

    // Add data
    ObservableList<DropdownRow> tableRows = FXCollections.observableArrayList();
    for (MedicalEquipment equipment : data)
      tableRows.add(
          new DropdownRow(
              equipment.getType(),
              equipment.getStatus().toString(),
              equipment.getCurrentLocation().getLongName()));

    table.setItems(tableRows);

    // Return
    return table;
  }

  public void sortRequests(List<ServiceRequest> requests) {
    // Sort requests based on time
    requests.sort(
        (a, b) -> {
          LocalDateTime truncA = a.getOpened().truncatedTo(ChronoUnit.HOURS);
          LocalDateTime truncB = b.getOpened().truncatedTo(ChronoUnit.HOURS);
          if (truncA.equals(truncB)) return a.getStatus().compareTo(b.getStatus());
          else return truncA.compareTo(truncB);
        });
  }

  public void loadServiceRequests(List<ServiceRequest> requests) throws IOException {
    // Sort requests based on time
    sortRequests(requests);

    // Iterate through list and load in elements
    LocalDateTime base = requests.get(0).getOpened().truncatedTo(ChronoUnit.HOURS);
    AnchorPane currentTimeCont =
        (AnchorPane) PopupLoader.loadPopup("TimeStack", timeContainer).get(0);
    ((Label) (currentTimeCont.lookup("#timeLabel"))).setText(hourFormat.format(base));

    for (ServiceRequest request : requests) {
      // "Floor" the time
      LocalDateTime time = request.getOpened().truncatedTo(ChronoUnit.HOURS);
      // If within same hour
      if (!time.equals(base)) {
        base = time;
        currentTimeCont = (AnchorPane) PopupLoader.loadPopup("TimeStack", timeContainer).get(0);
        ((Label) (currentTimeCont.lookup("#timeLabel"))).setText(hourFormat.format(base));
      }

      AnchorPane serviceLabel =
          (AnchorPane)
              PopupLoader.loadPopup(
                      "ServiceRequestLabel", (Pane) currentTimeCont.lookup("#serviceContainer"))
                  .get(0);
      ((Label) (serviceLabel.lookup("#requestLabel"))).setText(request.getType().toString());
      if (request.getStatus().equals(ServiceRequest.RequestStatus.UNASSIGNED)) {
        ((SVGPath) serviceLabel.lookup("#statusSvg")).setContent(xSvg);
        serviceLabel.setStyle(
            "-fx-background-radius: 50; -fx-background-color: " + statusColors.get("DIRTY"));
      } else if (request.getStatus().equals(ServiceRequest.RequestStatus.PROCESSING)) {
        ((SVGPath) serviceLabel.lookup("#statusSvg")).setContent(timeSvg);
        serviceLabel.setStyle(
            "-fx-background-radius: 50; -fx-background-color: " + statusColors.get("INUSE"));
      } else {
        ((SVGPath) serviceLabel.lookup("#statusSvg")).setContent(checkSvg);
        serviceLabel.setStyle(
            "-fx-background-radius: 50; -fx-background-color: " + statusColors.get("CLEAN"));
      }
      ((MFXButton) serviceLabel.lookup("#detailsButton"))
          .setOnAction(
              (event) -> {
                Stage stage = new Stage();

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(
                    getClass().getClassLoader().getResource(toServiceRequestProperties));
                Parent root = null;
                try {
                  root = loader.load();
                } catch (IOException e) {
                  e.printStackTrace();
                }

                Scene window = new Scene(root);
                stage.setScene(window);
                stage.show();

                SingleServiceRequestController controller = loader.getController();
                controller.setValues(request);
              });
    }
  }

  @FXML
  private void noneButtonPressed() {
    equipmentFilter = "";
    prevButton.setStyle("");
    noneMapButton.setStyle(String.format(buttonColorSelect, equipmentColors.get(0)));
    prevButton = noneMapButton;
    loadMap();
  }

  @FXML
  private void bedsButtonPressed() {
    equipmentFilter = "Bed";
    prevButton.setStyle("");
    bedsMapButton.setStyle(String.format(buttonColorSelect, equipmentColors.get(1)));
    prevButton = bedsMapButton;
    loadMap();
  }

  @FXML
  private void pumpsButtonPressed() {
    equipmentFilter = "IPumps";
    prevButton.setStyle("");
    pumpsMapButton.setStyle(String.format(buttonColorSelect, equipmentColors.get(2)));
    prevButton = pumpsMapButton;
    loadMap();
  }

  @FXML
  private void reclinerButtonPressed() {
    equipmentFilter = "Recliner";
    prevButton.setStyle("");
    reclinerMapButton.setStyle(String.format(buttonColorSelect, equipmentColors.get(3)));
    prevButton = reclinerMapButton;
    loadMap();
  }

  @FXML
  private void xrayButtonPressed() {
    equipmentFilter = "XRay";
    prevButton.setStyle("");
    xRayMapButton.setStyle(String.format(buttonColorSelect, equipmentColors.get(4)));
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

  static class SpecialCell extends TableCell<DropdownRow, String> {
    public SpecialCell() {
      Text text = new Text();
      setGraphic(text);
      setPrefHeight(Control.USE_COMPUTED_SIZE);
      text.wrappingWidthProperty().bind(widthProperty());
      text.setTextAlignment(TextAlignment.CENTER);
      text.textProperty().bind(itemProperty());
    }

    @Override
    protected void updateItem(String item, boolean empty) {
      super.updateItem(item, empty);

      if (!empty) {
        setText(item);
        // setStyle("-fx-background-color: #FF00FF; -fx-text-fill: #00FF00;");
        // if (statusColors.containsKey(item))
        //  this.setStyle(String.format("-fx-text-fill: %s;", statusColors.get(item)));
      }
    }
  }
}
