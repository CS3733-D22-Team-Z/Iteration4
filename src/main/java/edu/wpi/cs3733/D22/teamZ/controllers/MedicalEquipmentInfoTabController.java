package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.MapLabel;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCircleToggleNode;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class MedicalEquipmentInfoTabController {

  public MFXButton resetButton;
  @FXML private MFXTextField equipmentTypeField;
  @FXML private ChoiceBox equipmentStatusChoice;
  @FXML private MFXTextField equipmentLocationField;
  @FXML private ChoiceBox<String> equipmentComboBox;
  @FXML private Label errorLabel;
  @FXML private MFXTextField equipmentIDField;
  @FXML private MFXButton exitButton;
  @FXML private MFXButton saveButton;
  @FXML private MFXCircleToggleNode editToggle;

  ObservableList<String> listOfStatus;
  FacadeDAO facadeDAO = FacadeDAO.getInstance();
  ArrayList<MedicalEquipment> medicalEquipmentList;
  ArrayList<String> medicalEquipmentIDs;

  private MapLabel activeLabel;
  private SVGPath icon;

  private final String white = "#FFFFFF";
  private final String svgCSSLine = "-fx-background-color: %s";

  @FXML
  public void initialize() {
    SVGPath icon = new SVGPath();

    icon.setContent(
        "M4.41999 20.5789C4.28125 20.5787 4.14408 20.5496 4.0172 20.4934C3.89031 20.4373 3.7765 20.3554 3.68299 20.2529C3.58779 20.1513 3.51514 20.0307 3.46982 19.899C3.42449 19.7674 3.40751 19.6276 3.41999 19.4889L3.66499 16.7949L14.983 5.48091L18.52 9.01691L7.20499 20.3299L4.51099 20.5749C4.48074 20.5777 4.45037 20.579 4.41999 20.5789V20.5789ZM19.226 8.30991L15.69 4.77391L17.811 2.65291C17.9039 2.55993 18.0142 2.48617 18.1355 2.43585C18.2569 2.38552 18.3871 2.35962 18.5185 2.35962C18.6499 2.35962 18.78 2.38552 18.9014 2.43585C19.0228 2.48617 19.1331 2.55993 19.226 2.65291L21.347 4.77391C21.44 4.86678 21.5137 4.97707 21.5641 5.09847C21.6144 5.21986 21.6403 5.34999 21.6403 5.48141C21.6403 5.61282 21.6144 5.74295 21.5641 5.86435C21.5137 5.98574 21.44 6.09603 21.347 6.18891L19.227 8.30891L19.226 8.30991V8.30991Z");
    icon.setFill(Color.GRAY);

    editToggle.setGraphic(icon);

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
  }

  public void onSelectMedicalEquipment(ActionEvent actionEvent) {
    MedicalEquipment selectedEquipment =
        getMedicalEquipmentbyID(equipmentComboBox.getSelectionModel().getSelectedItem().toString());
    equipmentTypeField.setText(selectedEquipment.getType());
    equipmentStatusChoice.setValue(selectedEquipment.getStatus().toString());
    equipmentIDField.setText(selectedEquipment.getEquipmentID());
    equipmentLocationField.setText(selectedEquipment.getCurrentLocation().getNodeID());
    editToggle.setDisable(false);
  }

  public void onEditEquipment(ActionEvent actionEvent) {
    equipmentStatusChoice.setDisable(false);
    equipmentLocationField.setDisable(false);
    saveButton.setVisible(true);
    resetButton.setVisible(true);
  }

  public void onEditToggle() {
    equipmentStatusChoice.setDisable(false);
    equipmentLocationField.setDisable(false);
    saveButton.setVisible(true);
    resetButton.setVisible(true);
  }

  public void toggleAction() {
    System.out.println("hello");
    if (editToggle.isSelected()) {
      SVGPath icon2 = new SVGPath();

      icon2.setContent(
          "M4.41999 20.5789C4.28125 20.5787 4.14408 20.5496 4.0172 20.4934C3.89031 20.4373 3.7765 20.3554 3.68299 20.2529C3.58779 20.1513 3.51514 20.0307 3.46982 19.899C3.42449 19.7674 3.40751 19.6276 3.41999 19.4889L3.66499 16.7949L14.983 5.48091L18.52 9.01691L7.20499 20.3299L4.51099 20.5749C4.48074 20.5777 4.45037 20.579 4.41999 20.5789V20.5789ZM19.226 8.30991L15.69 4.77391L17.811 2.65291C17.9039 2.55993 18.0142 2.48617 18.1355 2.43585C18.2569 2.38552 18.3871 2.35962 18.5185 2.35962C18.6499 2.35962 18.78 2.38552 18.9014 2.43585C19.0228 2.48617 19.1331 2.55993 19.226 2.65291L21.347 4.77391C21.44 4.86678 21.5137 4.97707 21.5641 5.09847C21.6144 5.21986 21.6403 5.34999 21.6403 5.48141C21.6403 5.61282 21.6144 5.74295 21.5641 5.86435C21.5137 5.98574 21.44 6.09603 21.347 6.18891L19.227 8.30891L19.226 8.30991V8.30991Z");
      icon2.setFill(Color.WHITE);
      editToggle.setGraphic(icon2);
      System.out.println("Selected");
      equipmentStatusChoice.setDisable(false);
      equipmentLocationField.setDisable(false);
      saveButton.setVisible(true);
      resetButton.setVisible(true);
    } else {
      SVGPath icon3 = new SVGPath();

      icon3.setContent(
          "M4.41999 20.5789C4.28125 20.5787 4.14408 20.5496 4.0172 20.4934C3.89031 20.4373 3.7765 20.3554 3.68299 20.2529C3.58779 20.1513 3.51514 20.0307 3.46982 19.899C3.42449 19.7674 3.40751 19.6276 3.41999 19.4889L3.66499 16.7949L14.983 5.48091L18.52 9.01691L7.20499 20.3299L4.51099 20.5749C4.48074 20.5777 4.45037 20.579 4.41999 20.5789V20.5789ZM19.226 8.30991L15.69 4.77391L17.811 2.65291C17.9039 2.55993 18.0142 2.48617 18.1355 2.43585C18.2569 2.38552 18.3871 2.35962 18.5185 2.35962C18.6499 2.35962 18.78 2.38552 18.9014 2.43585C19.0228 2.48617 19.1331 2.55993 19.226 2.65291L21.347 4.77391C21.44 4.86678 21.5137 4.97707 21.5641 5.09847C21.6144 5.21986 21.6403 5.34999 21.6403 5.48141C21.6403 5.61282 21.6144 5.74295 21.5641 5.86435C21.5137 5.98574 21.44 6.09603 21.347 6.18891L19.227 8.30891L19.226 8.30991V8.30991Z");
      icon3.setFill(Color.GRAY);
      editToggle.setGraphic(icon3);
      equipmentStatusChoice.setDisable(true);
      equipmentLocationField.setDisable(true);
      saveButton.setVisible(false);
      resetButton.setVisible(false);
    }
  }

  public void onSaveEquipment(ActionEvent actionEvent) {

    MedicalEquipment editedMedicalEquipment = getMedicalEquipmentbyID(equipmentIDField.getText());

    editedMedicalEquipment.setStatus(
        MedicalEquipment.EquipmentStatus.getRequestStatusByString(
            equipmentStatusChoice.getValue().toString()));

    editedMedicalEquipment.setCurrentLocation(
        FacadeDAO.getInstance().getLocationByID(equipmentLocationField.getText()));

    if (editedMedicalEquipment.getCurrentLocation().getNodeType().equals("DIRT")) {
      editedMedicalEquipment.setStatus(MedicalEquipment.EquipmentStatus.DIRTY);
    } else if (editedMedicalEquipment.getCurrentLocation().equals("STOR")) {
      editedMedicalEquipment.setStatus(MedicalEquipment.EquipmentStatus.CLEAN);
    }

    if (facadeDAO.updateMedicalEquipment(editedMedicalEquipment)) {
      equipmentLocationField.setDisable(true);
      saveButton.setVisible(false);
      editToggle.setDisable(true);
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
    editToggle.setDisable(true);
    equipmentLocationField.setDisable(false);
  }
}
