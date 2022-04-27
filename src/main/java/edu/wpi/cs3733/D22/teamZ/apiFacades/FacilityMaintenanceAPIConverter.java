package edu.wpi.cs3733.D22.teamZ.apiFacades;

import static java.util.Map.entry;

import edu.wpi.cs3733.D22.teamC.entity.employee.EmployeeAPI;
import edu.wpi.cs3733.D22.teamC.entity.service_request.facility_maintenance.FacilityMaintenanceSRAPI;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import edu.wpi.cs3733.D22.teamZ.entity.FacilityMaintenanceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.util.Map;

class FacilityMaintenanceAPIConverter {
  private final Map<String, String> employeeIDMap =
      Map.ofEntries(entry("nurse", "1"), entry("doctor", "2"), entry("admin", "3"));

  // ---------- Request Converters ----------
  FacilityMaintenanceSRAPI convertToAPIRequest(FacilityMaintenanceRequest request) {
    int apiRequestID = convertToAPIRequestID(request.getRequestID());
    String apiLocationID = request.getTargetLocation().getNodeID();
    int apiIssuerID = convertToAPIEmployeeID(request.getIssuer().getEmployeeID());
    int apiHandlerID = convertToAPIEmployeeID(request.getHandler().getEmployeeID());
    String description = request.getDescription();
    FacilityMaintenanceSRAPI.Status apiStatus = convertToAPIStatus(request.getStatus());
    FacilityMaintenanceSRAPI.MaintenanceType apiMaintenanceType =
        convertToAPIMaintenanceType(request.getMaintenanceType());
    FacilityMaintenanceSRAPI.Priority priority = request.getPriority();

    FacilityMaintenanceSRAPI apiRequest = new FacilityMaintenanceSRAPI();
    apiRequest.setID(apiRequestID);
    apiRequest.setLocationID(apiLocationID);
    apiRequest.setCreator(apiIssuerID);
    apiRequest.setAssigneeID(apiHandlerID);
    apiRequest.setDescription(description);
    apiRequest.setStatus(apiStatus);
    apiRequest.setMaintenanceType(apiMaintenanceType);
    apiRequest.setPriority(priority);
    return apiRequest;
  }

  FacilityMaintenanceRequest convertFromAPIRequest(FacilityMaintenanceSRAPI apiRequest) {
    String requestID = convertFromAPIRequestID(apiRequest.getID());
    ServiceRequest.RequestStatus status = convertFromAPIStatus(apiRequest.getStatus());
    Employee issuer =
        FacadeDAO.getInstance().getEmployeeByID(convertFromAPIEmployeeID(apiRequest.getCreator()));
    Employee handler =
        FacadeDAO.getInstance()
            .getEmployeeByID(convertFromAPIEmployeeID(apiRequest.getAssigneeID()));
    Location targetLocation = FacadeDAO.getInstance().getLocationByID(apiRequest.getLocationID());
    FacilityMaintenanceRequest.MaintenanceType maintenanceType =
        convertFromAPIMaintenanceType(apiRequest.getMaintenanceType());
    String description = apiRequest.getDescription();
    FacilityMaintenanceSRAPI.Priority priority = apiRequest.getPriority();

    return new FacilityMaintenanceRequest(
        requestID, status, issuer, handler, targetLocation, maintenanceType, priority, description);
  }

  FacilityMaintenanceSRAPI.Status convertToAPIStatus(ServiceRequest.RequestStatus status) {
    switch (status) {
      case UNASSIGNED:
        return FacilityMaintenanceSRAPI.Status.Blank;
      case PROCESSING:
        return FacilityMaintenanceSRAPI.Status.Processing;
      case DONE:
        return FacilityMaintenanceSRAPI.Status.Done;
      default:
        return null;
    }
  }

  ServiceRequest.RequestStatus convertFromAPIStatus(FacilityMaintenanceSRAPI.Status apiStatus) {
    switch (apiStatus) {
      case Blank:
        return ServiceRequest.RequestStatus.UNASSIGNED;
      case Processing:
        return ServiceRequest.RequestStatus.PROCESSING;
      case Done:
        return ServiceRequest.RequestStatus.DONE;
      default:
        return null;
    }
  }

  FacilityMaintenanceSRAPI.MaintenanceType convertToAPIMaintenanceType(
      FacilityMaintenanceRequest.MaintenanceType maintenanceType) {
    switch (maintenanceType) {
      case DOOR:
        return FacilityMaintenanceSRAPI.MaintenanceType.Broken_Door;
      case PIPE:
        return FacilityMaintenanceSRAPI.MaintenanceType.Broken_Pipe;
      case SINK:
        return FacilityMaintenanceSRAPI.MaintenanceType.Broken_Sink;
      case LIGHT:
        return FacilityMaintenanceSRAPI.MaintenanceType.Broken_Light;
      case PHONE:
        return FacilityMaintenanceSRAPI.MaintenanceType.Broken_Phone;
      case TOILET:
        return FacilityMaintenanceSRAPI.MaintenanceType.Broken_Toilet;
      case MACHINE:
        return FacilityMaintenanceSRAPI.MaintenanceType.Broken_Machine;
      case COMPUTER:
        return FacilityMaintenanceSRAPI.MaintenanceType.Broken_Computer;
      case MEDICALTOOL:
        return FacilityMaintenanceSRAPI.MaintenanceType.Broken_Medical_Tool;
      default:
        return null;
    }
  }

  FacilityMaintenanceRequest.MaintenanceType convertFromAPIMaintenanceType(
      FacilityMaintenanceSRAPI.MaintenanceType apiMaintenanceType) {
    switch (apiMaintenanceType) {
      case Broken_Medical_Tool:
        return FacilityMaintenanceRequest.MaintenanceType.MEDICALTOOL;
      case Broken_Computer:
        return FacilityMaintenanceRequest.MaintenanceType.COMPUTER;
      case Broken_Machine:
        return FacilityMaintenanceRequest.MaintenanceType.MACHINE;
      case Broken_Toilet:
        return FacilityMaintenanceRequest.MaintenanceType.TOILET;
      case Broken_Phone:
        return FacilityMaintenanceRequest.MaintenanceType.PHONE;
      case Broken_Light:
        return FacilityMaintenanceRequest.MaintenanceType.LIGHT;
      case Broken_Sink:
        return FacilityMaintenanceRequest.MaintenanceType.SINK;
      case Broken_Pipe:
        return FacilityMaintenanceRequest.MaintenanceType.PIPE;
      default:
        return null;
    }
  }

  int convertToAPIRequestID(String id) {
    return Integer.parseInt(id);
  }

  String convertFromAPIRequestID(int id) {
    return Integer.toString(id);
  }

  // ---------- Employee Converters ----------
  EmployeeAPI convertToAPIEmployee(Employee emp) {
    EmployeeAPI apiEmp = new EmployeeAPI(convertToAPIEmployeeID(emp.getEmployeeID()));
    apiEmp.setFirstName(emp.getName());
    return apiEmp;
  }

  int convertToAPIEmployeeID(String id) {
    for (String key : employeeIDMap.keySet()) {
      if (id.startsWith(key)) {
        String firstNum = employeeIDMap.get(key);
        String restNum = id.replaceFirst(key, "");
        return Integer.parseInt(firstNum + restNum);
      }
    }

    System.out.println("Facility Maintenance API: Invalid conversion to API employee id.");
    return Integer.parseInt(id);
  }

  String convertFromAPIEmployeeID(int id) {
    String idStr = Integer.toString(id);
    String firstNum = Character.toString(idStr.charAt(0));
    String restNum = idStr.substring(1);

    String empType = "";
    for (String key : employeeIDMap.keySet()) {
      if (employeeIDMap.get(key).equals(firstNum)) {
        empType = key;
        break;
      }
    }

    if (empType.equals("")) {
      System.out.println("Facility Maintenance API: Invalid conversion from API employee id.");
    }

    return empType + restNum;
  }
}
