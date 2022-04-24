package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.apiFacades.ExternalTransportFacadeAPI;
import edu.wpi.cs3733.D22.teamZ.apiFacades.InternalTransportFacadeAPI;
import edu.wpi.cs3733.D22.teamZ.apiFacades.FacilityMaintenanceFacadeAPI;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class APILandingPageController implements IMenuAccess {
  @FXML private MFXToggleButton toggleNames;
  @FXML private ScrollPane scrollPane;
  @FXML private Region externalRegion;
  @FXML private Label teamZLabel;

  private final String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";

  private final String apiCSSPath = "edu/wpi/cs3733/D22/teamZ/styles/ServiceRequestDefault.css";

  private final String[] icons = {
    "M8 16C8.26522 16 8.51957 15.8946 8.70711 15.7071C8.89464 15.5196 9 15.2652 9 15C9 14.7348 8.89464 14.4804 8.70711 14.2929C8.51957 14.1054 8.26522 14 8 14C7.73478 14 7.48043 14.1054 7.29289 14.2929C7.10536 14.4804 7 14.7348 7 15C7 15.2652 7.10536 15.5196 7.29289 15.7071C7.48043 15.8946 7.73478 16 8 16ZM17 15C17 15.2652 16.8946 15.5196 16.7071 15.7071C16.5196 15.8946 16.2652 16 16 16C15.7348 16 15.4804 15.8946 15.2929 15.7071C15.1054 15.5196 15 15.2652 15 15C15 14.7348 15.1054 14.4804 15.2929 14.2929C15.4804 14.1054 15.7348 14 16 14C16.2652 14 16.5196 14.1054 16.7071 14.2929C16.8946 14.4804 17 14.7348 17 15V15ZM10.75 5C10.5511 5 10.3603 5.07902 10.2197 5.21967C10.079 5.36032 10 5.55109 10 5.75C10 5.94891 10.079 6.13968 10.2197 6.28033C10.3603 6.42098 10.5511 6.5 10.75 6.5H13.25C13.4489 6.5 13.6397 6.42098 13.7803 6.28033C13.921 6.13968 14 5.94891 14 5.75C14 5.55109 13.921 5.36032 13.7803 5.21967C13.6397 5.07902 13.4489 5 13.25 5H10.75ZM7.75 2C6.75544 2 5.80161 2.39509 5.09835 3.09835C4.39509 3.80161 4 4.75544 4 5.75V9.5H2.75C2.55109 9.5 2.36032 9.57902 2.21967 9.71967C2.07902 9.86032 2 10.0511 2 10.25C2 10.4489 2.07902 10.6397 2.21967 10.7803C2.36032 10.921 2.55109 11 2.75 11H4V19.75C4 20.716 4.783 21.5 5.75 21.5H7.25C7.71413 21.5 8.15925 21.3156 8.48744 20.9874C8.81563 20.6592 9 20.2141 9 19.75V18.5H15V19.75C15 20.716 15.784 21.5 16.75 21.5H18.25C18.7141 21.5 19.1592 21.3156 19.4874 20.9874C19.8156 20.6592 20 20.2141 20 19.75V11H21.227C21.4259 11 21.6167 10.921 21.7573 10.7803C21.898 10.6397 21.977 10.4489 21.977 10.25C21.977 10.0511 21.898 9.86032 21.7573 9.71967C21.6167 9.57902 21.4259 9.5 21.227 9.5H20V5.75C20 4.75544 19.6049 3.80161 18.9017 3.09835C18.1984 2.39509 17.2446 2 16.25 2H7.75ZM18.5 18.5V19.75C18.5 19.8163 18.4737 19.8799 18.4268 19.9268C18.3799 19.9737 18.3163 20 18.25 20H16.75C16.6837 20 16.6201 19.9737 16.5732 19.9268C16.5263 19.8799 16.5 19.8163 16.5 19.75V18.5H18.5ZM18.5 17H5.5V13H18.5V17ZM5.5 19.75V18.5H7.5V19.75C7.5 19.8163 7.47366 19.8799 7.42678 19.9268C7.37989 19.9737 7.3163 20 7.25 20H5.75C5.6837 20 5.62011 19.9737 5.57322 19.9268C5.52634 19.8799 5.5 19.8163 5.5 19.75ZM5.5 5.75C5.5 5.15326 5.73705 4.58097 6.15901 4.15901C6.58097 3.73705 7.15326 3.5 7.75 3.5H16.25C16.8467 3.5 17.419 3.73705 17.841 4.15901C18.2629 4.58097 18.5 5.15326 18.5 5.75V11.5H5.5V5.75Z",
  };

  private MenuController menu;
  private final String grey = "#0067B1";
  private final String svgCSSLine = "-fx-background-color: %s";

  public void initialize() {
    toggleNames.setMainColor(Color.rgb(0, 103, 177));
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    SVGPath externalIcon = new SVGPath();
    externalIcon.setContent(icons[0]);
    externalRegion.setShape(externalIcon);
    externalRegion.setStyle(String.format(svgCSSLine, grey));

    // Set each name label to center
    teamZLabel.setAlignment(Pos.CENTER);
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "API Service Request Landing";
  }

  @FXML
  private void navLandingPage() throws IOException {
    System.out.println("navigating to landing page from api landing page");
    menu.load(toLandingPageURL);
  }

  @FXML
  private void toExternalPatientTransportation() {
    System.out.println("navigating to transportation from api landing page");
    ExternalTransportFacadeAPI.getInstance().run(apiCSSPath);
  }

  @FXML
  private void toSanitationRequest() {
    System.out.println("navigating to sanitation from api landing page");
    InternalTransportFacadeAPI.getInstance().run(apiCSSPath);
  }

  @FXML
  private void toSecurityRequest() {
    System.out.println("navigating to security from api landing page");
    FacilityMaintenanceFacadeAPI.getInstance().run(apiCSSPath);
  }

  @FXML
  private void showNameLabels() {
    boolean set = toggleNames.isSelected();
    teamZLabel.setVisible(set);
  }
}
