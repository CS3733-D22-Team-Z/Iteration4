package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.controllers.subControllers.MapController;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.*;
import edu.wpi.cs3733.D22.teamZ.helpers.PopupLoader;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import org.kynosarges.tektosyne.geometry.VoronoiResults;

// issues: getAllLocations doesn't work if the DB is disconnected, is this how it's supposed to
// work?

// LocationController controls Location.fxml, loads location data into a tableView on page
public class LocationListController implements IMenuAccess {

  // alert
  @FXML private Pane addAlertPane;
  @FXML private Pane deleteAlertPane;
  @FXML private MFXTextField alertLocationFieldAdd;
  @FXML private MFXTextField alertLocationFieldDelete;
  @FXML private MFXButton submitAlert;
  // @FXML private MFXButton addAlertButton;
  // @FXML private ComboBox<String> alertCodeField;
  // @FXML private MFXButton addLocationButton;
  // @FXML private MFXButton deleteAlert;
  // @FXML private MFXButton addAlertButton;
  @FXML private ChoiceBox<String> alertCodeFieldAdd;
  @FXML private ComboBox<String> alertCodeFieldDelete;
  // @FXML private MFXButton addLocationButton;
  @FXML private AnchorPane rightPane;
  @FXML private SplitPane splitPane;
  MenuController menu;
  // init ui components
  @FXML private AnchorPane mapContainer;
  @FXML private ChoiceBox<String> changeFloor;
  // @FXML private MFXButton editLocation;
  // @FXML private MFXButton deleteLocation;

  // Andrew's stuff
  @FXML private MFXTextField selectLocationTextField;
  @FXML private ChoiceBox<String> typeChoiceTextField;
  @FXML private ChoiceBox<String> floorChoiceTextField;
  @FXML private TextField changeNumberTextField;
  @FXML private TextField changeNameTextField;
  @FXML private TextField abbreviationTextField;
  @FXML private Text alreadyExistsText;
  @FXML private Label locationName;
  // @FXML private MFXButton submitButton;
  // @FXML private MFXButton clearButton;
  // @FXML private MFXButton editLocationExitButton;
  @FXML private Pane editLocationPane;
  @FXML private Pane locationChangeDarkenPane;
  @FXML private Pane zoomInButton;
  @FXML private Pane zoomOutButton;

  private static MapLabel activeLabel;
  //

  // Casey's
  @FXML private TextField searchField;
  @FXML private MFXListView<String> searchResultList;
  private SearchControl filter;
  private final BooleanProperty multiFocusProperty = new SimpleBooleanProperty();
  private final ObservableList<String> displayResult =
      FXCollections.observableList(new ArrayList<>());

  // Daniel's Stuff
  // Buttons
  // @FXML private MFXButton deleteMapLocation;
  // @FXML private MFXButton cancelLocationSelection;
  // text field box to select location to delete
  @FXML private MFXTextField locationToDeleteTextField;
  @FXML private Pane deleteLocationPlane1;

  // Neha's stuff
  // Buttons
  // @FXML private MFXButton submitAddLocation;
  // @FXML private MFXButton clearAddLocation;
  // @FXML private MFXButton addLocationExitButton;
  // text fields
  @FXML private MFXTextField xCoordTextField;
  @FXML private MFXTextField yCoordTextField;
  @FXML private ChoiceBox<String> locationTypeField;
  @FXML private ChoiceBox<String> floorField;
  @FXML private MFXTextField locationNameTextField;
  @FXML private MFXTextField nameAbbreviationTextField;

  // pane
  @FXML private Pane addLocationPane;

  @FXML @Getter private MFXRadioButton locRadio;
  @FXML @Getter private MFXRadioButton equipRadio;
  @FXML private MFXRadioButton servRadio;
  @FXML private MFXRadioButton cctvRadio;
  @FXML final ToggleGroup radioGroup = new ToggleGroup();

  @FXML private AnchorPane root;

  // init LocationDAOImpl to use sql methods from db
  FacadeDAO facadeDAO = FacadeDAO.getInstance();

  private final ObservableList<Location> totalLocations =
      FXCollections.observableList(new ArrayList<>());

  @Getter
  private final ObservableList<MapLabel> allLabels =
      FXCollections.observableList(new ArrayList<>());
  // private ObservableList<Label> alertLabels = FXCollections.observableList(new ArrayList<>());

  VoronoiResults[] accessable = new VoronoiResults[7];
  private ContextMenu rightClickMenu;

  private final String toServiceRequestProperties =
      "edu/wpi/cs3733/D22/teamZ/views/ServiceRequestProperties.fxml";
  private final String toLocationProperties =
      "edu/wpi/cs3733/D22/teamZ/views/LocationProperties.fxml";
  private final String toMedicalInfoProperties =
      "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentInfoTab.fxml";

  // The embedded map
  private MapController mapController;
  private ScrollPane mapPane;

  private List<Location> allLocations;
  private String mode;

  private int scrollCount;
  private int curZoom;

  // initialize location labels to display on map
  @FXML
  private void initialize() {
    // Setup embedded map
    PopupLoader.delay(
        10,
        () -> {
          List<Object> popupResults = null;
          try {
            popupResults = PopupLoader.loadPopup("Map", mapContainer);
          } catch (IOException e) {
            e.printStackTrace();
          }
          mapPane = (ScrollPane) popupResults.get(0);
          mapController = (MapController) popupResults.get(1);

          // Change dims
          mapPane.setPrefWidth(mapContainer.getWidth());
          mapPane.setPrefHeight(mapContainer.getHeight());
          mapPane.setLayoutX(0);
          mapPane.setLayoutY(0);

          // Set Label Clicked method of embedded map
          mapController.setLabelClickedMethod((label) -> activeLabel = label);

          // Set double clicked method of embedded map
          mapController.setDoubleClicked(this::doubleClickAdd);

          mapController.setRightClickedMethod(
              (label) -> {
                Bounds boundsToScreen = label.localToScreen(label.getBoundsInLocal());
                // root.sceneToLocal(label.localToScene(label.getBoundsInLocal()));*/
                // Cringe magic numbers
                rightClickMenu.show(
                    label, boundsToScreen.getCenterX(), boundsToScreen.getCenterY());
              });

          // Load default floor
          changeToFloor("3");

          Map<Integer, Double> locKeys = new HashMap<>();
          locKeys.put(45, 0.0);
          locKeys.put(50, 0.11);
          locKeys.put(55, .2);
          locKeys.put(60, .29);
          locKeys.put(65, .376);
          locKeys.put(70, .465);
          locKeys.put(75, .556);
          locKeys.put(80, .645);
          locKeys.put(85, .732);
          locKeys.put(90, .821);
          locKeys.put(95, .909);
          locKeys.put(100, 1.0);
          curZoom = 100;

          mapController.setZooms(locKeys);

          zoomInButton.addEventFilter(
              MouseEvent.MOUSE_CLICKED,
              e -> {
                curZoom += 5;
                curZoom = Math.max(45, Math.min(curZoom, 100));
                mapController.setScale(curZoom);
              });

          zoomOutButton.addEventFilter(
              MouseEvent.MOUSE_CLICKED,
              e -> {
                curZoom -= 5;
                curZoom = Math.max(45, Math.min(curZoom, 100));
                mapController.setScale(curZoom);
              });
        });

    System.out.println("loading labels");

    changeFloor.getItems().add("L2");
    changeFloor.getItems().add("L1");
    changeFloor.getItems().add("1");
    changeFloor.getItems().add("2");
    changeFloor.getItems().add("3");
    changeFloor.getItems().add("4");
    changeFloor.getItems().add("5");

    floorField.getItems().add("L2");
    floorField.getItems().add("L1");
    floorField.getItems().add("1");
    floorField.getItems().add("2");
    floorField.getItems().add("3");
    floorField.getItems().add("4");
    floorField.getItems().add("5");

    alertCodeFieldAdd.getItems().add("Code Red");
    alertCodeFieldAdd.getItems().add("Code Grey");
    alertCodeFieldAdd.getItems().add("Code Blue");
    alertCodeFieldAdd.getItems().add("Code Green");
    alertCodeFieldAdd.getItems().add("Code White");
    alertCodeFieldAdd.getItems().add("Code Pink");
    alertCodeFieldAdd.getItems().add("Code Amber");
    alertCodeFieldDelete.getItems().add("Code Red");
    alertCodeFieldDelete.getItems().add("Code Grey");
    alertCodeFieldDelete.getItems().add("Code Blue");
    alertCodeFieldDelete.getItems().add("Code Green");
    alertCodeFieldDelete.getItems().add("Code White");
    alertCodeFieldDelete.getItems().add("Code Pink");
    alertCodeFieldDelete.getItems().add("Code Amber");

    // showLocations("1");
    changeFloor.getSelectionModel().select(2);

    // change floor with dropdown
    changeFloor.setOnAction(
        (event) -> {
          String selectedItem = changeFloor.getSelectionModel().getSelectedItem();

          // System.out.println("Selection made: [" + selectedIndex + "] " + selectedItem);
          // System.out.println("   ChoiceBox.getValue(): " + changeFloor.getValue());
          // get list of locations from db and transfer into ObservableList
          System.out.println(selectedItem);
          changeToFloor(selectedItem);
        });

    // Andrew's stuff
    typeChoiceTextField.setItems(
        FXCollections.observableArrayList(
            "DEPT", "HALL", "ELEV", "STOR", "EXIT", "INFO", "RETL", "SERV", "STAI", "BATH", "LABS",
            "PATI"));
    locationTypeField.setItems(
        FXCollections.observableArrayList(
            "DEPT", "HALL", "ELEV", "STOR", "EXIT", "INFO", "RETL", "SERV", "STAI", "BATH", "LABS",
            "PATI"));

    floorChoiceTextField.setItems(FXCollections.observableArrayList("L2", "L1", "1", "2", "3"));

    Location displayResult = facadeDAO.getLocationByID(selectLocationTextField.getText());
    typeChoiceTextField.setValue(displayResult.getNodeType());

    locationChangeDarkenPane.setVisible(false);
    editLocationPane.setVisible(false);
    locationChangeDarkenPane.setDisable(true);
    editLocationPane.setDisable(true);

    // Casey's
    List<ISearchable> parentDataList = new ArrayList<>(facadeDAO.getAllLocations());
    filter = new SearchControl(parentDataList);

    List<String> longNames = new ArrayList<>();
    for (ISearchable loc : parentDataList) {
      longNames.add(loc.getDisplayName());
    }
    this.displayResult.addAll(FXCollections.observableList(longNames));
    searchResultList.setItems(this.displayResult);

    multiFocusProperty.bind(searchField.focusedProperty().or(searchResultList.focusedProperty()));

    multiFocusProperty.addListener(
        (observable, oldValue, newValue) -> {
          // System.out.println("vis swap");
          searchResultList.setVisible(newValue);
          searchResultList.setDisable(!newValue);
        });

    this.displayResult.addListener(
        (ListChangeListener<String>)
            c -> {
              searchResultList.setPrefHeight(
                  this.displayResult.size() * 32 // row height
                      + 2); // this gets called way too much, but whatever
              // System.out.println("height changed");
            });

    searchResultList.setCellFactory(param -> new MFXListCell<>(searchResultList, param));

    MenuItem edit = new MenuItem("Edit");
    edit.setOnAction(
        event -> {
          try {
            editLocationButtonClicked();
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
    MenuItem delete = new MenuItem("Delete");
    delete.setOnAction(
        event -> {
          try {
            deleteLocationButtonClicked();
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
    MenuItem prop = new MenuItem("Properties");
    prop.setOnAction(
        event -> {
          try {
            propertiesWindow();
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
    MenuItem addAlert = new MenuItem("Add Alert");
    addAlert.setOnAction(
        event -> {
          try {
            showAddAlertPane();
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
    MenuItem deleteAlert = new MenuItem("Delete Alert");
    deleteAlert.setOnAction(
        event -> {
          try {
            showDeleteAlertPane();
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
    // prop.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));

    rightClickMenu = new ContextMenu(edit, delete, prop, addAlert, deleteAlert);

    rightClickMenu.setPrefHeight(82);
    rightClickMenu.setPrefWidth(120);

    this.displayResult.remove(5, this.displayResult.size());

    // Daniel's Stuff

    locRadio.setToggleGroup(radioGroup);
    locRadio.setUserData("Locations");
    equipRadio.setToggleGroup(radioGroup);
    equipRadio.setUserData("Equipment");
    servRadio.setToggleGroup(radioGroup);
    servRadio.setUserData("Service Requests");
    //    cctvRadio.setToggleGroup(); // Not implemented
    //    cctvRadio.setUserData(); // Not implemented
    cctvRadio.setDisable(true); // temporary; disable for now
    cctvRadio.setVisible(false); // temporary; hide for now

    radioGroup
        .selectedToggleProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              MFXRadioButton selectedButton = (MFXRadioButton) newValue;
              mode = selectedButton.getText();
              changeToFloor(changeFloor.getSelectionModel().getSelectedItem());
            });
    mode = "Locations";

    allLocations = facadeDAO.getAllLocations();

    searchField
        .focusedProperty()
        .addListener(
            evt -> {
              if (searchField.getText().length() > 0) {
                search();
              }
            });
  }

  private void propertiesWindow() throws IOException {
    Stage stage = new Stage();
    TabPane root = new TabPane();
    // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    root.setPrefWidth(600);
    root.setPrefHeight(440);
    root.getStylesheets().add("edu/wpi/cs3733/D22/teamZ/styles/MenuDefault.css");

    // 3 tabs: do service request tab

    stage.setTitle("Properties");
    stage.getIcons().add(new Image("edu/wpi/cs3733/D22/teamZ/images/Hospital-Logo.png"));
    stage.setResizable(false);

    Tab locInfoTab = new Tab("Location Info");
    Tab medInfoTab = new Tab("Medical Equipment Info");
    Tab servReqTab = new Tab("Service Request Info");

    root.getTabs().addAll(medInfoTab, servReqTab, locInfoTab);

    root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

    FXMLLoader loader1 = new FXMLLoader();
    FXMLLoader loader2 = new FXMLLoader();
    FXMLLoader loader3 = new FXMLLoader();

    // Med Info
    loader1.setLocation(getClass().getClassLoader().getResource(toMedicalInfoProperties));
    Node page1 = loader1.load();
    medInfoTab.setContent(page1);

    // Location properties
    loader2.setLocation(getClass().getClassLoader().getResource(toLocationProperties));
    Node page2 = loader2.load();
    ((LocationPropertiesController) (loader2.getController()))
        .setLocation(activeLabel.getLocation());
    locInfoTab.setContent(page2);

    // Service Requests
    loader3.setLocation(getClass().getClassLoader().getResource(toServiceRequestProperties));
    Node page3 = loader3.load();
    ((ServiceRequestPropertiesController) (loader3.getController()))
        .setRequests(activeLabel.getReqs());
    servReqTab.setContent(page3);

    Scene window = new Scene(root);
    stage.setScene(window);
    stage.show();
  }

  /**
   * Adds locations to the map depending on which mode is selected. Also sets dragging behavior.
   *
   * @param floor the floor to pull locations from
   */
  private void showLocations(String floor) {
    List<Location> allFloorLocations = facadeDAO.getAllLocationsByFloor(floor);

    // If within Locations mode
    if (mode.equals("Locations")) {

      // Want all floor locations to be displayed + all locations are draggable anywhere.
      mapController.setLabels(allFloorLocations, allFloorLocations, false, "location");
      mapController.setIconShift(0);
      mapController.setDraggable(
          (label) -> {
            label.getLocation().setXcoord((int) label.getLayoutX());
            label.getLocation().setYcoord((int) label.getLayoutY());
            facadeDAO.updateLocation(label.getLocation());
          });

      // Within Equipment mode
    } else if (mode.equals("Equipment")) {
      // Retrieve all locations with equipment
      AtomicReference<List<Location>> locsWithEquip =
          new AtomicReference<>(
              allFloorLocations.stream()
                  .filter((loc) -> loc.getEquipmentList().size() > 0)
                  .collect(Collectors.toList()));
      mapController.setLabels(locsWithEquip.get(), allFloorLocations, true, "equipment");
      mapController.setIconShift(20);
      mapController.setDraggable(
          (label) -> {
            for (Location loc : allFloorLocations) {
              // Merge equipment when dragged to another location
              if (loc.getXcoord() == label.getLayoutX() && loc.getYcoord() == label.getLayoutY()) {
                System.out.println(
                    "Merging " + label.getLocation().toString() + " into " + loc.toString());
                // Extract
                List<MedicalEquipment> equip =
                    new ArrayList<>(label.getLocation().getEquipmentList());

                for (MedicalEquipment equipment : equip) {
                  // Clear
                  label.getLocation().removeEquipmentFromList(equipment);

                  // Add
                  loc.addEquipmentToList(equipment);

                  // Update equipment
                  equipment.setCurrentLocation(loc);
                  facadeDAO.updateMedicalEquipment(equipment);
                }

                // Update locations?
                facadeDAO.updateLocation(label.getLocation());
                facadeDAO.updateLocation(loc);
                break;
              }
            }

            // Refresh
            showLocations(floor);
          });
    } else if (mode.equals("Service Requests")) {
      AtomicReference<List<Location>> locsWithServices =
          new AtomicReference<>(
              allFloorLocations.stream()
                  .filter((loc) -> facadeDAO.getServiceRequestsByLocation(loc).size() > 0)
                  .collect(Collectors.toList()));
      mapController.setLabels(
          locsWithServices.get(), locsWithServices.get(), false, "servicerequest");
      mapController.setIconShift(0);
    }
  }
  // Andrew's Stuff

  @FXML
  private void submitEditLocationButtonClicked() {

    if (Integer.parseInt(changeNumberTextField.getText()) <= 0) {
      // good do nothing
      return;
    }

    // change later to Neha's nodeID info
    Location tempLocation = facadeDAO.getLocationByID(selectLocationTextField.getText());

    tempLocation.setNodeType(typeChoiceTextField.getValue());
    tempLocation.setFloor(floorChoiceTextField.getValue());
    tempLocation.setLongName(changeNameTextField.getText());
    tempLocation.setBuilding("Tower");
    tempLocation.setShortName(abbreviationTextField.getText());

    Location oldLoc = new Location(tempLocation.getNodeID());

    String newNodeID =
        "z"
            + typeChoiceTextField.getValue()
            + "0".repeat(3 - changeNumberTextField.getText().length())
            + changeNumberTextField.getText()
            + "0".repeat(2 - floorChoiceTextField.getValue().length())
            + floorChoiceTextField.getValue();

    // check if already exists
    if (facadeDAO.getLocationByID(newNodeID).getNodeID() == null) {
      alreadyExistsText.setVisible(false);
      List<MedicalEquipment> medicalEquipmentList =
          facadeDAO.getAllMedicalEquipmentByLocation(tempLocation);

      tempLocation.setNodeID(newNodeID);
      if (facadeDAO.addLocation(tempLocation)) {
        System.out.println("Added updated location successful");
      }

      // check if there are medical equipment stuff there
      if (!medicalEquipmentList.isEmpty()) {
        for (MedicalEquipment tempMedEquip : medicalEquipmentList) {
          tempMedEquip.setCurrentLocation(tempLocation);
          facadeDAO.updateMedicalEquipment(tempMedEquip);
        }
      }

      if (facadeDAO.deleteLocation(oldLoc)) {
        System.out.println("Delete location successful");
      }
      editLocationPane.setVisible(false);
      locationChangeDarkenPane.setVisible(false);

      // refreshMap(floorChoiceTextField.getSelectionModel().getSelectedItem());
      // refreshMap(oldFloor);
      changeFloor
          .getSelectionModel()
          .select(floorChoiceTextField.getSelectionModel().getSelectedItem());

      changeToFloor(floorChoiceTextField.getSelectionModel().getSelectedItem());

    } else {
      alreadyExistsText.setVisible(true);
    }
  }

  @FXML
  private void clearEditLocationButtonClicked() {
    typeChoiceTextField.setValue("DEPT");
    floorChoiceTextField.setValue("1");
    changeNumberTextField.setText("");
    changeNameTextField.setText("");
    abbreviationTextField.setText("");
  }

  @FXML
  private void editLocationButtonClicked() throws IOException {
    locationChangeDarkenPane.setVisible(true);
    editLocationPane.setVisible(true);
    locationChangeDarkenPane.setDisable(false);
    editLocationPane.setDisable(false);
    selectLocationTextField.setText(activeLabel.getLocation().getNodeID());
  }

  @FXML
  private void exitEditLocationButtonClicked() {
    locationChangeDarkenPane.setVisible(false);
    editLocationPane.setVisible(false);
    locationChangeDarkenPane.setDisable(true);
    editLocationPane.setDisable(true);
  }

  // Casey's
  @FXML
  public void search() {
    // searchField.requestFocus();
    List<ISearchable> tempResultList;
    tempResultList = filter.filterList(searchField.getText());
    List<String> longNames = new ArrayList<>();
    for (ISearchable loc : tempResultList) {
      longNames.add(loc.getDisplayName());
    }
    displayResult.remove(0, displayResult.size());
    displayResult.addAll(FXCollections.observableList(longNames));
    displayResult.remove(5, displayResult.size());
    searchResultList.setItems(displayResult);
  }

  @FXML
  public void resultMouseClick() {
    // MaterialFX goofy
    ObservableMap<Integer, String> selections = searchResultList.getSelectionModel().getSelection();
    searchResultList.getSelectionModel().clearSelection();
    String searched = "";
    for (Integer k : selections.keySet()) searched = selections.get(k);

    Location selectedLoc = null;
    for (Location loc : allLocations) {
      if (loc.getLongName().equals(searched)) {
        selectedLoc = loc;
        break;
      }
    }

    String selectedFloor = selectedLoc.getFloor();
    changeToFloor(selectedFloor);

    Location finalSelectedLoc = selectedLoc;
    activeLabel =
        (MapLabel)
            mapController.getIconContainer().getChildren().stream()
                .filter(
                    (label) -> {
                      if (label instanceof MapLabel) {
                        return ((MapLabel) label).getLocation().equals(finalSelectedLoc);
                      } else {
                        return false;
                      }
                    })
                .collect(Collectors.toList())
                .get(0);

    // activeLabel = allLabels.get(theoreticalGenericIndex);
    searchField.setText(activeLabel.getLocation().getLongName());
    activeLabel.requestFocus();
    // displayLocationInformation();
  }

  void changeToFloor(String nFloor) {
    // Dashboard button stuff
    changeFloor.getSelectionModel().select(nFloor);
    mapController.setFloor(nFloor);
    showLocations(nFloor);
  }

  @FXML
  public void deleteLocation() {
    Location temp = facadeDAO.getLocationByID(locationToDeleteTextField.getText());
    if (temp == null) {
      System.out.println("Did not find location in database");
      return;
    }
    if (facadeDAO.deleteLocation(temp)) {
      System.out.println("Deletion Successful");
      // TODO: fix
      // refreshMap(activeLabel.getLocation().getFloor());
      changeToFloor(activeLabel.getLocation().getFloor());
    } else {
      System.out.println("There are still stuff in this location");
    }

    locationChangeDarkenPane.setVisible(false);
    deleteLocationPlane1.setVisible(false);
    locationChangeDarkenPane.setDisable(true);
    deleteLocationPlane1.setDisable(true);
  }

  @FXML
  public void cancelLocationToDelete() {
    locationChangeDarkenPane.setVisible(false);
    deleteLocationPlane1.setVisible(false);
    locationChangeDarkenPane.setDisable(true);
    deleteLocationPlane1.setDisable(true);
  }

  @FXML
  private void deleteLocationButtonClicked() throws IOException {
    locationChangeDarkenPane.setVisible(true);
    deleteLocationPlane1.setVisible(true);
    locationChangeDarkenPane.setDisable(false);
    deleteLocationPlane1.setDisable(false);
    locationToDeleteTextField.setText(activeLabel.getLocation().getNodeID());
  }

  @FXML
  private void addLocationButtonClicked() {
    locationChangeDarkenPane.setVisible(true);
    addLocationPane.setVisible(true);
    locationChangeDarkenPane.setDisable(false);
    addLocationPane.setDisable(false);

    floorField.getSelectionModel().select(changeFloor.getSelectionModel().getSelectedIndex());
    floorField.setDisable(true);
    xCoordTextField.setEditable(false);
    yCoordTextField.setEditable(false);
    // selectLocationTextField.setText(activeLocation.getNodeID());
  }

  private void doubleClickAdd(MouseEvent evt) {
    locationChangeDarkenPane.setVisible(true);
    addLocationPane.setVisible(true);
    locationChangeDarkenPane.setDisable(false);
    addLocationPane.setDisable(false);

    floorField.getSelectionModel().select(changeFloor.getSelectionModel().getSelectedIndex());
    floorField.setDisable(true);
    xCoordTextField.setEditable(false);
    yCoordTextField.setEditable(false);
    xCoordTextField.setText(String.valueOf((int) evt.getX()));
    yCoordTextField.setText(String.valueOf((int) evt.getY()));
  }

  @FXML
  private void cancelAddLocation() {
    locationChangeDarkenPane.setVisible(false);
    addLocationPane.setVisible(false);
    locationChangeDarkenPane.setDisable(true);
    addLocationPane.setDisable(true);
  }

  @FXML
  private void cancelAddAlert() {
    addAlertPane.setVisible(false);
    addAlertPane.setDisable(true);
  }

  @FXML
  private void cancelDeleteAlert() {
    deleteAlertPane.setVisible(false);
    deleteAlertPane.setDisable(true);
  }

  @FXML
  private void addLocation() {
    // check if coords are valid
    if (Integer.parseInt(xCoordTextField.getText()) < 0
        || Integer.parseInt(xCoordTextField.getText()) > 1021) {
      return;
    }

    if (Integer.parseInt(yCoordTextField.getText()) < 0
        || Integer.parseInt(yCoordTextField.getText()) > 850) {
      return;
    }

    // check if fields arent empty
    if (!xCoordTextField.getText().isEmpty()
        && !yCoordTextField.getText().isEmpty()
        && !locationNameTextField.getText().isEmpty()
        && !nameAbbreviationTextField.getText().isEmpty()) {
      System.out.println("then add location");
    } else {
      return;
    }

    // make a location
    Location newLocation = new Location();

    newLocation.setXcoord(Integer.parseInt(xCoordTextField.getText()));
    newLocation.setYcoord(Integer.parseInt(yCoordTextField.getText()));
    newLocation.setNodeType(locationTypeField.getValue());
    newLocation.setFloor(floorField.getValue());
    newLocation.setLongName(locationNameTextField.getText());
    newLocation.setShortName(nameAbbreviationTextField.getText());
    newLocation.setBuilding("Tower");

    // generate a node id
    // generate numb
    List<Location> locations = facadeDAO.getAllLocationsByFloor(floorField.getValue());
    int size =
        (int)
            locations.stream()
                .filter(
                    unusedLocation ->
                        unusedLocation.getNodeType().equalsIgnoreCase(locationTypeField.getValue()))
                .count();

    String newNodeID =
        "z"
            + locationTypeField.getValue()
            + "0".repeat(3 - Integer.toString(size + 1).length())
            + (size + 1)
            + "0".repeat(2 - floorField.getValue().length())
            + floorField.getValue();

    newLocation.setNodeID(newNodeID);

    // check if exists, if not add it
    if (facadeDAO.getLocationByID(newNodeID).getNodeID() == null) {
      // add it
      facadeDAO.addLocation(newLocation);
    } else {
      return;
    }

    int floorIndex = floorField.getSelectionModel().getSelectedIndex();
    changeFloor.getSelectionModel().select(floorIndex);

    changeToFloor(changeFloor.getSelectionModel().getSelectedItem());
    // refreshMap(changeFloor.getSelectionModel().getSelectedItem());

    addLocationPane.setVisible(false);
    locationChangeDarkenPane.setVisible(false);
    addLocationPane.setDisable(true);
    locationChangeDarkenPane.setDisable(true);
  }

  /**
   * Captures all locations and stores in CSV. Runs when export to CSV is pressed.
   *
   * @param actionEvent
   */
  public void exportToCSV(ActionEvent actionEvent) {
    FileChooser fileChooser = new FileChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    fileChooser.setTitle("Enter a .csv file...");
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);

    File defaultFile = facadeDAO.getDefaultLocationCSVPath();
    if (defaultFile.isDirectory()) {
      fileChooser.setInitialDirectory(defaultFile);
    } else {
      fileChooser.setInitialDirectory(defaultFile.getParentFile());
      fileChooser.setInitialFileName(defaultFile.getName());
    }

    File file = fileChooser.showSaveDialog(stage);

    if (file != null) {
      facadeDAO.exportLocationsToCSV(file);
    }
  }

  /**
   * Prompts user to choose a csv file, and reads location data from that CSV. Runs when Import CSV
   * is clicked.
   *
   * @param actionEvent from event
   */
  public void importFromCSV(ActionEvent actionEvent) {
    FileChooser fileChooser = new FileChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    fileChooser.setTitle("Enter a .csv file...");
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);
    fileChooser.setInitialDirectory(facadeDAO.getDefaultLocationCSVPath());

    File defaultFile = facadeDAO.getDefaultLocationCSVPath();
    if (defaultFile.isDirectory()) {
      fileChooser.setInitialDirectory(defaultFile);
    } else {
      fileChooser.setInitialDirectory(defaultFile.getParentFile());
      fileChooser.setInitialFileName(defaultFile.getName());
    }

    File file = fileChooser.showOpenDialog(stage);

    if (file != null) {
      int numberConflicts = facadeDAO.importLocationsFromCSV(file);

      changeToFloor(changeFloor.getSelectionModel().getSelectedItem());
      System.out.println(
          "Detected "
              + numberConflicts
              + " locations that are"
              + " trying to get deleted but still have equipment in it");
    }
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Location Map";
  }

  public static MapLabel getActiveLabel() {
    return activeLabel;
  }

  @FXML
  public void showAddAlertPane() throws IOException {
    addAlertPane.setVisible(true);
    alertLocationFieldAdd.setText(activeLabel.getLocation().getNodeID());
    alertCodeFieldAdd.valueProperty().set(null);
    addAlertPane.setDisable(false);
    submitAlert.setOnAction(
        (e) -> {
          createAlert(
              alertCodeFieldAdd.getSelectionModel().getSelectedItem().toString(),
              activeLabel.getLocation());
          addAlertPane.setVisible(false);
        });
  }

  @FXML
  public void showDeleteAlertPane() throws IOException {
    deleteAlertPane.setVisible(true);
    alertLocationFieldDelete.setText(activeLabel.getLocation().getNodeID());
    alertCodeFieldDelete.valueProperty().set(null);
    deleteAlertPane.setDisable(false);
    submitAlert.setOnAction(
        (e) -> {
          createAlert(
              alertCodeFieldDelete.getSelectionModel().getSelectedItem().toString(),
              activeLabel.getLocation());
          deleteAlertPane.setVisible(false);
        });
  }

  public void createAlert(String code, Location location) {
    Alert newAlert = new Alert(Alert.AlertType.WARNING);
    switch (code) {
      case "Code Red":
        ImageView redAlertIcon =
            new ImageView(new Image("edu/wpi/cs3733/D22/teamZ/images/RedAlert.png"));
        newAlert.setTitle("Code Red Alert");
        newAlert.setHeaderText("Code Red");
        newAlert.setGraphic(redAlertIcon);
        createAlertLabel(redAlertIcon, location);
        if (location.getFloor().equals("1")) {
          newAlert.setContentText(
              "Fire at "
                  + location.getLongName()
                  + " on Floor "
                  + location.getFloor()
                  + ". The closest exit is "
                  + findClosestExit(location).getLongName());
        } else {
          newAlert.setContentText(
              "Fire at " + location.getLongName() + " on Floor " + location.getFloor());
        }
        findClosestExit(location);
        break;
      case "Code Grey":
        ImageView greyAlertIcon =
            new ImageView(new Image("edu/wpi/cs3733/D22/teamZ/images/GreyAlert.png"));
        newAlert.setTitle("Code Grey Alert");
        newAlert.setHeaderText("Code Grey");
        newAlert.setContentText(
            "Security Personnel Needed Urgently at "
                + location.getLongName()
                + " on Floor "
                + location.getFloor());
        newAlert.setGraphic(greyAlertIcon);
        createAlertLabel(greyAlertIcon, location);
        break;
      case "Code Green":
        System.out.println("green label");
        ImageView greenAlertIcon =
            new ImageView(new Image("edu/wpi/cs3733/D22/teamZ/images/GreenAlert.png"));
        newAlert.setTitle("Code Green Alert");
        newAlert.setHeaderText("Code Green");
        newAlert.setContentText(
            "M.D. or Specialty Needed Urgently at "
                + location.getLongName()
                + " on Floor "
                + location.getFloor());
        newAlert.setGraphic(greenAlertIcon);
        createAlertLabel(greenAlertIcon, location);
        break;
      case "Code White":
        ImageView whiteAlertIcon =
            new ImageView(new Image("edu/wpi/cs3733/D22/teamZ/images/WhiteAlert.png"));
        newAlert.setTitle("Code White Alert");
        newAlert.setHeaderText("Code White");
        newAlert.setContentText(
            "Bomb Threat at " + location.getLongName() + " on Floor " + location.getFloor());
        newAlert.setGraphic(whiteAlertIcon);
        createAlertLabel(whiteAlertIcon, location);
        break;
      case "Code Pink":
        ImageView pinkAlertIcon =
            new ImageView(new Image("edu/wpi/cs3733/D22/teamZ/images/PinkAlert.png"));
        newAlert.setTitle("Code Pink Alert");
        newAlert.setHeaderText("Code Pink");
        newAlert.setContentText(
            "Infant Abduction at " + location.getLongName() + " on Floor " + location.getFloor());
        newAlert.setGraphic(pinkAlertIcon);
        createAlertLabel(pinkAlertIcon, location);
        break;
      case "Code Amber":
        ImageView amberAlertIcon =
            new ImageView(new Image("edu/wpi/cs3733/D22/teamZ/images/AmberAlert.png"));
        newAlert.setTitle("Code Amber Alert");
        newAlert.setHeaderText("Code Amber");
        newAlert.setContentText("Disaster Plan in Effect");
        newAlert.setGraphic(amberAlertIcon);
        createAlertLabel(amberAlertIcon, location);
        break;
      case "Code Blue":
        ImageView blueAlertIcon =
            new ImageView(new Image("edu/wpi/cs3733/D22/teamZ/images/BlueAlert.png"));
        newAlert.setTitle("Code Blue Alert");
        newAlert.setHeaderText("Code Blue");
        newAlert.setContentText(
            "Immediate Medical Assistance Needed at "
                + location.getLongName()
                + " on Floor "
                + location.getFloor());
        newAlert.setGraphic(blueAlertIcon);
        createAlertLabel(blueAlertIcon, location);
        break;
      default:
        break;
    }

    newAlert.show();
    newAlert.setOnCloseRequest((e) -> {});
  }

  public void createAlertLabel(ImageView icon, Location location) {
    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(5.0);
    dropShadow.setOffsetX(3.0);
    dropShadow.setOffsetY(3.0);
    dropShadow.setColor(Color.GRAY);

    ContextMenu contextMenu = new ContextMenu();
    MenuItem menuItem1 = new MenuItem("Resolve alert");

    // create the label
    Label label = new Label();
    label.setEffect(dropShadow);
    label.setGraphic(icon);
    label.relocate(location.getXcoord() + 2, location.getYcoord() + 2);
    label.setContextMenu(contextMenu);
    contextMenu.getItems().add(menuItem1);
    menuItem1.setOnAction(
        (e) -> {
          // remove from map
        });
  }

  public Location findClosestExit(Location location) {
    Location closestExit = new Location();
    Location current;
    double distance;
    double bestDistance = 1000;

    for (Location totalLocation : totalLocations) {
      if (totalLocation.getNodeType().equals("EXIT")
          && totalLocation.getFloor().equals(location.getFloor())) {
        current = totalLocation;
        distance =
            Math.sqrt(
                (Math.pow((current.getXcoord() - location.getXcoord()), 2))
                    + (Math.pow((current.getYcoord() - location.getYcoord()), 2)));
        if (distance < bestDistance) {
          bestDistance = distance;
          closestExit = current;
        }
      }
    }
    System.out.println(closestExit.getLongName());
    for (MapLabel label : allLabels) {
      if (label.getLocation().getLongName().equals(closestExit.getLongName())) {
        activeLabel = label;
        System.out.println(activeLabel.getLocation().getLongName());
        activeLabel.setScaleX(1);
        activeLabel.setScaleY(1);
      }
    }
    return closestExit;
  }
}
