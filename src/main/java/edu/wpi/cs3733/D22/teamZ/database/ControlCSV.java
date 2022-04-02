package edu.wpi.cs3733.D22.teamZ.database;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ControlCSV {

  private File path;

  public ControlCSV() {
    path = new File("");
  }

  public void setPath(File path) {
    this.path = path;
  }

  protected final void writeCSV(List<List<String>> in, String... headers) {
    String fLine = String.join(",", headers);
    FileWriter file = null;
    try {
      file = new FileWriter(path);
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      file.write(fLine + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
    // List<List<String>> data = objToData(in);

    // int n = data.get(0).size();

    for (List<String> a : in) {

      String line = String.join(",", a);

      line += "\n";

      try {
        file.write(line);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    try {
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected final List<List<String>> readCSV() throws IOException {
    FileReader temp = null;
    try {
      temp = new FileReader(path);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    BufferedReader in = new BufferedReader(temp);

    String line = null;
    try {
      line = in.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
    List<List<String>> mid = new ArrayList<>();
    int c = 0;

    while ((line = in.readLine()) != null) {
      String[] split = line.split(",");
      mid.add(List.of(split));
    }
    in.close();

    return mid;
  }
}
