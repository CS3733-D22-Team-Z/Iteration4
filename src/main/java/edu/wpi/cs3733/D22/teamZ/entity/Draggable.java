package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.stage.Window;

public class Draggable {

  private double mouseAnchorX;
  private double mouseAnchorY;
  private Location location;
  private FacadeDAO facadeDAO;
  private ScrollPane scrollPane;
  private List<MedicalEquipment> medicalEquipment;
  private ImageView map;

  public Draggable(ScrollPane scrollpane, Location location) {
    facadeDAO = FacadeDAO.getInstance();
    this.scrollPane = scrollpane;
    this.location = location;
  }

  public Draggable(ScrollPane scrollpane, List<MedicalEquipment> medicalEquipment) {
    facadeDAO = FacadeDAO.getInstance();
    this.scrollPane = scrollpane;
    this.medicalEquipment = medicalEquipment;
  }

  /**
   * Makes something draggable
   *
   * @param node
   * @return void
   */
  public void makeDraggable(Node node) {
    node.setOnMousePressed(
        mouseEvent -> {
          mouseAnchorX = mouseEvent.getSceneX();
          mouseAnchorY = mouseEvent.getSceneY();
          scrollPane.setPannable(false);
        });

    node.setOnMouseDragged(
        mouseEvent -> {
          // node.startDragAndDrop(TransferMode.ANY);
          Node home = (Node) mouseEvent.getSource();
          Window view = home.getScene().getWindow();
          System.out.println(view.getWidth());
          node.setTranslateX((mouseEvent.getSceneX() - mouseAnchorX) / (view.getWidth() / 1200));
          node.setTranslateY((mouseEvent.getSceneY() - mouseAnchorY) / (view.getHeight() / 800));
        });
    node.setOnMouseReleased(
        mouseEvent -> {
          if (!(location == null)) { // updates location X and Y coord
            location.setXcoord((int) mouseEvent.getSceneX());
            location.setYcoord((int) mouseEvent.getSceneY());
            facadeDAO.updateLocation(location);
          }
          if (!(medicalEquipment == null)) {
            System.out.println("medequipt"); // I imagine this is where you would snap to location
          }
          scrollPane.setPannable(true);
        });
  }
}
