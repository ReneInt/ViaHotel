package via.sep2.ui.popup;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import via.sep2.ui.common.Controller;
import via.sep2.ui.popup.MessageType;

public class PopupController implements Controller
{
  private final Stage stage;
  private final MessageType type;
  private final String message;
  public Button closeButton;
  public Label messageLabel;
  public Label errorLabel;
  public Label warningLabel;
  public Label successLabel;
  public Label notificationLabel;

  public PopupController(Stage stage, MessageType type, String message)
  {
    this.stage = stage;
    this.type = type;
    this.message = message;
  }

  public void initialize()
  {
    messageLabel.setText(message);
    notificationLabel.setVisible(false);
    errorLabel.setVisible(false);
    warningLabel.setVisible(false);
    successLabel.setVisible(false);

    switch (type)
    {
      case ERROR -> {errorLabel.setVisible(true);
      closeButton.setText("Close application");}
      case SUCCESS -> successLabel.setVisible(true);
      case WARNING -> warningLabel.setVisible(true);
      case NOTIFICATION -> notificationLabel.setVisible(true);
    }
  }

  public void onClose()
  {
    switch (type){
      case ERROR -> Platform.exit();
      case null, default -> stage.close();
    }
  }
}