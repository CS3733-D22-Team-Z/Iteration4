package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;

public class LaundryServiceRequest extends ServiceRequest {

  private String laundryType;
  private LaundryStatus status;

  private FacadeDAO facadeDAO = FacadeDAO.getInstance();

  public LaundryServiceRequest(
      String requestID,
      ServiceRequest.RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation,
      LaundryStatus laundryStatus,
      String linenType) {
    super(requestID, ServiceRequest.RequestType.LAUNDRY, status, issuer, handler, targetLocation);
    laundryType = linenType;
    this.status = laundryStatus;
  }

  public LaundryServiceRequest(
      String requestID,
      ServiceRequest.RequestStatus status,
      String issuer,
      String handler,
      String targetLocation,
      LaundryStatus laundryStatus,
      String linenType) {
    super(requestID, ServiceRequest.RequestType.LAUNDRY, status, issuer, handler, targetLocation);
    laundryType = linenType;
    this.status = laundryStatus;
  }

  public enum LaundryStatus {
    CLEAN("CLEAN"),
    DIRTY("DIRTY"),
    CLEANING("CLEANING"),
    INUSE("INUSE");

    private final String statusStr;

    LaundryStatus(String statusStr) {
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
    public static LaundryServiceRequest.LaundryStatus getRequestStatusByString(String statusStr) {
      switch (statusStr) {
        case "CLEAN":
          return CLEAN;
        case "DIRTY":
          return DIRTY;
        case "CLEANING":
          return CLEANING;
        case "INUSE":
          return INUSE;
        default:
          System.out.println("No LaundryStatus found for the string: |" + statusStr + "|");
          return null;
      }
    }
  }

  // GetterFunctions

  public String getLaundryType() {
    return this.laundryType;
  }

  public LaundryStatus getLaundryStatus() {
    return status;
  }
  // SetterFunctions

  public void setLaundryStatus(LaundryStatus status) {
    this.status = status;
  }

  public void setLaundryType(String laundryType) {
    this.laundryType = laundryType;
  }
}
