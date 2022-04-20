package edu.wpi.cs3733.D22.teamZ.controllers;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;
import edu.wpi.cs3733.D22.teamZ.entity.Employee;
import edu.wpi.cs3733.D22.teamZ.nfcCardReader.NFCCardReaderController;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javax.smartcardio.CardException;

public class LoginPageController implements Initializable {
  @FXML private TextField usernameField;
  @FXML private TextField passwordField;
  @FXML private Label errorLabel;
  @FXML private MFXButton loginButton;
  @FXML private AnchorPane loginPane;

  public ObservableList<Transform> initialStates;
  public double initialHeight;
  public double initialWidth;
  public double initialRatio;
  private ChangeListener<? super Number> sizeChangeListener;

  private FacadeDAO facadeDAO;
  private String toHomepageURL = "edu/wpi/cs3733/D22/teamZ/views/Menu.fxml";

  /**
   * Initalizes the employee database for the controller
   *
   * @param location
   * @param resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    facadeDAO = FacadeDAO.getInstance();
  }

  /**
   * Reads information from the username and password fields, and determines whether it is a valid
   * login or not. If so, it loads the success screen. If not, it switches to the error state.
   */
  @FXML
  private void loginButtonPressed(ActionEvent event) {
    Employee tryLog = facadeDAO.getEmployeeByUsername(usernameField.getText());

    if (tryLog == null || tryLog.getName() == null || tryLog.getName().equals("")) {
      errorLabel.setText("Invalid username. Try again.");
      enterErrorState();
    } else { // theoretically valid username
      if (tryLog
          .getPassword()
          .equals(passwordField.getText())) { // edit this line for hashcode eventually

        try {
          MenuController.setLoggedInUser(tryLog);
          loadSuccessScreen(usernameField.getText(), event);
        } catch (IOException e) {
          e.printStackTrace();
        }

      } else {
        errorLabel.setText("Invalid password for this username. Try again.");
        enterErrorState();
      }
    }
  }

  /**
   * binds loginButton to enter key
   *
   * @param event
   */
  @FXML
  private void enterButtonPressed(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      loginButton.fire();
    }
  }

  public void enterErrorState() {
    usernameField.setStyle("-fx-border-color: #FF4343");
    passwordField.setStyle("-fx-border-color: #FF4343");
    errorLabel.setVisible(true);
  }

  public void enterNormalState() {
    usernameField.setStyle("-fx-border-color: #0075ff");
    passwordField.setStyle("-fx-border-color: #0075ff");
    errorLabel.setVisible(false);
  }

  public void loadSuccessScreen(String username, ActionEvent event) throws IOException {
    // Load the default FXML file and set that scene to the main stage.
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getClassLoader().getResource(toHomepageURL));
    Parent root = loader.load();
    Scene scene = new Scene(root);
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    primaryStage.setScene(scene);
    primaryStage.minHeightProperty().unbind();
    primaryStage.maxHeightProperty().unbind();
    primaryStage.setMinHeight(475); // initial size. doesnt work if less so ignore lol.
    // TODO was 292^
    primaryStage.setMinWidth(745);
    // TODO fix scaling on other login pages after logout

    initialHeight = primaryStage.getHeight();
    initialWidth = primaryStage.getWidth();
    initialRatio = initialHeight / initialWidth;

    primaryStage.minHeightProperty().bind(primaryStage.widthProperty().multiply(initialRatio));
    primaryStage.maxHeightProperty().bind(primaryStage.widthProperty().multiply(initialRatio));

    sizeChangeListener =
        (ChangeListener<Number>)
            (observable, oldValue, newValue) -> {
              onSizeChange(root, primaryStage);
            };

    primaryStage.heightProperty().addListener(sizeChangeListener);
    primaryStage.widthProperty().addListener(sizeChangeListener);
  }
  // thisController.setWelcomeMessage(username);

  public void onSizeChange(Parent root, Stage primaryStage) {
    // System.out.println("old:" + oldValue + " new:" + newValue);
    float scaleY = (float) (primaryStage.getHeight() / initialHeight);
    float scaleX = (float) (primaryStage.getWidth() / initialWidth);
    if (initialStates == null) {
      initialStates = root.getTransforms();
    }
    root.getTransforms().setAll(initialStates);

    root.getTransforms().add(new Scale(scaleY, scaleY, 0, 0));
  }

  @FXML
  public void readCard(ActionEvent evt) {
    NFCCardReaderController obj = new NFCCardReaderController();
    try {
      obj.initialize();
    } catch (CardException e) {
      e.printStackTrace();
    }
    obj.setUsername();
    obj.setPassword();

    // System.out.println("User:" + obj.getUsername() + " Pass: " + obj.getPassword());
    Employee tryLog = facadeDAO.getEmployeeByUsername(obj.getUsername());

    if (tryLog == null || tryLog.getName() == null || tryLog.getName().equals("")) {
      errorLabel.setText("Invalid username. Try again.");
      enterErrorState();
    } else { // theoretically valid username
      if (tryLog
          .getPassword()
          .equals(obj.getPassword())) { // edit this line for hashcode eventually

        try {
          MenuController.setLoggedInUser(tryLog);
          loadSuccessScreen(obj.getUsername(), evt);
        } catch (IOException e) {
          e.printStackTrace();
        }

      } else {
        errorLabel.setText("Invalid password for this username. Try again.");
        enterErrorState();
      }
    }
  }
}
