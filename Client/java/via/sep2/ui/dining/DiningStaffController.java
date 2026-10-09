package via.sep2.ui.dining;

import dtos.person.PersonDataDto;
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
 * @author Mario
 */
public class DiningStaffController implements Controller
{
  public TextField enterEmail = new TextField();
  private final DiningStaffVM vm;
  @FXML public TableView<PersonDataDto> personTable;
  @FXML public TableColumn<PersonDataDto, String> firstNameColumn;
  @FXML public TableColumn<PersonDataDto, String> lastNameColumn;
  @FXML public TableColumn<PersonDataDto, String> emailColumn;
  public Button cancelButton = new Button();
  public Button diningButton = new Button();
  public DatePicker date = new DatePicker();
  public ComboBox<HourValues> hourValues = new ComboBox<>();
  public Label messageLabel = new Label("");
  public Label errorLabel = new Label("");

  public DiningStaffController(DiningStaffVM vm)
  {
    this.vm = vm;
  }

  public void onManage()
  {
    ClientData.removePropertyChangeListener(vm);
    ViewHandler.showView(ViewType.DINING_MANAGE_EMPLOYEE);
  }

  public void onReserve()
  {
    vm.onReserve();
  }

  public void initialize()
  {
    firstNameColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().firstName()));
    lastNameColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty(cellData.getValue().lastName()));
    emailColumn.setCellValueFactory(
        cellData -> new SimpleStringProperty((cellData.getValue().email())));
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
    date.valueProperty().bindBidirectional(vm.dateProperty());
    hourValues.setItems(FXCollections.observableArrayList(HourValues.values()));
    hourValues.valueProperty().bindBidirectional(vm.hourValuesProperty());
    diningButton.disableProperty().bind(vm.enableReserveButtonProp());
    cancelButton.disableProperty().bind(vm.enableCancel());
    enterEmail.textProperty().bindBidirectional(vm.emailProp());
    ClientData.addPropertyChangeListener(vm); // Ensure listener is registered
    vm.loadPeople();
  }

  public void onSearch()
  {
    vm.searchDiningForEmail();
  }
}
