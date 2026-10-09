package via.sep2.ui.overview;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import via.sep2.data.ClientData;
import via.sep2.startup.Style;
import via.sep2.startup.ViewHandler;
import via.sep2.startup.ViewType;
import via.sep2.ui.popup.MessageType;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class OverviewManagerController implements Overview,
    PropertyChangeListener
{
  @FXML private BorderPane contentPane;
  @FXML private Button themeButton = new Button();

  public OverviewManagerController()
  {
  }

  public BorderPane getContentPane()
  {
    return contentPane;
  }

  public void initialize()
  {
    ClientData.addPropertyChangeListener(this);
    switch (ViewHandler.getStyle())
    {
      case DARK ->
      {
        themeButton.setText("Switch to Light");
      }
      case LIGHT ->
      {
        themeButton.setText("Switch to Dark");
      }
    }

  }

  public void onLogOut()
  {

    ClientData.removePropertyChangeListener(this);
    ViewHandler.showView(ViewType.LOGIN);
  }

  public void onChangeTheme()
  {
    switch (ViewHandler.getStyle())
    {
      case DARK ->
      {
        themeButton.setText("Switch to Dark");
        ViewHandler.setStyle(Style.LIGHT);
      }
      case LIGHT ->
      {
        themeButton.setText("Switch to Light");
        ViewHandler.setStyle(Style.DARK);
      }
    }
  }

  public void onReservation()
  {
    ViewHandler.showView(ViewType.RESERVATION_EMPLOYEE);
  }

  public void onDining()
  {
    ViewHandler.showView(ViewType.DINING_EMPLOYEE);
  }
  public void onCustomer()
  {
    ViewHandler.showView(ViewType.ADD_CUSTOMER);
  }
  //This made me cry.
  //Update... made it work... I m crying of joy, Mario
  @Override
  public void propertyChange(PropertyChangeEvent evt)
  {
    if ("notification".equals(evt.getPropertyName()))
    {
      if (evt.getNewValue() instanceof String message)
      {
        Platform.runLater(() -> ViewHandler.popupMessage(MessageType.NOTIFICATION, message));
      }
      else
      {
        System.err.println("Invalid notification payload: " + evt.getNewValue());
      }
    }
  }
  public void onEmployee()
  {
    ViewHandler.showView(ViewType.ADD_EMPLOYEE);
  }
  public void onPrice()
  {
    ViewHandler.showView(ViewType.PRICE);
  }
}