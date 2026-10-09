package via.sep2.ui.reservation;

import dtos.person.PersonDataDto;
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
 * @author Rodrigo
 */
public class ReservationEmployeeController implements Controller
{
  private final ReservationEmployeeVM vm;
  public TextField enterEmail = new TextField();
  public Button reserveButton = new Button();
  public Label messageLabel = new Label("");
  public Label errorLabel = new Label("");
  public Label finalPrice = new Label("");
  public ComboBox<ReservationType> reservationType = new ComboBox<>();
  public ComboBox<RoomType> roomType = new ComboBox<>();
  @FXML public TableView<PersonDataDto> personTable;
  @FXML public TableColumn<PersonDataDto, String> firstNameColumn;
  @FXML public TableColumn<PersonDataDto, String> lastNameColumn;
  @FXML public TableColumn<PersonDataDto, String> emailColumn;
  public DatePicker startDate = new DatePicker();
  public DatePicker endDate = new DatePicker();

  public ReservationEmployeeController(ReservationEmployeeVM vm)
  {
    this.vm=vm;
  }
  public void onManage()
  {
    ClientData.removePropertyChangeListener(vm);
    ViewHandler.showView(ViewType.RESERVATION_MANAGE_EMPLOYEE);
  }
  public void onReserve()
  {
    vm.onReserve();
  }
  public void onSearch()
  {
    vm.searchReservationsForEmail();
  }

  public void initialize()
  {
    firstNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().firstName()));
    lastNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().lastName()));
    emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        (cellData.getValue().email())));
    personTable.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldSelection, newSelection) -> {
          if (newSelection != null)
          {
            PersonDataDto selected = vm.onPressedItem(newSelection);
          }
        });
    personTable.setItems(vm.getPeople());
    messageLabel.textProperty().bind(vm.messageProperty());
    errorLabel.textProperty().bind(vm.errorProperty());
    startDate.valueProperty().bindBidirectional(vm.startDateProperty());
    endDate.valueProperty().bindBidirectional(vm.endDateProperty());
    finalPrice.textProperty().bind(vm.finalPricePropProperty());
    enterEmail.textProperty().bindBidirectional(vm.emailProp());
    reservationType.setItems(
        FXCollections.observableArrayList(ReservationType.values()));
    reservationType.valueProperty()
        .bindBidirectional(vm.reservationTypeProperty());
    roomType.setItems(FXCollections.observableArrayList(RoomType.values()));
    roomType.valueProperty().bindBidirectional(vm.roomTypeProperty());
    reserveButton.disableProperty().bind(vm.enableReserveButtonProp());
    ClientData.addPropertyChangeListener(vm); // Ensure listener is registered
    vm.loadCustomers();
  }
}
