package edu.wpi.cs3733.D22.teamZ.apiFacades;

import edu.wpi.cs3733.D22.teamC.TeamCAPI;
import edu.wpi.cs3733.D22.teamC.controller.service_request.facility_maintenance.ServiceException;

public class FacilityMaintenanceFacadeAPI {
  private static final FacilityMaintenanceFacadeAPI instance = new FacilityMaintenanceFacadeAPI();
  private final TeamCAPI api;

  public static FacilityMaintenanceFacadeAPI getInstance() {
    return instance;
  }

  private FacilityMaintenanceFacadeAPI() {
    this.api = new TeamCAPI();
  }

  public void run(String cssPath) {
    try {
      api.run(0, 0, 300, 300, cssPath, "", "");
    } catch (ServiceException e) {
      System.out.println("Security API Service Exception");
    }
  }
}
