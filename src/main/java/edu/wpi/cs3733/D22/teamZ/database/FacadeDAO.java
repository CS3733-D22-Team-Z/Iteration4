package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.*;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class FacadeDAO {
  private LocationDAOImpl locationDAO;
  private MedicalEquipmentDAOImpl medicalEquipmentDAO;
  private MedEquipReqDAOImpl medEquipReqDAO;
  private EmployeeDAOImpl employeeDAO;
  private PatientDAOImpl patientDAO;
  private LabRequestServiceDAOImpl labRequestServiceDAO;
  private ServiceRequestDAOImpl serviceRequestDAO;

  public FacadeDAO() {
    locationDAO = new LocationDAOImpl();
    medicalEquipmentDAO = new MedicalEquipmentDAOImpl();
    medEquipReqDAO = new MedEquipReqDAOImpl();
    employeeDAO = new EmployeeDAOImpl();
    patientDAO = new PatientDAOImpl();
    labRequestServiceDAO = new LabRequestServiceDAOImpl();
    serviceRequestDAO = new ServiceRequestDAOImpl();
  }

  // Get All methods
  /**
   * Gets all of the locations in the database
   *
   * @return List of locations
   */
  public List<Location> getAllLocations() {
    return locationDAO.getAllLocations();
  }
  /**
   * Get all MedicalEquipment in the database
   *
   * @return list of MedicalEquipment
   */
  public List<MedicalEquipment> getAllMedicalEquipment() {
    return medicalEquipmentDAO.getAllMedicalEquipment();
  }
  /**
   * Gets all MedicalEquipmentRequests from database
   *
   * @return list of MedicalEquipmentRequests
   */
  public List<MedicalEquipmentDeliveryRequest> getAllMedicalEquipmentRequest() {
    return medEquipReqDAO.getAllMedEquipReq();
  }
  /**
   * Returns all the service requests currently stored
   *
   * @return A List of all ServiceRequest objects in the database
   */
  public List<ServiceRequest> getAllServiceRequests() {
    return serviceRequestDAO.getAllServiceRequests();
  }
  /**
   * Gets all lab service requests
   *
   * @return list of lab service requests
   * @throws SQLException
   */
  public List<LabServiceRequest> getAllLabServiceRequests() {
    return labRequestServiceDAO.getAllLabServiceRequests();
  }
  /**
   * Gets all of the employees in the database
   *
   * @return List of employees
   */
  public List<Employee> getAllEmployees() {
    return employeeDAO.getAllEmployees();
  }
  /**
   * Gets all of the patients in the database
   *
   * @return List of patients
   */
  public List<Patient> getAllPatients() {
    return patientDAO.getAllPatients();
  }

  // Get By ID methods
  /**
   * Gets ONE lcoation from the database based on the provided nodeID
   *
   * @param id
   * @return Location object with provided nodeID
   */
  public Location getLocationByID(String id) {
    return locationDAO.getLocationByID(id);
  }
  /**
   * Get MedicalEquipment with the given ID
   *
   * @param id ID of MedicalEquipment to be fetched
   * @return MedicalEquipment with the given ID
   */
  public MedicalEquipment getMedicalEquipmentByID(String id) {
    return medicalEquipmentDAO.getMedicalEquipmentByID(id);
  }
  /**
   * Gets ONE Employee from the database based on the provided EmployeeID
   *
   * @param id
   * @return Employee object with provided employeeID
   */
  public Employee getEmployeeByID(String id) {
    return employeeDAO.getEmployeeByID(id);
  }
  /**
   * Gets ONE patient from the database based on the provided patientID
   *
   * @param id
   * @return Patient object with provided patientID
   */
  public Patient getPatientByID(String id) {
    return patientDAO.getPatientByID(id);
  }
  /**
   * Returns a single ServiceRequest object that is stored in the database and has the id that is
   * provided
   *
   * @param id The id of the service request with the given id in the database
   * @return A ServiceRequest object representing the request in the database with the given
   *     serviceRequestID
   */
  public ServiceRequest getServiceRequestByID(String id) {
    return serviceRequestDAO.getServiceRequestByID(id);
  }
  /**
   * Gets a MedicalEquipmentRequest of the given requestID
   *
   * @param id ID of request to be fetched
   * @return MedicalEquipmentRequest of given ID, null if not found
   */
  public MedicalEquipmentDeliveryRequest getMedicalEquipmentRequestByID(String id) {
    return medEquipReqDAO.getMedEquipReqByID(id);
  }
  /**
   * Get a LabServiceRequest with provided requestID
   *
   * @param id
   * @return LabServiceRequest object with given ID
   */
  public LabServiceRequest getLabServiceRequestByID(String id) {
    return labRequestServiceDAO.getLabRequestByID(id);
  }

  // Add methods
  /**
   * Adds a new location to database. Will automatically check if already in database
   *
   * @param loc
   * @return True if successful, false if not
   */
  public boolean addLocation(Location loc) {
    return locationDAO.addLocation(loc);
  }
  /**
   * Adds MedicalEquipment to the database
   *
   * @param medicalEquipment MedicalEquipment to be added
   * @return True if successful, false otherwise
   */
  public boolean addMedicalEquipment(MedicalEquipment medicalEquipment) {
    return medicalEquipmentDAO.addMedicalEquipment(medicalEquipment);
  }
  /**
   * Adds a new Patient to database. Will automatically check if already in database
   *
   * @param patient
   * @return True if successful, false if not
   */
  public boolean addPatient(Patient patient) {
    return patientDAO.addPatient(patient);
  }
  /**
   * Adds a new Employee to database. Will automatically check if already in database
   *
   * @param employee
   * @return True if successful, false if not
   */
  public boolean addEmployee(Employee employee) {
    return employeeDAO.addEmployee(employee);
  }
  /**
   * Adds the given ServiceRequest object to the database
   *
   * @param serviceRequest The request to be added
   * @return true if successfully added, false otherwise
   */
  private boolean addServiceRequest(ServiceRequest serviceRequest) {
    return serviceRequestDAO.addServiceRequest(serviceRequest);
  }
  /**
   * Adds a MedicalEquipmentRequest to the database
   *
   * @param medicalEquipmentDeliveryRequest MedicalEquipmentRequest to be added
   * @return True if successful, false otherwise
   */
  public boolean addMedicalEquipmentRequest(
      MedicalEquipmentDeliveryRequest medicalEquipmentDeliveryRequest) {
    boolean val =
        serviceRequestDAO.addServiceRequest(medicalEquipmentDeliveryRequest)
            && medEquipReqDAO.addMedEquipReq(medicalEquipmentDeliveryRequest);
    return val;
  }
  /**
   * Adds a LabServiceRequest to the database
   *
   * @param labServiceRequest LabServiceRequest to be added
   * @return True if successful, false otherwise
   */
  public boolean addLabServiceRequest(LabServiceRequest labServiceRequest) {
    boolean val =
        serviceRequestDAO.addServiceRequest(labServiceRequest)
            && labRequestServiceDAO.addLabRequest(labServiceRequest);
    return val;
  }

  // Delete methods
  /**
   * Deletes a location from database. Will automatically check if exists in database already
   *
   * @param loc
   * @return True if successful, false if not
   */
  public boolean deleteLocation(Location loc) {
    return locationDAO.deleteLocation(loc);
  }
  /**
   * Deletes MedicalEquipment in the database
   *
   * @param medicalEquipment MedicalEquipment to be deleted
   * @return True if successful, false otherwise
   */
  public boolean deleteMedicalEquipment(MedicalEquipment medicalEquipment) {
    return medicalEquipmentDAO.deleteMedicalEquipment(medicalEquipment);
  }
  /**
   * Deletes a patient from database. Will automatically check if exists in database
   *
   * @param patient
   * @return True if successful, false if not
   */
  public boolean deletePatient(Patient patient) {
    return patientDAO.deletePatient(patient);
  }
  /**
   * Deletes a Employee from database. Will automatically check if exists in database
   *
   * @param employee
   * @return True if successful, false if not
   */
  public boolean deleteEmployee(Employee employee) {
    return employeeDAO.deleteEmployee(employee);
  }
  /**
   * Takes a ServiceRequest object and deletes the respective one from the database with the same
   * requestID
   *
   * @param serviceRequest The request to be deleted
   * @return true if the deletion was successful, false otherwise
   */
  private boolean deleteServiceRequest(ServiceRequest serviceRequest) {
    return serviceRequestDAO.deleteServiceRequest(serviceRequest);
  }
  /**
   * Deletes MedicalEquipmentRequest from database
   *
   * @param medicalEquipmentDeliveryRequest MedicalEquipmentRequest to be deleted
   * @return True if successful, false otherwise
   */
  public boolean deleteMedicalServiceRequest(
      MedicalEquipmentDeliveryRequest medicalEquipmentDeliveryRequest) {
    boolean val =
        medEquipReqDAO.deleteMedEquipReq(medicalEquipmentDeliveryRequest)
            && serviceRequestDAO.deleteServiceRequest(medicalEquipmentDeliveryRequest);
    return val;
  }
  /**
   * Deletes a LabServiceRequest from the database
   *
   * @param labServiceRequest LabServiceRequest to be deleted
   * @return True if successful, false otherwise
   */
  public boolean deleteLabServiceRequest(LabServiceRequest labServiceRequest) {
    boolean val =
        labRequestServiceDAO.deleteLabRequest(labServiceRequest)
            && serviceRequestDAO.deleteServiceRequest(labServiceRequest);
    return labRequestServiceDAO.deleteLabRequest(labServiceRequest);
  }

  // Update methods
  /**
   * Updates a location in the database. Will automatically check if exists in database
   *
   * @param loc
   * @return True if successful, false if not
   */
  public boolean updateLocation(Location loc) {
    return locationDAO.updateLocation(loc);
  }
  /**
   * Updates existing MedicalEquipment in the database with an updated MedicalEquipment
   *
   * @param medicalEquipment Updated MedicalEquipment
   * @return True if successful, false otherwise
   */
  public boolean updateMedicalEquipment(MedicalEquipment medicalEquipment) {
    return medicalEquipmentDAO.updateMedicalEquipment(medicalEquipment);
  }
  /**
   * Updates a Employee in the database. Will automatically check if exists in database
   *
   * @param employee
   * @return True if successful, false if not
   */
  public boolean updateEmployee(Employee employee) {
    return employeeDAO.updateEmployee(employee);
  }
  /**
   * Updates a patient in the database. Will automatically check if exists in database
   *
   * @param patient
   * @return True if successful, false if not
   */
  public boolean updatePatient(Patient patient) {
    return patientDAO.updatePatient(patient);
  }
  /**
   * Update a ServiceRequest object in database and list of service requests
   *
   * @param serviceRequest ServiceRequest object that stores updated information
   * @return True if success, false otherwise
   */
  private boolean updateServiceRequest(ServiceRequest serviceRequest) {
    return serviceRequestDAO.updateServiceRequest(serviceRequest);
  }
  // not in use rn
  /**
   * Updates an existing MedicalEquipmentRequest in the database with the given request
   *
   * @param medicalEquipmentDeliveryRequest MedicalEquipmentRequest with updated information
   * @return True if successful, false otherwise
   */
  public boolean updateMedicalEquipmentRequest(
      MedicalEquipmentDeliveryRequest medicalEquipmentDeliveryRequest) {
    return updateServiceRequest(medicalEquipmentDeliveryRequest)
        && medEquipReqDAO.updateMedEquipReq(medicalEquipmentDeliveryRequest);
  }
  /**
   * Updates an existing LabServiceRequest in database with new request
   *
   * @param labServiceRequest LabServiceRequest to be updated
   * @return True if successful, false otherwise
   */
  public boolean updateLabServiceRequest(LabServiceRequest labServiceRequest) {
    return updateServiceRequest(labServiceRequest)
        && labRequestServiceDAO.updateLabRequest(labServiceRequest);
  }

  // Import methods
  /**
   * Imports data from CSV into location database
   *
   * @param locData
   * @return number of times there are conflicts when trying to import
   */
  public int importLocationsFromCSV(File locData) {
    return locationDAO.importLocationFromCSV(locData);
  }
  /**
   * Imports the MedicalEquipment into the database from specified file location for csv
   *
   * @param equipmentData file location for csv
   * @return number of conflicts when importing
   */
  public int importMedicalEquipmentFromCSV(File equipmentData) {
    return medicalEquipmentDAO.importMedicalEquipmentFromCSV(equipmentData);
  }
  /**
   * Imports the Employee csv into the Employee table
   *
   * @param employeeData file location of employee csv
   * @return number of conflicts when inserting
   */
  public int importEmployeesFromCSV(File employeeData) {
    return employeeDAO.importEmployeesFromCSV(employeeData);
  }
  /**
   * Imports Patients to database from a specified file location for the csv
   *
   * @param patientData file location for csv
   * @return number of conflicts when importing
   */
  public int importPatientsFromCSV(File patientData) {
    return patientDAO.importPatientsFromCSV(patientData);
  }
  /**
   * Import ServiceRequest to database from a specified file location for csv
   *
   * @param serviceRequestData file location for csv
   * @return number of conflicts when importing
   */
  public int importServiceRequestsFromCSV(File serviceRequestData) {
    return serviceRequestDAO.importServiceRequestsFromCSV(serviceRequestData);
  }
  /**
   * Imports MedicalEquipmentRequests into database from specified file location for csv
   *
   * @param equipmentRequestsData file location of csv
   * @return number of conflicts during import
   */
  public int importMedicalEquipmentRequestsFromCSV(File equipmentRequestsData) {
    return medEquipReqDAO.importMedEquipReqFromCSV(equipmentRequestsData);
  }
  // TODO not yet created labRequestControlCSV
  /**
   * Imports all LabServiceRequests in specified file location of csv into the database
   *
   * @param labRequestData file location of csv
   * @return True if successful, false otherwise
   */
  public int importLabServiceRequestsFromCSV(File labRequestData) {
    return labRequestServiceDAO.importLabRequestFromCSV(labRequestData);
  }

  // Export methods
  /**
   * Exports the Location table into a csv file to the working directory
   *
   * @return True if successful, false if not
   */
  public boolean exportLocationsToCSV(File locData) {
    return locationDAO.exportToLocationCSV(locData);
  }
  /**
   * Exports the MedicalEquipment in the database to the specified file location of csv
   *
   * @param medicalEquipmentData File location of csv
   * @return True if successful, false otherwise
   */
  public boolean exportMedicalEquipmentToCSV(File medicalEquipmentData) {
    return medicalEquipmentDAO.exportToMedicalEquipmentCSV(medicalEquipmentData);
  }
  /**
   * Exports the patient table into a csv file to the working directory
   *
   * @return True if successful, false if not
   */
  public boolean exportPatientsToCSV(File patientData) {
    return patientDAO.exportToPatientCSV(patientData);
  }
  /**
   * Exports the Employee table into a csv file to the working directory
   *
   * @param employeeData File location of employee data csv
   * @return True if successful, false if not
   */
  public boolean exportEmployeesToCSV(File employeeData) {
    return employeeDAO.exportToEmployeeCSV(employeeData);
  }
  /** Writes the current database to a .csv file */
  // TODO fix the export services function
  public void exportServiceRequestsToCSV(File serviceRequestData) {
    serviceRequestDAO.exportToServiceRequestCSV();
  }
  /**
   * Exports the MedicalEquipmentRequest database to specified file location for csv
   *
   * @param equipmentData file location for csv
   * @return True if successful, false otherwise
   */
  public boolean exportMedicalEquipmentRequestsToCSV(File equipmentData) {
    return medEquipReqDAO.exportToMedEquipReqCSV(equipmentData);
  }
  // TODO create csv controller for lab requests
  /**
   * Exports all LabServiceRequests in the database to specified file location of csv
   *
   * @param labData file location of csv
   * @return True if successful, false otherwise
   */
  public boolean exportLabRequestsToCSV(File labData) {
    return labRequestServiceDAO.exportToLabRequestCSV(labData);
  }

  // Special methods for location
  /**
   * Gets all the nodeIDs for the locations in database
   *
   * @return list of nodeIDs
   */
  public List<String> getAllLocationNodeIDs() {
    return locationDAO.getAllLocationNodeIDs();
  }
  /**
   * Gets all locations on the given floor
   *
   * @param floor
   * @return list of locations
   */
  public List<Location> getAllLocationsByFloor(String floor) {
    return locationDAO.getAllLocationsByFloor(floor);
  }
  /**
   * Gets all locations of the given type
   * @param type type of location
   * @return list of locations of the given type
   */
  public List<Location> getALlLocationsByType(String type){
    return locationDAO.getALlLocationsByType(type);
  }

  // Special methods for medical equipment
  /**
   * Gets all MedicalEquipment in a given location
   *
   * @param location Location to extract MedicalEquipment inside
   * @return list of MedicalEquipment in the given location
   */
  public List<MedicalEquipment> getAllMedicalEquipmentByLocation(Location location) {
    return medicalEquipmentDAO.getAllMedicalEquipmentByLocation(location);
  }
  /**
   * Get the first avalable equipment with the given equipment type
   *
   * @param equipment type of equipment
   * @return equipmentID of the first available equipment of the given type
   */
  public String getFirstAvailableEquipmentByType(String equipment) {
    return medicalEquipmentDAO.getFirstAvailableEquipmentByType(equipment);
  }

  // Special methods for employee
  /**
   * Gets ONE Employee from the database based on the provided Username
   *
   * @param employeeUsername
   * @return Employee object with provided employeeID
   */
  public Employee getEmployeeByUsername(String employeeUsername) {
    return employeeDAO.getEmployeeByUsername(employeeUsername);
  }

  // Special methods for patient

  // Special methods for service requests

  // Special methods for medical equipment requests

  // Special methods for lab requests
}
