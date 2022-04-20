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
    List<MedicalEquipment> totalList =
        FacadeDAO.getInstance().getAllMedicalEquipmentByFloor(subject.getFloor());
    List<MedicalEquipment> dirtyBedList =
        totalList.stream()
            .filter(
                medEquip ->
                    medEquip.getStatus().equals(MedicalEquipment.EquipmentStatus.DIRTY)
                        && medEquip.getType().equals("Bed"))
            .collect(Collectors.toList());
    List<MedicalEquipment> dirtyPumpList =
        totalList.stream()
            .filter(
                equipment ->
                    equipment.getStatus().equals(MedicalEquipment.EquipmentStatus.DIRTY)
                        && equipment.getType().equals("IPumps"))
            .collect(Collectors.toList());
    List<MedicalEquipment> cleanPumpList =
        totalList.stream()
            .filter(
                equipment ->
                    equipment.getStatus().equals(MedicalEquipment.EquipmentStatus.CLEAN)
                        && equipment.getType().equals("IPumps"))
            .collect(Collectors.toList());
    // Check if location has 6 or more dirty beds OR 10+ dirty IPumps
    if (dirtyBedList.size() >= 6 || dirtyPumpList.size() >= 10 || cleanPumpList.size() < 5) {
      dashboard.updateBedAlert(
          subject.getFloor(), dirtyBedList.size(), dirtyPumpList.size(), cleanPumpList.size());
      dashboard.floorAlert(subject.getFloor());
    }
  }
}
