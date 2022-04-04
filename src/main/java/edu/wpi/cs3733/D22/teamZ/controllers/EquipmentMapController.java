package edu.wpi.cs3733.D22.teamZ.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.D22.teamZ.App;
import edu.wpi.cs3733.D22.teamZ.database.ILocationDAO;
import edu.wpi.cs3733.D22.teamZ.database.IMedicalEquipmentDAO;
import edu.wpi.cs3733.D22.teamZ.database.LocationDAOImpl;
import edu.wpi.cs3733.D22.teamZ.database.MedicalEquipmentDAOImpl;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 * The controller for the Medical Equipment Map. In addition to handling interaction with the UI, it
 * also handles pulling data from the Location and Medical Equipment DAOs, drawing the map, location
 * markers, and equipment icons on screen, and displaying the popup window.
 */
public class EquipmentMapController implements Initializable {
  // XML variables
  @FXML private ImageView mapImage;
  @FXML private Pane mapContainer;
  @FXML private Canvas mapCanvas;
  private GraphicsContext mapCtx;
  @FXML private VBox detailsPopup;
  @FXML private HBox equipmentNames;
  @FXML private HBox equipmentQuantities;
  @FXML private Pane iconContainer;
  @FXML private JFXComboBox<String> changeFloor;
  @FXML private JFXButton backButton;

  // Database variables
  private ILocationDAO locationDAO;
  private IMedicalEquipmentDAO medicalEquipmentDAO;
  private List<Location> locations;

  // Hold onto the marker and icon images
  private Image markerImage;
  private Image iconImage;

  // URL to landing page on exit.
  private String toLandingPageURL = "views/LandingPage.fxml";

  /**
   * Initializes the map, changeFloor box, images, and database operations.
   *
   * @param location idk
   * @param resources idk
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Set mapCtx to newly created canvas' context
    mapCtx = mapCanvas.getGraphicsContext2D();

    // Add floors to changeFloor
    changeFloor.getItems().addAll("L1", "L2", "1", "2", "3");

    // Load images now for use later
    InputStream rsc = App.class.getResourceAsStream("images/location.png");
    markerImage = new Image(rsc);

    rsc = App.class.getResourceAsStream("images/home.png");
    iconImage = new Image(rsc);

    // Setup DAOs
    locationDAO = new LocationDAOImpl();
    medicalEquipmentDAO = new MedicalEquipmentDAOImpl();

    // Pull all locations
    locations = locationDAO.getAllLocations();

    // Zoom stuff
    Scale scaleTransform = new Scale(0.85, 0.85, 0, 0);
    mapContainer.getTransforms().add(scaleTransform);

    // Initalize map
    changeFloor.getSelectionModel().select(2);
    fillLocationMarkers("1");
    displayMedicalEquipmentIcons("1");
  }

  /**
   * Called when the changeFloor combo box is selected. The map is switched to a new floor, and
   * location data for that floor is loaded.
   */
  @FXML
  private void onFloorSelected() {
    // Read new floor
    String floor = changeFloor.getSelectionModel().getSelectedItem();

    // Switch map image
    mapImage.setImage(
        new Image(App.class.getResourceAsStream(String.format("images/%s.png", floor))));

    // Reset canvas and iconContainer
    iconContainer.getChildren().clear();
    mapCtx.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());

    // Re-fill elements with locations from newly selected floor
    fillLocationMarkers(floor);
    displayMedicalEquipmentIcons(floor);
  }

  /**
   * Called when the iconContainer pane is clicked, which will be the highest pane in the map area.
   * When it is clicked, the detailsPopup's visibility is disabled.
   */
  @FXML
  public void iconContainerClicked() {
    detailsPopup.setVisible(false);
  }

  /**
   * Called when the back button is clicked. This will exit the EquipmentMap and return the user to
   * the landing page.
   *
   * @throws IOException XML file was not found
   */
  @FXML
  private void toLandingPage() throws IOException {
    Stage primaryStage = (Stage) backButton.getScene().getWindow();
    Parent root = FXMLLoader.load(App.class.getResource(toLandingPageURL));
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
  }

  /**
   * Iterates through locations and draw markers to visualize location.
   *
   * @param floor only draw locations from the given floor
   */
  public void fillLocationMarkers(String floor) {
    // Draw location markers
    for (Location loc : locations) {
      // Only draw dots from selected floor
      if (loc.getFloor().equals(floor)) {
        mapCtx.drawImage(
            markerImage,
            loc.getXcoord() - markerImage.getWidth() / 2,
            loc.getYcoord() - markerImage.getHeight());
      }
    }
  }

  /**
   * Display the details window popup for a selected location.
   *
   * @param loc the location to display information from
   * @param equipment the list of equipment for that location
   */
  public void showInfoDialog(Location loc, List<MedicalEquipment> equipment) {
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
    detailsPopup.relocate(
        loc.getXcoord() - detailsPopup.getBoundsInParent().getWidth() / 2,
        loc.getYcoord() - detailsPopup.getBoundsInParent().getHeight() - 50);
  }

  /**
   * Count the number of occurrences of each medical equipment in a MedicalEquipment list.
   *
   * @param reqList the list of MedicalEquipment to count in
   * @return a Dictionary that maps each type of equipment to its number of occurrences.
   */
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

  /**
   * Creates labels in a certain style
   *
   * @param text the text to be displayed
   * @param color the background color of the label
   * @param topLeft the radius of the top left corner
   * @param topRight the radius of the top right corner
   * @param botLeft the radius of the bottom left corner
   * @param botRight the radius of the bottom right corner
   * @return the stylized label
   */
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

  /**
   * Displays a clickable icon for each location with medical equipment that will open the details
   * popup when clicked.
   *
   * @param floor the floor to filter locations by
   */
  public void displayMedicalEquipmentIcons(String floor) {
    // Iterate through all locations
    for (int i = 0; i < locations.size(); i++) {
      Location tempLocation = locations.get(i);

      // If this location is on the current floor, then proceed.
      if (tempLocation.getFloor().equals(floor)) {
        List<MedicalEquipment> medicalEquipmentAtLocation =
            medicalEquipmentDAO.getAllMedicalEquipmentByLocation(tempLocation);

        // If there is medical equipment at the location, then proceed.
        if (!medicalEquipmentAtLocation.isEmpty()) {

          // Setup icon image view
          ImageView iconImageView = new ImageView();
          iconImageView.setImage(iconImage);

          // Setup JFXButton
          JFXButton iconButton = new JFXButton();
          iconButton.setMaxWidth(iconImage.getWidth());
          iconButton.setMaxHeight(iconImage.getHeight());
          iconButton.setPadding(Insets.EMPTY);
          iconButton.setText("");
          iconButton.setGraphic(iconImageView);
          iconButton.setOnMouseClicked(
              event -> showInfoDialog(tempLocation, medicalEquipmentAtLocation));

          // Add button to iconContainer and relocate to correct spot.
          iconContainer.getChildren().add(iconButton);
          iconButton.relocate(
              tempLocation.getXcoord() - iconImage.getWidth() / 2,
              tempLocation.getYcoord() - iconImage.getHeight() * 3);
        }
      }
    }
  }
}
