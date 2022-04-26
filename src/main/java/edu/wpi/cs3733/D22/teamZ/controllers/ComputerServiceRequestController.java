package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.ComputerServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.UniqueID;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;

public class ComputerServiceRequestController extends ServiceRequestController
    implements IMenuAccess, Initializable {

  @FXML public Label operatingSystemFormHeader;
  @FXML public Label descriptionOfProblemFormHeader;
  @FXML private Region backRegion;
  @FXML private ChoiceBox osField;
  @FXML private MFXTextField descField;
  @FXML private Label errorSavingLabel;
  @FXML private Label successfulSubmitLabel;
  @FXML private MFXButton submitButton;

  protected MenuController menu;
  private String toComputerServiceListURL =
      "edu/wpi/cs3733/D22/teamZ/views/ComputerServiceRequestList.fxml";

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

    osField.setItems(FXCollections.observableArrayList("Linux", "Windows", "MacOS"));

    errorSavingLabel.setVisible(false);
    submitButton.setDisable(true);
    successfulSubmitLabel.setVisible(false);

    osField.setOnAction(event -> validateButton());
    descField.setOnAction(event -> validateButton());
    successfulSubmitLabel.setText("Successfully Submitted Request!");
    initializeHelpGraphic();
  }

  @FXML
  protected void onSubmitButtonClicked(ActionEvent event) throws SQLException {
    List<ServiceRequest> serviceRequestList = FacadeDAO.getInstance().getAllServiceRequests();
    UniqueID id = new UniqueID();
    String requestID = id.generateID("COMP");

    // Create entities for submission

    ServiceRequest.RequestStatus status = ServiceRequest.RequestStatus.UNASSIGNED;
    Employee issuer = MenuController.getLoggedInUser();
    Employee handler = null;
    LocalDateTime opened = LocalDateTime.now();
    LocalDateTime closed = null;

    ComputerServiceRequest temp =
        new ComputerServiceRequest(
            requestID,
            status,
            issuer,
            handler,
            FacadeDAO.getInstance().getLocationByID("zSTOR00101"),
            opened,
            closed,
            osField.getSelectionModel().getSelectedItem().toString(),
            descField.getText());

    if (FacadeDAO.getInstance().addComputerServiceRequest(temp)) {
      this.clearFields();
      successfulSubmitLabel.setVisible(true);
    }
  }

  @FXML
  protected void onResetButtonClicked(ActionEvent event) throws IOException {
    osField.setValue(null);
    descField.setText("");
    successfulSubmitLabel.setVisible(false);
  }

  @Override
  protected void highlightRequirements(boolean visible) {
    if (visible) {
      operatingSystemFormHeader.getStyleClass().clear();
      operatingSystemFormHeader.getStyleClass().add("form-header-help");
      descriptionOfProblemFormHeader.getStyleClass().clear();
      descriptionOfProblemFormHeader.getStyleClass().add("form-header-help");
    } else {
      operatingSystemFormHeader.getStyleClass().clear();
      operatingSystemFormHeader.getStyleClass().add("form-header");
      descriptionOfProblemFormHeader.getStyleClass().clear();
      descriptionOfProblemFormHeader.getStyleClass().add("form-header");
    }
  }

  public void validateButton() {
    if (!osField.getSelectionModel().isEmpty()
    // && !descField.getText().equals("")
    ) {
      submitButton.setDisable(false);
      System.out.println("Equipment Purchase Request Submit Button enabled");
    } else {
      submitButton.setDisable(true);
      System.out.println("Equipment Purchase Request Submit Button disabled");
    }
  }

  @FXML
  public void clearFields() {
    osField.setValue(null);
    descField.setText("");
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Computer Service Request";
  }

  public void toComputerServiceListButton(ActionEvent event) throws IOException {
    menu.load(toComputerServiceListURL);
  }
}
