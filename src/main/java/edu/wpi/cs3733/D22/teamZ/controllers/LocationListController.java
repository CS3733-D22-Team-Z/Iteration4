package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.LocationDAOImpl;
import edu.wpi.cs3733.D22.teamZ.database.MedicalEquipmentDAOImpl;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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

  // urls to other pages
  private String toLocationsURL = "edu/wpi/cs3733/D22/teamZ/views/Location.fxml";
  private String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";
  private String toMedicalEquipmentRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentRequestList.fxml";
  private String toHomeURL = "edu/wpi/cs3733/D22/teamZ/views/Homepage.fxml";
  private String toEquipmentMapURL = "edu/wpi/cs3733/D22/teamZ/views/EquipmentMap.fxml";

  // init LocationDAOImpl to use sql methods from db
  LocationDAOImpl locDAO = new LocationDAOImpl();

  // init MedicalEquipmentDAOImpl
  MedicalEquipmentDAOImpl medicalEquipmentDAO = new MedicalEquipmentDAOImpl();

  // create ObservableList to load locations into map
  // private ObservableList<Location> floorLocations = FXCollections.observableList(new
  // ArrayList<>());
  private ObservableList<Location> totalLocations = FXCollections.observableList(new ArrayList<>());
  private ObservableList<Label> allLabels = FXCollections.observableList(new ArrayList<>());

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

    // floorLocations.remove(0, floorLocations.size());
    totalLocations.addAll(FXCollections.observableList(locDAO.getAllLocations()));
    map.setImage(new Image("edu/wpi/cs3733/D22/teamZ/images/1.png"));
    // floorLocations.addAll(totalLocations.filtered(loc -> loc.getFloor().equalsIgnoreCase("1")));

    // initLabels();

    // showLocations("1");
    refreshMap("1");
    changeFloor.getSelectionModel().select(2);

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

    Location displayResult = locDAO.getLocationByID(selectLocationTextField.getText());
    typeChoiceTextField.setValue(displayResult.getNodeType());

    locationChangeDarkenPane.setVisible(false);
    editLocationPane.setVisible(false);
    locationChangeDarkenPane.setDisable(true);
    editLocationPane.setDisable(true);

    // Casey's
    parentDataList = new ArrayList<>();
    parentDataList.addAll(locDAO.getAllLocations());
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
            allLabels.filtered(
                label -> {
                  Location temp = totalLocations.get(allLabels.indexOf(label));
                  return temp.getFloor().equalsIgnoreCase(floor)
                      && !temp.getNodeType()
                          .equalsIgnoreCase("hall"); // disable line to enable halls
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
    Location tempLocation = locDAO.getLocationByID(selectLocationTextField.getText());

    // old floor
    String oldFloor = tempLocation.getFloor();

    tempLocation.setNodeType(typeChoiceTextField.getValue());
    tempLocation.setFloor(floorChoiceTextField.getValue());
    tempLocation.setLongName(changeNameTextField.getText());
    tempLocation.setBuilding("Tower");
    tempLocation.setShortName(abbreviationTextField.getText());

    String newNodeID =
        "z"
            + typeChoiceTextField.getValue()
            + "0".repeat(3 - changeNumberTextField.getText().length())
            + changeNumberTextField.getText()
            + "0".repeat(2 - floorChoiceTextField.getValue().length())
            + floorChoiceTextField.getValue();

    // check if already exists
    if (locDAO.getLocationByID(newNodeID).getNodeID() == null) {
      alreadyExistsText.setVisible(false);
      List<MedicalEquipment> medicalEquipmentList =
          medicalEquipmentDAO.getAllMedicalEquipmentByLocation(tempLocation);
      // check if there are medical equipment stuff there
      if (medicalEquipmentList.isEmpty()) {
        // do nothing
      }
      // there is equipment so update to new location
      else {
        for (int i = 0; i < medicalEquipmentList.size(); i++) {
          MedicalEquipment tempMedEquip = medicalEquipmentList.get(i);
          tempMedEquip.setCurrentLocation(tempLocation);
          medicalEquipmentDAO.updateMedicalEquipment(tempMedEquip);
        }
      }

      if (locDAO.deleteLocation(tempLocation)) {
        System.out.println("Delete location successful");
      }
      tempLocation.setNodeID(newNodeID);
      if (locDAO.addLocation(tempLocation)) {
        System.out.println("Added updated location successful");
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
    totalLocations.addAll(locDAO.getAllLocations());
    initLabels();
    showLocations(floor);
  }

  @FXML
  public void deleteLocation() throws IOException {
    Location temp = locDAO.getLocationByID(locationToDeleteTextField.getText());
    if (temp.getNodeID().equals(null)) {
      System.out.println("Did not find location in database");
      return;
    }
    if (locDAO.deleteLocation(temp)) {
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
    List<Location> locations = locDAO.getAllLocationsByFloor(floorField.getValue().toString());
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
    if (locDAO.getLocationByID(newNodeID).getNodeID() == null) {
      // add it
      locDAO.addLocation(newLocation);
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

    // ControlCSV writer = new LocationControlCSV(file);
    LocationDAOImpl writer = new LocationDAOImpl();
    writer.exportToLocationCSV(file);
  }

  public void importFromCSV(ActionEvent actionEvent) {
    FileChooser fileChooser = new FileChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    fileChooser.setTitle("Enter a .csv file...");
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);

    File file = fileChooser.showOpenDialog(stage);

    // ControlCSV writer = new LocationControlCSV(file);
    LocationDAOImpl writer = new LocationDAOImpl();

    int numberConflicts = writer.importLocationFromCSV(file);

    refreshMap(changeFloor.getSelectionModel().getSelectedItem().toString());
    System.out.println(
        "Detected "
            + numberConflicts
            + " locations that are"
            + " trying to get deleted but still have equipment in it");
  }
}
