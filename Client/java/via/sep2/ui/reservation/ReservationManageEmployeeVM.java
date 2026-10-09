package via.sep2.ui.reservation;
import dtos.reservation.ReservationRoomTypeDto;
import dtos.time.DateDto;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import types.ReservationType;
import types.RoomType;
import via.sep2.networking.reservation.ReservationClient;
import via.sep2.data.AppState;
import via.sep2.data.ClientData;
import via.sep2.utils.Utilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Mario
 */
public class ReservationManageEmployeeVM implements PropertyChangeListener
{
  private final ReservationClient reservationClient;
  BooleanProperty isSelected = new SimpleBooleanProperty();
  private final StringProperty messageProp = new SimpleStringProperty();
  private final ObjectProperty<RoomType> roomTypeProp = new SimpleObjectProperty<>();
  private final StringProperty finalPriceProp = new SimpleStringProperty();
  private final ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>();
  private final ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>();
  private ReservationRoomTypeDto currentDto;
  private final ObjectProperty<ReservationType> reservationTypeProp = new SimpleObjectProperty<>();
  private final BooleanProperty disableUpdateButton = new SimpleBooleanProperty(
      true);
  private final StringProperty emailProp = new SimpleStringProperty();
  private final ObservableList<ReservationRoomTypeDto> reservations = FXCollections.observableArrayList();
  private final BooleanProperty disableCancelButton=new SimpleBooleanProperty(true);
  public ReservationManageEmployeeVM(ReservationClient reservationClient)
  {
    isSelected.set(false);
    this.reservationClient = reservationClient;
    startDate.addListener(this::updateFinalPrice);
    endDate.addListener(this::updateFinalPrice);
    reservationTypeProp.addListener(this::updateFinalPrice);
    roomTypeProp.addListener(this::updateFinalPrice);
    startDate.addListener(this::updateUpdateButtonState);
    endDate.addListener(this::updateUpdateButtonState);
    reservationTypeProp.addListener(this::updateUpdateButtonState);
    roomTypeProp.addListener(this::updateUpdateButtonState);
    isSelected.addListener(this::updateUpdateButtonState);
    isSelected.addListener(this::enableCancelButtonProp);
    ClientData.addPropertyChangeListener(this);
  }

  public void loadReservations()
  {
    try
    {
      reservations.setAll(ClientData.getReservations());
      messageProp.set(null);
    }
    catch (Exception e)
    {
      messageProp.set(e.getMessage());
    }
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

  public ObservableList<ReservationRoomTypeDto> getReservations()
  {
    return reservations;
  }

  public ReservationRoomTypeDto onPressedItem(
      ReservationRoomTypeDto selectedItem)
  {
    currentDto = selectedItem;
    isSelected.set(true);
    startDate.set(Utilities.toLocalDate(currentDto.startDate()));
    endDate.set(Utilities.toLocalDate(currentDto.endDate()));
    roomTypeProp.set(currentDto.roomType());
    reservationTypeProp.set(currentDto.type());
    return selectedItem;
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

  public void clearFields()
  {
    startDate.set(null);
    endDate.set(null);
    roomTypeProp.set(null);
    reservationTypeProp.set(null);
  }

  public ObjectProperty<RoomType> roomTypeProperty()
  {
    return roomTypeProp;
  }

  public StringProperty finalPricePropProperty()
  {
    return finalPriceProp;
  }

  private void updateUpdateButtonState(Observable observable)
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
            reservationTypeProp.get() == null || roomTypeProp.get() == null||!isSelected.getValue();
      }
    }
    disableUpdateButton.set(disable);
  }

  public void onEdit()
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
      messageProp.set("Both dates cannot be empty");
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
    ReservationRoomTypeDto request = new ReservationRoomTypeDto(currentDto.id(),
        reservationTypeProp.get(), currentDto.email(), start, end,
        roomTypeProp.get(), 0);
    try
    {
      reservationClient.edit(request);
      clearFields();
      messageProp.set("Success");
    }
    catch (Exception e)
    {
      messageProp.set(e.getMessage());
    }
  }

  public BooleanProperty enableReserveButtonProp()
  {
    return disableUpdateButton;
  }

  public ObjectProperty<ReservationType> reservationTypeProperty()
  {
    return reservationTypeProp;
  }

  public void onCancel()
  {
    reservationClient.cancelReservation(currentDto);
  }
@Override
  public void propertyChange(PropertyChangeEvent evt)
  {
    if (evt.getPropertyName().equals("reservationUpdate"))
    {
      Platform.runLater(() -> {
        Object newValue = evt.getNewValue();
        if (newValue instanceof List)
        {
          try
          {
            List<ReservationRoomTypeDto> updatedReservations = (List<ReservationRoomTypeDto>) newValue;
            List<ReservationRoomTypeDto> userReservations = new ArrayList<>();
            for (ReservationRoomTypeDto reservation : updatedReservations)
            {
              if(emailProp.get()==null)
              {
                userReservations=updatedReservations;
              }
              else if (reservation.email().contains(emailProp.get()))
              {
                userReservations.add(reservation);
              }
            }
            reservations.setAll(userReservations);
            messageProp.set("");
          }
          catch (Exception e)
          {
            messageProp.set("Failed to update reservations: " + e.getMessage());
            reservations.clear();
          }
        }
        else
        {
          messageProp.set(
              "Failed to refresh reservations: Invalid data received");
          reservations.clear();
        }
      });
    }
  }
  public Property<String> emailProp()
  {
    return emailProp;
  }
  public void searchReservationsForEmail()
  {
    List<ReservationRoomTypeDto> tempList =ClientData.getReservations();
    List<ReservationRoomTypeDto> currentList = new ArrayList<>();
    for (ReservationRoomTypeDto temp : tempList)
    {
      if (emailProp==null||temp.email().contains(emailProp.get()))
      {
        currentList.add(temp);
      }
    }
    try
    {
      reservations.setAll(currentList);
      messageProp.set(null);
    }
    catch (Exception e)
    {
      messageProp.set(e.getMessage());
    }
  }
  private void enableCancelButtonProp(Observable observable)
  {
    disableCancelButton.set(!isSelected.get());
  }
  public BooleanProperty enableCancel()
  {
    return disableCancelButton;
  }
}
