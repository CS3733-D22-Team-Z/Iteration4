package edu.wpi.cs3733.D22.teamZ.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class AboutPageController implements IMenuAccess {
  private MenuController menu;
  @FXML private ImageView groupPhoto;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "About Page";
  }

  public void initialize() {}
}
