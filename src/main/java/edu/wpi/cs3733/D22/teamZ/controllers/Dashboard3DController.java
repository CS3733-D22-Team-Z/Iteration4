package edu.wpi.cs3733.D22.teamZ.controllers;

// import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import java.net.URL;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

public class Dashboard3DController extends Application {

  private static final double BASEW = 50;
  private static final double BASEH = 50;

  SmartGroup group;

  private Scene createNewScene() {
    /*Group model =
        loadModel(
            getClass()
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamZ/models/Scooter-smgrps.obj"));
    //    PhongMaterial pm = new PhongMaterial();
    //    pm.setDiffuseMap(new Image((new File("models/computer.png").toURI().toString())));
    //    pm.setSpecularColor(Color.WHITE);
    //    mesh.setMaterial(pm);
    // mesh.setMaterial(new PhongMaterial(Color.RED));
    // mesh.setDrawMode(DrawMode.LINE);
    PerspectiveCamera camera = new PerspectiveCamera(true);
    camera.getTransforms().addAll(new Rotate(-5, Rotate.Y_AXIS), new Translate(0, 0, -1));

    SmartGroup root = new SmartGroup();
    root.getChildren().add(model);
    root.setRotationAxis(Rotate.Y_AXIS);

    this.group = root;*/

    /* Use a SubScene */
    //    SubScene subScene = new SubScene(root, 600, 400);
    //    subScene.setFill(Color.SILVER);
    //    subScene.setCamera(camera);
    //    Group group = new Group();
    //    group.getChildren().add(subScene);

    SmartGroup root = new SmartGroup();
    Box base = new Box(50, 50, 10);
    Box base1 = new Box(50, 50, 10);
    Cylinder podA = new Cylinder(20, 10);
    Cylinder podB = new Cylinder(20, 10);
    Cylinder podC = new Cylinder(20, 10);
    Cylinder podD = new Cylinder(20, 10);
    base.setTranslateX(0);
    base.setTranslateY(0);
    podA.setTranslateX(-BASEW / 2);
    podA.setTranslateY(-BASEH / 2);
    podB.setTranslateX(-BASEW / 2);
    podB.setTranslateY(BASEH / 2);
    podC.setTranslateX(BASEW / 2);
    podC.setTranslateY(-BASEH / 2);
    podD.setTranslateX(BASEW / 2);
    podD.setTranslateY(BASEH / 2);

    podA.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podB.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podC.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
    podD.getTransforms().add(new Rotate(90, Rotate.X_AXIS));

    root.getChildren().add(base);
    root.getChildren().add(podA);
    root.getChildren().add(podB);
    root.getChildren().add(podC);
    root.getChildren().add(podD);
    root.getChildren().add(base1);

    this.group = root;

    PerspectiveCamera camera = new PerspectiveCamera(true);
    camera.setTranslateZ(-10);

    Scene scene = new Scene(root, 600, 400);
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

  private Group loadModel(URL url) {
    Group modelRoot = new Group();

    ObjModelImporter importer = new ObjModelImporter();
    importer.read(url);

    for (MeshView view : importer.getImport()) {
      modelRoot.getChildren().add(view);
    }

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
