package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GiftServiceRequestListController implements Initializable, IMenuAccess {
  // Main table
  @FXML public TableView<GiftServiceRequest> tableContainer;
  public MFXButton exportCSVButton;
  public MFXButton refreshButton;
  @FXML private TableColumn<GiftServiceRequest, String> idCol;
  @FXML private TableColumn<GiftServiceRequest, String> typeCol;
  @FXML private TableColumn<GiftServiceRequest, Employee> issuerCol;
  @FXML private TableColumn<GiftServiceRequest, Employee> handlerCol;
  @FXML private TableColumn<GiftServiceRequest, ServiceRequest.RequestStatus> statusCol;
  @FXML private TableColumn<GiftServiceRequest, Location> locationCol;
  @FXML private MFXButton backToGifts;

  private final String toGiftRequest = "edu/wpi/cs3733/D22/teamZ/views/GiftServices.fxml";

  protected MenuController menu;

  // Changes per every implementation
  // The name of the page that will be displayed in the bottom menu bar.
  protected String menuName;

  private ObservableList<GiftServiceRequest> requests;

  // Database object
  private FacadeDAO facadeDAO;

  public void onBackToGiftsClicked(ActionEvent event) {
    try {
      menu.load(toGiftRequest);
    } catch (IOException e) {
      System.out.println("failed to go back to gift request");
    }
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Gift Request List Page";
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    facadeDAO = FacadeDAO.getInstance();
    try {
      createTable();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /** creates table of all cleaning requests */
  public void createTable() throws SQLException {
    tableContainer.refresh();
    idCol.setCellValueFactory(new PropertyValueFactory<>("requestID"));
    typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    issuerCol.setCellValueFactory(new PropertyValueFactory<>("issuer"));
    handlerCol.setCellValueFactory(new PropertyValueFactory<>("handler"));
    statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    locationCol.setCellValueFactory(new PropertyValueFactory<>("targetLocation"));
    requests = FXCollections.observableList(facadeDAO.getAllGiftRequests());
    tableContainer.setItems(requests);
  }

  public void refreshClicked(ActionEvent event) {
    try {
      createTable();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /** EXPORT to CSV button is clicked */
  public void exportToCSV(ActionEvent actionEvent) {
    FileChooser fileChooser = new FileChooser();
    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    fileChooser.setTitle("Enter a .csv file...");
    FileChooser.ExtensionFilter extFilter =
        new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
    fileChooser.getExtensionFilters().add(extFilter);

    File defaultFile = facadeDAO.getDefaultGiftServiceCSVPath();
    if (defaultFile.isDirectory()) {
      fileChooser.setInitialDirectory(defaultFile);
    } else {
      fileChooser.setInitialDirectory(defaultFile.getParentFile());
      fileChooser.setInitialFileName(defaultFile.getName());
    }

    File file = fileChooser.showSaveDialog(stage);

    if (file != null) {
      facadeDAO.exportGiftRequestToCSV(file);
    }
  }
}
