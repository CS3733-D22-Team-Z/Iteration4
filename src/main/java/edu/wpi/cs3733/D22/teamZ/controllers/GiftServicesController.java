package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.*;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
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

  @FXML public Label patientNameFormHeader;
  @FXML public Label patientIDFormHeader;
  @FXML public Label roomNumberFormHeader;
  @FXML public Label giftServiceOptionFormHeader;
  private List<Location> roomList;
  private List<String> roomNumbers;
  private List<GiftServiceRequest> giftRequestList;
  @FXML private MFXTextField enterPatientName;
  @FXML private MFXTextField enterPatientID;
  @FXML private ChoiceBox<String> nodeTypeDropDown;
  @FXML private ChoiceBox<String> giftDropDown;
  private ObservableList roomNumberNames;
  @FXML private Label submittedLabel;

  private String backSVG =
      "M 13.83 19 C 13.6806 19.0005 13.533 18.9675 13.398 18.9035 C 13.263 18.8395 13.1441 18.746 13.05 18.63 L 8.22 12.63 C 8.07291 12.4511 7.99251 12.2266 7.99251 11.995 C 7.99251 11.7634 8.07291 11.5389 8.22 11.36 L 13.22 5.36 C 13.3897 5.15578 13.6336 5.02736 13.8981 5.00298 C 14.1625 4.9786 14.4258 5.06026 14.63 5.23 C 14.8342 5.39974 14.9626 5.64365 14.987 5.90808 C 15.0114 6.1725 14.9297 6.43578 14.76 6.64 L 10.29 12 L 14.61 17.36 C 14.7323 17.5068 14.81 17.6855 14.8338 17.8751 C 14.8577 18.0646 14.8268 18.257 14.7447 18.4296 C 14.6627 18.6021 14.5329 18.7475 14.3708 18.8486 C 14.2087 18.9497 14.021 19.0022 13.83 19 Z";
  private String white = "FFFFFF";
  private String svgCSSLine = "-fx-background-color: %s";

  private String toList = "edu/wpi/cs3733/D22/teamZ/views/GiftServicesList.fxml";

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    menuName = "Gift Services";
    roomList = database.getAllLocationsByType("PATI");
    roomNumbers = roomList.stream().map(Location::getLongName).collect(Collectors.toList());
    roomNumberNames = FXCollections.observableList(roomNumbers);
    roomNumberNames.add(0, "");
    try {
      giftRequestList = FacadeDAO.getInstance().getAllGiftRequests();
    } catch (SQLException e) {
      System.out.println("failed to make gift list");
    }

    giftDropDown.setItems(
        FXCollections.observableArrayList("", "Bouquet", "Chocolates", "Stuffed Animal", "Card"));

    nodeTypeDropDown.setItems(roomNumberNames);

    // //example
    nodeTypeDropDown.getSelectionModel().select(0);
    giftDropDown.getSelectionModel().select(0);

    submitButton.setDisable(true);

    nodeTypeDropDown.setOnAction(event -> validateButton());
    giftDropDown.setOnAction(event -> validateButton());
    submittedLabel.setVisible(false);
    initializeHelpGraphic();
  }

  @Override
  protected void onSubmitButtonClicked(ActionEvent event) throws SQLException {

    UniqueID id = new UniqueID();
    String requestID = id.generateID("GIFT");

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

    assert tempPat != null;
    LocalDateTime opened = LocalDateTime.now();
    LocalDateTime closed = null;

    GiftServiceRequest gift =
        new GiftServiceRequest(
            requestID,
            status,
            issuer,
            handler,
            temp,
            tempPat.getName(),
            tempPat.getPatientID(),
            giftDropDown.getValue(),
            opened,
            closed);

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

  @Override
  protected void highlightRequirements(boolean visible) {
    if (visible) {
      patientNameFormHeader.getStyleClass().clear();
      patientNameFormHeader.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(
          patientNameFormHeader, "Enter name of patient that\ngift is delivered to");

      patientIDFormHeader.getStyleClass().clear();
      patientIDFormHeader.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(patientIDFormHeader, "Enter ID of patient that\ngift is delivered to");

      roomNumberFormHeader.getStyleClass().clear();
      roomNumberFormHeader.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(roomNumberFormHeader, "Select patient room that\ngift is delivered to");

      giftServiceOptionFormHeader.getStyleClass().clear();
      giftServiceOptionFormHeader.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(giftServiceOptionFormHeader, "Select type of gift\nthat is delivered");
    } else {
      patientNameFormHeader.getStyleClass().clear();
      patientNameFormHeader.getStyleClass().add("form-header");
      patientNameFormHeader.setTooltip(null);

      patientIDFormHeader.getStyleClass().clear();
      patientIDFormHeader.getStyleClass().add("form-header");
      patientIDFormHeader.setTooltip(null);

      roomNumberFormHeader.getStyleClass().clear();
      roomNumberFormHeader.getStyleClass().add("form-header");
      roomNumberFormHeader.setTooltip(null);

      giftServiceOptionFormHeader.getStyleClass().clear();
      giftServiceOptionFormHeader.getStyleClass().add("form-header");
      giftServiceOptionFormHeader.setTooltip(null);
    }
  }

  public void validateButton() {
    submitButton.setDisable(
        enterPatientName.getText().trim().isEmpty()
            || enterPatientID.getText().trim().isEmpty()
            || nodeTypeDropDown.getSelectionModel().getSelectedItem().equals("")
            || giftDropDown.getSelectionModel().getSelectedItem().equals(""));
  }

  @FXML
  protected void toList(ActionEvent actionEvent) throws IOException {
    menu.load(toList);
  }
}
