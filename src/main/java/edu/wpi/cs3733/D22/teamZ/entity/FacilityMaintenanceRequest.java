package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamC.entity.service_request.facility_maintenance.FacilityMaintenanceSRAPI;
import lombok.Getter;
import lombok.Setter;

public class FacilityMaintenanceRequest extends ServiceRequest {
  @Getter @Setter MaintenanceType maintenanceType;
  @Getter @Setter String description;
  @Getter @Setter FacilityMaintenanceSRAPI.Priority priority;

  public enum MaintenanceType {
    PIPE("PIPE"),
    MACHINE("MACHINE"),
    LIGHT("LIGHT"),
    MEDICALTOOL("MEDICALTOOL"),
    COMPUTER("COMPUTER"),
    TOILET("TOILET"),
    SINK("SINK"),
    DOOR("DOOR"),
    PHONE("PHONE");

    private final String typeStr;

    MaintenanceType(String typeStr) {
      this.typeStr = typeStr;
    }

    /**
     * Converts this MaintenanceType into a String
     *
     * @return A String representing this MaintenanceType
     */
    public String toString() {
      return this.typeStr;
    }

    /**
     * Returns a MaintenanceType based on the String provided
     *
     * @param typeStr The String used to base the MaintenanceType on
     * @return The MaintenanceType associated with the String provided or null if no MaintenanceType
     *     is found
     */
    public static MaintenanceType getMaintenanceTypeByString(String typeStr) {
      for (MaintenanceType type : MaintenanceType.values()) {
        if (type.toString().equals(typeStr)) {
          return type;
        }
      }
      System.out.println("No RequestStatus found for the string: |" + typeStr + "|");
      return null;
    }
  }

  public FacilityMaintenanceRequest(
      String requestID,
      RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation,
      MaintenanceType maintenanceType,
      FacilityMaintenanceSRAPI.Priority priority,
      String description) {
    super(requestID, RequestType.FACILITY, status, issuer, handler, targetLocation, null, null);
    this.maintenanceType = maintenanceType;
    this.description = description;
    this.priority = priority;
  }
}
