package edu.wpi.cs3733.D22.teamZ.apiFacades;

import edu.wpi.cs3733.D22.teamC.DBManagerAPI;
import edu.wpi.cs3733.D22.teamC.TeamCAPI;
import edu.wpi.cs3733.D22.teamC.controller.service_request.facility_maintenance.ServiceException;
import edu.wpi.cs3733.D22.teamC.entity.employee.EmployeeAPI;
import edu.wpi.cs3733.D22.teamC.entity.employee.EmployeeDAOImplAPI;
import edu.wpi.cs3733.D22.teamC.entity.service_request.facility_maintenance.FacilityMaintenanceSRAPI;
import edu.wpi.cs3733.D22.teamC.entity.service_request.facility_maintenance.FacilityMaintenanceSRDAOImplAPI;
import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import edu.wpi.cs3733.D22.teamZ.entity.FacilityMaintenanceRequest;
import java.util.List;
import java.util.stream.Collectors;

public class FacilityMaintenanceFacadeAPI {
  private static final FacilityMaintenanceFacadeAPI instance = new FacilityMaintenanceFacadeAPI();
  private final TeamCAPI api;
  private final EmployeeDAOImplAPI apiEmployeeDatabase;
  private final FacilityMaintenanceSRDAOImplAPI apiRequestDatabase;
  private final FacilityMaintenanceAPIConverter apiConverter;

  public static FacilityMaintenanceFacadeAPI getInstance() {
    return instance;
  }

  private FacilityMaintenanceFacadeAPI() {
    this.api = new TeamCAPI();
    this.apiEmployeeDatabase = new EmployeeDAOImplAPI();
    this.apiRequestDatabase = new FacilityMaintenanceSRDAOImplAPI();
    this.apiConverter = new FacilityMaintenanceAPIConverter();

    DBManagerAPI.getInstance().initializeEmployeeTable(true);
    DBManagerAPI.getInstance().initializeFacilityMaintenanceSRTable(true);
  }

  public void run(String cssPath) {
    try {
      api.run(0, 0, 300, 100, cssPath, "", "");
    } catch (ServiceException e) {
      System.out.println("Security API Service Exception");
    }
  }

  // ---------- Database Functions ----------
  public boolean addEmployee(Employee emp) {
    apiEmployeeDatabase.insertEmployee(apiConverter.convertToAPIEmployee(emp));
    EmployeeAPI val =
        apiEmployeeDatabase.getEmployee(apiConverter.convertToAPIEmployee(emp).getID());
    return (val == null);
  }

  public boolean updateEmployee(Employee emp) {
    EmployeeAPI apiEmp = apiConverter.convertToAPIEmployee(emp);
    return apiEmployeeDatabase.updateEmployee(apiEmp);
  }

  public boolean deleteEmployee(Employee emp) {
    EmployeeAPI apiEmp = apiConverter.convertToAPIEmployee(emp);
    return apiEmployeeDatabase.deleteEmployee(apiEmp);
  }

  public List<FacilityMaintenanceRequest> getAllFacilityMaintenanceRequests() {
    List<FacilityMaintenanceSRAPI> apiRequests = apiRequestDatabase.getAllFacilityMaintenanceSRs();
    List<FacilityMaintenanceRequest> requests =
        apiRequests.stream().map(apiConverter::convertFromAPIRequest).collect(Collectors.toList());
    return requests;
  }

  public FacilityMaintenanceRequest getFacilityMaintenanceRequestByID(String id) {
    int idInt = apiConverter.convertToAPIRequestID(id);
    FacilityMaintenanceSRAPI apiRequest = apiRequestDatabase.getFacilityMaintenanceSR(idInt);
    return apiConverter.convertFromAPIRequest(apiRequest);
  }

  public boolean addFacilityMaintenanceRequest(FacilityMaintenanceRequest request) {
    apiRequestDatabase.insertFacilityMaintenanceSR(apiConverter.convertToAPIRequest(request));
    FacilityMaintenanceSRAPI val =
        apiRequestDatabase.getFacilityMaintenanceSR(
            apiConverter.convertToAPIRequest(request).getID());
    return (val == null);
  }

  public boolean updateFacilityMaintenanceRequest(FacilityMaintenanceRequest request) {
    FacilityMaintenanceSRAPI apiRequest = apiConverter.convertToAPIRequest(request);
    return apiRequestDatabase.updateFacilityMaintenanceSR(apiRequest);
  }

  public boolean deleteFacilityMaintenanceRequest(FacilityMaintenanceRequest request) {
    FacilityMaintenanceSRAPI apiRequest = apiConverter.convertToAPIRequest(request);
    return apiRequestDatabase.deleteFacilityMaintenanceSR(apiRequest);
  }
}
