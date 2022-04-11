package edu.wpi.cs3733.D22.teamZ;

import edu.wpi.cs3733.D22.teamZ.database.DBInitializer;

public class Main {

  public static void main(String[] args) {
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
