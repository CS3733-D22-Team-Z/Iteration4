package edu.wpi.cs3733.D22.teamZ.apiFacades;

import edu.wpi.cs3733.D22.teamB.api.API;
import edu.wpi.cs3733.D22.teamB.api.DatabaseController;
import edu.wpi.cs3733.D22.teamB.api.ServiceException;
import java.io.IOException;

public class SanitationFacadeAPI {
  private static final SanitationFacadeAPI instance = new SanitationFacadeAPI();
  private final API api;
  private final DatabaseController apiDatabase;

  public static SanitationFacadeAPI getInstance() {
    return instance;
  }

  private SanitationFacadeAPI() {
    api = new API();
    apiDatabase = new DatabaseController();
  }

  public void run(String cssPath) {
    try {
      api.run(0, 0, 450, 800, cssPath, "", "");
    } catch (ServiceException e) {
      System.out.println("Error accessing External Patient API");
      // System.out.println(e.getMessage());
      // e.printStackTrace();
    } catch (IOException ex) {
      System.out.println("weird");
    }
  }
}
