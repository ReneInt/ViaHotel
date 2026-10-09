package via.sep2.ui.dining;

import dtos.dining.DiningRequestDto;
import dtos.person.PersonDataDto;
import dtos.time.DateDto;
import dtos.time.HourDto;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import types.HourValues;
import types.PersonType;
import via.sep2.networking.dining.DiningClient;
import via.sep2.data.ClientData;
import via.sep2.startup.ViewHandler;
import via.sep2.ui.popup.MessageType;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mario, Rodrigo
 */
public class DiningStaffVM implements PropertyChangeListener
{
  private final StringProperty emailProp = new SimpleStringProperty();
  private final DiningClient diningClient;
  private final StringProperty messageProp = new SimpleStringProperty();
  private final StringProperty errorLabel = new SimpleStringProperty("");
  private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
  BooleanProperty isSelected = new SimpleBooleanProperty();
  private PersonDataDto currentDto;
  private final ObjectProperty<HourValues> hourValuesProp = new SimpleObjectProperty<>();
  private final BooleanProperty disableReserveButton = new SimpleBooleanProperty(
      true);
  private final BooleanProperty disableCancelButton = new SimpleBooleanProperty(
      true);
  private final ObservableList<PersonDataDto> people = FXCollections.observableArrayList();

  public DiningStaffVM(DiningClient diningClient)
  {
    isSelected.set(false);
    this.diningClient = diningClient;
    date.addListener(this::updateDiningButtonState);
    isSelected.addListener(this::updateDiningButtonState);
    isSelected.addListener(this::enableCancelButtonProp);
    ClientData.addPropertyChangeListener(this);
  }

  public void enableCancelButtonProp(Observable observable)
  {
    disableCancelButton.set(!isSelected.get());
  }

  public StringProperty messageProperty()
  {
    return messageProp;
  }

  public BooleanProperty enableReserveButtonProp()
  {
    return disableReserveButton;
  }

  public ObjectProperty<HourValues> hourValuesProperty()
  {
    return hourValuesProp;
  }

  public ObjectProperty<LocalDate> dateProperty()
  {
    return date;
  }

  public ObservableList<PersonDataDto> getPeople()
  {
    return people;
  }

  public void searchDiningForEmail()
  {
    ArrayList<PersonDataDto> currentList = new ArrayList<>();
    List<PersonDataDto> templist = ClientData.getCustomers();
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

  private void updateDiningButtonState(Observable observable)
  {
    boolean disable = true;
    if (currentDto != null)
    {
      if (date.get() != null)
      {
        if (LocalDate.now().isAfter(date.get()))
        {
          messageProp.set("Dining can not be made in the past");
        }
        else if (date.get() != null)
        {
          messageProp.set("");
          {
            disable = hourValuesProp.get() == null || !isSelected.getValue();
          }
        }
      }
      disableReserveButton.set(disable);
    }
  }

  public void clearFields()
  {
    date.set(null);
    hourValuesProp.set(null);
  }

  public PersonDataDto onPressedItem(PersonDataDto selectedItem)
  {
    currentDto = selectedItem;
    isSelected.set(true);
    return selectedItem;
  }

  public void onReserve()
  {
    messageProp.set(""); // clean potential existing message

    // validate all input is present
    if (date.get() == null)
    {
      messageProp.set("The date cannot be empty");
      return;
    }
    if (hourValuesProp.get() == null)
    {
      messageProp.set("Please select what hour you would like to go in");
      return;
    }
    DateDto diningDate = new DateDto(date.get().getYear(),
        date.get().getMonthValue(), date.get().getDayOfMonth());
    DiningRequestDto request = new DiningRequestDto(0, diningDate,
        new HourDto(hourValuesProp.get().getHour(),
            hourValuesProp.get().getMinute()), currentDto.email());
    try
    {
      diningClient.create(request);
      clearFields();
      messageProp.set("Success");
    }
    catch (Exception e)
    {
      messageProp.set(e.getMessage());
    }
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
            List<PersonDataDto> customers = new ArrayList<>();
            for (PersonDataDto person : updatedPeople)
            {
              if (person.position().equals(PersonType.CUSTOMER))
                customers.add(person);
            }
            people.setAll(customers);
            if (customers.isEmpty())
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

  public Property<String> emailProp()
  {
    return emailProp;
  }

  public void loadPeople()
  {
    try
    {

      if (ClientData.getCustomers().isEmpty())
      {
        throw new RuntimeException("ERROR 404 DATA NOT FOUND");
      }
      people.setAll(ClientData.getCustomers());
      messageProp.set(null);
    }
    catch (Exception e)
    {
      errorLabel.set(e.getMessage());
      ViewHandler.popupMessage(MessageType.WARNING, e.getMessage());
    }
  }

  public BooleanProperty enableCancel()
  {
    return disableCancelButton;
  }

  public StringProperty errorProperty()
  {
    return errorLabel;
  }
}