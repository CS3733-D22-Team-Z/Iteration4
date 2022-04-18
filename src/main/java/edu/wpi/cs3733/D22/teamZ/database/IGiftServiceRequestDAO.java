package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.GiftServiceRequest;
import edu.wpi.cs3733.D22.teamZ.entity.LabServiceRequest;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public interface IGiftServiceRequestDAO {

    List<GiftServiceRequest> getAllLabServiceRequests() throws SQLException;

    LabServiceRequest getLabRequestByID(String requestID);

    boolean addLabRequest(LabServiceRequest request);

    boolean updateLabRequest(LabServiceRequest request);

    boolean deleteLabRequest(LabServiceRequest request);

    boolean exportToLabRequestCSV(File data);

    int importLabRequestFromCSV(File data);

    boolean addLabRequestFromList(List<LabServiceRequest> list);
}
