package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.util.Duration;

/** The controller for the Menu FXML. */
public class MenuController implements Initializable {
  @FXML Pane menuPane;
  @FXML SplitPane rootElement;
  @FXML Pane contentPane;
  @FXML Button exitButton;
  @FXML Button logoutButton;
  @FXML VBox menuContainer;
  @FXML Region exitGraphic;
  @FXML Region logoutGraphic;
  @FXML VBox iconContainer;
  @FXML Label timeLabel;
  @FXML Label dateLabel;

  private static Employee loggedInUser;

  // SVGS
  private String exitIcon =
      "M10 4H8V8H4V10H10V4ZM8 20H10V14H4V16H8V20ZM20 14H14V20H16V16H20V14ZM20 8H16V4H14V10H20V8Z";
  private String[] icons = {
    "M12 5.69l5 4.5V18h-2v-6H9v6H7v-7.81l5-4.5M12 3L2 12h3v8h6v-6h2v6h6v-8h3L12 3z",
    "M20.5 3l-.16.03L15 5.1 9 3 3.36 4.9c-.21.07-.36.25-.36.48V20.5c0 .28.22.5.5.5l.16-.03L9 18.9l6 2.1 5.64-1.9c.21-.07.36-.25.36-.48V3.5c0-.28-.22-.5-.5-.5zM10 5.47l4 1.4v11.66l-4-1.4V5.47zm-5 .99l3-1.01v11.7l-3 1.16V6.46zm14 11.08l-3 1.01V6.86l3-1.16v11.84z",
    "M3,3v8h8V3H3z M9,9H5V5h4V9z M3,13v8h8v-8H3z M9,19H5v-4h4V19z M13,3v8h8V3H13z M19,9h-4V5h4V9z M13,13v8h8v-8H13z M19,19h-4v-4h4V19z",
    "M11 7h6v2h-6zm0 4h6v2h-6zm0 4h6v2h-6zM7 7h2v2H7zm0 4h2v2H7zm0 4h2v2H7zM20.1 3H3.9c-.5 0-.9.4-.9.9v16.2c0 .4.4.9.9.9h16.2c.4 0 .9-.5.9-.9V3.9c0-.5-.5-.9-.9-.9zM19 19H5V5h14v14z",
  };
  private String logoutIcon =
      "M17,8l-1.41,1.41L17.17,11H9v2h8.17l-1.58,1.58L17,16l4-4L17,8z M5,5h7V3H5C3.9,3,3,3.9,3,5v14c0,1.1,0.9,2,2,2h7v-2H5V5z";

  // FXMLS
  @FXML private Label pageLabel;

  // String that holds the pageLabel's text
  private SimpleStringProperty currentPage;

  // CSS line to set text fill
  private String textCSSLine = "-fx-text-fill: %s;";
  private String svgCSSLine = "-fx-background-color: %s";

  // Colors representing the grey and blue values used in the fxmls.
  private String grey = "#C4C4C4";
  private String blue = "#0075FF";

  // Store the ClassLoader for future use
  ClassLoader rscLoader;

  // The currently selected menu item
  private int selectedItem = 0;

  // Path to login lage
  private String toLoginURL = "edu/wpi/cs3733/D22/teamZ/views/LoginPage.fxml";

  // Paths to other pages
  private String toLocationsURL = "edu/wpi/cs3733/D22/teamZ/views/Location.fxml";
  private String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";
  private String toMedicalEquipmentRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentRequestList.fxml";
  private String toHomeURL = "edu/wpi/cs3733/D22/teamZ/views/Homepage.fxml";

  public MenuController() {
    currentPage = new SimpleStringProperty();
    rscLoader = getClass().getClassLoader();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      load(toHomeURL);
    } catch (IOException e) {
      e.printStackTrace();
    }

    pageLabel.textProperty().bind(currentPage);

    // Initialize icons
    for (int i = 0; i < iconContainer.getChildren().size(); i++) {
      Region graphic = (Region) iconContainer.getChildren().get(i);
      SVGPath icon = new SVGPath();
      icon.setContent(icons[i]);
      graphic.setShape(icon);
      if (i > 0) graphic.setStyle(String.format(svgCSSLine, grey));
      else graphic.setStyle(String.format(svgCSSLine, blue));
    }

    // Initialize exit menu
    SVGPath ExitIcon = new SVGPath();
    ExitIcon.setContent(exitIcon);
    exitGraphic.setShape(ExitIcon);
    exitGraphic.setStyle(String.format(svgCSSLine, "#FFFFFF"));

    // Initialize logout menu
    SVGPath LogoutIcon = new SVGPath();
    LogoutIcon.setContent(logoutIcon);
    /*phic.setShape(LogoutIcon);
    logoutGraphic.setStyle(String.format(svgCSSLine, "#FFFFFF"));*/
    // TOFO fix later

    // Setup timer
    DateTimeFormatter timeFormatA = DateTimeFormatter.ofPattern("hh:mm a");
    // DateFormat timeFormatB = new SimpleDateFormat("h:mm aa");
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    final Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.minutes(1),
                event -> {
                  timeLabel.setText(timeFormatA.format(LocalDateTime.now()));
                  dateLabel.setText(dateFormat.format(LocalDateTime.now()));
                }));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();

    // Initialize labels too
    timeLabel.setText(timeFormatA.format(LocalDateTime.now()));
    dateLabel.setText(dateFormat.format(LocalDateTime.now()));
  }

  /**
   * Loads the given FXML file into the content pane.
   *
   * @param path the path to the FXML file
   * @return the controller associated with the FXML
   */
  public Object load(String path) throws IOException {
    // Remove current pane
    contentPane.getChildren().clear();

    // Load FXML
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(rscLoader.getResource(path));
    Node root = loader.load();
    contentPane.getChildren().add(root);

    // Set the menu controller of controller
    IMenuAccess cont = loader.getController();
    cont.setMenuController(this);
    currentPage.set(cont.getMenuName());
    return cont;
  }

  /**
   * Highlights the selected menu item
   *
   * @param item the menu button to be selected
   */
  public void selectMenu(int item) {
    // De-select current item
    MFXButton oldMenuItem = (MFXButton) menuContainer.getChildren().get(selectedItem);
    oldMenuItem.setStyle(String.format(textCSSLine, grey));
    Region oldGraphic = (Region) iconContainer.getChildren().get(selectedItem);
    oldGraphic.setStyle(String.format(svgCSSLine, grey));

    // Select current item
    selectedItem = item;

    // Repeat
    MFXButton newMenuItem = (MFXButton) menuContainer.getChildren().get(selectedItem);
    newMenuItem.setStyle(String.format(textCSSLine, blue));
    Region newGraphic = (Region) iconContainer.getChildren().get(selectedItem);
    newGraphic.setStyle(String.format(svgCSSLine, blue));
  }

  @FXML
  private void toHome() throws IOException {
    selectMenu(0);
    load(toHomeURL);
  }

  @FXML
  private void toLocations() throws IOException {
    selectMenu(1);
    load(toLocationsURL);
  }

  @FXML
  private void toLandingPage() throws IOException {
    selectMenu(2);
    load(toLandingPageURL);
  }

  @FXML
  private void toMedicalEquipmentRequest() throws IOException {
    selectMenu(3);
    load(toMedicalEquipmentRequestURL);
  }

  @FXML
  private void toExit() {
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  private void toLogout(ActionEvent event) throws IOException {
    //    contentPane.getChildren().clear();
    //    // Load FXML
    //    FXMLLoader loader = new FXMLLoader();
    //    FXMLLoader.load(toLoginURL);
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getClassLoader().getResource(toLoginURL));
    Parent root = loader.load();
    Scene scene = new Scene(root);
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    primaryStage.setScene(scene);
  }

  public static Employee getLoggedInUser() {
    return MenuController.loggedInUser;
  }

  public static void setLoggedInUser(Employee loggedInUser) {
    MenuController.loggedInUser = loggedInUser;
  }
}
