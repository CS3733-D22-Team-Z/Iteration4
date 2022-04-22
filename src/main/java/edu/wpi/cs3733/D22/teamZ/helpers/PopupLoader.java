package edu.wpi.cs3733.D22.teamZ.helpers;

import java.io.IOException;
import java.util.List;
import java.util.List;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
   * @return the popup window and controller. 0: Pane, 1: Controller
   */
  public static List<Object> loadPopup(String popup, Pane parent) throws IOException {
    // Load the popup
    FXMLLoader loader = new FXMLLoader();
    String path = String.format(pathToPopups, popup);
    loader.setLocation(parent.getClass().getClassLoader().getResource(path));
    Node popupWindow = loader.load();

    // Place the window in the scene
    parent.getChildren().add(popupWindow);
    /*popupWindow.setLayoutX(x - popupWindow.getPrefWidth() / 2);
    popupWindow.setLayoutY(y - popupWindow.getPrefHeight() / 2);*/

    return List.of(popupWindow, loader.getController());
  }

  public static void delay(long millis, Runnable continuation) {
    Task<Void> sleeper =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            try {
              Thread.sleep(millis);
            } catch (InterruptedException e) {
            }
            return null;
          }
        };
    sleeper.setOnSucceeded(event -> continuation.run());
    new Thread(sleeper).start();
  }
}
