package via.sep2.ui.reservation;

import dtos.reservation.ReservationRoomTypeDto;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import types.ReservationType;
import types.RoomType;
import via.sep2.startup.ViewHandler;
import via.sep2.startup.ViewType;
import via.sep2.data.ClientData;
import via.sep2.ui.common.Controller;
/**
 * @author Mario
 */
public class ReservationManageEmployeeController implements Controller
{
  public TextField enterEmail = new TextField("");
  @FXML public TableView<ReservationRoomTypeDto> reservationTable;
  @FXML public TableColumn<ReservationRoomTypeDto, String> startDateColumn;
  @FXML public TableColumn<ReservationRoomTypeDto, String> endDateColumn;
  @FXML public TableColumn<ReservationRoomTypeDto, String> roomTypeColumn;
  @FXML public TableColumn<ReservationRoomTypeDto, String> reservationTypeColumn;
  @FXML public TableColumn<ReservationRoomTypeDto, String> finalPriceColumn;
  @FXML public TableColumn<ReservationRoomTypeDto, String> emailColumn;
  public Button buttonUpdate = new Button();
  public Button cancelUpdate = new Button();
  @FXML public DatePicker startDate = new DatePicker();
  public DatePicker endDate = new DatePicker();
  public ComboBox<ReservationType> reservationType = new ComboBox<>();
  public ComboBox<RoomType> roomType = new ComboBox<>();
  public Label messageLabel = new Label("");
  public Label finalPrice = new Label("");
  private final ReservationManageEmployeeVM vm;
  public ReservationManageEmployeeController(ReservationManageEmployeeVM vm)
  {
    this.vm=vm;
  }
  public void onReturn()
  {
    ClientData.removePropertyChangeListener(vm);
    ViewHandler.showView(ViewType.RESERVATION_EMPLOYEE);
  }
  public void initialize()
  {
    //Initializing with VM bindings
    startDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().startDate().toString()));
    enterEmail.textProperty().bindBidirectional(vm.emailProp());
    endDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().endDate().toString()));
    roomTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        String.valueOf(cellData.getValue().roomType())));
    emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        String.valueOf(cellData.getValue().email())));
    reservationTypeColumn.setCellValueFactory(
        (cellData -> new SimpleStringProperty(
            cellData.getValue().type().toString())));
    finalPriceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        (String.valueOf(cellData.getValue().finalPrice()))));
    reservationTable.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldSelection, newSelection) -> {
          if (newSelection != null)
          {
            ReservationRoomTypeDto selected = vm.onPressedItem(newSelection);
          }
        });
    reservationTable.setItems(vm.getReservations());
    messageLabel.textProperty().bind(vm.messageProperty());
    startDate.valueProperty().bindBidirectional(vm.startDateProperty());
    endDate.valueProperty().bindBidirectional(vm.endDateProperty());
    finalPrice.textProperty().bind(vm.finalPricePropProperty());
    reservationType.setItems(
        FXCollections.observableArrayList(ReservationType.values()));
    reservationType.valueProperty()
        .bindBidirectional(vm.reservationTypeProperty());
    roomType.setItems(FXCollections.observableArrayList(RoomType.values()));
    roomType.valueProperty().bindBidirectional(vm.roomTypeProperty());
    buttonUpdate.disableProperty().bind(vm.enableReserveButtonProp());
    cancelUpdate.disableProperty().bind(vm.enableCancel());
    ClientData.addPropertyChangeListener(vm); // Ensure listener is registered
    vm.loadReservations();
  }

  public void onEdit()
  {
    vm.onEdit();
  }

  public void onCancel()
  {
    vm.onCancel();
  }
  public void onSearch()
  {
    vm.searchReservationsForEmail();
  }
}
