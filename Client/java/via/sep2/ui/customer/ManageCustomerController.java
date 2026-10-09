package via.sep2.ui.customer;

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
 * @author Mario
 */
public class ManageCustomerController implements Controller
{

  public TextField enterEmail = new TextField();
  public TextField firstName = new TextField();
  public TextField lastName = new TextField();
  public Button updateButton = new Button();
  public Button cancelButton = new Button();
  public Label messageLabel = new Label("");
  public Label errorLabel = new Label("");
  @FXML public TableView<PersonDataDto> personTable;
  @FXML public TableColumn<PersonDataDto, String> firstNameColumn;
  @FXML public TableColumn<PersonDataDto, String> lastNameColumn;
  @FXML public TableColumn<PersonDataDto, String> emailColumn;

  private final ManageCustomerVM vm;
  public ManageCustomerController(ManageCustomerVM vm)
  {
    this.vm=vm;
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
    enterEmail.textProperty().bindBidirectional(vm.emailProperty());
    firstName.textProperty().bindBidirectional(vm.firstNameProperty());
    lastName.textProperty().bindBidirectional(vm.lastNameProperty());
    cancelButton.disableProperty().bind(vm.disableCancelProperty());
    updateButton.disableProperty().bind(vm.disableUpdateButtonProperty());
    ClientData.addPropertyChangeListener(vm); // Ensure listener is registered
    vm.loadPeople();
  }

  public void onReturn()
  {
    ClientData.removePropertyChangeListener(vm);
    ViewHandler.showView(ViewType.ADD_CUSTOMER);
  }
  public void onUpdate()
  {
    vm.onUpdate();
  }
  public void onSearch()
  {
    vm.searchPeopleForEmail();
  }
  public void onDelete()
  {
    vm.onDelete();
  }


}
