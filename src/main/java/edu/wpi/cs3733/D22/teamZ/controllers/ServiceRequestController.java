package edu.wpi.cs3733.D22.teamZ.controllers;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * A class that all service request controllers will inherit from.
 * Each Service Request will have...
 * *A function to set the menu for the class
 */
public abstract class ServiceRequestController implements Initializable, IMenuAccess {
    MenuController menu;

    @Override
    public void setMenuController(MenuController menu) {
        this.menu = menu;
    }

    @Override
    public abstract void initialize(URL location, ResourceBundle resources);
}
