package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.controllers.ISearchable;
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
}
