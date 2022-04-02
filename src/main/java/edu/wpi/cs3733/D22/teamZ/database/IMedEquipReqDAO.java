package edu.wpi.cs3733.D22.teamZ.database;

import java.util.List;

public interface IMedEquipReqDAO {
  List<MedEquipReq> getAllMedEquipReq();

  MedEquipReq getMedEquipReqByID(String requestID);

  void addMedEquipReq(MedEquipReq req);

  void updateMedEquipReq(MedEquipReq req);

  void deleteMedEquipReq(MedEquipReq req);

  boolean exportToMedEquipReqCSV();
}
