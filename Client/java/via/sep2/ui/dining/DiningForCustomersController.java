package via.sep2.ui.dining;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import types.HourValues;
import via.sep2.startup.ViewHandler;
import via.sep2.startup.ViewType;
import via.sep2.ui.common.Controller;
/**
 * @author Rodrigo
 */
public class DiningForCustomersController implements Controller
{ @FXML private Button reserveButton = new Button();
  @FXML private Label messageLabel = new Label("");
 @FXML private ComboBox<HourValues> hours = new ComboBox<>();
 @FXML private DatePicker diningDate = new DatePicker();

  private final DiningForCustomersVM vm;

  public DiningForCustomersController(DiningForCustomersVM vm) {
    this.vm = vm;

  }

  public void manage() {
    ViewHandler.showView(ViewType.DINING_MANAGE_CUSTOMER);
  }

  public void initialize() {
    messageLabel.textProperty().bind(vm.messageProperty());
    diningDate.valueProperty().bindBidirectional(vm.diningDateProperty());
    reserveButton.disableProperty().bind(vm.enableReserveButtonProp());
    hours.valueProperty().bindBidirectional(vm.hourProperty());
    hours.setItems(FXCollections.observableArrayList(HourValues.values()));
  }
  public void onReserve()
  {
    vm.onReserve();
  }
}