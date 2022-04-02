package edu.wpi.cs3733.D22.teamZ.entity;

public class ServiceRequest {
	protected String requestID;
	protected RequestType type;
	protected String itemID;
	protected RequestStatus status;
	protected Employee issuer;
	protected Employee handler;
	protected Location targetLocation;

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

	/**
	 * Constructs a ServiceRequest object. By default, handler is null
	 * and status is RequestStatus.UNASSIGNED.
	 * @param requestID The unique ID for this ServiceRequest object.
	 * @param type The type of service request this is. Must come from the RequestType enum.
	 * @param itemID The unique ID for the item assigned to this request
	 * @param targetLocation The location at which this request will take place
	 * @param issuer The employee that issued the request
	 */
	public ServiceRequest(String requestID, RequestType type, String itemID, RequestStatus status, Employee issuer, Employee handler, Location targetLocation) {
		this.requestID = requestID;
		this.type = type;
		this.itemID = itemID;
		this.targetLocation = targetLocation;
		this.status = status;
		this.issuer = issuer;
		this.handler = handler;
	}

	/**
	 * Gets the requestID for this ServiceRequest
	 * @return A String representing the requestID
	 */
	public final String getRequestID() {
		return this.requestID;
	}

	/**
	 * Gets the type of ServiceRequest that this is
	 * @return The RequestType of this object
	 */
	public final RequestType getType() {
		return this.type;
	}

	/**
	 * Gets the itemID of the item assigned to this service request
	 * @return A String representing the itemID
	 */
	public final String getItemID() {
		return itemID;
	}

	/**
	 * Changes the item assigned to this service to the one with
	 * the given itemID
	 * @param itemID A String representing the itemID of the item
	 *                  assigned
	 */
	public final void setItemID(String itemID) {
		this.itemID = itemID;
	}

	/**
	 * Gets the current status of this service request
	 * @return The RequestStatus associated with this service request
	 */
	public final RequestStatus getStatus() {
		return status;
	}

	/**
	 * Sets the current status of this service request
	 * @param status The status that this request should be set to
	 */
	public final void setStatus(RequestStatus status) {
		this.status = status;
	}

	/**
	 * Gets the Employee who issued this service request
	 * @return The Employee who issued this service request
	 */
	public final Employee getIssuer() {
		return issuer;
	}

	/**
	 * Gets the Employee who is assigned to this service request
	 * @return The Employee who is assigned to this service request
	 */
	public final Employee getHandler() {
		return handler;
	}

	/**
	 * Sets the Employee who will be assigned to this service request
	 * @param handler The Employee who will handle this request
	 */
	public final void setHandler(Employee handler) {
		this.handler = handler;
	}

	/**
	 * Gets the Location where this service request will occur
	 * @return The Location that this service request is assigned to
	 */
	public final Location getTargetLocation() {
		return targetLocation;
	}

	/**
	 * Sets the Location where this service request will be assigned
	 * @param targetLocation The Location that this request will take
	 *                          place
	 */
	public final void setTargetLocation(Location targetLocation) {
		this.targetLocation = targetLocation;
	}
}
