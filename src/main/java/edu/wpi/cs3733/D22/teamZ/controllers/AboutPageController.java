package edu.wpi.cs3733.D22.teamZ.controllers;

public class AboutPageController implements IMenuAccess {
  private MenuController menu;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "About Page";
  }
}
