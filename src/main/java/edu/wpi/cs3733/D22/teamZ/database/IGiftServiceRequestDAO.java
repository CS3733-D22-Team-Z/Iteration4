package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.GiftServiceRequest;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public interface IGiftServiceRequestDAO {

  List<GiftServiceRequest> getAllGiftServiceRequests() throws SQLException;

  GiftServiceRequest getGiftRequestByID(String requestID);

  boolean addGiftRequest(GiftServiceRequest request);

  boolean updateGiftRequest(GiftServiceRequest request);

  boolean deleteGiftRequest(GiftServiceRequest request);

  boolean exportToGiftRequestCSV(File data);

  int importGiftRequestFromCSV(File data);

  boolean addGiftRequestFromList(List<GiftServiceRequest> list);
}
