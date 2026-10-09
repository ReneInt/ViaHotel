package via.sep2.ui.reservation;

import dtos.person.PersonDataDto;
import dtos.reservation.ReservationRoomTypeDto;
import dtos.time.DateDto;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import types.PersonType;
import types.ReservationType;
import types.RoomType;
import via.sep2.networking.reservation.ReservationClient;
import via.sep2.data.AppState;
import via.sep2.data.ClientData;
import via.sep2.startup.ViewHandler;
import via.sep2.ui.popup.MessageType;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rodrigo
 */
public class ReservationEmployeeVM implements PropertyChangeListener
{
  private final ReservationClient reservationClient;
  private PersonDataDto currentDto;
  private final StringProperty messageProp = new SimpleStringProperty();
  private final StringProperty errorLabel = new SimpleStringProperty("");
  private final StringProperty emailProp = new SimpleStringProperty();
  private final ObjectProperty<RoomType> roomTypeProp = new SimpleObjectProperty<>();
  private final StringProperty finalPriceProp = new SimpleStringProperty();
  private final ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>();
  private final ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>();
  private final ObjectProperty<ReservationType> reservationTypeProp = new SimpleObjectProperty<>();
  private final BooleanProperty disableUpdateButton = new SimpleBooleanProperty(
      true);
  BooleanProperty isSelected = new SimpleBooleanProperty();
  private final ObservableList<PersonDataDto> people = FXCollections.observableArrayList();

  public ReservationEmployeeVM(ReservationClient reservationClient)
  {
    isSelected.set(false);
    this.reservationClient = reservationClient;
    startDate.addListener(this::updateFinalPrice);
    endDate.addListener(this::updateFinalPrice);
    reservationTypeProp.addListener(this::updateFinalPrice);
    roomTypeProp.addListener(this::updateFinalPrice);
    startDate.addListener(this::updateReserveButtonState);
    endDate.addListener(this::updateReserveButtonState);
    reservationTypeProp.addListener(this::updateReserveButtonState);
    roomTypeProp.addListener(this::updateReserveButtonState);
    isSelected.addListener(this::updateReserveButtonState);
    ClientData.addPropertyChangeListener(this);
  }

  public void clearFields()
  {
    startDate.set(null);
    endDate.set(null);
    roomTypeProp.set(null);
    reservationTypeProp.set(null);
  }

  public StringProperty messageProperty()
  {
    return messageProp;
  }

  public ObjectProperty<LocalDate> startDateProperty()
  {
    return startDate;
  }

  public ObjectProperty<LocalDate> endDateProperty()
  {
    return endDate;
  }

  public ObjectProperty<RoomType> roomTypeProperty()
  {
    return roomTypeProp;
  }

  public StringProperty finalPricePropProperty()
  {
    return finalPriceProp;
  }

  private void updateFinalPrice(Observable observable)
  {
    if (startDate.get() != null && endDate.get() != null
        && reservationTypeProp.get() != null && roomTypeProp.get() != null)
    {
      DateDto start = new DateDto(startDate.get().getYear(),
          startDate.get().getMonthValue(), startDate.get().getDayOfMonth());
      DateDto end = new DateDto(endDate.get().getYear(),
          endDate.get().getMonthValue(), endDate.get().getDayOfMonth());
      ReservationRoomTypeDto dto = new ReservationRoomTypeDto(0,
          reservationTypeProp.get(), AppState.getCurrentUser().email(), start,
          end, roomTypeProp.get(), 0);
      try
      {
        double price = reservationClient.calculateFinalPrice(dto);
        finalPriceProp.set(String.valueOf(price));
      }
      catch (Exception e)
      {
        messageProp.set(e.getMessage());
      }
    }
    else
    {
      finalPriceProp.set("");
    }
  }

  public ObservableList<PersonDataDto> getPeople()
  {
    return people;
  }

  public PersonDataDto onPressedItem(PersonDataDto selectedItem)
  {
    currentDto = selectedItem;
    isSelected.set(true);
    return selectedItem;
  }

  public BooleanProperty enableReserveButtonProp()
  {
    return disableUpdateButton;
  }

  public ObjectProperty<ReservationType> reservationTypeProperty()
  {
    return reservationTypeProp;
  }

  private void updateReserveButtonState(Observable observable)
  {
    boolean disable = true;
    if (startDate.get() != null && endDate.get() != null)
    {
      if (LocalDate.now().isAfter(startDate.get()))
      {
        messageProp.set("Start Date can not be in the past");
      }
      else if (endDate.get().isAfter(startDate.get()))
      {
        messageProp.set("");
        disable =
            reservationTypeProp.get() == null || roomTypeProp.get() == null
                || !isSelected.getValue();
      }
    }
    disableUpdateButton.set(disable);
  }

  public void loadCustomers()
  {
    List<PersonDataDto> currentList = ClientData.getCustomers();
    try
    {
      if (currentList.isEmpty())
      {
        throw new RuntimeException("ERROR 404 DATA NOT FOUND");
      }
      people.setAll(currentList);
      messageProp.set(null);
    }
    catch (Exception e)
    {
      errorLabel.set(e.getMessage());
      ViewHandler.popupMessage(MessageType.WARNING, e.getMessage());
    }
  }

  public void onReserve()
  {
    messageProp.set(""); // clean potential existing message

    // validate all input is present
    if (startDate.get() == null)
    {
      messageProp.set("The start date cannot be empty");
      return;
    }
    if (endDate.get() == null)
    {
      messageProp.set("The end date cannot be empty");
      return;
    }
    if (startDate.get() == null || endDate.get() == null)
    {
      messageProp.set("Both dates cannot be empty, whore");
      return;
    }
    if (roomTypeProp.get() == null)
    {
      messageProp.set("Please select the type of room you want to stay in");
      return;
    }
    if (reservationTypeProp == null)
    {
      messageProp.set("Please select what reservation type you would like");
      return;
    }
    DateDto start = new DateDto(startDate.get().getYear(),
        startDate.get().getMonthValue(), startDate.get().getDayOfMonth());
    DateDto end = new DateDto(endDate.get().getYear(),
        endDate.get().getMonthValue(), endDate.get().getDayOfMonth());
    ReservationRoomTypeDto request = new ReservationRoomTypeDto(0,
        reservationTypeProp.get(), currentDto.email(), start, end,
        roomTypeProp.get(), 0);
    try
    {
      reservationClient.create(request);
      clearFields();
      messageProp.set("Success");
    }
    catch (Exception e)
    {
      messageProp.set(e.getMessage());
    }
  }

  public void searchReservationsForEmail()
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

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if (evt.getPropertyName().equals("peopleUpdate"))
    {
      Platform.runLater(() -> {
        Object newValue = evt.getNewValue();
        errorLabel.set("");
        if (newValue instanceof List)
        {
          try
          {
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

  public Property<String> errorProperty()
  {
    return errorLabel;
  }
}
