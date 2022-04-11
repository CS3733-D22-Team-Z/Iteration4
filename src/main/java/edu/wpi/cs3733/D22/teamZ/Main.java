package edu.wpi.cs3733.D22.teamZ;

import edu.wpi.cs3733.D22.teamZ.database.DBInitializer;
import edu.wpi.cs3733.D22.teamZ.database.EnumDatabaseConnection;

public class Main {

  public static void main(String[] args) {
    EnumDatabaseConnection.CONNECTION.setConnection("embedded");
    DBInitializer init = new DBInitializer();
    init.createTables();
    init.populateLocationTable();
    // TODO insert populateEmployeeTable here
    init.populateMedicalEquipmentTable();
    init.populateServiceRequestTable();
    init.populateMedicalEquipmentServiceRequestTable();
    App.launch(App.class, args);
  }
}
