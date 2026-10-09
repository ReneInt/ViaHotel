package via.sep2.ui.login;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import via.sep2.data.ClientData;
import via.sep2.startup.ViewHandler;
import via.sep2.startup.ViewType;
import via.sep2.ui.common.Controller;
/**
 * @author Troels, Mario, Rene
 */
public class LoginController implements Controller
{
    public Label messageLabel;
    public PasswordField passwordInput;
    public TextField emailInput;
    public Button buttonLogin;

    private final LoginVM vm;

    public LoginController(LoginVM vm)
    {
        this.vm = vm;
    }

    public void initialize()
    {
        messageLabel.textProperty().bind(vm.messageProperty());
        emailInput.textProperty().bindBidirectional(vm.emailProperty());
        passwordInput.textProperty().bindBidirectional(vm.passwordProperty());
        buttonLogin.disableProperty().bind(vm.enableLoginButtonProperty());
    }

    public void onRegister()
    {
        ViewHandler.showView(ViewType.REGISTER);
    }
    public void onUpdatePassword()
    {
        ViewHandler.showView(ViewType.UPDATE_PASSWORD);
    }
    public void onLogin()
    {
        vm.login();
    }
}
