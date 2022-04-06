package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.MealServiceRequest;
import java.util.List;

public interface IMealServiceReqDAO {

  // TODO: TEMPORARY FILE. FIX

  List<MealServiceRequest> getAllMealServiceRequest();

  MealServiceRequest getMealServiceReqByID(String requestID);

  void addMealServiceRequest(MealServiceRequest req);

  void updateMealServiceRequest(MealServiceRequest req);

  void deleteMealServiceRequest(MealServiceRequest req);

  boolean exportToMealServiceRequestCSV();
}
