package edu.wpi.cs3733.D22.teamZ.observers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An observer that will issue service requests when the dirty equipment pickup location has 10+
 * dirty pumps.
 */
public class DirtyPumpObserver {
  Location subject;
  UniqueID idMaker = new UniqueID();

  public DirtyPumpObserver(Location subject) {
    this.subject = subject;
    subject.attachDirtyPumpObservers(this);
  }

  public void update() {
    List<MedicalEquipment> totalList = subject.getEquipmentList();
    List<MedicalEquipment> dirtyList =
        totalList.stream()
            .filter(
                medEquip ->
                    (medEquip.getStatus().equals(MedicalEquipment.EquipmentStatus.DIRTY)
                        && medEquip.getType().equals("IPumps")))
            .collect(Collectors.toList());
    FacadeDAO dao = FacadeDAO.getInstance();
    List<MedicalEquipment> equipmentRequestList = dao.getAllMedicalEquipment();
    if (dirtyList.size() >= 10) {
      // Create a Medical Equipment Delivery service request for each dirty equipment
      for (MedicalEquipment dirtyEquip : dirtyList) {
        String id;
        // Check for empty db and set first request (will appear as REQ1 in the db)
        String requestID = idMaker.generateID("EQUIP");

        // Create a delivery request to zSTOR00101 for dirty equipment
        MedicalEquipmentDeliveryRequest newReq =
            new MedicalEquipmentDeliveryRequest(
                requestID,
                ServiceRequest.RequestStatus.UNASSIGNED,
                "admin1",
                null,
                dirtyEquip.getEquipmentID(),
                "zSTOR00101",
                LocalDateTime.now().toString(),
                null);
        dao.addMedicalEquipmentRequest(newReq);
      }
    }
  }
}
