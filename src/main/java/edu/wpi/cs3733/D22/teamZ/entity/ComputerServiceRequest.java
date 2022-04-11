package edu.wpi.cs3733.D22.teamZ.entity;

public class ComputerServiceRequest extends ServiceRequest {
  private String operatingSystem;
  private String problemDescription;

  public ComputerServiceRequest(
      String requestID,
      RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation,
      String operatingSystem,
      String problemDescription) {
    super(requestID, RequestType.COMP, status, issuer, handler, targetLocation);
    this.operatingSystem = operatingSystem;
    this.problemDescription = problemDescription;
  }

  public ComputerServiceRequest(
      String requestID,
      RequestStatus status,
      String issuer,
      String handler,
      String targetLocation,
      String operatingSystem,
      String problemDescription) {
    super(requestID, RequestType.COMP, status, issuer, handler, targetLocation);
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
