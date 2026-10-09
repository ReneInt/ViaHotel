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
import types.ReservationType;
import types.RoomType;
import via.sep2.networking.reservation.ReservationClient;
import via.sep2.data.AppState;
import via.sep2.data.ClientData;
import via.sep2.startup.ViewHandler;
import via.sep2.ui.popup.MessageType;
import via.sep2.utils.Utilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Mario
 */
public class ReservationManageCustomerVM implements PropertyChangeListener
{
  private final StringProperty messageProp = new SimpleStringProperty();
  private final StringProperty errorLabel = new SimpleStringProperty("");
  private final ObjectProperty<RoomType> roomTypeProp = new SimpleObjectProperty<>();
  private final StringProperty finalPriceProp = new SimpleStringProperty();
  private final ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>();
  private final ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>();
  BooleanProperty isSelected = new SimpleBooleanProperty();
  private ReservationRoomTypeDto currentDto;
  private final ObjectProperty<ReservationType> reservationTypeProp = new SimpleObjectProperty<>();
  private final ReservationClient reservationClient;
  private final BooleanProperty disableUpdateButton = new SimpleBooleanProperty(true);
  private final BooleanProperty disableCancelButton = new SimpleBooleanProperty(true);
  private final ObservableList<ReservationRoomTypeDto> reservations = FXCollections.observableArrayList();

  public ReservationManageCustomerVM(ReservationClient reservationClient) {
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

  public void loadReservationsForCurrentUser() {
    PersonDataDto user = AppState.getCurrentUser();
    ArrayList<ReservationRoomTypeDto> currentList = new ArrayList<>();
    List<ReservationRoomTypeDto> templist=ClientData.getReservations();
    for (ReservationRoomTypeDto temp: templist)
    {
      if (temp.email().equals(user.email()))
      {
        currentList.add(temp);
      }

    }
    if (user == null) {
      messageProp.set("User is not authorised.");
      return;
    }
    String email = user.email();

    try {
      if (currentList.isEmpty())
      {
        throw new RuntimeException("ERROR 404 DATA NOT FOUND");
      }
      reservations.setAll(currentList);
      messageProp.set(null);
    } catch (Exception e) {
      errorLabel.set(e.getMessage());
      ViewHandler.popupMessage(MessageType.WARNING,e.getMessage());
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
          ReservationRoomTypeDto dto = new ReservationRoomTypeDto(
              0,
              reservationTypeProp.get(), AppState.getCurrentUser().email(), start,
              end, roomTypeProp.get(),0);
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
  public Double getPrice(ReservationRoomTypeDto payload)
  {
   return reservationClient.calculateFinalPrice(payload);
  }
  public ObservableList<ReservationRoomTypeDto> getReservations() {
    return reservations;
  }

  public ReservationRoomTypeDto onPressedItem(ReservationRoomTypeDto selectedItem)
  {
    currentDto=selectedItem;
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

  public ObjectProperty<RoomType>roomTypeProperty()
  {
    return roomTypeProp;
  }

  public StringProperty finalPricePropProperty()
  {
    return finalPriceProp;
  }
  private void updateUpdateButtonState(Observable observable)
  {
    boolean disable=true;
    if (startDate.get() != null && endDate.get() != null)
    {
      if(LocalDate.now().isAfter(startDate.get())){
        messageProp.set("Start Date can not be in the past");
      }
      else if (endDate.get().isAfter(startDate.get()))
      {
        messageProp.set("");
       disable  = reservationTypeProp.get() == null || roomTypeProp.get() == null||!isSelected.getValue();
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
        reservationTypeProp.get(), AppState.getCurrentUser().email(), start,
        end, roomTypeProp.get(),0);
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
    try
    {
      reservationClient.cancelReservation(currentDto);
    }
    catch (RuntimeException e)
    {
      messageProp.set(e.getMessage());
      ViewHandler.popupMessage(MessageType.WARNING,e.getMessage());
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals("reservationUpdate")) {
      Platform.runLater(() -> {
        PersonDataDto user = AppState.getCurrentUser();
        if (user == null) {
          messageProp.set("User is not authorized.");
          reservations.clear();
          return;
        }
        Object newValue = evt.getNewValue();
        if (newValue instanceof List) {
          try {
            errorLabel.set("");
            List<ReservationRoomTypeDto> updatedReservations = (List<ReservationRoomTypeDto>) newValue;
            List<ReservationRoomTypeDto> userReservations = new ArrayList<>();
            for (ReservationRoomTypeDto reservation : updatedReservations) {
              if (reservation.email().equals(user.email())) {
                userReservations.add(reservation);
              }
            }
            if (updatedReservations.isEmpty())
            {
              throw new RuntimeException("ERROR 404 DATA NOT FOUND");
            }
            reservations.setAll(userReservations);
            messageProp.set("");
          } catch (Exception e) {
            errorLabel.set(e.getMessage());
            ViewHandler.popupMessage(MessageType.WARNING,e.getMessage());
            messageProp.set("Failed to update reservations: " + e.getMessage());
            reservations.clear();
          }
        } else {
          errorLabel.set("ERROR 404 DATA NOT FOUND");
          ViewHandler.popupMessage(MessageType.WARNING,"ERROR 404 DATA NOT FOUND");
          messageProp.set("Failed to refresh reservations: Invalid data received");
          reservations.clear();
        }
      });
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

  public StringProperty errorProperty()
  {
    return errorLabel;
  }
}
