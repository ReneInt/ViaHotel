package via.sep2.ui.price;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import types.PersonType;
import types.ReservationType;
import types.RoomType;
import via.sep2.startup.ViewHandler;
import via.sep2.startup.ViewType;
import via.sep2.ui.common.Controller;

public class PriceController implements Controller
{
  public Label messageLabelRes;
  public Label messageLabelRoom;
  public TextField priceReservation;
  public TextField priceRoom;
  public ComboBox<RoomType> roomType=new ComboBox<>();
  public ComboBox<ReservationType> reservationType=new ComboBox<>();
  public Button updateReservation;
  public Button updateRoom;

  private final PriceVM vm;

  public PriceController(PriceVM vm)
  {
    this.vm = vm;
  }

  public void initialize()
  {
    messageLabelRes.textProperty().bind(vm.messagePropertyRes());
    messageLabelRoom.textProperty().bind(vm.messagePropertyRoom());
    NumberStringConverter converter = new NumberStringConverter("#0.00");

    Bindings.bindBidirectional(priceReservation.textProperty(), vm.priceReservationProperty(), converter);
    Bindings.bindBidirectional(priceRoom.textProperty(), vm.priceRoomProperty(), converter);
    roomType.setItems(
        FXCollections.observableArrayList(RoomType.values()));
    roomType.valueProperty().bindBidirectional(vm.roomTypeProp());
    reservationType.setItems(
        FXCollections.observableArrayList(ReservationType.values()));
    reservationType.valueProperty().bindBidirectional(vm.reservationTypeProp());
    updateRoom.disableProperty().bind(vm.disableRoomButtonProp());
    updateReservation.disableProperty().bind(vm.disableReservationButtonProp());
  }
  public void onReservation()
  {
    vm.onReservation();
  }
  public void onRoom()
  {
   vm.onRoom();
  }
}
