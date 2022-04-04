package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;

import java.util.List;

public interface IServiceRequestDAO {
	List<ServiceRequest> getAllServiceRequests();
	ServiceRequest getServiceRequestByID(String serviceRequestID);
	boolean addServiceRequest(ServiceRequest request);
	boolean updateServiceRequest(ServiceRequest request);
	boolean deleteServiceRequest(ServiceRequest request);
	void writeServiceRequestsToCSV();
}
