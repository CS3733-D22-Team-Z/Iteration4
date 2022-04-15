package edu.wpi.cs3733.D22.teamZ.database;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

abstract class ControlCSV {

  private File defaultPath;

  ControlCSV() {
    defaultPath = new File("");
  }

  void setDefaultPath(File defaultPath) {
    this.defaultPath = defaultPath;
  }

  File getDefaultPath() { return this.defaultPath; }

  final void writeCSV(List<List<String>> in, String... headers) throws IOException {
    writeCSV(in, defaultPath, headers);
  }

  final List<List<String>> readCSV() throws IOException {
    return readCSV(defaultPath);
  }

  final void writeCSV(List<List<String>> in, File path, String... headers) throws IOException {
    String fLine = String.join(",", headers);
    FileWriter file = new FileWriter(path);
    file.write(fLine + "\n");

    for (List<String> a : in) {
      String line = String.join(",", a);
      line += "\n";

      file.write(line);
    }

    file.close();
  }

  final List<List<String>> readCSV(File path) throws IOException {
    List<List<String>> mid = new ArrayList<>();

    try {
      FileReader temp = new FileReader(path);

      BufferedReader in = new BufferedReader(temp);

      in.readLine();

      String line;
      while ((line = in.readLine()) != null) {
        String[] split = line.split(",");
        mid.add(List.of(split));
      }
      in.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return mid;
  }
}
