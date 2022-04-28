package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.apiFacades.ExternalTransportFacadeAPI;
import edu.wpi.cs3733.D22.teamZ.apiFacades.FacilityMaintenanceFacadeAPI;
import edu.wpi.cs3733.D22.teamZ.apiFacades.InternalTransportFacadeAPI;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.SVGPath;

public class APILandingPageController implements IMenuAccess {
  @FXML private Region externalRegionFM;
  @FXML private Label teamCLabel;
  @FXML private Region externalRegionIPT;
  @FXML private Label teamBLabel;
  @FXML private ScrollPane scrollPane;
  @FXML private Region externalRegion;
  @FXML private Label teamZLabel;

  private final String toLandingPageURL = "edu/wpi/cs3733/D22/teamZ/views/LandingPage.fxml";

  private final String apiCSSPath = "edu/wpi/cs3733/D22/teamZ/styles/ServiceRequestDefault.css";

  private final String[] icons = {
    "M8 16C8.26522 16 8.51957 15.8946 8.70711 15.7071C8.89464 15.5196 9 15.2652 9 15C9 14.7348 8.89464 14.4804 8.70711 14.2929C8.51957 14.1054 8.26522 14 8 14C7.73478 14 7.48043 14.1054 7.29289 14.2929C7.10536 14.4804 7 14.7348 7 15C7 15.2652 7.10536 15.5196 7.29289 15.7071C7.48043 15.8946 7.73478 16 8 16ZM17 15C17 15.2652 16.8946 15.5196 16.7071 15.7071C16.5196 15.8946 16.2652 16 16 16C15.7348 16 15.4804 15.8946 15.2929 15.7071C15.1054 15.5196 15 15.2652 15 15C15 14.7348 15.1054 14.4804 15.2929 14.2929C15.4804 14.1054 15.7348 14 16 14C16.2652 14 16.5196 14.1054 16.7071 14.2929C16.8946 14.4804 17 14.7348 17 15V15ZM10.75 5C10.5511 5 10.3603 5.07902 10.2197 5.21967C10.079 5.36032 10 5.55109 10 5.75C10 5.94891 10.079 6.13968 10.2197 6.28033C10.3603 6.42098 10.5511 6.5 10.75 6.5H13.25C13.4489 6.5 13.6397 6.42098 13.7803 6.28033C13.921 6.13968 14 5.94891 14 5.75C14 5.55109 13.921 5.36032 13.7803 5.21967C13.6397 5.07902 13.4489 5 13.25 5H10.75ZM7.75 2C6.75544 2 5.80161 2.39509 5.09835 3.09835C4.39509 3.80161 4 4.75544 4 5.75V9.5H2.75C2.55109 9.5 2.36032 9.57902 2.21967 9.71967C2.07902 9.86032 2 10.0511 2 10.25C2 10.4489 2.07902 10.6397 2.21967 10.7803C2.36032 10.921 2.55109 11 2.75 11H4V19.75C4 20.716 4.783 21.5 5.75 21.5H7.25C7.71413 21.5 8.15925 21.3156 8.48744 20.9874C8.81563 20.6592 9 20.2141 9 19.75V18.5H15V19.75C15 20.716 15.784 21.5 16.75 21.5H18.25C18.7141 21.5 19.1592 21.3156 19.4874 20.9874C19.8156 20.6592 20 20.2141 20 19.75V11H21.227C21.4259 11 21.6167 10.921 21.7573 10.7803C21.898 10.6397 21.977 10.4489 21.977 10.25C21.977 10.0511 21.898 9.86032 21.7573 9.71967C21.6167 9.57902 21.4259 9.5 21.227 9.5H20V5.75C20 4.75544 19.6049 3.80161 18.9017 3.09835C18.1984 2.39509 17.2446 2 16.25 2H7.75ZM18.5 18.5V19.75C18.5 19.8163 18.4737 19.8799 18.4268 19.9268C18.3799 19.9737 18.3163 20 18.25 20H16.75C16.6837 20 16.6201 19.9737 16.5732 19.9268C16.5263 19.8799 16.5 19.8163 16.5 19.75V18.5H18.5ZM18.5 17H5.5V13H18.5V17ZM5.5 19.75V18.5H7.5V19.75C7.5 19.8163 7.47366 19.8799 7.42678 19.9268C7.37989 19.9737 7.3163 20 7.25 20H5.75C5.6837 20 5.62011 19.9737 5.57322 19.9268C5.52634 19.8799 5.5 19.8163 5.5 19.75ZM5.5 5.75C5.5 5.15326 5.73705 4.58097 6.15901 4.15901C6.58097 3.73705 7.15326 3.5 7.75 3.5H16.25C16.8467 3.5 17.419 3.73705 17.841 4.15901C18.2629 4.58097 18.5 5.15326 18.5 5.75V11.5H5.5V5.75Z",
    "M476.721 0C411.119 0.471 359.144 50.855 356.346 119.094C356.823 185.496 407.741 236.962 476.721 239.5C538.147 238.668 592.71 188.025 595.815 119.094C595.349 52.962 544.73 3.015 476.721 0ZM510.002 266.375C482.682 266.375 459.63 275.55 440.846 293.906C425.585 309.223 408.499 343.007 413.971 370.125H412.69L483.127 709.5C490.392 748.373 522.385 773.438 565.096 776.094C663.212 775.824 769.603 773.597 869.877 774.813L1038.94 1069.38C1057.96 1109.52 1106.67 1117.98 1142.03 1088.59C1159 1071.76 1165.86 1043.12 1154.19 1018.16L958.252 677.469C944.135 648.261 916.282 631.807 881.408 630.094H663.689L638.095 504.594H808.408C845.226 507.318 871.751 479.219 864.127 440.563C858.525 421.675 838.953 404.285 816.096 403.408H616.314C606.246 351.945 607.848 320.835 568.284 285.595C551.635 272.787 532.2 266.375 510.002 266.375ZM326.283 411.625C320.87 411.703 315.482 412.384 310.22 413.656C234.776 443.011 166.903 493.457 114.282 563.5C67.913 628.887 41.989 707.595 40.002 794.031C40.774 896.473 84.7629 1001.6 163.596 1080.91C240.004 1153.68 346.061 1197.61 460.065 1200C543.217 1199.54 626.339 1174.98 702.128 1126.38C770.198 1080.14 823.542 1013.79 855.784 931.063C861.761 916.547 866.889 900.304 871.159 882.375C877.409 854.189 861.589 831.291 831.471 822.187C804.077 816.118 779.578 831.233 770.002 860.625C767.458 873.367 763.593 885.809 758.471 897.75C736.722 955.257 696.24 1005.03 642.565 1043.75C590.38 1079.24 527.881 1098.56 460.065 1100.09C379.416 1099.52 301.733 1068.78 236.596 1010.47C180.627 956.205 145.614 878.557 143.752 794.031C144.11 733.776 163.514 674.316 200.096 620.5C235.971 570.693 286.192 532.228 347.377 507.156C373.16 498.7 387.05 472.006 378.096 443.125C369.447 422.364 349.072 411.317 326.283 411.625V411.625Z",
    "M7.00008 5C7.00006 4.35 7.15845 3.70979 7.46153 3.13477C7.76461 2.55975 8.20326 2.06726 8.7395 1.69991C9.27575 1.33257 9.89344 1.10144 10.5391 1.02653C11.1848 0.951621 11.839 1.03519 12.4451 1.27C12.5225 1.3001 12.5912 1.34903 12.645 1.41235C12.6988 1.47568 12.736 1.55141 12.7531 1.6327C12.7703 1.71399 12.767 1.79828 12.7434 1.87795C12.7198 1.95762 12.6767 2.03016 12.6181 2.089L10.7081 4L12.0001 5.293L13.9111 3.383C13.9699 3.3241 14.0424 3.28079 14.1221 3.25701C14.2019 3.23322 14.2863 3.22971 14.3677 3.24679C14.4491 3.26387 14.525 3.30101 14.5885 3.35483C14.6519 3.40865 14.7009 3.47745 14.7311 3.555C15.0002 4.24915 15.0703 5.00464 14.9335 5.73645C14.7966 6.46827 14.4582 7.14736 13.9564 7.69731C13.4545 8.24726 12.8092 8.64625 12.0929 8.84936C11.3766 9.05247 10.6179 9.05166 9.90209 8.847L4.89608 13.92C4.71394 14.1067 4.49681 14.2556 4.2571 14.3584C4.01738 14.4612 3.75977 14.5157 3.49898 14.5189C2.97228 14.5254 2.46459 14.3224 2.08758 13.9545C1.71058 13.5866 1.49515 13.0841 1.48868 12.5574C1.48221 12.0307 1.68523 11.523 2.05308 11.146L7.10408 5.912C7.03462 5.61299 6.99973 5.30698 7.00008 5V5ZM11.0001 2C10.5287 1.99983 10.0639 2.11074 9.64337 2.32374C9.22285 2.53675 8.85844 2.84585 8.57968 3.22599C8.30093 3.60613 8.11568 4.04662 8.03896 4.51172C7.96224 4.97683 7.9962 5.45347 8.13808 5.903C8.16523 5.98885 8.16869 6.08044 8.1481 6.1681C8.1275 6.25575 8.08362 6.33622 8.02108 6.401L2.77308 11.84C2.5969 12.0283 2.50151 12.2782 2.50738 12.536C2.51325 12.7939 2.6199 13.0391 2.80447 13.2193C2.98904 13.3994 3.23684 13.5 3.49474 13.4996C3.75264 13.4992 4.0001 13.3977 4.18408 13.217L9.40808 7.924C9.47615 7.85513 9.56276 7.80751 9.65737 7.78692C9.75199 7.76633 9.85055 7.77365 9.94108 7.808C10.4174 7.98775 10.9316 8.04333 11.4353 7.9695C11.9391 7.89567 12.4157 7.69487 12.8204 7.38599C13.2251 7.07711 13.5445 6.67033 13.7487 6.20393C13.9528 5.73753 14.0348 5.22686 13.9871 4.72L12.3541 6.354C12.2603 6.44774 12.1332 6.5004 12.0006 6.5004C11.868 6.5004 11.7408 6.44774 11.6471 6.354L9.64709 4.354C9.55335 4.26024 9.50069 4.13309 9.50069 4.0005C9.50069 3.86792 9.55335 3.74077 9.64709 3.647L11.2801 2.013C11.187 2.00437 11.0936 2.00003 11.0001 2V2Z"
  };

  private MenuController menu;
  private final String grey = "#0067B1";
  private final String svgCSSLine = "-fx-background-color: %s";

  public void initialize() {
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    SVGPath externalIcon = new SVGPath();
    externalIcon.setContent(icons[0]);
    externalRegion.setShape(externalIcon);
    externalRegion.setStyle(String.format(svgCSSLine, grey));

    SVGPath internalPatientIcon = new SVGPath();
    internalPatientIcon.setContent(icons[1]);
    externalRegionIPT.setShape(internalPatientIcon);
    externalRegionIPT.setStyle(String.format(svgCSSLine, grey));

    SVGPath facilityMaitIcon = new SVGPath();
    facilityMaitIcon.setContent(icons[2]);
    externalRegionFM.setShape(facilityMaitIcon);
    externalRegionFM.setStyle(String.format(svgCSSLine, grey));

    // Set each name label to center
    teamZLabel.setAlignment(Pos.CENTER);
    teamBLabel.setAlignment(Pos.CENTER);
    teamCLabel.setAlignment(Pos.CENTER);
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "API Service Request Landing";
  }

  @FXML
  private void navLandingPage() throws IOException {
    System.out.println("navigating to landing page from api landing page");
    menu.load(toLandingPageURL);
  }

  @FXML
  private void toExternalPatientTransportation() {
    System.out.println("navigating to transportation from api landing page");
    ExternalTransportFacadeAPI.getInstance().run(apiCSSPath);
  }

  @FXML
  private void toInternalTransportRequest() {
    System.out.println("navigating to sanitation from api landing page");
    InternalTransportFacadeAPI.getInstance().run(apiCSSPath);
  }

  @FXML
  private void toFacilityMaintenanceRequest() {
    System.out.println("navigating to security from api landing page");
    FacilityMaintenanceFacadeAPI.getInstance().run(apiCSSPath);
  }
}
