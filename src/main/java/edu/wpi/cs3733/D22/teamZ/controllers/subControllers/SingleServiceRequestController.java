package edu.wpi.cs3733.D22.teamZ.controllers.subControllers;

import edu.wpi.cs3733.D22.teamZ.entity.ServiceRequest;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SingleServiceRequestController {
  @FXML private VBox valueBox;

  DateTimeFormatter hourFormat = DateTimeFormatter.ofPattern("hh:mm a");

  public void setValues(ServiceRequest request) {
    ((Label) valueBox.getChildren().get(0)).setText(request.getRequestID());
    ((Label) valueBox.getChildren().get(1)).setText(request.getType().toString());
    ((Label) valueBox.getChildren().get(2)).setText(request.getStatus().toString());
    ((Label) valueBox.getChildren().get(3)).setText(request.getIssuer().getDisplayName());
    if (request.getHandler() != null)
      ((Label) valueBox.getChildren().get(4)).setText(request.getHandler().getDisplayName());
    ((Label) valueBox.getChildren().get(5)).setText(request.getTargetLocation().getLongName());
    ((Label) valueBox.getChildren().get(6)).setText(hourFormat.format(request.getOpened()));
    if (request.getClosed() != null)
      ((Label) valueBox.getChildren().get(7)).setText(hourFormat.format(request.getClosed()));
  }
}
