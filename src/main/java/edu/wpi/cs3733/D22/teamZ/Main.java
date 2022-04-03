package edu.wpi.cs3733.D22.teamZ;

import edu.wpi.cs3733.D22.teamZ.database.DBInitializer;

public class Main {

  public static void main(String[] args) {
    DBInitializer init = new DBInitializer();
    init.createTables();
    init.populateLocationTable();
    init.populateMedicalEquipmentTable();
    // init.populateReqTable();
    // pp.launch(App.class, args);
  }
}
