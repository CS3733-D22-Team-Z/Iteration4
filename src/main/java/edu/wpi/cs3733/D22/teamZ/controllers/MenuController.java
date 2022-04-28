package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.animation.*;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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
  @FXML AnchorPane contentPane;
  @FXML Button exitButton;
  @FXML Button logoutButton;
  @FXML VBox menuContainer;
  @FXML VBox sliderContainer;
  @FXML Region exitGraphic;
  @FXML Region logoutGraphic;
  @FXML VBox iconContainer;
  @FXML Label timeLabel;
  @FXML Label dateLabel;
  @FXML Region menuRegionButton;

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
    "M18.42 16.31C17.2952 16.2982 16.1992 15.9538 15.2697 15.3204C14.3403 14.6869 13.6191 13.7926 13.1968 12.7501C12.7746 11.7075 12.6702 10.5634 12.8969 9.46169C13.1236 8.35998 13.6711 7.34996 14.4706 6.55879C15.2701 5.76762 16.2858 5.23067 17.3898 5.01556C18.4939 4.80044 19.6369 4.91677 20.6749 5.3499C21.7129 5.78302 22.5996 6.51358 23.2234 7.4496C23.8471 8.38562 24.1799 9.48524 24.18 10.61C24.1694 12.1289 23.5572 13.5817 22.4776 14.65C21.398 15.7184 19.9389 16.3154 18.42 16.31ZM18.42 6.91003C17.6909 6.92185 16.9815 7.14886 16.381 7.56255C15.7804 7.97623 15.3156 8.55814 15.0447 9.23518C14.7738 9.91222 14.7091 10.6542 14.8585 11.3679C15.008 12.0817 15.365 12.7353 15.8848 13.2468C16.4046 13.7583 17.0639 14.1047 17.7799 14.2427C18.496 14.3807 19.2368 14.304 19.9094 14.0223C20.582 13.7405 21.1564 13.2664 21.5603 12.6593C21.9643 12.0522 22.1799 11.3392 22.18 10.61C22.1694 9.62159 21.768 8.67753 21.0634 7.98421C20.3588 7.29088 19.4084 6.90468 18.42 6.91003V6.91003Z M18.42 16.31C17.2953 16.2982 16.1992 15.9538 15.2698 15.3204C14.3403 14.6869 13.6191 13.7926 13.1968 12.7501C12.7746 11.7075 12.6703 10.5634 12.8969 9.46169C13.1236 8.35998 13.6711 7.34996 14.4706 6.55879C15.2701 5.76762 16.2858 5.23067 17.3899 5.01556C18.4939 4.80044 19.6369 4.91677 20.6749 5.3499C21.713 5.78302 22.5997 6.51358 23.2234 7.4496C23.8471 8.38562 24.1799 9.48524 24.18 10.61C24.1694 12.1289 23.5573 13.5817 22.4776 14.65C21.398 15.7184 19.9389 16.3154 18.42 16.31ZM18.42 6.91003C17.6909 6.92185 16.9815 7.14886 16.381 7.56255C15.7805 7.97623 15.3156 8.55814 15.0447 9.23518C14.7738 9.91222 14.7091 10.6542 14.8585 11.3679C15.008 12.0817 15.3651 12.7353 15.8848 13.2468C16.4046 13.7583 17.0639 14.1047 17.7799 14.2427C18.496 14.3807 19.2368 14.304 19.9094 14.0223C20.582 13.7405 21.1564 13.2664 21.5603 12.6593C21.9643 12.0522 22.1799 11.3392 22.18 10.61C22.1694 9.62159 21.768 8.67753 21.0634 7.98421C20.3589 7.29088 19.4085 6.90468 18.42 6.91003V6.91003ZM21.91 17.65C17.4745 16.8782 12.9083 17.5807 8.90999 19.65C8.62616 19.8076 8.39114 20.0402 8.23064 20.3224C8.07015 20.6046 7.99035 20.9255 7.99999 21.25V24.81C7.99999 25.0752 8.10534 25.3296 8.29288 25.5171C8.48042 25.7047 8.73477 25.81 8.99999 25.81C9.2652 25.81 9.51956 25.7047 9.70709 25.5171C9.89463 25.3296 9.99999 25.0752 9.99999 24.81V21.38C13.7077 19.5197 17.924 18.9294 22 19.7L21.91 17.65Z M33 22H26.3V20.52C26.3 20.2548 26.1946 20.0004 26.0071 19.8129C25.8196 19.6254 25.5652 19.52 25.3 19.52C25.0348 19.52 24.7804 19.6254 24.5929 19.8129C24.4054 20.0004 24.3 20.2548 24.3 20.52V22H17C16.7348 22 16.4804 22.1054 16.2929 22.2929C16.1054 22.4804 16 22.7348 16 23V33C16 33.2652 16.1054 33.5196 16.2929 33.7071C16.4804 33.8947 16.7348 34 17 34H33C33.2652 34 33.5196 33.8947 33.7071 33.7071C33.8946 33.5196 34 33.2652 34 33V23C34 22.7348 33.8946 22.4804 33.7071 22.2929C33.5196 22.1054 33.2652 22 33 22ZM32 32H18V24H24.3V24.41C24.3 24.6752 24.4054 24.9296 24.5929 25.1171C24.7804 25.3047 25.0348 25.41 25.3 25.41C25.5652 25.41 25.8196 25.3047 26.0071 25.1171C26.1946 24.9296 26.3 24.6752 26.3 24.41V24H32V32Z M21.81 27.42H27.77V28.82H21.81V27.42ZM10.84 12.24C8.07144 12.2863 5.3509 12.9707 2.89 14.24C2.62236 14.3814 2.39818 14.5928 2.24141 14.8517C2.08465 15.1107 2.00121 15.4073 2 15.71V18.81C2 19.0752 2.10536 19.3296 2.29289 19.5171C2.48043 19.7046 2.73478 19.81 3 19.81C3.26522 19.81 3.51957 19.7046 3.70711 19.5171C3.89464 19.3296 4 19.0752 4 18.81V15.91C6.3513 14.7396 8.95456 14.1661 11.58 14.24C11.2365 13.6133 10.9872 12.9394 10.84 12.24V12.24ZM33.11 14.23C30.9029 13.0722 28.4772 12.3908 25.99 12.23C25.8435 12.9281 25.5977 13.6017 25.26 14.23C27.6031 14.2861 29.9047 14.8598 32 15.91V18.81C32 19.0752 32.1054 19.3296 32.2929 19.5171C32.4804 19.7046 32.7348 19.81 33 19.81C33.2652 19.81 33.5196 19.7046 33.7071 19.5171C33.8946 19.3296 34 19.0752 34 18.81V15.71C34.0006 15.4056 33.918 15.1069 33.7612 14.846C33.6043 14.5852 33.3791 14.3722 33.11 14.23V14.23ZM10.66 10.61V9.94C9.87662 9.83516 9.16385 9.43223 8.67007 8.81509C8.17629 8.19795 7.93955 7.41415 8.00914 6.62685C8.07872 5.83955 8.44926 5.10942 9.04362 4.58845C9.63798 4.06748 10.4104 3.79581 11.2 3.83C12.0213 3.82851 12.8107 4.14786 13.4 4.72C13.9199 4.29118 14.4909 3.92841 15.1 3.64C14.4196 2.85765 13.5181 2.29983 12.5144 2.04007C11.5106 1.7803 10.4517 1.83077 9.47724 2.18484C8.50275 2.5389 7.65842 3.17994 7.05553 4.02345C6.45264 4.86696 6.11949 5.87336 6.1 6.91C6.12099 8.17875 6.6142 9.39412 7.4833 10.3187C8.3524 11.2433 9.53497 11.8106 10.8 11.91C10.7152 11.4816 10.6683 11.0466 10.66 10.61V10.61ZM24.77 1.83C24.0828 1.83015 23.4025 1.9673 22.7689 2.23344C22.1353 2.49958 21.5612 2.88936 21.08 3.38C21.756 3.62497 22.3953 3.96143 22.98 4.38C23.4468 4.05593 23.9929 3.86463 24.5599 3.8265C25.1269 3.78838 25.6937 3.90486 26.1997 4.16352C26.7057 4.42218 27.1321 4.81332 27.4333 5.29524C27.7345 5.77716 27.8992 6.33179 27.91 6.9C27.9037 7.48363 27.7322 8.05355 27.4155 8.54377C27.0987 9.034 26.6495 9.42447 26.12 9.67C26.1602 9.97836 26.1803 10.289 26.18 10.6C26.1773 11.002 26.1439 11.4031 26.08 11.8C27.1694 11.5199 28.1356 10.8876 28.8283 10.0014C29.521 9.11514 29.9013 8.02478 29.91 6.9C29.8968 5.5473 29.3488 4.2548 28.3857 3.30483C27.4226 2.35487 26.1228 1.82465 24.77 1.83V1.83Z",
    "M 22.65 34 H 25.65 V 22 H 22.65 Z M 24 18.3 Q 24.7 18.3 25.175 17.85 Q 25.65 17.4 25.65 16.7 Q 25.65 16 25.175 15.5 Q 24.7 15 24 15 Q 23.3 15 22.825 15.5 Q 22.35 16 22.35 16.7 Q 22.35 17.4 22.825 17.85 Q 23.3 18.3 24 18.3 Z M 24 44 Q 19.75 44 16.1 42.475 Q 12.45 40.95 9.75 38.25 Q 7.05 35.55 5.525 31.9 Q 4 28.25 4 24 Q 4 19.8 5.525 16.15 Q 7.05 12.5 9.75 9.8 Q 12.45 7.1 16.1 5.55 Q 19.75 4 24 4 Q 28.2 4 31.85 5.55 Q 35.5 7.1 38.2 9.8 Q 40.9 12.5 42.45 16.15 Q 44 19.8 44 24 Q 44 28.25 42.45 31.9 Q 40.9 35.55 38.2 38.25 Q 35.5 40.95 31.85 42.475 Q 28.2 44 24 44 Z M 24 24 Q 24 24 24 24 Q 24 24 24 24 Q 24 24 24 24 Q 24 24 24 24 Q 24 24 24 24 Q 24 24 24 24 Q 24 24 24 24 Q 24 24 24 24 Z M 24 41 Q 31 41 36 36 Q 41 31 41 24 Q 41 17 36 12 Q 31 7 24 7 Q 17 7 12 12 Q 7 17 7 24 Q 7 31 12 36 Q 17 41 24 41 Z",
    "M24 29.25 18.75 24 24 18.75 29.25 24ZM19.75 14.55 15.6 10.4 24 2 32.4 10.4 28.25 14.55 24 10.3ZM10.4 32.4 2 24 10.4 15.6 14.55 19.75 10.3 24 14.55 28.25ZM37.6 32.4 33.45 28.25 37.7 24 33.45 19.75 37.6 15.6 46 24ZM24 46 15.6 37.6 19.75 33.45 24 37.7 28.25 33.45 32.4 37.6Z"
  };
  private String logoutIcon =
      "M60 16L54.36 21.64L64.68 32H24V40H64.68L54.36 50.32L60 56L80 36L60 16ZM8 8H40V0H8C3.6 0 0 3.6 0 8V64C0 68.4 3.6 72 8 72H40V64H8V8Z";

  private String menuIcon = "M21 18H3V16H21V18ZM21 13H3V11H21V13ZM21 8H3V6H21V8Z";
  // FXMLS
  @FXML private Label pageLabel;
  @FXML private BorderPane menuBar;

  // String that holds the pageLabel's text
  private SimpleStringProperty currentPage;

  // CSS line to set text fill
  private String textCSSLine = "-fx-text-fill: %s;";
  private String svgCSSLine = "-fx-background-color: %s";

  // Colors representing the grey and blue values used in the fxmls.
  private String grey = "#C4C4C4";
  private String blue = "#FFFFFF";
  private String yellow = "#F6BD38";

  // Store the ClassLoader for future use
  ClassLoader rscLoader;

  // The currently selected menu item
  private int selectedItem = 0;

  // Whether or not menu is currently enabled
  boolean menuEnabled = true;

  // Animations
  TranslateTransition menuSlide;

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
  private String toProfileURL = "edu/wpi/cs3733/D22/teamZ/views/ProfilePage.fxml";
  private String toCreditsPageURL = "edu/wpi/cs3733/D22/teamZ/views/CreditsPage.fxml";
  private String toEmployeePageURL = "edu/wpi/cs3733/D22/teamZ/views/Employee.fxml";

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
    contentPane.setTranslateX(200);

    // Initialize exit menu
    SVGPath MenuIcon = new SVGPath();
    MenuIcon.setContent(menuIcon);
    menuRegionButton.setShape(MenuIcon);
    menuRegionButton.setStyle(String.format(svgCSSLine, yellow));

    menuRegionButton.setOnMouseClicked(
        (e) -> {
          toggleMenu();
        });

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

    // Animations
    menuSlide = new TranslateTransition();
    menuSlide.setNode(menuPane);
    menuSlide.setInterpolator(Interpolator.EASE_BOTH);

    // Auto-hide menu
    contentPane.addEventFilter(
        MouseEvent.MOUSE_PRESSED,
        event -> {
          if (menuEnabled) {
            toggleMenu();
          }
        });
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

  public void slideBar(int item) {
    selectedItem = item;
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
    selectMenu(5);
    load(toAboutPageURL);
  }

  @FXML
  private void toCredits() throws IOException {
    selectMenu(6);
    load(toCreditsPageURL);
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
  private void toEmployeePage() throws IOException {
    selectMenu(4);
    load(toEmployeePageURL);
  }

  @FXML
  private void toMedicalEquipmentRequest() throws IOException {
    selectMenu(3);
    load(toServiceURL);
  }

  @FXML
  private void toProfile() throws IOException {
    load(toProfileURL);
  }

  @FXML
  private void toExit() {
    Stage stage = (Stage) exitButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  private void toggleMenu() {
    if (menuSlide.getCurrentTime().equals(Duration.millis(0))
        || menuSlide.getCurrentTime().equals(menuSlide.getTotalDuration())) {
      if (menuEnabled) {
        menuSlide.setFromX(0);
        menuSlide.setToX(-menuPane.getWidth());
        TranslateTransition tt = new TranslateTransition(Duration.millis(400), contentPane);
        tt.setToX(0);
        tt.play();
      } else {
        menuSlide.setFromX(-menuPane.getWidth());
        menuSlide.setToX(0);
        TranslateTransition tt = new TranslateTransition(Duration.millis(400), contentPane);
        tt.setToX(200);
        tt.play();
        // contentPane.setTranslateX(200);
      }

      System.out.println(menuSlide.getTotalDuration());

      menuSlide.playFromStart();
      menuEnabled = !menuEnabled;
    }
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
