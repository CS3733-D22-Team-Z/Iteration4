package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import java.util.List;

public interface IMedicalEquipmentDAO {
  List<MedicalEquipment> getAllMedicalEquipment();

  MedicalEquipment getMedicalEquipmentByID(String itemID);

  boolean addMedicalEquipment(MedicalEquipment equipment);

  boolean updateMedicalEquipment(MedicalEquipment equipment);

  boolean deleteMedicalEquipment(MedicalEquipment equipment);

  boolean exportToMedicalEquipmentCSV();
}
