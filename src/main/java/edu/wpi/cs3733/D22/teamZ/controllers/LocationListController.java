package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

// issues: getAllLocations doesn't work if the DB is disconnected, is this how it's supposed to
// work?

// LocationController controls Location.fxml, loads location data into a tableView on page
public class LocationListController implements IMenuAccess {

  // alert
  @FXML private Pane addAlertPane;
  @FXML private MFXTextField alertLocationField;
  @FXML private MFXButton submitAlert;
  @FXML private MFXButton addAlertButton;
  @FXML private ComboBox<String> alertCodeField;
  @FXML private MFXButton addLocationButton;
  @FXML private AnchorPane rightPane;
  @FXML private SplitPane splitPane;
  @FXML private Group group;
  @FXML private ScrollPane scrollPane;
  MenuController menu;
  // init ui components
  @FXML private AnchorPane pane;
  @FXML private MFXLegacyComboBox<String> changeFloor;
  @FXML private ImageView map;
  @FXML private MFXButton editLocation;
  @FXML private MFXButton deleteLocation;

  // Andrew's stuff
  @FXML private MFXTextField selectLocationTextField;
  @FXML private MFXLegacyComboBox<String> typeChoiceTextField;
  @FXML private MFXLegacyComboBox<String> floorChoiceTextField;
  @FXML private TextField changeNumberTextField;
  @FXML private TextField changeNameTextField;
  @FXML private TextField abbreviationTextField;
  @FXML private Text alreadyExistsText;
  @FXML private MFXButton submitButton;
  @FXML private MFXButton clearButton;
  @FXML private MFXButton editLocationExitButton;
  @FXML private Pane editLocationPane;
  @FXML private Pane locationChangeDarkenPane;

  private static MapLabel activeLabel;
  //

  // Casey's
  @FXML private TextField searchField;
  @FXML private ListView<String> searchResultList;
  private SearchControl filter;
  private List<ISearchable> parentDataList;
  private final BooleanProperty multiFocusProperty = new SimpleBooleanProperty();
  private ObservableList<String> displayResult = FXCollections.observableList(new ArrayList<>());

  // Daniel's Stuff
  // Buttons
  @FXML private MFXButton deleteMapLocation;
  @FXML private MFXButton cancelLocationSelection;
  // text field box to select location to delete
  @FXML private MFXTextField locationToDeleteTextField;
  @FXML private Pane deleteLocationPlane;

  // Neha's stuff
  // Buttons
  @FXML private MFXButton submitAddLocation;
  @FXML private MFXButton clearAddLocation;
  @FXML private MFXButton addLocationExitButton;
  // text fields
  @FXML private MFXTextField xCoordTextField;
  @FXML private MFXTextField yCoordTextField;
  @FXML private MFXLegacyComboBox<String> locationTypeField;
  @FXML private MFXLegacyComboBox<String> floorField;
  @FXML private MFXTextField locationNameTextField;
  @FXML private MFXTextField nameAbbreviationTextField;

  // pane
  @FXML private Pane addLocationPane;

  @FXML private MFXRadioButton locRadio;
  @FXML private MFXRadioButton equipRadio;
  @FXML private MFXRadioButton servRadio;
  @FXML final ToggleGroup radioGroup = new ToggleGroup();

  // urls to other pages
  private String toLocationsURL = "edu/wpi/cs3733/D22/teamZ/views/Location.fxml";
  private String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";
  private String toMedicalEquipmentRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentRequestList.fxml";
  private String toHomeURL = "edu/wpi/cs3733/D22/teamZ/views/Homepage.fxml";
  private String toEquipmentMapURL = "edu/wpi/cs3733/D22/teamZ/views/EquipmentMap.fxml";
  private String toServiceRequestProperties =
      "edu/wpi/cs3733/D22/teamZ/views/ServiceRequestProperties.fxml";

  // init LocationDAOImpl to use sql methods from db
  FacadeDAO facadeDAO = FacadeDAO.getInstance();

  // create ObservableList to load locations into map
  // private ObservableList<Location> floorLocations = FXCollections.observableList(new
  // ArrayList<>());
  private ObservableList<Location> totalLocations = FXCollections.observableList(new ArrayList<>());
  private ObservableList<MapLabel> allLabels = FXCollections.observableList(new ArrayList<>());
  private ObservableList<Label> equipLabels = FXCollections.observableList(new ArrayList<>());
  private ObservableList<Label> alertLabels = FXCollections.observableList(new ArrayList<>());

  private ContextMenu rightClickMenu;

  // initialize location labels to display on map
  @FXML
  private void initialize() {

    rightPane.maxWidthProperty().bind(splitPane.widthProperty().multiply(.23));
    pane.maxWidthProperty().bind(splitPane.widthProperty().multiply(.75));

    StackPane zoomPane = new StackPane();
    zoomPane.getChildren().add(group);

    Group content = new Group(zoomPane, pane);
    scrollPane.setContent(group);

    // group.setScaleX(group.getScaleX() / 1.1);
    // group.setScaleY(group.getScaleY() / 1.1);

    zoomPane.setOnScroll(
        new EventHandler<ScrollEvent>() {
          @Override
          public void handle(ScrollEvent event) {
            event.consume();

            if (event.getDeltaY() == 0) {
              return;
            }

            double scaleFactor = (event.getDeltaY() > 0) ? 1.1 : 1 / 1.1;

            // amount of scrolling in each direction in scrollContent coordinate
            // units
            Point2D scrollOffset = figureScrollOffset(content, scrollPane);

            group.setScaleX(group.getScaleX() * scaleFactor);
            group.setScaleY(group.getScaleY() * scaleFactor);

            // move viewport so that old center remains in the center after the
            // scaling
            repositionScroller(content, scrollPane, scaleFactor, scrollOffset);
          }
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

    alertCodeField.getItems().add("Code Red");
    alertCodeField.getItems().add("Code Grey");
    alertCodeField.getItems().add("Code Blue");
    alertCodeField.getItems().add("Code Green");
    alertCodeField.getItems().add("Code White");
    alertCodeField.getItems().add("Code Pink");
    alertCodeField.getItems().add("Code Amber");

    // floorLocations.remove(0, floorLocations.size());

    totalLocations.addAll(FXCollections.observableList(facadeDAO.getAllLocations()));
    map.setImage(new Image("edu/wpi/cs3733/D22/teamZ/images/1.png"));
    // floorLocations.addAll(totalLocations.filtered(loc -> loc.getFloor().equalsIgnoreCase("1")));

    // initLabels();

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
    parentDataList = new ArrayList<>();
    parentDataList.addAll(facadeDAO.getAllLocations());
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
                  this.displayResult.size() * 19 // row height
                      + 2); // this gets called way too much, but whatever
              // System.out.println("height changed");
            });

    searchResultList.setCellFactory(
        new Callback<ListView<String>, ListCell<String>>() {
          @Override
          public ListCell<String> call(ListView<String> param) {
            return new ListCell<>() {
              @Override
              protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setFont(Font.font(9));
              }
            };
          }
        });

    // if user has clicked out of label, and on an empty part of the pane, disable buttons and
    // unenlarge previous label
    pane.addEventFilter(
        MouseEvent.MOUSE_CLICKED,
        evt -> {
          if (!inHierarchy(evt.getPickResult().getIntersectedNode(), activeLabel)) {
            pane.requestFocus();

            editLocation.setDisable(true);
            deleteLocation.setDisable(true);
            addAlertButton.setDisable(true);

            /*floorLabel.setText("Floor: ");
            longnameLabel.setText("Long Name: ");
            xCoordLabel.setText("xCoord: ");
            yCoordLabel.setText("yCoord: ");*/
          }
        });

    pane.addEventFilter(
        MouseEvent.MOUSE_CLICKED,
        evt -> {
          Node clicked = evt.getPickResult().getIntersectedNode();

          if (clicked instanceof Pane) {
            pane.requestFocus();
          }

          List<MapLabel> temp = allLabels.filtered(l -> l.equals(clicked));
          if (temp.size() > 0) {
            activeLabel = temp.get(0);
            System.out.println(activeLabel.getLocation().getLongName());
            // displayLocationInformation();
          }
        });

    scrollPane.addEventFilter(
        MouseEvent.MOUSE_CLICKED,
        evt -> {
          if (evt.getClickCount() > 1) {
            doubleClickAdd(evt);
          }
        });

    MenuItem edit = new MenuItem("Edit");
    edit.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            try {
              editLocationButtonClicked();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });
    MenuItem delete = new MenuItem("Delete");
    delete.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            try {
              deleteLocationButtonClicked();
            } catch (IOException e) {
              e.printStackTrace();
            }
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
    MenuItem alert = new MenuItem("Alert");
    alert.setOnAction(
        event -> {
          try {
            showAlertPane();
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
    // prop.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));

    rightClickMenu = new ContextMenu(edit, delete, prop, alert);

    rightClickMenu.setPrefHeight(82);
    rightClickMenu.setPrefWidth(120);

    this.displayResult.remove(5, this.displayResult.size());

    // Daniel's Stuff
    //    deleteLocationPlane.setVisible(false);
    //   deleteLocationPlane.setDisable(true);

    locRadio.setToggleGroup(radioGroup);
    locRadio.setUserData("Locations");
    equipRadio.setToggleGroup(radioGroup);
    equipRadio.setUserData("Equipment");
    servRadio.setToggleGroup(radioGroup);
    servRadio.setUserData("Service Requests");

    radioGroup
        .selectedToggleProperty()
        .addListener(
            new ChangeListener<Toggle>() {
              @Override
              public void changed(
                  ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                refreshMap(changeFloor.getSelectionModel().getSelectedItem());
              }
            });

    refreshMap("1");
  }

  private void propertiesWindow() throws IOException {
    Stage stage = new Stage();
    TabPane root = new TabPane();
    // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    root.setPrefWidth(600);
    root.setPrefHeight(440);

    // 3 tabs: do service request tab

    stage.setTitle("Properties");
    stage.getIcons().add(new Image("edu/wpi/cs3733/D22/teamZ/images/Hospital-Logo.png"));
    stage.setResizable(false);

    Tab locInfoTab = new Tab("Location Info");
    Tab medInfoTab = new Tab("Medical Equipment Info");
    Tab servReqTab = new Tab("Service Request Info");

    AnchorPane medPane = new AnchorPane();
    loadMedPane(medPane);
    medInfoTab.setContent(medPane);

    root.getTabs().addAll(medInfoTab, servReqTab, locInfoTab);
    root.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

    Pane locPane = new Pane();
    ObservableList<String> locationHeads = FXCollections.observableList(new ArrayList<>());
    ObservableList<String> locationInfo = FXCollections.observableList(new ArrayList<>());

    locationInfo.add(activeLabel.getLocation().getLongName());
    locationInfo.add(activeLabel.getLocation().getShortName());
    locationInfo.add(activeLabel.getLocation().getNodeID());
    locationInfo.add(activeLabel.getLocation().getNodeType());
    locationInfo.add(activeLabel.getLocation().getFloor());
    locationInfo.add(String.valueOf(activeLabel.getLocation().getXcoord()));
    locationInfo.add(String.valueOf(activeLabel.getLocation().getYcoord()));

    locationHeads.add("Long name:");
    locationHeads.add("Short name:");
    locationHeads.add("Node ID:");
    locationHeads.add("Node Type:");
    locationHeads.add("Floor:");
    locationHeads.add("XCoord:");
    locationHeads.add("YCoord:");

    ListView<String> info = new ListView<>(locationInfo);
    ListView<String> heads = new ListView<>(locationHeads);
    heads.setPrefWidth(85);
    info.setPrefWidth(300);
    info.setPrefHeight(7 * 24);
    heads.setPrefHeight(7 * 24);
    info.relocate(85, 0);

    info.getSelectionModel()
        .selectedIndexProperty()
        .addListener(
            (observable, oldValue, newValue) ->
                Platform.runLater(() -> info.getSelectionModel().select(-1)));
    heads
        .getSelectionModel()
        .selectedIndexProperty()
        .addListener(
            (observable, oldValue, newValue) ->
                Platform.runLater(() -> heads.getSelectionModel().select(-1)));

    locPane.getChildren().addAll(info, heads);

    locInfoTab.setContent(locPane);

    // Service Request page stuff
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getClassLoader().getResource(toServiceRequestProperties));
    Node serviceReqPage = loader.load();
    ((ServiceRequestPropertiesController) (loader.getController()))
        .setRequests(activeLabel.getReqs());

    servReqTab.setContent(serviceReqPage);

    Scene window = new Scene(root);
    stage.setScene(window);
    stage.show();
  }

  private void loadMedPane(AnchorPane pane) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    pane.getChildren()
        .add(
            loader.load(
                getClass()
                    .getClassLoader()
                    .getResource("edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentInfoTab.fxml")));
  }

  private void showLocations(String floor) {
    group.getChildren().removeIf(child -> child instanceof MapLabel);

    for (int i = 0; i < allLabels.size(); i++) {
      if (allLabels.get(i).isOnFloor(floor)) {
        switch (radioGroup.getSelectedToggle().getUserData().toString()) {
          case "Locations":
            group.getChildren().add(allLabels.get(i));
            break;
          case "Equipment":
            if (allLabels.get(i).getEquip().size() > 0) {
              group.getChildren().add(allLabels.get(i));
            }
            break;
          case "Service Requests":
            System.out.println("serv");
            if (allLabels.get(i).getReqs().size() > 0) {
              group.getChildren().add(allLabels.get(i));
            }
            break;
          default:
            System.out.println("lolno");
            break;
        }
      }
    }
  }

  // function to check if user has clicked outside of label
  public static boolean inHierarchy(Node node, Node potentialHierarchyElement) {
    if (potentialHierarchyElement == null) {
      return true;
    }
    while (node != null) {
      if (node == potentialHierarchyElement) {
        return true;
      }
      node = node.getParent();
    }
    return false;
  }

  // Andrew's Stuff
  /**
   * Will update location data in database given text inputs on app
   *
   * @param event
   * @throws IOException
   */
  @FXML
  private void submitEditLocationButtonClicked(ActionEvent event) throws IOException {

    if (Integer.parseInt(changeNumberTextField.getText()) > 0) {
      // good do nothing
    } else {
      return;
    }

    // change later to Neha's nodeID info
    Location tempLocation = facadeDAO.getLocationByID(selectLocationTextField.getText());

    // old floor
    String oldFloor = tempLocation.getFloor();

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
      if (medicalEquipmentList.isEmpty()) {
        // do nothing
      }
      // there is equipment so update to new location
      else {
        for (int i = 0; i < medicalEquipmentList.size(); i++) {
          MedicalEquipment tempMedEquip = medicalEquipmentList.get(i);
          tempMedEquip.setCurrentLocation(tempLocation);
          facadeDAO.updateMedicalEquipment(tempMedEquip);
        }
      }

      if (facadeDAO.deleteLocation(oldLoc)) {
        System.out.println("Delete location successful");
      }
      editLocationPane.setVisible(false);
      locationChangeDarkenPane.setVisible(false);

      refreshMap(floorChoiceTextField.getSelectionModel().getSelectedItem().toString());
      refreshMap(oldFloor);
      changeFloor
          .getSelectionModel()
          .select(floorChoiceTextField.getSelectionModel().getSelectedItem().toString());

      changeToFloor(floorChoiceTextField.getSelectionModel().getSelectedItem().toString());

    } else {
      alreadyExistsText.setVisible(true);
    }
  }

  @FXML
  private void clearEditLocationButtonClicked(ActionEvent event) throws IOException {
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
  private void exitEditLocationButtonClicked(ActionEvent event) throws IOException {
    locationChangeDarkenPane.setVisible(false);
    editLocationPane.setVisible(false);
    locationChangeDarkenPane.setDisable(true);
    editLocationPane.setDisable(true);
  }

  // Casey's
  @FXML
  public void search(KeyEvent keyEvent) {
    searchField.requestFocus();
    List<ISearchable> tempResultList = new ArrayList<>();
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
  public void resultMouseClick(MouseEvent mouseEvent) {
    // System.out.println(searchResultList.getSelectionModel().getSelectedItem());

    String searched = searchResultList.getSelectionModel().getSelectedItem();

    for (MapLabel label : allLabels) {
      if (label.getLocation().getLongName().equals(searched)) {
        activeLabel = label;
      }
    }

    String selectedItem = activeLabel.getLocation().getFloor();
    changeToFloor(selectedItem);

    activeLabel.requestFocus();
    // activeLabel = allLabels.get(theoreticalGenericIndex);
    searchField.setText(activeLabel.getLocation().getLongName());
    // displayLocationInformation();
  }

  private void changeToFloor(String nFloor) {
    // floorLocations.remove(0, floorLocations.size());
    // floorLocations.addAll(totalLocations.filtered(loc ->
    // loc.getFloor().equalsIgnoreCase(nFloor)));
    map.setImage(new Image("edu/wpi/cs3733/D22/teamZ/images/" + nFloor + ".png"));
    showLocations(nFloor);
  }

  private void initLabels() {
    allLabels.remove(0, allLabels.size());
    for (int i = 0; i < totalLocations.size(); i++) {

      Location current = totalLocations.get(i);

      MapLabel label =
          new MapLabel.mapLabelBuilder()
              .location(current)
              .equipment(facadeDAO.getAllMedicalEquipmentByLocation(current))
              // @TODO add location stuff .requests(facadeDAO.getAllServiceRequestsByLocation())
              .build();

      // stylize label icon
      Image locationImg;
      ImageView locationIcon = null;

      switch (radioGroup.getSelectedToggle().getUserData().toString()) {
        case "Locations":
          locationImg = new Image("edu/wpi/cs3733/D22/teamZ/images/location.png");
          locationIcon = new ImageView(locationImg);
          break;
        case "Equipment":
          locationImg = new Image("edu/wpi/cs3733/D22/teamZ/images/equipment.png");
          locationIcon = new ImageView(locationImg);
          break;
        case "Service Requests":
          locationImg = new Image("edu/wpi/cs3733/D22/teamZ/images/servicerequest.png");
          locationIcon = new ImageView(locationImg);
          break;
        default:
          System.out.println("hopefully not");
          break;
      }

      DropShadow dropShadow = new DropShadow();
      dropShadow.setRadius(5.0);
      dropShadow.setOffsetX(3.0);
      dropShadow.setOffsetY(3.0);
      dropShadow.setColor(Color.GRAY);

      // create the label
      label.setEffect(dropShadow);
      label.setGraphic(locationIcon);

      label
          .focusedProperty()
          .addListener(
              (observable, oldValue, newValue) -> {
                if (!newValue) {
                  label.setScaleX(.7);
                  label.setScaleY(.7);
                  // returnOnClick();
                } else {
                  label.setScaleX(1.1);
                  label.setScaleY(1.1);
                }
              });

      label.setScaleX(.7);
      label.setScaleY(.7);
      label.setOnMouseClicked(
          evt -> {
            label.requestFocus();
          });
      // place label at correct coords
      label.relocate(
          (label.getLocation().getXcoord() - 12) * (map.getFitWidth() / 1021),
          (label.getLocation().getYcoord() - 24) * (map.getFitHeight() / 850));

      label.setContextMenu(rightClickMenu);
      label.setOnContextMenuRequested(
          new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
              rightClickMenu.show(label, event.getScreenX(), event.getScreenY());
              Location loc = label.getLocation();
            }
          });
      allLabels.add(label);
    }
  }

  private void refreshMap(String floor) {
    totalLocations.remove(0, totalLocations.size());
    totalLocations.addAll(facadeDAO.getAllLocations());
    initLabels();
    showLocations(floor);
  }

  @FXML
  public void deleteLocation() throws IOException {
    Location temp = facadeDAO.getLocationByID(locationToDeleteTextField.getText());
    if (temp.getNodeID().equals(null)) {
      System.out.println("Did not find location in database");
      return;
    }
    if (facadeDAO.deleteLocation(temp)) {
      System.out.println("Deletion Successful");
      // TODO: fix
      refreshMap(activeLabel.getLocation().getFloor());
    } else {
      System.out.println("There are still stuff in this location");
    }

    locationChangeDarkenPane.setVisible(false);
    deleteLocationPlane.setVisible(false);
    locationChangeDarkenPane.setDisable(true);
    deleteLocationPlane.setDisable(true);
  }

  @FXML
  public void cancelLocationToDelete() throws IOException {
    locationChangeDarkenPane.setVisible(false);
    deleteLocationPlane.setVisible(false);
    locationChangeDarkenPane.setDisable(true);
    deleteLocationPlane.setDisable(true);
  }

  @FXML
  private void deleteLocationButtonClicked() throws IOException {
    locationChangeDarkenPane.setVisible(true);
    deleteLocationPlane.setVisible(true);
    locationChangeDarkenPane.setDisable(false);
    deleteLocationPlane.setDisable(false);
    locationToDeleteTextField.setText(activeLabel.getLocation().getNodeID());
  }

  @FXML
  private void addLocationButtonClicked(ActionEvent evt) throws IOException {
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
    xCoordTextField.setText(String.valueOf((int) evt.getSceneX()));
    yCoordTextField.setText(String.valueOf((int) evt.getSceneY()));
  }

  @FXML
  private void cancelAddLocation(ActionEvent event) throws IOException {
    locationChangeDarkenPane.setVisible(false);
    addLocationPane.setVisible(false);
    locationChangeDarkenPane.setDisable(true);
    addLocationPane.setDisable(true);
  }

  @FXML
  private void addLocation() throws IOException {
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
    newLocation.setNodeType(locationTypeField.getValue().toString());
    newLocation.setFloor(floorField.getValue().toString());
    newLocation.setLongName(locationNameTextField.getText());
    newLocation.setShortName(nameAbbreviationTextField.getText());
    newLocation.setBuilding("Tower");

    // generate a node id
    // generate numb
    List<Location> locations = facadeDAO.getAllLocationsByFloor(floorField.getValue().toString());
    int size =
        (int)
            locations.stream()
                .filter(
                    unusedLocation ->
                        unusedLocation
                            .getNodeType()
                            .equalsIgnoreCase(locationTypeField.getValue().toString()))
                .count();

    String newNodeID =
        "z"
            + locationTypeField.getValue()
            + "0".repeat(3 - Integer.toString(size + 1).length())
            + Integer.toString(size + 1)
            + "0".repeat(2 - floorField.getValue().toString().length())
            + floorField.getValue();

    newLocation.setNodeID(newNodeID);

    // check if exists, if not add it
    if (facadeDAO.getLocationByID(newNodeID).getNodeID() == null) {
      // add it
      facadeDAO.addLocation(newLocation);
    } else {
      return;
    }

    // initLabels();
    int floorIndex = floorField.getSelectionModel().getSelectedIndex();
    changeFloor.getSelectionModel().select(floorIndex);

    changeToFloor(changeFloor.getSelectionModel().getSelectedItem());
    refreshMap(changeFloor.getSelectionModel().getSelectedItem());

    activeLabel = allLabels.get(allLabels.size() - 1);

    addLocationPane.setVisible(false);
    locationChangeDarkenPane.setVisible(false);
    addLocationPane.setDisable(true);
    locationChangeDarkenPane.setDisable(true);
  }

  public void exportToCSV(ActionEvent actionEvent) {
    FileChooser fileChooser = new FileChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    fileChooser.setTitle("Enter a .csv file...");
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);

    File file = fileChooser.showSaveDialog(stage);

    facadeDAO.exportLocationsToCSV(file);
  }

  public void importFromCSV(ActionEvent actionEvent) {
    FileChooser fileChooser = new FileChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    fileChooser.setTitle("Enter a .csv file...");
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);

    File file = fileChooser.showOpenDialog(stage);

    int numberConflicts = facadeDAO.importLocationsFromCSV(file);

    refreshMap(changeFloor.getSelectionModel().getSelectedItem().toString());
    System.out.println(
        "Detected "
            + numberConflicts
            + " locations that are"
            + " trying to get deleted but still have equipment in it");
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

  /*public void showInfoDialog(Label labelEquip, Location loc, List<MedicalEquipment> equipment) {
    // Make the popup visible again, and reset the HBoxes' children.


    detailsPopup.setVisible(true);
    equipmentNames.getChildren().clear();
    equipmentQuantities.getChildren().clear();

    // Get a Dictionary of each type of equipment and how much of each is present.
    Dictionary<String, Integer> equipFrequency = getMedicalEquipmentInstances(equipment);

    // Create popup with information about medical equipment
    int i = 0;
    Enumeration<String> keys = equipFrequency.keys();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      // This entire system is hideous and should probably be replaced by a factory at some point.
      if (i == 0) {
        equipmentNames.getChildren().add(getPopupLabel(key, "#FFFFFF", 5, 0, 0, 0));
        equipmentQuantities
            .getChildren()
            .add(getPopupLabel(equipFrequency.get(key).toString(), "#FFFFFF", 0, 0, 5, 0));
      } else if (i == equipFrequency.size() - 1) {
        equipmentNames.getChildren().add(getPopupLabel(key, "#FFFFFF", 0, 5, 0, 0));
        equipmentQuantities
            .getChildren()
            .add(getPopupLabel(equipFrequency.get(key).toString(), "#FFFFFF", 0, 0, 0, 5));
      } else {
        equipmentNames.getChildren().add(getPopupLabel(key, "#FFFFFF", 0, 0, 0, 0));
        equipmentQuantities
            .getChildren()
            .add(getPopupLabel(equipFrequency.get(key).toString(), "#FFFFFF", 0, 0, 0, 0));
      }
      i++;
    }

    // Move the popup so that it is centered with the location, but well above the equipment icon.
    detailsPopup.relocate(loc.getXcoord() + 60, loc.getYcoord());
  }

  public Label getPopupLabel(
      String text, String color, int topLeft, int topRight, int botLeft, int botRight) {
    Label newLabel = new Label();

    // Set the padding of text, and the preferred width of the label
    newLabel.setPadding(new Insets(4, 0, 4, 0));
    newLabel.setPrefWidth(70);

    // Set the CSS of the label
    newLabel.setStyle(
        String.format(
            "-fx-border-width: 1px; -fx-background-color: %s; -fx-border-color: #000000; -fx-border-radius: %dpx %dpx %dpx %dpx; -fx-background-radius: %dpx %dpx %dpx %dpx",
            color, topLeft, topRight, botRight, botLeft, topLeft, topRight, botRight, botLeft));

    // Set and center the text
    newLabel.setText(text);
    newLabel.setAlignment(Pos.CENTER);
    return newLabel;
  }

  private Dictionary<String, Integer> getMedicalEquipmentInstances(List<MedicalEquipment> reqList) {
    // Make dictionary
    Dictionary<String, Integer> dict = new Hashtable<>();

    // For each piece of equipment...
    for (MedicalEquipment equipment : reqList) {

      // If the dictionary doesn't contain the item, add it in with one occurrence.
      if (dict.get(equipment.getType()) == null) {
        dict.put(equipment.getType(), 1);

        // If it is already in, add 1 to the amount of times it occurs.
      } else {
        dict.put(equipment.getType(), dict.get(equipment.getType()) + 1);
      }
    }

    return dict;
  }*/

  /*public void contextMenuClick(MouseEvent mouseEvent) throws IOException {
    int id = rightClickMenu.getSelectionModel().getSelectedIndex();
    rightClickMenu.setVisible(false);
    rightClickMenu.setDisable(true);
    rightClickMenu.getSelectionModel().clearSelection();
    switch (id) {
      case 0:
        editLocationButtonClicked();
        break;
      case 1:
        deleteLocationButtonClicked();
        break;
    }
  }*/

  private Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {
    double extraWidth =
        scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
    double hScrollProportion =
        (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
    double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
    double extraHeight =
        scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
    double vScrollProportion =
        (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
    double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
    return new Point2D(scrollXOffset, scrollYOffset);
  }

  private void repositionScroller(
      Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
    double scrollXOffset = scrollOffset.getX();
    double scrollYOffset = scrollOffset.getY();
    double extraWidth =
        scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
    if (extraWidth > 0) {
      double halfWidth = scroller.getViewportBounds().getWidth() / 2;
      double newScrollXOffset = (scaleFactor - 1) * halfWidth + scaleFactor * scrollXOffset;
      scroller.setHvalue(
          scroller.getHmin()
              + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
    } else {
      scroller.setHvalue(scroller.getHmin());
    }
    double extraHeight =
        scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
    if (extraHeight > 0) {
      double halfHeight = scroller.getViewportBounds().getHeight() / 2;
      double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
      scroller.setVvalue(
          scroller.getVmin()
              + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
    } else {
      scroller.setHvalue(scroller.getHmin());
    }
  }

  @FXML
  public void showAlertPane() throws IOException {
    addAlertPane.setVisible(true);
    alertLocationField.setText(activeLabel.getLocation().getNodeID());
    submitAlert.setOnAction(
        (e) -> {
          createAlert(
              alertCodeField.getSelectionModel().getSelectedItem().toString(),
              activeLabel.getLocation());
          addAlertPane.setVisible(false);
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
    ImageView locationIcon = icon;
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
    label.setGraphic(locationIcon);
    label.relocate(location.getXcoord() + 2, location.getYcoord() + 2);
    label.setContextMenu(contextMenu);
    contextMenu.getItems().add(menuItem1);
    menuItem1.setOnAction(
        (e) -> {
          // remove from map
        });
    MapLabel mapLabel = new MapLabel.mapLabelBuilder().label(label).location(location).build();

    // System.out.println("adding label");
    // allLabels.add(mapLabel);
  }

  public Location findClosestExit(Location location) {
    Location closestExit = new Location();
    Location current = new Location();
    double distance;
    double bestDistance = 1000;
    int index = 0;

    for (int i = 0; i < totalLocations.size(); i++) {
      if (totalLocations.get(i).getNodeType().equals("EXIT")
          && totalLocations.get(i).getFloor().equals(location.getFloor())) {
        current = totalLocations.get(i);
        distance =
            Math.sqrt(
                (Math.pow((current.getXcoord() - location.getXcoord()), 2))
                    + (Math.pow((current.getYcoord() - location.getYcoord()), 2)));
        if (distance < bestDistance) {
          bestDistance = distance;
          closestExit = current;
          index = i;
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
