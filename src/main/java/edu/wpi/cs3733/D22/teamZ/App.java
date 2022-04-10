package edu.wpi.cs3733.D22.teamZ;

import java.io.IOException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  public ObservableList<Transform> initialStates;

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
    primaryStage.setTitle("Team Z - Brigham and Women's Hospital App");
    primaryStage.getIcons().add(new Image("edu/wpi/cs3733/D22/teamZ/images/Hospital-Logo.png"));
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage
        .maximizedProperty()
        .addListener(
            new ChangeListener<Boolean>() {
              @Override
              public void changed(
                  ObservableValue<? extends Boolean> observable,
                  Boolean oldValue,
                  Boolean newValue) {
                Stage window = primaryStage;
                float oldWidth = window.widthProperty().floatValue();
                float oldHeight = window.heightProperty().floatValue();
                Rectangle2D screenSize = Screen.getPrimary().getBounds();
                float scaleX = (float) (screenSize.getWidth() / oldWidth);
                float scaleY = (float) (screenSize.getHeight() / oldHeight);
                System.out.println(root);
                if (initialStates == null) {
                  initialStates = ((SplitPane) root).getItems().get(1).getTransforms();
                }
                ((SplitPane) root).getItems().get(1).getTransforms().setAll(initialStates);
                // ((SplitPane) root).getItems().get(1).setScaleX(1.2);
                // ((SplitPane) root).getItems().get(1).setScaleY(1.2);
                ((SplitPane) root)
                    .getItems()
                    .get(1)
                    .getTransforms()
                    .add(new Scale(scaleX, scaleY, 0, 0));
              }
            });
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
