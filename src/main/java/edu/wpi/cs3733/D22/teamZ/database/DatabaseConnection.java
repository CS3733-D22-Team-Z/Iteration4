package edu.wpi.cs3733.D22.teamZ.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

  private static Connection con = null;

  static {
    String url = "jdbc:derby:myDB;create=true";
    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
      con = DriverManager.getConnection(url);
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }

  public static Connection getConnection() {
    return con;
  }
}
