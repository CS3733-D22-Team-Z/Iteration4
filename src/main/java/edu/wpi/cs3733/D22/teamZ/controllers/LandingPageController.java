package edu.wpi.cs3733.D22.teamZ.controllers;

import io.github.palexdev.materialfx.controls.MFXToggleButton;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

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

  @FXML VBox iconContainer;
  @FXML MFXToggleButton toggle;
  @FXML private Label Daniel;
  @FXML private Label Patrick;
  @FXML private Label Maya;
  @FXML private Label Neha;
  @FXML private Label Alex;
  @FXML private Label Jake;
  @FXML private Label Nelson;

  private String[] icons = {
    "M20,6h-4V4c0-1.1-0.9-2-2-2h-4C8.9,2,8,2.9,8,4v2H4C2.9,6,2,6.9,2,8v12c0,1.1,0.9,2,2,2h16c1.1,0,2-0.9,2-2V8 C22,6.9,21.1,6,20,6z M10,4h4v2h-4V4z M20,20H4V8h16V20z",
    "M13,11.33L18,18H6l5-6.67V6h2 M15.96,4H8.04C7.62,4,7.39,4.48,7.65,4.81L9,6.5v4.17L3.2,18.4C2.71,19.06,3.18,20,4,20h16 c0.82,0,1.29-0.94,0.8-1.6L15,10.67V6.5l1.35-1.69C16.61,4.48,16.38,4,15.96,4L15.96,4z",
    "M11,5.5H8V4h0.5c0.55,0,1-0.45,1-1c0-0.55-0.45-1-1-1h-3c-0.55,0-1,0.45-1,1c0,0.55,0.45,1,1,1H6v1.5H3c-0.55,0-1,0.45-1,1 c0,0.55,0.45,1,1,1V15c0,1.1,0.9,2,2,2h1v4l2,1.5V17h1c1.1,0,2-0.9,2-2V7.5c0.55,0,1-0.45,1-1C12,5.95,11.55,5.5,11,5.5z M9,9H7.25 C6.84,9,6.5,9.34,6.5,9.75c0,0.41,0.34,0.75,0.75,0.75H9V12H7.25c-0.41,0-0.75,0.34-0.75,0.75c0,0.41,0.34,0.75,0.75,0.75H9L9,15H5 V7.5h4V9z M19.5,10.5V10c0.55,0,1-0.45,1-1c0-0.55-0.45-1-1-1h-5c-0.55,0-1,0.45-1,1c0,0.55,0.45,1,1,1v0.5c0,0.5-1.5,1.16-1.5,3V20 c0,1.1,0.9,2,2,2h4c1.1,0,2-0.9,2-2v-6.5C21,11.66,19.5,11,19.5,10.5z M16.5,10.5V10h1v0.5c0,1.6,1.5,2,1.5,3V14h-4 c0-0.21,0-0.39,0-0.5C15,12.5,16.5,12.1,16.5,10.5z M19,15.5V17h-4c0-0.51,0-1.02,0-1.5H19z M15,20c0,0,0-0.63,0-1.5h4V20H15z",
    "M12.87 15.07l-2.54-2.51.03-.03c1.74-1.94 2.98-4.17 3.71-6.53H17V4h-7V2H8v2H1v1.99h11.17C11.5 7.92 10.44 9.75 9 11.35 8.07 10.32 7.3 9.19 6.69 8h-2c.73 1.63 1.73 3.17 2.98 4.56l-5.09 5.02L4 19l5-5 3.11 3.11.76-2.04zM18.5 10h-2L12 22h2l1.12-3h4.75L21 22h2l-4.5-12zm-2.62 7l1.62-4.33L19.12 17h-3.24z",
    "M16 6v8h3v8h2V2c-2.76 0-5 2.24-5 4zm-5 3H9V2H7v7H5V2H3v7c0 2.21 1.79 4 4 4v9h2v-9c2.21 0 4-1.79 4-4V2h-2v7z",
    "M12 19c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm2.36-7.36c1.3 1.3 1.3 3.42 0 4.72-1.3 1.3-3.42 1.3-4.72 0l4.72-4.72z",
    "M20 18c1.1 0 1.99-.9 1.99-2L22 6c0-1.1-.9-2-2-2H4c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2H0v2h24v-2h-4zM4 6h16v10H4V6z"
  };

  private MenuController menu;
  private String grey = "#C4C4C4";
  private String svgCSSLine = "-fx-background-color: %s";

  public void initialize() {
    toggle.setSelected(true);
    for (int i = 0; i < iconContainer.getChildren().size(); i++) {
      Region graphic = (Region) iconContainer.getChildren().get(i);
      SVGPath icon = new SVGPath();
      icon.setContent(icons[i]);
      graphic.setShape(icon);
      graphic.setStyle(String.format(svgCSSLine, grey));
    }
  }

  public void onToggleClicked() {
    boolean set = toggle.isSelected();
    Daniel.setVisible(set);
    Patrick.setVisible(set);
    Maya.setVisible(set);
    Neha.setVisible(set);
    Alex.setVisible(set);
    Jake.setVisible(set);
    Nelson.setVisible(set);
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Service Request Landing";
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
