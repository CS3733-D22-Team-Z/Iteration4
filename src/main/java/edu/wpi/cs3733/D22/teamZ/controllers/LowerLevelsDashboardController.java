package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class LowerLevelsDashboardController implements IMenuAccess {

  private FacadeDAO dao = FacadeDAO.getInstance();
  private MenuController menu;

  private ObservableList<MedicalEquipment> data;
  @FXML private TableView lowerLevel1Table;
  @FXML private TableView lowerLevel2Table;
  @FXML private TableColumn idColumnLL1;
  @FXML private TableColumn locationColumnLL1;
  @FXML private TableColumn idColumnLL2;
  @FXML private TableColumn locationColumnLL2;

  private final String toUpperFloorsURL =
      "edu/wpi/cs3733/D22/teamZ/views/UpperFloorsDashboard.fxml";

  @FXML
  public void initialize() {
    createTableLL1();
    createTableLL2();
  }

  /**
   * Button to upper level dashboard
   *
   * @param actionEvent
   */
  public void toUpperFloorsDashboard(ActionEvent actionEvent) throws IOException {
    menu.load(toUpperFloorsURL);
  }

  public void createTableLL1() {
    lowerLevel1Table.getItems().clear();
    idColumnLL1.setCellValueFactory(
        new PropertyValueFactory<MedicalEquipment, String>("equipmentID"));
    locationColumnLL1.setCellValueFactory(
        new PropertyValueFactory<MedicalEquipment, String>("currentLocation"));
    data = FXCollections.observableList(dao.getAllMedicalEquipment());
    lowerLevel1Table.setItems(data);
  }

  public void createTableLL2() {
    lowerLevel2Table.getItems().clear();
    idColumnLL2.setCellValueFactory(
        new PropertyValueFactory<MedicalEquipment, String>("equipmentID"));
    locationColumnLL2.setCellValueFactory(
        new PropertyValueFactory<MedicalEquipment, String>("currentLocation"));
    data = FXCollections.observableList(dao.getAllMedicalEquipment());
    lowerLevel2Table.setItems(data);
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }
}
