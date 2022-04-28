package edu.wpi.cs3733.D22.teamZ.controllers;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class Dashboard3DController implements IMenuAccess, Initializable {

    private MenuController menu;

    @Override
    public void setMenuController(MenuController menu) {
        this.menu = menu;
    }

    @Override
    public String getMenuName() {
        return "3D Dashboard";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
