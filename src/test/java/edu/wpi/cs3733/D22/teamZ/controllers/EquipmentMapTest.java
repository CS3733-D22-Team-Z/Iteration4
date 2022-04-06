package edu.wpi.cs3733.D22.teamZ.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.D22.teamZ.database.ILocationDAO;
import edu.wpi.cs3733.D22.teamZ.database.IMedicalEquipmentDAO;
import edu.wpi.cs3733.D22.teamZ.database.LocationDAOImpl;
import edu.wpi.cs3733.D22.teamZ.database.MedicalEquipmentDAOImpl;
import edu.wpi.cs3733.D22.teamZ.entity.Location;
import edu.wpi.cs3733.D22.teamZ.entity.MedicalEquipment;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class EquipmentMapTest extends ApplicationTest {
  /** Will be called with {@code @Before} semantics, i. e. before each test method. */
  private String toEquipmentMapURL = "edu/wpi/cs3733/D22/teamZ/views/EquipmentMap.fxml";

  private String imageRoot = "edu/wpi/cs3733/D22/teamZ/images/%s.png";

  private Random rng;

  // Frequently used XML elements
  JFXComboBox<String> changeFloor;
  Pane iconContainer;
  ImageView mapImage;

  // Databases
  ILocationDAO locDB;
  IMedicalEquipmentDAO medDB;

  @Override
  public void start(Stage stage) throws IOException, InterruptedException {
    rng = new Random(System.currentTimeMillis());

    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getClassLoader().getResource(toEquipmentMapURL));
    Parent equipmentMap = loader.load();
    stage.setScene(new Scene(equipmentMap));
    stage.show();

    changeFloor = lookup("#changeFloor").query();
    iconContainer = lookup("#iconContainer").query();
    mapImage = lookup("#mapImage").query();

    locDB = new LocationDAOImpl();
    medDB = new MedicalEquipmentDAOImpl();
  }

  public String getImgPath(String name) {
    URL rsc = getClass().getClassLoader().getResource(String.format(imageRoot, name));
    return "file:" + rsc.getPath();
  }

  // Check if map defaults to first page
  @Test
  public void map_on_first_page() {
    Assert.assertEquals(mapImage.getImage().getUrl(), getImgPath("1"));
  }

  // Check if map properly switches pages
  @Test
  public void map_switch_page() throws InterruptedException {
    // JFXComboBox<String> box = lookup("#changeFloor").query();
    clickOn(changeFloor);
    for (int i = 0; i < 2; i++) {
      int option = rng.nextInt(2);
      if (option == 0) type(KeyCode.DOWN);
      else type(KeyCode.UP);
    }
    type(KeyCode.ENTER);
    Thread.sleep(1000);
    Assert.assertEquals(
        mapImage.getImage().getUrl(),
        getImgPath(changeFloor.getSelectionModel().getSelectedItem()));
  }

  @Test
  public void map_icons_generated() throws InterruptedException {
    clickOn(changeFloor);
    type(KeyCode.DOWN);
    type(KeyCode.DOWN);
    type(KeyCode.ENTER);
    Thread.sleep(100);
    Assert.assertFalse(iconContainer.getChildren().isEmpty());
  }

  public boolean inRange(int a, int b, int range) {
    return ((b - range) <= a) && ((b + range) >= a);
  }

  @Test
  public void map_icon_properly_configured() throws InterruptedException {
    clickOn(changeFloor);
    type(KeyCode.DOWN);
    type(KeyCode.DOWN);
    type(KeyCode.ENTER);
    Thread.sleep(100);

    List<Location> locs = locDB.getAllLocationsByFloor("3");
    Location sampleLoc = locs.get(rng.nextInt(locs.size()));
    List<MedicalEquipment> sampleEquip = medDB.getAllMedicalEquipmentByLocation(sampleLoc);

    boolean iconFound = false;
    for (Node child : iconContainer.getChildren()) {
      JFXButton btn = (JFXButton) child;
      ImageView graphic = (ImageView) btn.getGraphic();
      Image img = graphic.getImage();
      int x = (int) (btn.getLayoutX() + img.getWidth() / 2);
      int y = (int) (btn.getLayoutY() + img.getWidth() * 3);
      if (!sampleEquip.isEmpty()) {
        if (x == sampleLoc.getXcoord() && y == sampleLoc.getYcoord()) {
          iconFound = true;
          break;
        }
      }
    }

    if (sampleEquip.isEmpty() || iconFound) assert true;
    else assert false;
  }
}
