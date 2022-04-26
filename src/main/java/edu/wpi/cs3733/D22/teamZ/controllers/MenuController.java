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
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
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
  @FXML VBox sliderContainer;
  @FXML Region exitGraphic;
  @FXML Region logoutGraphic;
  @FXML VBox iconContainer;
  @FXML Label timeLabel;
  @FXML Label dateLabel;

  double initialHeight;
  double initialWidth;

  // intermediate login scaling ans size
  public ObservableList<Transform> initialStates;
  private ChangeListener<? super Number> sizeChangeListener;

  private static Employee loggedInUser;

  // SVGS
  private String exitIcon =
      "M10 4H8V8H4V10H10V4ZM8 20H10V14H4V16H8V20ZM20 14H14V20H16V16H20V14ZM20 8H16V4H14V10H20V8Z";
  private String[] icons = {
    "M12 5.69l5 4.5V18h-2v-6H9v6H7v-7.81l5-4.5M12 3L2 12h3v8h6v-6h2v6h6v-8h3L12 3z",
    "M20.5 3l-.16.03L15 5.1 9 3 3.36 4.9c-.21.07-.36.25-.36.48V20.5c0 .28.22.5.5.5l.16-.03L9 18.9l6 2.1 5.64-1.9c.21-.07.36-.25.36-.48V3.5c0-.28-.22-.5-.5-.5zM10 5.47l4 1.4v11.66l-4-1.4V5.47zm-5 .99l3-1.01v11.7l-3 1.16V6.46zm14 11.08l-3 1.01V6.86l3-1.16v11.84z",
    "M3,3v8h8V3H3z M9,9H5V5h4V9z M3,13v8h8v-8H3z M9,19H5v-4h4V19z M13,3v8h8V3H13z M19,9h-4V5h4V9z M13,13v8h8v-8H13z M19,19h-4v-4h4V19z",
    "M11 7h6v2h-6zm0 4h6v2h-6zm0 4h6v2h-6zM7 7h2v2H7zm0 4h2v2H7zm0 4h2v2H7zM20.1 3H3.9c-.5 0-.9.4-.9.9v16.2c0 .4.4.9.9.9h16.2c.4 0 .9-.5.9-.9V3.9c0-.5-.5-.9-.9-.9zM19 19H5V5h14v14z",
    "M 22.65 34 H 25.65 V 22 H 22.65 Z M 24 18.3 Q 24.7 18.3 25.175 17.85 Q 25.65 17.4 25.65 16.7 Q 25.65 16 25.175 15.5 Q 24.7 15 24 15 Q 23.3 15 22.825 15.5 Q 22.35 16 22.35 16.7 Q 22.35 17.4 22.825 17.85 Q 23.3 18.3 24 18.3 Z M 24 44 Q 19.75 44 16.1 42.475 Q 12.45 40.95 9.75 38.25 Q 7.05 35.55 5.525 31.9 Q 4 28.25 4 24 Q 4 19.8 5.525 16.15 Q 7.05 12.5 9.75 9.8 Q 12.45 7.1 16.1 5.55 Q 19.75 4 24 4 Q 28.2 4 31.85 5.55 Q 35.5 7.1 38.2 9.8 Q 40.9 12.5 42.45 16.15 Q 44 19.8 44 24 Q 44 28.25 42.45 31.9 Q 40.9 35.55 38.2 38.25 Q 35.5 40.95 31.85 42.475 Q 28.2 44 24 44 Z M 24 24 Q 24 24 24 24 Q 24 24 24 24 Q 24 24 24 24 Q 24 24 24 24 Q 24 24 24 24 Q 24 24 24 24 Q 24 24 24 24 Q 24 24 24 24 Z M 24 41 Q 31 41 36 36 Q 41 31 41 24 Q 41 17 36 12 Q 31 7 24 7 Q 17 7 12 12 Q 7 17 7 24 Q 7 31 12 36 Q 17 41 24 41 Z"
  };
  private String logoutIcon =
      "M60 16L54.36 21.64L64.68 32H24V40H64.68L54.36 50.32L60 56L80 36L60 16ZM8 8H40V0H8C3.6 0 0 3.6 0 8V64C0 68.4 3.6 72 8 72H40V64H8V8Z";

  // FXMLS
  @FXML private Label pageLabel;

  // String that holds the pageLabel's text
  private SimpleStringProperty currentPage;

  // CSS line to set text fill
  private String textCSSLine = "-fx-text-fill: %s;";
  private String svgCSSLine = "-fx-background-color: %s";

  // Colors representing the grey and blue values used in the fxmls.
  private String grey = "#C4C4C4";
  private String blue = "#FFFFFF";

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
  private String toServiceURL = "edu/wpi/cs3733/D22/teamZ/views/ServiceRequest.fxml";
  private String toAboutPageURL = "edu/wpi/cs3733/D22/teamZ/views/AboutPage.fxml";

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
    logoutGraphic.setShape(LogoutIcon);
    logoutGraphic.setStyle(String.format(svgCSSLine, "#FFFFFF"));

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
    Rectangle oldSlider = (Rectangle) sliderContainer.getChildren().get(selectedItem);
    oldSlider.setVisible(false);

    // Select current item
    selectedItem = item;

    // Repeat
    MFXButton newMenuItem = (MFXButton) menuContainer.getChildren().get(selectedItem);
    newMenuItem.setStyle(String.format(textCSSLine, blue));
    Region newGraphic = (Region) iconContainer.getChildren().get(selectedItem);
    newGraphic.setStyle(String.format(svgCSSLine, blue));
    Rectangle newSlider = (Rectangle) sliderContainer.getChildren().get(selectedItem);
    newSlider.setVisible(true);
  }

  @FXML
  private void toHome() throws IOException {
    selectMenu(0);
    load(toHomeURL);
  }

  @FXML
  private void toAbout() throws IOException {
    selectMenu(4);
    load(toAboutPageURL);
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
    load(toServiceURL);
  }

  @FXML
  private void toExit() {
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  private void toLogout(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getClassLoader().getResource(toLoginURL));
    Parent root = loader.load();
    Scene scene = new Scene(root);
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    primaryStage.setScene(scene);
    primaryStage.minHeightProperty().unbind();
    primaryStage.maxHeightProperty().unbind();
    initialHeight = 400;
    initialWidth = 600;
    primaryStage.setMinHeight(initialHeight); // initial size. doesnt work if less so ignore lol.
    primaryStage.setMinWidth(initialWidth);
    double initialRatio = initialHeight / initialWidth;
    primaryStage.minHeightProperty().bind(primaryStage.widthProperty().multiply(initialRatio));
    primaryStage.maxHeightProperty().bind(primaryStage.widthProperty().multiply(initialRatio));
    sizeChangeListener =
        (ChangeListener<Number>)
            (observable, oldValue, newValue) -> {
              onSizeChange(root, primaryStage);
            };

    primaryStage.heightProperty().addListener(sizeChangeListener);
    primaryStage.widthProperty().addListener(sizeChangeListener);
  }

  public void onSizeChange(Parent root, Stage primaryStage) {
    // System.out.println("old:" + oldValue + " new:" + newValue);
    float scaleY = (float) (primaryStage.getHeight() / initialHeight);
    float scaleX = (float) (primaryStage.getWidth() / initialWidth);
    if (initialStates == null) {
      initialStates = root.getTransforms();
    }
    root.getTransforms().setAll(initialStates);

    root.getTransforms().add(new Scale(scaleY, scaleY, 0, 0));
  }

  public static Employee getLoggedInUser() {
    return MenuController.loggedInUser;
  }

  public static void setLoggedInUser(Employee loggedInUser) {
    MenuController.loggedInUser = loggedInUser;
  }
}
