package via.sep2.ui.reservation;

import dtos.reservation.ReservationRoomTypeDto;
import dtos.time.DateDto;
import javafx.beans.Observable;
import javafx.beans.property.*;
import types.ReservationType;
import types.RoomType;
import via.sep2.networking.reservation.ReservationClient;
import via.sep2.data.AppState;

import java.time.LocalDate;
/**
 * @author Mario, Rodrigo
 */
public class ReservationCustomerVM
{
  private final StringProperty messageProp = new SimpleStringProperty();
  private final StringProperty roomTypeProp = new SimpleStringProperty();
  private final StringProperty finalPriceProp = new SimpleStringProperty();
  private final ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>();
  private final ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>();
  private RoomType roomType;
  private final ObjectProperty<ReservationType> reservationTypeProp = new SimpleObjectProperty<>();
  private final ReservationClient reservationClient;
  private final BooleanProperty disableReserveButton = new SimpleBooleanProperty(true);

  public ReservationCustomerVM(ReservationClient reservationClient)
  {
    this.reservationClient = reservationClient;
    startDate.addListener(this::updateFinalPrice);
    endDate.addListener(this::updateFinalPrice);
    reservationTypeProp.addListener(this::updateFinalPrice);
    roomTypeProp.addListener(this::updateFinalPrice);
    startDate.addListener(this::updateReserveButtonState);
    endDate.addListener(this::updateReserveButtonState);
    reservationTypeProp.addListener(this::updateReserveButtonState);
    roomTypeProp.addListener(this::updateReserveButtonState);
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
      messageProp.set("Both dates cannot be empty");
      return;
    }
    if (roomType == null)
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
        reservationTypeProp.get(), AppState.getCurrentUser().email(), start,
        end, roomType,0);
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

  public void clearFields()
  {
    startDate.set(null);
    endDate.set(null);
    roomTypeProp.set("");
    reservationTypeProp.set(null);
  }

  public StringProperty roomTypeProperty()
  {
    if (roomType == null)
    {
      roomTypeProp.set("");
    }
    else
    {
      switch (roomType)
      {
        case SMALL -> roomTypeProp.set("Small");
        case KING -> roomTypeProp.set("King Size");
        case FAMILY -> roomTypeProp.set("Family");
      }
    }
    return roomTypeProp;
  }

  public StringProperty finalPricePropProperty()
  {
    return finalPriceProp;
  }

  private void updateFinalPrice(Observable observable)
  {
    if (startDate.get() != null && endDate.get() != null
        && reservationTypeProp.get() != null && roomType != null)
    {
      DateDto start = new DateDto(startDate.get().getYear(),
          startDate.get().getMonthValue(), startDate.get().getDayOfMonth());
      DateDto end = new DateDto(endDate.get().getYear(),
          endDate.get().getMonthValue(), endDate.get().getDayOfMonth());
      ReservationRoomTypeDto dto = new ReservationRoomTypeDto(0,
          reservationTypeProp.get(), AppState.getCurrentUser().email(), start,
          end, roomType,0);
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

  private void updateReserveButtonState(Observable observable)
  {
    boolean disable=true;
    if (startDate.get() != null && endDate.get() != null)
    {
      if(LocalDate.now().isAfter(startDate.get())){
        messageProp.set("Start Date can not be in the past");
      }
      else
        if (endDate.get().isAfter(startDate.get()))
      {
        messageProp.set("");
        disable = reservationTypeProp.get() == null || roomType == null;
      }
    }
    disableReserveButton.set(disable);
  }

  public BooleanProperty enableReserveButtonProp()
  {
    return disableReserveButton;
  }

  public ObjectProperty<ReservationType> reservationTypeProperty()
  {
    return reservationTypeProp;
  }

  public void onSmall()
  {
    roomType = RoomType.SMALL;
    roomTypeProperty();
  }

  public void onKing()
  {
    roomType = RoomType.KING;
    roomTypeProperty();
  }

  public void onFamily()
  {
    roomType = RoomType.FAMILY;
    roomTypeProperty();
  }
}