package via.sep2.ui.login;

import dtos.auth.LoginRequest;
import dtos.person.PersonDataDto;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utils.StringUtils;
import via.sep2.networking.authentication.AuthenticationClient;
import via.sep2.startup.ViewHandler;
import via.sep2.startup.ViewType;
import via.sep2.data.AppState;
/**
 * @author Troels, Mario
 */
public class LoginVM
{
    public final StringProperty emailProp = new SimpleStringProperty();
    public final StringProperty passwordProp = new SimpleStringProperty();
    public final StringProperty messageProp = new SimpleStringProperty();
    public final BooleanProperty disableLoginButtonProp = new SimpleBooleanProperty(true);

    private final AuthenticationClient authService;

    public LoginVM(AuthenticationClient authService)
    {
        this.authService = authService;
        emailProp.addListener(this::updateLoginButtonState);
        passwordProp.addListener(this::updateLoginButtonState);
    }

    public StringProperty emailProperty()
    {
        return emailProp;
    }

    public StringProperty passwordProperty()
    {
        return passwordProp;
    }

    public StringProperty messageProperty()
    {
        return messageProp;
    }

    public BooleanProperty enableLoginButtonProperty()
    {
        return disableLoginButtonProp;
    }

    private void updateLoginButtonState(Observable observable)
    {
        boolean shouldDisable =
                StringUtils.isNullOrEmpty(emailProp.get()) ||
                        StringUtils.isNullOrEmpty(passwordProp.get());

        disableLoginButtonProp.set(shouldDisable);
    }

    public void login()
    {
        messageProp.set("You are logged in");
        LoginRequest loginRequest = new LoginRequest(emailProp.get(), passwordProp.get());
        try
        {
            PersonDataDto user = authService.login(loginRequest);
            AppState.setCurrentUser(user);
            switch (user.position())
            {
                case CUSTOMER ->  ViewHandler.showView(ViewType.OVERVIEW_CUSTOMER);
                case EMPLOYEE ->  ViewHandler.showView(ViewType.OVERVIEW_EMPLOYEE);
                case MANAGER ->  ViewHandler.showView(ViewType.OVERVIEW_MANAGER);

            }
        }
        catch (Exception e)
        {
            messageProp.set(e.getMessage());
        }
    }
}
