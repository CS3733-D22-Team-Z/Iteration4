package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.Location;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationControlCSV extends ControlCSV {
  private final String[] headers = {
    "NodeID", "xcoord", "ycoord", "floor", "building", "nodeType", "longName", "shortName"
  };

  public LocationControlCSV(File path) {
    this.setDefaultPath(path);
  }

  protected void writeLocCSV(List<Location> in) throws IOException {
    writeCSV(objToData(in), headers);
  }

  public List<Location> readLocCSV() throws IOException {
    return dataToObj(readCSV());
  }

  protected void writeLocCSV(List<Location> in, File path) throws IOException {
    writeCSV(objToData(in), path, headers);
  }

  public List<Location> readLocCSV(File path) throws IOException {
    return dataToObj(readCSV(path));
  }

  private List<Location> dataToObj(List<List<String>> data) {
    List<Location> ret = new ArrayList<>();
    for (List<String> a : data) {
      ret.add(
          new Location(
              a.get(0),
              Integer.parseInt(a.get(1)),
              Integer.parseInt(a.get(2)),
              a.get(3),
              a.get(4),
              a.get(5),
              a.get(6),
              a.get(7)));
    }
    return ret;
  }

  protected List<List<String>> objToData(List<Location> in) {
    List<List<String>> ret = new ArrayList<>();

    for (Location a : in) {
      List<String> entry =
          new ArrayList<>(
              List.of(
                  new String[] {
                    a.getNodeID(),
                    Integer.toString(a.getXcoord()),
                    Integer.toString(a.getYcoord()),
                    a.getFloor(),
                    a.getBuilding(),
                    a.getNodeType(),
                    a.getLongName(),
                    a.getShortName()
                  }));
      ret.add(entry);
    }
    return ret;
  }
}
