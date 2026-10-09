package via.sep2.ui.employees;

import dtos.person.PersonDataDto;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import types.PersonType;
import utils.StringUtils;
import via.sep2.data.ClientData;
import via.sep2.networking.people.PeopleClient;
import via.sep2.startup.ViewHandler;
import via.sep2.ui.popup.MessageType;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mario
 */
public class ManageEmployeeVM implements PropertyChangeListener
{
  private final ObjectProperty<PersonType> personTypeProp=new SimpleObjectProperty<>();
  private final StringProperty emailProp = new SimpleStringProperty();
  private final StringProperty firstNameProp = new SimpleStringProperty();
  private final StringProperty lastNameProp = new SimpleStringProperty();
  private final StringProperty errorLabel = new SimpleStringProperty("");
  private final ObservableList<PersonDataDto> people = FXCollections.observableArrayList();
  private final StringProperty messageProp = new SimpleStringProperty();
  private final BooleanProperty disableRegisterButtonProp = new SimpleBooleanProperty(
      true);
  private final BooleanProperty disableCancelButton = new SimpleBooleanProperty(
      true);
  private final BooleanProperty isSelected = new SimpleBooleanProperty();
  private PersonDataDto currentDto;
  private final PeopleClient peopleClient;
  public ObjectProperty<PersonType> personTypePropProperty()
  {
    return personTypeProp;
  }
  public ManageEmployeeVM(PeopleClient peopleClient)
  {
    isSelected.set(false);
    this.peopleClient = peopleClient;
    firstNameProp.addListener(this::updateUpdateButtonState);
    lastNameProp.addListener(this::updateUpdateButtonState);
    isSelected.addListener(this::updateUpdateButtonState);
    isSelected.addListener(this::enableCancelButtonProp);
    ClientData.addPropertyChangeListener(this);
  }
  public void enableCancelButtonProp(Observable observable)
  {
    disableCancelButton.set(!isSelected.get());
  }
  public void loadPeople()
  {
    try
    {
      if (ClientData.getEmployees().isEmpty())
      {
        throw new RuntimeException("ERROR 404 DATA NOT FOUND");
      }
      people.setAll(ClientData.getEmployees());
      messageProp.set(null);
    }
    catch (Exception e)
    {
      errorLabel.set(e.getMessage());
      ViewHandler.popupMessage(MessageType.WARNING, e.getMessage());
    }
  }
  public void searchPeopleForEmail()
  {
    List<PersonDataDto> currentList = new ArrayList<>();
    List<PersonDataDto> templist = ClientData.getEmployees();
    for (PersonDataDto temp : templist)
    {
      if (temp.email().contains(emailProp.get()))
      {
        currentList.add(temp);
      }
    }
    try
    {
      people.setAll(currentList);
      messageProp.set(null);
    }
    catch (Exception e)
    {
      messageProp.set(e.getMessage());
    }
  }

  public void onUpdate()
  {
    messageProp.set(""); // clea potential existing message

    // validate all input is present
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
      peopleClient.update(new PersonDataDto(firstNameProp.get(),lastNameProp.get(),currentDto.email(),personTypeProp.get()));

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
  public void onDelete()
  {
    try
    {
      peopleClient.delete(new PersonDataDto(null,null,currentDto.email(),null));
    }
    catch (Exception e)
    {
      ViewHandler.popupMessage(MessageType.WARNING,"Cannot delete customer because a dining/reservation is made on their email");
      messageProp.set("Cannot delete customer because a dining/reservation is made on their email");
    }
  }

  private void clearFields()
  {
    emailProp.set("");
    firstNameProp.set("");
    lastNameProp.set("");
  }

  private void updateUpdateButtonState(Observable observable)
  {
    boolean shouldDisable =
        StringUtils.isNullOrEmpty(firstNameProp.get())
            || StringUtils.isNullOrEmpty(lastNameProp.get()) || !isSelected.get();

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

  public BooleanProperty disableUpdateButtonProperty()
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

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if (evt.getPropertyName().equals("peopleUpdate"))
    {
      Platform.runLater(() -> {
        Object newValue = evt.getNewValue();
        if (newValue instanceof List)
        {
          try
          {
            errorLabel.set("");
            List<PersonDataDto> updatedPeople = (List<PersonDataDto>) newValue;
            List<PersonDataDto> employees = new ArrayList<>();
            for (PersonDataDto person : updatedPeople)
            {
              if (!person.position().equals(PersonType.CUSTOMER))
                employees.add(person);
            }
            people.setAll(employees);
            if (employees.isEmpty())
            {
              throw new RuntimeException("ERROR 404, DATA NOT FOUND");
            }
            messageProp.set("");
          }
          catch (Exception e)
          {
            ViewHandler.popupMessage(MessageType.WARNING,
                "Failed to update customers: " + e.getMessage());
            messageProp.set("Failed to update customers: " + e.getMessage());
            errorLabel.set("ERROR 404 DATA NOT FOUND");
            people.clear();
          }
        }
        else
        {
          ViewHandler.popupMessage(MessageType.WARNING,
              "Failed to refresh reservations: Invalid data received");
          errorLabel.set("ERROR 404 DATA NOT FOUND");
          people.clear();
        }
      });
    }
  }
  public PersonDataDto onPressedItem(PersonDataDto selectedItem)
  {
    currentDto = selectedItem;
    isSelected.set(true);
    firstNameProp.set(currentDto.firstName());
    lastNameProp.set(currentDto.lastName());
    personTypeProp.set(currentDto.position());
    return selectedItem;
  }

  public ObservableList<PersonDataDto> getPeople()
  {
    return people;
  }

  public StringProperty errorProperty()
  {
    return errorLabel;
  }
  public BooleanProperty disableCancelProperty()
  {
    return disableCancelButton;
  }
}
