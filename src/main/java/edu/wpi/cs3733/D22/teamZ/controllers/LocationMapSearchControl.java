package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.LocationControlCSV;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class LocationMapSearchControl {

  public VBox searchBox;
  public Button directoryTestButton;
  @FXML private TextField testFocusField;
  @FXML private ListView<String> resultsList;

  @FXML private TextField textField;

  private SearchControl filter;
  private List<ISearchable> parentDataList;
  private final BooleanProperty multiFocusProperty = new SimpleBooleanProperty();
  private final int ROW_HEIGHT = 24;
  private ObservableList<String> dispResult = FXCollections.observableList(new ArrayList<>());

  @FXML
  void search(KeyEvent event) {
    // textField.requestFocus();
    // System.out.println("run search");
    parentDataList = filter.filterList(textField.getText());
    List<String> longNames = new ArrayList<>();
    for (ISearchable loc : parentDataList) {
      longNames.add(loc.getDisplayName());
    }
    dispResult.remove(0, dispResult.size());
    dispResult.addAll(FXCollections.observableList(longNames));
    dispResult.remove(5, dispResult.size());
    resultsList.setItems(dispResult);
  }

  @FXML
  void resultMouseClick(MouseEvent event) {
    System.out.println(resultsList.getSelectionModel().getSelectedItem());
  }

  public void initialize() {
    System.out.println("init");
    LocationControlCSV dataIn =
        new LocationControlCSV(
            new File(
                System.getProperty("user.dir")
                    + System.getProperty("file.separator")
                    + "TowerLocations.csv"));
    parentDataList = new ArrayList<>();
    try {
      parentDataList.addAll(dataIn.readLocCSV());
    } catch (IOException e) {
      e.printStackTrace();
    }
    filter = new SearchControl(parentDataList);
    List<String> longNames = new ArrayList<>();
    for (ISearchable loc : parentDataList) {
      longNames.add(loc.getDisplayName());
    }
    dispResult.addAll(FXCollections.observableList(longNames));
    resultsList.setItems(dispResult);
    // resultsList = new ListView<>(dispResult);

    resultsList.setVisible(false);

    multiFocusProperty.addListener(
        (observable, oldValue, newValue) -> {
          // System.out.println("vis swap");
          resultsList.setVisible(newValue);
        });

    multiFocusProperty.bind(textField.focusedProperty().or(resultsList.focusedProperty()));

    resultsList.setPrefHeight(dispResult.size() * ROW_HEIGHT + 2);

    dispResult.addListener(
        (ListChangeListener<String>)
            c -> {
              resultsList.setPrefHeight(
                  dispResult.size() * ROW_HEIGHT
                      + 2); // this gets called way too much, but whatever
              // System.out.println("height changed");
            });

    dispResult.remove(5, dispResult.size());
  }

  public void onButtonClick(ActionEvent actionEvent) {
    DirectoryChooser n = new DirectoryChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    File a = n.showDialog(stage);
    System.out.println(a.getAbsolutePath());
  }
}
