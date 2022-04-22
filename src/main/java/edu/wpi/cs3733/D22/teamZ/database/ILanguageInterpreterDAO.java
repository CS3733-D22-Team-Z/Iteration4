package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.LanguageInterpreterRequest;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public interface ILanguageInterpreterDAO {

  List<LanguageInterpreterRequest> getAllLanguageInterpreterServiceRequests() throws SQLException;

  LanguageInterpreterRequest getLanguageInterpreterRequestByID(String requestID);

  boolean addLanguageInterpreterRequest(LanguageInterpreterRequest request);

  boolean updateLanguageInterpreterRequest(LanguageInterpreterRequest request);

  boolean deleteLanguageInterpreterRequest(LanguageInterpreterRequest request);

  boolean exportToLanguageInterpreterRequestCSV(File data);

  int importLanguageInterpreterRequestFromCSV(File data);

  boolean addLanguageInterpreterFromList(List<LanguageInterpreterRequest> list);
}
