package edu.wpi.cs3733.D22.teamZ.controllers;

import io.github.palexdev.materialfx.controls.MFXToggleButton;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
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
    "M9.91667 9.16663L15.3699 17.8896C15.3971 17.9335 15.4122 17.9839 15.4134 18.0356C15.4147 18.0872 15.4022 18.1383 15.3771 18.1835C15.3521 18.2287 15.3155 18.2664 15.271 18.2927C15.2266 18.3191 15.1759 18.3331 15.1243 18.3333H2.87575C2.82382 18.333 2.77291 18.3189 2.72831 18.2923C2.68371 18.2657 2.64706 18.2276 2.62216 18.182C2.59727 18.1364 2.58504 18.085 2.58676 18.0331C2.58847 17.9812 2.60407 17.9307 2.63192 17.8869L8.08334 9.16663V2.74996H9.91667V9.16663ZM4.41667 0.916626V2.74996H6.25V8.67163L1.07909 16.9125C0.877471 17.2338 0.765551 17.6032 0.75495 17.9824C0.744349 18.3616 0.835453 18.7367 1.0188 19.0688C1.20215 19.4009 1.47106 19.6778 1.7976 19.8709C2.12415 20.0639 2.49641 20.1661 2.87575 20.1666H15.1243C15.5036 20.1661 15.8759 20.0639 16.2024 19.8709C16.5289 19.6778 16.7979 19.4009 16.9812 19.0688C17.1646 18.7367 17.2557 18.3616 17.2451 17.9824C17.2345 17.6032 17.1225 17.2338 16.9209 16.9125L11.7509 8.67163L11.75 2.74996H13.5833V0.916626H4.41667Z",
    "M8 16C8.26522 16 8.51957 15.8946 8.70711 15.7071C8.89464 15.5196 9 15.2652 9 15C9 14.7348 8.89464 14.4804 8.70711 14.2929C8.51957 14.1054 8.26522 14 8 14C7.73478 14 7.48043 14.1054 7.29289 14.2929C7.10536 14.4804 7 14.7348 7 15C7 15.2652 7.10536 15.5196 7.29289 15.7071C7.48043 15.8946 7.73478 16 8 16ZM17 15C17 15.2652 16.8946 15.5196 16.7071 15.7071C16.5196 15.8946 16.2652 16 16 16C15.7348 16 15.4804 15.8946 15.2929 15.7071C15.1054 15.5196 15 15.2652 15 15C15 14.7348 15.1054 14.4804 15.2929 14.2929C15.4804 14.1054 15.7348 14 16 14C16.2652 14 16.5196 14.1054 16.7071 14.2929C16.8946 14.4804 17 14.7348 17 15V15ZM10.75 5C10.5511 5 10.3603 5.07902 10.2197 5.21967C10.079 5.36032 10 5.55109 10 5.75C10 5.94891 10.079 6.13968 10.2197 6.28033C10.3603 6.42098 10.5511 6.5 10.75 6.5H13.25C13.4489 6.5 13.6397 6.42098 13.7803 6.28033C13.921 6.13968 14 5.94891 14 5.75C14 5.55109 13.921 5.36032 13.7803 5.21967C13.6397 5.07902 13.4489 5 13.25 5H10.75ZM7.75 2C6.75544 2 5.80161 2.39509 5.09835 3.09835C4.39509 3.80161 4 4.75544 4 5.75V9.5H2.75C2.55109 9.5 2.36032 9.57902 2.21967 9.71967C2.07902 9.86032 2 10.0511 2 10.25C2 10.4489 2.07902 10.6397 2.21967 10.7803C2.36032 10.921 2.55109 11 2.75 11H4V19.75C4 20.716 4.783 21.5 5.75 21.5H7.25C7.71413 21.5 8.15925 21.3156 8.48744 20.9874C8.81563 20.6592 9 20.2141 9 19.75V18.5H15V19.75C15 20.716 15.784 21.5 16.75 21.5H18.25C18.7141 21.5 19.1592 21.3156 19.4874 20.9874C19.8156 20.6592 20 20.2141 20 19.75V11H21.227C21.4259 11 21.6167 10.921 21.7573 10.7803C21.898 10.6397 21.977 10.4489 21.977 10.25C21.977 10.0511 21.898 9.86032 21.7573 9.71967C21.6167 9.57902 21.4259 9.5 21.227 9.5H20V5.75C20 4.75544 19.6049 3.80161 18.9017 3.09835C18.1984 2.39509 17.2446 2 16.25 2H7.75ZM18.5 18.5V19.75C18.5 19.8163 18.4737 19.8799 18.4268 19.9268C18.3799 19.9737 18.3163 20 18.25 20H16.75C16.6837 20 16.6201 19.9737 16.5732 19.9268C16.5263 19.8799 16.5 19.8163 16.5 19.75V18.5H18.5ZM18.5 17H5.5V13H18.5V17ZM5.5 19.75V18.5H7.5V19.75C7.5 19.8163 7.47366 19.8799 7.42678 19.9268C7.37989 19.9737 7.3163 20 7.25 20H5.75C5.6837 20 5.62011 19.9737 5.57322 19.9268C5.52634 19.8799 5.5 19.8163 5.5 19.75ZM5.5 5.75C5.5 5.15326 5.73705 4.58097 6.15901 4.15901C6.58097 3.73705 7.15326 3.5 7.75 3.5H16.25C16.8467 3.5 17.419 3.73705 17.841 4.15901C18.2629 4.58097 18.5 5.15326 18.5 5.75V11.5H5.5V5.75Z",
    // "M11,5.5H8V4h0.5c0.55,0,1-0.45,1-1c0-0.55-0.45-1-1-1h-3c-0.55,0-1,0.45-1,1c0,0.55,0.45,1,1,1H6v1.5H3c-0.55,0-1,0.45-1,1 c0,0.55,0.45,1,1,1V15c0,1.1,0.9,2,2,2h1v4l2,1.5V17h1c1.1,0,2-0.9,2-2V7.5c0.55,0,1-0.45,1-1C12,5.95,11.55,5.5,11,5.5z M9,9H7.25 C6.84,9,6.5,9.34,6.5,9.75c0,0.41,0.34,0.75,0.75,0.75H9V12H7.25c-0.41,0-0.75,0.34-0.75,0.75c0,0.41,0.34,0.75,0.75,0.75H9L9,15H5 V7.5h4V9z M19.5,10.5V10c0.55,0,1-0.45,1-1c0-0.55-0.45-1-1-1h-5c-0.55,0-1,0.45-1,1c0,0.55,0.45,1,1,1v0.5c0,0.5-1.5,1.16-1.5,3V20 c0,1.1,0.9,2,2,2h4c1.1,0,2-0.9,2-2v-6.5C21,11.66,19.5,11,19.5,10.5z M16.5,10.5V10h1v0.5c0,1.6,1.5,2,1.5,3V14h-4 c0-0.21,0-0.39,0-0.5C15,12.5,16.5,12.1,16.5,10.5z M19,15.5V17h-4c0-0.51,0-1.02,0-1.5H19z M15,20c0,0,0-0.63,0-1.5h4V20H15z",
    "M12.87 15.07l-2.54-2.51.03-.03c1.74-1.94 2.98-4.17 3.71-6.53H17V4h-7V2H8v2H1v1.99h11.17C11.5 7.92 10.44 9.75 9 11.35 8.07 10.32 7.3 9.19 6.69 8h-2c.73 1.63 1.73 3.17 2.98 4.56l-5.09 5.02L4 19l5-5 3.11 3.11.76-2.04zM18.5 10h-2L12 22h2l1.12-3h4.75L21 22h2l-4.5-12zm-2.62 7l1.62-4.33L19.12 17h-3.24z",
    "M7.34736 9.8306C7.51805 9.79648 7.66819 9.69596 7.76476 9.55115C7.86132 9.40634 7.89641 9.22909 7.8623 9.05841C7.82819 8.88773 7.72767 8.73758 7.58285 8.64102C7.43804 8.54445 7.26079 8.50936 7.09011 8.54347C6.29999 8.70097 5.70324 9.25397 5.38561 9.9951C5.07061 10.7283 5.02424 11.6453 5.27011 12.6271C5.28931 12.7124 5.32537 12.793 5.37618 12.8642C5.42698 12.9354 5.4915 12.9957 5.56595 13.0416C5.6404 13.0875 5.72327 13.1181 5.80969 13.1315C5.89612 13.1449 5.98435 13.1409 6.06921 13.1198C6.15406 13.0986 6.23383 13.0607 6.30383 13.0082C6.37382 12.9558 6.43263 12.8899 6.47679 12.8144C6.52095 12.7389 6.54958 12.6554 6.56099 12.5686C6.5724 12.4819 6.56637 12.3938 6.54324 12.3095C6.35074 11.5412 6.41374 10.927 6.59136 10.5122C6.76636 10.1045 7.04461 9.89097 7.34736 9.82972V9.8306ZM4.78711 2.9916C4.74977 3.61282 4.84074 4.23508 5.05435 4.81962C5.26797 5.40415 5.59965 5.93845 6.02874 6.38922C5.19518 6.62822 4.44889 7.10399 3.88043 7.75881C3.31196 8.41363 2.94576 9.21935 2.82624 10.0782C2.53823 12.139 2.96105 14.2365 4.02499 16.0247L4.33124 16.541C4.34284 16.56 4.3554 16.5784 4.36886 16.5961L5.60786 18.2638C5.87029 18.6173 6.20661 18.9093 6.5934 19.1196C6.98018 19.3298 7.40815 19.4532 7.8475 19.4812C8.28684 19.5092 8.72701 19.4411 9.13734 19.2816C9.54768 19.1221 9.91833 18.8752 10.2235 18.5578C10.2592 18.5207 10.302 18.4912 10.3493 18.4711C10.3967 18.4509 10.4476 18.4405 10.4991 18.4405C10.5506 18.4405 10.6015 18.4509 10.6489 18.4711C10.6963 18.4912 10.7391 18.5207 10.7747 18.5578C11.0799 18.8752 11.4505 19.1221 11.8609 19.2816C12.2712 19.4411 12.7114 19.5092 13.1507 19.4812C13.5901 19.4532 14.018 19.3298 14.4048 19.1196C14.7916 18.9093 15.1279 18.6173 15.3904 18.2638L16.6294 16.5952C16.6425 16.5775 16.6548 16.5591 16.6661 16.5401L16.9732 16.0238C18.0379 14.2361 18.4616 12.139 18.1746 10.0782C18.0899 9.47098 17.8814 8.8876 17.5621 8.36418C17.2428 7.84076 16.8195 7.38846 16.3183 7.03526C15.8172 6.68207 15.2489 6.4355 14.6485 6.31083C14.0482 6.18616 13.4287 6.18604 12.8284 6.31047L11.165 6.65522H11.1624C11.1886 5.97272 11.2954 5.23772 11.5045 4.60947C11.7582 3.84997 12.11 3.39322 12.502 3.23047C12.6627 3.16341 12.7902 3.03525 12.8564 2.87419C12.9226 2.71313 12.9221 2.53236 12.855 2.37166C12.788 2.21095 12.6598 2.08347 12.4988 2.01726C12.3377 1.95105 12.1569 1.95153 11.9962 2.0186C11.0757 2.40272 10.5525 3.3136 10.2594 4.19385L10.2191 4.3181C10.0036 3.90173 9.72533 3.52102 9.39399 3.18935C8.94019 2.73504 8.39506 2.38232 7.7947 2.15454C7.19433 1.92677 6.55243 1.82913 5.91149 1.8681C5.61898 1.88493 5.34284 2.00854 5.13542 2.21547C4.92801 2.4224 4.80376 2.69826 4.78624 2.99072L4.78711 2.9916ZM6.09349 3.17272C6.53257 3.16005 6.9696 3.23735 7.3777 3.39986C7.78579 3.56238 8.15632 3.80668 8.46649 4.11772C8.77753 4.42789 9.02183 4.79842 9.18434 5.20651C9.34686 5.61461 9.42416 6.05164 9.41149 6.49072C9.25279 6.49503 9.09399 6.48772 8.93636 6.46885L8.39649 6.35685C7.88334 6.20376 7.41655 5.92495 7.03849 5.54572C6.72744 5.23555 6.48314 4.86503 6.32063 4.45693C6.15811 4.04883 6.08081 3.6118 6.09349 3.17272V3.17272ZM7.90386 7.59497L8.08061 7.63172C8.29061 7.69122 8.50411 7.73585 8.71936 7.76472L9.56811 7.93972C10.1828 8.06707 10.8171 8.06707 11.4319 7.93972L13.0961 7.59497C13.5206 7.50699 13.9587 7.50709 14.3831 7.59526C14.8076 7.68343 15.2095 7.8578 15.5638 8.10757C15.9182 8.35733 16.2175 8.67718 16.4432 9.04731C16.6689 9.41744 16.8163 9.82997 16.8761 10.2593C17.1218 12.0248 16.7586 13.8214 15.8462 15.3527L15.5566 15.8401L14.3377 17.4816C14.189 17.682 13.9983 17.8475 13.7791 17.9667C13.5598 18.0858 13.3172 18.1558 13.0682 18.1716C12.8191 18.1874 12.5696 18.1488 12.337 18.0583C12.1045 17.9678 11.8944 17.8278 11.7215 17.6478C11.5634 17.4833 11.3737 17.3524 11.1638 17.263C10.9539 17.1736 10.7281 17.1275 10.5 17.1275C10.2718 17.1275 10.046 17.1736 9.83614 17.263C9.62624 17.3524 9.43656 17.4833 9.27849 17.6478C9.10557 17.8278 8.89551 17.9678 8.66293 18.0583C8.43035 18.1488 8.18085 18.1874 7.9318 18.1716C7.68275 18.1558 7.44015 18.0858 7.22089 17.9667C7.00163 17.8475 6.81099 17.682 6.66224 17.4816L5.44336 15.8401L5.15374 15.3527C4.24171 13.8213 3.87876 12.0247 4.12474 10.2593C4.18459 9.83006 4.33191 9.41762 4.55758 9.04756C4.78326 8.6775 5.08248 8.3577 5.43673 8.10794C5.79098 7.85819 6.19272 7.68379 6.61709 7.59556C7.04146 7.50732 7.47942 7.50712 7.90386 7.59497V7.59497Z",
    "M22.0477 5.5074L17.6094 2.9738H17.6004L17.5555 2.95584H17.5375L17.4926 2.93787H14.375C14.1844 2.93787 14.0016 3.01359 13.8668 3.14838C13.732 3.28317 13.6563 3.46599 13.6563 3.65662C13.6563 4.22849 13.4291 4.77694 13.0247 5.18132C12.6203 5.58569 12.0719 5.81287 11.5 5.81287C10.9281 5.81287 10.3797 5.58569 9.9753 5.18132C9.57093 4.77694 9.34375 4.22849 9.34375 3.65662C9.34375 3.46599 9.26803 3.28317 9.13324 3.14838C8.99844 3.01359 8.81563 2.93787 8.625 2.93787H5.50742L5.4625 2.95584H5.44453L5.39961 2.9738H5.39063L0.952346 5.5074C0.632329 5.68766 0.394041 5.98425 0.286966 6.33559C0.179891 6.68693 0.212258 7.06602 0.377346 7.39412L2.03047 10.7094C2.15034 10.9472 2.33395 11.1471 2.56081 11.2867C2.78767 11.4263 3.04886 11.5002 3.31524 11.5H5.03125V18.6875C5.03125 19.0687 5.1827 19.4344 5.45229 19.7039C5.72187 19.9735 6.0875 20.125 6.46875 20.125H16.5313C16.9125 20.125 17.2781 19.9735 17.5477 19.7039C17.8173 19.4344 17.9688 19.0687 17.9688 18.6875V11.5H19.6848C19.9511 11.5002 20.2123 11.4263 20.4392 11.2867C20.6661 11.1471 20.8497 10.9472 20.9695 10.7094L22.6227 7.39412C22.7877 7.06602 22.8201 6.68693 22.713 6.33559C22.606 5.98425 22.3677 5.68766 22.0477 5.5074V5.5074ZM3.31524 10.0625L1.66211 6.75623L5.03125 4.83357V10.0625H3.31524ZM16.5313 18.6875H6.46875V4.31248H7.97813C8.14309 5.12487 8.58384 5.85525 9.22569 6.37987C9.86754 6.90449 10.671 7.19108 11.5 7.19108C12.329 7.19108 13.1325 6.90449 13.7743 6.37987C14.4162 5.85525 14.8569 5.12487 15.0219 4.31248H16.5313V18.6875ZM19.6848 10.0625H17.9688V4.83357L21.3379 6.75623L19.6848 10.0625Z",
    "M26 24.005H6C5.46973 24.0045 4.96133 23.7936 4.58637 23.4186C4.21141 23.0437 4.00053 22.5353 4 22.005V8.005C4.00053 7.47473 4.21141 6.96633 4.58637 6.59138C4.96133 6.21642 5.46973 6.00553 6 6.005H26C26.5303 6.00553 27.0387 6.21642 27.4136 6.59138C27.7886 6.96633 27.9995 7.47473 28 8.005V22.005C27.9992 22.5352 27.7882 23.0434 27.4133 23.4183C27.0384 23.7932 26.5302 24.0042 26 24.005V24.005ZM6 8.005V22.005H26V8.005H6ZM2 26.005H30V28.005H2V26.005Z",
    "M880 310H732.4C746 288.6 754 263.2 754 236C754 159.9 692.1 98 616 98C574.6 98 537.3 116.4 512 145.4C486.7 116.4 449.4 98 408 98C331.9 98 270 159.9 270 236C270 263.2 277.9 288.6 291.6 310H144C126.3 310 112 324.3 112 342V542C112 546.4 115.6 550 120 550H160V894C160 911.7 174.3 926 192 926H832C849.7 926 864 911.7 864 894V550H904C908.4 550 912 546.4 912 542V342C912 324.3 897.7 310 880 310ZM546 236C546 197.4 577.4 166 616 166C654.6 166 686 197.4 686 236C686 274.6 654.6 306 616 306H546V236ZM408 166C446.6 166 478 197.4 478 236V306H408C369.4 306 338 274.6 338 236C338 197.4 369.4 166 408 166ZM180 482V378H478V482H180ZM228 550H478V858H228V550ZM796 858H546V550H796V858ZM844 482H546V378H844V482Z",
    "M22.062 25.602L11.33 5.41597C11.2684 5.30002 11.2301 5.17305 11.2176 5.04232C11.205 4.9116 11.2183 4.77967 11.2566 4.65408C11.3342 4.40044 11.5093 4.18799 11.7435 4.06347C11.9777 3.93895 12.2518 3.91256 12.5054 3.99011C12.631 4.02851 12.7478 4.09127 12.8491 4.1748C12.9505 4.25834 13.0344 4.36102 13.096 4.47697L23.829 24.663L25.351 23.853C25.8149 23.6064 26.3228 23.4535 26.8458 23.4032C27.3687 23.3529 27.8964 23.4062 28.3988 23.5598C28.9012 23.7135 29.3684 23.9647 29.7737 24.2989C30.179 24.6332 30.5145 25.044 30.761 25.508L31.409 26.726L38.278 36.781L24.029 44.357L19.534 33.039L18.887 31.821C18.6404 31.3571 18.4875 30.8493 18.4371 30.3264C18.3868 29.8035 18.4399 29.2758 18.5935 28.7734C18.7471 28.271 18.9981 27.8038 19.3323 27.3985C19.6664 26.9931 20.0772 26.6576 20.541 26.411L22.063 25.602H22.062ZM21.479 28.177L26.289 25.62C26.5209 25.4966 26.7749 25.4202 27.0363 25.395C27.2978 25.3698 27.5616 25.3964 27.8128 25.4732C28.064 25.55 28.2976 25.6755 28.5003 25.8426C28.7029 26.0097 28.8707 26.215 28.994 26.447L29.642 27.664L21.299 32.1L20.652 30.882C20.5287 30.6501 20.4522 30.3961 20.4271 30.1347C20.4019 29.8732 20.4284 29.6094 20.5052 29.3582C20.582 29.107 20.7076 28.8734 20.8746 28.6707C21.0417 28.468 21.2471 28.3003 21.479 28.177V28.177ZM22.309 34.609L25.062 41.542L26.896 40.567L24.731 36.352L26.511 35.438L28.663 39.628L35.365 36.064L31.157 29.904L22.309 34.609V34.609Z"
    "M11 9h2V6h3V4h-3V1h-2v3H8v2h3v3zm-4 9c-1.1 0-1.99.9-1.99 2S5.9 22 7 22s2-.9 2-2-.9-2-2-2zm10 0c-1.1 0-1.99.9-1.99 2s.89 2 1.99 2 2-.9 2-2-.9-2-2-2zm-8.9-5h7.45c.75 0 1.41-.41 1.75-1.03l3.86-7.01L19.42 4l-3.87 7H8.53L4.27 2H1v2h2l3.6 7.59-1.35 2.44C4.52 15.37 5.48 17 7 17h12v-2H7l1.1-2z"
  };

  String cleaningPath =
      "M22.062 25.602L11.33 5.41597C11.2684 5.30002 11.2301 5.17305 11.2176 5.04232C11.205 4.9116 11.2183 4.77967 11.2566 4.65408C11.3342 4.40044 11.5093 4.18799 11.7435 4.06347C11.9777 3.93895 12.2518 3.91256 12.5054 3.99011C12.631 4.02851 12.7478 4.09127 12.8491 4.1748C12.9505 4.25834 13.0344 4.36102 13.096 4.47697L23.829 24.663L25.351 23.853C25.8149 23.6064 26.3228 23.4535 26.8458 23.4032C27.3687 23.3529 27.8964 23.4062 28.3988 23.5598C28.9012 23.7135 29.3684 23.9647 29.7737 24.2989C30.179 24.6332 30.5145 25.044 30.761 25.508L31.409 26.726L38.278 36.781L24.029 44.357L19.534 33.039L18.887 31.821C18.6404 31.3571 18.4875 30.8493 18.4371 30.3264C18.3868 29.8035 18.4399 29.2758 18.5935 28.7734C18.7471 28.271 18.9981 27.8038 19.3323 27.3985C19.6664 26.9931 20.0772 26.6576 20.541 26.411L22.063 25.602H22.062ZM21.479 28.177L26.289 25.62C26.5209 25.4966 26.7749 25.4202 27.0363 25.395C27.2978 25.3698 27.5616 25.3964 27.8128 25.4732C28.064 25.55 28.2976 25.6755 28.5003 25.8426C28.7029 26.0097 28.8707 26.215 28.994 26.447L29.642 27.664L21.299 32.1L20.652 30.882C20.5287 30.6501 20.4522 30.3961 20.4271 30.1347C20.4019 29.8732 20.4284 29.6094 20.5052 29.3582C20.582 29.107 20.7076 28.8734 20.8746 28.6707C21.0417 28.468 21.2471 28.3003 21.479 28.177V28.177ZM22.309 34.609L25.062 41.542L26.896 40.567L24.731 36.352L26.511 35.438L28.663 39.628L35.365 36.064L31.157 29.904L22.309 34.609V34.609Z";

  private MenuController menu;
  private final String grey = "#0075FF";
  private final String svgCSSLine = "-fx-background-color: %s";

  public void initialize() {
    toggleNames.setMainColor(Color.rgb(0, 117, 225));
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
    cleaningIcon.setContent(cleaningPath);
    cleaningRegion.setShape(cleaningIcon);
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
