package edu.wpi.cs3733.D22.teamZ.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

  private static Connection con = null;
  private static ConnectionType connectionType = ConnectionType.EMBEDDED;

  private enum ConnectionType {
    EMBEDDED("embedded"),
    CLIENT("client");

    private final String type;

    ConnectionType(String type) {
      this.type = type;
    }

    @Override
    public String toString() {
      return type;
    }

    public static ConnectionType getConnectionTypeByString(String type) {
      switch (type.toLowerCase()) {
        case "embedded":
          return EMBEDDED;
        case "client":
          return CLIENT;
        default:
          return null;
      }
    }
  }

  static {
    String url = "";
    if (connectionType.equals(ConnectionType.EMBEDDED)) {
      url = "jdbc:derby:myDB;create=true";
    } else {
      url = "jdbc:derby://localhost:1527/TZDB;create=true";
    }
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      Class.forName("org.apache.derby.jdbc.ClientDriver");
      con = DriverManager.getConnection(url);
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }

  public static Connection getConnection() {
    return con;
  }

  public void setConnectionType(String type) {
    this.connectionType = ConnectionType.getConnectionTypeByString(type);
  }
}
