package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import javafx.scene.Node;

public class Draggable {

  private double mouseAnchorX;
  private double mouseAnchorY;
  private Location location;
  private FacadeDAO facadeDAO;

  public Draggable() {
    facadeDAO = FacadeDAO.getInstance();
  }

  public Draggable(Location location) {
    facadeDAO = FacadeDAO.getInstance();
    this.location = location;
  }

  public void makeDraggable(Node node) {
    node.setOnMousePressed(
        mouseEvent -> {
          mouseAnchorX = mouseEvent.getSceneX() - node.getTranslateX();
          mouseAnchorY = mouseEvent.getSceneY() - node.getTranslateY();
        });

    node.setOnMouseDragged(
        mouseEvent -> {
          node.setTranslateX(mouseEvent.getSceneX() - mouseAnchorX);
          node.setTranslateY(mouseEvent.getSceneY() - mouseAnchorY);

          if (!(location == null)) {
            location.setXcoord((int) mouseEvent.getSceneX());
            location.setYcoord((int) mouseEvent.getSceneY());
            facadeDAO.updateLocation(location);
          }
        });
  }
}
