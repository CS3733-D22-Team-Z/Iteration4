package edu.wpi.cs3733.D22.teamZ.controllers;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.MealServiceRequest;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MealServiceListController implements Initializable, IMenuAccess {
  // Back button to go back to request page
  @FXML private MFXButton backToRequestPage;
  // Button that re-fetches requests and refreshes table.
  @FXML private MFXButton refreshButton;

  // Buttons to select the sorting/filter parameters.
  @FXML private HBox sortingButtons;

  // Selector button stuff
  private MFXButton lastButtonPressed;
  private List<String> prevCSS;

  // Drop-down box that selects which data type to filter by.
  @FXML private ComboBox<String> filterCBox;

  // Details Table
  @FXML public TableView<TableColumnItems> statusTable;
  @FXML private TableColumn<TableColumnItems, String> labelsColumn;
  @FXML private TableColumn<TableColumnItems, String> detailsColumn;

  // Main table
  @FXML public TableView<RequestRow> tableContainer;

  private final String toHomepageURL = "views/Homepage.fxml";
  private final String requestPageURL = "edu/wpi/cs3733/D22/teamZ/views/MealService.fxml"; // change

  // List of identifiers for each
  private final String[] identifiers = {
    "ID", "Meal Type", "Issuer", "Handler", "Status", "Target Location" // change
  };

  // Columns to be represented by the table
  private final List<String> visibleColumns =
      List.of("ID", "Meal Type", "Status", "Issuer"); // change

  // Retriever functions. Correspond to visible columns.
  private final List<RequestRowFunc> retrievers =
      List.of(row -> row.id, row -> row.mealType, row -> row.status, row -> row.issuer);

  private final List<RequestFunc> detailRetrievers =
      List.of(
          request -> request.getRequestID(),
          request -> request.getEntree(),
          request -> request.getIssuer().getDisplayName(),
          request -> {
            if (request.getHandler() != null) return request.getHandler().getDisplayName();
            else return "";
          },
          request -> request.getStatus().toString(),
          request -> request.getTargetLocation().getLongName());

  // List of requests that represents raw data
  private List<MealServiceRequest> rawRequests; // change

  // List of RequestRows currently being displayed on the table
  private ObservableList<RequestRow> requests;
  private ObservableList<RequestRow> dispRequests;
  private String filter = "";

  private MenuController menu;

  // Database object
  private final FacadeDAO facadeDAO;

  public MealServiceListController() { // change
    // Create new database object
    facadeDAO = FacadeDAO.getInstance();

    // Grab data
    loadRequests();

    // Setup CSS map
    prevCSS =
        List.of(
            "-fx-background-radius: 5 0 0 5; ",
            "-fx-border-width: 0 1 0 1; -fx-border-color: #D2D2D2; ",
            "-fx-border-width: 0 1 0 0; -fx-border-color: #D2D2D2; ",
            "-fx-background-radius: 0 5 5 0; ");
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Meal Service Request List";
  } // change

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Create labels for field values
    for (String identifier : identifiers) {
      Label ID = new Label();
      ID.setText(identifier);
    }

    // Setup details window
    // double width = statusTable.getPrefWidth() / 2;
    statusTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    labelsColumn.setCellValueFactory(tRow -> tRow.getValue().label);
    labelsColumn.setSortable(false);
    labelsColumn.setReorderable(false);

    detailsColumn.setCellValueFactory(tRow -> tRow.getValue().detail);
    detailsColumn.setSortable(false);
    detailsColumn.setReorderable(false);

    List<TableColumn<RequestRow, String>> columns = new ArrayList<>();
    for (int i = 0; i < visibleColumns.size(); i++) {
      TableColumn<RequestRow, String> column = new TableColumn<>();
      column.setText(visibleColumns.get(i));
      column.setReorderable(false);
      column.setSortable(false);
      int finalI = i;
      column.setCellValueFactory(rRow -> retrievers.get(finalI).call(rRow.getValue()));
      columns.add(column);
    }

    tableContainer.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    tableContainer.getColumns().addAll(columns);

    tableContainer
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (obs, oldVal, newVal) ->
                loadRow(tableContainer.getSelectionModel().getSelectedItem().id.get()));

    // Initialize requests
    requests = FXCollections.observableArrayList();
    createRRList();
  }

  // Called whenever one of the filter buttons are clicked.
  public void filterClicked(ActionEvent event) {
    // Get CSS idx
    int cssIdx = sortingButtons.getChildren().indexOf(event.getTarget());
    if (cssIdx > 1)
      if (cssIdx == (sortingButtons.getChildren().size() - 1)) cssIdx = 3;
      else cssIdx = 2;

    // Revert previous button
    if (lastButtonPressed != null) lastButtonPressed.setStyle(prevCSS.get(cssIdx));

    filterCBox.getItems().clear();
    if (lastButtonPressed == null || !lastButtonPressed.equals(event.getTarget())) {
      lastButtonPressed = (MFXButton) event.getTarget();
      filter = lastButtonPressed.getText();
      lastButtonPressed.setStyle(
          prevCSS.get(cssIdx) + "-fx-background-color: #0075ff; -fx-text-fill: #FFFFFF");

      // Filter buttons
      Comparator<RequestRow> comparator =
          Comparator.comparing(r -> r.retrievePropertyFromType(filter));
      FXCollections.sort(requests, comparator);

      // Setup filter box
      Set<String> allFilterOptions = new HashSet<>();
      for (RequestRow row : requests) {
        allFilterOptions.add(row.retrievePropertyFromType(filter));
      }
      allFilterOptions.add("None");
      filterCBox.getItems().addAll(allFilterOptions);
      filterCBox.getSelectionModel().select("None");
    } else {
      tableContainer.setItems(requests);
      lastButtonPressed = null;
    }
  }

  // Called whenever the refresh button is clicked.
  public void refreshClicked(ActionEvent event) {
    System.out.println(refreshButton.getText());

    // Reload requests
    loadRequests();

    // Reload table
    createRRList();
  }

  // Called whenever the filter select was set?
  public void filterSet() {
    String filterOption = filterCBox.getSelectionModel().getSelectedItem();
    FilteredList<RequestRow> fList =
        requests.filtered(
            requestRow -> {
              if (filterOption == null || filterOption.equals("None")) return true;
              return requestRow.retrievePropertyFromType(filter).equals(filterOption);
            });

    tableContainer.setItems(fList);
  }

  public void createRRList() {
    // Clear old requests
    requests.clear();

    // Iterate through each request entity and create RequestRow for each
    for (MealServiceRequest request : rawRequests) { // change

      requests.add(
          new RequestRow(
              detailRetrievers.get(0).call(request),
              detailRetrievers.get(1).call(request),
              detailRetrievers.get(2).call(request),
              detailRetrievers.get(4).call(request)));
    }

    tableContainer.setItems(requests);
  }

  // Load a request into the Details row.
  public void loadRow(String reqID) {
    // Clear out current details data
    statusTable.getItems().clear();

    // Retrieve the request with the given ID.
    MealServiceRequest selectedReq = getRequestFromID(reqID); // change

    // statusTable.getColumns().add(labelsColumn);
    // statusTable.getColumns().add(detailsColumn);

    for (int i = 0; i < identifiers.length; i++) {
      statusTable
          .getItems()
          .add(new TableColumnItems(identifiers[i], detailRetrievers.get(i).call(selectedReq)));
    }
    /*statusTable.getItems().add(new TableColumnItems("ID", selectedReq.getRequestID()));
    statusTable.getItems().add(new TableColumnItems("Type", selectedReq.getType().toString()));
    statusTable.getItems().add(new TableColumnItems("Status", selectedReq.getStatus().toString()));
    statusTable.getItems().add(new TableColumnItems("Issuer", selectedReq.getIssuer().getName()));
    statusTable.getItems().add(new TableColumnItems("Handler", handlerName));
    statusTable
            .getItems()
            .add(new TableColumnItems("Destination", selectedReq.getTargetLocation().getLongName()));*/
  }

  @FXML
  private void onBackToRequestClicked(ActionEvent actionEvent) throws IOException {
    menu.load(requestPageURL);
  }

  public void loadRequests() {
    rawRequests = FacadeDAO.getInstance().getAllMealServiceRequests(); // change
  }

  public MealServiceRequest getRequestFromID(String MeqID) { // change
    return FacadeDAO.getInstance().getMealServiceRequestByID(MeqID); // change
  }

  public void exportToCSV(ActionEvent actionEvent) {

    FileChooser fileChooser = new FileChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    fileChooser.setTitle("Enter a .csv file...");
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);

    File defaultFile = facadeDAO.getDefaultMealServReqCSVPath(); // change
    if (defaultFile.isDirectory()) {
      fileChooser.setInitialDirectory(defaultFile);
    } else {
      fileChooser.setInitialDirectory(defaultFile.getParentFile());
      fileChooser.setInitialFileName(defaultFile.getName());
    }

    File file = fileChooser.showSaveDialog(stage);
    facadeDAO.exportMealServiceRequestsToCSV(file); // change
  }

  public static class TableColumnItems {
    SimpleStringProperty label;
    SimpleStringProperty detail;

    public TableColumnItems(String label, String detail) {
      this.label = new SimpleStringProperty(label);
      this.detail = new SimpleStringProperty(detail);
    }
  }

  // Data structure to represent a row in the request list.
  private static class RequestRow {
    SimpleStringProperty id; // change depending on what you want displayed
    SimpleStringProperty mealType;
    SimpleStringProperty issuer;
    SimpleStringProperty status;

    public RequestRow(String newId, String newType, String newIssuer, String newStatus) {
      id = new SimpleStringProperty(newId);
      mealType = new SimpleStringProperty(newType);
      issuer = new SimpleStringProperty(newIssuer);
      status = new SimpleStringProperty(newStatus);
    }

    /**
     * Gets the property from the String identifier
     *
     * @param type the type of property to be retrieved
     * @return the value of the property
     */
    public String retrievePropertyFromType(String type) {
      switch (type) { // change
        case "ID":
          return id.get();
        case "Meal Type":
          return mealType.get();
        case "Issuer":
          return issuer.get();
        case "Status":
          return status.get();
        default:
          return "";
      }
    }
  }

  private interface RequestRowFunc {
    ObservableStringValue call(RequestRow row);
  }

  private interface RequestFunc {
    String call(MealServiceRequest request); // change
  }
}
