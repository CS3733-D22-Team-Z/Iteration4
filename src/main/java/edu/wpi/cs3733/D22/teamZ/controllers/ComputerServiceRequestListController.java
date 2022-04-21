package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.ComputerServiceRequest;
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

public class ComputerServiceRequestListController implements Initializable, IMenuAccess {

  @FXML private TableView<ComputerServiceRequest> computerRequestTable;
  @FXML private TableColumn<ComputerServiceRequest, ServiceRequest.RequestStatus> status;
  @FXML private TableColumn<ComputerServiceRequest, String> requestID;
  @FXML private TableColumn<ComputerServiceRequest, String> issuer;
  @FXML private TableColumn<ComputerServiceRequest, String> handler;
  @FXML private TableColumn<ComputerServiceRequest, String> targetLocation;
  @FXML private TableColumn<ComputerServiceRequest, String> osType;
  @FXML private TableColumn<ComputerServiceRequest, String> desc;

  private MenuController menu;
  private String toComputerServiceRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/ComputerServiceRequest.fxml";

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Computer Service Request List";
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    System.out.println("loading data");
    computerRequestTable.refresh();

    // get list of locations from db and transfer into ObservableList
    ObservableList<ComputerServiceRequest> data =
        FXCollections.observableList(FacadeDAO.getInstance().getAllComputerServiceRequests());

    // link columnNames to data
    status.setCellValueFactory(
        new PropertyValueFactory<ComputerServiceRequest, ServiceRequest.RequestStatus>("status"));
    requestID.setCellValueFactory(
        new PropertyValueFactory<ComputerServiceRequest, String>("requestID"));
    issuer.setCellValueFactory(new PropertyValueFactory<ComputerServiceRequest, String>("issuer"));
    handler.setCellValueFactory(
        new PropertyValueFactory<ComputerServiceRequest, String>("handler"));
    targetLocation.setCellValueFactory(
        new PropertyValueFactory<ComputerServiceRequest, String>("targetLocation"));
    osType.setCellValueFactory(
        new PropertyValueFactory<ComputerServiceRequest, String>("operatingSystem"));
    desc.setCellValueFactory(
        new PropertyValueFactory<ComputerServiceRequest, String>("problemDescription"));

    // load data into tableView

    computerRequestTable.setItems(data);
  }

  public void toComputerServiceRequest(ActionEvent event) throws IOException {
    menu.load(toComputerServiceRequestURL);
  }
}
