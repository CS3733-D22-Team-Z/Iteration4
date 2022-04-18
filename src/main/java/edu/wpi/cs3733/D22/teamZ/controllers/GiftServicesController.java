package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.entity.Location;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class GiftServicesController extends ServiceRequestController {

  private List<Location> roomList;
  private List<String> roomNumbers;
  @FXML private MFXTextField enterPatientName;
  @FXML private MFXTextField enterPatientID;
  @FXML private ChoiceBox<String> nodeTypeDropDown;
  @FXML private ChoiceBox<String> giftDropDown;
  private ObservableList roomNumberNames;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    menuName = "Gift Services";
    roomList = database.getALlLocationsByType("PATI");
    roomNumbers = roomList.stream().map(loc -> loc.getLongName()).collect(Collectors.toList());
    roomNumberNames = FXCollections.observableList(roomNumbers);
    roomNumberNames.add(0, "");

    giftDropDown.setItems(
        FXCollections.observableArrayList(
            "", "Bouquet", "Chocolates", "Stuffed Animal", "Card", "Stripper"));

    nodeTypeDropDown.setItems(roomNumberNames);

    // //example
    nodeTypeDropDown.getSelectionModel().select(0);
    giftDropDown.getSelectionModel().select(0);

    submitButton.setDisable(true);

    nodeTypeDropDown.setOnAction(event -> validateButton());
    giftDropDown.setOnAction(event -> validateButton());
  }

  @Override
  protected void onSubmitButtonClicked(ActionEvent event) throws SQLException {
    // Add submitting functionality here!
  }

  @Override
  protected void onResetButtonClicked(ActionEvent event) throws IOException {
    enterPatientName.clear();
    enterPatientID.clear();
    nodeTypeDropDown.getSelectionModel().select(0);
    giftDropDown.getSelectionModel().select(0);
    validateButton();
  }

  public void validateButton() {
    if (!enterPatientName.getText().trim().isEmpty()
        && !enterPatientID.getText().trim().isEmpty()
        && !nodeTypeDropDown.getSelectionModel().getSelectedItem().equals("")
        && !giftDropDown.getSelectionModel().getSelectedItem().equals("")) {
      submitButton.setDisable(false);
    } else {
      submitButton.setDisable(true);
    }
  }
}
