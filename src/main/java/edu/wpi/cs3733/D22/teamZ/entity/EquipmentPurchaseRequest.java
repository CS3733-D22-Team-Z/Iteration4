package edu.wpi.cs3733.D22.teamZ.entity;

import java.time.LocalDateTime;

public class EquipmentPurchaseRequest extends ServiceRequest {
  private String equipmentType;
  private String paymentMethod;

  public EquipmentPurchaseRequest(
      String requestID,
      RequestStatus status,
      Employee issuer,
      Employee handler,
      Location targetLocation,
      LocalDateTime opened,
      LocalDateTime closed,
      String equipmentType,
      String paymentMethod) {
    super(requestID, RequestType.BUYEQUIP, status, issuer, handler, targetLocation, opened, closed);
    this.equipmentType = equipmentType;
    this.paymentMethod = paymentMethod;
  }

  public String getEquipmentType() {
    return equipmentType;
  }

  public void setEquipmentType(String equipmentType) {
    this.equipmentType = equipmentType;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }
}
