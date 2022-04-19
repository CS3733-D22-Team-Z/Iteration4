package edu.wpi.cs3733.D22.teamZ.controllers;

import io.github.palexdev.materialfx.controls.MFXToggleButton;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;

public class LandingPageController implements IMenuAccess {

  private final String toMedicalEquipmentDeliveryURL =
      "edu/wpi/cs3733/D22/teamZ/views/MedicalEquipmentDelivery.fxml";
  private final String toLabRequestURL = "edu/wpi/cs3733/D22/teamZ/views/LabServiceRequest.fxml";
  private final String toExternalPatientTransportationRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/ExternalPatientTransportationRequest.fxml";
  private final String toMealRequestsURL = "edu/wpi/cs3733/D22/teamZ/views/MealService.fxml";
  private final String toLanguageInterpreterURL =
      "edu/wpi/cs3733/D22/teamZ/views/LanguageInterpreter.fxml";
  private final String toLaundryServiceURL = "edu/wpi/cs3733/D22/teamZ/views/LaundryService.fxml";
  private final String toComputerServiceRequestURL =
      "edu/wpi/cs3733/D22/teamZ/views/ComputerServiceRequest.fxml";
  private final String toGiftServicesURL = "edu/wpi/cs3733/D22/teamZ/views/GiftServices.fxml";
  private final String toEquipmentPurchaseServiceURL =
      "edu/wpi/cs3733/D22/teamZ/views/EquipmentPurchaseRequest.fxml";

  // @FXML VBox iconContainer;
  // @FXML MFXToggleButton toggle;
  // @FXML private Label Daniel;
  // @FXML private Label Patrick;
  // @FXML private Label Maya;
  // @FXML private Label Neha;
  // @FXML private Label Alex;
  // @FXML private Label Jake;
  // @FXML private Label Nelson;
  @FXML private Region medRegion;
  @FXML private Region labRegion;
  @FXML private Region externalRegion;
  @FXML private Region languageRegion;
  @FXML private Region computerRegion;
  @FXML private Region laundryRegion;
  @FXML private Region mealRegion;
  @FXML private Region giftRegion;
  @FXML private Region cleaningRegion;
  @FXML private Region purchaseRegion;
  @FXML private Label danLabel;
  @FXML private Label patrickLabel;
  @FXML private Label claireLabel;
  @FXML private Label mayaLabel;
  @FXML private Label jacobLabel;
  @FXML private Label nehaLabel;
  @FXML private Label nelsonLabel;
  @FXML private Label alexLabel;
  @FXML private Label oliviaLabel;
  @FXML private Label andrewLabel;
  @FXML private MFXToggleButton toggleNames;
  @FXML private ScrollPane scrollPane;

  private String[] icons = {
    "M20,6h-4V4c0-1.1-0.9-2-2-2h-4C8.9,2,8,2.9,8,4v2H4C2.9,6,2,6.9,2,8v12c0,1.1,0.9,2,2,2h16c1.1,0,2-0.9,2-2V8 C22,6.9,21.1,6,20,6z M10,4h4v2h-4V4z M20,20H4V8h16V20z",
    "M13,11.33L18,18H6l5-6.67V6h2 M15.96,4H8.04C7.62,4,7.39,4.48,7.65,4.81L9,6.5v4.17L3.2,18.4C2.71,19.06,3.18,20,4,20h16 c0.82,0,1.29-0.94,0.8-1.6L15,10.67V6.5l1.35-1.69C16.61,4.48,16.38,4,15.96,4L15.96,4z",
    "M21,11.18V9.72c0-0.47-0.16-0.92-0.46-1.28L16.6,3.72C16.22,3.26,15.66,3,15.06,3H3C1.9,3,1,3.9,1,5v8c0,1.1,0.9,2,2,2 h0.18C3.6,16.16,4.7,17,6,17s2.4-0.84,2.82-2h8.37c0.41,1.16,1.51,2,2.82,2c1.66,0,3-1.34,3-3C23,12.7,22.16,11.6,21,11.18z M6,15c-0.55,0-1-0.45-1-1s0.45-1,1-1s1,0.45,1,1S6.55,15,6,15z M7,11.17C6.69,11.06,6.35,11,6,11c-1.3,0-2.42,0.84-2.83,2H3v-3 h4V11.17z M7,8H3V5h4V8z M14,13H9v-3h3V8H9V5h5V13z M16,6.12L18.4,9H16V6.12z M17.17,13H16v-2h3v0.17 C18.15,11.47,17.47,12.15,17.17,13z M20,15c-0.55,0-1-0.45-1-1s0.45-1,1-1s1,0.45,1,1S20.55,15,20,15z",
    // "M11,5.5H8V4h0.5c0.55,0,1-0.45,1-1c0-0.55-0.45-1-1-1h-3c-0.55,0-1,0.45-1,1c0,0.55,0.45,1,1,1H6v1.5H3c-0.55,0-1,0.45-1,1 c0,0.55,0.45,1,1,1V15c0,1.1,0.9,2,2,2h1v4l2,1.5V17h1c1.1,0,2-0.9,2-2V7.5c0.55,0,1-0.45,1-1C12,5.95,11.55,5.5,11,5.5z M9,9H7.25 C6.84,9,6.5,9.34,6.5,9.75c0,0.41,0.34,0.75,0.75,0.75H9V12H7.25c-0.41,0-0.75,0.34-0.75,0.75c0,0.41,0.34,0.75,0.75,0.75H9L9,15H5 V7.5h4V9z M19.5,10.5V10c0.55,0,1-0.45,1-1c0-0.55-0.45-1-1-1h-5c-0.55,0-1,0.45-1,1c0,0.55,0.45,1,1,1v0.5c0,0.5-1.5,1.16-1.5,3V20 c0,1.1,0.9,2,2,2h4c1.1,0,2-0.9,2-2v-6.5C21,11.66,19.5,11,19.5,10.5z M16.5,10.5V10h1v0.5c0,1.6,1.5,2,1.5,3V14h-4 c0-0.21,0-0.39,0-0.5C15,12.5,16.5,12.1,16.5,10.5z M19,15.5V17h-4c0-0.51,0-1.02,0-1.5H19z M15,20c0,0,0-0.63,0-1.5h4V20H15z",
    "M12.87 15.07l-2.54-2.51.03-.03c1.74-1.94 2.98-4.17 3.71-6.53H17V4h-7V2H8v2H1v1.99h11.17C11.5 7.92 10.44 9.75 9 11.35 8.07 10.32 7.3 9.19 6.69 8h-2c.73 1.63 1.73 3.17 2.98 4.56l-5.09 5.02L4 19l5-5 3.11 3.11.76-2.04zM18.5 10h-2L12 22h2l1.12-3h4.75L21 22h2l-4.5-12zm-2.62 7l1.62-4.33L19.12 17h-3.24z",
    "M16 6v8h3v8h2V2c-2.76 0-5 2.24-5 4zm-5 3H9V2H7v7H5V2H3v7c0 2.21 1.79 4 4 4v9h2v-9c2.21 0 4-1.79 4-4V2h-2v7z",
    "M12 19c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm2.36-7.36c1.3 1.3 1.3 3.42 0 4.72-1.3 1.3-3.42 1.3-4.72 0l4.72-4.72z",
    "M20 18c1.1 0 1.99-.9 1.99-2L22 6c0-1.1-.9-2-2-2H4c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2H0v2h24v-2h-4zM4 6h16v10H4V6z",
    "M880 310H732.4C746 288.6 754 263.2 754 236C754 159.9 692.1 98 616 98C574.6 98 537.3 116.4 512 145.4C486.7 116.4 449.4 98 408 98C331.9 98 270 159.9 270 236C270 263.2 277.9 288.6 291.6 310H144C126.3 310 112 324.3 112 342V542C112 546.4 115.6 550 120 550H160V894C160 911.7 174.3 926 192 926H832C849.7 926 864 911.7 864 894V550H904C908.4 550 912 546.4 912 542V342C912 324.3 897.7 310 880 310ZM546 236C546 197.4 577.4 166 616 166C654.6 166 686 197.4 686 236C686 274.6 654.6 306 616 306H546V236ZM408 166C446.6 166 478 197.4 478 236V306H408C369.4 306 338 274.6 338 236C338 197.4 369.4 166 408 166ZM180 482V378H478V482H180ZM228 550H478V858H228V550ZM796 858H546V550H796V858ZM844 482H546V378H844V482Z",
    "M770.25 549.375H592.5V494.438H770.25V549.375ZM888.75 769.125H711V714.188H888.75V769.125ZM829.5 659.25H651.75V604.312H829.5V659.25Z",
    "M11 9h2V6h3V4h-3V1h-2v3H8v2h3v3zm-4 9c-1.1 0-1.99.9-1.99 2S5.9 22 7 22s2-.9 2-2-.9-2-2-2zm10 0c-1.1 0-1.99.9-1.99 2s.89 2 1.99 2 2-.9 2-2-.9-2-2-2zm-8.9-5h7.45c.75 0 1.41-.41 1.75-1.03l3.86-7.01L19.42 4l-3.87 7H8.53L4.27 2H1v2h2l3.6 7.59-1.35 2.44C4.52 15.37 5.48 17 7 17h12v-2H7l1.1-2z"
  };

  private MenuController menu;
  private final String grey = "#0075FF";
  private final String svgCSSLine = "-fx-background-color: %s";

  public void initialize() {
    toggleNames.setSelected(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    // toggle.setSelected(true);
    SVGPath icon = new SVGPath();
    icon.setContent(icons[0]);
    medRegion.setShape(icon);
    medRegion.setStyle(String.format(svgCSSLine, grey));

    SVGPath labIcon = new SVGPath();
    labIcon.setContent(icons[1]);
    labRegion.setShape(labIcon);
    labRegion.setStyle(String.format(svgCSSLine, grey));

    SVGPath externalIcon = new SVGPath();
    externalIcon.setContent(icons[2]);
    externalRegion.setShape(externalIcon);
    externalRegion.setStyle(String.format(svgCSSLine, grey));

    SVGPath languageIcon = new SVGPath();
    languageIcon.setContent(icons[3]);
    languageRegion.setShape(languageIcon);
    languageRegion.setStyle(String.format(svgCSSLine, grey));

    SVGPath computerIcon = new SVGPath();
    computerIcon.setContent(icons[6]);
    computerRegion.setShape(computerIcon);
    computerRegion.setStyle(String.format(svgCSSLine, grey));

    SVGPath laundryIcon = new SVGPath();
    laundryIcon.setContent(icons[5]);
    laundryRegion.setShape(laundryIcon);
    laundryRegion.setStyle(String.format(svgCSSLine, grey));

    SVGPath mealIcon = new SVGPath();
    mealIcon.setContent(icons[4]);
    mealRegion.setShape(mealIcon);
    mealRegion.setStyle(String.format(svgCSSLine, grey));

    SVGPath giftIcon = new SVGPath();
    giftIcon.setContent(icons[7]);
    giftRegion.setShape(giftIcon);
    giftRegion.setStyle(String.format(svgCSSLine, grey));

    SVGPath cleaningIcon = new SVGPath();
    cleaningIcon.setContent(icons[8]);
    cleaningRegion.setShape(giftIcon);
    cleaningRegion.setStyle(String.format(svgCSSLine, grey));

    SVGPath purchaseIcon = new SVGPath();
    purchaseIcon.setContent(icons[9]);
    purchaseRegion.setShape(purchaseIcon);
    purchaseRegion.setStyle(String.format(svgCSSLine, grey));
  }

  /**
   * public void onToggleClicked() { boolean set = toggle.isSelected(); Daniel.setVisible(set);
   * Patrick.setVisible(set); Maya.setVisible(set); Neha.setVisible(set); Alex.setVisible(set);
   * Jake.setVisible(set); Nelson.setVisible(set); } *
   */
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

  @FXML
  public void toExternalPatientTransportation(ActionEvent actionEvent) throws IOException {
    System.out.println("navigating to transportation from landing page");
    menu.load(toExternalPatientTransportationRequestURL);
  }

  @FXML
  private void navGifts() throws IOException {
    System.out.println("navigating to gift services from landing page");
    menu.load(toGiftServicesURL);
  }

  @FXML
  private void navPurchase(ActionEvent event) throws IOException {
    System.out.println("navigating to equipment purchase services from landing page");
    menu.load(toEquipmentPurchaseServiceURL);
  }

  public void showNameLabels() {
    boolean set = toggleNames.isSelected();
    danLabel.setVisible(set);
    claireLabel.setVisible(set);
    mayaLabel.setVisible(set);
    jacobLabel.setVisible(set);
    nehaLabel.setVisible(set);
    patrickLabel.setVisible(set);
    nelsonLabel.setVisible(set);
    alexLabel.setVisible(set);
    oliviaLabel.setVisible(set);
    andrewLabel.setVisible(set);
  }
}
