package edu.wpi.cs3733.D22.teamZ.entity;

public class LocationMapDelete {
  private String locationSelection;

  public LocationMapDelete() {}

  /**
   * Basic constructor for only LocationMapDelete with a location selected
   *
   * @param locationSelection
   */
  public LocationMapDelete(String locationSelection) {
    this.locationSelection = locationSelection;
  }

  /**
   * Gives the section of the location to delete of a LocationMapDelete object
   *
   * @return locationSelection of LocationMapDelete object
   */
  public String getLocationSelection() {
    return locationSelection;
  }

  /**
   * sets the section of the location to delete of a LocationMapDelete object
   *
   * @param locationSelection
   */
  public void setLocationSelection(String locationSelection) {
    this.locationSelection = locationSelection;
  }
}
