package edu.wpi.cs3733.D22.teamZ.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

public class MealServiceController extends ServiceRequestController {
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    menuName = "Meal Request";
  }

  @Override
  protected void onSubmitButtonClicked(ActionEvent event) throws SQLException {
    // Add submitting functionality here!
  }

  @Override
  protected void onResetButtonClicked(ActionEvent event) throws IOException {
    // Reset fields here!
  }
}
