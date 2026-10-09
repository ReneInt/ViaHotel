package via.sep2.ui.reservation;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import types.ReservationType;
import via.sep2.startup.ViewHandler;
import via.sep2.startup.ViewType;
import via.sep2.ui.common.Controller;
/**
 * @author Mario, Rodrigo
 */
public class ReservationCustomerController implements Controller
{
  public Button buttonReserve =new Button();
  public DatePicker startDate=new DatePicker();
  public DatePicker endDate=new DatePicker();
  public ComboBox<ReservationType> reservationType=new ComboBox<>();
  public Label messageLabel=new Label("");
  public Label roomTypeLabel=new Label("");
  public Label finalPrice=new Label("");

  private final ReservationCustomerVM vm;

  public ReservationCustomerController(ReservationCustomerVM vm)
  {
    this.vm = vm;
  }

  public void manage()
  {
    ViewHandler.showView(ViewType.RESERVATION_MANAGE_CUSTOMER);
  }

  public void initialize()
  {
    //Initialize bindings
    messageLabel.textProperty().bind(vm.messageProperty());
    startDate.valueProperty().bindBidirectional(vm.startDateProperty());
    endDate.valueProperty().bindBidirectional(vm.endDateProperty());
    finalPrice.textProperty().bind(vm.finalPricePropProperty());
    reservationType.setItems(FXCollections.observableArrayList(ReservationType.values()));
    reservationType.valueProperty().bindBidirectional(vm.reservationTypeProperty());
    roomTypeLabel.textProperty().bind(vm.roomTypeProperty());
    buttonReserve.disableProperty().bind(vm.enableReserveButtonProp());
  }
  public void onSmall(){
    vm.onSmall();
  }
  public void onKing(){
    vm.onKing();
  }
  public void onFamily(){
    vm.onFamily();
  }
  public void onReserve()
  {
    vm.onReserve();
  }

}
