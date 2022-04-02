package edu.wpi.cs3733.D22.teamZ.entity;

public class Employee {
	private String employeeID;
	private String name;
	private AccessType accesstype;
	private String username;
	private String password;

	public enum AccessType {
		ADMIN,
		DOCTOR,
		NURSE
	}
}
