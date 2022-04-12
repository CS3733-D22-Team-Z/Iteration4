package edu.wpi.cs3733.D22.teamZ.controllers;

import java.io.IOException;
import javafx.fxml.FXML;

public class LandingPageController implements IMenuAccess {

  private final String toMedicalEquipmentDeliveryURL =
      "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentDelivery.fxml";
  private final String toLabRequestURL = "edu/wpi/cs3733/D22/teamZ/views/LabServiceRequest.fxml";
  private final String toMedicineRequestURL = "edu/wpi/cs3733/D22/teamZ/views/MedicineRequest.fxml";
  private final String toMealRequestsURL = "edu/wpi/cs3733/D22/teamZ/views/MealService.fxml";
  private final String toLanguageInterpreterURL =
      "edu/wpi/cs3733/D22/teamZ/views/LanguageInterpreter.fxml";
  private final String toLaundryServiceURL = "edu/wpi/cs3733/D22/teamZ/views/LaundryService.fxml";
  private final String toComputerServiceRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/ComputerServiceRequest.fxml";

  private MenuController menu;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @FXML
  private void navMedicalEquipment() throws IOException {
    System.out.println("navigating to medical equipment delivery from landing page");
    menu.load(toMedicalEquipmentDeliveryURL);
  }

  @FXML
  private void navLab() throws IOException {
    System.out.println("navigating to lab requests from landing page");
    menu.load(toLabRequestURL);
  }

  @FXML
  private void navMedicine() throws IOException {
    System.out.println("navigating to medicine from landing page");
    menu.load(toMedicineRequestURL);
  }

  @FXML
  private void navMeal() throws IOException {
    System.out.println("navigating to meal from landing page");
    menu.load(toMealRequestsURL);
  }

  @FXML
  private void navLanguage() throws IOException {
    System.out.println("navigating to language from landing page");
    menu.load(toLanguageInterpreterURL);
  }

  @FXML
  private void navLaundry() throws IOException {
    System.out.println("navigating to laundry from landing page");
    menu.load(toLaundryServiceURL);
  }

  @FXML
  private void navComputer() throws IOException {
    System.out.println("navigating to computer from landing page");
    menu.load(toComputerServiceRequestURL);
  }
}
