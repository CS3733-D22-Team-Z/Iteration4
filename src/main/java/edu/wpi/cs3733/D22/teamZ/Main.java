package edu.wpi.cs3733.D22.teamZ;

import edu.wpi.cs3733.D22.teamZ.database.DBInitializer;

public class Main {

  public static void main(String[] args) {

    System.out.println("0".repeat(0));

    DBInitializer init = new DBInitializer();
    init.createTables();
    init.populateLocationTable();
    init.populateServiceRequestTable();
    init.populateMedicalEquipmentTable();

    init.populateMedicalEquipmentServiceRequestTable();
    App.launch(App.class, args);
  }
}
