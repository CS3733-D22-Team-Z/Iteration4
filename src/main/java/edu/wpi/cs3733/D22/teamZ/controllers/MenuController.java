package edu.wpi.cs3733.D22.teamZ.controllers;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/** The controller for the Menu FXML. */
public class MenuController implements Initializable {
  @FXML Pane contentPane;
  @FXML Button exitButton;
  @FXML VBox menuContainer;

  // Colors representing the grey and blue values used in the fxmls.
  private String grey = "#C4C4C4";
  private String blue = "#0075FF";

  // Store the ClassLoader for future use
  ClassLoader rscLoader;

  // The currently selected menu item
  private int selectedItem = 0;

  // Paths to other pages
  private String toLocationsURL = "edu/wpi/cs3733/D22/teamZ/views/Location.fxml";
  private String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";
  private String toMedicalEquipmentRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentRequestList.fxml";
  private String toHomeURL = "edu/wpi/cs3733/D22/teamZ/views/Homepage.fxml";

  public MenuController() {
    rscLoader = getClass().getClassLoader();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      load(toHomeURL);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Grey out all other menu buttons
    for (int i = 1; i < menuContainer.getChildren().size(); i++) {
      JFXButton button = (JFXButton) menuContainer.getChildren().get(i);
      ImageView graphic = (ImageView) button.getGraphic();
      graphic.setImage(recolorImage(graphic.getImage(), Color.valueOf(grey)));
    }
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
    return cont;
  }

  public void setScale(float scaleX, float scaleY) {
    contentPane.setScaleX(scaleX);
    contentPane.setScaleY(scaleY);
  }

  /**
   * Highlights the selected menu item
   *
   * @param item the menu button to be selected
   */
  public void selectMenu(int item) {
    // De-select current item
    JFXButton oldMenuItem = (JFXButton) menuContainer.getChildren().get(selectedItem);
    oldMenuItem.setTextFill(Color.valueOf(grey));
    ImageView oldGraphic = (ImageView) oldMenuItem.getGraphic();
    oldGraphic.setImage(recolorImage(oldGraphic.getImage(), Color.valueOf(grey)));

    // Select current item
    selectedItem = item;

    // Repeat
    JFXButton newMenuItem = (JFXButton) menuContainer.getChildren().get(selectedItem);
    newMenuItem.setTextFill(Color.valueOf(blue));
    ImageView newGraphic = (ImageView) newMenuItem.getGraphic();
    newGraphic.setImage(recolorImage(newGraphic.getImage(), Color.valueOf(blue)));
  }

  /**
   * Re-color a given image with a new color.
   *
   * @param source the image to be colored
   * @param color the color to color the image with
   * @return the image colored in the specified color
   */
  private Image recolorImage(Image source, Color color) {
    // Stackoverflow code
    int w = (int) source.getWidth();
    int h = (int) source.getHeight();

    final WritableImage outputImage = new WritableImage(w, h);
    final PixelWriter writer = outputImage.getPixelWriter();
    final PixelReader reader = source.getPixelReader();
    for (int y = 0; y < h; y++) {
      for (int x = 0; x < w; x++) {
        // Keeping the opacity of every pixel as it is.
        writer.setColor(
            x,
            y,
            new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                reader.getColor(x, y).getOpacity()));
      }
    }

    return outputImage;
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
}
