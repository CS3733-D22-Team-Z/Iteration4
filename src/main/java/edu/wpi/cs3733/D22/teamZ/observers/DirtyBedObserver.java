package edu.wpi.cs3733.D22.teamZ.observers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipmentDeliveryRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An observer that will issue service requests when the dirty equipment pickup location has 6+
 * dirty beds.
 */
public class DirtyBedObserver {
  Location subject;
  // UpperFloorsDashboardController dashboard;

  public DirtyBedObserver(Location subject) {
    this.subject = subject;
    subject.attachBedObs(this);
  }

  /*public void addSubject(MedicalEquipment subject) {
    subjects.add(subject);
    subject.attach(this);
  }

  public void setSubjects(List<MedicalEquipment> subjects) {
    for (MedicalEquipment equipment : subjects) addSubject(equipment);
  }*/

  public void update() {
    // Only count if equipment is dirty.
    // if (subject.getStatus().equals(MedicalEquipment.EquipmentStatus.DIRTY)) {
    // Check if location has 6+ dirty beds
    List<MedicalEquipment> totalList = subject.getEquipmentList();
    List<MedicalEquipment> dirtyList =
        totalList.stream()
            .filter(
                medEquip ->
                    (medEquip.getStatus().equals(MedicalEquipment.EquipmentStatus.DIRTY)
                        && medEquip.getType().equals("Bed")))
            .collect(Collectors.toList());
    FacadeDAO dao = FacadeDAO.getInstance();
    List<ServiceRequest> equipmentRequestList = dao.getAllServiceRequests();
    if (dirtyList.size() >= 6) {
      // Create a Medical Equipment Delivery service request for each dirty equipment
      for (MedicalEquipment dirtyEquip : dirtyList) {
        // Use streams to check if there is already a medical equipment request made.
        if (equipmentRequestList.stream()
            .noneMatch(
                req ->
                    (req instanceof MedicalEquipmentDeliveryRequest
                        && ((MedicalEquipmentDeliveryRequest) req)
                            .getEquipmentID()
                            .equals(dirtyEquip.getEquipmentID())))) {
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

          // Create a delivery request to zSTOR001L1 for dirty equipment
          MedicalEquipmentDeliveryRequest newReq =
              new MedicalEquipmentDeliveryRequest(
                  requestID,
                  ServiceRequest.RequestStatus.UNASSIGNED,
                  "admin1",
                  null,
                  dirtyEquip.getEquipmentID(),
                  "zSTOR001L1");
          dao.addMedicalEquipmentRequest(newReq);
        }
      }
    }
    // }
  }

  //  public void removeSubjects() {
  //    for (MedicalEquipment subject : subjects) subject.detach(this);
  //    subjects.clear();
  //  }
}
