package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.LocationDAOImpl;
import edu.wpi.cs3733.D22.teamZ.database.MedicalEquipmentDAOImpl;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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

  // Daniel's Stuff
  // Buttons
  @FXML private Button deleteMapLocation;
  @FXML private Button cancelLocationSelection;
  // text field box to select location to delete
  @FXML private TextField locationToDeleteTextField;
  @FXML private Pane deleteLocationPlane;

  // urls to other pages
  private String toLocationsURL = "edu/wpi/cs3733/D22/teamZ/views/Location.fxml";
  private String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";
  private String toMedicalEquipmentRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentRequestList.fxml";
  private String toHomeURL = "edu/wpi/cs3733/D22/teamZ/views/Homepage.fxml";

  // init LocationDAOImpl to use sql methods from db
  LocationDAOImpl locDAO = new LocationDAOImpl();

  // init MedicalEquipmentDAOImpl
  MedicalEquipmentDAOImpl medicalEquipmentDAO = new MedicalEquipmentDAOImpl();

  // create ObservableList to load locations into map
  private ObservableList<Location> firstFloorLocations =
      FXCollections.observableList(new ArrayList<>());

  // initialize location labels to display on map
  @FXML
  private void initialize() {
    System.out.println("loading labels");

    changeFloor.getItems().add("L1");
    changeFloor.getItems().add("L2");
    changeFloor.getItems().add("1");
    changeFloor.getItems().add("2");
    changeFloor.getItems().add("3");

    firstFloorLocations.remove(0, firstFloorLocations.size());
    firstFloorLocations.addAll(FXCollections.observableList(locDAO.getAllLocationsByFloor("1")));
    map.setImage(new Image("edu/wpi/cs3733/D22/teamZ/images/1.png"));
    showLocations(firstFloorLocations);

    changeFloor.setOnAction(
        (event) -> {
          int selectedIndex = changeFloor.getSelectionModel().getSelectedIndex();
          String selectedItem = changeFloor.getSelectionModel().getSelectedItem().toString();

          System.out.println("Selection made: [" + selectedIndex + "] " + selectedItem);
          System.out.println("   ChoiceBox.getValue(): " + changeFloor.getValue());
          // get list of locations from db and transfer into ObservableList

          firstFloorLocations.remove(0, firstFloorLocations.size());
          firstFloorLocations.addAll(
              FXCollections.observableList(locDAO.getAllLocationsByFloor(selectedItem)));
          map.setImage(new Image("edu/wpi/cs3733/D22/teamZ/images/" + selectedItem + ".png"));
          showLocations(firstFloorLocations);
        });

    // Andrew's stuff
    typeChoiceTextField.setItems(
        FXCollections.observableArrayList(
            "DEPT", "HALL", "ELEV", "STOR", "EXIT", "INFO", "RETL", "SERV", "STAI", "BATH", "LABS",
            "PATI"));
    floorChoiceTextField.setItems(FXCollections.observableArrayList("L2", "L1", "1", "2", "3"));

    Location temp = locDAO.getLocationByID(selectLocationTextField.getText());
    typeChoiceTextField.setValue(temp.getNodeType());

    locationChangeDarkenPane.setVisible(false);
    editLocationPane.setVisible(false);
    locationChangeDarkenPane.setDisable(true);
    editLocationPane.setDisable(true);

    // Daniel's Stuff
    deleteLocationPlane.setVisible(false);
    deleteLocationPlane.setDisable(true);
  }

  private void showLocations(ObservableList<Location> floor) {
    pane.getChildren().clear();
    for (int i = 0; i < floor.size(); i++) {
      // styilize label icon
      Image locationImg = new Image("edu/wpi/cs3733/D22/teamZ/images/location.png");
      ImageView locationIcon = new ImageView(locationImg);
      Location current = floor.get(i);
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
            displayLocationInformation(current, pane, label);
            activeLocation = current;
          });

      // place label at correct coords
      label.relocate(current.getXcoord() - 10, current.getYcoord() - 10);
      pane.getChildren().add(label);
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

  // when a location label is clicked on map, information about that label is shown on the side
  private void displayLocationInformation(Location clickedLocation, Pane pane, Label label) {

    label.setScaleX(2);
    label.setScaleY(2);
    // update labels to correct info
    floorLabel.setText("Floor: " + clickedLocation.getFloor());
    longnameLabel.setText("Long Name: " + clickedLocation.getLongName());
    xCoordLabel.setText("xCoord: " + String.valueOf(clickedLocation.getXcoord()));
    yCoordLabel.setText("yCoord: " + String.valueOf(clickedLocation.getYcoord()));

    // set the labels visible
    floorLabel.setVisible(true);
    longnameLabel.setVisible(true);
    xCoordLabel.setVisible(true);
    yCoordLabel.setVisible(true);

    editLocation.setDisable(false);
    deleteLocation.setDisable(false);

    // if user has clicked out of label, and on an empty part of the pane, disable buttons and
    // unenlarge previous label
    pane.addEventFilter(
        MouseEvent.MOUSE_CLICKED,
        evt -> {
          if (!inHierarchy(evt.getPickResult().getIntersectedNode(), label)) {
            pane.requestFocus();
            label.setScaleX(1);
            label.setScaleY(1);

            editLocation.setDisable(true);
            deleteLocation.setDisable(true);

            floorLabel.setText("Floor: ");
            longnameLabel.setText("Long Name: ");
            xCoordLabel.setText("xCoord: ");
            yCoordLabel.setText("yCoord: ");
          }
        });
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

    tempLocation.setNodeType(typeChoiceTextField.getValue());
    tempLocation.setFloor(floorChoiceTextField.getValue());
    tempLocation.setLongName(changeNameTextField.getText());
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

  @FXML
  public void deleteLocation() throws IOException {
    Location temp = locDAO.getLocationByID(locationToDeleteTextField.getText());
    if (temp.getNodeID().equals(null)) {
      System.out.println("Did not find location in database");
      return;
    }
    if (locDAO.deleteLocation(temp)) {
      System.out.println("Deletion Successful");
    } else {
      System.out.println("There are still stuff in this location");
    }
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
}
