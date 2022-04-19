package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class CleaningRequestController implements Initializable, IMenuAccess {
  private final String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";
  private FacadeDAO facadeDAO;
  private MenuController menu;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Lab Request List";
  }

  @FXML
  public void initialize(URL location, ResourceBundle resources) {}

  @FXML
  private void onBackButtonClicked(ActionEvent event) throws IOException {
    menu.load(toLandingPageURL);
  }
}
