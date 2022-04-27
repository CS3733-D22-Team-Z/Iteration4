package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.MapLabel;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class MedicalEquipmentInfoTabController {

  public MFXButton resetButton;
  @FXML private MFXTextField equipmentTypeField;
  @FXML private ChoiceBox equipmentStatusChoice;
  @FXML private MFXTextField equipmentLocationField;
  @FXML private ChoiceBox<String> equipmentComboBox;
  @FXML private Label errorLabel;
  @FXML private MFXTextField equipmentIDField;
  @FXML private MFXButton exitButton;
  @FXML private MFXButton editButton;
  @FXML private MFXButton saveButton;

  ObservableList<String> listOfStatus;
  FacadeDAO facadeDAO = FacadeDAO.getInstance();
  ArrayList<MedicalEquipment> medicalEquipmentList;
  ArrayList<String> medicalEquipmentIDs;

  private MapLabel activeLabel;

  @FXML
  public void initialize() {

    medicalEquipmentIDs = new ArrayList<String>();
    // Get all medical equipment
    medicalEquipmentList = new ArrayList<>(facadeDAO.getAllMedicalEquipment());
    listOfStatus = FXCollections.observableList(new ArrayList<>());
    listOfStatus.setAll(
        MedicalEquipment.EquipmentStatus.DIRTY.toString(),
        MedicalEquipment.EquipmentStatus.INUSE.toString(),
        MedicalEquipment.EquipmentStatus.CLEAN.toString(),
        MedicalEquipment.EquipmentStatus.CLEANING.toString());

    setLabel(LocationListController.getActiveLabel());

    // Get all id's into arrayList
    for (MedicalEquipment medicalEquipment : medicalEquipmentList) {
      if (medicalEquipment
          .getCurrentLocation()
          .getNodeID()
          .equals(activeLabel.getLocation().getNodeID())) {
        medicalEquipmentIDs.add(medicalEquipment.getEquipmentID());
      }
    }
    // Set equipment combo box items to list of IDS at location
    equipmentComboBox.setItems(FXCollections.observableList(medicalEquipmentIDs));

    // Set all fields to uneditable
    equipmentIDField.setDisable(true);
    equipmentLocationField.setDisable(true);
    equipmentStatusChoice.setDisable(true);
    equipmentTypeField.setDisable(true);

    // Hide buttons
    saveButton.setVisible(false);
    resetButton.setVisible(false);
    editButton.setDisable(true);

    // Hide error field
    errorLabel.setVisible(false);

    exitButton.setVisible(false);

    // listener
    equipmentStatusChoice
        .valueProperty()
        .addListener(
            observable -> {
              if (getMedicalEquipmentbyID(equipmentComboBox.getValue()).getType().equals("Bed")) {
                if (equipmentStatusChoice.getValue().equals("DIRTY")) {
                  equipmentLocationField.setText(
                      FacadeDAO.getInstance()
                          .getLocationByID(
                              FacadeDAO.getInstance()
                                  .getRandomBedParkNodeIDByFloor(
                                      getMedicalEquipmentbyID(equipmentComboBox.getValue())
                                          .getCurrentLocation()
                                          .getFloor()))
                          .getNodeID());
                  equipmentLocationField.setDisable(true);
                }
              } else if (getMedicalEquipmentbyID(equipmentComboBox.getValue())
                  .getType()
                  .equals("IPumps")) {
                if (equipmentStatusChoice.getValue().equals("DIRTY")) {
                  equipmentLocationField.setText(
                      FacadeDAO.getInstance()
                          .getLocationByID(
                              FacadeDAO.getInstance()
                                  .getDirtyNodeIDbyFloor(
                                      getMedicalEquipmentbyID(equipmentComboBox.getValue())
                                          .getCurrentLocation()
                                          .getFloor()))
                          .getNodeID());
                  equipmentLocationField.setDisable(true);
                } else {
                  equipmentLocationField.setText(
                      getMedicalEquipmentbyID(equipmentComboBox.getValue())
                          .getCurrentLocation()
                          .getNodeID());
                  equipmentLocationField.setDisable(false);
                }
              }
            });

    equipmentStatusChoice.setItems(listOfStatus);
  }

  public void onSelectMedicalEquipment(ActionEvent actionEvent) {
    MedicalEquipment selectedEquipment =
        getMedicalEquipmentbyID(equipmentComboBox.getSelectionModel().getSelectedItem().toString());
    equipmentTypeField.setText(selectedEquipment.getType());
    equipmentStatusChoice.setValue(selectedEquipment.getStatus().toString());
    equipmentIDField.setText(selectedEquipment.getEquipmentID());
    equipmentLocationField.setText(selectedEquipment.getCurrentLocation().getNodeID());
    editButton.setDisable(false);
  }

  public void onEditEquipment(ActionEvent actionEvent) {
    equipmentStatusChoice.setDisable(false);
    equipmentLocationField.setDisable(false);
    editButton.setVisible(false);
    saveButton.setVisible(true);
    resetButton.setVisible(true);
  }

  public void onSaveEquipment(ActionEvent actionEvent) {

    MedicalEquipment editedMedicalEquipment = getMedicalEquipmentbyID(equipmentIDField.getText());

    editedMedicalEquipment.setStatus(
        MedicalEquipment.EquipmentStatus.getRequestStatusByString(
            equipmentStatusChoice.getValue().toString()));

    editedMedicalEquipment.setCurrentLocation(
        FacadeDAO.getInstance().getLocationByID(equipmentLocationField.getText()));

    // TODO
    /*
    if (editedMedicalEquipment.getCurrentLocation().getNodeType().equals("DIRT")) {
      editedMedicalEquipment.setStatus(MedicalEquipment.EquipmentStatus.DIRTY);
    } else if (editedMedicalEquipment.getCurrentLocation().equals("STOR")) {
      editedMedicalEquipment.setStatus(MedicalEquipment.EquipmentStatus.CLEAN);
    }*/

    if (facadeDAO.updateMedicalEquipment(editedMedicalEquipment)) {
      equipmentLocationField.setDisable(true);
      saveButton.setVisible(false);
      editButton.setVisible(true);
      resetButton.setVisible(false);
      errorLabel.setVisible(false);
    } else {
      errorLabel.setVisible(true);
    }
  }

  public void onExit(ActionEvent actionEvent) {
    // nothing
  }

  public void setLabel(MapLabel activeLabel) {
    this.activeLabel = activeLabel;
  }

  public MedicalEquipment getMedicalEquipmentbyID(String equipmentID) {
    for (MedicalEquipment medicalEquipment : medicalEquipmentList) {
      if (medicalEquipment.getEquipmentID().equals(equipmentID)) {
        return medicalEquipment;
      }
    }
    return null;
  }

  public void onResetButtonClicked(ActionEvent actionEvent) {
    equipmentLocationField.setText(
        getMedicalEquipmentbyID(equipmentComboBox.getValue()).getCurrentLocation().getNodeID());
    equipmentStatusChoice.setValue(
        getMedicalEquipmentbyID(equipmentComboBox.getValue()).getStatus());
    errorLabel.setVisible(false);
    saveButton.setVisible(false);
    editButton.setVisible(true);
    equipmentLocationField.setDisable(false);
  }
}
