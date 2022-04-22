package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.PathEdge;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EdgesControlCSV extends ControlCSV {
  private final String[] headers = {"startNode", "endNode"};

  public EdgesControlCSV(File path) {
    this.setDefaultPath(path);
  }

  protected void writeEdgeCSV(List<PathEdge> in) throws IOException {
    writeCSV(objToData(in), headers);
  }

  protected void writeEdgeCSV(List<PathEdge> in, File path) throws IOException {
    writeCSV(objToData(in), path, headers);
  }

  protected List<PathEdge> readEdgeCSV() throws IOException {
    return dataToObj(readCSV());
  }

  protected List<PathEdge> readEdgeCSV(File path) throws IOException {
    return dataToObj(readCSV(path));
  }

  private List<PathEdge> dataToObj(List<List<String>> data) {
    FacadeDAO dao = FacadeDAO.getInstance();
    List<PathEdge> ret = new ArrayList<>();
    for (List<String> a : data) {
      ret.add(new PathEdge(dao.getLocationByID(a.get(0)), dao.getLocationByID(a.get(1))));
    }
    return ret;
  }

  protected List<List<String>> objToData(List<PathEdge> in) {
    List<List<String>> ret = new ArrayList<>();

    for (PathEdge edge : in) {
      List<String> entry =
          new ArrayList<>(
              List.of(new String[] {edge.getFrom().getNodeID(), edge.getDest().getNodeID()}));
      ret.add(entry);
    }
    return ret;
  }
}
