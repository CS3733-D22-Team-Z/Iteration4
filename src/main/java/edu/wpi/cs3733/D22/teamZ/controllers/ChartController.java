package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipmentDeliveryRequest;
import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class ChartController implements Initializable, IMenuAccess {
  private FacadeDAO facadeDAO;
  private MenuController menu;
  private String menuName;

  @FXML private CategoryAxis x;
  @FXML private NumberAxis y;
  @FXML private LineChart<?, ?> medChart;

  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    facadeDAO = FacadeDAO.getInstance();
    XYChart.Series series = new XYChart.Series();
    series.setName("Equipment Requests");
    int count = 0;
    for (MedicalEquipmentDeliveryRequest medReq : facadeDAO.getAllMedicalEquipmentRequest()) {
      if (medReq.getStatus().equals(ServiceRequest.RequestStatus.DONE)) {
        count++;
        double length = (double) medReq.getOpened().until(medReq.getClosed(), ChronoUnit.HOURS);
        length +=
            (double) ((medReq.getOpened().until(medReq.getClosed(), ChronoUnit.MINUTES)) % 60)
                / 60.0;
        series.getData().add(new XYChart.Data(String.valueOf(count), length));
      }
    }
    XYChart.Series series2 = new XYChart.Series();
    series2.getData().add(new XYChart.Data("1", 5));
    series2.getData().add(new XYChart.Data(String.valueOf(count), 5));
    series2.setName("Goal Time");

    medChart.getData().addAll(series, series2);
  }

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "Chart";
  }
}
