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
  private double scaleFactor;
  private ImageView map;

  public Draggable(ScrollPane scrollpane, Location location, double scaleFactor) {
    facadeDAO = FacadeDAO.getInstance();
    this.scrollPane = scrollpane;
    this.location = location;
    this.scaleFactor = scaleFactor;
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
          Node home = (Node) mouseEvent.getSource();
          Window view = home.getScene().getWindow();
          mouseAnchorX = mouseEvent.getSceneX() - node.getTranslateX();
          mouseAnchorY = mouseEvent.getSceneY() - node.getTranslateY();
          scrollPane.setPannable(false);
        });

    node.setOnMouseDragged(
        mouseEvent -> {
          // node.startDragAndDrop(TransferMode.ANY);
          Node home = (Node) mouseEvent.getSource();
          Window view = home.getScene().getWindow();
          // System.out.println(view.getWidth());
          node.setTranslateX(
              (mouseEvent.getSceneX() - mouseAnchorX) / (view.getWidth() / 775) / scaleFactor);
          node.setTranslateY(
              (mouseEvent.getSceneY() - mouseAnchorY) / (view.getHeight() / 450) / scaleFactor);
        });
    node.setOnMouseReleased(
        mouseEvent -> {
          Node home = (Node) mouseEvent.getSource();
          Window view = home.getScene().getWindow();
          if (!(location == null)) { // updates location X and Y coord
            location.setXcoord((int) (mouseAnchorX + node.getTranslateX()));
            location.setYcoord((int) (mouseAnchorY + node.getTranslateY()));
            facadeDAO.updateLocation(location);
          }
          if (!(medicalEquipment == null)) {
            System.out.println("medequip"); // I imagine this is where you would snap to location
          }
          scrollPane.setPannable(true);
        });
  }
}
