package edu.wpi.cs3733.D22.teamZ.controllers.subControllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MapLabel;
import edu.wpi.cs3733.D22.teamZ.helpers.BiPolygon;
import edu.wpi.cs3733.D22.teamZ.helpers.LocationMethod;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import org.kynosarges.tektosyne.geometry.PointD;
import org.kynosarges.tektosyne.geometry.RectD;
import org.kynosarges.tektosyne.geometry.Voronoi;
import org.kynosarges.tektosyne.geometry.VoronoiResults;

/**
 * Controller for the Map popup, which implements an interactive map window with location support.
 */
public class MapController implements Initializable {
  // FXML elements
  @FXML private ImageView mapImage;
  @FXML private Pane iconContainer;
  @FXML private ScrollPane scrollPane;
  @FXML private StackPane mapContainer;

  // DB
  FacadeDAO database;

  // Paths
  private final String mapPath = "edu/wpi/cs3733/D22/teamZ/images/%s.png";

  // Specific attributes
  private ClassLoader loader;
  private final ObservableList<MapLabel> currentLabels =
      FXCollections.observableList(new ArrayList<>());
  private boolean draggable;
  private LocationMethod dragExitMethod;
  private VoronoiResults snapLocations = null;
  @Getter @Setter private int iconShift = 0;
  private BiPolygon prevBounds;

  // Frequent variables
  private MapLabel activeLabel;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    loader = getClass().getClassLoader();
    database = FacadeDAO.getInstance();

    addMouseBehaviors();
  }

  /** Implements mouse-related behavior in iconContainer */
  private void addMouseBehaviors() {
    // Mouse pressed
    iconContainer.addEventFilter(
        MouseEvent.MOUSE_PRESSED,
        pressEvent -> {
          Node clickedNode = pressEvent.getPickResult().getIntersectedNode();
          if (clickedNode instanceof MapLabel) {
            MapLabel label = (MapLabel) clickedNode;

            // Set active label
            activeLabel = label;

            // Enable dragging & disable panning
            activeLabel.setDragging(true, pressEvent);
            scrollPane.setPannable(false);

          } else {
            activeLabel = null;
          }
        });

    // Mouse dragged
    iconContainer.addEventFilter(
        MouseEvent.MOUSE_DRAGGED,
        dragEvent -> {
          if (draggable) {

            // If a label is currently selected and is being dragged...
            if (activeLabel != null && activeLabel.isDragging()) {
              // Move it
              activeLabel.setTranslateX(dragEvent.getX() - activeLabel.getMouseHomeX() + iconShift);
              activeLabel.setTranslateY(dragEvent.getY() - activeLabel.getMouseHomeY() + iconShift);

              // If voroni regions are present
              if (snapLocations != null) {
                Node clickedNode = dragEvent.getPickResult().getIntersectedNode();
                if (clickedNode instanceof BiPolygon) {
                  BiPolygon bounds = (BiPolygon) clickedNode;

                  // Remove old stroke
                  if (prevBounds != bounds && prevBounds != null) {
                    prevBounds.setStroke(Color.TRANSPARENT);
                  }

                  bounds.setStroke(new Color(0, .459, 1, 1));
                  prevBounds = bounds;
                }
              }
            }
          }
        });

    // Mouse released
    iconContainer.addEventFilter(
        MouseEvent.MOUSE_RELEASED,
        releaseEvent -> {
          // If a label was pressed, release it from dragging.
          if (activeLabel != null) {
            activeLabel.setDragging(false, null);

            // If there aren't any voroni regions
            if (snapLocations == null) {
              // Update layout of label
              activeLabel.setLayoutX(
                  activeLabel.getLayoutX() + activeLabel.getTranslateX() - iconShift);
              activeLabel.setLayoutY(
                  activeLabel.getLayoutY() + activeLabel.getTranslateY() - iconShift);
            } else {
              if (prevBounds != null) {
                activeLabel.setLayoutX(prevBounds.getParentLocation().getXcoord());
                activeLabel.setLayoutY(prevBounds.getParentLocation().getYcoord());
              }
            }

            // Reset translation
            activeLabel.setTranslateX(0);
            activeLabel.setTranslateY(0);

            // Run dragExit
            if (draggable) dragExitMethod.call(activeLabel.getLocation());

            // Re-enable panning
            scrollPane.setPannable(true);
          }
        });

    // Mouse clicked
    iconContainer.addEventFilter(
        MouseEvent.MOUSE_CLICKED,
        clickEvent -> {
          Node clickedNode = clickEvent.getPickResult().getIntersectedNode();
          if (clickedNode instanceof MapLabel) {
            // Give activeLabel focus
            activeLabel.requestFocus();
          }
        });
  }

  /**
   * Sets scaling of the map
   *
   * @param scale new scale of map
   */
  public void setScale(double scale) {
    mapContainer.setScaleX(scale);
    mapContainer.setScaleY(scale);
  }

  /**
   * Sets floor image of the map
   *
   * @param floor the floor the map should be set to
   */
  public void setFloor(String floor) {
    // Switch image
    Image newImage = new Image(String.format(mapPath, floor));
    mapImage.setImage(newImage);
  }

  /**
   * Given a list of locations, adds labels to the map.
   *
   * @param locations the locations to be added
   * @param rightClickEvent method to be run when the location is right-clicked.
   * @param voroniLocations the locations that serve to generate the voroni regions
   */
  public void setLabels(
      List<Location> locations,
      javafx.event.EventHandler<? super javafx.scene.input.ContextMenuEvent> rightClickEvent,
      List<Location> voroniLocations) {
    currentLabels.clear();

    for (Location loc : locations) {
      MapLabel label =
          new MapLabel.mapLabelBuilder()
              .location(loc)
              .equipment(loc.getEquipmentList())
              .requests(database.getServiceRequestsByLocation(loc)) // Replace?
              .build();

      // stylize label icon
      label.getStyleClass().add("map-label");
      label.setScaleX(.7);
      label.setScaleY(.7);

      // Grow/shrink behavior
      label
          .focusedProperty()
          .addListener(
              (observable, oldValue, newValue) -> {
                if (!newValue) {
                  label.setScaleX(.7);
                  label.setScaleY(.7);
                  // returnOnClick();
                } else {
                  label.setScaleX(1.1);
                  label.setScaleY(1.1);
                }
              });

      // place label at correct coords
      label.relocate(
          label.getLocation().getXcoord(), // * (map.getFitWidth() / 1021),
          label.getLocation().getYcoord()); // * (map.getFitHeight() / 850));

      if (voroniLocations != null) {
        generateVoronoi(voroniLocations);
        // Iterate through all original voroni locations
        for (int i = 0; i < snapLocations.generatorSites.length; i++) {
          PointD point = snapLocations.generatorSites[i];

          // If the current point is this label...
          if (point.x == label.getLayoutX() && point.y == label.getLayoutY()) {

            // Create bound for label
            label.setBound(pointDtoPoly(snapLocations.voronoiRegions()[i], label.getLocation()));
            iconContainer.getChildren().add(0, label.getBound());

            break;
          }
        }
      }

      // label.getBound().setOnMouseClicked(evt -> label.requestFocus());
      if (rightClickEvent != null) label.setOnContextMenuRequested(rightClickEvent);
      //            label
      //                    .getBound()
      //                    .setOnContextMenuRequested(
      //                            event -> rightClickMenu.show(label, event.getScreenX(),
      // event.getScreenY()));
      currentLabels.add(label);

      // Add graphic
      Image locationImg = new Image("edu/wpi/cs3733/D22/teamZ/images/location.png");
      ImageView locationIcon = new ImageView(locationImg);
      label.setGraphic(locationIcon);
      iconContainer.getChildren().add(label);
    }
  }

  /**
   * Enables dragging of locations on the map, and sets the drag end function
   *
   * @param method the method to be called after the drag ends.
   */
  public void setDraggable(LocationMethod method) {
    draggable = true;
    this.dragExitMethod = method;
  }

  /**
   * Generates the voronoi points? from a given list of locations.
   *
   * @param voroniLocations the locations to be used as input for the voronoi algorithm
   */
  private void generateVoronoi(List<Location> voroniLocations) {
    Set<PointD> pointDList =
        voroniLocations.stream()
            .map(l -> new PointD(l.getXcoord(), l.getYcoord()))
            .collect(Collectors.toSet());

    PointD[] points = new PointD[pointDList.size()];
    pointDList.toArray(points);

    snapLocations =
        Voronoi.findAll(
            points,
            new RectD(
                new PointD(0, 0), new PointD(mapImage.getFitWidth(), mapImage.getFitHeight())));
  }

  /**
   * Creates a BiPolygon from a list of points.
   *
   * @param points the list of points that make up the polygon
   * @return a BiPolygon node
   */
  private BiPolygon pointDtoPoly(PointD[] points, Location parentLocation) {
    BiPolygon ret = new BiPolygon(parentLocation);

    ret.setFill(Color.DARKRED);
    for (PointD point : points) {
      ret.getPoints().addAll(point.x, point.y);
    }

    return ret;
  }

  /**
   * Retrieves the active label
   *
   * @return the active label
   */
  public MapLabel getActiveLabel() {
    return activeLabel;
  }
}
