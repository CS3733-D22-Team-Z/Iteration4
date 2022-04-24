package edu.wpi.cs3733.D22.teamZ.helpers;

import edu.wpi.cs3733.D22.teamZ.entity.MapLabel;

/** Represents a method that can be called with a MapLabel as its parameter. */
public interface LabelMethod {
  void call(MapLabel loc);
}
