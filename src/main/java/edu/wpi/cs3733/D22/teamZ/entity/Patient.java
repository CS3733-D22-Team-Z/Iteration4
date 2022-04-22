package edu.wpi.cs3733.D22.teamZ.entity;

public class Patient {
  private String patientID;
  private String name;
  private Location location;

  public Patient() {}

  public Patient(String patientID, String name, Location location) {
    this.patientID = patientID;
    this.name = name;
    this.location = location;
  }

  public String getPatientID() {
    return patientID;
  }

  public void setPatientID(String patientID) {
    this.patientID = patientID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  @Override
  public String toString() {
    return patientID;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Patient) {
      Patient objectPatient = (Patient) o;
      return (this.getPatientID().equals(objectPatient.getPatientID()));
    } else {
      return false;
    }
  }
}
