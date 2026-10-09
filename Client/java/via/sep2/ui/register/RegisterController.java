package via.sep2.ui.register;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import via.sep2.startup.ViewHandler;
import via.sep2.startup.ViewType;
import via.sep2.ui.common.Controller;
/**
 * @author Troels, Mario, Rene
 */
public class RegisterController implements Controller
{
  @FXML
  public TextField emailInput;
  @FXML
  public PasswordField passwordInput;
  @FXML
  public PasswordField repeatPasswordInput;
  @FXML
  public TextField firstNameInput;
  @FXML
  public TextField lastNameInput;
  @FXML
  public Label messageLabel;
  @FXML
  public Button buttonRegister;

  private final RegisterVM viewModel;

  public RegisterController(RegisterVM vm)
  {
    this.viewModel = vm;
  }

  public void initialize()
  {
    emailInput.textProperty().bindBidirectional(viewModel.emailProperty());
    passwordInput.textProperty().bindBidirectional(viewModel.passwordProperty());
    repeatPasswordInput.textProperty().bindBidirectional(viewModel.repeatProperty());
    firstNameInput.textProperty().bindBidirectional(viewModel.firstNameProperty());
    lastNameInput.textProperty().bindBidirectional(viewModel.lastNameProperty());

    messageLabel.textProperty().bind(viewModel.messageProperty());

    buttonRegister.disableProperty().bind(viewModel.disableRegisterButtonProperty());
  }

  public void onRegister()
  {
    viewModel.registerUser();
  }

  public void onLogin()
  {
    ViewHandler.showView(ViewType.LOGIN);
  }

}
