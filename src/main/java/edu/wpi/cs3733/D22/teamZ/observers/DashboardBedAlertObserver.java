package edu.wpi.cs3733.D22.teamZ.observers;

import edu.wpi.cs3733.D22.teamZ.controllers.UpperFloorsDashboardController;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardBedAlertObserver {
  Location subject;
  UpperFloorsDashboardController dashboard;

  public DashboardBedAlertObserver(Location subject, UpperFloorsDashboardController dashboard) {
    this.subject = subject;
    this.dashboard = dashboard;
    subject.attachAlertObs(this);
  }

  public void update() {
    // Count dirty equipment
    List<MedicalEquipment> totalList = subject.getEquipmentList();
    List<MedicalEquipment> dirtyList =
        totalList.stream()
            .filter(
                medEquip ->
                    medEquip.getStatus().equals(MedicalEquipment.EquipmentStatus.DIRTY)
                        && medEquip.getType().equals("Bed"))
            .collect(Collectors.toList());
    FacadeDAO dao = FacadeDAO.getInstance();
    List<MedicalEquipment> equipmentResultList = dao.getAllMedicalEquipment();
    // Check if location has 6 or more dirty beds
    if (dirtyList.size() >= 6) {
      dashboard.floorAlert(subject.getFloor());
    }
  }
}
