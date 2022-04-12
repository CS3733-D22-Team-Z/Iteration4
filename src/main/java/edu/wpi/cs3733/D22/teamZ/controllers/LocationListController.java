package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
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
public class LocationListController {

  // init ui components
  @FXML private Pane pane;
  @FXML private Label floorLabel;
  @FXML private Label longnameLabel;
  @FXML private Label xCoordLabel;
  @FXML private Label yCoordLabel;
  @FXML private Button exitButton;
  @FXML private ChoiceBox changeFloor;
  @FXML private ImageView map;
  @FXML private Button editLocation;
  @FXML private Button deleteLocation;
  @FXML private ScrollPane scrollPane;
  @FXML private Group group;
  @FXML private StackPane stackPane;
  @FXML private AnchorPane anchorPane;
  @FXML private Pane alertPane;
  @FXML private Button addAlertButton;
  @FXML private ChoiceBox selectAlertChoice;
  @FXML private Button submitAlert;
  @FXML private ImageView arrowGIF1;
  @FXML private ImageView arrowGIF2;
  @FXML private ImageView arrowGIF3;

  // Andrew's stuff
  @FXML private TextField selectLocationTextField;
  @FXML private ChoiceBox<String> typeChoiceTextField;
  @FXML private ChoiceBox<String> floorChoiceTextField;
  @FXML private TextField changeNumberTextField;
  @FXML private TextField changeNameTextField;
  @FXML private TextField abbreviationTextField;
  @FXML private Text alreadyExistsText;
  @FXML private Button submitButton;
  @FXML private Button clearButton;
  @FXML private Button editLocationExitButton;
  @FXML private Pane editLocationPane;
  @FXML private Pane locationChangeDarkenPane;
  private Location activeLocation;
  private Label activeLabel;
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
  @FXML private Button deleteMapLocation;
  @FXML private Button cancelLocationSelection;
  // text field box to select location to delete
  @FXML private TextField locationToDeleteTextField;
  @FXML private Pane deleteLocationPlane;
  @FXML private TextField selectAlertLocation;

  // Neha's stuff
  // Buttons
  @FXML private Button submitAddLocation;
  @FXML private Button clearAddLocation;
  @FXML private Button addLocationExitButton;
  // text fields
  @FXML private TextField xCoordTextField;
  @FXML private TextField yCoordTextField;
  @FXML private ChoiceBox locationTypeField;
  @FXML private ChoiceBox floorField;
  @FXML private TextField locationNameTextField;
  @FXML private TextField nameAbbreviationTextField;
  // pane
  @FXML private Pane addLocationPane;

  // Patricks stuff
  @FXML private VBox detailsPopup;
  @FXML private HBox equipmentNames;
  @FXML private HBox equipmentQuantities;

  // urls to other pages
  private String toLocationsURL = "edu/wpi/cs3733/D22/teamZ/views/Location.fxml";
  private String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";
  private String toMedicalEquipmentRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentRequestList.fxml";
  private String toHomeURL = "edu/wpi/cs3733/D22/teamZ/views/Homepage.fxml";
  private String toEquipmentMapURL = "edu/wpi/cs3733/D22/teamZ/views/EquipmentMap.fxml";

  // init LocationDAOImpl to use sql methods from db
  FacadeDAO facadeDAO = FacadeDAO.getInstance();

  // create ObservableList to load locations into map
  // private ObservableList<Location> floorLocations = FXCollections.observableList(new
  // ArrayList<>());
  private ObservableList<Location> totalLocations = FXCollections.observableList(new ArrayList<>());
  private ObservableList<Label> allLabels = FXCollections.observableList(new ArrayList<>());
  private ObservableList<Label> equipLabels = FXCollections.observableList(new ArrayList<>());
  private ObservableList<Label> alertLabels = FXCollections.observableList(new ArrayList<>());

  // initialize location labels to display on map
  @FXML
  private void initialize() {
    System.out.println("loading labels");

    changeFloor.getItems().add("L2");
    changeFloor.getItems().add("L1");
    changeFloor.getItems().add("1");
    changeFloor.getItems().add("2");
    changeFloor.getItems().add("3");

    floorField.getItems().add("L2");
    floorField.getItems().add("L1");
    floorField.getItems().add("1");
    floorField.getItems().add("2");
    floorField.getItems().add("3");

    selectAlertChoice.getItems().add("Code Red");
    selectAlertChoice.getItems().add("Code Grey");
    selectAlertChoice.getItems().add("Code Blue");
    selectAlertChoice.getItems().add("Code Green");
    selectAlertChoice.getItems().add("Code White");
    selectAlertChoice.getItems().add("Code Pink");
    selectAlertChoice.getItems().add("Code Amber");

    // floorLocations.remove(0, floorLocations.size());

    scrollPane.setPannable(true);

    totalLocations.addAll(FXCollections.observableList(facadeDAO.getAllLocations()));
    map.setImage(new Image("edu/wpi/cs3733/D22/teamZ/images/1.png"));
    // floorLocations.addAll(totalLocations.filtered(loc -> loc.getFloor().equalsIgnoreCase("1")));

    // initLabels();

    // showLocations("1");
    changeFloor.getSelectionModel().select(2);
    refreshMap("1");

    // change floor with dropdown
    changeFloor.setOnAction(
        (event) -> {
          String selectedItem = changeFloor.getSelectionModel().getSelectedItem().toString();

          // System.out.println("Selection made: [" + selectedIndex + "] " + selectedItem);
          // System.out.println("   ChoiceBox.getValue(): " + changeFloor.getValue());
          // get list of locations from db and transfer into ObservableList
          System.out.println(selectedItem);
          changeToFloor(selectedItem);
        });

    StackPane zoomPane = new StackPane();
    zoomPane.getChildren().add(group);

    Group content = new Group(zoomPane);
    scrollPane.setContent(content);

    group.setScaleX(group.getScaleX() / 1.1);
    group.setScaleY(group.getScaleY() / 1.1);

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

    multiFocusProperty.addListener(
        (observable, oldValue, newValue) -> {
          // System.out.println("vis swap");
          searchResultList.setVisible(newValue);
          searchResultList.setDisable(!newValue);
        });

    multiFocusProperty.bind(searchField.focusedProperty().or(searchResultList.focusedProperty()));

    this.displayResult.addListener(
        (ListChangeListener<String>)
            c -> {
              searchResultList.setPrefHeight(
                  this.displayResult.size() * 40 // row height
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
                setFont(Font.font(20));
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
            activeLabel.setScaleX(1);
            activeLabel.setScaleY(1);

            editLocation.setDisable(true);
            deleteLocation.setDisable(true);
            addAlertButton.setDisable(true);

            floorLabel.setText("Floor: ");
            longnameLabel.setText("Long Name: ");
            xCoordLabel.setText("xCoord: ");
            yCoordLabel.setText("yCoord: ");
          }
        });

    this.displayResult.remove(5, this.displayResult.size());

    // Daniel's Stuff
    deleteLocationPlane.setVisible(false);
    deleteLocationPlane.setDisable(true);
  }

  private void showLocations(String floor) {
    pane.getChildren().clear();

    pane.getChildren()
        .addAll(
            alertLabels.filtered(
                label -> {
                  Location temp = totalLocations.get(alertLabels.indexOf(label));
                  return temp.getFloor().equalsIgnoreCase(floor);
                  // && !temp.getNodeType()
                  // .equalsIgnoreCase("hall"); // disable line to enable halls
                }));

    pane.getChildren()
        .addAll(
            equipLabels.filtered(
                label -> {
                  return changeFloor.getSelectionModel().getSelectedItem().toString().equals("3");
                }));

    pane.getChildren()
        .addAll(
            allLabels.filtered(
                label -> {
                  Location temp = totalLocations.get(allLabels.indexOf(label));
                  return temp.getFloor().equalsIgnoreCase(floor);
                  // && !temp.getNodeType()
                  // .equalsIgnoreCase("hall"); // disable line to enable halls
                }));
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

  // when a location label is clicked on map, information about that label is shown on the side
  private void displayLocationInformation() {

    activeLabel.requestFocus();
    activeLabel.setScaleX(2);
    activeLabel.setScaleY(2);
    // update labels to correct info
    floorLabel.setText("Floor: " + activeLocation.getFloor());
    longnameLabel.setText("Long Name: " + activeLocation.getLongName());
    xCoordLabel.setText("xCoord: " + String.valueOf(activeLocation.getXcoord()));
    yCoordLabel.setText("yCoord: " + String.valueOf(activeLocation.getYcoord()));

    // set the labels visible
    floorLabel.setVisible(true);
    longnameLabel.setVisible(true);
    xCoordLabel.setVisible(true);
    yCoordLabel.setVisible(true);

    editLocation.setDisable(false);
    deleteLocation.setDisable(false);
    addAlertButton.setDisable(false);
  }

  // when locations menu button is clicked navigate to locations page
  @FXML
  private void toLocations(ActionEvent event) throws IOException {
    System.out.println("navigating to locations from home");
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(toLocationsURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  // when landing page menu button is clicked navigate to landing page
  @FXML
  private void toLandingPage(ActionEvent event) throws IOException {
    System.out.println("navigating to landing page from home");
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(toLandingPageURL));
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  // when medical equipment request button is clicked on menu navigate to medical equipment request
  // page
  @FXML
  private void toMedicalEquipmentRequest(ActionEvent event) throws IOException {
    System.out.println("navigating to Medical Equipment Request page from home");
    Parent root =
        FXMLLoader.load(getClass().getClassLoader().getResource(toMedicalEquipmentRequestURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  // when home button on menu is clicked navigate to home page
  @FXML
  private void toHome(ActionEvent event) throws IOException {
    System.out.println("navigating to home using home button on sidebar");
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(toHomeURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  // when the medical equipment map menu button is clicked navigate to medical equipment map page
  @FXML
  private void toEquipmentMap(ActionEvent event) throws IOException {
    System.out.println("navigating to medical equipment map from home");
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(toEquipmentMapURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  // when exit button is clicked on menu exit application
  @FXML
  private void toExit(ActionEvent event) {
    System.out.println("exit the app using exit button bottom left");
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
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

      activeLocation = tempLocation;
      activeLabel =
          allLabels.get(
              totalLocations
                  .filtered(loc -> loc.getNodeID().equalsIgnoreCase(activeLocation.getNodeID()))
                  .getSourceIndex(0));
      displayLocationInformation();
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
  private void editLocationButtonClicked(ActionEvent event) throws IOException {
    locationChangeDarkenPane.setVisible(true);
    editLocationPane.setVisible(true);
    locationChangeDarkenPane.setDisable(false);
    editLocationPane.setDisable(false);
    selectLocationTextField.setText(activeLocation.getNodeID());
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

    List<String> longNames = new ArrayList<>();
    for (ISearchable loc : parentDataList) {
      longNames.add(loc.getDisplayName());
    }
    int theoreticalGenericIndex =
        longNames.indexOf(searchResultList.getSelectionModel().getSelectedItem());

    activeLocation = parentDataList.get(theoreticalGenericIndex).getAssociatedLocation();

    String selectedItem = activeLocation.getFloor();
    changeToFloor(selectedItem);

    activeLabel = allLabels.get(theoreticalGenericIndex);
    searchField.setText(activeLocation.getLongName());
    displayLocationInformation();
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
      // styilize label icon
      Image locationImg = new Image("edu/wpi/cs3733/D22/teamZ/images/location.png");
      ImageView locationIcon = new ImageView(locationImg);
      Location current = totalLocations.get(i);
      DropShadow dropShadow = new DropShadow();
      dropShadow.setRadius(5.0);
      dropShadow.setOffsetX(3.0);
      dropShadow.setOffsetY(3.0);
      dropShadow.setColor(Color.GRAY);

      // create the label
      Label label = new Label();
      label.setEffect(dropShadow);
      label.setGraphic(locationIcon);

      // equipment labels
      List<MedicalEquipment> medicalEquipmentAtLocation =
          facadeDAO.getAllMedicalEquipmentByLocation(current);

      // If there is medical equipment at the location, then proceed.
      if (!medicalEquipmentAtLocation.isEmpty()) {

        // Setup icon image view
        Image equipmentImg = new Image("edu/wpi/cs3733/D22/teamZ/images/equipment.png");
        ImageView equipmentIcon = new ImageView(equipmentImg);

        DropShadow dropShadowEquip = new DropShadow();
        dropShadowEquip.setRadius(5.0);
        dropShadowEquip.setOffsetX(3.0);
        dropShadowEquip.setOffsetY(3.0);
        dropShadowEquip.setColor(Color.GRAY);

        // create the label
        Label labelEquip = new Label();
        labelEquip.setEffect(dropShadowEquip);
        labelEquip.setGraphic(equipmentIcon);
        labelEquip.setPrefWidth(7);
        labelEquip.setPrefHeight(7);

        labelEquip.relocate(current.getXcoord() - 8, current.getYcoord() - 8);
        equipLabels.add(labelEquip);
        System.out.println("adding a label");

        labelEquip.setOnMouseClicked(
            (e) -> {
              showInfoDialog(
                  labelEquip, current, facadeDAO.getAllMedicalEquipmentByLocation(current));
            });

        // pane.getChildren().add(labelEquip);
      }

      // call function when clicked to display information about that location label on side
      label.setOnMouseClicked(
          (e) -> {
            activeLocation = current;
            activeLabel = label;
            displayLocationInformation();
          });
      label
          .focusedProperty()
          .addListener(
              (observable, oldValue, newValue) -> {
                if (!newValue) {
                  label.setScaleX(1);
                  label.setScaleY(1);
                }
              });

      // place label at correct coords
      label.relocate(current.getXcoord() - 8, current.getYcoord() - 10);
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
      refreshMap(activeLocation.getFloor());
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
  public void cancelAddAlert() throws IOException {
    locationChangeDarkenPane.setVisible(false);
    alertPane.setVisible(false);
    locationChangeDarkenPane.setDisable(true);
    alertPane.setDisable(true);
  }

  @FXML
  private void deleteLocationButtonClicked(ActionEvent event) throws IOException {
    locationChangeDarkenPane.setVisible(true);
    deleteLocationPlane.setVisible(true);
    locationChangeDarkenPane.setDisable(false);
    deleteLocationPlane.setDisable(false);
    locationToDeleteTextField.setText(activeLocation.getNodeID());
  }

  @FXML
  private void addLocationButtonClicked(ActionEvent event) throws IOException {
    locationChangeDarkenPane.setVisible(true);
    addLocationPane.setVisible(true);
    locationChangeDarkenPane.setDisable(false);
    addLocationPane.setDisable(false);
    // selectLocationTextField.setText(activeLocation.getNodeID());
  }

  @FXML
  private void cancelAddLocation(ActionEvent event) throws IOException {
    locationChangeDarkenPane.setVisible(false);
    addLocationPane.setVisible(false);
    locationChangeDarkenPane.setDisable(true);
    addLocationPane.setDisable(true);
  }

  @FXML
  private void addLocation(ActionEvent event) throws IOException {

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

    changeToFloor(changeFloor.getSelectionModel().getSelectedItem().toString());
    refreshMap(changeFloor.getSelectionModel().getSelectedItem().toString());

    activeLocation = newLocation;
    activeLabel = allLabels.get(allLabels.size() - 1);
    displayLocationInformation();

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

  public void showInfoDialog(Label labelEquip, Location loc, List<MedicalEquipment> equipment) {
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
  }

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

  public void showAlert(Location location) {
    Image locationImg = new Image("edu/wpi/cs3733/D22/teamZ/images/alert.png");
    ImageView locationIcon = new ImageView(locationImg);
    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(5.0);
    dropShadow.setOffsetX(3.0);
    dropShadow.setOffsetY(3.0);
    dropShadow.setColor(Color.GRAY);
    // create the label
    Label label = new Label();
    label.setEffect(dropShadow);
    label.setGraphic(locationIcon);
    label.relocate(location.getXcoord() + 20, location.getYcoord() + 20);
    // pane.getChildren().add(label);
    alertLabels.add(label);
    label.setOnMouseClicked(
        (e) -> {
          activeLocation = location;
          activeLabel = label;
        });
  }

  @FXML
  public void showAlertPane(ActionEvent Event) {
    locationChangeDarkenPane.setVisible(true);
    locationChangeDarkenPane.setDisable(false);
    alertPane.setVisible(true);
    alertPane.setDisable(false);
    selectAlertLocation.setText(activeLocation.getNodeID());
    submitAlert.setOnAction(
        (e) -> {
          createAlert(
              selectAlertChoice.getSelectionModel().getSelectedItem().toString(), activeLocation);
          alertPane.setVisible(false);
          locationChangeDarkenPane.setVisible(false);
          locationChangeDarkenPane.setDisable(true);
          alertPane.setDisable(true);
        });
  }

  public void createAlert(String alert, Location location) {
    Alert newAlert = new Alert(Alert.AlertType.WARNING);
    switch (alert) {
      case "Code Red":
        ImageView redAlertIcon =
            new ImageView(new Image("edu/wpi/cs3733/D22/teamZ/images/RedAlert.png"));
        newAlert.setTitle("Code Red Alert");
        newAlert.setHeaderText("Code Red");
        newAlert.setContentText(
            "Fire at " + location.getLongName() + " on Floor " + location.getFloor());
        newAlert.setGraphic(redAlertIcon);
        createAlertLabel(redAlertIcon, location, newAlert);
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
        createAlertLabel(greyAlertIcon, location, newAlert);
        break;
      case "Code Green":
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
        createAlertLabel(greenAlertIcon, location, newAlert);
        break;
      case "Code White":
        ImageView whiteAlertIcon =
            new ImageView(new Image("edu/wpi/cs3733/D22/teamZ/images/WhiteAlert.png"));
        newAlert.setTitle("Code White Alert");
        newAlert.setHeaderText("Code White");
        newAlert.setContentText(
            "Bomb Threat at " + location.getLongName() + " on Floor " + location.getFloor());
        newAlert.setGraphic(whiteAlertIcon);
        createAlertLabel(whiteAlertIcon, location, newAlert);
        break;
      case "Code Pink":
        ImageView pinkAlertIcon =
            new ImageView(new Image("edu/wpi/cs3733/D22/teamZ/images/PinkAlert.png"));
        newAlert.setTitle("Code Pink Alert");
        newAlert.setHeaderText("Code Pink");
        newAlert.setContentText(
            "Infant Abduction at " + location.getLongName() + " on Floor " + location.getFloor());
        newAlert.setGraphic(pinkAlertIcon);
        createAlertLabel(pinkAlertIcon, location, newAlert);
        break;
      case "Code Amber":
        ImageView amberAlertIcon =
            new ImageView(new Image("edu/wpi/cs3733/D22/teamZ/images/AmberAlert.png"));
        newAlert.setTitle("Code Amber Alert");
        newAlert.setHeaderText("Code Amber");
        newAlert.setContentText("Disaster Plan in Effect");
        newAlert.setGraphic(amberAlertIcon);
        createAlertLabel(amberAlertIcon, location, newAlert);
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
        createAlertLabel(blueAlertIcon, location, newAlert);
        break;
      default:
        break;
    }
    newAlert.show();
    newAlert.setOnCloseRequest(
        (e) -> {
          refreshMap(location.getFloor());
          if (alert == "Code Red") {
            findClosestExit(location);
          }
        });
  }

  public void createAlertLabel(ImageView icon, Location location, Alert alert) {
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
    alertLabels.add(label);
    contextMenu.getItems().add(menuItem1);
    menuItem1.setOnAction(
        (e) -> {
          alertLabels.remove(label);
          arrowGIF1.setVisible(false);
          arrowGIF2.setVisible(false);
          arrowGIF3.setVisible(false);
          refreshMap(location.getFloor());
        });
  }

  public void findClosestExit(Location location) {
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
    switch (closestExit.getNodeID()) {
      case "zEXIT00101":
        arrowGIF1.setVisible(true);
        System.out.println(closestExit.getLongName());
        changeToFloor(closestExit.getFloor());
        activeLabel = allLabels.get(index);
        displayLocationInformation();

        break;
      case "zEXIT00201":
        arrowGIF2.setVisible(true);
        System.out.println(closestExit.getLongName());
        changeToFloor(closestExit.getFloor());
        activeLabel = allLabels.get(index);
        displayLocationInformation();
        break;
      case "zEXIT00301":
        arrowGIF3.setVisible(true);
        System.out.println(closestExit.getLongName());
        changeToFloor(closestExit.getFloor());
        activeLabel = allLabels.get(index);
        displayLocationInformation();
        break;
      default:
        break;
    }
  }
}
