package edu.wpi.cs3733.D22.teamZ;

import edu.wpi.cs3733.D22.teamZ.database.DBInitializer;

public class Main {

  public static void main(String[] args) {
    DBInitializer init = new DBInitializer();
    init.createTables();
    init.populateLocationTable();
    init.populateMedicalEquipmentTable();

//    LocationDAOImpl locDAO = new LocationDAOImpl();
//    Location loc = locDAO.getLocationByID("zPATI00103");
//    MedicalEquipmentDAOImpl medDAO = new MedicalEquipmentDAOImpl();
//    List<MedicalEquipment> medeq = medDAO.getAllMedicalEquipmentByLocation(loc);
//    for (int i = 0; i < medeq.size(); i++) {
//      MedicalEquipment temp = medeq.get(i);
//      System.out.println(temp.getItemID());
//    }

    // init.populateReqTable();
    App.launch(App.class, args);
  }
}
