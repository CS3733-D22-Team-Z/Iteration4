package edu.wpi.cs3733.D22.teamZ;

import edu.wpi.cs3733.D22.teamZ.database.DBInitializer;
import edu.wpi.cs3733.D22.teamZ.database.EnumDatabaseConnection;

public class Main {

  public static void main(String[] args) {
    EnumDatabaseConnection.CONNECTION.setConnection("embedded");
    DBInitializer init = new DBInitializer();
    init.createTables();
    init.populateLocationTable();
    init.populateMedicalEquipmentTable();
    init.populateEmployeeTable();
    init.populatePatientTable();
    init.populateServiceRequestTable();
    init.populateMedicalEquipmentServiceRequestTable();
    init.populateMealServiceRequestsTable();
    init.populateCleaningServiceRequestTable();

    /*FacadeDAO dao = FacadeDAO.getInstance();
    for (ServiceRequest test :
        dao.getServiceRequestsByLocation(dao.getLocationByID("zDIRT00103"))) {
      System.out.println(test);
    }*/
    App.launch(App.class, args);
  }
}
