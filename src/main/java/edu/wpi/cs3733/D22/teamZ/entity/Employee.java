package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.controllers.ISearchable;
import java.util.List;

public class Employee implements ISearchable {
  private String employeeID;
  private String name;
  private AccessType accesstype;
  private String username;
  private String password;

  @Override
  public List<String> toSearchTerms() {
    return List.of("E:" + name, "P:" + accesstype.toString(), employeeID);
  }

  @Override
  public Location getAssociatedLocation() {
    return null; // this can be changed whenever employees get integrated with service requests.
  } // Just return an associated location whenever this happens.

  @Override
  public String getDisplayName() {
    return this.name;
  }

  public enum AccessType {
    ADMIN,
    DOCTOR,
    NURSE
  }
}
