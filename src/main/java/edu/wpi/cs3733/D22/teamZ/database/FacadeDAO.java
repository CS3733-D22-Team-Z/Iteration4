package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.apiFacades.ExternalTransportFacadeAPI;
import edu.wpi.cs3733.D22.teamZ.apiFacades.FacilityMaintenanceFacadeAPI;
import edu.wpi.cs3733.D22.teamZ.apiFacades.InternalTransportFacadeAPI;
import edu.wpi.cs3733.D22.teamZ.entity.*;
import java.io.File;
import java.sql.SQLException;
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
  private final LaundryServiceDAOImpl laundryServiceRequestDAO;
  private final ServiceRequestDAOImpl serviceRequestDAO;
  private final GiftServiceRequestDAOImpl giftRequestDAO;
  private final MealServiceRequestDAOImpl mealServiceRequestDAO;
  private final CleaningRequestDAOImpl cleaningRequestDAO;
  private final EquipmentPurchaseDAOImpl equipmentPurchaseDAO;
  private final SecurityRequestDAOImpl securityRequestDAO;
  private final LanguageInterpreterRequestDAOImpl languageInterpreterRequestDAO;
  private final ComputerServiceRequestDAOImpl computerRequestDAO;
  private final InternalTransportFacadeAPI internalTransportAPI;
  private final FacilityMaintenanceFacadeAPI facilityMaintenanceAPI;

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
    laundryServiceRequestDAO = new LaundryServiceDAOImpl();
    serviceRequestDAO = new ServiceRequestDAOImpl();
    giftRequestDAO = new GiftServiceRequestDAOImpl();
    mealServiceRequestDAO = new MealServiceRequestDAOImpl();
    cleaningRequestDAO = new CleaningRequestDAOImpl();
    equipmentPurchaseDAO = new EquipmentPurchaseDAOImpl();
    securityRequestDAO = new SecurityRequestDAOImpl();
    languageInterpreterRequestDAO = new LanguageInterpreterRequestDAOImpl();
    computerRequestDAO = new ComputerServiceRequestDAOImpl();
    internalTransportAPI = InternalTransportFacadeAPI.getInstance();
    facilityMaintenanceAPI = FacilityMaintenanceFacadeAPI.getInstance();
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
    List<ServiceRequest> allRequests = new ArrayList<>();
    allRequests.addAll(serviceRequestDAO.getAllServiceRequests());
    allRequests.addAll(ExternalTransportFacadeAPI.getInstance().getAllExternalTransportRequests());
    allRequests.addAll(internalTransportAPI.getAllInternalTransportRequests());
    allRequests.addAll(facilityMaintenanceAPI.getAllFacilityMaintenanceRequests());
    return allRequests;
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
  /**
   * Gets all meal service requests in the database
   *
   * @return List of meal service requests
   */
  public List<MealServiceRequest> getAllMealServiceRequests() {
    return mealServiceRequestDAO.getAllMealServiceReq();
  }
  /**
   * Gets all the GiftServiceRequests in the database
   *
   * @return List of gift requests
   * @throws SQLException yes
   */
  public List<GiftServiceRequest> getAllGiftRequests() throws SQLException {
    return giftRequestDAO.getAllGiftServiceRequests();
  }
  /**
   * Gets all CleaningRequests in the database
   *
   * @return List of cleaning requests
   * @throws SQLException yes
   */
  public List<CleaningRequest> getAllCleaningRequests() throws SQLException {
    return cleaningRequestDAO.getAllCleaningServiceRequests();
  }
  /**
   * Gets all the EquipmentPurchaseRequests in the database
   *
   * @return list of equipment purchase requests
   */
  public List<EquipmentPurchaseRequest> getAllEquipmentPurchaseRequests() {
    return equipmentPurchaseDAO.getAllEquipmentPurchaseRequests();
  }
  /**
   * Gets all security requests
   *
   * @return list of security requests
   */
  public List<SecurityServiceRequest> getAllSecurityServiceRequests() {
    return securityRequestDAO.getAllSecurityServiceRequests();
  }
  /**
   * Gets all computer service requests
   *
   * @return list of computer service requests
   */
  public List<ComputerServiceRequest> getAllComputerServiceRequests() {
    return computerRequestDAO.getAllComputerServiceRequests();
  }

  /**
   * Gets all language interpreter requests
   *
   * @return list of language interpreter requests
   */
  public List<LanguageInterpreterRequest> getAllLanguageInterpreterRequests() throws SQLException {
    return languageInterpreterRequestDAO.getAllLanguageInterpreterServiceRequests();
  }

  /**
   * Gets all InternalTransportRequest objects
   *
   * @return list of InternalTransportRequest objects
   */
  public List<InternalTransportRequest> getAllInternalTransportRequests() {
    return internalTransportAPI.getAllInternalTransportRequests();
  }

  /**
   * Gets all FacilityMaintenanceRequest objects
   *
   * @return list of FacilityMaintenanceRequest objects
   */
  public List<FacilityMaintenanceRequest> getAllFacilityMaintenanceRequests() {
    return facilityMaintenanceAPI.getAllFacilityMaintenanceRequests();
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
   * Gets a MealServiceRequest of the given requestID
   *
   * @param id ID of request to be fetched
   * @return MealServiceRequest of given ID, null if not found
   */
  public MealServiceRequest getMealServiceRequestByID(String id) {
    return mealServiceRequestDAO.getMealServReqByID(id);
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
  /**
   * Get a GiftServiceRequest with provided requestID
   *
   * @param id the id of the gift service request to be searched for
   * @return GiftSerivceRequest object with given ID
   */
  public GiftServiceRequest getGiftServiceRequestByID(String id) {
    return giftRequestDAO.getGiftRequestByID(id);
  }
  /**
   * Get a CleaningRequest with provided requestID
   *
   * @param id the id of the cleaning request to be searched for
   * @return CleaningRequest object with given ID
   */
  public CleaningRequest getCleaningRequestByID(String id) {
    return cleaningRequestDAO.getCleaningRequestByID(id);
  }
  /**
   * Get an EquipmentPurchaseRequest with provided requestID
   *
   * @param id requestID to be fetched
   * @return EquipmentPurchaseRequest with given id; default is null
   */
  public EquipmentPurchaseRequest getEquipmentPurchaseRequestByID(String id) {
    return equipmentPurchaseDAO.getEquipmentPurchaseRequestByID(id);
  }
  /**
   * Get a SecurityServiceRequest with provided requestID
   *
   * @param requestID ID to find
   * @return SecurityServiceRequest object with given ID
   */
  public SecurityServiceRequest getSecurityServiceRequestByID(String requestID) {
    return securityRequestDAO.getSecurityServiceRequestByID(requestID);
  }
  /**
   * Get a LanguageInterpreterRequest with provided requestID
   *
   * @param id the id of the language interpreter request to be searched for
   * @return LanguageInterpreter object with given ID
   */
  public LanguageInterpreterRequest getLanguageInterpreterRequestByID(String id) {
    return languageInterpreterRequestDAO.getLanguageInterpreterRequestByID(id);
  }
  /**
   * Get a ComputerServiceRequest with provided requestID
   *
   * @param requestID ID to find
   * @return ComputerServiceRequest object with given ID
   */
  public ComputerServiceRequest getComputerServiceRequestByID(String requestID) {
    return computerRequestDAO.getComputerServiceRequestByID(requestID);
  }
  /**
   * Get an InternalTransportRequest with provided requestID
   *
   * @param requestID ID to find
   * @return InternalTransportRequest object with given ID
   */
  public InternalTransportRequest getInternalTransportRequestByID(String requestID) {
    return internalTransportAPI.getInternalTransportRequestByID(requestID);
  }
  /**
   * Get an FacilityMaintenanceRequest with provided requestID
   *
   * @param requestID ID to find
   * @return FacilityMaintenanceRequest object with given ID
   */
  public FacilityMaintenanceRequest getFacilityMaintenanceRequestByID(String requestID) {
    return facilityMaintenanceAPI.getFacilityMaintenanceRequestByID(requestID);
  }

  // Add methods
  /**
   * Adds a new location to database. Will automatically check if already in database
   *
   * @param loc The location to be added
   * @return True if successful, false if not
   */
  public boolean addLocation(Location loc) {
    return locationDAO.addLocation(loc) && internalTransportAPI.addLocation(loc);
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
    boolean val1 = employeeDAO.addEmployee(employee);
    boolean val2 = internalTransportAPI.addEmployee(employee);
    boolean val3 = facilityMaintenanceAPI.addEmployee(employee);
    return val1 && val2 && val3;
  }
  /**
   * Adds a new Meal Service Request to database. Will automatically check if already in database
   *
   * @param request The meal request to be added
   * @return True if successful, false if not
   */
  public boolean addMealServiceRequest(MealServiceRequest request) {
    return serviceRequestDAO.addServiceRequest(request)
        && mealServiceRequestDAO.addMealServReq(request);
  }
  /**
   * ONLY USE THIS TO POPULATE DB: will add to MealServReq table
   *
   * @param mealServiceRequest reqeust to be added
   * @return true if successsful, false otherwise
   */
  public boolean addMealServiceRequestToDatabase(MealServiceRequest mealServiceRequest) {
    return mealServiceRequestDAO.addMealServReq(mealServiceRequest);
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
    return serviceRequestDAO.addServiceRequest(medicalEquipmentDeliveryRequest)
        && medEquipReqDAO.addMedEquipReq(medicalEquipmentDeliveryRequest);
  }
  /**
   * Adds a CleaningRequest to the database
   *
   * @param cleaningRequest CleaningRequest to be added
   * @return True if successful, false otherwise
   */
  public boolean addCleaningRequest(CleaningRequest cleaningRequest) {
    boolean val =
        serviceRequestDAO.addServiceRequest(cleaningRequest)
            && cleaningRequestDAO.addCleaningRequest(cleaningRequest);
    return val;
  }
  /**
   * Adds an EquipmentPurchaseRequest to the database
   *
   * @param purchaseRequest EquipmentPurchaseRequest to be added
   * @return true if successful, false otherwise
   */
  public boolean addEquipmentPurchaseRequest(EquipmentPurchaseRequest purchaseRequest) {
    return serviceRequestDAO.addServiceRequest(purchaseRequest)
        && equipmentPurchaseDAO.addEquipmentPurchaseRequest(purchaseRequest);
  }
  /**
   * Adds SecurityServiceRequest to the database
   *
   * @param request SecurityServiceRequest to be added
   * @return True if successful, false otherwise
   */
  public boolean addSecurityServiceRequest(SecurityServiceRequest request) {
    return serviceRequestDAO.addServiceRequest(request)
        && securityRequestDAO.addSecurityServiceRequest(request);
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
   * ONLY USE THIS TO POPULATE DB: will add to EquipmentPurchase table
   *
   * @param request request to be added
   * @return true if successful, false otherwise
   */
  public boolean addEquipmentPurchaseRequestToDatabase(EquipmentPurchaseRequest request) {
    return equipmentPurchaseDAO.addEquipmentPurchaseRequest(request);
  }
  /**
   * ONLY USE THIS TO POPULATE DB: will add to EquipmentPurchase table
   *
   * @param request request to be added
   * @return true if successful, false otherwise
   */
  public boolean addSecurityServiceRequestToDatabase(SecurityServiceRequest request) {
    return securityRequestDAO.addSecurityServiceRequest(request);
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
   * Adds a LabServiceRequest to the database
   *
   * @param laundryServiceRequest LabServiceRequest to be added
   * @return True if successful, false otherwise
   */
  public boolean addLaundryServiceRequest(LaundryServiceRequest laundryServiceRequest) {
    boolean val =
        serviceRequestDAO.addServiceRequest(laundryServiceRequest)
            && laundryServiceRequestDAO.addLaundryServiceRequest(laundryServiceRequest);
    return val;
  }
  /**
   * Adds a ExternalPatientTransportationRequest to the database
   *
   * @param request request to be added
   * @return True if successful, false otherwise
   */
  public boolean addExternalPatientTransport(ExternalPatientTransportationRequest request) {
    return ExternalTransportFacadeAPI.getInstance().addExternalTransportRequest(request);
  }
  /**
   * Adds an InternalTransportRequest to the database
   *
   * @param request request to be added
   * @return True if successful, false otherwise
   */
  public boolean addInternalPatientTransport(InternalTransportRequest request) {
    return internalTransportAPI.addInternalTransportRequest(request);
  }
  /**
   * Adds an FacilityMaintenanceRequest to the database
   *
   * @param request request to be added
   * @return True if successful, false otherwise
   */
  public boolean addFacilityMaintenanceRequest(FacilityMaintenanceRequest request) {
    return facilityMaintenanceAPI.addFacilityMaintenanceRequest(request);
  }
  /**
   * Adds a CleaningRequest to the database
   *
   * @param request request to be added
   * @return True if successful, false otherwise
   */
  public boolean addCleaningRequestToDatabase(CleaningRequest request) {
    return cleaningRequestDAO.addCleaningRequest(request);
  }
  /**
   * Adds a GiftRequest to the database
   *
   * @param request request to be added
   * @return True if successful, false otherwise
   */
  public boolean addGiftRequest(GiftServiceRequest request) {
    return serviceRequestDAO.addServiceRequest(request) && giftRequestDAO.addGiftRequest(request);
  }
  /**
   * Adds a Language Interpreter to the database
   *
   * @param request request to be added
   * @return True if successful, false otherwise
   */
  public boolean addLanguageInterpreterRequest(LanguageInterpreterRequest request) {
    return serviceRequestDAO.addServiceRequest(request)
        && languageInterpreterRequestDAO.addLanguageInterpreterRequest(request);
  }
  /**
   * Adds ComputerServiceRequest to the database
   *
   * @param request ComputerServiceRequest to be added
   * @return True if successful, false otherwise
   */
  public boolean addComputerServiceRequest(ComputerServiceRequest request) {
    return serviceRequestDAO.addServiceRequest(request)
        && computerRequestDAO.addComputerServiceRequest(request);
  }

  // Delete methods
  /**
   * Deletes a location from database. Will automatically check if exists in database already
   *
   * @param loc The location to be deleted
   * @return True if successful, false if not
   */
  public boolean deleteLocation(Location loc) {
    return locationDAO.deleteLocation(loc) && internalTransportAPI.deleteLocation(loc);
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
   * Deletes an external transport request from database. Will automatically check if exists in
   * database
   *
   * @param req The request to be deleted
   * @return True if successful, false if not
   */
  public boolean deleteExternalTransportRequest(ExternalPatientTransportationRequest req) {
    return ExternalTransportFacadeAPI.getInstance().deleteExternalTransportRequest(req);
  }

  /**
   * Deletes an internal transport request from database. Will automatically check if exists in
   * database
   *
   * @param req The request to be deleted
   * @return True if successful, false if not
   */
  public boolean deleteInternalTransportRequest(InternalTransportRequest req) {
    return internalTransportAPI.deleteInternalTransportRequest(req);
  }

  /**
   * Deletes an facility maintenance request from database. Will automatically check if exists in
   * database
   *
   * @param req The request to be deleted
   * @return True if successful, false if not
   */
  public boolean deleteFacilityMaintenanceRequest(FacilityMaintenanceRequest req) {
    return facilityMaintenanceAPI.deleteFacilityMaintenanceRequest(req);
  }

  /**
   * Deletes a Employee from database. Will automatically check if exists in database
   *
   * @param employee The employee to be deleted
   * @return True if successful, false if not
   */
  public boolean deleteEmployee(Employee employee) {
    return employeeDAO.deleteEmployee(employee)
        && internalTransportAPI.deleteEmployee(employee)
        && facilityMaintenanceAPI.deleteEmployee(employee);
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
    return medEquipReqDAO.deleteMedEquipReq(medicalEquipmentDeliveryRequest)
        && serviceRequestDAO.deleteServiceRequest(medicalEquipmentDeliveryRequest);
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
  /**
   * Deletes a GiftServiceRequest from the database
   *
   * @param request GiftServiceRequest to be deleted
   * @return True if successful, false otherwise
   */
  public boolean deleteGiftRequest(GiftServiceRequest request) {
    boolean val =
        giftRequestDAO.deleteGiftRequest(request)
            && serviceRequestDAO.deleteServiceRequest(request);
    return giftRequestDAO.deleteGiftRequest(request);
  }
  /**
   * Deletes a CleaningRequest from the database
   *
   * @param request CleaningRequest to be deleted
   * @return True if successful, false otherwise
   */
  public boolean deleteCleaningRequest(CleaningRequest request) {
    return cleaningRequestDAO.deleteCleaningRequest(request)
        && serviceRequestDAO.deleteServiceRequest(request);
  }
  /**
   * Deletes an EquipmentPurchaseRequest from the database
   *
   * @param request request to be deleted
   * @return true if successful, false otherwise
   */
  public boolean deleteEquipmentPurchaseRequest(EquipmentPurchaseRequest request) {
    return equipmentPurchaseDAO.deleteEquipmentPurchaseRequest(request)
        && serviceRequestDAO.deleteServiceRequest(request);
  }
  /**
   * Deletes an SecurityServiceRequest from the database
   *
   * @param request SecurityServiceRequest to be deleted
   * @return True if successful, false otherwise
   */
  public boolean deleteSecurityServiceRequest(SecurityServiceRequest request) {
    return securityRequestDAO.deleteSecurityServiceRequest(request)
        && serviceRequestDAO.deleteServiceRequest(request);
  }
  /**
   * Deletes a Language Interpreter Request from the database
   *
   * @param request LanguageInterpreterRequest to be deleted
   * @return True if successful, false otherwise
   */
  public boolean deleteLanguageInterpreterRequest(LanguageInterpreterRequest request) {
    boolean val =
        languageInterpreterRequestDAO.deleteLanguageInterpreterRequest(request)
            && serviceRequestDAO.deleteServiceRequest(request);
    return languageInterpreterRequestDAO.deleteLanguageInterpreterRequest(request);
  }
  /**
   * Deletes an ComputerServiceRequest from the database
   *
   * @param request ComputerServiceRequest to be deleted
   * @return True if successful, false otherwise
   */
  public boolean deleteComputerServiceRequest(ComputerServiceRequest request) {
    return computerRequestDAO.deleteComputerServiceRequest(request)
        && serviceRequestDAO.deleteServiceRequest(request);
  }

  // Update methods
  /**
   * Updates a location in the database. Will automatically check if exists in database
   *
   * @param loc The location to be updated
   * @return True if successful, false if not
   */
  public boolean updateLocation(Location loc) {
    return locationDAO.updateLocation(loc) && internalTransportAPI.updateLocation(loc);
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
   * Updates an Employee in the database. Will automatically check if exists in database
   *
   * @param employee The employee to be updated
   * @return True if successful, false if not
   */
  public boolean updateEmployee(Employee employee) {
    return employeeDAO.updateEmployee(employee)
        && internalTransportAPI.updateEmployee(employee)
        && facilityMaintenanceAPI.updateEmployee(employee);
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
                serviceRequest.getTargetLocation(),
                serviceRequest.getOpened(),
                serviceRequest.getClosed());
        val = medEquipReqDAO.updateMedEquipReq(req);
      } else if (serviceRequest.getType().equals(ServiceRequest.RequestType.LABS)) {
        val = labRequestServiceDAO.updateLabRequest((LabServiceRequest) serviceRequest);
      } else if (serviceRequest.getType().equals(ServiceRequest.RequestType.EXTERNAL)) {
        return ExternalTransportFacadeAPI.getInstance()
            .updateExternalTransportRequest((ExternalPatientTransportationRequest) serviceRequest);
      } else if (serviceRequest.getType().equals(ServiceRequest.RequestType.INTERNAL)) {
        return internalTransportAPI.updateInternalTransportRequest(
            (InternalTransportRequest) serviceRequest);
      } else if (serviceRequest.getType().equals(ServiceRequest.RequestType.FACILITY)) {
        return facilityMaintenanceAPI.updateFacilityMaintenanceRequest(
            (FacilityMaintenanceRequest) serviceRequest);
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
  /**
   * updates an existing GiftServiceRequest in database with new request
   *
   * @param request GiftServiceRequest to be updated
   * @return True if successful, false otherwise
   */
  public boolean updateGiftRequest(GiftServiceRequest request) {
    return updateServiceRequest(request) && giftRequestDAO.updateGiftRequest(request);
  }
  /**
   * updates an existing external patient transport service request in database with new request
   *
   * @param req external patient request to be updated
   * @return True if successful, false otherwise
   */
  public boolean updateExternalPatientTransportRequest(ExternalPatientTransportationRequest req) {
    return ExternalTransportFacadeAPI.getInstance().updateExternalTransportRequest(req);
  }
  /**
   * updates an existing internal patient transport service request in database with new request
   *
   * @param req internal patient request to be updated
   * @return True if successful, false otherwise
   */
  public boolean updateInternalTransportRequest(InternalTransportRequest req) {
    return internalTransportAPI.updateInternalTransportRequest(req);
  }
  /**
   * updates an existing facility maintenance service request in database with new request
   *
   * @param req facility maintenance request to be updated
   * @return True if successful, false otherwise
   */
  public boolean updateFacilityMaintenanceRequest(FacilityMaintenanceRequest req) {
    return facilityMaintenanceAPI.updateFacilityMaintenanceRequest(req);
  }
  /**
   * updates an existing CleaningRequest in database with new request
   *
   * @param request CleaningRequest to be updated
   * @return True if successful, false otherwise
   */
  public boolean updateCleaningRequest(CleaningRequest request) {
    return serviceRequestDAO.updateServiceRequest(request)
        && cleaningRequestDAO.updateCleaningRequest(request);
  }
  /**
   * updates an existing EquipmentPurchaseRequest in database with new request
   *
   * @param request PurchaseRequest to be updated
   * @return true if successful, false otherwise
   */
  public boolean updateEquipmentPurchaseRequest(EquipmentPurchaseRequest request) {
    return serviceRequestDAO.updateServiceRequest(request)
        && equipmentPurchaseDAO.updateEquipmentPurchaseRequest(request);
  }
  /**
   * Updates an existing SecurityServiceRequest in database with new request
   *
   * @param request SecurityServiceRequest to be updated
   * @return True if successful, false otherwise
   */
  public boolean updateSecurityServiceRequest(SecurityServiceRequest request) {
    return serviceRequestDAO.updateServiceRequest(request)
        && securityRequestDAO.updateSecurityServiceRequest(request);
  }
  /**
   * updates an existing LanguageInterpreterRequest in database with new request
   *
   * @param request LanguageInterpreterRequest to be updated
   * @return True if successful, false otherwise
   */
  public boolean updateLanguageInterpreterRequest(LanguageInterpreterRequest request) {
    return updateServiceRequest(request)
        && languageInterpreterRequestDAO.updateLanguageInterpreterRequest(request);
  }
  /**
   * Updates an existing ComputerServiceRequest in database with new request
   *
   * @param request ComputerServiceRequest to be updated
   * @return True if successful, false otherwise
   */
  public boolean updateComputerServiceRequest(ComputerServiceRequest request) {
    return serviceRequestDAO.updateServiceRequest(request)
        && computerRequestDAO.updateComputerServiceRequest(request);
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
  /**
   * Imports all LabServiceRequests in specified file location of csv into the database
   *
   * @param labRequestData file location of csv
   * @return True if successful, false otherwise
   */
  public int importLabServiceRequestsFromCSV(File labRequestData) {
    return labRequestServiceDAO.importLabRequestFromCSV(labRequestData);
  }
  /**
   * imports all GiftServiceRequests in specified file location of csv into the database
   *
   * @param request file location of csv
   * @return True if successful, false otherwise
   */
  public int importGiftRequestFromCSV(File request) {
    return giftRequestDAO.importGiftRequestFromCSV(request);
  }
  /**
   * imports all EquipmentPurchaseRequests in a specified file location of csv into the database
   *
   * @param request EquipmentPurchaseRequest to be imported
   * @return number of conflicts when importing
   */
  public int importEquipmentPurchaseRequestFromCSV(File request) {
    return equipmentPurchaseDAO.importEquipmentPurchaseRequestFromCSV(request);
  }
  /**
   * Imports all SecurityServiceRequest in specified file location of csv into the database
   *
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  public int importSecurityServiceRequestFromCSV(File data) {
    return securityRequestDAO.importSecurityServiceRequestFromCSV(data);
  }
  /**
   * imports all LanguageInterpreterRequests in a specified file location of csv into the database
   *
   * @param request LanguageInterpreterRequest to be imported
   * @return number of conflicts when importing
   */
  public int importLanguageInterpreterRequestFromCSV(File request) {
    return languageInterpreterRequestDAO.importLanguageInterpreterRequestFromCSV(request);
  }
  /**
   * Imports all ComputerServiceRequest in specified file location of csv into the database
   *
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  public int importComputerServiceRequestFromCSV(File data) {
    return computerRequestDAO.importComputerServiceRequestFromCSV(data);
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
  /**
   * Exports the cleaning table into a csv file to the working directory
   *
   * @param cleaningData File location of cleaning req data csv
   * @return True if successful, false if not
   */
  public boolean exportCleaningReqToCSV(File cleaningData) {
    return cleaningRequestDAO.exportToCleaningRequestCSV(cleaningData);
  }
  /** Writes the current database to a .csv file */
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
  /**
   * Exports the MealServiceRequest database to specified file location for csv
   *
   * @param mealData file location for csv
   * @return True if successful, false otherwise
   */
  public boolean exportMealServiceRequestsToCSV(File mealData) {
    return mealServiceRequestDAO.exportToMealServReqCSV(mealData);
  }
  /**
   * Exports all LabServiceRequests in the database to specified file location of csv
   *
   * @param labData file location of csv
   * @return True if successful, false otherwise
   */
  public boolean exportLabRequestsToCSV(File labData) {
    return labRequestServiceDAO.exportToLabRequestCSV(labData);
  }
  /**
   * Exports all GiftServiceRequests in the database to specified file location of csv
   *
   * @param giftData file location of csv
   * @return True if successful, false otherwise
   */
  public boolean exportGiftRequestToCSV(File giftData) {
    return giftRequestDAO.exportToGiftRequestCSV(giftData);
  }
  /**
   * Exports all EquipmentPurchaseRequests in the database to a specified fule location of csv
   *
   * @param purchaseData csv file location
   * @return true if successful, false otherwise
   */
  public boolean exportEquipmentPurchaseRequestsToCSV(File purchaseData) {
    return equipmentPurchaseDAO.exportToEquipmentPurchaseRequestCSV(purchaseData);
  }
  /**
   * Exports all SecurityServiceRequest in the database to specified file location of csv
   *
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  public boolean exportToSecurityServiceRequestCSV(File data) {
    return securityRequestDAO.exportToSecurityServiceRequestCSV(data);
  }
  /**
   * Exports LanguageInterpreterRequest database to specified file location for csv
   *
   * @param languageInterpreterData file location for csv
   * @return True if successful, false otherwise
   */
  public boolean exportLanguageInterpreterRequestsToCSV(File languageInterpreterData) {
    return languageInterpreterRequestDAO.exportToLanguageInterpreterRequestCSV(
        languageInterpreterData);
  }

  /**
   * Exports all ComputerServiceRequest in the database to specified file location of csv
   *
   * @param data file location of csv
   * @return True if successful, false otherwise
   */
  public boolean exportToComputerServiceRequestCSV(File data) {
    return computerRequestDAO.exportToComputerServiceRequestCSV(data);
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
  /**
   * Returns the default path that cleaning request csv files are printed to
   *
   * @return The default path that cleaning request csv files are printed to
   */
  public File getDefaultCleaningReqCSVPath() {
    return cleaningRequestDAO.getDefaultCleaningReqCSVPath();
  }

  public File getDefaultMealServReqCSVPath() {
    return mealServiceRequestDAO.getDefaultMealServReqCSVPath();
  }

  public File getDefaultEquipmentPurchaseRequestCSVPath() {
    return equipmentPurchaseDAO.getDefaultEquipmentPurchaseRequestCSVPath();
  }

  public File getDefaultComputerRequestCSVPath() {
    return computerRequestDAO.getDefaultComputerServiceRequestCSVPath();
  }

  /**
   * Returns the default path that gift service request csv files are printed to
   *
   * @return The default path that gift service request csv files are printed to
   */
  public File getDefaultGiftServiceCSVPath() {
    return giftRequestDAO.getDefaultGiftServiceCSVPath();
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
  /**
   * Adds GiftServiceRequest into database from list
   *
   * @param list Request to be added
   * @return True if successful, false otherwise
   */
  public boolean addGiftRequestFromList(List<GiftServiceRequest> list) {
    return giftRequestDAO.addGiftRequestFromList(list);
  }
  /**
   * Adds EquipmentPurchaseRequests into database from list
   *
   * @param list list of purchase requests to be added
   * @return true if successful, false otherwise
   */
  public boolean addEquipmentPurchaseRequestFromList(List<EquipmentPurchaseRequest> list) {
    return equipmentPurchaseDAO.addEquipmentPurchaseRequestFromList(list);
  }
  /**
   * Inserts SecurityServiceRequest into database from given list
   *
   * @param list list of SecurityServiceRequest to be added
   * @return true if successful, false otherwise
   */
  public boolean addSecurityServiceRequestFromList(List<SecurityServiceRequest> list) {
    return securityRequestDAO.addSecurityServiceRequestFromList(list);
  }
  /**
   * Inserts ComputerServiceRequest into database from given list
   *
   * @param list list of ComputerServiceRequest to be added
   * @return true if successful, false otherwise
   */
  public boolean addComputerServiceRequestFromList(List<ComputerServiceRequest> list) {
    return computerRequestDAO.addComputerServiceRequestFromList(list);
  }

  /**
   * Adds languageInterpreterRequest into database from list
   *
   * @param list Request to be added
   * @return True if successful, false otherwise
   */
  public boolean addLanguageInterpreterRequestFromList(List<LanguageInterpreterRequest> list) {
    return languageInterpreterRequestDAO.addLanguageInterpreterFromList(list);
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
  public List<Location> getAllLocationsByType(String type) {
    return locationDAO.getAllLocationsByType(type);
  }
  /**
   * Get the nodeID of a dirty location on the given floor
   *
   * @param floor floor to be inspected
   * @return nodeID of the dirty location; default is dirty location on thrid floor
   */
  public String getDirtyNodeIDbyFloor(String floor) {
    return locationDAO.getDirtyNodeIDbyFloor(floor);
  }
  /**
   * Get the nodeID of a clean storage location on the given floor
   *
   * @param floor floor to be inspected
   * @return nodeID of the clean location
   */
  public String getCleanNodeIDbyFloor(String floor) {
    return "";
  }
  /**
   * Get the nodeID of a random bed park on the given floor
   *
   * @param floor floor to be inspected
   * @return nodeID of the bed park
   */
  public String getRandomBedParkNodeIDByFloor(String floor) {
    return locationDAO.getRandomBedParkNodeIDByFloor(floor);
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

  /**
   * Get the number of dirty beds for the specified floor
   *
   * @param floor floor to be searched
   * @return number of dirty beds
   */
  public int countDirtyBedsByFloor(String floor) {
    return medicalEquipmentDAO.countDirtyBedsByFloor(floor);
  }

  /**
   * Get the number of clean beds for the specified floor
   *
   * @param floor floor to be searched
   * @return number of clean beds
   */
  public int countCleanBedsByFloor(String floor) {
    return medicalEquipmentDAO.countCleanBedsByFloor(floor);
  }

  /**
   * Get the number of in use beds for the specified floor
   *
   * @param floor floor to be searched
   * @return number of in use beds
   */
  public int countInUseBedsByFloor(String floor) {
    return medicalEquipmentDAO.countInUseBedsByFloor(floor);
  }

  /**
   * Get the number of dirty IPumps for the specified floor
   *
   * @param floor floor to be searched
   * @return number of dirty IPumps
   */
  public int countDirtyIPumpsByFloor(String floor) {
    return medicalEquipmentDAO.countDirtyIPumpsByFloor(floor);
  }

  /**
   * Get the number of clean IPumps for the specified floor
   *
   * @param floor floor to be searched
   * @return number of clean IPumps
   */
  public int countCleanIPumpsByFloor(String floor) {
    return medicalEquipmentDAO.countCleanIPumpsByFloor(floor);
  }

  /**
   * Get the number of in use IPumps for the specified floor
   *
   * @param floor floor to be searched
   * @return number of in use IPumps
   */
  public int countInUseIPumpsByFloor(String floor) {
    return medicalEquipmentDAO.countInUseIPumpsByFloor(floor);
  }

  /**
   * Get the number of dirty Recliners for the specified floor
   *
   * @param floor floor to be searched
   * @return number of dirty Recliners
   */
  public int countDirtyReclinersByFloor(String floor) {
    return medicalEquipmentDAO.countDirtyReclinersByFloor(floor);
  }

  /**
   * Get the number of clean Recliners for the specified floor
   *
   * @param floor floor to be searched
   * @return number of clean Recliners
   */
  public int countCleanReclinersByFloor(String floor) {
    return medicalEquipmentDAO.countCleanReclinersByFloor(floor);
  }

  /**
   * Get the number of in use Recliners for the specified floor
   *
   * @param floor floor to be searched
   * @return number of in use Recliners
   */
  public int countInUseReclinersByFloor(String floor) {
    return medicalEquipmentDAO.countInUseReclinersByFloor(floor);
  }

  /**
   * Get the number of dirty XRays for the specified floor
   *
   * @param floor floor to be searched
   * @return number of dirty XRays
   */
  public int countDirtyXRaysByFloor(String floor) {
    return medicalEquipmentDAO.countDirtyXRaysByFloor(floor);
  }

  /**
   * Get the number of clean RXays for the specified floor
   *
   * @param floor floor to be searched
   * @return number of clean XRays
   */
  public int countCleanXRaysByFloor(String floor) {
    return medicalEquipmentDAO.countCleanXRaysByFloor(floor);
  }

  /**
   * Get the number of in use RXays for the specified floor
   *
   * @param floor floor to be searched
   * @return number of clean XRays
   */
  public int countInUseXRaysByFloor(String floor) {
    return medicalEquipmentDAO.countInUseXRaysByFloor(floor);
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

  /**
   * Gets laundry requests from DAO Impl
   *
   * @return all current laundry service requests
   */
  public List<LaundryServiceRequest> getAllLaundryServiceRequests() {
    return laundryServiceRequestDAO.getAllLaundryServiceRequests();
  }

  /**
   * @param laundryID service request ID
   * @return the associated laundry service request
   */
  public LaundryServiceRequest getLaundryRequestByID(String laundryID) {
    return laundryServiceRequestDAO.getLaundryRequestById(laundryID);
  }

  public File getDefaultLaundryServiceRequestCSVPath() {
    return laundryServiceRequestDAO.getDefaultLaundryServiceRequestCSVPath();
  }

  public boolean exportLaundryRequestsToCSV(File laundryData) {
    if (laundryServiceRequestDAO.exportToLaundryServiceRequestCSV(laundryData)) return true;
    return false;
  }

  public void addLaundryServiceRequestToDatabase(LaundryServiceRequest info) {
    laundryServiceRequestDAO.addLaundryServiceRequest(info);
  }

  public void addComputerServiceRequestToDatabase(ComputerServiceRequest info) {
    computerRequestDAO.addComputerServiceRequest(info);
  }

  // Special methods for medical equipment requests

  // Special methods for lab requests
}
