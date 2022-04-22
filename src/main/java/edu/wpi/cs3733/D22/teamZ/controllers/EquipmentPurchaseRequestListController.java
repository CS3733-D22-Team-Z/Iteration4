package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.EquipmentPurchaseRequest;
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

public class EquipmentPurchaseRequestListController implements IMenuAccess, Initializable {
  @FXML TableView<EquipmentPurchaseRequest> purchaseListTable;
  @FXML private TableColumn<EquipmentPurchaseRequest, String> requestID;
  @FXML private TableColumn<EquipmentPurchaseRequest, String> equipmentType;
  @FXML private TableColumn<EquipmentPurchaseRequest, String> paymentType;
  @FXML private TableColumn<EquipmentPurchaseRequest, String> issuer;
  @FXML private TableColumn<EquipmentPurchaseRequest, String> handler;
  @FXML private TableColumn<EquipmentPurchaseRequest, String> targetLocation;
  @FXML private TableColumn<EquipmentPurchaseRequest, String> status;

  private final String toEquipmentPurchaseRequest =
      "edu/wpi/cs3733/D22/teamZ/views/EquipmentPurchaseRequest.fxml";

  private MenuController menu;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    System.out.println("loading data");
    purchaseListTable.refresh();

    // get a list of equipment purchase requests from database
    ObservableList<EquipmentPurchaseRequest> totalList =
        FXCollections.observableList(FacadeDAO.getInstance().getAllEquipmentPurchaseRequests());

    // link columns to data
    requestID.setCellValueFactory(
        new PropertyValueFactory<EquipmentPurchaseRequest, String>("requestID"));
    status.setCellValueFactory(
        new PropertyValueFactory<EquipmentPurchaseRequest, String>("status"));
    issuer.setCellValueFactory(
        new PropertyValueFactory<EquipmentPurchaseRequest, String>("issuer"));
    handler.setCellValueFactory(
        new PropertyValueFactory<EquipmentPurchaseRequest, String>("handler"));
    targetLocation.setCellValueFactory(
        new PropertyValueFactory<EquipmentPurchaseRequest, String>("targetLocation"));
    equipmentType.setCellValueFactory(
        new PropertyValueFactory<EquipmentPurchaseRequest, String>("equipmentType"));
    paymentType.setCellValueFactory(
        new PropertyValueFactory<EquipmentPurchaseRequest, String>("paymentMethod"));

    purchaseListTable.setItems(totalList);
  }

  public void toEquipmentPurchaseRequest(ActionEvent event) throws IOException {
    menu.load(toEquipmentPurchaseRequest);
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Equipment Purchase Request";
  }
}
