package edu.wpi.cs3733.D22.teamZ;

import edu.wpi.cs3733.D22.teamZ.database.DBInitializer;
import edu.wpi.cs3733.D22.teamZ.database.EnumDatabaseConnection;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;

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
    FacadeDAO dao = FacadeDAO.getInstance();
    for (ServiceRequest test : dao.getAllServiceRequests()) {
      System.out.println(test);
    }
    App.launch(App.class, args);
  }
}
