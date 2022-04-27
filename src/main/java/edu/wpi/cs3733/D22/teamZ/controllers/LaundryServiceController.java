package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.LaundryServiceRequest;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;

public class LaundryServiceController extends ServiceRequestController implements IMenuAccess {

  @FXML public Label patientIDFormHeader;
  @FXML public Label locationFormHeader;
  @FXML public Label linenTypeFormHeader;
  private FacadeDAO dao = FacadeDAO.getInstance();
  private final String toHomePageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";
  private final String toListPageURL =
      "edu/wpi/cs3733/D22/teamZ/views/LaundryServiceRequestList.fxml";

  @FXML private Label successSubmitLabel;
  @FXML private Label errorLabel;
  @FXML private Label seeRequestsLabel;
  @FXML private MFXTextField patientIDField;
  @FXML private MFXTextField locationField;
  @FXML private MFXButton labServiceRequestListButton;
  @FXML private MFXTextField linenTypeField;
  @FXML private Region backRegion;

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

    menuName = "Laundry Service Request";
    submitButton.setDisable(true);
    labServiceRequestListButton.setVisible(true);
    seeRequestsLabel.setVisible(true);
    errorLabel.setVisible(false);
    successSubmitLabel.setVisible(false);
    initializeHelpGraphic();
  }

  @Override
  protected void onSubmitButtonClicked(ActionEvent event) throws SQLException {
    List<ServiceRequest> serviceRequestList = database.getAllServiceRequests();
    if (dao.getLocationByID(locationField.getText()).getNodeID() == null) {
      errorLabel.setVisible(true);
      System.out.println("FAIL");
    } else {
      UniqueID id = new UniqueID();
      String requestID = id.generateID("LAUND");

      LaundryServiceRequest temp =
          new LaundryServiceRequest(
              requestID,
              ServiceRequest.RequestStatus.UNASSIGNED,
              MenuController.getLoggedInUser(),
              null,
              dao.getLocationByID(locationField.getText()),
              LocalDateTime.now(),
              null,
              LaundryServiceRequest.LaundryStatus.DIRTY,
              linenTypeField.getText());

      if (dao.addServiceRequest(temp) && dao.addLaundryServiceRequest(temp)) {
        successSubmitLabel.setVisible(true);
        errorLabel.setVisible(false);
      } else {
        errorLabel.setVisible(true);
        successSubmitLabel.setVisible(false);
      }
    }
    // Add submitting functionality here!
  }

  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  protected void onResetButtonClicked(ActionEvent event) throws IOException {
    // Reset fields here!
    locationField.clear();
    patientIDField.clear();
    linenTypeField.clear();
    errorLabel.setVisible(false);
    submitButton.setDisable(true);
    successSubmitLabel.setVisible(false);
  }

  @Override
  protected void highlightRequirements(boolean visible) {
    if (visible) {
      patientIDFormHeader.getStyleClass().clear();
      patientIDFormHeader.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(
          patientIDFormHeader, "Enter ID of patient that\nneeds laundry to be done");

      locationFormHeader.getStyleClass().clear();
      locationFormHeader.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(
          locationFormHeader, "Enter room number that\npatient's used laundry is located");

      linenTypeFormHeader.getStyleClass().clear();
      linenTypeFormHeader.getStyleClass().add("form-header-help");
      enableToolTipOnLabel(linenTypeFormHeader, "Enter linen type that\nneeds to be washed");
    } else {
      patientIDFormHeader.getStyleClass().clear();
      patientIDFormHeader.getStyleClass().add("form-header");
      patientIDFormHeader.setTooltip(null);

      locationFormHeader.getStyleClass().clear();
      locationFormHeader.getStyleClass().add("form-header");
      locationFormHeader.setTooltip(null);

      linenTypeFormHeader.getStyleClass().clear();
      linenTypeFormHeader.getStyleClass().add("form-header");
      linenTypeFormHeader.setTooltip(null);
    }
  }

  public void validateButton(KeyEvent keyEvent) {
    if (!locationField.getText().isEmpty()
        && !patientIDField.getText().isEmpty()
        && !linenTypeField.getText().isEmpty()) submitButton.setDisable(false);
  }

  public void onLaundryListButtonClicked(ActionEvent actionEvent) throws IOException {
    //    FXMLLoader loader = new FXMLLoader();
    //    loader.setLocation(getClass().getClassLoader().getResource(toListPageURL));
    //    Parent root = loader.load();
    //    Scene scene = new Scene(root);
    //    Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    //    primaryStage.setScene(scene);
    //    primaryStage.minHeightProperty().unbind();
    //    primaryStage.maxHeightProperty().unbind();
    //    primaryStage.setMinHeight(
    //        ((Node) actionEvent.getSource())
    //            .getScene()
    //            .getHeight()); // initial size. doesnt work if less so ignore lol.
    //    primaryStage.setMinWidth(((Node) actionEvent.getSource()).getScene().getWidth());
    menu.load(toListPageURL);
  }

  //  public void onThisBackButtonClicked(ActionEvent actionEvent) throws IOException {
  //    menu = new MenuController();
  //    menu.load(toHomePageURL);
  //  }
}
