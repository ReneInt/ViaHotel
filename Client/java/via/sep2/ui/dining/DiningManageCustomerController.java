package via.sep2.ui.dining;

import dtos.dining.DiningRequestDto;
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
public class DiningManageCustomerController implements Controller
{
  @FXML public TableView<DiningRequestDto> diningTable;
  @FXML public TableColumn<DiningRequestDto, String> dateColumn;
  @FXML public TableColumn<DiningRequestDto, String> hourColumn;
  @FXML public TableColumn<DiningRequestDto, String> emailColumn;
  @FXML public Button buttonUpdate = new Button();
  public Button cancelButton = new Button();
  public Label messageLabel = new Label("");
  public Label errorLabel = new Label("");
  @FXML private ComboBox<HourValues> hours = new ComboBox<>();
  public DatePicker diningDate = new DatePicker();
  private final DiningManageCustomerVM vm;

  public DiningManageCustomerController(DiningManageCustomerVM vm)
  {
    this.vm = vm;
  }

  public void initialize()
  {
    dateColumn.setCellValueFactory(
        cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().date().toString()));
    emailColumn.setCellValueFactory(
        cellData -> new javafx.beans.property.SimpleStringProperty(
            String.valueOf(cellData.getValue().email())));
    hourColumn.setCellValueFactory(
        (cellData -> new javafx.beans.property.SimpleStringProperty(
            cellData.getValue().hour().toString())));
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
    diningDate.valueProperty().bindBidirectional(vm.diningDateProperty());
    cancelButton.disableProperty().bind(vm.updateCancel());
    hours.valueProperty().bindBidirectional(vm.hourProperty());
    hours.setItems(FXCollections.observableArrayList(HourValues.values()));
    buttonUpdate.disableProperty().bind(vm.enableUpdateButtonProp());
    vm.loadDiningForCurrentUser();
  }

  public void onReturn()
  {
    ClientData.removePropertyChangeListener(vm);
    ViewHandler.showView(ViewType.DINING_CUSTOMER);
  }

  public void onUpdate()
  {
    vm.onUpdate();
  }

  public void onCancel()
  {
    vm.onCancel();
  }
}
