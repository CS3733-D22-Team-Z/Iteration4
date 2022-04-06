package edu.wpi.cs3733.D22.teamZ.entity;

public class MealServiceOption {
  // TODO: UPDATED PARAMS (add quantity, cost, dietaryInfo, etc.)
  private String itemID;
  private String type;
  private String status;
  private Location currentLocation;

  // Constructors

  /**
   * Basic constructor for only MealServiceOption with itemID
   *
   * @param itemID
   */
  public MealServiceOption(String itemID) {
    this.itemID = itemID;
  }

  /**
   * TODO: UPDATE PARAMS (add quantity, cost, dietaryInfo, etc.)
   *
   * <p>Full constructor for MealServiceOption
   *
   * @param itemID
   * @param type
   * @param status
   * @param currentLocation
   */
  public MealServiceOption(String itemID, String type, String status, Location currentLocation) {
    this.itemID = itemID;
    this.type = type;
    this.status = status;
    this.currentLocation = currentLocation;
  }

  /**
   * Gives the itemID of an MealServiceOption object
   *
   * @return itemID of MealServiceOption object
   */
  public String getItemID() {
    return itemID;
  }

  /**
   * Gives the availability of a MealServiceOption object
   *
   * @return status of MealServiceOption availability
   */
  public String getStatus() {
    return status;
  }

  /**
   * Sets the availability of a MealServiceOption object
   *
   * @param status
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Gives the current location of the MealServiceOption object
   *
   * @return Location object
   */
  public Location getCurrentLocation() {
    return currentLocation;
  }

  /**
   * Sets the current location of the MealServiceOption object
   *
   * @param currentLocation
   */
  public void setCurrentLocation(Location currentLocation) {
    this.currentLocation = currentLocation;
  }

  /**
   * Gives the type of the MealServiceOption object
   *
   * @return type of MedicalEquipment
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type of the MealServiceOption object
   *
   * @param type
   */
  public void setType(String type) {
    this.type = type;
  }
}
