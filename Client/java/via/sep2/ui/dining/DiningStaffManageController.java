package via.sep2.ui.dining;

import dtos.dining.DiningRequestDto;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import types.HourValues;
import via.sep2.startup.ViewHandler;
import via.sep2.startup.ViewType;
import via.sep2.data.ClientData;
import via.sep2.ui.common.Controller;

/**
 * @author Mario, Rodrigo
 */
public class DiningStaffManageController implements Controller
{
  public TextField enterEmail = new TextField();
  private final DiningStaffManageVM vm;
  @FXML public TableView<DiningRequestDto> diningTable;
  @FXML public TableColumn<DiningRequestDto, String> idColumn;
  @FXML public TableColumn<DiningRequestDto, String> dateColumn;
  @FXML public TableColumn<DiningRequestDto, String> hourColumn;
  @FXML public TableColumn<DiningRequestDto, String> emailColumn;
  public Button diningButton = new Button();
  public Button cancelButton = new Button();
  public DatePicker date = new DatePicker();
  public ComboBox<HourValues> hourValues = new ComboBox<>();
  public Label messageLabel = new Label("");
  public Label errorLabel = new Label("");

  public DiningStaffManageController(DiningStaffManageVM vm)
  {
    this.vm = vm;
  }

  public void onCancel()
  {
    vm.onCancel();
  }

  public void onReturn()
  {
    ClientData.removePropertyChangeListener(vm);
    ViewHandler.showView(ViewType.DINING_EMPLOYEE);
  }

  public void onUpdate()
  {
    vm.onReserve();
  }

  public void initialize()
  {
    dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().date().toString()));
    idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        (String.valueOf(cellData.getValue().id()))));
    hourColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        (String.valueOf(cellData.getValue().hour()))));
    emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
        (String.valueOf(cellData.getValue().email()))));
    diningTable.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldSelection, newSelection) -> {
          if (newSelection != null)
          {
            DiningRequestDto selected = vm.onPressedItem(newSelection);
          }
        });
    diningTable.setItems(vm.getDining());
    messageLabel.textProperty().bind(vm.messageProperty());
    errorLabel.textProperty().bind(vm.errorProperty());
    date.valueProperty().bindBidirectional(vm.dateProperty());
    hourValues.setItems(FXCollections.observableArrayList(HourValues.values()));
    hourValues.valueProperty().bindBidirectional(vm.hourValuesProperty());
    diningButton.disableProperty().bind(vm.enableReserveButtonProp());
    enterEmail.textProperty().bindBidirectional(vm.emailProp());
    cancelButton.disableProperty().bind(vm.updateCancel());
    ClientData.addPropertyChangeListener(vm); // Ensure listener is registered
    vm.loadDining();
  }

  public void onSearch()
  {
    vm.searchDiningForEmail();
  }
}
