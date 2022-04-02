package edu.wpi.cs3733.D22.teamZ.entity;

public abstract class ServiceRequest {
	private String requestID;
	private RequestType type;
	private String itemID;
	private Location targetLocation;
	private RequestStatus status;
	private Employee issuer;
	private Employee handler;
	private Patient patientID;

	public enum RequestType {
		MEDEQUIP,
		MEDIC,
		LABS,
		MEAL,
		COMP,
		LAUNDRY,
		LANG
	}

	public enum RequestStatus {
		UNASSIGNED,
		PROCESSING,
		DONE
	}

	public String getRequestID() {
		return this.requestID;
	}
}
