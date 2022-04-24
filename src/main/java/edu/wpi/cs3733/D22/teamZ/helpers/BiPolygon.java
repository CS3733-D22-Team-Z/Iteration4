package edu.wpi.cs3733.D22.teamZ.helpers;

import edu.wpi.cs3733.D22.teamZ.entity.Location;
import javafx.scene.shape.Polygon;
import lombok.Getter;

/** A Polygon that is bidirectionally linked to a location */
public class BiPolygon extends Polygon {
  @Getter private Location parentLocation;

  public BiPolygon(Location newParent) {
    parentLocation = newParent;
  }
}
