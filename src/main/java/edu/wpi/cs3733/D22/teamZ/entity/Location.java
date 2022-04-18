package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.controllers.ISearchable;
import edu.wpi.cs3733.D22.teamZ.controllers.MenuController;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;

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
      if(this.nodeType.equals("DIRT")){
        //THIS IS FOR 10 OR MORE
        if(FacadeDAO.getInstance().countDirtyInfusionPumpsInFloor(this.floor) > 9){
          for(MedicalEquipment equipmentObj : this.equipmentList){
            String id;
            // Check for empty db and set first request (will appear as REQ1 in the db)

            if (FacadeDAO.getInstance().getAllServiceRequests().isEmpty()) {
              System.out.println("Service requests are empty");
              id = "REQ0";
            } else {
              List<ServiceRequest> currentList = FacadeDAO.getInstance().getAllServiceRequests();
              ServiceRequest lastestReq = currentList.get(currentList.size() - 1);
              id = lastestReq.getRequestID();
            }
            // Create new REQID
            int num = 1 + Integer.parseInt(id.substring(id.lastIndexOf("Q") + 1));
            String requestID = "REQ" + num;
            ServiceRequest.RequestStatus status = ServiceRequest.RequestStatus.UNASSIGNED;
            Employee issuer = MenuController.getLoggedInUser();
            Employee handler = null;
            MedicalEquipmentDeliveryRequest request = new MedicalEquipmentDeliveryRequest(requestID, status, issuer,
                    handler, equipmentObj.getEquipmentID(), FacadeDAO.getInstance().getLocationByID("zSTOR00101"));
            FacadeDAO.getInstance().addMedicalEquipmentRequest(request);

            //CREATE ALERT
          }
        }
      }
    }
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
}
