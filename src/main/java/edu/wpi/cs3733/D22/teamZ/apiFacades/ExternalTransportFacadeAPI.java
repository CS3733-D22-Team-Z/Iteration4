package edu.wpi.cs3733.D22.teamZ.apiFacades;

import edu.wpi.cs3733.D22.teamZ.api.API;
import edu.wpi.cs3733.D22.teamZ.api.exception.ServiceException;

public class ExternalTransportFacadeAPI {

  private static final ExternalTransportFacadeAPI instance = new ExternalTransportFacadeAPI();
  private final API api;

  public static ExternalTransportFacadeAPI getInstance() {
    return instance;
  }

  private ExternalTransportFacadeAPI() {
    api = new API();
  }

  public void run(String cssPath) {
    try {
      api.run(0, 0, 450, 800, cssPath, "", "");
    } catch (ServiceException e) {
      System.out.println("Error accessing External Patient API");
      System.out.println(e.getMessage());
      // e.printStackTrace();
    }
  }
}
