package edu.wpi.cs3733.D22.teamZ.controllers.subControllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MapLabel;
import edu.wpi.cs3733.D22.teamZ.helpers.BiPolygon;
import edu.wpi.cs3733.D22.teamZ.helpers.LabelMethod;
import edu.wpi.cs3733.D22.teamZ.helpers.MouseMethod;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
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
  public static final String mapPath = "edu/wpi/cs3733/D22/teamZ/images/%s.png";

  // Specific attributes
  private ClassLoader loader;
  private final ObservableList<MapLabel> currentLabels =
      FXCollections.observableList(new ArrayList<>());
  private final ObservableList<MapLabel> allLabels =
      FXCollections.observableList(new ArrayList<>());
  private boolean draggable;
  private LabelMethod dragExitMethod;
  @Setter private LabelMethod labelClickedMethod;
  @Setter private MouseMethod doubleClicked;
  @Setter private LabelMethod rightClickedMethod;
  private VoronoiResults snapLocations = null;
  @Getter @Setter private int iconShift = 0;
  private BiPolygon prevBounds;
  private double minX;
  private double minY;
  private double maxX;
  private double maxY;
  private double scale;

  // List of zooms supported by the map.
  // Integer is the zoom * 100. For example, 50 corresponds to 0.5.
  // Double is the maximum HBar value supported.
  // Must have mapping for 100.
  @Setter private Map<Integer, Double> zooms;
  int currentScale = 100;

  // Frequent variables
  private MapLabel activeLabel;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    loader = getClass().getClassLoader();
    database = FacadeDAO.getInstance();

    addMouseBehaviors();

    //    scrollPane
    //        .hvalueProperty()
    //        .addListener(
    //            (listener) -> {
    //              System.out.println(scrollPane.getHvalue());
    //            });

    // Default
    zooms = Map.of(100, 1.0);
    setScale(100);
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

            // If the mouse is being primary clicked...
            if (pressEvent.getButton().equals(MouseButton.PRIMARY)) {
              // Enable dragging & disable panning
              activeLabel.setDragging(true, pressEvent);
              scrollPane.setPannable(false);
            } else if (pressEvent.getButton().equals(MouseButton.SECONDARY)) {
              rightClickedMethod.call(activeLabel);
            }
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

            // If there aren't any voroni regions
            if (snapLocations == null) {
              // Update layout of label
              activeLabel.setLayoutX(
                  activeLabel.getLayoutX() + activeLabel.getTranslateX() - iconShift);
              activeLabel.setLayoutY(
                  activeLabel.getLayoutY() + activeLabel.getTranslateY() - iconShift);
            } else {
              if (prevBounds != null && activeLabel.isDragging()) {
                activeLabel.setLayoutX(prevBounds.getParentLocation().getXcoord());
                activeLabel.setLayoutY(prevBounds.getParentLocation().getYcoord());
              }
            }

            prevBounds = null;

            // Reset translation
            activeLabel.setTranslateX(0);
            activeLabel.setTranslateY(0);

            // Run dragExit
            if (draggable && activeLabel.isDragging()) dragExitMethod.call(activeLabel);

            // Disable dragging & re-enable panning
            activeLabel.setDragging(false, null);
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
            activeLabel = (MapLabel) clickedNode;
            activeLabel.requestFocus();
          } else {
            if (clickEvent.getClickCount() > 1 && doubleClicked != null) {
              doubleClicked.call(clickEvent);
            }
          }
        });

    scrollPane.addEventFilter(ScrollEvent.SCROLL, Event::consume);
  }

  /**
   * Sets scaling of the map
   *
   * @param scaleKey new scale of map
   */
  public void setScale(int scaleKey) {
    if (zooms.containsKey(scaleKey)) {
      currentScale = scaleKey;
      scale = scaleKey / 100.0;
      Scale transform = new Scale(scale, scale);
      mapContainer.getTransforms().clear();
      mapContainer.getTransforms().add(transform);
      repositionScroller(
          mapContainer, scrollPane, scale, figureScrollOffset(mapContainer, scrollPane));
    }
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
   * @param visibleLocations the locations that will appear on the map
   * @param allLocations the locations that should be present, but aren't displayed. Used for
   *     Voronoi algorithm. Also must contain every visible location
   * @param genVoronoi if voronoi regions be generated from allLocations. Automatically enables
   *     snapping
   * @param graphicMethod the method for setting the graphic
   */
  public void setLabels(
      List<Location> visibleLocations,
      List<Location> allLocations,
      boolean genVoronoi,
      LabelMethod graphicMethod) {
    // Reset everything
    currentLabels.clear();
    iconContainer.getChildren().clear();
    snapLocations = null;
    draggable = false;

    for (Location loc : allLocations) {
      MapLabel label =
          new MapLabel.mapLabelBuilder()
              .location(loc)
              .equipment(loc.getEquipmentList())
              .requests(database.getServiceRequestsByLocation(loc)) // Replace?
              .build();

      // place label at correct coords
      label.relocate(
          label.getLocation().getXcoord(), // * (map.getFitWidth() / 1021),
          label.getLocation().getYcoord()); // * (map.getFitHeight() / 850));

      if (genVoronoi) {
        generateVoronoi(allLocations);
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

      if (visibleLocations.contains(loc)) {
        // label.getBound().setOnMouseClicked(evt -> label.requestFocus());
        // if (rightClickEvent != null) label.setOnContextMenuRequested(rightClickEvent);
        //            label
        //                    .getBound()
        //                    .setOnContextMenuRequested(
        //                            event -> rightClickMenu.show(label, event.getScreenX(),
        // event.getScreenY()));
        currentLabels.add(label);

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
                    if (labelClickedMethod != null) labelClickedMethod.call(label);
                    // Move to label
                    panToPoint(activeLabel.getLayoutX(), activeLabel.getLayoutY());
                  }
                });

        // Add graphic
        graphicMethod.call(label);
      }

      allLabels.add(label);
      iconContainer.getChildren().add(label);
    }
  }

  /**
   * Enables dragging of locations on the map, and sets the drag end function
   *
   * @param method the method to be called after the drag ends.
   */
  public void setDraggable(LabelMethod method) {
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

    ret.setFill(Color.TRANSPARENT);
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

  /**
   * Gets a list of all labels currently being displayed
   *
   * @return all currently active labels
   */
  public List<MapLabel> getCurrentLabels() {
    return currentLabels;
  }

  /**
   * Gets a list of all labels in map (includes hidden)
   *
   * @return all labels
   */
  public List<MapLabel> getAllLabels() {
    return allLabels;
  }

  /**
   * Given coordinates, move "camera" to that point.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public void panToPoint(double x, double y) {
    minX = scrollPane.getWidth() * (1 / (2 * scale));
    maxX = mapImage.getFitWidth() - minX;
    minY = scrollPane.getHeight() * (1 / (2 * scale));
    maxY = mapImage.getFitHeight() - minY;

    x = Math.max(minX, Math.min(x, maxX));
    y = Math.max(minY, Math.min(y, maxY));

    double scrollMax = zooms.get(currentScale);

    scrollPane.setHvalue(scrollMax * (x - minX) / (maxX - minX));
    scrollPane.setVvalue(scrollMax * (y - minY) / (maxY - minY));
  }

  // Don't use for now
  //  public List<Double> getCameraPoint() {
  //    minX = scrollPane.getWidth() / 2;
  //    maxX = mapImage.getFitWidth() - scrollPane.getWidth() / 2;
  //    minY = scrollPane.getHeight() / 2;
  //    maxY = mapImage.getFitHeight() - scrollPane.getHeight() / 2;
  //
  //    double x = scrollPane.getHvalue() * (maxX - minX) + minX;
  //    double y = scrollPane.getVvalue() * (maxY - minY) + minY;
  //    return List.of(x, y);
  //  }

  /**
   * Determines how many pixels are to the left/top of the scrollpane.
   *
   * @param scrollContent
   * @param scroller
   * @return
   */
  private Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {
    double extraWidth =
        scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
    double hScrollProportion =
        (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
    double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
    double extraHeight =
        scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
    double vScrollProportion =
        (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
    double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
    return new Point2D(scrollXOffset, scrollYOffset);
  }

  /**
   * ????
   *
   * @param scrollContent
   * @param scroller
   * @param scaleFactor
   * @param scrollOffset
   */
  private void repositionScroller(
      Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
    double scrollXOffset = scrollOffset.getX();
    double scrollYOffset = scrollOffset.getY();
    double extraWidth =
        scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
    if (extraWidth > 0) {
      double halfWidth = scroller.getViewportBounds().getWidth() / 2;
      double newScrollXOffset = (scaleFactor - 1) * halfWidth + scaleFactor * scrollXOffset;
      scroller.setHvalue(
          scroller.getHmin()
              + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
    } else {
      scroller.setHvalue(scroller.getHmin());
    }
    double extraHeight =
        scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
    if (extraHeight > 0) {
      double halfHeight = scroller.getViewportBounds().getHeight() / 2;
      double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
      scroller.setVvalue(
          scroller.getVmin()
              + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
    } else {
      scroller.setHvalue(scroller.getHmin());
    }
  }

  public static LabelMethod loadImage(String img) {
    return loc -> {
      Image labelGraphic = new Image(String.format(mapPath, img));
      ImageView imageCont = new ImageView(labelGraphic);
      loc.setGraphic(imageCont);
    };
  }
}
