package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class DBInitializer {
  private LocationControlCSV locCSV;
  private EmployeeControlCSV employeeCSV;
  private MedicalEquipmentControlCSV medicalEquipmentControlCSV;
  private ServiceRequestControlCSV serviceControlCSV;
  private MedEqReqControlCSV medEqReqControlCSV;

  static Connection connection = DatabaseConnection.getConnection();

  public DBInitializer() {
    File locData =
        new File(
            System.getProperty("user.dir")
                + System.getProperty("file.separator")
                + "TowerLocations.csv");
    File employeeData =
        new File(
            System.getProperty("user.dir")
                + System.getProperty("file.separator")
                + "Employees.csv");
    File medicalEquipmentData =
        new File(
            System.getProperty("user.dir")
                + System.getProperty("file.separator")
                + "MedicalEquipment.csv");
    File serviceRequestData =
        new File(
            System.getProperty("user.dir")
                + System.getProperty("file.separator")
                + "ServiceRequest.csv");
    File medEquipReqData =
        new File(
            System.getProperty("user.dir")
                + System.getProperty("file.separator")
                + "MedEquipReq.csv");

    locCSV = new LocationControlCSV(locData);
    employeeCSV = new EmployeeControlCSV(employeeData);
    medicalEquipmentControlCSV = new MedicalEquipmentControlCSV(medicalEquipmentData);
    serviceControlCSV = new ServiceRequestControlCSV(serviceRequestData);
    medEqReqControlCSV = new MedEqReqControlCSV(medEquipReqData);
  }

  public boolean createTables() {
    Statement stmt = null;

    if (connection == null) {
      System.out.println("Connection is null.");
      return false;
    }

    try {
      stmt = connection.createStatement();
    } catch (SQLException e) {
      System.out.println("Failed to access database.");
      return false;
    }

    // if you drop tables, drop them in the order from last created to first created
    // Drop tables
    dropExistingTable("MEDEQUIPREQ");
    dropExistingTable("SERVICEREQUEST");
    dropExistingTable("LABRESULT");
    dropExistingTable("MEALSERVICE");
    dropExistingTable("MEDICALEQUIPMENT");
    dropExistingTable("PATIENTS");
    dropExistingTable("EMPLOYEES");
    dropExistingTable("LOCATION");

    try {
      // Now recreate in the opposite order
      stmt.execute(
          "CREATE TABLE Location ("
              + "nodeID VARCHAR(15),"
              + "xcoord INTEGER,"
              + "ycoord INTEGER ,"
              + "floor VARCHAR(10),"
              + "building VARCHAR(20),"
              + "nodeType VARCHAR(5),"
              + "longName VARCHAR(50),"
              + "shortName Varchar(50),"
              + "constraint LOCATION_PK Primary Key (nodeID))");

      stmt.execute(
          "CREATE TABLE EMPLOYEES("
              + "employeeID VARCHAR(15),"
              + "name VARCHAR(50),"
              + "accessType VARCHAR(15),"
              + "username VARCHAR(20),"
              + "password VARCHAR(20),"
              + "CONSTRAINT EMPLOYEES_PK PRIMARY KEY (employeeID),"
              + "CONSTRAINT ACCESSTYPE_VAL CHECK (accessType in ('ADMIN', 'DOCTOR', 'NURSE')))");

      stmt.execute(
          "CREATE TABLE PATIENTS("
              + "patientID VARCHAR(15),"
              + "name VARCHAR(50),"
              + "location VARCHAR(15),"
              + "CONSTRAINT PATIENTS_PK PRIMARY KEY (patientID),"
              + "CONSTRAINT LOCATION_FK FOREIGN KEY (location) REFERENCES LOCATION(nodeID))");

      stmt.execute(
          "CREATE TABLE MEDICALEQUIPMENT ("
              + "itemID VARCHAR(15),"
              + "type VARCHAR(20),"
              + "status VARCHAR(20) DEFAULT 'Available',"
              + "currentLocation VARCHAR(15),"
              + "constraint MEDEQUIPMENT_PK Primary Key (itemID),"
              + "constraint MEDEQUIPMENT_CURRENTLOC_FK Foreign Key (currentLocation) References LOCATION(nodeID),"
              + "constraint medEquipmentStatusVal check (status in ('In-Use', 'Available')))");

      stmt.execute(
          "CREATE TABLE MEALSERVICE ("
              + "itemID VARCHAR(50),"
              + "type VARCHAR(50),"
              + "status VARCHAR(50) DEFAULT 'Available',"
              + "currentLocation VARCHAR(15),"
              + "constraint MEALSERVICE_PK Primary Key (itemID),"
              + "constraint MEALSERVICE_CURRENTLOC_FK Foreign Key (currentLocation) References LOCATION(nodeID),"
              + "constraint mealStatusVal check (status in ('In-Use', 'Available')))");

      stmt.execute(
          "CREATE TABLE LABRESULT ("
              + "itemID VARCHAR(50),"
              + "type VARCHAR(50),"
              + "status VARCHAR(50) DEFAULT 'Available',"
              + "currentLocation VARCHAR(15),"
              + "constraint LABRESULTS_PK Primary Key (itemID),"
              + "constraint LABRESULTS_CURRENTLOC_FK Foreign Key (currentLocation) References LOCATION(nodeID),"
              + "constraint labResultsStatusVal check (status in ('PROCESSING', 'DONE')))");

      stmt.execute(
          "CREATE TABLE SERVICEREQUEST ("
              + "requestID VARCHAR(15),"
              + "type VARCHAR(20),"
              + "status VARCHAR(20),"
              + "issuerID VARCHAR(15),"
              + "handlerID VARCHAR(15),"
              + "targetLocationID Varchar(15),"
              + "constraint SERVICEREQUEST_PK Primary Key (requestID),"
              + "constraint TARGETLOC_FK Foreign Key (targetLocationID) References LOCATION(nodeID),"
              + "constraint statusVal check (status in ('UNASSIGNED', 'PROCESSING', 'DONE')))");

      stmt.execute(
          "CREATE TABLE MEDEQUIPREQ ("
              + "requestID VARCHAR(15),"
              + "equipmentID VARCHAR(15),"
              + "constraint MEDEQUIPREQ_PK Primary Key (requestID),"
              + "constraint REQUEST_FK Foreign Key (requestID) References SERVICEREQUEST(requestID),"
              + "constraint EQUIPMENT_FK Foreign Key (equipmentID) References MEDICALEQUIPMENT(itemID))");

    } catch (SQLException e) {
      System.out.println("Failed to create tables");
      return false;
    }
    return true;
  }

  private void dropExistingTable(String tableName) {
    try {
      Statement stmt = connection.createStatement();
      stmt.execute("DROP TABLE " + tableName);
    } catch (SQLException e) {
      System.out.println("Failed to drop " + tableName + " as it does not exist.");
    }
  }

  public boolean populateLocationTable() {
    try {
      List<Location> tempLoc = locCSV.readLocCSV();

      for (Location info : tempLoc) {
        PreparedStatement pstmt =
            connection.prepareStatement(
                "INSERT INTO Location (nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName) values (?, ?, ?, ?, ?, ?, ?, ?)");
        pstmt.setString(1, info.getNodeID());
        pstmt.setInt(2, info.getXcoord());
        pstmt.setInt(3, info.getYcoord());
        pstmt.setString(4, info.getFloor());
        pstmt.setString(5, info.getBuilding());
        pstmt.setString(6, info.getNodeType());
        pstmt.setString(7, info.getLongName());
        pstmt.setString(8, info.getShortName());

        // insert it
        pstmt.executeUpdate();
        connection.commit();
      }

    } catch (SQLException e) {
      System.out.println("Failed to populate LOCATION table");
      return false;
    } catch (IOException e) {
      System.out.println("Failed to read CSV");
      return false;
    }
    return true;
  }

  public boolean populateEmployeeTable() {
    try {
      List<Employee> employeeList = employeeCSV.readEmployeeCSV();

      for (Employee info : employeeList) {
        PreparedStatement pstmt =
            connection.prepareStatement(
                "INSERT INTO EMPLOYEES (employeeID, name, accessType, username, password) values (?, ?, ?, ?, ?)");
        pstmt.setString(1, info.getEmployeeID());
        pstmt.setString(2, info.getName());
        pstmt.setString(3, info.getAccesstype().toString());
        pstmt.setString(4, info.getUsername());
        pstmt.setString(5, info.getPassword());

        // insert it
        pstmt.executeUpdate();
        connection.commit();
      }

    } catch (SQLException e) {
      System.out.println("Failed to populate LOCATION table");
      return false;
    } catch (IOException e) {
      System.out.println("Failed to read CSV");
      return false;
    }
    return true;
  }

  public boolean populateMedicalEquipmentTable() {
    try {
      List<MedicalEquipment> tempMedicalEquipment =
          medicalEquipmentControlCSV.readMedicalEquipmentCSV();

      for (MedicalEquipment info : tempMedicalEquipment) {
        PreparedStatement pstmt =
            connection.prepareStatement(
                "INSERT INTO MEDICALEQUIPMENT (ITEMID, TYPE, STATUS, CURRENTLOCATION) "
                    + "values (?, ?, ?, ?)");
        pstmt.setString(1, info.getItemID());
        pstmt.setString(2, info.getType());
        pstmt.setString(3, info.getStatus());
        pstmt.setString(4, info.getCurrentLocation().getNodeID());

        // insert it
        pstmt.executeUpdate();
      }
    } catch (IOException | SQLException e) {
      System.out.println("Failed to populate MedicalEquipment table");
      return false;
    }
    return true;
  }

  public boolean populateServiceRequestTable() {
    try {
      List<ServiceRequest> requestList = serviceControlCSV.readServiceRequestCSV();

      for (ServiceRequest request : requestList) {
        PreparedStatement pstmt =
            connection.prepareStatement(
                "INSERT INTO SERVICEREQUEST (requestID, type, status, issuerID, handlerID, targetLocationID)"
                    + "values (?, ?, ?, ?, ?, ?)");
        pstmt.setString(1, request.getRequestID());
        pstmt.setString(2, request.getType().toString());
        pstmt.setString(3, request.getStatus().toString());
        if (request.getIssuer() == null) {
          pstmt.setString(4, "null");
        } else {
          pstmt.setString(4, request.getIssuer().getEmployeeID());
        }
        if (request.getHandler() == null) {
          pstmt.setString(5, "null");
        } else {
          pstmt.setString(5, request.getHandler().getEmployeeID());
        }
        pstmt.setString(6, request.getTargetLocation().getNodeID());

        // insert it
        pstmt.executeUpdate();
        connection.commit();
      }

    } catch (SQLException e) {
      System.out.println("Failed to populate ServiceRequest table");
      return false;
    } catch (IOException e) {
      System.out.println("Failed to read CSV");
      return false;
    }
    return true;
  }

  public boolean populateMedicalEquipmentServiceRequestTable() {
    try {
      List<MedicalEquipmentDeliveryRequest> requestList = medEqReqControlCSV.readMedReqCSV();

      for (MedicalEquipmentDeliveryRequest medEqRequest : requestList) {
        PreparedStatement pstmt =
            connection.prepareStatement(
                "INSERT INTO MEDEQUIPREQ (requestID, equipmentID) values (?, ?)");
        pstmt.setString(1, medEqRequest.getRequestID());
        pstmt.setString(2, medEqRequest.getEquipmentID());

        // insert it
        pstmt.executeUpdate();
        connection.commit();
      }

    } catch (SQLException e) {
      System.out.println("Failed to populate MedEquipReq table");
      return false;
    } catch (IOException e) {
      System.out.println("Failed to read MedEquipReq.csv");
      return false;
    }
    return true;
  }
}
