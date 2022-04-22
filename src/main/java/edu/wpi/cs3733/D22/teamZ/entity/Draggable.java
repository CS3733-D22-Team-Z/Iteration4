package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.controllers.LocationListController;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Window;

public class Draggable {

  public MapLabel activeLocation;
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
  private Label locationName;

  public Draggable(
      ScrollPane scrollpane,
      MapLabel active,
      double scaleFactor,
      LocationListController mapRef,
      Label locationName) {
    facadeDAO = FacadeDAO.getInstance();
    this.scrollPane = scrollpane;
    this.location = active.getLocation();
    this.scaleFactor = scaleFactor;
    this.mapRef = mapRef;
    this.medicalEquipment = active.getEquip();
    this.locationName = locationName;
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
          if (mapRef.getEquipRadio().isSelected()) {
            for (MapLabel label :
                mapRef.getAllLabels().stream()
                    .filter(ml -> ml.isOnFloor(location.getFloor()))
                    .collect(Collectors.toList())) {
              if (mouseEvent.getPickResult().getIntersectedNode().equals(label.getBound())
                  || mouseEvent.getPickResult().getIntersectedNode().equals(label)) {
                label.getBound().setStroke(new Color(0, .459, 1, 1));
                locationName.setText(label.getLocation().getShortName());
              } else {
                label.getBound().setStroke(Color.TRANSPARENT);
              }
            }
          }
        });
    node.setOnMouseReleased(
        mouseEvent -> {
          if (mapRef.getLocRadio().isSelected()) { // updates location X and Y coord
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
            if (location.getXcoord() < 0) {
              location.setXcoord(0);
            }
            if (location.getXcoord() > 1021) {
              location.setXcoord(1021);
            }
            if (location.getYcoord() < 0) {
              location.setYcoord(0);
            }
            if (location.getYcoord() > 840) {
              location.setYcoord(840);
            }
            facadeDAO.updateLocation(location);
          } else {
            System.out.println("intersected: " + mouseEvent.getPickResult().getIntersectedNode());
            for (MapLabel label :
                mapRef.getAllLabels().stream()
                    .filter(ml -> ml.isOnFloor(location.getFloor()))
                    .collect(Collectors.toList())) {
              if (mouseEvent.getPickResult().getIntersectedNode().equals(label.getBound())
                  || mouseEvent.getPickResult().getIntersectedNode().equals(label)) {
                System.out.println("found: " + label.getLocation().getLongName());
                for (MedicalEquipment meds : medicalEquipment) {
                  meds.setCurrentLocation(label.getLocation());
                  facadeDAO.updateMedicalEquipment(meds);
                  /*try {
                    EquipmentWindow();
                  } catch (IOException e) {
                    e.printStackTrace();
                  }*/
                }
              }
            }
          }

          prevTransX = node.getTranslateX();
          prevTransY = node.getTranslateY();
          scrollPane.setPannable(true);
          locationName.setText(null);
          mapRef.refreshMap(location.getFloor());
        });
  }

  /*private void EquipmentWindow() throws IOException {
    Stage stage = new Stage();

    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(
        getClass()
            .getClassLoader()
            .getResource("edu/wpi/cs3733/D22/teamZ/views/MedicalMovePopup.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root, 400, 300);
    scene.getStylesheets().add("edu/wpi/cs3733/D22/teamZ/styles/MenuDefault.css");
    stage.setScene(scene);
    stage.show();
  }*/
}
