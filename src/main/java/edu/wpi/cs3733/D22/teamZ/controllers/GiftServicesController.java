package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.*;
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
import javafx.scene.control.Label;

public class GiftServicesController extends ServiceRequestController {

  private List<Location> roomList;
  private List<String> roomNumbers;
  private List<GiftServiceRequest> giftRequestList;
  @FXML private MFXTextField enterPatientName;
  @FXML private MFXTextField enterPatientID;
  @FXML private ChoiceBox<String> nodeTypeDropDown;
  @FXML private ChoiceBox<String> giftDropDown;
  private ObservableList roomNumberNames;
  @FXML private Label submittedLabel;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    menuName = "Gift Services";
    roomList = database.getALlLocationsByType("PATI");
    roomNumbers = roomList.stream().map(loc -> loc.getLongName()).collect(Collectors.toList());
    roomNumberNames = FXCollections.observableList(roomNumbers);
    roomNumberNames.add(0, "");
    try {
      giftRequestList = FacadeDAO.getInstance().getAllGiftRequests();
    } catch (SQLException e) {
      System.out.println("failed to make gift list");
    }

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
    submittedLabel.setVisible(false);
  }

  @Override
  protected void onSubmitButtonClicked(ActionEvent event) throws SQLException {
    String id;
    // Check for empty db and set first request (will appear as REQ1 in the db)

    if (FacadeDAO.getInstance().getAllServiceRequests().isEmpty()) {
      System.out.println("Gift is empty");
      id = "REQ0";
    } else {
      List<ServiceRequest> currentList = database.getAllServiceRequests();
      ServiceRequest lastestReq = currentList.get(currentList.size() - 1);
      id = lastestReq.getRequestID();
    }
    // Create new REQID
    int num = 1 + Integer.parseInt(id.substring(id.lastIndexOf("Q") + 1));
    String requestID = "REQ" + num;

    // Creates entities for submission
    ServiceRequest.RequestStatus status = ServiceRequest.RequestStatus.UNASSIGNED;
    Employee issuer = MenuController.getLoggedInUser();
    Employee handler = null;

    Location temp = null;

    for (Location loc : FacadeDAO.getInstance().getAllLocations()) {
      if (loc.getLongName().equals(nodeTypeDropDown.getValue())) {
        temp = FacadeDAO.getInstance().getLocationByID(loc.getNodeID());
      }
    }

    Patient tempPat = null;

    for (Patient pat : FacadeDAO.getInstance().getAllPatients()) {
      if (pat.getPatientID().equals(enterPatientID.getText())) {
        tempPat = FacadeDAO.getInstance().getPatientByID(pat.getPatientID());
      }
    }

    GiftServiceRequest gift =
        new GiftServiceRequest(
            requestID,
            status,
            issuer,
            handler,
            temp,
            tempPat.getName(),
            tempPat.getPatientID(),
            giftDropDown.getValue());

    FacadeDAO.getInstance().addGiftRequest(gift);

    submittedLabel.setVisible(true);
    try {
      onResetButtonClicked(event);
    } catch (IOException e) {
      System.out.println("failed to clear fields");
    }
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
