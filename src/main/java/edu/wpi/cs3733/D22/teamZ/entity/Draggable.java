package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;

public class Draggable {

  private double mouseAnchorX;
  private double mouseAnchorY;
  private Location location;
  private FacadeDAO facadeDAO;
  private ScrollPane scrollPane;
  private List<MedicalEquipment> medicalEquipment;
  private double scaleFactor;
  private ImageView map;

  public Draggable(ScrollPane scrollpane, Location location, double scaleFactor) {
    facadeDAO = FacadeDAO.getInstance();
    this.scrollPane = scrollpane;
    this.location = location;
    this.scaleFactor = scaleFactor;
    System.out.println(scaleFactor);
  }

  public Draggable(
      ScrollPane scrollpane, List<MedicalEquipment> medicalEquipment, double scaleFactor) {
    facadeDAO = FacadeDAO.getInstance();
    this.scrollPane = scrollpane;
    this.medicalEquipment = medicalEquipment;
    this.scaleFactor = scaleFactor;
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
          mouseAnchorX = mouseEvent.getSceneX() - node.getTranslateX();
          mouseAnchorY = mouseEvent.getSceneY() - node.getTranslateY();
          scrollPane.setPannable(false);
          System.out.println(map.getLayoutX());
        });

    node.setOnMouseDragged(
        mouseEvent -> {
          // node.startDragAndDrop(TransferMode.ANY);
          node.setTranslateX((mouseEvent.getSceneX() - mouseAnchorX) / scaleFactor);
          node.setTranslateY((mouseEvent.getSceneY() - mouseAnchorY) / scaleFactor);
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
