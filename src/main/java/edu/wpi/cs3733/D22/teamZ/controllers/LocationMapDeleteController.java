package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LocationMapDeleteController {
  // Buttons
  @FXML private Button deleteMapLocation;
  @FXML private Button cancelLocationSelection;

  // text field box to select location to delete
  @FXML private TextField locationToDeleteTextField;

  FacadeDAO facadeDAO = FacadeDAO.getInstance();

  @FXML
  public void deleteLocation() throws IOException {
    Location temp = facadeDAO.getLocationByID(locationToDeleteTextField.getText());
    if (temp.getNodeID().equals(null)) {
      System.out.println("Did not find location in database");
      return;
    }
    if (facadeDAO.deleteLocation(temp)) {
      System.out.println("Deletion Successful");
    } else {
      System.out.println("There are still stuff in this location");
    }
  }

  @FXML
  public void cancelLocationToDelete() throws IOException {}
}
