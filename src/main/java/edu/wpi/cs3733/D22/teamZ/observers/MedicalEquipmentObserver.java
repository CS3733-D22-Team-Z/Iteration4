package edu.wpi.cs3733.D22.teamZ.observers;

import edu.wpi.cs3733.D22.teamZ.controllers.UpperFloorsDashboardController;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import java.util.ArrayList;
import java.util.List;

/** An observer meant to connect Medical Equipment to the dashboard */
public class MedicalEquipmentObserver {
  List<MedicalEquipment> subjects;
  UpperFloorsDashboardController dashboard;

  public MedicalEquipmentObserver(UpperFloorsDashboardController dashboard) {
    this.subjects = new ArrayList<>();
    this.dashboard = dashboard;
  }

  public void addSubject(MedicalEquipment subject) {
    subjects.add(subject);
    subject.attach(this);
  }

  public void setSubjects(List<MedicalEquipment> subjects) {
    this.subjects = subjects;
    for (MedicalEquipment equipment : subjects) equipment.attach(this);
  }

  public void update(MedicalEquipment subject) {
    dashboard.medEquipUpdate(subject);
  }

  public void removeSubjects() {
    for (MedicalEquipment subject : subjects) subject.detatch(this);
    subjects.clear();
  }
}
