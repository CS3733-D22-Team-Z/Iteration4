package edu.wpi.cs3733.D22.teamZ.entity;

public class Service {
	private String itemID;

	public Service(String itemID) {
		this.itemID = itemID;
	}

	/**
	 * Gets the itemID associated with this Service
	 * @return the String representing the itemID
	 */
	public String getItemID() {
		return itemID;
	}
}
