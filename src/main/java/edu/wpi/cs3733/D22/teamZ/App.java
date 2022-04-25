package edu.wpi.cs3733.D22.teamZ;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.observers.DirtyBedObserver;
import edu.wpi.cs3733.D22.teamZ.observers.DirtyPumpObserver;
import java.io.IOException;
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
    Parent root = FXMLLoader.load(App.class.getResource("views/LoginPage.fxml"));
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    //    primaryStage.minHeightProperty().unbind();
    //    primaryStage.maxHeightProperty().unbind();
    //    initialHeight = 400;
    //    initialWidth = 600;
    //    primaryStage.setHeight(initialHeight); // initial size. doesnt work if less so ignore lol.
    //    primaryStage.setWidth(initialWidth);
    //
    //    // TODO fix scaling on other login pages after logout
    //
    //    initialRatio = initialHeight / initialWidth;
    //    //
    //    //
    // primaryStage.minHeightProperty().bind(primaryStage.widthProperty().multiply(initialRatio));
    //    //
    //    //
    // primaryStage.maxHeightProperty().bind(primaryStage.widthProperty().multiply(initialRatio));
    //
    //    sizeChangeListener =
    //        (ChangeListener<Number>)
    //            (observable, oldValue, newValue) -> {
    //              onSizeChange(root, primaryStage);
    //            };
    // primaryStage.heightProperty().addListener(sizeChangeListener);
    // primaryStage.widthProperty().addListener(sizeChangeListener);
    primaryStage.setTitle("Team Z - Brigham and Women's Hospital App");
    primaryStage.getIcons().add(new Image("edu/wpi/cs3733/D22/teamZ/images/Hospital-Logo.png"));
    primaryStage.setScene(scene);
    primaryStage.show();
    FacadeDAO dao = FacadeDAO.getInstance();
    List<Location> dirtyTest =
        List.of(
            dao.getLocationByID("zSTOR00305"),
            dao.getLocationByID("zSTOR00303"),
            dao.getLocationByID("zSTOR00403"),
            dao.getLocationByID("zSTOR00304"),
            dao.getLocationByID("zSTOR00404"));

    for (Location dirtyLocation : dirtyTest) {
      new DirtyBedObserver(dirtyLocation);
    }

    List<Location> dirtyPumpLocations =
        List.of(
            dao.getLocationByID("zDIRT00103"),
            dao.getLocationByID("zDIRT00104"),
            dao.getLocationByID("zDIRT00105"));

    for (Location dirtyLocation : dirtyPumpLocations) {
      new DirtyPumpObserver(dirtyLocation);
    }
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }

  public void onSizeChange(Parent root, Stage primaryStage) {
    // System.out.println("old:" + oldValue + " new:" + newValue);
    float scaleY = (float) (primaryStage.getHeight() / initialHeight);
    float scaleX = (float) (primaryStage.getWidth() / initialWidth);
    if (initialStates == null) {
      initialStates = root.getTransforms();
    }
    root.getTransforms().setAll(initialStates);

    root.getTransforms().add(new Scale(scaleX, scaleY, 0, 0));
  }
}
