package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.observers.MedicalEquipmentObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MedicalEquipment {
  private String equipmentID;
  private String type;
  private MedicalEquipment.EquipmentStatus status;
  private Location currentLocation;
  private List<MedicalEquipmentObserver> observers;

  // Constructors

  /**
   * Full constructor for MedicalEquipment
   *
   * @param equipmentID
   * @param type
   * @param status
   * @param currentLocation
   */
  public MedicalEquipment(
      String equipmentID, String type, String status, Location currentLocation) {
    this.equipmentID = equipmentID;
    this.type = type;
    this.status = EquipmentStatus.getRequestStatusByString(status);
    this.currentLocation = currentLocation;
    if (!this.currentLocation.getEquipmentList().contains(this)) {
      this.currentLocation.addEquipmentToList(this);
    }
    this.observers = new ArrayList<>();
  }

  public enum EquipmentStatus {
    CLEAN("CLEAN"),
    DIRTY("DIRTY"),
    CLEANING("CLEANING"),
    INUSE("INUSE");

    private final String statusStr;

    EquipmentStatus(String statusStr) {
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
    public static MedicalEquipment.EquipmentStatus getRequestStatusByString(String statusStr) {
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
          System.out.println("No EquipmentStatus found for the string: |" + statusStr + "|");
          return null;
      }
    }
  }

  /**
   * Gives the itemID of an MedicalEquipment object
   *
   * @return itemID of MedicalEquipment object
   */
  public String getEquipmentID() {
    return equipmentID;
  }

  /**
   * Gives the availability of a MedicalEquipment object
   *
   * @return status of MedicalEquipment availability
   */
  public EquipmentStatus getStatus() {
    return status;
  }

  /**
   * Sets the availability of a MedicalEquipment object
   *
   * @param status
   */
  public void setStatus(EquipmentStatus status) {
    this.status = status;
    notifyAllObservers();
  }

  public void notifyAllObservers() {
    for (MedicalEquipmentObserver obs : observers) {
      obs.update(this);
    }
  }

  public void attach(MedicalEquipmentObserver observer) {
    observers.add(observer);
    observer.update(this);
  }

  public void detatch(MedicalEquipmentObserver observer) {
    observers.remove(observer);
  }

  /** Check if parent location has 6+ dirty beds. */
  public void checkParentLoc() {
    // Check if location has 6+ dirty beds
    List<MedicalEquipment> totalList = currentLocation.getEquipmentList();
    List<MedicalEquipment> dirtyList =
        totalList.stream()
            .filter(medEquip -> medEquip.getStatus().equals(MedicalEquipment.EquipmentStatus.DIRTY))
            .collect(Collectors.toList());
    FacadeDAO dao = FacadeDAO.getInstance();
    List<MedicalEquipment> equipmentRequestList = dao.getAllMedicalEquipment();
    if (dirtyList.size() >= 6) {
      // Create a Medical Equipment Delivery service request for each dirty equipment
      for (MedicalEquipment dirtyEquip : dirtyList) {
        String id;
        // Check for empty db and set first request (will appear as REQ1 in the db)

        if (equipmentRequestList.isEmpty()) {
          System.out.println("Equipment is empty");
          id = "REQ0";
        } else {
          List<ServiceRequest> currentList = dao.getAllServiceRequests();
          ServiceRequest lastestReq = currentList.get(currentList.size() - 1);
          id = lastestReq.getRequestID();
        }
        // Create new REQID
        int num = 1 + Integer.parseInt(id.substring(id.lastIndexOf("Q") + 1));
        String requestID = "REQ" + num;

        MedicalEquipmentDeliveryRequest newReq =
            new MedicalEquipmentDeliveryRequest(
                requestID,
                ServiceRequest.RequestStatus.UNASSIGNED,
                "admin1",
                null,
                dirtyEquip.getEquipmentID(),
                "zSTOR001L1");
        dao.addMedicalEquipmentRequest(newReq);
      }
    }
  }

  /**
   * Gives the current location of the MedicalEquipment object
   *
   * @return Location object
   */
  public Location getCurrentLocation() {
    return currentLocation;
  }

  /**
   * Sets the current location of the MedicalEquipment object
   *
   * @param currentLocation
   */
  public void setCurrentLocation(Location currentLocation) {
    if (this.currentLocation != null) {
      this.getCurrentLocation().removeEquipmentFromList(this);
    }
    this.currentLocation = currentLocation;
    if (!currentLocation.getEquipmentList().contains(this)) {
      currentLocation.addEquipmentToList(this);
    }
  }

  /**
   * Gives the type of the MedicalEquipment object
   *
   * @return type of MedicalEquipment
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type of the MedicalEquipment object
   *
   * @param type
   */
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof MedicalEquipment) {
      MedicalEquipment objectEquipment = (MedicalEquipment) o;
      return (this.getEquipmentID().equals(objectEquipment.getEquipmentID()));
    } else {
      return false;
    }
  }
}
