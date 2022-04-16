package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

public class Draggable {

  private double mouseAnchorX;
  private double mouseAnchorY;
  private Location location;
  private FacadeDAO facadeDAO;
  private ScrollPane scrollPane;

  public Draggable(ScrollPane scrollpane) {

    facadeDAO = FacadeDAO.getInstance();
    this.scrollPane = scrollpane;
  }

  public Draggable(ScrollPane scrollpane, Location location) {
    facadeDAO = FacadeDAO.getInstance();
    this.scrollPane = scrollpane;
    this.location = location;
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
          scrollPane.setPannable(true);
        });
  }
}
