package via.sep2.ui.price;

import dtos.auth.LoginRequest;
import dtos.person.PersonDataDto;
import dtos.reservation.ReservationRoomTypeDto;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import types.PersonType;
import types.ReservationType;
import types.RoomType;
import utils.StringUtils;
import via.sep2.data.AppState;
import via.sep2.networking.authentication.AuthenticationClient;
import via.sep2.networking.reservation.ReservationClient;
import via.sep2.networking.room.RoomClient;
import via.sep2.startup.ViewHandler;
import via.sep2.startup.ViewType;
import via.sep2.ui.popup.MessageType;

import javax.swing.text.View;

public class PriceVM
{
  private final DoubleProperty priceReservationProp = new SimpleDoubleProperty();
  private final DoubleProperty priceRoomProp = new SimpleDoubleProperty();
  private final StringProperty messagePropRes = new SimpleStringProperty();
  private final StringProperty messagePropRoom = new SimpleStringProperty();
  private final BooleanProperty disableRoomButton = new SimpleBooleanProperty(
      true);
  private final BooleanProperty disableReservationButton = new SimpleBooleanProperty(
      true);
  private final ObjectProperty<RoomType> roomTypeProp = new SimpleObjectProperty<>();
  private final ObjectProperty<ReservationType> reservationTypeProp = new SimpleObjectProperty<>();
  private final RoomClient roomService;
  private final ReservationClient reservationClient;

  public ObjectProperty<RoomType> roomTypeProp()
  {
    return roomTypeProp;
  }

  public ObjectProperty<ReservationType> reservationTypeProp()
  {
    return reservationTypeProp;
  }

  public PriceVM(RoomClient roomService, ReservationClient reservationClient)
  {
    this.roomService = roomService;
    this.reservationClient = reservationClient;
    priceRoomProp.addListener(this::updateRoomButtonState);
    priceReservationProp.addListener(this::updateReservationButtonState);
  }

  public DoubleProperty priceRoomProperty()
  {
    return priceRoomProp;
  }

  public DoubleProperty priceReservationProperty()
  {
    return priceReservationProp;
  }

  public StringProperty messagePropertyRes()
  {
    return messagePropRes;
  }

  public StringProperty messagePropertyRoom()
  {
    return messagePropRoom;
  }

  public BooleanProperty disableRoomButtonProp()
  {
    return disableRoomButton;
  }

  public BooleanProperty disableReservationButtonProp()
  {
    return disableReservationButton;
  }

  private void updateReservationButtonState(Observable observable)
  {
    boolean shouldDisable = priceReservationProp.getValue()==0.00 || reservationTypeProp.get()==null;

    disableReservationButton.set(shouldDisable);
  }

  private void updateRoomButtonState(Observable observable)
  {
    boolean shouldDisable = priceRoomProp.getValue()==0.00 || roomTypeProp.get()==null;

    disableRoomButton.set(shouldDisable);
  }

  public void onReservation()
  {
    messagePropRes.set("");
    ReservationRoomTypeDto payload=new ReservationRoomTypeDto(0,reservationTypeProp.get(),null,null,null,null,priceReservationProp.getValue());
    try
    {
      reservationClient.updatePrice(payload);
      clearFieldsReservation();
      ViewHandler.popupMessage(MessageType.SUCCESS,"Price changed!");
      messagePropRes.set("Success");
    }
    catch (Exception e)
    {
      ViewHandler.popupMessage(MessageType.WARNING,"Could not change price");
      messagePropRes.set(e.getMessage());
    }
  }
  public void onRoom()
  {
    messagePropRoom.set("");
    ReservationRoomTypeDto payload=new ReservationRoomTypeDto(0,null,null,null,null,roomTypeProp.get(),priceRoomProp.getValue());
    try
    {
      roomService.updatePrice(payload);
      ViewHandler.popupMessage(MessageType.SUCCESS,"Price changed!");
      clearFieldsRoom();
      messagePropRoom.set("Success");
    }
    catch (Exception e)
    {
      ViewHandler.popupMessage(MessageType.WARNING,"Could not change price");
      messagePropRoom.set(e.getMessage());
    }
  }
  private void clearFieldsRoom()
  {
    roomTypeProp.set(null);
    priceRoomProp.set(0.00);
  }
  private void clearFieldsReservation()
  {
    reservationTypeProp.set(null);
    priceReservationProp.set(0.00);
  }
}
