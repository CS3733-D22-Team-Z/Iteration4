package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

public class Draggable {

  private double mouseAnchorX;
  private double mouseAnchorY;
  private Location location;
  private FacadeDAO facadeDAO;
  private ScrollPane scrollPane;
  private List<MedicalEquipment> medicalEquipment;

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
          mouseAnchorX = mouseEvent.getSceneX() - node.getTranslateX();
          mouseAnchorY = mouseEvent.getSceneY() - node.getTranslateY();
          scrollPane.setPannable(false);
          System.out.println(scrollPane.getScaleX());
        });

    node.setOnMouseDragged(
        mouseEvent -> {
          // node.startDragAndDrop(TransferMode.ANY);
          node.setTranslateX(mouseEvent.getSceneX() - mouseAnchorX);
          node.setTranslateY(mouseEvent.getSceneY() - mouseAnchorY);
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
