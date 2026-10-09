package via.sep2.ui.customer;

import dtos.auth.RegisterUserRequest;
import dtos.person.PersonDataDto;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import types.PersonType;
import utils.StringUtils;
import via.sep2.networking.authentication.AuthenticationClient;
import via.sep2.networking.people.PeopleClient;

/**
 * @author Mario
 */
public class AddCustomerVM
{
  private final StringProperty emailProp = new SimpleStringProperty();
  private final StringProperty firstNameProp = new SimpleStringProperty();
  private final StringProperty lastNameProp = new SimpleStringProperty();

  private final StringProperty messageProp = new SimpleStringProperty();
  private final BooleanProperty disableRegisterButtonProp = new SimpleBooleanProperty(
      true);
  private final PeopleClient peopleClient;

  public AddCustomerVM(PeopleClient peopleClient)
  {
    this.peopleClient = peopleClient;
    emailProp.addListener(this::updateRegisterButtonState);
    firstNameProp.addListener(this::updateRegisterButtonState);
    lastNameProp.addListener(this::updateRegisterButtonState);
  }

  public void registerUser()
  {
    messageProp.set(""); // clea potential existing message

    // validate all input is present
    if (emailProp.get() == null || emailProp.get().isEmpty())
    {
      messageProp.set("Email cannot be empty");
      return;
    }
    if (firstNameProp.get() == null || firstNameProp.get().isEmpty())
    {
      messageProp.set("First name cannot be empty");
      return;
    }
    if (lastNameProp.get() == null || lastNameProp.get().isEmpty())
    {
      messageProp.set("Last name cannot be empty");
      return;
    }
    try
    {
      peopleClient.create(new PersonDataDto(firstNameProp.get(),lastNameProp.get(),emailProp.get(),PersonType.CUSTOMER));

      messageProp.set("Success");
      // clear fields
      clearFields();
    }
    catch (Exception e)
    {
      // might receive exception from lower layer (i.e. client)
      messageProp.set(e.getMessage());
    }
  }

  private void clearFields()
  {
    emailProp.set("");
    firstNameProp.set("");
    lastNameProp.set("");
  }

  private void updateRegisterButtonState(Observable observable)
  {
    boolean shouldDisable =
        StringUtils.isNullOrEmpty(emailProp.get()) || StringUtils.isNullOrEmpty(firstNameProp.get())
            || StringUtils.isNullOrEmpty(lastNameProp.get());

    disableRegisterButtonProp.set(shouldDisable);
  }

  public StringProperty emailProperty()
  {
    return emailProp;
  }

  public StringProperty messageProperty()
  {
    return messageProp;
  }

  public BooleanProperty disableRegisterButtonProperty()
  {
    return disableRegisterButtonProp;
  }

  public StringProperty firstNameProperty()
  {
    return firstNameProp;
  }

  public StringProperty lastNameProperty()
  {
    return lastNameProp;
  }
}
