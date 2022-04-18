package edu.wpi.cs3733.D22.teamZ.observers;

import edu.wpi.cs3733.D22.teamZ.controllers.UpperFloorsDashboardController;
import edu.wpi.cs3733.D22.teamZ.entity.Location;

public class DashboardBedAlertObserver {
  Location subject;
  UpperFloorsDashboardController dashboard;

  public DashboardBedAlertObserver(Location subject, UpperFloorsDashboardController dashboard) {
    this.subject = subject;
    this.dashboard = dashboard;
    subject.attachAlertObs(this);
  }

  public void update() {
    // Line 37 to line 44; if statement for alert
  }
}
