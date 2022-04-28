package edu.wpi.cs3733.D22.teamZ.controllers;

import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class AboutPageController implements IMenuAccess {
  private MenuController menu;
  @FXML private ImageView groupPhoto;
  @FXML private MFXToggleButton toggleMaya;
  @FXML private Label mayaLabel;
  @FXML private ImageView mayaNFTImage;
  @FXML private MFXToggleButton toggleNelson;
  @FXML private Label nelsonLabel;
  @FXML private ImageView nelsonNFTImage;
  @FXML private MFXToggleButton toggleJake;
  @FXML private Label jakeLabel;
  @FXML private ImageView jakeNFTImage;
  @FXML private MFXToggleButton toggleNeha;
  @FXML private Label nehaLabel;
  @FXML private ImageView nehaNFTImage;
  @FXML private MFXToggleButton toggleAndrew;
  @FXML private Label andrewLabel;
  @FXML private ImageView andrewNFTImage;
  @FXML private MFXToggleButton togglePatrick;
  @FXML private Label patrickLabel;
  @FXML private ImageView patrickNFTImage;
  @FXML private MFXToggleButton toggleAlex;
  @FXML private Label alexLabel;
  @FXML private ImageView alexNFTImage;
  @FXML private MFXToggleButton toggleCasey;
  @FXML private Label caseyLabel;
  @FXML private ImageView caseyNFTImage;
  @FXML private MFXToggleButton toggleOlivia;
  @FXML private Label oliviaLabel;
  @FXML private ImageView oliviaNFTImage;
  @FXML private MFXToggleButton toggleDaniel;
  @FXML private Label danielLabel;
  @FXML private ImageView danielNFTImage;
  @FXML private MFXToggleButton toggleClaire;
  @FXML private Label claireLabel;
  @FXML private ImageView claireNFTImage;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "About Page";
  }

  public void initialize() {
    toggleMaya.setMainColor(Color.rgb(0, 103, 177));
    toggleAlex.setMainColor(Color.rgb(0, 103, 177));
    toggleAndrew.setMainColor(Color.rgb(0, 103, 177));
    toggleCasey.setMainColor(Color.rgb(0, 103, 177));
    toggleClaire.setMainColor(Color.rgb(0, 103, 177));
    toggleDaniel.setMainColor(Color.rgb(0, 103, 177));
    toggleJake.setMainColor(Color.rgb(0, 103, 177));
    toggleNeha.setMainColor(Color.rgb(0, 103, 177));
    toggleOlivia.setMainColor(Color.rgb(0, 103, 177));
    togglePatrick.setMainColor(Color.rgb(0, 103, 177));

    mayaNFTImage.setVisible(false);
    mayaLabel.setVisible(false);
    nelsonNFTImage.setVisible(false);
    nelsonLabel.setVisible(false);
    jakeNFTImage.setVisible(false);
    jakeLabel.setVisible(false);
    nehaNFTImage.setVisible(false);
    nehaLabel.setVisible(false);
    andrewNFTImage.setVisible(false);
    andrewLabel.setVisible(false);
    patrickNFTImage.setVisible(false);
    patrickLabel.setVisible(false);
    alexNFTImage.setVisible(false);
    alexLabel.setVisible(false);
    caseyNFTImage.setVisible(false);
    caseyLabel.setVisible(false);
    oliviaNFTImage.setVisible(false);
    oliviaLabel.setVisible(false);
    danielNFTImage.setVisible(false);
    danielLabel.setVisible(false);
    claireNFTImage.setVisible(false);
    claireLabel.setVisible(false);
  }

  public void showMayaNFT() {
    boolean set = toggleMaya.isSelected();
    mayaNFTImage.setVisible(set);
    mayaLabel.setVisible(set);
  }

  public void showNelsonNFT() {
    boolean set = toggleNelson.isSelected();
    nelsonNFTImage.setVisible(set);
    nelsonLabel.setVisible(set);
  }

  public void showJakeNFT() {
    boolean set = toggleJake.isSelected();
    jakeNFTImage.setVisible(set);
    jakeLabel.setVisible(set);
  }

  public void showNehaNFT() {
    boolean set = toggleNeha.isSelected();
    nehaNFTImage.setVisible(set);
    nehaLabel.setVisible(set);
  }

  public void showAndrewNFT() {
    boolean set = toggleAndrew.isSelected();
    andrewNFTImage.setVisible(set);
    andrewLabel.setVisible(set);
  }

  public void showPatrickNFT() {
    boolean set = togglePatrick.isSelected();
    patrickNFTImage.setVisible(set);
    patrickLabel.setVisible(set);
  }

  public void showAlexNFT() {
    boolean set = toggleAlex.isSelected();
    alexNFTImage.setVisible(set);
    alexLabel.setVisible(set);
  }

  public void showCaseyNFT() {
    boolean set = toggleCasey.isSelected();
    caseyNFTImage.setVisible(set);
    caseyLabel.setVisible(set);
  }

  public void showOliviaNFT() {
    boolean set = toggleOlivia.isSelected();
    oliviaNFTImage.setVisible(set);
    oliviaLabel.setVisible(set);
  }

  public void showDanielNFT() {
    boolean set = toggleDaniel.isSelected();
    danielNFTImage.setVisible(set);
    danielLabel.setVisible(set);
  }

  public void showClaireNFT() {
    boolean set = toggleClaire.isSelected();
    claireNFTImage.setVisible(set);
    claireLabel.setVisible(set);
  }
}
