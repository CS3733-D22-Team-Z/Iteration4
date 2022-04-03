package edu.wpi.cs3733.D22.teamZ.entity;

public class Patient {
    private String patientID;
    private String name;
    private String location;

    public Patient(){}

    public Patient(String patientID, String name, String location){
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
