package edu.wpi.cs3733.D22.teamZ.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D22.teamZ.App;
import edu.wpi.cs3733.D22.teamZ.database.LocationDAOImpl;
import edu.wpi.cs3733.D22.teamZ.database.MedicalEquipmentDAOImpl;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class EquipmentMapController implements Initializable {
  @FXML private ImageView mapImage;
  @FXML private Pane mapContainer;
  @FXML private Canvas mapCanvas;
  private GraphicsContext mapCtx;
  private LocationDAOImpl locationDAO;
  @FXML private HBox detailsPopup;
  @FXML private VBox detailsIdentifiers;
  @FXML private VBox detailsValues;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Set mapCtx to newly created canvas' context
    mapCtx = mapCanvas.getGraphicsContext2D();

    // Setup DAO
    locationDAO = new LocationDAOImpl();

    // Pull all locations
    List<Location> locations = locationDAO.getAllLocations();

    // Draw dots
    for (Location loc : locations) {
      // Only draw dots from first floor
      if (loc.getFloor().equals("1")) {
        if (loc.getNodeID().equals("zDEPT00101")) mapCtx.setFill(Color.RED);
        else mapCtx.setFill(Color.BLUE);
        int size = 10;
        mapCtx.fillOval(loc.getXcoord() - size / 2, loc.getYcoord() - size / 2, size, size);

        // Draw icon
        // drawIcons(loc);
      }
    }

    displayMedicalEquipmentIcon();

    /* Zoom stuff
    Scale scaleTransform = new Scale(0.75, 0.75, 0, 0);
    mapContainer.getTransforms().add(scaleTransform);*/
  }

  public void drawIcon(Location loc) {
    // Create ImageView to hold icon
    ImageView iconImage = new ImageView();
    InputStream rsc = App.class.getResourceAsStream("images/home.png");
    Image img = new Image(rsc);
    iconImage.setImage(img);

    // Create the icon button
    JFXButton iconButton = new JFXButton();
    iconButton.setMaxWidth(img.getWidth());
    iconButton.setMaxHeight(img.getHeight());
    iconButton.setPadding(Insets.EMPTY);
    iconButton.setText("");

    // Add ImageView to the JFXButton and the JFXButton to the Pane.
    iconButton.setGraphic(iconImage);
    mapContainer.getChildren().add(iconButton);

    // Move iconButton to Location
    iconButton.relocate(
        loc.getXcoord() - img.getWidth() / 2, loc.getYcoord() - img.getHeight() * 2);

    // Set iconButton clicked listener
    iconButton.setOnMouseClicked(
        event -> {
          System.out.println(detailsIdentifiers.getWidth());
          showInfoDialog(loc);
        });
  }

  public void showInfoDialog(Location loc) {
    detailsPopup.setDisable(false);
    detailsIdentifiers.getChildren().clear();
    detailsValues.getChildren().clear();

    // Create popup with information about medical equipment
    for (int i = 1; i <= 2; i++) {
      Label test = new Label();
      test.setStyle("-fx-background-color: #00FF00");
      test.setText(String.format("Item %d", i));
      detailsIdentifiers.getChildren().add(test);
    }

    for (int i = 1; i <= 2; i++) {
      Label test = new Label();
      test.setStyle("-fx-background-color: #FF0000");
      test.setText(String.format("Item %d", i));
      detailsValues.getChildren().add(test);
    }

    detailsPopup.relocate(
        loc.getXcoord() - detailsIdentifiers.getBoundsInParent().getWidth(),
        loc.getYcoord() - detailsIdentifiers.getBoundsInParent().getHeight() - 35);
  }

  public void displayMedicalEquipmentIcon() {
    LocationDAOImpl locationDAO = new LocationDAOImpl();
    MedicalEquipmentDAOImpl medicalEquipmentDAO = new MedicalEquipmentDAOImpl();
    List<Location> locationList = locationDAO.getAllLocations();
    for (int i = 0; i < locationList.size(); i++) {
      Location tempLocation = locationList.get(i);
      if (tempLocation.getFloor().equals("3")) {
        List<MedicalEquipment> medicalEquipmentAtLocation =
            medicalEquipmentDAO.getAllMedicalEquipmentByLocation(tempLocation);
        int tempX = tempLocation.getXcoord();
        int tempY = tempLocation.getYcoord();
        ImageView iconImage = new ImageView();
        InputStream rsc = App.class.getResourceAsStream("images/home.png");
        Image img = new Image(rsc);
        iconImage.setImage(img);
        JFXButton iconButton = new JFXButton();
        iconButton.setMaxWidth(img.getWidth());
        iconButton.setMaxHeight(img.getHeight());
        iconButton.setPadding(Insets.EMPTY);
        iconButton.setText("");
        iconButton.setGraphic(iconImage);
        mapContainer.getChildren().add(iconButton);
        iconButton.relocate(tempX - img.getWidth() / 2 - 190, tempY - img.getHeight() * 2);
        if (medicalEquipmentAtLocation.isEmpty()) {
          iconButton.setVisible(false);
        }
      }
    }
  }
}
