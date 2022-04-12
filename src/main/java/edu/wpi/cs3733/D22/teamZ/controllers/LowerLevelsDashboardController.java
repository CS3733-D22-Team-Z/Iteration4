package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableRow;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyTableView;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LowerLevelsDashboardController implements IMenuAccess {

  private FacadeDAO dao = new FacadeDAO();
  private MenuController menu;
  @FXML private MFXLegacyTableView lowerLevel1Table;
  @FXML private MFXLegacyTableView lowerLevel2Table;

  private final String toUpperFloorsURL =
      "edu/wpi/cs3733/D22/teamZ/views/UpperFloorsDashboard.fxml";

  @FXML
  public void initialize() {
    ObservableList<MedicalEquipment> L1temp =
        FXCollections.observableList(dao.getAllMedicalEquipment());
    lowerLevel1Table.setItems(L1temp);
  }

  /**
   * Button to upper level dashboard
   *
   * @param actionEvent
   */
  public void toUpperFloorsDashboard(ActionEvent actionEvent) throws IOException {
    menu.load(toUpperFloorsURL);
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }
}
