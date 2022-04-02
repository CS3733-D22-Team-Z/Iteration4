package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipmentDeliveryRequest;

import java.util.List;

public interface IMedEquipReqDAO {
  List<MedicalEquipmentDeliveryRequest> getAllMedEquipReq();

  MedicalEquipmentDeliveryRequest getMedEquipReqByID(String requestID);

  void addMedEquipReq(MedicalEquipmentDeliveryRequest req);

  void updateMedEquipReq(MedicalEquipmentDeliveryRequest req);

  void deleteMedEquipReq(MedicalEquipmentDeliveryRequest req);

  boolean exportToMedEquipReqCSV();
}
