package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.entity.LabServiceRequest;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class LabRequestListController implements Initializable {
  @FXML private TableView<String> labRequestTable;
  @FXML private TableColumn<LabServiceRequest, String> employeeName;
  @FXML private TableColumn<LabServiceRequest, String> employeeID;
  @FXML private TableColumn<LabServiceRequest, String> labType;
  @FXML private TableColumn<LabServiceRequest, String> labStatus;
  @FXML private TableColumn<LabServiceRequest, String> staffAssigned;

  private final String toLabServiceRequestListURL =
      "edu/wpi/cs3733/D22/teamZ/views/LabServiceRequest.fxml";

  @FXML
  private void toLabServiceRequest(ActionEvent event) throws IOException {
    Parent root =
        FXMLLoader.load(getClass().getClassLoader().getResource(toLabServiceRequestListURL));
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
  // @TODO Add database impl for lab requests

  // loadDataFromDatabase when button loadData is clicked
  @FXML
  public void initialize(URL location, ResourceBundle resources) {

    //        System.out.println("loading data");
    //        labRequestTable.getItems().clear();
    //
    //        // get list of locations from db and transfer into ObservableList
    //        data = FXCollections.observableList(locDAO.getAllLocations());
    //
    //
    //        // link columnNames to data
    //        employeeName.setCellValueFactory(new PropertyValueFactory<LabServiceRequest,
    // String>("nodeID"));
    //        employeeID.setCellValueFactory(new PropertyValueFactory<LabServiceRequest,
    // Integer>("xcoord"));
    //        labType.setCellValueFactory(new PropertyValueFactory<LabServiceRequest,
    // Integer>("ycoord"));
    //        labStatus.setCellValueFactory(new PropertyValueFactory<LabServiceRequest,
    // String>("floor"));
    //        staffAssigned.setCellValueFactory(new PropertyValueFactory<LabServiceRequest,
    // String>("building"));
    //
    //        // load data into tableView
    //
    //        labRequestTable.setItems(data);
  }
}
