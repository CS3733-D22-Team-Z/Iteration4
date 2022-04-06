package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.LabRequestServiceDAOImpl;
import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import edu.wpi.cs3733.D22.teamZ.entity.LabServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class LabRequestListController implements Initializable {
  @FXML private TableView<LabServiceRequest> labRequestTable;
  @FXML private TableColumn<LabServiceRequest, ServiceRequest.RequestStatus> status;
  @FXML private TableColumn<LabServiceRequest, String> requestID;
  @FXML private TableColumn<LabServiceRequest, Employee> issuer;
  @FXML private TableColumn<LabServiceRequest, Employee> handler;
  @FXML private TableColumn<LabServiceRequest, Location> targetLocation;
  @FXML private TableColumn<LabServiceRequest, String> labType;

  private final String toLabServiceRequestListURL =
      "edu/wpi/cs3733/D22/teamZ/views/LabServiceRequest.fxml";

  private LabRequestServiceDAOImpl labDatabase;

  @FXML
  private void toLabServiceRequest(ActionEvent event) throws IOException {
    Parent root =
        FXMLLoader.load(getClass().getClassLoader().getResource(toLabServiceRequestListURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  // loadDataFromDatabase when button loadData is clicked
  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    labDatabase = new LabRequestServiceDAOImpl();

    System.out.println("loading data");
    labRequestTable.getItems().clear();

    // get list of locations from db and transfer into ObservableList
    ObservableList<LabServiceRequest> data =
        FXCollections.observableList(labDatabase.getAllLabServiceRequests());

    // link columnNames to data
    status.setCellValueFactory(
        new PropertyValueFactory<LabServiceRequest, ServiceRequest.RequestStatus>("Status"));
    requestID.setCellValueFactory(new PropertyValueFactory<LabServiceRequest, String>("ID"));
    issuer.setCellValueFactory(new PropertyValueFactory<LabServiceRequest, Employee>("Employee"));
    handler.setCellValueFactory(new PropertyValueFactory<LabServiceRequest, Employee>("Handler"));
    targetLocation.setCellValueFactory(
        new PropertyValueFactory<LabServiceRequest, Location>("Location"));
    labType.setCellValueFactory(new PropertyValueFactory<LabServiceRequest, String>("Type"));

    // load data into tableView

    labRequestTable.setItems(data);
  }
}
