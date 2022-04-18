package edu.wpi.cs3733.D22.teamZ;

import edu.wpi.cs3733.D22.teamZ.database.DBInitializer;
import edu.wpi.cs3733.D22.teamZ.database.EnumDatabaseConnection;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;

public class Main {

  public static void main(String[] args) {
    EnumDatabaseConnection.CONNECTION.setConnection("embedded");
    DBInitializer init = new DBInitializer();
    init.createTables();
    init.populateLocationTable();
    init.populateMedicalEquipmentTable();
    init.populateEmployeeTable();
    init.populateServiceRequestTable();
    init.populateMedicalEquipmentServiceRequestTable();
    //System.out.println(FacadeDAO.getInstance().countDirtyInfusionPumpsInFloor("5"));
    /*FacadeDAO dao = FacadeDAO.getInstance();
    for (ServiceRequest test :
        dao.getServiceRequestsByLocation(dao.getLocationByID("zDIRT00103"))) {
      System.out.println(test);
    }*/
    App.launch(App.class, args);
  }
}
