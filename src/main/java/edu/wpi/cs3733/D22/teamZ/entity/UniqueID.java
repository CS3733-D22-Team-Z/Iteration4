package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import java.util.Random;

public class UniqueID {
  public UniqueID() {}

  public String generateID(String type) {
    String id = type;
    Random rand = new Random();
    int int_random = rand.nextInt(9) + 1;
    id += int_random;
    while (FacadeDAO.getInstance().getServiceRequestByID(id) != null) {
      int_random = rand.nextInt(10);
      id += int_random;
    }
    return id;
  }
}
