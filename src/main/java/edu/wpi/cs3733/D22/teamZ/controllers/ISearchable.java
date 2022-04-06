package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.entity.Location;
import java.util.List;

public interface ISearchable {
  /**
   * This function should return a list of searchable strings.
   *
   * <p>Additionally, add a prefix (ex. "N:") to enable type searching Prefix should be one capital
   * letter that has not yet been registered at the top of the SearchControl class, followed by a
   * single colon. New prefixes should be registered in SearchControl as well.
   *
   * @return A list of strings that can/should be searched.
   */
  List<String> toSearchTerms();

  /**
   * Get a Location to navigate to. This should be the place where the searched object can be found
   * on the map.
   *
   * @return The location of the searched object.
   */
  Location getAssociatedLocation();

  String getDisplayName();
}
