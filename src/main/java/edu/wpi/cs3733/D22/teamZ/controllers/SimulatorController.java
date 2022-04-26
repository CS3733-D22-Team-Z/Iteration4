package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.controllers.subControllers.MapController;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class SimulatorController implements IMenuAccess, Initializable {
  private MenuController menu;
  FacadeDAO facadeDAO;
  @FXML private AnchorPane mapContainer;
  @FXML private ChoiceBox speedBox;
  @FXML private MFXButton startSim;
  @FXML private MFXButton pauseSim;
  @FXML private MFXButton endSim;
  @FXML private Label clock;
  private MapController mapController;

  private Image iconImage;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Simulator";
  }

  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    facadeDAO = FacadeDAO.getInstance();
    iconImage = new Image("edu/wpi/cs3733/D22/teamZ/images/equipment.png");
    displayMedicalEquipmentIcons("3");
    speedBox.getItems().setAll("Real time", "5 min/sec", "10 min/sec", "30 min/sec", "1 hour/sec");
    pauseSim.setDisable(true);
    endSim.setDisable(true);
  }

  public void displayMedicalEquipmentIcons(String floor) {
    // Iterate through all locations
    List<Location> locations = facadeDAO.getAllLocations();
    for (int i = 0; i < locations.size(); i++) {
      Location tempLocation = locations.get(i);

      // If this location is on the current floor, then proceed.
      if (tempLocation.getFloor().equals(floor)) {
        List<MedicalEquipment> medicalEquipmentAtLocation =
            facadeDAO.getAllMedicalEquipmentByLocation(tempLocation);

        // If there is medical equipment at the location, then proceed.
        if (!medicalEquipmentAtLocation.isEmpty()) {

          // Setup icon image view
          ImageView iconImageView = new ImageView();
          iconImageView.setImage(iconImage);

          MFXButton iconButton = new MFXButton();
          iconButton.setMaxWidth(iconImage.getWidth());
          iconButton.setMaxHeight(iconImage.getHeight());
          iconButton.setPadding(Insets.EMPTY);
          iconButton.setText("");
          iconButton.setGraphic(iconImageView);
          iconButton.setStyle("-fx-background-color: transparent;");

          // Add button to iconContainer and relocate to correct spot.
          double scaleX = 1.25;
          double scaleY = 1.15;
          mapContainer.getChildren().add(iconButton);
          iconButton.relocate(tempLocation.getXcoord() * scaleX, tempLocation.getYcoord() * scaleY);
        }
      }
    }
  }

  public void startSimulation(ActionEvent actionEvent) {
    if (speedBox.getSelectionModel().getSelectedItem() == null) {
      return;
    } else {
      startSim.setDisable(true);
      pauseSim.setDisable(false);
      endSim.setDisable(false);
      DateTimeFormatter timeFormatA = DateTimeFormatter.ofPattern("hh:mm a");
      clock.setText(timeFormatA.format(LocalDateTime.now()));
    }
  }
}
