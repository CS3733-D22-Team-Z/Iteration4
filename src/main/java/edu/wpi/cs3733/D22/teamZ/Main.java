package edu.wpi.cs3733.D22.teamZ;


import edu.wpi.cs3733.D22.teamZ.database.DBInitializer;
import edu.wpi.cs3733.D22.teamZ.database.EnumDatabaseConnection;
import edu.wpi.cs3733.D22.teamZ.database.LocationDAOImpl;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.PathEdge;
import java.util.List;
import java.util.stream.Collectors;

import static edu.wpi.cs3733.D22.teamZ.entity.PathEdge.findPath;

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

    /*FacadeDAO dao = FacadeDAO.getInstance();
    for (ServiceRequest test :
        dao.getServiceRequestsByLocation(dao.getLocationByID("zDIRT00103"))) {
      System.out.println(test);
    }*/

    LocationDAOImpl locationDAO = new LocationDAOImpl();

    List<Location> allLoc = locationDAO.getAllLocations();
    allLoc.get(0).addConnection(new PathEdge(allLoc.get(0), allLoc.get(21)));
    allLoc.get(21).addConnection(new PathEdge(allLoc.get(21), allLoc.get(0)));

    allLoc.get(21).addConnection(new PathEdge(allLoc.get(21), allLoc.get(17)));
    allLoc.get(17).addConnection(new PathEdge(allLoc.get(17), allLoc.get(21)));

    allLoc.get(17).addConnection(new PathEdge(allLoc.get(17), allLoc.get(16)));
    allLoc.get(16).addConnection(new PathEdge(allLoc.get(16), allLoc.get(17)));

    allLoc.get(17).addConnection(new PathEdge(allLoc.get(17), allLoc.get(19)));
    allLoc.get(19).addConnection(new PathEdge(allLoc.get(19), allLoc.get(17)));

    allLoc.get(19).addConnection(new PathEdge(allLoc.get(19), allLoc.get(126)));
    allLoc.get(126).addConnection(new PathEdge(allLoc.get(126), allLoc.get(19)));

    allLoc.get(16).addConnection(new PathEdge(allLoc.get(16), allLoc.get(15)));
    allLoc.get(15).addConnection(new PathEdge(allLoc.get(15), allLoc.get(16)));

    allLoc.get(15).addConnection(new PathEdge(allLoc.get(15), allLoc.get(24)));
    allLoc.get(24).addConnection(new PathEdge(allLoc.get(24), allLoc.get(15)));

    allLoc.get(24).addConnection(new PathEdge(allLoc.get(24), allLoc.get(23)));
    allLoc.get(23).addConnection(new PathEdge(allLoc.get(23), allLoc.get(24)));

    allLoc.get(23).addConnection(new PathEdge(allLoc.get(23), allLoc.get(19)));
    allLoc.get(19).addConnection(new PathEdge(allLoc.get(19), allLoc.get(23)));

    allLoc.get(15).addConnection(new PathEdge(allLoc.get(55), allLoc.get(55)));
    allLoc.get(55).addConnection(new PathEdge(allLoc.get(55), allLoc.get(15)));

    List<PathEdge> elev = findPath(allLoc.get(0), allLoc.get(55)).collect(Collectors.toList());

    List<PathEdge> west = findPath(allLoc.get(0), allLoc.get(126)).collect(Collectors.toList());

    System.out.println("to elev:");
    for (PathEdge l : elev) {
      System.out.println(l.getDest().getLongName());
    }

    System.out.println("to west:");
    for (PathEdge l : west) {
      System.out.println(l.getDest().getLongName());
    }

    App.launch(App.class, args);
  }
}
