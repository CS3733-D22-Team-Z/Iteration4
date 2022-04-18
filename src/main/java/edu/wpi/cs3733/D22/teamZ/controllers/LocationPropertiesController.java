package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.entity.Location;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class LocationPropertiesController implements Initializable {

  @FXML TableView<LocationRow> tableContainer;
  @FXML TableColumn<LocationRow, String> identifierColumn;
  @FXML TableColumn<LocationRow, String> valueColumn;

  ObservableList<LocationRow> details;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    identifierColumn.setCellValueFactory(lRow -> lRow.getValue().field);
    valueColumn.setCellValueFactory(lRow -> lRow.getValue().value);

    details = FXCollections.observableArrayList();
    tableContainer.setItems(details);
  }

  protected void setLocation(Location newLoc) {
    details.clear();

    details.add(new LocationRow("Long Name", newLoc.getLongName()));
    details.add(new LocationRow("Short Name", newLoc.getShortName()));
    details.add(new LocationRow("Node ID", newLoc.getNodeID()));
    details.add(new LocationRow("Node Type", newLoc.getNodeType()));
    details.add(new LocationRow("Floor", newLoc.getFloor()));
    details.add(new LocationRow("X Coordinate", Integer.toString(newLoc.getXcoord())));
    details.add(new LocationRow("Y Coordinate", Integer.toString(newLoc.getYcoord())));
  }

  class LocationRow {
    SimpleStringProperty field;
    SimpleStringProperty value;

    public LocationRow(String field, String value) {
      this.field = new SimpleStringProperty(field);
      this.value = new SimpleStringProperty(value);
    }
  }
}
