package via.sep2.ui.dining;

import dtos.dining.DiningRequestDto;
import dtos.person.PersonDataDto;
import dtos.time.DateDto;
import dtos.time.HourDto;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import types.HourValues;
import via.sep2.networking.dining.DiningClient;
import via.sep2.data.AppState;
import via.sep2.data.ClientData;
import via.sep2.startup.ViewHandler;
import via.sep2.ui.popup.MessageType;
import via.sep2.utils.Utilities;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mario
 */
public class DiningManageCustomerVM implements PropertyChangeListener
{
  private final BooleanProperty isSelected = new SimpleBooleanProperty();
  private final StringProperty messageProp = new SimpleStringProperty();
  private final StringProperty errorLabelProp = new SimpleStringProperty("");
  private final ObjectProperty<LocalDate> diningDate = new SimpleObjectProperty<>();
  private final ObjectProperty<HourValues> hourProp = new SimpleObjectProperty<>();
  private final BooleanProperty disableUpdateButton = new SimpleBooleanProperty(
      true);
  private final BooleanProperty disableCancelButton = new SimpleBooleanProperty(
      true);
  private final ObservableList<DiningRequestDto> dining = FXCollections.observableArrayList();
  private final DiningClient service;
  private DiningRequestDto currentDto;

  public DiningManageCustomerVM(DiningClient service)
  {
    isSelected.set(false);
    this.service = service;
    this.hourProp.addListener(this::updateUpdateButtonState);
    this.diningDate.addListener(this::updateUpdateButtonState);
    isSelected.addListener(this::updateUpdateButtonState);
    isSelected.addListener(this::enableCancelButtonProp);
    ClientData.addPropertyChangeListener(this);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if (evt.getPropertyName().equals("diningUpdate"))
    {
      Platform.runLater(() -> {
        PersonDataDto user = AppState.getCurrentUser();
        if (user == null)
        {
          messageProp.set("User is not authorized.");
          dining.clear();
          return;
        }
        Object newValue = evt.getNewValue();
        if (newValue instanceof List)
        {
          try
          {
            errorLabelProp.set("");
            List<DiningRequestDto> updatedReservations = (List<DiningRequestDto>) newValue;
            List<DiningRequestDto> userReservations = new ArrayList<>();
            for (DiningRequestDto dining : updatedReservations)
            {
              if (dining.email().equals(user.email()))
              {
                userReservations.add(dining);
              }
            }
            if (userReservations.isEmpty())
            {
              throw new RuntimeException("ERROR 404 DATA NOT FOUND");
            }
            dining.setAll(userReservations);
            messageProp.set("");
          }
          catch (Exception e)
          {
            ViewHandler.popupMessage(MessageType.WARNING,"Failed to update dining: " + e.getMessage());
            errorLabelProp.set(e.getMessage());
            dining.clear();
          }
        }
        else
        {
          ViewHandler.popupMessage(MessageType.WARNING,"Failed to refresh dining: Invalid data received");
          errorLabelProp.set("ERROR 404 DATA NOT FOUND");
          messageProp.set("Failed to refresh dining: Invalid data received");
          dining.clear();
        }
      });
    }
  }

  public StringProperty messageProperty()
  {
    return messageProp;
  }

  public ObjectProperty<LocalDate> diningDateProperty()
  {
    return diningDate;
  }

  public ObjectProperty<HourValues> hourProperty()
  {
    return hourProp;
  }

  public void onUpdate()
  {
    messageProp.set("");

    if (diningDate.get() == null)
    {
      messageProp.set("The dining date cannot be empty");
      return;
    }

    if (hourProp == null)
    {
      messageProp.set(
          "Please select what time you would like to make your reservation");
    }
    DateDto date = new DateDto(diningDate.get().getYear(),
        diningDate.get().getMonthValue(), diningDate.get().getDayOfMonth());
    DiningRequestDto request = new DiningRequestDto(currentDto.id(), date,
        new HourDto(hourProp.get().getHour(), hourProp.get().getMinute()),
        AppState.getCurrentUser().email());
    try
    {
      service.edit(request);
      clearFields();
      messageProp.set("Success");
    }
    catch (Exception e)
    {
      messageProp.set(e.getMessage());
    }
  }

  public ObservableList<DiningRequestDto> getDining()
  {
    return dining;
  }

  public void clearFields()
  {
    diningDate.set(null);
    hourProp.set(null);
  }

  private void updateUpdateButtonState(Observable observable)
  {
    boolean disable = true;
    if (diningDate.get() != null)
    {
      if (LocalDate.now().isAfter(diningDate.get()))
      {
        messageProp.set("Dining can not be made in the past");
      }
      else if (diningDate.get() != null)
      {
        messageProp.set("");
        disable = hourProp.get() == null;
      }
    }
    disableUpdateButton.set(disable);
  }

  public BooleanProperty enableUpdateButtonProp()
  {
    return disableUpdateButton;
  }

  public void loadDiningForCurrentUser()
  {
    PersonDataDto user = AppState.getCurrentUser();
    ArrayList<DiningRequestDto> currentList = new ArrayList<>();
    List<DiningRequestDto> templist = ClientData.getDining();
    for (DiningRequestDto temp : templist)
    {
      if (temp.email().equals(user.email()))
      {
        currentList.add(temp);
      }

    }
    if (user == null)
    {
      messageProp.set("User is not authorised.");
      return;
    }
    String email = user.email();

    try
    {
      if (currentList.isEmpty())
      {
        throw new RuntimeException("ERROR 404 DATA NOT FOUND");
      }
      dining.setAll(currentList);
      messageProp.set(null);
    }
    catch (Exception e)
    {
      errorLabelProp.set(e.getMessage());
      ViewHandler.popupMessage(MessageType.WARNING,e.getMessage());
      messageProp.set(e.getMessage());
    }
  }

  public DiningRequestDto onPressedItem(DiningRequestDto selectedItem)
  {
    currentDto = selectedItem;
    isSelected.set(true);
    diningDate.set(Utilities.toLocalDate(currentDto.date()));
    hourProp.set(
        HourValues.from(currentDto.hour().hours(), currentDto.hour().minute()));
    return selectedItem;
  }

  public void onCancel()
  {
    service.cancelDining(
        new DiningRequestDto(currentDto.id(), null, null, null));
  }

  public void enableCancelButtonProp(Observable observable)
  {
    disableCancelButton.set(!isSelected.get());
  }
  public BooleanProperty updateCancel()
  {
    return disableCancelButton;
  }

  public StringProperty errorProperty()
  {
    return errorLabelProp;
  }
}
