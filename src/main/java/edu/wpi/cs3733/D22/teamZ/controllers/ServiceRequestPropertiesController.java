package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ServiceRequestPropertiesController implements Initializable {
  @FXML private TableView<ServiceRow> tableContainer;
  @FXML private TableColumn<ServiceRow, String> idColumn;
  @FXML private TableColumn<ServiceRow, String> typeColumn;
  @FXML private TableColumn<ServiceRow, String> statusColumn;
  @FXML private TableColumn<ServiceRow, String> issuerColumn;
  @FXML private TableColumn<ServiceRow, String> handlerColumn;
  @FXML private TableColumn<ServiceRow, String> locationColumn;

  private ObservableList<ServiceRow> requests;

  private List<ServiceRequest> rawRequests;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    idColumn.setCellValueFactory(sRow -> sRow.getValue().id);
    typeColumn.setCellValueFactory(sRow -> sRow.getValue().type);
    statusColumn.setCellValueFactory(sRow -> sRow.getValue().status);
    issuerColumn.setCellValueFactory(sRow -> sRow.getValue().issuer);
    handlerColumn.setCellValueFactory(sRow -> sRow.getValue().handler);
    locationColumn.setCellValueFactory(sRow -> sRow.getValue().location);

    requests = FXCollections.observableArrayList();
    tableContainer.setItems(requests);
  }

  protected void setRequests(List<ServiceRequest> newList) {
    rawRequests = newList;

    requests.clear();

    for (ServiceRequest req : rawRequests) {
      String handlerName = (req.getHandler() != null) ? req.getHandler().toString() : "null";
      requests.add(
          new ServiceRow(
              req.getRequestID(),
              req.getType().toString(),
              req.getStatus().toString(),
              req.getIssuer().toString(),
              handlerName,
              req.getTargetLocation().toString()));
    }
  }

  static class ServiceRow {
    SimpleStringProperty id;
    SimpleStringProperty type;
    SimpleStringProperty status;
    SimpleStringProperty issuer;
    SimpleStringProperty handler;
    SimpleStringProperty location;

    public ServiceRow(
        String id, String type, String status, String issuer, String handler, String location) {
      this.id = new SimpleStringProperty(id);
      this.type = new SimpleStringProperty(type);
      this.status = new SimpleStringProperty(status);
      this.issuer = new SimpleStringProperty(issuer);
      this.handler = new SimpleStringProperty(handler);
      this.location = new SimpleStringProperty(location);
    }
  }
}
