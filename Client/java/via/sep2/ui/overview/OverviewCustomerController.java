package via.sep2.ui.overview;

import dtos.person.PersonDataDto;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import types.PersonType;
import via.sep2.startup.Style;
import via.sep2.startup.ViewHandler;
import via.sep2.startup.ViewType;
import via.sep2.ui.common.Controller;
import via.sep2.ui.popup.MessageType;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class OverviewCustomerController implements Overview
{
  @FXML private BorderPane contentPane;
  @FXML private Button themeButton= new Button();

  public BorderPane getContentPane() {
    return contentPane;
  }
  public void onReservations()
  {
    ViewHandler.showView(ViewType.RESERVATION_CUSTOMER);
  }
  public void initialize()
  {
    switch (ViewHandler.getStyle())
    {
      case DARK -> {
        themeButton.setText("Switch to Light");
      }
      case LIGHT -> {
        themeButton.setText("Switch to Dark");
      }
    }

  }
  public void onLogOut()
  {
    ViewHandler.showView(ViewType.LOGIN);
  }

  public void onChangeTheme()
  {
    switch (ViewHandler.getStyle())
    {
      case DARK->{
      themeButton.setText("Switch to Dark");
      ViewHandler.setStyle(Style.LIGHT);
      }
      case LIGHT->{
      themeButton.setText("Switch to Light");
      ViewHandler.setStyle(Style.DARK);
      }
    }
  }
  public void onDining()
  {
    ViewHandler.showView(ViewType.DINING_CUSTOMER);
  }
}
