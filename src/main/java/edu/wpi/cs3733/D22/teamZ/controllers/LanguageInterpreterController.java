package edu.wpi.cs3733.D22.teamZ.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;

public class LanguageInterpreterController extends ServiceRequestController {

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    menuName = "Language Interpreter Request";
  }

  @Override
  protected void onSubmitButtonClicked(ActionEvent event) throws SQLException {
    // Add submitting functionality here!
  }

  @Override
  protected void onResetButtonClicked(ActionEvent event) throws IOException {
    // Reset fields here!
  }

  public void validateButton(KeyEvent keyEvent) {}
}
