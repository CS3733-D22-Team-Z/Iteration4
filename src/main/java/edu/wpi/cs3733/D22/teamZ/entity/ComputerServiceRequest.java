package edu.wpi.cs3733.D22.teamZ.entity;

import java.time.LocalDateTime;

public class ComputerServiceRequest extends ServiceRequest {
  private String operatingSystem;
  private String problemDescription;

  public ComputerServiceRequest(
      String requestID,
      RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation,
      LocalDateTime opened,
      LocalDateTime closed,
      String operatingSystem,
      String problemDescription) {
    super(requestID, RequestType.COMP, status, issuer, handler, targetLocation, opened, closed);
    this.operatingSystem = operatingSystem;
    this.problemDescription = problemDescription;
  }

  // GetterFunctions
  public String getOperatingSystem() {
    return operatingSystem;
  }

  public String getProblemDescription() {
    return problemDescription;
  }

  // SetterFunctions
  public void setOperatingSystem(String operatingSystem) {
    this.operatingSystem = operatingSystem;
  }

  public void setProblemDescription(String problemDescription) {
    this.problemDescription = problemDescription;
  }
}
