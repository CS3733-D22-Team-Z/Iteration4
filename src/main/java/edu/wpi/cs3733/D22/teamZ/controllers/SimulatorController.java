package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.controllers.subControllers.MapController;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
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
  @FXML private TableView<String> infoTable;
  @FXML private TableColumn<String, String> info;
  @FXML private ImageView imageView;
  private MapController mapController;
  private Timeline timeline;

  private Image iconImage;

  DateTimeFormatter timeFormatA = DateTimeFormatter.ofPattern("hh:mm a");
  long speed;
  int timesLoop;
  LocalDateTime simStart;
  LocalDateTime simEnd;
  List<MedicalEquipment> medEquip;
  List<MedicalEquipmentDeliveryRequest> processing;
  List<MedicalEquipmentDeliveryRequest> unassigned;
  List<Employee> employees;
  ObservableList<String> updates;

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

    imageView.setImage(new Image("edu/wpi/cs3733/D22/teamZ/images/3.png"));

    medEquip = FacadeDAO.getInstance().getAllMedicalEquipment();
    employees = FacadeDAO.getInstance().getAllEmployees();
    List<MedicalEquipmentDeliveryRequest> medEquipReq =
        FacadeDAO.getInstance().getAllMedicalEquipmentRequest();
    List<MedicalEquipmentDeliveryRequest> tempProc =
        new ArrayList<MedicalEquipmentDeliveryRequest>();
    List<MedicalEquipmentDeliveryRequest> tempUn = new ArrayList<MedicalEquipmentDeliveryRequest>();
    for (MedicalEquipmentDeliveryRequest req : medEquipReq) {
      if (req.getStatus().equals(ServiceRequest.RequestStatus.PROCESSING)) {
        tempProc.add(req);
      } else if (req.getStatus().equals(ServiceRequest.RequestStatus.UNASSIGNED)) {
        tempUn.add(req);
      }
    }
    processing = tempProc;
    unassigned = tempUn;
    List<String> begin = new ArrayList<String>();
    begin.add("Begin Simulation");
    info.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
    updates = FXCollections.observableList(begin);
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
          double scaleX = 1.20;
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
    updates.add(timeFormatA.format(simStart));
    Random rand = new Random();
    if (processing.size() > 0) {
      if (rand.nextInt(10) <= 5) {
        moveEquip(processing.get(rand.nextInt(processing.size())));
      }
    }
    if (unassigned.size() > 0) {
      if (rand.nextInt(10) <= 5) {
        assignReq(unassigned.get(rand.nextInt(unassigned.size())));
      }
    }
    if (rand.nextInt(10) <= 5) {
      int index = (rand.nextInt(medEquip.size()));
      Location loc;
      MedicalEquipment.EquipmentStatus stat;
      if (medEquip.get(index).getStatus().equals(MedicalEquipment.EquipmentStatus.INUSE)) {
        loc = facadeDAO.getLocationByID("zDIRT00103");
        medEquip.get(index).setStatus(MedicalEquipment.EquipmentStatus.DIRTY);
      } else if (medEquip.get(index).getStatus().equals(MedicalEquipment.EquipmentStatus.CLEAN)) {
        loc =
            facadeDAO
                .getAllLocationsByType("PATI")
                .get(rand.nextInt(facadeDAO.getAllLocationsByType("PATI").size()));
        medEquip.get(index).setStatus(MedicalEquipment.EquipmentStatus.INUSE);
      } else if (medEquip.get(index).getStatus().equals(MedicalEquipment.EquipmentStatus.DIRTY)) {
        loc = facadeDAO.getLocationByID("zSTOR00304");
        medEquip.get(index).setStatus(MedicalEquipment.EquipmentStatus.CLEANING);
      } else {
        if (medEquip.get(index).getType().equals("Bed")
            || medEquip.get(index).getType().equals("Recliner")) {
          loc = facadeDAO.getLocationByID("zSTOR00303");
        } else if (medEquip.get(index).getType().equals("IPump")) {
          loc = facadeDAO.getLocationByID("zSTOR00101");
        } else {
          loc = facadeDAO.getAllLocations().get(rand.nextInt(facadeDAO.getAllLocations().size()));
        }
        medEquip.get(index).setStatus(MedicalEquipment.EquipmentStatus.CLEAN);
      }

      makeReq(medEquip.get(index).getEquipmentID(), loc);
    }
    infoTable.refresh();
    infoTable.setItems(updates);
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
    pauseSim.setText("Pause Simulation");
    pauseSim.setDisable(true);
    endSim.setDisable(true);
    startSim.setDisable(false);
    updates.clear();
    infoTable.setItems(updates);
    clock.setText("00:00 AM");
  }

  public void moveEquip(MedicalEquipmentDeliveryRequest req) {
    Location target = req.getTargetLocation();
    MedicalEquipment equip = medEquip.get(0);
    int i = 0;
    int count = 0;
    for (MedicalEquipment med : medEquip) {
      if (req.getEquipmentID().equals(med.getEquipmentID())) {
        equip = med;
        i = count;
      }
      count++;
    }

    if (target.getNodeType().equals("PATI")) {
      medEquip.get(i).setStatus(MedicalEquipment.EquipmentStatus.INUSE);
      equip.setStatus(MedicalEquipment.EquipmentStatus.INUSE);
    } else if (target.getNodeType().equals("DIRT")) {
      medEquip.get(i).setStatus(MedicalEquipment.EquipmentStatus.DIRTY);
      equip.setStatus(MedicalEquipment.EquipmentStatus.DIRTY);
    } else if (target.getNodeType().equals("STOR")) {
      medEquip.get(i).setStatus(MedicalEquipment.EquipmentStatus.CLEAN);
      equip.setStatus(MedicalEquipment.EquipmentStatus.CLEAN);
    }
    updates.add(
        "Request "
            + req.getRequestID()
            + " is completed by "
            + req.getHandler().getDisplayName()
            + ", "
            + equip.getEquipmentID()
            + " has status "
            + equip.getStatus().toString()
            + " and was moved to "
            + target);
    for (int j = 0; j < medEquip.size(); j++) {
      if (equip.equals(medEquip.get(i))) {
        medEquip.get(j).setCurrentLocation(target);
      }
    }
    processing.remove(req);
  }

  public void assignReq(MedicalEquipmentDeliveryRequest req) {
    Random rand = new Random();
    Employee randomEmp = employees.get(rand.nextInt(employees.size()));
    // MedicalEquipment equip = facadeDAO.getMedicalEquipmentByID(req.getEquipmentID());
    unassigned.remove(req);
    req.setStatus(ServiceRequest.RequestStatus.PROCESSING);
    req.setHandler(randomEmp);
    processing.add(req);
    updates.add(
        "Request " + req.getRequestID() + " is assigned to " + req.getHandler().getDisplayName());
  }

  public void makeReq(String medEq, Location target) {
    Random rand = new Random();
    Employee randomEmp = employees.get(rand.nextInt(employees.size()));
    String id = generateID("EQUIP");
    MedicalEquipmentDeliveryRequest medEquipReq =
        new MedicalEquipmentDeliveryRequest(
            id,
            ServiceRequest.RequestStatus.UNASSIGNED,
            randomEmp,
            null,
            medEq,
            target,
            simStart,
            null);
    unassigned.add(medEquipReq);
    updates.add(
        "New request "
            + medEquipReq.getRequestID()
            + " was made by "
            + medEquipReq.getIssuer().getDisplayName()
            + " for "
            + medEq
            + " to move to "
            + target);
  }

  public String generateID(String type) {
    String id = type;
    Random rand = new Random();
    int int_random = rand.nextInt(9) + 1;
    id += int_random;
    boolean notUnique = true;
    List<MedicalEquipmentDeliveryRequest> all = new ArrayList<MedicalEquipmentDeliveryRequest>();
    all.addAll(unassigned);
    all.addAll(processing);
    while (notUnique) {
      boolean notfound = true;
      for (MedicalEquipmentDeliveryRequest unReq : all) {
        if (unReq.getRequestID().equals(id)) {
          int_random = rand.nextInt(10);
          id += int_random;
          notfound = false;
        }
      }
      if (notfound) {
        notUnique = false;
      }
    }
    return id;
  }
}
