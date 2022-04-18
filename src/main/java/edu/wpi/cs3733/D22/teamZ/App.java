package edu.wpi.cs3733.D22.teamZ;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.observers.DirtyBedObserver;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  public ObservableList<Transform> initialStates;
  public double initialHeight;
  public double initialWidth;
  public double initialRatio;
  private ChangeListener<? super Number> sizeChangeListener;

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {

    Text text = new Text(1.0, 2.0, "Custom Font");
    Font font = Font.loadFont("resources/fonts/Montserrat-Bold.ttf", 45);
    text.setFont(font);
    text.setFill(Color.BROWN);
    text.setStroke(Color.BLUEVIOLET);
    text.setStrokeWidth(0.5);
    Parent root = FXMLLoader.load(App.class.getResource("views/LoginPage.fxml"));
    Scene scene = new Scene(root);
    primaryStage.setTitle("Team Z - Brigham and Women's Hospital App");
    primaryStage.getIcons().add(new Image("edu/wpi/cs3733/D22/teamZ/images/Hospital-Logo.png"));
    primaryStage.setScene(scene);
    primaryStage.show();

    // Create observers for each dirty location
    FacadeDAO dao = FacadeDAO.getInstance();
    List<Location> dirtyTest = List.of(dao.getLocationByID("zDIRT00103"),dao.getLocationByID("zDIRT00104"),dao.getLocationByID("zDIRT00105"));

    for(Location dirtyLocation : dirtyTest) {
      new DirtyBedObserver(dirtyLocation);
    }
  }

  @Override
  public void stop() {
    // LocationDAOImpl locDAO = new LocationDAOImpl();
    // MedEquipReqDAOImpl reqDAO = new MedEquipReqDAOImpl();
    // locDAO.exportToLocationCSV();
    // reqDAO.exportToMedEquipReqCSV();
    log.info("Shutting Down");
  }
}
