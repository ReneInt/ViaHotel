package via.sep2.ui.updatePassword;

import dtos.auth.UpdatePasswordRequest;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utils.StringUtils;
import via.sep2.networking.authentication.AuthenticationClient;
import via.sep2.startup.ViewHandler;
import via.sep2.startup.ViewType;
/**
 * @author Mario
 */
public class UpdatePasswordVM
{
  private final StringProperty emailProp = new SimpleStringProperty();
  private final StringProperty newPasswordProp = new SimpleStringProperty();
  private final StringProperty newPasswordProp2 = new SimpleStringProperty();
  private final StringProperty oldPasswordProp = new SimpleStringProperty();
  private final StringProperty messageProp = new SimpleStringProperty();
  private final BooleanProperty disableUpdatePasswordButton = new SimpleBooleanProperty(
      true);

  private final AuthenticationClient authService;

  public UpdatePasswordVM(AuthenticationClient authService)
  {
    this.authService = authService;
    emailProp.addListener(this::updateUpdateButtonState);
    newPasswordProp.addListener(this::updateUpdateButtonState);
    newPasswordProp2.addListener(this::updateUpdateButtonState);
    oldPasswordProp.addListener(this::updateUpdateButtonState);
  }

  public StringProperty emailProperty()
  {
    return emailProp;
  }

  public StringProperty oldPasswordProperty()
  {
    return oldPasswordProp;
  }

  public StringProperty newPasswordProperty()
  {
    return newPasswordProp;
  }

  public StringProperty newPasswordProperty2()
  {
    return newPasswordProp2;
  }

  public StringProperty messageProperty()
  {
    return messageProp;
  }

  public BooleanProperty enableButtonUpdatePasswordProperty()
  {
    return disableUpdatePasswordButton;
  }

  private void updateUpdateButtonState(Observable observable)
  {
    boolean shouldDisable =
        StringUtils.isNullOrEmpty(emailProp.get()) || StringUtils.isNullOrEmpty(
            newPasswordProp.get()) || StringUtils.isNullOrEmpty(
            oldPasswordProp.get());

    disableUpdatePasswordButton.set(shouldDisable);
  }

  public void updatePassword()
  {
    if (newPasswordProp.get().equals(newPasswordProp2.get()))
    {
      messageProp.set("Password changed");
      UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest(
          emailProp.get(), oldPasswordProp.get(), newPasswordProp.get());
      try
      {
        authService.updatePassword(updatePasswordRequest);
        ViewHandler.showView(ViewType.LOGIN);
      }
      catch (Exception e)
      {
        messageProp.set(e.getMessage());
      }
    }
    else
    {
      messageProp.set("Passwords don't match cunt");
    }
  }
}
