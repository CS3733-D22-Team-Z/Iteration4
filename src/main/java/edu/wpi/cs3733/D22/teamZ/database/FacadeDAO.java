package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.*;
import edu.wpi.cs3733.D22.teamZ.observers.DirtyBedObserver;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FacadeDAO {
  private static final FacadeDAO instance = new FacadeDAO();

  private final LocationDAOImpl locationDAO;
  private final MedicalEquipmentDAOImpl medicalEquipmentDAO;
  private final MedEquipReqDAOImpl medEquipReqDAO;
  private final EmployeeDAOImpl employeeDAO;
  private final PatientDAOImpl patientDAO;
  private final LabRequestServiceDAOImpl labRequestServiceDAO;
  private final ServiceRequestDAOImpl serviceRequestDAO;
  private final ExternalPatientDAOImpl transportRequestDAO;
  private final MealServiceRequestDAOImpl mealServiceRequestDAO;
  private final List<DirtyBedObserver> medEquipObs;

  public static FacadeDAO getInstance() {
    return instance;
  }

  private FacadeDAO() {
    locationDAO = new LocationDAOImpl();
    medicalEquipmentDAO = new MedicalEquipmentDAOImpl();
    medEquipReqDAO = new MedEquipReqDAOImpl();
    employeeDAO = new EmployeeDAOImpl();
    patientDAO = new PatientDAOImpl();
    labRequestServiceDAO = new LabRequestServiceDAOImpl();
    serviceRequestDAO = new ServiceRequestDAOImpl();
    transportRequestDAO = new ExternalPatientDAOImpl();
    mealServiceRequestDAO = new MealServiceRequestDAOImpl();
    medEquipObs = new ArrayList<>();
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
   * @param id The od of the location to be searched for
   * @return Location object with provided nodeID
   */
  public Location getLocationByID(String id) {
    // TODO is this necessary
    if (locationDAO.getLocationByID(id) == null) {
      return new Location();
    }
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
   * @param id The id of the employee to be searched for
   * @return Employee object with provided employeeID
   */
  public Employee getEmployeeByID(String id) {
    return employeeDAO.getEmployeeByID(id);
  }

  /**
   * Gets ONE patient from the database based on the provided patientID
   *
   * @param id The id of the patient to be searched for
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
   * @param id The id of the lab service request to be searched for
   * @return LabServiceRequest object with given ID
   */
  public LabServiceRequest getLabServiceRequestByID(String id) {
    return labRequestServiceDAO.getLabRequestByID(id);
  }

  // Add methods
  /**
   * Adds a new location to database. Will automatically check if already in database
   *
   * @param loc The location to be added
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
   * @param patient The patient to be added
   * @return True if successful, false if not
   */
  public boolean addPatient(Patient patient) {
    return patientDAO.addPatient(patient);
  }
  /**
   * Adds a new Employee to database. Will automatically check if already in database
   *
   * @param employee The employee to be added
   * @return True if successful, false if not
   */
  public boolean addEmployee(Employee employee) {
    return employeeDAO.addEmployee(employee);
  }
  /**
   * Adds a new Meal Service Request to database. Will automatically check if already in database
   *
   * @param request The meal request to be added
   * @return True if successful, false if not
   */
  public boolean addMealServiceRequest(MealServiceRequest request) {
    return mealServiceRequestDAO.addMealServReq(request);
  }
  /**
   * Adds the given ServiceRequest object to the database
   *
   * @param serviceRequest The request to be added
   * @return true if successfully added, false otherwise
   */
  public boolean addServiceRequest(ServiceRequest serviceRequest) {
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
   * ONLY USE THIS TO POPULATE DB: will add to MedEquipReq table
   *
   * @param medicalEquipmentDeliveryRequest reqeust to be added
   * @return true if successsful, false otherwise
   */
  public boolean addMedicalEquipmentRequestToDatabase(
      MedicalEquipmentDeliveryRequest medicalEquipmentDeliveryRequest) {
    return medEquipReqDAO.addMedEquipReq(medicalEquipmentDeliveryRequest);
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
  /**
   * Adds a ExternalPatientTransportationRequest to the database
   *
   * @param request request to be added
   * @return True if successful, false otherwise
   */
  public boolean addPatientTransportRequest(ExternalPatientTransportationRequest request) {
    return serviceRequestDAO.addServiceRequest(request)
        && transportRequestDAO.addPatientTransportRequest(request);
  }

  // Delete methods
  /**
   * Deletes a location from database. Will automatically check if exists in database already
   *
   * @param loc The location to be deleted
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
   * @param patient The patient to be deleted
   * @return True if successful, false if not
   */
  public boolean deletePatient(Patient patient) {
    return patientDAO.deletePatient(patient);
  }
  /**
   * Deletes a Employee from database. Will automatically check if exists in database
   *
   * @param employee The employee to be deleted
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
  public boolean deleteServiceRequest(ServiceRequest serviceRequest) {
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
   * @param loc The location to be updated
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
   * @param employee The employee to be updated
   * @return True if successful, false if not
   */
  public boolean updateEmployee(Employee employee) {
    return employeeDAO.updateEmployee(employee);
  }
  /**
   * Updates a patient in the database. Will automatically check if exists in database
   *
   * @param patient The patient to be updated
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
  public boolean updateServiceRequest(ServiceRequest serviceRequest) {
    boolean val = false;
    if (serviceRequestDAO.updateServiceRequest(serviceRequest)) {
      if (serviceRequest.getType().equals(ServiceRequest.RequestType.MEDEQUIP)) {
        MedicalEquipmentDeliveryRequest req =
            new MedicalEquipmentDeliveryRequest(
                serviceRequest.getRequestID(),
                serviceRequest.getStatus(),
                serviceRequest.getIssuer(),
                serviceRequest.getHandler(),
                null,
                serviceRequest.getTargetLocation());
        val = medEquipReqDAO.updateMedEquipReq(req);
      } else if (serviceRequest.getType().equals(ServiceRequest.RequestType.LABS)) {
        val = labRequestServiceDAO.updateLabRequest((LabServiceRequest) serviceRequest);
      } else if (serviceRequest.getType().equals(ServiceRequest.RequestType.EXTERNAL)) {
        // TODO implement update function for external patient transport
      }
    }
    return val;
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
   * @param locData The file path to the location csv that will be imported
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
    serviceRequestDAO.exportToServiceRequestCSV(serviceRequestData);
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

  // Get default path methods
  /**
   * Returns the default path that location csv files are printed to
   *
   * @return The default path that location csv files are printed to
   */
  public File getDefaultLocationCSVPath() {
    return locationDAO.getDefaultLocationCSVPath();
  }

  /**
   * Returns the default path that service request csv files are printed to
   *
   * @return The default path that service request csv files are printed to
   */
  public File getDefaultServiceRequestCSVPath() {
    return serviceRequestDAO.getDefaultServiceRequestCSVPath();
  }

  /**
   * Returns the default path that service request csv files are printed to
   *
   * @return The default path that service request csv files are printed to
   */
  public File getDefaultEmployeeCSVPath() {
    return employeeDAO.getDefaultEmployeeCSVPath();
  }

  /**
   * Returns the default path that medical equipment delivery request csv files are printed to
   *
   * @return The default path that medical equipment delivery request csv files are printed to
   */
  public File getDefaultMedEquipReqCSVPath() {
    return medEquipReqDAO.getDefaultMedEquipReqCSVPath();
  }

  // Add from list functions
  /**
   * Insert locations into the database from given list
   *
   * @param list list of locations to be added
   * @return true if successful, false otherwise
   */
  public boolean addLocationFromList(List<Location> list) {
    return locationDAO.addLocationFromList(list);
  }
  /**
   * Insert patients into database from given list
   *
   * @param list list of patients that need to be added
   * @return True if successful, false otherwise
   */
  public boolean addPatientFromList(List<Patient> list) {
    return patientDAO.addPatientFromList(list);
  }
  /**
   * Inserts employees from a list
   *
   * @param list list of employees to be added
   * @return true if successful, false otherwise
   */
  public boolean addEmployeeFromList(List<Employee> list) {
    return employeeDAO.addEmployeeFromList(list);
  }
  /**
   * Insert Medical Equipment into database from list
   *
   * @param list list of medical equipment to be added
   * @return True if successful, false otherwise
   */
  public boolean addMedicalEquipmentFromList(List<MedicalEquipment> list) {
    return medicalEquipmentDAO.addMedicalEquipmentFromList(list);
  }
  /**
   * Insert service requests into the database from the given list
   *
   * @param list list of service requests to be added
   * @return True if successful, false otherwise
   */
  public boolean addServiceRequestFromList(List<ServiceRequest> list) {
    return serviceRequestDAO.addServiceRequestFromList(list);
  }
  /**
   * Inserts lab requests into database from given list
   *
   * @param list list of lab requests to be added
   * @return true if successful, false otherwise
   */
  public boolean addLabRequestFromList(List<LabServiceRequest> list) {
    return labRequestServiceDAO.addLabRequestFromList(list);
  }
  /**
   * Adds MedicalEquipmentDeliveryRequest into database from list
   *
   * @param list Request to be added
   * @return True if successful, false otherwise
   */
  public boolean addMedicalEquipmentRequestFromList(List<MedicalEquipmentDeliveryRequest> list) {
    return medEquipReqDAO.addMedicalEquipReqFromList(list);
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
   * @param floor The floor to be searched
   * @return list of locations
   */
  public List<Location> getAllLocationsByFloor(String floor) {
    return locationDAO.getAllLocationsByFloor(floor);
  }
  /**
   * Gets all locations of the given type
   *
   * @param type type of location
   * @return list of locations of the given type
   */
  public List<Location> getALlLocationsByType(String type) {
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

  /**
   * Gets all medical equipment on particular floor
   *
   * @param floor floor to be inspected
   * @return list of medical equipment on a particular floor
   */
  public List<MedicalEquipment> getAllMedicalEquipmentByFloor(String floor) {
    return medicalEquipmentDAO.getAllMedicalEquipmentByFloor(floor);
  }
  /**
   * Get dirty equipment for the specified floor
   *
   * @param floor floor to be searched
   * @return number of dirty equipment
   */
  public int countDirtyEquipmentByFloor(String floor) {
    return medicalEquipmentDAO.countDirtyEquipmentByFloor(floor);
  }

  /**
   * Get clean equipment for the specified floor
   *
   * @param floor floor to be searched
   * @return number of dirty equipment
   */
  public int countCleanEquipmentByFloor(String floor) {
    return medicalEquipmentDAO.countCleanEquipmentByFloor(floor);
  }

  // Special methods for employee
  /**
   * Gets ONE Employee from the database based on the provided Username
   *
   * @param employeeUsername The username being searched for
   * @return Employee object with provided employeeID
   */
  public Employee getEmployeeByUsername(String employeeUsername) {
    return employeeDAO.getEmployeeByUsername(employeeUsername);
  }

  // Special methods for patient

  // Special methods for service requests
  /**
   * Gets the ServiceRequests in the given locations
   *
   * @param target location of service requests
   * @return ServiceRequest at that location
   */
  public List<ServiceRequest> getServiceRequestsByLocation(Location target) {
    return serviceRequestDAO.getServiceRequestsByLocation(target);
  }

  /**
   * Gets the ServiceRequests of a given status
   *
   * @param status
   * @return ServiceRequest of that Status
   */
  public List<ServiceRequest> getServiceRequestsByStatus(ServiceRequest.RequestStatus status) {
    return serviceRequestDAO.getServiceRequestsByStatus(status);
  }

  // Special methods for medical equipment requests

  // Special methods for lab requests

  //  /**
  //   * Add a Medical Equipment observer to list of observers
  //   *
  //   * @param observer the Medical Equipment observer to be added
  //   */
  //  public void addMedEquipObserver(MedicalEquipmentObserver observer) {
  //    medEquipObs.add(observer);
  //  }
  //
  //  /**
  //   * Gets the list of medical equipment observers currently available.
  //   *
  //   * @return the list of observers currently available
  //   */
  //  public List<MedicalEquipmentObserver> getMedEquipObservers() {
  //    return medEquipObs;
  //  }
  //
  //  /** Clears the observer list */
  //  public void removeMedEquipObserver() {
  //    for (MedicalEquipmentObserver observer : medEquipObs) observer.removeSubjects();
  //    medEquipObs.clear();
  //  }
}
