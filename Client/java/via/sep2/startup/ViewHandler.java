package via.sep2.startup;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import via.sep2.networking.authentication.AuthenticationClient;
import via.sep2.networking.authentication.SocketAuthenticationClient;
import via.sep2.networking.dining.DiningClient;
import via.sep2.networking.dining.SocketDiningClient;
import via.sep2.networking.people.PeopleClient;
import via.sep2.networking.people.SocketPeopleClient;
import via.sep2.networking.reservation.ReservationClient;
import via.sep2.networking.reservation.SocketReservationClient;
import via.sep2.networking.room.RoomClient;
import via.sep2.networking.room.SocketRoomClient;
import via.sep2.ui.common.Controller;
import via.sep2.ui.customer.AddCustomerController;
import via.sep2.ui.customer.AddCustomerVM;
import via.sep2.ui.customer.ManageCustomerController;
import via.sep2.ui.customer.ManageCustomerVM;
import via.sep2.ui.dining.*;
import via.sep2.ui.employees.AddEmployeeController;
import via.sep2.ui.employees.AddEmployeeVM;
import via.sep2.ui.employees.ManageEmployeeController;
import via.sep2.ui.employees.ManageEmployeeVM;
import via.sep2.ui.login.LoginController;
import via.sep2.ui.login.LoginVM;
import via.sep2.ui.overview.Overview;
import via.sep2.ui.overview.OverviewCustomerController;
import via.sep2.ui.overview.OverviewEmployeeController;
import via.sep2.ui.overview.OverviewManagerController;
import via.sep2.ui.popup.MessageType;
import via.sep2.ui.popup.PopupController;
import via.sep2.ui.price.PriceController;
import via.sep2.ui.price.PriceVM;
import via.sep2.ui.register.RegisterController;
import via.sep2.ui.register.RegisterVM;
import via.sep2.ui.reservation.*;
import via.sep2.ui.updatePassword.UpdatePasswordController;
import via.sep2.ui.updatePassword.UpdatePasswordVM;
import java.io.IOException;

/**
 * @author Troels, Mario, Rene
 */
public class ViewHandler
{
  private static Scene scene;
  private static Stage stage;
  private static Style style = Style.DARK;
  public static Style getStyle()
  {
    return style;
  }

  public ViewHandler(Stage stage)
  {
    ViewHandler.stage = stage;
    stage.setResizable(false);
  }

  private static Overview overview;
  public void start()
  {
    showView(ViewType.LOGIN);
    stage.show();
  }

  public static void showView(ViewType viewToShow)
  {
    try
    {
      switch (viewToShow)
      {
        case UPDATE_PASSWORD -> openUpdateView();
       case RESERVATION_CUSTOMER -> openCustomerReservation();
        case REGISTER -> openRegisterCustomerView();
        case LOGIN -> openLoginView();
        case OVERVIEW_CUSTOMER -> openCustomerView();
        case OVERVIEW_EMPLOYEE -> openEmployeeView();
        case OVERVIEW_MANAGER -> openManagerView();
        case RESERVATION_MANAGE_CUSTOMER -> openCustomerManageReservationView();
        case DINING_CUSTOMER -> openDiningView();
        case DINING_MANAGE_CUSTOMER -> openDiningManageView();
        case ADD_CUSTOMER -> openCustomerAddView();
        case MANAGE_CUSTOMER ->openCustomerManageView();
        case ADD_EMPLOYEE -> openEmployeeAdd();
        case MANAGE_EMPLOYEE -> openEmployeeManage();
        case DINING_EMPLOYEE -> openStaffDining();
        case DINING_MANAGE_EMPLOYEE ->openStaffDiningManage();
        case RESERVATION_EMPLOYEE -> openStaffReservationView();
        case RESERVATION_MANAGE_EMPLOYEE ->openStaffManageReservationView();
        case PRICE ->openPriceView();
        default -> throw new RuntimeException("View not found.");
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  private static void openPriceView() throws IOException
  {
    RoomClient roomClient = new SocketRoomClient();
    ReservationClient reservationClient = new SocketReservationClient();
    PriceVM priceVM = new PriceVM(roomClient,reservationClient);
    PriceController priceController = new PriceController(priceVM);
    String viewSubPathBorder = "price/Price.fxml";
    openInPane(viewSubPathBorder, priceController,overview.getContentPane());
  }
  private static void openEmployeeManage() throws IOException
  {
    PeopleClient peopleClient = new SocketPeopleClient();
    ManageEmployeeVM employeeVM = new ManageEmployeeVM(peopleClient);
    ManageEmployeeController customerController = new ManageEmployeeController(employeeVM);
    String viewSubPathBorder = "employees/ManageEmployees.fxml";
    openInPane(viewSubPathBorder, customerController,overview.getContentPane());
  }
  private static void openEmployeeAdd() throws IOException
  {
    AuthenticationClient authClient = new SocketAuthenticationClient();
    AddEmployeeVM employeeVM = new AddEmployeeVM(authClient);
    AddEmployeeController customerController = new AddEmployeeController(employeeVM);
    String viewSubPathBorder = "employees/AddEmployee.fxml";
    openInPane(viewSubPathBorder, customerController,overview.getContentPane());
  }
  private static void openCustomerAddView() throws IOException
  {
    PeopleClient peopleClient = new SocketPeopleClient();
    AddCustomerVM customerVM = new AddCustomerVM(peopleClient);
    AddCustomerController customerController = new AddCustomerController(customerVM);
    String viewSubPathBorder = "customer/AddCustomer.fxml";
    openInPane(viewSubPathBorder, customerController,overview.getContentPane());
  }
  private static void openCustomerManageView() throws IOException
  {
    PeopleClient peopleClient = new SocketPeopleClient();
    ManageCustomerVM customerVM = new ManageCustomerVM(peopleClient);
    ManageCustomerController customerController = new ManageCustomerController(customerVM);
    String viewSubPathBorder = "customer/ManageCustomer.fxml";
    openInPane(viewSubPathBorder, customerController,overview.getContentPane());
  }

  private static void openStaffDiningManage() throws IOException
  {
    DiningClient diningService = new SocketDiningClient();
    DiningStaffManageVM diningVm = new DiningStaffManageVM(diningService);
    DiningStaffManageController diningController = new DiningStaffManageController(diningVm);
    String viewSubPathBorder = "dining/DiningManageForEmployees.fxml";
      openInPane(viewSubPathBorder, diningController,overview.getContentPane());
  }

  private static void openStaffDining() throws IOException
  {
    DiningClient diningService = new SocketDiningClient();
    DiningStaffVM diningVm = new DiningStaffVM(diningService);
    DiningStaffController diningController = new DiningStaffController(diningVm);
    String viewSubPathBorder = "dining/DiningForEmployees.fxml";
    openInPane(viewSubPathBorder, diningController,overview.getContentPane());
  }

  private static void openDiningManageView() throws IOException
  {
    DiningClient diningService = new SocketDiningClient();
    DiningManageCustomerVM diningVm = new DiningManageCustomerVM(diningService);
    DiningManageCustomerController diningController = new DiningManageCustomerController(diningVm);
    String viewSubPathBorder = "dining/DiningManageForCustomers.fxml";
      openInPane(viewSubPathBorder, diningController, overview.getContentPane());
  }

  private static void openDiningView() throws IOException
  {
    DiningClient diningService = new SocketDiningClient();
    DiningForCustomersVM diningVm = new DiningForCustomersVM(diningService);
    DiningForCustomersController diningController = new DiningForCustomersController(diningVm);
    String viewSubPathBorder = "dining/DiningForCustomers.fxml";
      openInPane(viewSubPathBorder, diningController, overview.getContentPane());

  }
  private static void openManagerView() throws IOException
  {
   OverviewManagerController controller = new OverviewManagerController();
    overview=controller;
    String viewTitle = "Overview";
    String viewSubPath = "overview/OverviewManager.fxml";
    openView(viewTitle, viewSubPath, controller);
    openStaffReservationView();
  }

  private static void openEmployeeView() throws IOException
  {
    OverviewEmployeeController controller = new OverviewEmployeeController();
    overview=controller;
    String viewTitle = "Overview";
    String viewSubPath = "overview/OverviewEmployee.fxml";
    openView(viewTitle, viewSubPath, controller);
    openStaffReservationView();
  }

  private static void openCustomerManageReservationView() throws IOException
  {
    ReservationClient reservationService = new SocketReservationClient();
    ReservationManageCustomerVM reservationVm = new ReservationManageCustomerVM(reservationService);
    ReservationManageCustomerController reservationController = new ReservationManageCustomerController(reservationVm);
    String viewSubPathBorder = "reservation/ReservationManageCustomer.fxml";
    openInPane(viewSubPathBorder, reservationController,overview.getContentPane());
  }
  private static void openStaffManageReservationView() throws IOException
  {
    ReservationClient reservationService = new SocketReservationClient();
    ReservationManageEmployeeVM reservationVm = new ReservationManageEmployeeVM(reservationService);
    ReservationManageEmployeeController reservationController = new ReservationManageEmployeeController(reservationVm);
    String viewSubPathBorder = "reservation/ReservationManageEmployee.fxml";
      openInPane(viewSubPathBorder, reservationController, overview.getContentPane());
  }
  private static void openStaffReservationView() throws IOException
  {
    ReservationClient reservationService = new SocketReservationClient();
    ReservationEmployeeVM reservationVm = new ReservationEmployeeVM(reservationService);
    ReservationEmployeeController reservationController = new ReservationEmployeeController(reservationVm);
    String viewSubPathBorder = "reservation/ReservationEmployee.fxml.";
      openInPane(viewSubPathBorder, reservationController,overview.getContentPane());
  }

    public static void popupMessage(MessageType type, String message) // currently always an error, will fix later for success message too.
    {
      Stage stage = new Stage();
      stage.setMinWidth(300);
      stage.setMinHeight(200);

      PopupController controller = new PopupController(stage, type, message);

      FXMLLoader fxmlLoader = new FXMLLoader(ViewHandler.class.getResource("../ui/popup/Popup.fxml"));
      fxmlLoader.setControllerFactory(ignore -> controller);

      try
      {
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().clear();
        scene.getStylesheets().add(ViewHandler.class.getResource(style.toString()).toExternalForm());
        stage.setTitle(type.toString());
        stage.setScene(scene);
      }
      catch (IOException e)
      {
        throw new RuntimeException(e);
      }
      stage.show();
    }

  private static void openCustomerReservation() throws IOException
  {
    ReservationClient reservationService = new SocketReservationClient();
    ReservationCustomerVM reservationVm = new ReservationCustomerVM(reservationService);
    ReservationCustomerController reservationController = new ReservationCustomerController(reservationVm);
    String viewSubPathBorder = "reservation/ReservationCustomer.fxml";
      openInPane(viewSubPathBorder, reservationController,overview.getContentPane());
  }

  private static void openLoginView() throws IOException
  {
    AuthenticationClient service = new SocketAuthenticationClient();
    LoginVM vm = new LoginVM(service);
    LoginController controller = new LoginController(vm);
    String viewTitle = "Login";
    String viewSubPath = "login/Login.fxml";
    openView(viewTitle, viewSubPath, controller);
  }

  private static void openRegisterCustomerView() throws IOException
  {
    AuthenticationClient service = new SocketAuthenticationClient();
    RegisterVM vm = new RegisterVM(service);
    RegisterController controller = new RegisterController(vm);
    String viewTitle = "Register";
    String viewSubPath = "register/SignUp.fxml";
    openView(viewTitle, viewSubPath, controller);
  }

  private static void openUpdateView() throws IOException
  {
    AuthenticationClient service = new SocketAuthenticationClient();
    UpdatePasswordVM vm = new UpdatePasswordVM (service);
    UpdatePasswordController controller = new UpdatePasswordController(vm);
    String viewTitle = "UpdatePassword";
    String viewSubPath = "updatePassword/UpdatePassword.fxml";
    openView(viewTitle, viewSubPath, controller);
  }
  private static void openCustomerView() throws IOException
  {
    OverviewCustomerController controller = new OverviewCustomerController();
    overview=controller;
    String viewTitle = "Overview";
    String viewSubPath = "overview/OverviewCustomer.fxml";
    openView(viewTitle, viewSubPath, controller);
    openCustomerReservation();
  }

  private static void openView(String viewTitle, String viewSubPath,
      Controller controller) throws IOException
  {
    FXMLLoader fxmlLoader = new FXMLLoader(
        ViewHandler.class.getResource("../ui/" + viewSubPath));
    fxmlLoader.setControllerFactory(ignore -> controller);
    scene = new Scene(fxmlLoader.load());
    stage.setTitle(viewTitle);
    setStyle(style);
    stage.setScene(scene);
  }
  private static void openInPane(String viewSubPath, Controller controller, BorderPane borderPane) throws IOException {
    if (borderPane == null) {
      throw new IllegalArgumentException("BorderPane cannot be null");
    }
    FXMLLoader fxmlLoader = new FXMLLoader(ViewHandler.class.getResource("../ui/" + viewSubPath));
    fxmlLoader.setControllerFactory(ignore -> controller);

    Parent view = fxmlLoader.load();
    borderPane.setCenter(view);
  }
  public static void setStyle(Style theme)
  {
    style=theme;
    scene.getStylesheets().clear();
    scene.getStylesheets().add(ViewHandler.class.getResource(style.toString()).toExternalForm());
  }
}