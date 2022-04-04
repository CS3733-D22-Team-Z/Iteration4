package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.LocationDAOImpl;
import edu.wpi.cs3733.D22.teamZ.database.MedicalEquipmentDAOImpl;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class EditLocationOverlayController {
  @FXML private TextField selectLocationTextField;
  @FXML private ChoiceBox<String> typeChoiceTextField;
  @FXML private ChoiceBox<String> floorChoiceTextField;
  @FXML private TextField changeNumberTextField;
  @FXML private TextField changeNameTextField;
  @FXML private TextField abbreviationTextField;
  @FXML private Text alreadyExistsText;
  @FXML private Button submitButton;
  @FXML private Button clearButton;

  LocationDAOImpl locationDAO = new LocationDAOImpl();
  MedicalEquipmentDAOImpl medicalEquipmentDAO = new MedicalEquipmentDAOImpl();

  /** Sets the dropdown choices to enum types */
  @FXML
  private void initialize() {
    typeChoiceTextField.setItems(
        FXCollections.observableArrayList(
            "DEPT", "HALL", "ELEV", "STOR", "EXIT", "INFO", "RETL", "SERV", "STAI", "BATH", "LABS",
            "PATI"));

    Location temp = locationDAO.getLocationByID(selectLocationTextField.getText());
    typeChoiceTextField.setValue(temp.getNodeType());
  }

  /**
   * Will update location data in database given text inputs on app
   *
   * @param event
   * @throws IOException
   */
  @FXML
  private void submitButtonClicked(ActionEvent event) throws IOException {
    // change later to Neha's nodeID info
    Location tempLocation = locationDAO.getLocationByID(selectLocationTextField.getText());

    tempLocation.setNodeType(typeChoiceTextField.getValue());
    tempLocation.setFloor(floorChoiceTextField.getValue());
    tempLocation.setLongName(changeNameTextField.getText());
    tempLocation.setShortName(abbreviationTextField.getText());

    String newNodeID =
        "z"
            + tempLocation.getNodeType()
            + "0".repeat(3 - changeNumberTextField.getText().length())
            + changeNumberTextField.getText()
            + "0".repeat(2 - tempLocation.getFloor().length())
            + tempLocation.getFloor();

    // check if already exists
    if (locationDAO.getLocationByID(newNodeID).equals(null)) {
      alreadyExistsText.setVisible(false);
      List<MedicalEquipment> medicalEquipmentList =
          medicalEquipmentDAO.getAllMedicalEquipmentByLocation(tempLocation);
      // check if there are medical equipment stuff there
      if (medicalEquipmentList.isEmpty()) {
        // do nothing
      }
      // there is equipment so update to new location
      else {
        for (int i = 0; i < medicalEquipmentList.size(); i++) {
          MedicalEquipment tempMedEquip = medicalEquipmentList.get(i);
          tempMedEquip.setCurrentLocation(tempLocation);
          medicalEquipmentDAO.updateMedicalEquipment(tempMedEquip);
        }
      }
      if (locationDAO.deleteLocation(tempLocation)) {
        System.out.println("Delete location successful");
      }
      tempLocation.setNodeID(newNodeID);
      if (locationDAO.addLocation(tempLocation)) {
        System.out.println("Added updated location successful");
      }
    } else {
      alreadyExistsText.setVisible(true);
    }
  }
}
