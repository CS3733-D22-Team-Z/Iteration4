package edu.wpi.cs3733.D22.teamZ.controllers;

// import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import java.net.URL;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

public class Dashboard3DController extends Application {

  private static final double BASEW = 50;
  private static final double BASEH = 50;

  SmartGroup group;

  private Scene createNewScene() {

    Group modelFloor3 =
        loadModelFloor3Up(
            getClass()
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamZ/models/floor3Up.obj"));

    Group modelFloor4 = modelFloor3;
    Group modelFloor5 = modelFloor3;

    Group model =
        loadModelFloor1(
            getClass().getClassLoader().getResource("edu/wpi/cs3733/D22/teamZ/models/floor1.obj"));

    //    PhongMaterial pm = new PhongMaterial();
    //    pm.setDiffuseMap(new Image((new File("models/computer.png").toURI().toString())));
    //    pm.setSpecularColor(Color.WHITE);
    //    mesh.setMaterial(pm);
    // mesh.setMaterial(new PhongMaterial(Color.RED));
    // mesh.setDrawMode(DrawMode.LINE);

    SmartGroup root = new SmartGroup();
    // root.getChildren().add(model);
    root.getChildren().add(modelFloor3);
    root.setRotationAxis(Rotate.Y_AXIS);

    this.group = root;

    this.group = root;

    PerspectiveCamera camera = new PerspectiveCamera();
    camera.setTranslateZ(-50);

    Scene scene = new Scene(root, 600, 400, true);
    scene.setFill(Color.SILVER);
    scene.setCamera(camera);
    return scene;

    //        camera.setTranslateX(WIDTH/2);
    //        camera.setTranslateY(HEIGHT/2);
    //        model.translateXProperty().set(WIDTH/2);
    //        model.translateYProperty().set(HEIGHT/2);
    // model.translateZProperty().set(-800);

    //        model.getChildren().add(model1);
    //
    //        SmartGroup root = new SmartGroup();
    //        root.getChildren().add(model);
    //        root.getChildren().add(model1);
    //        group = root;
    //
    //        Scene scene = new Scene(root, 800, 600);
    //        scene.setCamera(camera);
    //
    //        return scene;
  }

  private Group loadModelFloor1(URL url) {
    Group modelRoot = new Group();

    /*StlMeshImporter importer = new StlMeshImporter();
    importer.read(url);
    TriangleMesh result = importer.getImport();
    MeshView mesh = new MeshView(result);
    PhongMaterial material = new PhongMaterial();
    material.setDiffuseMap(
        new Image(
            getClass()
                .getClassLoader()
                .getResourceAsStream("edu/wpi/cs3733/D22/teamZ/images/floor1Base.png")));
    mesh.setMaterial(material);
    modelRoot.getChildren().add(mesh);*/

    ObjModelImporter importer = new ObjModelImporter();
    importer.read(url);

    String[] arrayURL = {
      "edu/wpi/cs3733/D22/teamZ/images/floor1Base.png",
      "edu/wpi/cs3733/D22/teamZ/images/towerMaterial.png",
      "edu/wpi/cs3733/D22/teamZ/images/towerMaterial.png",
      "edu/wpi/cs3733/D22/teamZ/images/towerMaterial.png",
      "edu/wpi/cs3733/D22/teamZ/images/towerMaterial.png",
      "edu/wpi/cs3733/D22/teamZ/images/towerBaseMaterial.png",
      "edu/wpi/cs3733/D22/teamZ/images/doorMaterial.png"
    };

    int i = 0;
    for (MeshView view : importer.getImport()) {
      PhongMaterial material = new PhongMaterial();
      material.setDiffuseMap(
          new Image(getClass().getClassLoader().getResourceAsStream(arrayURL[i])));
      view.setMaterial(material);
      modelRoot.getChildren().add(view);
      i++;
    }

    importer.close();

    return modelRoot;
  }

  private Group loadModelFloor3Up(URL url) {
    Group modelRoot = new Group();

    ObjModelImporter importer = new ObjModelImporter();
    importer.read(url);

    String[] arrayURL = {
      "edu/wpi/cs3733/D22/teamZ/images/baseTowerMaterial.png",
      "edu/wpi/cs3733/D22/teamZ/images/WingMatTower.png",
      "edu/wpi/cs3733/D22/teamZ/images/WingMatTower.png",
      "edu/wpi/cs3733/D22/teamZ/images/WingMatTower.png",
      "edu/wpi/cs3733/D22/teamZ/images/WingMatTower.png"
    };

    int i = 0;
    for (MeshView view : importer.getImport()) {
      PhongMaterial material = new PhongMaterial();
      material.setDiffuseMap(
          new Image(getClass().getClassLoader().getResourceAsStream(arrayURL[i])));
      view.setMaterial(material);
      modelRoot.getChildren().add(view);
      i++;
    }
    importer.close();
    return modelRoot;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    primaryStage.addEventHandler(
        KeyEvent.KEY_PRESSED,
        event -> {
          switch (event.getCode()) {
            case W:
              for (Node node : group.getChildren()) {
                if (node.getId() != null && node.getId().equals("floor1")) {
                  node.translateZProperty().set(node.getTranslateZ() - 10);
                }
              }
              group.translateZProperty().set(group.getTranslateZ() + 10);
              break;
            case S:
              group.translateZProperty().set(group.getTranslateZ() - 10);
              break;
            case Q:
              group.rotateByX(10);
              break;
            case E:
              group.rotateByX(-10);
              break;
            case Z:
              group.rotateByY(10);
              break;
            case X:
              group.rotateByY(-10);
              break;
          }
        });
    primaryStage.setResizable(false);
    primaryStage.setScene(createNewScene());
    primaryStage.show();
    prepareAnimation();
  }

  class SmartGroup extends Group {
    Rotate r;
    Transform t = new Rotate();

    public SmartGroup() {}

    public SmartGroup(Group group) {
      super(group);
    }

    void rotateByX(int ang) {
      r = new Rotate(ang, Rotate.X_AXIS);
      t = t.createConcatenation(r);
      this.getTransforms().clear();
      this.getTransforms().addAll(t);
    }

    void rotateByY(int ang) {
      r = new Rotate(ang, Rotate.Y_AXIS);
      t = t.createConcatenation(r);
      this.getTransforms().clear();
      this.getTransforms().addAll(t);
    }

    void rotateByZ(int ang) {
      r = new Rotate(ang, Rotate.Z_AXIS);
      t = t.createConcatenation(r);
      this.getTransforms().clear();
      this.getTransforms().addAll(t);
    }
  }

  private void prepareAnimation() {
    AnimationTimer timer =
        new AnimationTimer() {
          @Override
          public void handle(long l) {
            group.rotateProperty().set(group.getRotate() + 0.2);
          }
        };
    timer.start();
  }

  public static void main(String[] args) {
    /* TODO: Make decision about parsing command line arguments. */
    launch(args);
  }
}
