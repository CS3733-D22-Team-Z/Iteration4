package edu.wpi.cs3733.D22.teamZ.helpers;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

/**
 * A helper class that loads FXML files into other components at designated locations. Should only
 * be loading popups into main views.
 */
public class PopupLoader {
  private static String pathToPopups = "edu/wpi/cs3733/D22/teamZ/views/popups/%s.fxml";

  /**
   * Loads the given popup into the parent at the designated layout coordinates.
   *
   * @param popup the popup fxml to be loaded.
   * @param parent the parent that will contain the popup
   * @param x the layout x coordinate of the popup
   * @param y the layout y coordinate of the popup
   * @return the popup window
   */
  public static Pane loadPopup(String popup, Pane parent, int x, int y) throws IOException {
    // Load the popup
    FXMLLoader loader = new FXMLLoader();
    String path = String.format(pathToPopups, popup);
    loader.setLocation(parent.getClass().getClassLoader().getResource(path));
    Pane popupWindow = loader.load();

    // Place the window in the scene
    parent.getChildren().add(popupWindow);
    popupWindow.setLayoutX(x - popupWindow.getWidth() / 2);
    popupWindow.setLayoutY(y - popupWindow.getHeight() / 2);

    return popupWindow;
  }
}
