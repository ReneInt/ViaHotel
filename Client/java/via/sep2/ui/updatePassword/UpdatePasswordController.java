package via.sep2.ui.updatePassword;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import via.sep2.startup.ViewHandler;
import via.sep2.startup.ViewType;
import via.sep2.ui.common.Controller;
/**
 * @author Mario
 */
public class UpdatePasswordController implements Controller
{
  public Label messageLabel;
  public PasswordField oldPasswordInput;
  public PasswordField newPasswordInput;
  public PasswordField newPasswordInput2;
  public TextField emailInput;
  public Button buttonUpdatePassword;

    private final UpdatePasswordVM vm;

    public UpdatePasswordController(UpdatePasswordVM vm)
    {
      this.vm = vm;
    }

    public void initialize()
    {
      messageLabel.textProperty().bind(vm.messageProperty());
      emailInput.textProperty().bindBidirectional(vm.emailProperty());
      oldPasswordInput.textProperty().bindBidirectional(vm.oldPasswordProperty());
      newPasswordInput.textProperty().bindBidirectional(vm.newPasswordProperty());
      newPasswordInput2.textProperty().bindBidirectional(vm.newPasswordProperty2());
      buttonUpdatePassword.disableProperty().bind(vm.enableButtonUpdatePasswordProperty());
    }
    public void onLogin()
    {
      ViewHandler.showView(ViewType.LOGIN);
    }

    public void onUpdatePassword()
    {
      vm.updatePassword();
    }
}
