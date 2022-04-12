package edu.wpi.cs3733.D22.teamZ.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

// Controller for the FXML Layout
// https://stackoverflow.com/questions/51128717/javafx-how-to-display-image-captured-from-web-cam-in-imageview-in-another-scene
// ProfileTest.fxml
// ProfileTestA.fxml

public class AddParentController implements Initializable {

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  public void doAll() {
    ImageSelection profilePic = null;
    ImageSelection.getImageSelectionInstance()
        .imageProperty()
        .addListener((obs, oldImage, newImage) -> profilePic.setImage(newImage));
  }

  public void takePhoto() {
    try {
      Stage dialogStage = new Stage(StageStyle.UNDECORATED);
      BorderPane root = FXMLLoader.load(getClass().getResource("../views/WebCamPreview.fxml"));
      Scene scene = new Scene(root, 850, 390);
      dialogStage.setUserData("fromAddParent");
      dialogStage.initModality(Modality.APPLICATION_MODAL);
      dialogStage.setScene(scene);
      dialogStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
