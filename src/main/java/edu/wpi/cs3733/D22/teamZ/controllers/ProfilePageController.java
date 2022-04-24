package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class ProfilePageController implements Initializable, IMenuAccess {

  protected MenuController menu;
  protected String menuName;
  Employee emp;
  FacadeDAO facadeDAO;

  @FXML private Label userName;
  @FXML private MFXTextField ID;
  @FXML private MFXTextField accessType;
  @FXML private Pane password;
  @FXML private MFXButton changePassword;
  @FXML private MFXButton submitButton;
  @FXML private MFXTextField oldPassword;
  @FXML private MFXTextField newPassword;

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
    emp = MenuController.getLoggedInUser();
    userName.setText(userName.getText() + " " + emp.getName());
    ID.setText(emp.getEmployeeID());
    accessType.setText(emp.getAccesstype().accessTypeToString());
    ID.setDisable(true);
    accessType.setDisable(true);
    password.setVisible(false);
    facadeDAO = FacadeDAO.getInstance();
  }

  public void passwordButton(ActionEvent actionEvent) {
    password.setVisible(true);
    changePassword.setDisable(true);
  }

  public void submit(ActionEvent actionEvent) {
    if (oldPassword.getText().equals(emp.getPassword()) && newPassword != null) {
      changePassword.setDisable(false);
      emp.setPassword(newPassword.getText());
      facadeDAO.updateEmployee(emp);
      password.setVisible(false);
    }
  }

  public void closeChangePassword(MouseEvent mouseEvent) {
    password.setVisible(false);
    changePassword.setDisable(false);
  }
}
