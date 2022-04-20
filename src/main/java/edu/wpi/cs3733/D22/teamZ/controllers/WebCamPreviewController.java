package edu.wpi.cs3733.D22.teamZ.controllers;

import com.github.sarxos.webcam.Webcam;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

/**
 * This was the controller for WebCamPreview FXML, repurposed for CCTVPreview FXML
 * and CS3733 Iteration 3 Application
 *
 * Controller based on the JavaFX and FXML example for Sarxos's Webcam Capture API by Rakesh Bhatt (rakeshbhatt10)
 * Webcam Capture API
 * https://github.com/sarxos/webcam-capture
 * JavaFX Example
 * https://github.com/sarxos/webcam-capture/tree/master/webcam-capture-examples/webcam-capture-javafx-fxml
 */
public class WebCamPreviewController implements Initializable, IMenuAccess {

  @FXML Button btnStartCamera;

  @FXML Button btnStopCamera;

  @FXML Button btnDisposeCamera;

  @FXML ComboBox<WebCamInfo> cbCameraOptions;

  @FXML BorderPane bpWebCamPaneHolder;

  @FXML FlowPane fpBottomPane;

  @FXML ImageView imgWebCamCapturedImage;

  @FXML ToggleButton displayFPSToggleButton;
  @FXML Label indicatorFPS;

  private MenuController menu;

  @Override
  public void setMenuController(MenuController menu) {
    this.menu = menu;
  }

  @Override
  public String getMenuName() {
    return "CCTV";
  }

  private class WebCamInfo {

    private String webCamName;
    private int webCamIndex;
    //    private int webCamFPS;

    public String getWebCamName() {
      return webCamName;
    }

    public void setWebCamName(String webCamName) {
      this.webCamName = webCamName;
    }

    public int getWebCamIndex() {
      return webCamIndex;
    }

    public void setWebCamIndex(int webCamIndex) {
      this.webCamIndex = webCamIndex;
    }

    //    public int getWebCamFPS() {return webCamFPS;}

    @Override
    public String toString() {
      return webCamName;
    }
  }

  private BufferedImage grabbedImage;
  private Webcam selWebCam = null;
  private boolean stopCamera = false;
  private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();

  private String cameraListPromptText = "Choose Camera";

  @Override
  public void initialize(URL arg0, ResourceBundle arg1) {

    // For FPS, hide for now
    indicatorFPS.setText("");
    indicatorFPS.setVisible(false);
    displayFPSToggleButton.setDisable(true);
    displayFPSToggleButton.setVisible(false);

    fpBottomPane.setDisable(true);
    ObservableList<WebCamInfo> options = FXCollections.observableArrayList();
    int webCamCounter = 0;
    for (Webcam webcam : Webcam.getWebcams()) {
      WebCamInfo webCamInfo = new WebCamInfo();
      webCamInfo.setWebCamIndex(webCamCounter);
      webCamInfo.setWebCamName(webcam.getName());
      options.add(webCamInfo);
      webCamCounter++;
    }
    cbCameraOptions.setItems(options);
    cbCameraOptions.setPromptText(cameraListPromptText);
    cbCameraOptions
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            new ChangeListener<WebCamInfo>() {

              @Override
              public void changed(
                  ObservableValue<? extends WebCamInfo> arg0, WebCamInfo arg1, WebCamInfo arg2) {
                if (arg2 != null) {
                  System.out.println(
                      "WebCam Index: "
                          + arg2.getWebCamIndex()
                          + ": WebCam Name:"
                          + arg2.getWebCamName());
                  initializeWebCam(arg2.getWebCamIndex());
                }
              }
            });
    Platform.runLater(
        new Runnable() {

          @Override
          public void run() {
            setImageViewSize();
          }
        });
  }

  protected void setImageViewSize() {

    double height = bpWebCamPaneHolder.getHeight();
    double width = bpWebCamPaneHolder.getWidth();
    imgWebCamCapturedImage.setFitHeight(height);
    imgWebCamCapturedImage.setFitWidth(width);
    imgWebCamCapturedImage.prefHeight(height);
    imgWebCamCapturedImage.prefWidth(width);
    imgWebCamCapturedImage.setPreserveRatio(true);
  }

  protected void initializeWebCam(final int webCamIndex) {

    Task<Void> webCamIntilizer =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {

            if (selWebCam == null) {
              selWebCam = Webcam.getWebcams().get(webCamIndex);
              selWebCam.open();
            } else {
              closeCamera();
              selWebCam = Webcam.getWebcams().get(webCamIndex);
              selWebCam.open();
            }
            startWebCamStream();
            return null;
          }
        };

    new Thread(webCamIntilizer).start();
    fpBottomPane.setDisable(false);
    btnStartCamera.setDisable(true);
  }

  protected void startWebCamStream() {
    //    long[] t1 = new long[1]; // for FPS
    //    long[] t2 = new long[1]; // for FPS

    //    int p = 10; // for FPS
    //    int r = 5; // for FPS

    stopCamera = false;
    Task<Void> task =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {
            double prevFPS = 0; // for FPS

            while (!stopCamera) {
              try {
                if ((grabbedImage = selWebCam.getImage()) != null) {

                  Platform.runLater(
                      new Runnable() {

                        @Override
                        public void run() {
                          final Image mainImage = SwingFXUtils.toFXImage(grabbedImage, null);
                          imageProperty.set(mainImage);
                        }
                      });

                  grabbedImage.flush();

                  //                  if (displayFPSToggleButton.isSelected()) {
                  //                    double FPS = selWebCam.getFPS();
                  //
                  //                    String textFPS = "FPS: " + FPS;
                  //                    System.out.println(textFPS);
                  //                    if (prevFPS != FPS) {
                  //                      indicatorFPS.setText(textFPS);
                  //                    } else {
                  //                      prevFPS = FPS;
                  //                    }
                  //                  }

                  // New for FPS
                  //                  if (displayFPSToggleButton.isSelected()) {
                  //                    for (int k = 0; k < p; k++) {
                  //
                  //                      selWebCam.open();
                  //                      selWebCam.getImage();
                  //
                  //                      t1[0] = System.currentTimeMillis();
                  //                      for (int i = 0; ++i <= r; selWebCam.getImage()) {}
                  //                      t2[0] = System.currentTimeMillis();
                  //
                  //                      String textFPS = "FPS: " + (1000 * r / (t2[0] - t1[0] +
                  // 1));
                  //                      System.out.println(textFPS);
                  //                      //                      indicatorFPS.setText(textFPS);
                  //                      //                      selWebCam.close();
                  //                    }
                  //                  } // end of New for FPS
                }
              } catch (Exception e) {
                e.printStackTrace();
              }
            }

            return null;
          }
        };
    Thread th = new Thread(task);
    th.setDaemon(true);
    th.start();
    imgWebCamCapturedImage.imageProperty().bind(imageProperty);
  }

  private void closeCamera() {
    if (selWebCam != null) {
      selWebCam.close();
    }
  }

  public void stopCamera(ActionEvent event) {
    stopCamera = true;
    btnStartCamera.setDisable(false);
    btnStopCamera.setDisable(true);
  }

  public void startCamera(ActionEvent event) {
    stopCamera = false;
    startWebCamStream();
    btnStartCamera.setDisable(true);
    btnStopCamera.setDisable(false);
  }

  public void disposeCamera(ActionEvent event) {
    stopCamera = true;
    closeCamera();
    btnStopCamera.setDisable(true);
    btnStartCamera.setDisable(true);
  }
}
