package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.LabServiceRequest;
import java.sql.SQLException;
import java.util.List;

public interface ILabRequestServiceDAO {
  List<LabServiceRequest> getAllLabServiceRequests() throws SQLException;

  LabServiceRequest getLabRequestByID(String requestID);

  boolean addLabRequest(LabServiceRequest request);

  void updateLabRequest(LabServiceRequest request);

  void deleteLabRequest(LabServiceRequest request);

  boolean exportToLabRequestCSV();
}
