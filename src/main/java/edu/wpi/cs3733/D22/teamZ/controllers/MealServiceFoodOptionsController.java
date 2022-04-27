package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.entity.*;
import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.util.*;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

public class MealServiceFoodOptionsController extends ServiceRequestController {

  @FXML private MFXTextField foodOptionName;



  private final String toMealServiceRequestURL = "edu/wpi/cs3733/D22/teamZ/views/MealService.fxml";

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  @Override
  protected void highlightRequirements(boolean visible) {}

  @Override
  public void onBackButtonClicked(ActionEvent event) throws IOException {
    try {
      menu.load(toMealServiceRequestURL);
    } catch (IOException e) {
      System.out.println("Error: Failed to load Meal Service Request URL");
      e.printStackTrace();
      throw new IOException();
    }
  }

  public void onSubmitButtonClicked(ActionEvent event) {}

  public void onResetButtonClicked(ActionEvent event) {
    System.out.println("Reset Button Clicked");
    submitButton.setDisable(true);
    System.out.println("Meal Request Submit button disabled");
    mealRequestIndicator.setText("Form Reset");
    mealRequestIndicator.setStyle("-fx-text-fill: #7B7B7B");
    // Clear dropdown values
    patientIDDropDown.setValue(null);
    patientNameDropDown.setValue(null);
    roomNumberDropDown.setValue(null);
    drinkOptionDropDown.setValue(null);
    entreeOptionDropDown.setValue(null);
    snackOptionDropDown.setValue(null);
    // Reset radio buttons
    dairyChoice.setSelected(false);
    eggChoice.setSelected(false);
    peanutChoice.setSelected(false);
    treenutChoice.setSelected(false);
    soyChoice.setSelected(false);
    wheatChoice.setSelected(false);
    fishChoice.setSelected(false);
    shellfishChoice.setSelected(false);
    // Remove allergens from meal request
    patientAllergensList.clear();
    System.out.println("Patient Allergen List Emptied");
  }

  public void validateButton(KeyEvent keyEvent) {}
}
