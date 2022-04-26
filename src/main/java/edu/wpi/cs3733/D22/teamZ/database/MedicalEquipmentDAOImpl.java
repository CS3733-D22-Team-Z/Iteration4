package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class MedicalEquipmentDAOImpl implements IMedicalEquipmentDAO {

    Connection connection = EnumDatabaseConnection.CONNECTION.getConnection();
    // DatabaseConnection.getConnection();
    List<MedicalEquipment> medicalEquipmentsList;

    MedicalEquipmentControlCSV medicalEquipmentControlCSV;

    /**
     * Constructor for MedicalEquipmentDAOImpl
     */
    public MedicalEquipmentDAOImpl() {
        updateConnection();
        medicalEquipmentsList = new ArrayList<>();
    }

    /**
     * Get all MedicalEquipment in the database
     *
     * @return list of MedicalEquipment
     */
    @Override
    public List<MedicalEquipment> getAllMedicalEquipment() {
        updateConnection();
    /*try {
      PreparedStatement pstmt = connection.prepareStatement("Select * From MEDICALEQUIPMENT");
      ResultSet rset = pstmt.executeQuery();

      medicalEquipmentsList.clear();

      while (rset.next()) {
        String itemID = rset.getString("equipmentID");
        String type = rset.getString("type");
        String status = rset.getString("status");
        String locationNodeID = rset.getString("currentLocation");
        MedicalEquipment medicalEquipment =
            new MedicalEquipment(
                itemID, type, status, FacadeDAO.getInstance().getLocationByID(locationNodeID));
        if (!medicalEquipmentsList.contains(medicalEquipment)) {
          medicalEquipmentsList.add(medicalEquipment);
        }
      }
    } catch (SQLException e) {
      System.out.println("Failed to get all Medical Equipment");
    }*/
        return medicalEquipmentsList;
    }

    /**
     * Get MedicalEquipment with the given ID
     *
     * @param itemID ID of MedicalEquipment to be fetched
     * @return MedicalEquipment with the given ID
     */
    @Override
    public MedicalEquipment getMedicalEquipmentByID(String itemID) {
        updateConnection();
    /*MedicalEquipment medicalEquipment = new MedicalEquipment(itemID);
    try {
      PreparedStatement pstmt =
          connection.prepareStatement("Select * From MEDICALEQUIPMENT WHERE EQUIPMENTID = ?");
      pstmt.setString(1, itemID);
      ResultSet rset = pstmt.executeQuery();

      while (rset.next()) {
        String type = rset.getString("type");
        String status = rset.getString("status");
        String locationNodeID = rset.getString("currentLocation");
        medicalEquipment.setStatus(status);
        medicalEquipment.setType(type);
        medicalEquipment.setCurrentLocation(
            FacadeDAO.getInstance().getLocationByID(locationNodeID));
      }
    } catch (SQLException e) {
      System.out.println("Failed to get the Medical Equipment");
    }*/
        for (MedicalEquipment medicalEquipment : medicalEquipmentsList) {
            if (medicalEquipment.getEquipmentID().equals(itemID)) {
                return medicalEquipment;
            }
        }
        return null;
    }

    /**
     * Get the first avalable equipment with the given equipment type
     *
     * @param equipment type of equipment
     * @return equipmentID of the first available equipment of the given type
     */
    @Override
    public String getFirstAvailableEquipmentByType(String equipment) {
        updateConnection();

        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(
                            "Select * From MEDICALEQUIPMENT WHERE TYPE = ? AND STATUS = 'CLEAN'");
            pstmt.setString(1, equipment);
            ResultSet rset = pstmt.executeQuery();

            rset.next();
            String temp = rset.getString("EQUIPMENTID");
            rset.close();
            if (temp != null) {
                pstmt =
                        connection.prepareStatement(
                                "UPDATE MEDICALEQUIPMENT SET STATUS = 'INUSE' WHERE EQUIPMENTID = ?");
                pstmt.setString(1, temp);
                pstmt.executeUpdate();
                return temp;
            }

        } catch (SQLException e) {
            System.out.println("Failed to get the Medical Equipment");
        }
        // Only returns if no equipments of type are available
        return null;
    }

    /**
     * Gets all MedicalEquipment in a given location
     *
     * @param location Location to extract MedicalEquipment inside
     * @return list of MedicalEquipment in the given location
     */
    @Override
    public List<MedicalEquipment> getAllMedicalEquipmentByLocation(Location location) {
        updateConnection();
        List<MedicalEquipment> medicalEquipmentLocationList = new ArrayList<>();
        try {
            PreparedStatement pstnt =
                    connection.prepareStatement("select * from MEDICALEQUIPMENT where currentLocation = ?");
            pstnt.setString(1, location.getNodeID());
            ResultSet rset = pstnt.executeQuery();
            while (rset.next()) {
                String tempItemID = rset.getString("EQUIPMENTID");
                for (MedicalEquipment medicalEquipment : medicalEquipmentsList) {
                    if (medicalEquipment.getEquipmentID().equals(tempItemID)) {
                        medicalEquipmentLocationList.add(medicalEquipment);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("failed to get medical equipment by location");
            medicalEquipmentLocationList.clear();
        }
        return medicalEquipmentLocationList;
    }

    /**
     * Adds MedicalEquipment to the database
     *
     * @param equipment MedicalEquipment to be added
     * @return True if successful, false otherwise
     */
    @Override
    public boolean addMedicalEquipment(MedicalEquipment equipment) {
        updateConnection();
        boolean val = false;
        if (addToDatabase(equipment)) {
            val = true;
            medicalEquipmentsList.add(equipment);
        }
        return val;
    }

    /**
     * Updates existing MedicalEquipment in the database with an updated MedicalEquipment
     *
     * @param equipment Updated MedicalEquipment
     * @return True if successful, false otherwise
     */
    @Override
    public boolean updateMedicalEquipment(MedicalEquipment equipment) {
        updateConnection();
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(
                            ""
                                    + "UPDATE MEDICALEQUIPMENT SET status = ?, currentLocation = ? WHERE EQUIPMENTID = ?");
            pstmt.setString(1, equipment.getStatus().toString());
            pstmt.setString(2, equipment.getCurrentLocation().getNodeID());
            pstmt.setString(3, equipment.getEquipmentID());

            pstmt.executeUpdate();
            for (MedicalEquipment medicalEquipment : medicalEquipmentsList) {
                if (medicalEquipment.equals(equipment)) {
                    medicalEquipment.setStatus(equipment.getStatus());
                    medicalEquipment.setCurrentLocation(equipment.getCurrentLocation());
                    return true;
                }
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Failed to update MedicalEquipment");
            return false;
        }
    }

    /**
     * Deletes MedicalEquipment in the database
     *
     * @param equipment MedicalEquipment to be deleted
     * @return True if successful, false otherwise
     */
    @Override
    public boolean deleteMedicalEquipment(MedicalEquipment equipment) {
        updateConnection();
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement("" + "DELETE FROM MEDICALEQUIPMENT WHERE EQUIPMENTID = ?");
            pstmt.setString(1, equipment.getEquipmentID());

            pstmt.executeUpdate();
            equipment.getCurrentLocation().removeEquipmentFromList(equipment);
            medicalEquipmentsList.remove(equipment);
        } catch (SQLException e) {
            System.out.println("Failed to delete from database");
            return false;
        }
        return true;
    }

    /**
     * Exports the MedicalEquipment in the database to the specified file location of csv
     *
     * @param equipmentData File location of csv
     * @return True if successful, false otherwise
     */
    @Override
    public boolean exportToMedicalEquipmentCSV(File equipmentData) {
        updateConnection();
        medicalEquipmentControlCSV = new MedicalEquipmentControlCSV(equipmentData);
        try {
            medicalEquipmentControlCSV.writeMedicalEquipmentCSV(medicalEquipmentsList);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Imports the MedicalEquipment into the database from specified file location for csv
     *
     * @param equipmentData file location for csv
     * @return number of conflicts when importing
     */
    @Override
    public int importMedicalEquipmentFromCSV(File equipmentData) {
        updateConnection();
        medicalEquipmentControlCSV = new MedicalEquipmentControlCSV(equipmentData);
        int conflictCounter = 0;
        String temp = "";
        try {
            List<MedicalEquipment> tempMedicalEquipment =
                    medicalEquipmentControlCSV.readMedicalEquipmentCSV();

            try {
                for (MedicalEquipment info : tempMedicalEquipment) {
                    PreparedStatement pstmt =
                            connection.prepareStatement(
                                    "INSERT INTO MEDICALEQUIPMENT (EQUIPMENTID, TYPE, STATUS, CURRENTLOCATION) "
                                            + "values (?, ?, ?, ?)");
                    temp = info.getEquipmentID();
                    pstmt.setString(1, info.getEquipmentID());
                    pstmt.setString(2, info.getType());
                    pstmt.setString(3, info.getStatus().toString());
                    pstmt.setString(4, info.getCurrentLocation().getNodeID());

                    // insert it
                    pstmt.executeUpdate();
                }
            } catch (SQLException e) {
                conflictCounter++;
                System.out.println(
                        "Found "
                                + conflictCounter
                                + " conflicts. "
                                + temp
                                + " is in location that does not exist.");
            }
        } catch (IOException e) {
            System.out.println("Failed to populate MedicalEquipment table");
        }
        return conflictCounter;
    }

    /**
     * Updates the connection
     */
    private void updateConnection() {
        connection = EnumDatabaseConnection.CONNECTION.getConnection();
    }

    /**
     * Insert Medical Equipment into database from list
     *
     * @param list list of medical equipment to be added
     * @return True if successful, false otherwise
     */
    public boolean addMedicalEquipmentFromList(List<MedicalEquipment> list) {
        updateConnection();
        boolean val = true;
        for (MedicalEquipment info : list) {
            if (!addToDatabase(info)) {
                val = false;
            }
        }
        return val;
    }

    /**
     * Contains SQL command to insert MedicalEquipment to database
     *
     * @param equipment Medical Equipment to be added
     * @return True if successful, false otherwise
     */
    private boolean addToDatabase(MedicalEquipment equipment) {
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(
                            ""
                                    + "INSERT INTO MEDICALEQUIPMENT (EQUIPMENTID, type, status, currentLocation)"
                                    + "values (?, ?, ?, ?)");
            pstmt.setString(1, equipment.getEquipmentID());
            pstmt.setString(2, equipment.getType());
            pstmt.setString(3, equipment.getStatus().toString());
            pstmt.setString(4, equipment.getCurrentLocation().getNodeID());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to insert new MedicalEquipment");
            return false;
        }
        return true;
    }

    /**
     * Get all Medical Equipment in given floor
     *
     * @param floor floor to be searched
     * @return list of medical equipment for given floor
     */
    public List<MedicalEquipment> getAllMedicalEquipmentByFloor(String floor) {
        updateConnection();
        List<MedicalEquipment> tempMedEquipList = new ArrayList<>();
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(
                            "SELECT * FROM MEDICALEQUIPMENT, LOCATION WHERE CURRENTLOCATION = NODEID AND FLOOR = ?");
            pstmt.setString(1, floor);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                String id = rset.getString("EQUIPMENTID");
                for (MedicalEquipment equipment : medicalEquipmentsList) {
                    if (equipment.getEquipmentID().equals(id)) {
                        tempMedEquipList.add(equipment);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed medical equipment by floor");
            e.printStackTrace();
        }
        return tempMedEquipList;
    }

    /**
     * Get dirty equipment for the specified floor
     *
     * @param floor floor to be searched
     * @return number of dirty equipment
     */
    public int countDirtyEquipmentByFloor(String floor) {
        updateConnection();
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(
                            "SELECT COUNT(EQUIPMENTID) AS COUNT "
                                    + "FROM MEDICALEQUIPMENT, LOCATION WHERE MEDICALEQUIPMENT.CURRENTLOCATION = LOCATION.NODEID "
                                    + "AND LOCATION.FLOOR = ? AND MEDICALEQUIPMENT.STATUS = 'DIRTY'");
            pstmt.setString(1, floor);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                return rset.getInt("COUNT");
            }
        } catch (SQLException e) {
            System.out.println("Count dirty equipment by floor failed");
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get clean equipment for the specified floor
     *
     * @param floor floor to be searched
     * @return number of clean equipment
     */
    public int countCleanEquipmentByFloor(String floor) {
        updateConnection();
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(
                            "SELECT COUNT(EQUIPMENTID) AS COUNT "
                                    + "FROM MEDICALEQUIPMENT, LOCATION WHERE MEDICALEQUIPMENT.CURRENTLOCATION = LOCATION.NODEID "
                                    + "AND LOCATION.FLOOR = ? AND MEDICALEQUIPMENT.STATUS = 'CLEAN'");
            pstmt.setString(1, floor);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                return rset.getInt("COUNT");
            }
        } catch (SQLException e) {
            System.out.println("Count clean equipment by floor failed");
            e.printStackTrace();
        }
        return 0;
    }

    public int countDirtyBedsByFloor(String floor) {
        updateConnection();
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(
                            "SELECT COUNT(EQUIPMENTID) AS COUNT "
                                    + "FROM MEDICALEQUIPMENT, LOCATION WHERE MEDICALEQUIPMENT.CURRENTLOCATION = LOCATION.NODEID "
                                    + "AND LOCATION.FLOOR = ? AND MEDICALEQUIPMENT.STATUS = 'DIRTY' AND MEDICALEQUIPMENT.TYPE = 'Bed'");
            pstmt.setString(1, floor);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                return rset.getInt("COUNT");
            }
        } catch (SQLException e) {
            System.out.println("Count dirty beds by floor failed");
            e.printStackTrace();
        }
        return 0;
    }

    public int countCleanBedsByFloor(String floor) {
        updateConnection();
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(
                            "SELECT COUNT(EQUIPMENTID) AS COUNT "
                                    + "FROM MEDICALEQUIPMENT, LOCATION WHERE MEDICALEQUIPMENT.CURRENTLOCATION = LOCATION.NODEID "
                                    + "AND LOCATION.FLOOR = ? AND MEDICALEQUIPMENT.STATUS = 'CLEAN' AND MEDICALEQUIPMENT.TYPE = 'Bed'");
            pstmt.setString(1, floor);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                return rset.getInt("COUNT");
            }
        } catch (SQLException e) {
            System.out.println("Count clean beds by floor failed");
            e.printStackTrace();
        }
        return 0;
    }

    public int countDirtyIPumpsByFloor(String floor) {
        updateConnection();
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(
                            "SELECT COUNT(EQUIPMENTID) AS COUNT "
                                    + "FROM MEDICALEQUIPMENT, LOCATION WHERE MEDICALEQUIPMENT.CURRENTLOCATION = LOCATION.NODEID "
                                    + "AND LOCATION.FLOOR = ? AND MEDICALEQUIPMENT.STATUS = 'DIRTY' AND MEDICALEQUIPMENT.TYPE = 'IPumps'");
            pstmt.setString(1, floor);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                return rset.getInt("COUNT");
            }
        } catch (SQLException e) {
            System.out.println("Count dirty IPumps by floor failed");
            e.printStackTrace();
        }
        return 0;
    }

    public int countCleanIPumpsByFloor(String floor) {
        updateConnection();
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(
                            "SELECT COUNT(EQUIPMENTID) AS COUNT "
                                    + "FROM MEDICALEQUIPMENT, LOCATION WHERE MEDICALEQUIPMENT.CURRENTLOCATION = LOCATION.NODEID "
                                    + "AND LOCATION.FLOOR = ? AND MEDICALEQUIPMENT.STATUS = 'CLEAN' AND MEDICALEQUIPMENT.TYPE = 'IPumps'");
            pstmt.setString(1, floor);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                return rset.getInt("COUNT");
            }
        } catch (SQLException e) {
            System.out.println("Count clean IPumps by floor failed");
            e.printStackTrace();
        }
        return 0;
    }

    public int countDirtyReclinersByFloor(String floor) {
        updateConnection();
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(
                            "SELECT COUNT(EQUIPMENTID) AS COUNT "
                                    + "FROM MEDICALEQUIPMENT, LOCATION WHERE MEDICALEQUIPMENT.CURRENTLOCATION = LOCATION.NODEID "
                                    + "AND LOCATION.FLOOR = ? AND MEDICALEQUIPMENT.STATUS = 'DIRTY' AND MEDICALEQUIPMENT.TYPE = 'Recliner'");
            pstmt.setString(1, floor);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                return rset.getInt("COUNT");
            }
        } catch (SQLException e) {
            System.out.println("Count dirty recliners by floor failed");
            e.printStackTrace();
        }
        return 0;
    }

    public int countCleanReclinersByFloor(String floor) {
        updateConnection();
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(
                            "SELECT COUNT(EQUIPMENTID) AS COUNT "
                                    + "FROM MEDICALEQUIPMENT, LOCATION WHERE MEDICALEQUIPMENT.CURRENTLOCATION = LOCATION.NODEID "
                                    + "AND LOCATION.FLOOR = ? AND MEDICALEQUIPMENT.STATUS = 'CLEAN' AND MEDICALEQUIPMENT.TYPE = 'Recliner'");
            pstmt.setString(1, floor);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                return rset.getInt("COUNT");
            }
        } catch (SQLException e) {
            System.out.println("Count clean recliners by floor failed");
            e.printStackTrace();

        }
        return 0;
    }

    public int countDirtyXRaysByFloor(String floor) {
        updateConnection();
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(
                            "SELECT COUNT(EQUIPMENTID) AS COUNT "
                                    + "FROM MEDICALEQUIPMENT, LOCATION WHERE MEDICALEQUIPMENT.CURRENTLOCATION = LOCATION.NODEID "
                                    + "AND LOCATION.FLOOR = ? AND MEDICALEQUIPMENT.STATUS = 'CLEAN' AND MEDICALEQUIPMENT.TYPE = 'XRay'");
            pstmt.setString(1, floor);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                return rset.getInt("COUNT");
            }
        } catch (SQLException e) {
            System.out.println("Count clean XRays by floor failed");
            e.printStackTrace();
        }
        return 0;
    }

    public int countCleanXRaysByFloor(String floor) {
        return 0;

    }
}