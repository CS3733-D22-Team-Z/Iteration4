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
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;

public class LanguageInterpreterController extends ServiceRequestController {

  @FXML private Region backRegion;
  @FXML private MFXTextField enterPatientName;
  @FXML private MFXTextField enterPatientID;
  @FXML private ChoiceBox<String> nodeTypeDropDown;
  @FXML private ChoiceBox<String> languageDropDown;
  private ObservableList roomNumberNames;
  private List<Location> roomList;
  private List<String> roomNumbers;
  private List<LanguageInterpreterRequest> languageInterpreterRequestList;

  private String backSVG =
      "M 13.83 19 C 13.6806 19.0005 13.533 18.9675 13.398 18.9035 C 13.263 18.8395 13.1441 18.746 13.05 18.63 L 8.22 12.63 C 8.07291 12.4511 7.99251 12.2266 7.99251 11.995 C 7.99251 11.7634 8.07291 11.5389 8.22 11.36 L 13.22 5.36 C 13.3897 5.15578 13.6336 5.02736 13.8981 5.00298 C 14.1625 4.9786 14.4258 5.06026 14.63 5.23 C 14.8342 5.39974 14.9626 5.64365 14.987 5.90808 C 15.0114 6.1725 14.9297 6.43578 14.76 6.64 L 10.29 12 L 14.61 17.36 C 14.7323 17.5068 14.81 17.6855 14.8338 17.8751 C 14.8577 18.0646 14.8268 18.257 14.7447 18.4296 C 14.6627 18.6021 14.5329 18.7475 14.3708 18.8486 C 14.2087 18.9497 14.021 19.0022 13.83 19 Z";
  private String white = "FFFFFF";
  private String svgCSSLine = "-fx-background-color: %s";

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    SVGPath icon = new SVGPath();
    icon.setContent(backSVG);
    backRegion.setShape(icon);
    backRegion.setStyle(String.format(svgCSSLine, white));

    menuName = "Language Interpreter Request";

    roomList = database.getALlLocationsByType("PATI");
    roomNumbers = roomList.stream().map(loc -> loc.getLongName()).collect(Collectors.toList());
    roomNumberNames = FXCollections.observableList(roomNumbers);
    roomNumberNames.add(0, "");
    try {
      languageInterpreterRequestList = FacadeDAO.getInstance().getAllLanguageInterpreterRequests();
    } catch (SQLException e) {
      System.out.println("failed to make gift list");
    }

    languageDropDown.setItems(
        FXCollections.observableArrayList("", "French", "Spanish", "Hindi", "Mandarin"));

    nodeTypeDropDown.setItems(roomNumberNames);

    // //example
    nodeTypeDropDown.getSelectionModel().select(0);
    languageDropDown.getSelectionModel().select(0);

    submitButton.setDisable(true);

    nodeTypeDropDown.setOnAction(event -> validateButton());
    languageDropDown.setOnAction(event -> validateButton());
  }

  @Override
  protected void onSubmitButtonClicked(ActionEvent event) throws SQLException {

    UniqueID id = new UniqueID();
    String requestID = id.generateID("LANG");

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

    LocalDateTime opened = LocalDateTime.now();
    LocalDateTime closed = null;

    LanguageInterpreterRequest language =
        new LanguageInterpreterRequest(
            requestID,
            status,
            issuer,
            handler,
            temp,
            opened,
            closed,
            tempPat.getName(),
            tempPat.getPatientID(),
            languageDropDown.getValue());

    FacadeDAO.getInstance().addLanguageInterpreterRequest(language);

    // submittedLabel.setVisible(true);
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
    languageDropDown.getSelectionModel().select(0);
    validateButton();
  }

  @Override
  protected void highlightRequirements(boolean visible) {}

  public void validateButton() {
    if (!enterPatientName.getText().trim().isEmpty()
        && !enterPatientID.getText().trim().isEmpty()
        && !nodeTypeDropDown.getSelectionModel().getSelectedItem().equals("")
        && !languageDropDown.getSelectionModel().getSelectedItem().equals("")) {
      submitButton.setDisable(false);
    } else {
      submitButton.setDisable(true);
    }
  }
}
