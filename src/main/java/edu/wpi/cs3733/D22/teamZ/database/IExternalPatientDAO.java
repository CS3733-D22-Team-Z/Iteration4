package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.ExternalPatientTransportationRequest;

public interface IExternalPatientDAO {

  /**
   * Adds a ExternalPatientTransportationRequest to the database
   *
   * @param request request to be added
   * @return True if successful, false otherwise
   */
  boolean addPatientTransportRequest(ExternalPatientTransportationRequest request);
}
