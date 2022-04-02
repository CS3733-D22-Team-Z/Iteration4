package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.LocationDAOImpl;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
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
import javafx.stage.Stage;

// issues: getAllLocations doesn't work if the DB is disconnected, is this how it's supposed to
// work?

// LocationController controls Location.fxml, loads location data into a tableView on page
public class LocationListController {

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

  private final String toHomepageURL = "edu/wpi/cs3733/D22/teamZ/views/HomePage.fxml";

  // init LocationDAOImpl to getAllLocations from db
  LocationDAOImpl locDAO = new LocationDAOImpl();

  // create ObservableList to load locations into tableView
  private ObservableList<Location> data;

  // loadDataFromDatabase when button loadData is clicked
  @FXML
  private void loadDataFromDatabase(ActionEvent event) {
    System.out.println("loading data");
    Locations.setItems(null);

    // get list of locations from db and transfer into ObservableList
    data = FXCollections.observableList(locDAO.getAllLocations());

    // link columnNames to data
    nodeID.setCellValueFactory(new PropertyValueFactory<Location, String>("nodeID"));
    xCoord.setCellValueFactory(new PropertyValueFactory<Location, Integer>("xcoord"));
    yCoord.setCellValueFactory(new PropertyValueFactory<Location, Integer>("ycoord"));
    floor.setCellValueFactory(new PropertyValueFactory<Location, String>("floor"));
    building.setCellValueFactory(new PropertyValueFactory<Location, String>("building"));
    nodeType.setCellValueFactory(new PropertyValueFactory<Location, String>("nodeType"));
    longname.setCellValueFactory(new PropertyValueFactory<Location, String>("longName"));
    shortname.setCellValueFactory(new PropertyValueFactory<Location, String>("shortName"));

    // load data into tableView

    Locations.setItems(data);
  }

  @FXML
  public void writeExcel(ActionEvent event) throws Exception {
    System.out.println("exporting CSV of Location Data");
    data = FXCollections.observableList(locDAO.getAllLocations());
  }

  @FXML
  public void navHome(ActionEvent event) throws IOException {
    System.out.println("navigating to home from locations");
    Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(toHomepageURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
}
