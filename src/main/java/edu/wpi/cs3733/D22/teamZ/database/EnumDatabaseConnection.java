package edu.wpi.cs3733.D22.teamZ.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum EnumDatabaseConnection {
  CONNECTION(null, "");
  private Connection connection;
  private String type;

  private EnumDatabaseConnection(Connection connection, String type) {
    connection = connection;
    type = type;
  }

  public Connection getConnection() {
    return connection;
  }

  public void setConnection(String type) {
    this.type = type;
    String url = "";
    if (type.equalsIgnoreCase("embedded")) {
      url = "jdbc:derby:myDB;create=true";
    } else {
      url = "jdbc:derby://localhost:1527/TZDB;create=true";
    }
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      Class.forName("org.apache.derby.jdbc.ClientDriver");
      connection = DriverManager.getConnection(url);
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }

  public String getType() {
    return type;
  }
}
