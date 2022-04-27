package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import java.time.LocalDateTime;

public class ServiceRequest {
  protected String requestID;
  protected RequestType type;
  protected RequestStatus status;
  protected Employee issuer;
  protected Employee handler;
  protected Location targetLocation;
  protected LocalDateTime opened;
  protected LocalDateTime closed;

  private FacadeDAO facadeDAO = FacadeDAO.getInstance();

  public enum RequestType {
    MEDEQUIP("MEDEQUIP"),
    MEDIC("MEDIC"),
    LABS("LABS"),
    MEAL("MEAL"),
    COMP("COMP"),
    LAUNDRY("LAUNDRY"),
    LANG("LANG"),
    EXTERNAL("EXTRL"),
    GIFT("GIFT"),
    CLEANING("CLEANING"),
    MEALSERV("MEALSERV"),
    SECURITY("SECURE"),
    BUYEQUIP("BUYEQUIP"),
    INTERNAL("INTRL"),
    FACILITY("FACILITY");

    private final String typeStr;

    RequestType(String typeStr) {
      this.typeStr = typeStr;
    }

    /**
     * Converts this RequestType into a String
     *
     * @return A String representing this RequestType
     */
    public String toString() {
      return this.typeStr;
    }

    /**
     * Returns a RequestType based on the String provided
     *
     * @param typeStr The String used to base the RequestType on
     * @return The RequestType associated with the String provided or null if no RequestType is
     *     found
     */
    public static RequestType getRequestTypeByString(String typeStr) {
      switch (typeStr) {
        case "MEDEQUIP":
          return MEDEQUIP;
        case "MEDIC":
          return MEDIC;
        case "LABS":
          return LABS;
        case "MEAL":
          return MEAL;
        case "COMP":
          return COMP;
        case "LAUNDRY":
          return LAUNDRY;
        case "LANG":
          return LANG;
        case "EXTRL":
          return EXTERNAL;
        case "GIFT":
          return GIFT;
        case "MEALSERV":
          return MEALSERV;
        case "CLEANING":
          return CLEANING;
        case "BUYEQUIP":
          return BUYEQUIP;
        case "SECURE":
          return SECURITY;
        case "INTRL":
          return INTERNAL;
        case "FACILITY":
          return FACILITY;
        default:
          return null;
      }
    }
  }

  public enum RequestStatus {
    UNASSIGNED("UNASSIGNED"),
    PROCESSING("PROCESSING"),
    DONE("DONE");

    private final String statusStr;

    RequestStatus(String statusStr) {
      this.statusStr = statusStr;
    }

    /**
     * Converts this RequestStatus into a String
     *
     * @return A String representing this RequestStatus
     */
    public String toString() {
      return this.statusStr;
    }

    /**
     * Returns a RequestStatus based on the String provided
     *
     * @param statusStr The String used to base the RequestStatus on
     * @return The RequestStatus associated with the String provided or null if no RequestStatus is
     *     found
     */
    public static RequestStatus getRequestStatusByString(String statusStr) {
      switch (statusStr) {
        case "UNASSIGNED":
          return UNASSIGNED;
        case "PROCESSING":
          return PROCESSING;
        case "DONE":
          return DONE;
        default:
          System.out.println("No RequestStatus found for the string: |" + statusStr + "|");
          return null;
      }
    }
  }

  /**
   * Constructs a ServiceRequest object. By default, handler is null and status is
   * RequestStatus.UNASSIGNED.
   *
   * @param requestID The unique ID for this ServiceRequest object.
   * @param type The type of service request this is. Must come from the RequestType enum.
   * @param targetLocation The location at which this request will take place
   * @param issuer The employee that issued the request
   */
  public ServiceRequest(
      String requestID,
      RequestType type,
      RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation,
      LocalDateTime opened,
      LocalDateTime closed) {
    this.requestID = requestID;
    this.type = type;
    this.targetLocation = targetLocation;
    this.status = status;
    this.issuer = issuer;
    this.handler = handler;
    this.opened = opened;
    this.closed = closed;
  }

  public ServiceRequest(
      String requestID,
      RequestType type,
      RequestStatus status,
      String issuer,
      String handler,
      String targetLocation,
      String opened,
      String closed) {
    this.requestID = requestID;
    this.type = type;
    this.targetLocation = facadeDAO.getLocationByID(targetLocation);
    this.status = status;
    this.issuer = facadeDAO.getEmployeeByID(issuer);
    this.handler = facadeDAO.getEmployeeByID(handler);
    this.opened = LocalDateTime.parse(opened);
    if (closed != null) {
      this.closed = LocalDateTime.parse(closed);
    }
  }

  // TODO delete just temp so I can test
  /*public ServiceRequest(
      String requestID,
      RequestType type,
      RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation) {
    this.requestID = requestID;
    this.type = type;
    this.targetLocation = targetLocation;
    this.status = status;
    this.issuer = issuer;
    this.handler = handler;
  }

  public ServiceRequest(
      String requestID,
      RequestType type,
      RequestStatus status,
      String issuer,
      String handler,
      String targetLocation) {
    this.requestID = requestID;
    this.type = type;
    this.targetLocation = facadeDAO.getLocationByID(targetLocation);
    this.status = status;
    this.issuer = facadeDAO.getEmployeeByID(issuer);
    this.handler = facadeDAO.getEmployeeByID(handler);
  }*/

  /**
   * Gets the requestID for this ServiceRequest
   *
   * @return A String representing the requestID
   */
  public final String getRequestID() {
    return this.requestID;
  }

  /**
   * Gets the type of ServiceRequest that this is
   *
   * @return The RequestType of this object
   */
  public final RequestType getType() {
    return this.type;
  }

  /**
   * Gets the current status of this service request
   *
   * @return The RequestStatus associated with this service request
   */
  public final RequestStatus getStatus() {
    return status;
  }

  /**
   * Sets the current status of this service request
   *
   * @param status The status that this request should be set to
   */
  public final void setStatus(RequestStatus status) {
    this.status = status;
  }

  /**
   * Gets the Employee who issued this service request
   *
   * @return The Employee who issued this service request
   */
  public final Employee getIssuer() {
    return issuer;
  }

  /**
   * Gets the Employee who is assigned to this service request
   *
   * @return The Employee who is assigned to this service request
   */
  public final Employee getHandler() {
    return handler;
  }

  /**
   * Sets the Employee who will be assigned to this service request
   *
   * @param handler The Employee who will handle this request
   */
  public final void setHandler(Employee handler) {
    this.handler = handler;
  }

  /**
   * Gets the Location where this service request will occur
   *
   * @return The Location that this service request is assigned to
   */
  public final Location getTargetLocation() {
    return targetLocation;
  }

  /**
   * Sets the Location where this service request will be assigned
   *
   * @param targetLocation The Location that this request will take place
   */
  public final void setTargetLocation(Location targetLocation) {
    this.targetLocation = targetLocation;
  }

  public LocalDateTime getOpened() {
    return opened;
  }

  public void setOpened(LocalDateTime opened) {
    this.opened = opened;
  }

  public LocalDateTime getClosed() {
    return closed;
  }

  public void setClosed(LocalDateTime closed) {
    this.closed = closed;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof ServiceRequest) {
      ServiceRequest objectRequest = (ServiceRequest) o;
      return (this.getRequestID().equals(objectRequest.getRequestID()));
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return this.requestID;
  }
}
