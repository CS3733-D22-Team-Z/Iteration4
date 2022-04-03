package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.LocationDAOImpl;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

public class EquipmentMapController implements Initializable {
  @FXML private ImageView mapImage;
  @FXML private Pane mapContainer;
  private Canvas mapCanvas;
  private GraphicsContext mapCtx;
  private LocationDAOImpl locationDAO;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    // Make a Canvas with the exact dimensions as the ImageView
    mapCanvas = new Canvas();
    mapCanvas.setHeight(mapImage.getFitHeight());
    mapCanvas.setWidth(mapImage.getFitWidth());

    // Add to end of parent's children so it is rendered over ImageView
    mapContainer.getChildren().add(mapCanvas);

    // Set mapCtx to newly created canvas' context
    mapCtx = mapCanvas.getGraphicsContext2D();

    // Setup DAO
    locationDAO = new LocationDAOImpl();

    // Pull all locations
    List<Location> locations = locationDAO.getAllLocations();

    // Draw dots
    for (Location loc : locations) {
      // Only draw dots from first floor
      if (loc.getFloor().equals("1")) {
        if (loc.getNodeID().equals("zDEPT00301")) mapCtx.setFill(Color.RED);
        else mapCtx.setFill(Color.BLUE);
        int size = 10;
        mapCtx.fillOval(loc.getXcoord() - size / 2, loc.getYcoord() - size / 2, size, size);
      }
    }

    // Zoom stuff
    Scale scaleTransform = new Scale(0.75, 0.75, 0, 0);
    mapContainer.getTransforms().add(scaleTransform);
  }

  public void drawIcons() {
    // Image img = mapCtx.drawImage(App.class.getResource("images/home.png"), 20.0, 20.0);
  }
}
