package edu.wpi.cs3733.D22.teamZ.observers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipmentDeliveryRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An observer that will issue service requests when the dirty equipment pickup location has 10+
 * dirty pumps.
 */
public class DirtyPumpObserver {
  Location subject;

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
