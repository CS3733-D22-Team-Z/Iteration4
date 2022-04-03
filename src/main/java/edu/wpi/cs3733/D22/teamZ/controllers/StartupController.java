package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.*;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipmentDeliveryRequest;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class StartupController {

  // init ui components
  @FXML private TableView<Location> Locations;
  @FXML private TableColumn<Location, String> nodeID;
  @FXML private TableColumn<Location, Integer> xCoord;
  @FXML private TableColumn<Location, Integer> yCoord;
  @FXML private TableColumn<Location, String> floor;
  @FXML private TableColumn<Location, String> building;
  @FXML private TableColumn<Location, String> nodeType;
  @FXML private TableColumn<Location, String> longname;
  @FXML private TableColumn<Location, String> shortname;
  @FXML private Button loadData;

  // init ui components
  @FXML private TableView<MedicalEquipmentDeliveryRequest> MedRequestsTable;
  @FXML private TableColumn<MedicalEquipmentDeliveryRequest, String> requestIDCol;
  @FXML private TableColumn<MedicalEquipmentDeliveryRequest, String> statusCol;
  @FXML private TableColumn<MedicalEquipmentDeliveryRequest, String> issuerCol;
  @FXML private TableColumn<MedicalEquipmentDeliveryRequest, String> handlerCol;
  @FXML private TableColumn<MedicalEquipmentDeliveryRequest, String> equipmentCol;
  @FXML private TableColumn<MedicalEquipmentDeliveryRequest, String> currentLocCol;
  @FXML private TableColumn<MedicalEquipmentDeliveryRequest, String> targetLocCol;

  // URL for Homepage
  private String homepageURL = "edu/wpi/cs3733/D22/teamZ/views/Homepage.fxml";
  // URL for Hospital-Logo
  private String hospitalLogoURL = "edu/wpi/cs3733/D22/teamZ/images/Hospital-Logo.png";

  // init LocationDAOImpl to getAllLocations from db
  LocationDAOImpl locDAO = new LocationDAOImpl();

  // create ObservableList to load locations into tableView
  private ObservableList<Location> data;

  // init MedEquipReqDAOImpl
  IMedEquipReqDAO medDAO = new MedEquipReqDAOImpl();

  // create ObservableList to load MedEquipDAO into tableView
  private ObservableList<MedicalEquipmentDeliveryRequest> dataMed;

  // loadDataFromDatabase when button loadData is clicked
  @FXML
  private void loadDataFromDatabase(ActionEvent event) {
    System.out.println("loading data");
    Locations.getItems().clear();
    MedRequestsTable.getItems().clear();

    // get list of locations from db and transfer into ObservableList
    data = FXCollections.observableList(locDAO.getAllLocations());

    // get list of locations from db and transfer into ObservableList
    dataMed = FXCollections.observableList(medDAO.getAllMedEquipReq());

    // link columnNames to data
    nodeID.setCellValueFactory(new PropertyValueFactory<Location, String>("nodeID"));
    xCoord.setCellValueFactory(new PropertyValueFactory<Location, Integer>("xcoord"));
    yCoord.setCellValueFactory(new PropertyValueFactory<Location, Integer>("ycoord"));
    floor.setCellValueFactory(new PropertyValueFactory<Location, String>("floor"));
    building.setCellValueFactory(new PropertyValueFactory<Location, String>("building"));
    nodeType.setCellValueFactory(new PropertyValueFactory<Location, String>("nodeType"));
    longname.setCellValueFactory(new PropertyValueFactory<Location, String>("longName"));
    shortname.setCellValueFactory(new PropertyValueFactory<Location, String>("shortName"));

    requestIDCol.setCellValueFactory(
        new PropertyValueFactory<MedicalEquipmentDeliveryRequest, String>("requestID"));
    statusCol.setCellValueFactory(
        new PropertyValueFactory<MedicalEquipmentDeliveryRequest, String>("status"));
    issuerCol.setCellValueFactory(
        new PropertyValueFactory<MedicalEquipmentDeliveryRequest, String>("issuer"));
    handlerCol.setCellValueFactory(
        new PropertyValueFactory<MedicalEquipmentDeliveryRequest, String>("handler"));
    equipmentCol.setCellValueFactory(
        new PropertyValueFactory<MedicalEquipmentDeliveryRequest, String>("equipment"));
    currentLocCol.setCellValueFactory(
        new PropertyValueFactory<MedicalEquipmentDeliveryRequest, String>("currentLoc"));
    targetLocCol.setCellValueFactory(
        new PropertyValueFactory<MedicalEquipmentDeliveryRequest, String>("targetLoc"));

    // load data into tableView

    Locations.setItems(data);
    MedRequestsTable.setItems(dataMed);
    // MealRequestTable.setItems(dataMeal); //to implement
  }

  @FXML
  public void writeExcel(ActionEvent event) throws Exception {
    System.out.println("exporting CSV of LocationData");
    data = FXCollections.observableList(locDAO.getAllLocations());
    dataMed = FXCollections.observableList(medDAO.getAllMedEquipReq());
    // dataMeal
  }

  @FXML
  public void navHome(ActionEvent event) throws IOException {
    System.out.println("navigating to home from locations");
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(homepageURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    // All Stages have this title, unless updated
    stage.setTitle("Team Z - Brigham and Women's Hospital App");
    // All Stages have this icon, unless updated
    stage.getIcons().add(new Image(hospitalLogoURL));
    stage.setScene(scene);
    stage.show();
  }
}
