package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.DBInitializer;
import edu.wpi.cs3733.D22.teamZ.database.EnumDatabaseConnection;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ServerSwitcherController implements IMenuAccess {
  @FXML private Label switchServerLabel;
  @FXML private Label currentDatabaseLabel;
  @FXML private MFXButton switchServerButton;

  private DBInitializer database = new DBInitializer();
  private String currentDatabase = EnumDatabaseConnection.CONNECTION.getType();
  private String otherDatabaseOption;

  private MenuController menu;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Server Switcher";
  }

  @FXML
  private void initialize() {
    if (currentDatabase.equals("embedded")) {
      otherDatabaseOption = "client";
    } else {
      otherDatabaseOption = "embedded";
    }
    currentDatabaseLabel.setText(
        "You are currently using the " + currentDatabase + " server database");
    switchServerLabel.setText(
        "Press the button to switch to a " + otherDatabaseOption + " server database");
  }

  @FXML
  private void SwitchServerClicked(ActionEvent event) throws Exception {
    if (database.switchDatabase(otherDatabaseOption)) {
      System.out.println(EnumDatabaseConnection.CONNECTION.getConnection());
      currentDatabaseLabel.setText(
          "You are currently using the " + otherDatabaseOption + " server database");
      switchServerLabel.setText(
          "Press the button to switch to a " + currentDatabase + " server database");
      String temp = currentDatabase;
      currentDatabase = otherDatabaseOption;
      otherDatabaseOption = temp;
    } else {
      throw new Exception("Failed to switch database server");
    }
  }
}
