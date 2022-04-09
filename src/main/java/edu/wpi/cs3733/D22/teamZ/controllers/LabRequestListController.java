package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.LabServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class LabRequestListController implements Initializable, IMenuAccess {
  @FXML private TableView<LabServiceRequest> labRequestTable;
  @FXML private TableColumn<LabServiceRequest, ServiceRequest.RequestStatus> status;
  @FXML private TableColumn<LabServiceRequest, String> requestID;
  @FXML private TableColumn<LabServiceRequest, String> issuer;
  @FXML private TableColumn<LabServiceRequest, String> handler;
  @FXML private TableColumn<LabServiceRequest, String> targetLocation;
  @FXML private TableColumn<LabServiceRequest, String> labType;

  private final String toLabServiceRequestListURL =
      "edu/wpi/cs3733/D22/teamZ/views/LabServiceRequest.fxml";

  private FacadeDAO facadeDAO;

  private MenuController menu;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @FXML
  private void toLabServiceRequest(ActionEvent event) throws IOException {
    menu.load(toLabServiceRequestListURL);
  }

  // loadDataFromDatabase when button loadData is clicked
  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    facadeDAO = new FacadeDAO();

    System.out.println("loading data");
    labRequestTable.getItems().clear();

    // get list of locations from db and transfer into ObservableList
    ObservableList<LabServiceRequest> data =
        FXCollections.observableList(facadeDAO.getAllLabServiceRequests());

    // link columnNames to data
    status.setCellValueFactory(
        new PropertyValueFactory<LabServiceRequest, ServiceRequest.RequestStatus>("Status"));
    requestID.setCellValueFactory(new PropertyValueFactory<LabServiceRequest, String>("RequestID"));
    issuer.setCellValueFactory(new PropertyValueFactory<LabServiceRequest, String>("Issuer"));
    handler.setCellValueFactory(new PropertyValueFactory<LabServiceRequest, String>("Handler"));
    targetLocation.setCellValueFactory(
        new PropertyValueFactory<LabServiceRequest, String>("TargetLocation"));
    labType.setCellValueFactory(new PropertyValueFactory<LabServiceRequest, String>("LabType"));

    // load data into tableView

    labRequestTable.setItems(data);
  }
}
