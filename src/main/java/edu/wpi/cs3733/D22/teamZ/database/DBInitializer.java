package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class DBInitializer {
  private final LocationControlCSV locCSV;
  private final EmployeeControlCSV employeeCSV;
  private final PatientControlCSV patientCSV;
  private final MedicalEquipmentControlCSV medicalEquipmentControlCSV;
  private final ServiceRequestControlCSV serviceControlCSV;
  private final MedEqReqControlCSV medEqReqControlCSV;
  private final MealServReqControlCSV mealServReqControlCSV;
  private final CleaningReqControlCSV cleaningReqControlCSV;
  private final EquipmentPurchaseRequestControlCSV purchaseReqControlCSV;
  private final SecurityRequestControlCSV securityRequestControlCSV;
  private final LaundryServiceRequestControlCSV laundryServiceRequestControlCSV;
  private final LanguageInterpreterRequestControlCSV languageInterpreterRequestControlCSV;
  private final ComputerRequestControlCSV computerRequestControlCSV;
  private final FacadeDAO dao = FacadeDAO.getInstance();

  static Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();
  // DatabaseConnection.getConnection();

  public DBInitializer() {
    connection = EnumDatabaseConnection.CONNECTION.getConnection();
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
    File patientData =
        new File(
            System.getProperty("user.dir") + System.getProperty("file.separator") + "Patients.csv");
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
    File mealServReqData =
        new File(
            System.getProperty("user.dir")
                + System.getProperty("file.separator")
                + "MealServReq.csv");
    File cleanReqData =
        new File(
            System.getProperty("user.dir")
                + System.getProperty("file.separator")
                + "CleaningReq.csv");
    File purchaseReqData =
        new File(
            System.getProperty("user.dir")
                + System.getProperty("file.separator")
                + "PurchaseReq.csv");
    File securityReqData =
        new File(
            System.getProperty("user.dir")
                + System.getProperty("file.separator")
                + "SecurityReq.csv");
    File laundryReqData =
        new File(
            System.getProperty("user.dir")
                + System.getProperty("file.separator")
                + "LaundryServiceRequest.csv");
    File languageReqData =
        new File(
            System.getProperty("user.dir")
                + System.getProperty("file.separator")
                + "LanguageReq.csv");
    File computerReqData =
        new File(
            System.getProperty("user.dir")
                + System.getProperty("file.separator")
                + "ComputerReq.csv");

    locCSV = new LocationControlCSV(locData);
    employeeCSV = new EmployeeControlCSV(employeeData);
    patientCSV = new PatientControlCSV(patientData);
    medicalEquipmentControlCSV = new MedicalEquipmentControlCSV(medicalEquipmentData);
    serviceControlCSV = new ServiceRequestControlCSV(serviceRequestData);
    medEqReqControlCSV = new MedEqReqControlCSV(medEquipReqData);
    mealServReqControlCSV = new MealServReqControlCSV(mealServReqData);
    cleaningReqControlCSV = new CleaningReqControlCSV(cleanReqData);
    purchaseReqControlCSV = new EquipmentPurchaseRequestControlCSV(purchaseReqData);
    securityRequestControlCSV = new SecurityRequestControlCSV(securityReqData);
    laundryServiceRequestControlCSV = new LaundryServiceRequestControlCSV(laundryReqData);
    languageInterpreterRequestControlCSV =
        new LanguageInterpreterRequestControlCSV(languageReqData);
    computerRequestControlCSV = new ComputerRequestControlCSV(computerReqData);
  }

  public boolean createTables() {
    if (connection == null) {
      System.out.println("Connection is null.");
      return false;
    }

    Statement stmt;
    try {
      stmt = connection.createStatement();
    } catch (SQLException e) {
      System.out.println("Failed to access database.");
      return false;
    }

    // if you drop tables, drop them in the order from last created to first created
    // Drop tables
    dropExistingTable("LANGUAGEINTERPRETERREQUEST");
    dropExistingTable("COMPUTERREQUEST");
    dropExistingTable("SECURITYREQUEST");
    dropExistingTable("LAUNDRYREQUEST");
    dropExistingTable("EQUIPMENTPURCHASE");
    dropExistingTable("GIFTSERVICEREQUEST");
    dropExistingTable("MEALSERVICEREQUEST");
    dropExistingTable("EXTERNALTRANSPORTREQUEST");
    dropExistingTable("CLEANINGREQUEST");
    dropExistingTable("MEDEQUIPREQ");
    dropExistingTable("LABREQUEST");
    dropExistingTable("MEALSERVICE");
    dropExistingTable("SERVICEREQUEST");
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
    } catch (SQLException e) {
      System.out.println("Failed to create location tables");
      return false;
    }
    try {

      stmt.execute(
          "CREATE TABLE EMPLOYEES("
              + "employeeID VARCHAR(15),"
              + "name VARCHAR(50),"
              + "accessType VARCHAR(15),"
              + "username VARCHAR(20),"
              + "password VARCHAR(20),"
              + "CONSTRAINT EMPLOYEES_PK PRIMARY KEY (employeeID),"
              + "CONSTRAINT ACCESSTYPE_VAL CHECK (accessType in ('ADMIN', 'DOCTOR', 'NURSE')))");
    } catch (SQLException e) {
      System.out.println("Failed to create employee tables");
      return false;
    }

    try {
      stmt.execute(
          "CREATE TABLE PATIENTS("
              + "patientID VARCHAR(15),"
              + "name VARCHAR(50),"
              + "location VARCHAR(15),"
              + "CONSTRAINT PATIENTS_PK PRIMARY KEY (patientID),"
              + "CONSTRAINT LOCATION_FK FOREIGN KEY (location) REFERENCES LOCATION(nodeID))");
    } catch (SQLException e) {
      System.out.println("Failed to create patient tables");
      return false;
    }

    try {
      stmt.execute(
          "CREATE TABLE MEDICALEQUIPMENT ("
              + "equipmentID VARCHAR(15),"
              + "type VARCHAR(20),"
              + "status VARCHAR(20) DEFAULT 'CLEAN',"
              + "currentLocation VARCHAR(15),"
              + "constraint MEDEQUIPMENT_PK Primary Key (equipmentID),"
              + "constraint MEDEQUIPMENT_CURRENTLOC_FK Foreign Key (currentLocation) References LOCATION(nodeID),"
              + "constraint medEquipmentStatusVal check (status in ('CLEAN', 'CLEANING', 'DIRTY', 'INUSE')))");
    } catch (SQLException e) {
      System.out.println("Failed to create medical equipment tables");
      return false;
    }

    try {
      stmt.execute(
          "CREATE TABLE SERVICEREQUEST ("
              + "requestID VARCHAR(15),"
              + "type VARCHAR(20),"
              + "status VARCHAR(20),"
              + "issuerID VARCHAR(15),"
              + "handlerID VARCHAR(15),"
              + "targetLocationID Varchar(15),"
              + "constraint SERVICEREQUEST_PK Primary Key (requestID),"
              + "constraint ISSUER_FK Foreign Key (issuerID) References EMPLOYEES(employeeID),"
              + "constraint HANDLER_FK Foreign Key (handlerID) References EMPLOYEES(employeeID),"
              + "constraint TARGETLOC_FK Foreign Key (targetLocationID) References LOCATION(nodeID),"
              + "constraint statusVal check (status in ('UNASSIGNED', 'PROCESSING', 'DONE')))");
    } catch (SQLException e) {
      System.out.println("Failed to create service request tables");
      return false;
    }

    try {
      stmt.execute(
          "CREATE TABLE MEDEQUIPREQ ("
              + "requestID VARCHAR(15),"
              + "equipmentID VARCHAR(15),"
              + "constraint MEDEQUIPREQ_PK Primary Key (requestID),"
              + "constraint MEDEQUIPREQ_FK Foreign Key (requestID) References SERVICEREQUEST(requestID),"
              + "constraint EQUIPMENT_FK Foreign Key (equipmentID) References MEDICALEQUIPMENT(equipmentID))");
    } catch (SQLException e) {
      System.out.println("Failed to create medical equipment request tables");
      return false;
    }

    try {
      stmt.execute(
          "CREATE TABLE LABREQUEST ("
              + "requestID VARCHAR(15),"
              + "labType VARCHAR(50),"
              + "constraint LABREQUEST_PK Primary Key (requestID),"
              + "constraint LABREQUEST_FK Foreign Key (requestID) References SERVICEREQUEST(requestID))");
    } catch (SQLException e) {
      System.out.println("Failed to create lab request tables");
      return false;
    }

    try {
      stmt.execute(
          "CREATE TABLE MEALSERVICE ("
              + "itemID VARCHAR(50),"
              + "type VARCHAR(50),"
              + "status VARCHAR(50) DEFAULT 'Available',"
              + "currentLocation VARCHAR(15),"
              + "constraint MEALSERVICE_PK Primary Key (itemID),"
              + "constraint MEALSERVICE_CURRENTLOC_FK Foreign Key (currentLocation) References LOCATION(nodeID),"
              + "constraint mealStatusVal check (status in ('In-Use', 'Available')))");
    } catch (SQLException e) {
      System.out.println("Failed to create meal service tables");
      return false;
    }

    try {
      stmt.execute(
          "CREATE TABLE CLEANINGREQUEST ("
              + "requestID VARCHAR(15),"
              + "type VARCHAR(50),"
              + "constraint CLEANINGREQUEST_PK Primary Key (requestID),"
              + "constraint CLEANINGREQUEST_FK Foreign Key (requestID) References SERVICEREQUEST(requestID))");
    } catch (SQLException e) {
      System.out.println("Failed to create cleaning request tables");
      return false;
    }

    try {
      stmt.execute(
          "CREATE TABLE EXTERNALTRANSPORTREQUEST ("
              + "requestID VARCHAR(15),"
              + "patientID VARCHAR(15),"
              + "patientName VARCHAR(50),"
              + "destination VARCHAR(50),"
              + "departureDate DATE,"
              + "constraint TRANSPORTREQUEST_PK PRIMARY KEY (requestID),"
              + "constraint TRANSPORTREQUESTID_FK FOREIGN KEY (requestID) REFERENCES SERVICEREQUEST(requestid))");
    } catch (SQLException e) {
      System.out.println("Failed to create external patient transport tables");
      return false;
    }

    try {
      stmt.execute(
          "CREATE TABLE GIFTSERVICEREQUEST ("
              + "requestID VARCHAR(15),"
              + "patientName VARCHAR(50),"
              + "patientID VARCHAR(15),"
              + "giftType VARCHAR(25),"
              + "constraint GIFTSERVICEREQUEST_PK PRIMARY KEY (requestID),"
              + "constraint GIFTSERVICEREQUEST_FK FOREIGN KEY (requestID) REFERENCES SERVICEREQUEST(requestid),"
              + "constraint GIFTSERVICEREQUESTPATIENT_FK FOREIGN KEY (patientID) REFERENCES PATIENTS(patientID))");
    } catch (SQLException e) {
      System.out.println("Failed to create gift service request tables");
      return false;
    }

    try {
      stmt.execute(
          "CREATE TABLE LANGUAGEINTERPRETERREQUEST ("
              + "requestID VARCHAR(15),"
              + "patientName VARCHAR(50),"
              + "patientID VARCHAR(15),"
              + "language VARCHAR(25),"
              + "constraint LANGUAGEINTERPRETERREQUEST_PK PRIMARY KEY (requestID),"
              + "constraint LANGUAGEINTERPRETERREQUEST_FK FOREIGN KEY (requestID) REFERENCES SERVICEREQUEST(requestid),"
              + "constraint LANGUAGEINTERPRETERREQUESTPATIENT_FK FOREIGN KEY (patientID) REFERENCES PATIENTS(patientID))");
    } catch (SQLException e) {
      System.out.println("Failed to create language interpreter request tables");
      return false;
    }

    try {
      stmt.execute(
          "CREATE TABLE MEALSERVICEREQUEST ("
              + "requestID VARCHAR(15),"
              + "patientID VARCHAR(15),"
              + "drink VARCHAR(50),"
              + "entree VARCHAR(50),"
              + "side VARCHAR(50),"
              + "constraint MEALSERVICEREQUEST_PK PRIMARY KEY (requestID),"
              + "constraint MEALSERVICEREQUEST_FK FOREIGN KEY (requestID) REFERENCES SERVICEREQUEST(requestid),"
              + "constraint MEALSERVICEREQUESTPATIENT_FK FOREIGN KEY (patientID) REFERENCES PATIENTS(patientID))");
    } catch (SQLException e) {
      System.out.println("Failed to create meal service request tables");
      return false;
    }

    try {
      stmt.execute(
          "CREATE TABLE EQUIPMENTPURCHASE ("
              + "requestID VARCHAR(15),"
              + "equipmentType VARCHAR(15),"
              + "paymentMethod VARCHAR(20),"
              + "constraint EQUIPMENTPURCHASE_PK PRIMARY KEY (requestID),"
              + "constraint EQUIPMENTPURCHASE_FK FOREIGN KEY (requestID) REFERENCES SERVICEREQUEST(REQUESTID))");
    } catch (SQLException e) {
      System.out.println("Failed to create equipment purchase request table");
      return false;
    }

    try {
      stmt.execute(
          "CREATE TABLE SECURITYREQUEST ("
              + "requestID VARCHAR(15),"
              + "urgency VARCHAR(9),"
              + "reason VARCHAR(40),"
              + "constraint SECURITYREQUEST_PK PRIMARY KEY (requestID),"
              + "constraint SECURITYREQUEST_FK FOREIGN KEY (requestID) REFERENCES SERVICEREQUEST(REQUESTID))");
    } catch (SQLException e) {
      System.out.println("Failed to create security request table");
      return false;
    }

    try {
      stmt.execute(
          "CREATE TABLE LAUNDRYREQUEST ("
              + "requestID VARCHAR(15),"
              + "laundryType VARCHAR(20),"
              + "laundryStatus VARCHAR(20),"
              + "constraint LAUNDRYREQUEST_PK PRIMARY KEY (requestID),"
              + "constraint LAUNDRYREQUEST_FK FOREIGN KEY (requestID) REFERENCES SERVICEREQUEST(REQUESTID))");
    } catch (SQLException e) {
      System.out.println("Failed to create laundry service request table");
      return false;
    }

    try {
      stmt.execute(
          "CREATE TABLE COMPUTERREQUEST ("
              + "requestID VARCHAR(15),"
              + "operatingSystem VARCHAR(25),"
              + "problemDesc VARCHAR(100),"
              + "constraint COMPUTERREQUEST_PK PRIMARY KEY (requestID),"
              + "constraint COMPUTERREQUEST_FK FOREIGN KEY (requestID) REFERENCES SERVICEREQUEST(REQUESTID))");
    } catch (SQLException e) {
      System.out.println("Failed to create computer service request table");
      return false;
    }

    return true;
  }

  private void dropExistingTable(String tableName) {
    connection = EnumDatabaseConnection.CONNECTION.getConnection();
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
        dao.addLocation(info);
        /*PreparedStatement pstmt =
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
        connection.commit();*/
      }
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
        dao.addEmployee(info);
        /*PreparedStatement pstmt =
            connection.prepareStatement(
                "INSERT INTO EMPLOYEES (employeeID, name, accessType, username, password) values (?, ?, ?, ?, ?)");
        pstmt.setString(1, info.getEmployeeID());
        pstmt.setString(2, info.getName());
        pstmt.setString(3, info.getAccesstype().toString());
        pstmt.setString(4, info.getUsername());
        pstmt.setString(5, info.getPassword());

        // insert it
        pstmt.executeUpdate();
        connection.commit();*/
      }

    } catch (IOException e) {
      System.out.println("Failed to read CSV");
      return false;
    }
    return true;
  }

  public boolean populatePatientTable() {
    try {
      List<Patient> patientList = patientCSV.readPatientCSV();

      for (Patient info : patientList) {
        dao.addPatient(info);
        /*PreparedStatement pstmt =
            connection.prepareStatement(
                "INSERT INTO PATIENTS (patientID, name, Location) values (?, ?, ?)");
        pstmt.setString(1, info.getPatientID());
        pstmt.setString(2, info.getName());
        pstmt.setString(3, info.getLocation().getNodeType());

        // insert it
        pstmt.executeUpdate();
        connection.commit();*/
      }

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
        dao.addMedicalEquipment(info);
        /*PreparedStatement pstmt =
            connection.prepareStatement(
                "INSERT INTO MEDICALEQUIPMENT (EQUIPMENTID, TYPE, STATUS, CURRENTLOCATION) "
                    + "values (?, ?, ?, ?)");
        pstmt.setString(1, info.getEquipmentID());
        pstmt.setString(2, info.getType());
        pstmt.setString(3, info.getStatus());
        pstmt.setString(4, info.getCurrentLocation().getNodeID());

        // insert it
        pstmt.executeUpdate();*/
      }
    } catch (IOException e) {
      System.out.println("Failed to populate MedicalEquipment table");
      return false;
    }
    return true;
  }

  public boolean populateMealServiceRequestsTable() {
    try {
      List<MealServiceRequest> tempMealServRequests = mealServReqControlCSV.readMealServReqCSV();

      for (MealServiceRequest info : tempMealServRequests) {
        //        dao.addMealServiceRequest(info);
        dao.addMealServiceRequestToDatabase(info);
        /*PreparedStatement pstmt =
            connection.prepareStatement(
                "INSERT INTO MEDICALEQUIPMENT (EQUIPMENTID, TYPE, STATUS, CURRENTLOCATION) "
                    + "values (?, ?, ?, ?)");
        pstmt.setString(1, info.getEquipmentID());
        pstmt.setString(2, info.getType());
        pstmt.setString(3, info.getStatus());
        pstmt.setString(4, info.getCurrentLocation().getNodeID());

        // insert it
        pstmt.executeUpdate();*/
      }
    } catch (IOException e) {
      System.out.println("Failed to populate MealServiceRequest table");
      return false;
    }
    return true;
  }

  public boolean populateServiceRequestTable() {
    try {
      List<ServiceRequest> requestList = serviceControlCSV.readServiceRequestCSV();

      for (ServiceRequest request : requestList) {
        dao.addServiceRequest(request);
        /*PreparedStatement pstmt =
            connection.prepareStatement(
                "INSERT INTO SERVICEREQUEST (requestID, type, status, issuerID, handlerID, targetLocationID)"
                    + "values (?, ?, ?, ?, ?, ?)");
        pstmt.setString(1, request.getRequestID());
        pstmt.setString(2, request.getType().toString());
        pstmt.setString(3, request.getStatus().toString());
        if (request.getIssuer() == null) {
          pstmt.setString(4, null);
        } else {
          pstmt.setString(4, request.getIssuer().getEmployeeID());
        }
        if (request.getHandler() == null) {
          pstmt.setString(5, null);
        } else {
          pstmt.setString(5, request.getHandler().getEmployeeID());
        }
        pstmt.setString(6, request.getTargetLocation().getNodeID());

        // insert it
        pstmt.executeUpdate();
        connection.commit();*/
      }
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
        dao.addMedicalEquipmentRequestToDatabase(medEqRequest);
        /*PreparedStatement pstmt =
            connection.prepareStatement(
                "INSERT INTO MEDEQUIPREQ (requestID, equipmentID) values (?, ?)");
        pstmt.setString(1, medEqRequest.getRequestID());
        pstmt.setString(2, medEqRequest.getEquipmentID());

        // insert it
        pstmt.executeUpdate();
        connection.commit();*/
      }

    } catch (IOException e) {
      System.out.println("Failed to read MedEquipReq.csv");
      return false;
    }
    return true;
  }

  public boolean populateCleaningServiceRequestTable() {
    try {
      List<CleaningRequest> requestList = cleaningReqControlCSV.readCleanReqCSV();

      for (CleaningRequest cleaningRequest : requestList) {
        dao.addCleaningRequestToDatabase(cleaningRequest);
      }

    } catch (IOException e) {
      System.out.println("Failed to read CleaningReq.csv");
      return false;
    }
    return true;
  }

  public boolean populateLanguageInterpreterTable() {
    try {
      List<LanguageInterpreterRequest> requestList =
          languageInterpreterRequestControlCSV.readLanguageIntepreterRequestCSV();

      for (LanguageInterpreterRequest languageInterpreterRequest : requestList) {
        dao.addLanguageInterpreterRequest(languageInterpreterRequest);
      }

    } catch (IOException e) {
      System.out.println("Failed to read LanguageInterpreter.csv");
      return false;
    }
    return true;
  }

  public boolean populateEquipmentPurchaseTable() {
    try {
      List<EquipmentPurchaseRequest> requestList =
          purchaseReqControlCSV.readEquipmentPurchaseRequestCSV();
      for (EquipmentPurchaseRequest request : requestList) {
        dao.addEquipmentPurchaseRequestToDatabase(request);
      }
    } catch (IOException e) {
      System.out.println("Failed to read PurchaseReq.csv");
      return false;
    }
    return true;
  }

  public boolean populateComputerRequestTable() {
    try {
      List<ComputerServiceRequest> requestList =
          computerRequestControlCSV.readComputerServiceRequestCSV();
      for (ComputerServiceRequest request : requestList) {
        dao.addComputerServiceRequest(request);
      }
    } catch (IOException e) {
      System.out.println("Failed to read ComputerReq.csv");
      return false;
    }
    return true;
  }

  public boolean populateSecurityRequestTable() {
    try {
      List<SecurityServiceRequest> requestList =
          securityRequestControlCSV.readSecurityServiceRequestCSV();
      for (SecurityServiceRequest request : requestList) {
        dao.addSecurityServiceRequestToDatabase(request);
      }
    } catch (IOException e) {
      System.out.println("Failed to read SecurityReq.csv");
      return false;
    }
    return true;
  }

  public boolean populateLaundryServiceRequests() {
    try {
      List<LaundryServiceRequest> laundryList =
          laundryServiceRequestControlCSV.readLaundryServiceRequestCSV();

      for (LaundryServiceRequest info : laundryList) {
        dao.addLaundryServiceRequestToDatabase(info);
      }

    } catch (IOException e) {
      System.out.println("Failed to read Laundry CSV");
      return false;
    }
    return true;
  }

  public boolean switchDatabase(String type) {
    connection = EnumDatabaseConnection.CONNECTION.getConnection();

    FacadeDAO dao = FacadeDAO.getInstance();

    // transfer all stuff to temp lists
    List<Location> tempLocation = dao.getAllLocations();
    List<Employee> tempEmployee = dao.getAllEmployees();
    List<Patient> tempPatient = dao.getAllPatients();
    List<MedicalEquipment> tempMedicalEquipment = dao.getAllMedicalEquipment();
    List<ServiceRequest> tempServiceRequests = dao.getAllServiceRequests();
    List<MedicalEquipmentDeliveryRequest> tempMedicalDeliveryRequests =
        dao.getAllMedicalEquipmentRequest();
    List<LabServiceRequest> tempLabRequest = dao.getAllLabServiceRequests();

    try {
      connection.close();
    } catch (SQLException e) {
      System.out.println("connection closed");
    }

    // change the connection type
    EnumDatabaseConnection.CONNECTION.setConnection(type);
    connection = EnumDatabaseConnection.CONNECTION.getConnection();

    // drop the tables in order of creation
    createTables();

    // bool checker
    // reinsert info into new database
    boolean val =
        dao.addLocationFromList(tempLocation)
            && dao.addEmployeeFromList(tempEmployee)
            && dao.addPatientFromList(tempPatient)
            && dao.addMedicalEquipmentFromList(tempMedicalEquipment)
            && dao.addServiceRequestFromList(tempServiceRequests)
            && dao.addMedicalEquipmentRequestFromList(tempMedicalDeliveryRequests)
            && dao.addLabRequestFromList(tempLabRequest);
    return val;
  }
}
