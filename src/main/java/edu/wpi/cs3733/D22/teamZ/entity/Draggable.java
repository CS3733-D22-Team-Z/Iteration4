package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.controllers.LocationListController;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.stage.Window;

public class Draggable {

  private double mouseAnchorX;
  private double mouseAnchorY;
  private double prevTransX = 0;
  private double prevTransY = 0;
  private Location location;
  private FacadeDAO facadeDAO;
  private ScrollPane scrollPane;
  private List<MedicalEquipment> medicalEquipment;
  private double scaleFactor;
  private ImageView map;
  private LocationListController mapRef;

  public Draggable(
      ScrollPane scrollpane, Location location, double scaleFactor, LocationListController mapRef) {
    facadeDAO = FacadeDAO.getInstance();
    this.scrollPane = scrollpane;
    this.location = location;
    this.scaleFactor = scaleFactor;
    this.mapRef = mapRef;
  }

  public Draggable(
      ScrollPane scrollpane,
      List<MedicalEquipment> medicalEquipment,
      double scaleFactor,
      LocationListController mapRef) {
    facadeDAO = FacadeDAO.getInstance();
    this.scrollPane = scrollpane;
    this.medicalEquipment = medicalEquipment;
    this.scaleFactor = scaleFactor;
    this.mapRef = mapRef;
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
          Node home = (Node) mouseEvent.getSource();
          Window view = home.getScene().getWindow();
          // System.out.println(view.getWidth());
          node.setTranslateX(
              (mouseEvent.getSceneX() - mouseAnchorX + prevTransX)
                  / (view.getWidth() / 775)
                  / scaleFactor);
          node.setTranslateY(
              (mouseEvent.getSceneY() - mouseAnchorY + prevTransY)
                  / (view.getHeight() / 450)
                  / scaleFactor);
        });
    node.setOnMouseReleased(
        mouseEvent -> {
          if (!(location == null)) { // updates location X and Y coord
            location.setXcoord(
                (int)
                    ((location.getXcoord() * (mapRef.getMap().getFitWidth() / 1021)
                            + node.getTranslateX())
                        / (mapRef.getMap().getFitWidth() / 1021)));
            location.setYcoord(
                (int)
                    ((location.getYcoord() * (mapRef.getMap().getFitHeight() / 850)
                            + node.getTranslateY())
                        / (mapRef.getMap().getFitHeight() / 850)));
            facadeDAO.updateLocation(location);
          }
          if (!(medicalEquipment == null)) {
            System.out.println("medequip"); // I imagine this is where you would snap to location
          }
          prevTransX = node.getTranslateX();
          prevTransY = node.getTranslateY();
          scrollPane.setPannable(true);
          mapRef.refreshMap(location.getFloor());
        });
  }
}
