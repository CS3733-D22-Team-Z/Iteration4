package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ProfilePageController implements Initializable, IMenuAccess {

  protected MenuController menu;
  protected String menuName;
  @FXML private Label userName;
  @FXML private MFXTextField ID;
  @FXML private MFXTextField accessType;
  @FXML private AnchorPane password;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Profile Page";
  }

  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    Employee emp = MenuController.getLoggedInUser();
    userName.setText(userName.getText() + " " + emp.getName());
    ID.setText(emp.getEmployeeID());
    accessType.setText(emp.getAccesstype().accessTypeToString());
    ID.setDisable(true);
    accessType.setDisable(true);
    password.setVisible(false);
  }
}
