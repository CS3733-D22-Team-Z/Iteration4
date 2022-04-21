package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.controllers.ISearchable;
import edu.wpi.cs3733.D22.teamZ.observers.DashboardBedAlertObserver;
import edu.wpi.cs3733.D22.teamZ.observers.DirtyBedObserver;
import edu.wpi.cs3733.D22.teamZ.observers.DirtyPumpObserver;
import java.util.ArrayList;
import java.util.List;

public class Location implements ISearchable {
  private String nodeID;
  private int xcoord;
  private int ycoord;
  private String floor;
  private String building;
  private String nodeType;
  private String longName;
  private String shortName;
  private List<MedicalEquipment> equipmentList;
  private List<DirtyBedObserver> bedObservers;
  private List<DashboardBedAlertObserver> alertObserver;
  private List<DirtyPumpObserver> dirtyPumpObservers;

  public Location() {}

  public Location(
      String nodeID,
      int xcoord,
      int ycoord,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {
    this.nodeID = nodeID;
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    this.floor = floor;
    this.building = building;
    this.nodeType = nodeType;
    this.longName = longName;
    this.shortName = shortName;
    this.equipmentList = new ArrayList<>();
    this.bedObservers = new ArrayList<>();
    this.alertObserver = new ArrayList<>();
    this.dirtyPumpObservers = new ArrayList<>();
  }

  public Location(String nodeID) {
    this.nodeID = nodeID;
  }

  public String getNodeID() {
    return nodeID;
  }

  public int getXcoord() {
    return this.xcoord;
  }

  public int getYcoord() {
    return this.ycoord;
  }

  public String getFloor() {
    return this.floor;
  }

  public String getBuilding() {
    return this.building;
  }

  public String getNodeType() {
    return this.nodeType;
  }

  public String getLongName() {
    return this.longName;
  }

  public String getShortName() {
    return this.shortName;
  }

  public void setNodeID(String nodeID) {
    this.nodeID = nodeID;
  }

  public void setXcoord(int xcoord) {
    this.xcoord = xcoord;
  }

  public void setYcoord(int ycoord) {
    this.ycoord = ycoord;
  }

  public void setFloor(String floor) {
    this.floor = floor;
  }

  public void setNodeType(String type) {
    this.nodeType = type;
  }

  public void setBuilding(String building) {
    this.building = building;
  }

  public void setLongName(String longName) {
    this.longName = longName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public List<MedicalEquipment> getEquipmentList() {
    return equipmentList;
  }

  public void addEquipmentToList(MedicalEquipment equipment) {
    if (!this.equipmentList.contains(equipment)) {
      this.equipmentList.add(equipment);
    }
    notifyAllObservers();
  }

  public void removeEquipmentFromList(MedicalEquipment equipment) {
    this.equipmentList.remove(equipment);
  }

  @Override
  public String toString() {
    return nodeID;
  }

  @Override
  public List<String> toSearchTerms() {
    return List.of(
        nodeID, "T:" + nodeType, "F:" + floor, building, "R:" + shortName, "R:" + longName);
  }

  @Override
  public Location getAssociatedLocation() {
    return this;
  }

  @Override
  public String getDisplayName() {
    return this.longName;
  }

  public static String createNodeID(String nodeType, String roomNumber, String floor) {
    String newNodeID =
        "z"
            + nodeType
            + "0".repeat(3 - roomNumber.length())
            + roomNumber
            + "0".repeat(2 - floor.length())
            + floor;
    return newNodeID;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Location) {
      Location objectLocation = (Location) o;
      return (this.getNodeID().equals(objectLocation.getNodeID()));
    } else {
      return false;
    }
  }

  // Observers
  public void notifyAllObservers() {
    for (DirtyBedObserver obs : bedObservers) {
      obs.update();
    }
    for (DirtyPumpObserver obs : dirtyPumpObservers) {
      obs.update();
    }
  }

  public void attachBedObs(DirtyBedObserver observer) {
    bedObservers.add(observer);
    observer.update();
  }

  public void attachDirtyPumpObservers(DirtyPumpObserver observer) {
    dirtyPumpObservers.add(observer);
    observer.update();
  }

  public void detachBedObs(DirtyBedObserver observer) {
    bedObservers.remove(observer);
  }

  public void detachAlertObs(DashboardBedAlertObserver observer) {
    alertObserver.remove(observer);
  }

  public void detachDirtyPumpObservers(DirtyPumpObserver observer) {
    dirtyPumpObservers.remove(observer);
  }

  public void attachAlertObs(DashboardBedAlertObserver observer) {
    alertObserver.add(observer);
    observer.update();
  }
}
