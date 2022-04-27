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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

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
  private Timeline timeline;

  private Image iconImage;

  DateTimeFormatter timeFormatA = DateTimeFormatter.ofPattern("hh:mm a");
  long speed;
  int timesLoop;
  LocalDateTime simStart;
  LocalDateTime simEnd;

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

      if (speedBox.getSelectionModel().getSelectedItem().equals("Real time")) {
        speed = 1;
        timesLoop = 86400;
      } else if (speedBox.getSelectionModel().getSelectedItem().equals("5 min/sec")) {
        speed = 300;
        timesLoop = 288;
      } else if (speedBox.getSelectionModel().getSelectedItem().equals("10 min/sec")) {
        speed = 600;
        timesLoop = 144;
      } else if (speedBox.getSelectionModel().getSelectedItem().equals("30 min/sec")) {
        speed = 1800;
        timesLoop = 48;
      } else if (speedBox.getSelectionModel().getSelectedItem().equals("1 hour/sec")) {
        speed = 3600;
        timesLoop = 24;
      }

      simStart = LocalDateTime.now();
      simEnd = simStart.plusHours(20);

      clock.setText(timeFormatA.format(simStart));
      this.timeline = new Timeline(new KeyFrame(Duration.millis(1000), this::Simulate));
      this.timeline.setCycleCount(timesLoop);
      this.timeline.play();
    }
  }

  private void Simulate(ActionEvent actionevent) {
    simStart = simStart.plusSeconds(speed);
    clock.setText(timeFormatA.format(simStart));
  }

  public void pauseSim(ActionEvent actionEvent) {
    if (pauseSim.getText().equals("Pause Simulation")) {
      this.timeline.pause();
      pauseSim.setText("Play Simulation");
    } else {
      this.timeline.play();
      pauseSim.setText("Pause Simulation");
    }
  }

  public void endSim(ActionEvent actionEvent) {
    this.timeline.stop();
    pauseSim.setDisable(true);
    endSim.setDisable(true);
    startSim.setDisable(false);
    clock.setText("00:00 AM");
  }
}
