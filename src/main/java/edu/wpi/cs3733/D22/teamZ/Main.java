package edu.wpi.cs3733.D22.teamZ;

import static edu.wpi.cs3733.D22.teamZ.entity.PathEdge.findPath;

import edu.wpi.cs3733.D22.teamZ.database.DBInitializer;
import edu.wpi.cs3733.D22.teamZ.database.EnumDatabaseConnection;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.PathEdge;
import java.util.List;
import java.util.stream.Collectors;

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
    init.populateEquipmentPurchaseTable();
    init.populateLaundryServiceRequests();
    init.populateComputerRequestTable();

    /*FacadeDAO dao = FacadeDAO.getInstance();
    for (ServiceRequest test :
        dao.getServiceRequestsByLocation(dao.getLocationByID("zDIRT00103"))) {
      System.out.println(test);
    }*/

    FacadeDAO locationDAO = FacadeDAO.getInstance();

    List<Location> allLoc = locationDAO.getAllLocations();
    long start = System.nanoTime();
    List<PathEdge> pati = findPath(allLoc.get(0), allLoc.get(133)).collect(Collectors.toList());
    long fin = System.nanoTime();
    System.out.printf("%.6f", (double) ((fin - start) / 1000000000));
    List<PathEdge> west = findPath(allLoc.get(0), allLoc.get(126)).collect(Collectors.toList());

    System.out.println("to pati:");
    for (PathEdge l : pati) {
      System.out.println(l.getDest().getLongName());
    }

    System.out.println("to west:");
    for (PathEdge l : west) {
      System.out.println(l.getDest().getLongName());
    }

    App.launch(App.class, args);
  }
}
