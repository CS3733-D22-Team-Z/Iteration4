package edu.wpi.cs3733.D22.teamZ;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

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
    Parent root = FXMLLoader.load(App.class.getResource("views/Menu.fxml"));
    Scene scene = new Scene(root);
    /**
     * scene.addEventFilter( KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() { @Override public
     * void handle(KeyEvent event) { if (event.getCode() == KeyCode.UP || event.getCode() ==
     * KeyCode.DOWN || event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT) {
     * System.out.println("u"); } else if (event.getCode() == KeyCode.SPACE) { // your code for
     * shooting the missile } event.consume(); } });*
     */
    primaryStage.setTitle("Team Z - Brigham and Women's Hospital App");
    primaryStage.getIcons().add(new Image("edu/wpi/cs3733/D22/teamZ/images/Hospital-Logo.png"));
    primaryStage.setScene(scene);
    primaryStage.show();
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
