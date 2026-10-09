package via.sep2.ui.employees;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import types.PersonType;
import types.ReservationType;
import types.RoomType;
import via.sep2.startup.ViewHandler;
import via.sep2.startup.ViewType;
import via.sep2.ui.common.Controller;
/**
 * @author  Mario
 */
public class AddEmployeeController implements Controller
{
  @FXML
  public TextField emailInput;
  @FXML
  public PasswordField passwordInput;
  @FXML
  public PasswordField repeatPasswordInput;
  @FXML
  public TextField firstNameInput;
  @FXML
  public TextField lastNameInput;
  @FXML
  public Label messageLabel;
  @FXML
  public Button buttonRegister;
  public ComboBox<PersonType> personType = new ComboBox<>();

  private final AddEmployeeVM viewModel;

  public AddEmployeeController(AddEmployeeVM vm)
  {
    this.viewModel = vm;
  }

  public void initialize()
  {
    emailInput.textProperty().bindBidirectional(viewModel.emailProperty());
    passwordInput.textProperty().bindBidirectional(viewModel.passwordProperty());
    repeatPasswordInput.textProperty().bindBidirectional(viewModel.repeatProperty());
    firstNameInput.textProperty().bindBidirectional(viewModel.firstNameProperty());
    lastNameInput.textProperty().bindBidirectional(viewModel.lastNameProperty());
    messageLabel.textProperty().bind(viewModel.messageProperty());
    personType.setItems(
        FXCollections.observableArrayList(PersonType.EMPLOYEE,PersonType.MANAGER));
    personType.valueProperty().bindBidirectional(viewModel.personTypePropProperty());
    buttonRegister.disableProperty().bind(viewModel.disableRegisterButtonProperty());
  }

  public void onRegister()
  {
    viewModel.registerUser();
  }

  public void onManage()
  {
    ViewHandler.showView(ViewType.MANAGE_EMPLOYEE);
  }

}
