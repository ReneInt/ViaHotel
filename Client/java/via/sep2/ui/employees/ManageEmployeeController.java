package via.sep2.ui.employees;

import dtos.person.PersonDataDto;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import types.PersonType;
import types.ReservationType;
import types.RoomType;
import via.sep2.startup.ViewHandler;
import via.sep2.startup.ViewType;
import via.sep2.data.ClientData;
import via.sep2.ui.common.Controller;
import via.sep2.ui.employees.ManageEmployeeVM;

/**
 * @author Mario
 */
public class ManageEmployeeController implements Controller
{
  private final ManageEmployeeVM vm;
  public TextField enterEmail = new TextField();
  public TextField firstName = new TextField();
  public TextField lastName = new TextField();
  public Button updateButton = new Button();
  public Button cancelButton = new Button();
  public Label messageLabel = new Label("");
  public ComboBox<PersonType> personType = new ComboBox<>();
  public Label errorLabel = new Label("");
  @FXML public TableView<PersonDataDto> personTable;
  @FXML public TableColumn<PersonDataDto, String> firstNameColumn;
  @FXML public TableColumn<PersonDataDto, String> lastNameColumn;
  @FXML public TableColumn<PersonDataDto, String> emailColumn;


  public ManageEmployeeController(ManageEmployeeVM vm)
  {
    this.vm=vm;
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
    personType.setItems(
        FXCollections.observableArrayList(PersonType.EMPLOYEE, PersonType.MANAGER));
    personType.valueProperty().bindBidirectional(vm.personTypePropProperty());
    cancelButton.disableProperty().bind(vm.disableCancelProperty());
    updateButton.disableProperty().bind(vm.disableUpdateButtonProperty());
    ClientData.addPropertyChangeListener(vm); // Ensure listener is registered
    vm.loadPeople();
  }

}
