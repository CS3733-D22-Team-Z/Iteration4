package edu.wpi.cs3733.D22.teamZ;

import java.awt.*;
import java.io.IOException;
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
import javafx.scene.transform.Scale;
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
    Parent root = FXMLLoader.load(App.class.getResource("views/Menu.fxml"));
    Scene scene = new Scene(root);
    primaryStage.setTitle("Team Z - Brigham and Women's Hospital App");
    primaryStage.getIcons().add(new Image("edu/wpi/cs3733/D22/teamZ/images/Hospital-Logo.png"));
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.setMinHeight(392); // initial size. doesnt work if less so ignore lol.
    primaryStage.setMinWidth(745);

    initialHeight = primaryStage.getHeight();
    initialWidth = primaryStage.getWidth();
    initialRatio = initialHeight / initialWidth;

    primaryStage.minHeightProperty().bind(primaryStage.widthProperty().multiply(initialRatio));
    primaryStage.maxHeightProperty().bind(primaryStage.widthProperty().multiply(initialRatio));

    sizeChangeListener =
        (ChangeListener<Number>)
            (observable, oldValue, newValue) -> {
              System.out.println("old:" + oldValue + " new:" + newValue);
              float scaleY = (float) (primaryStage.getHeight() / initialHeight);
              float scaleX = (float) (primaryStage.getWidth() / initialWidth);
              if (initialStates == null) {
                initialStates = root.getTransforms();
              }
              root.getTransforms().setAll(initialStates);

              root.getTransforms().add(new Scale(scaleX, scaleY, 0, 0));
            };
    primaryStage.heightProperty().addListener(sizeChangeListener);
    primaryStage.widthProperty().addListener(sizeChangeListener);
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
