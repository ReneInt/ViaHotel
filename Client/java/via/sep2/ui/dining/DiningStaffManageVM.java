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
 * @author Mario, Rodrigo
 */
public class DiningStaffManageVM implements PropertyChangeListener
{
  private final StringProperty emailProp = new SimpleStringProperty();
  private final DiningClient diningClient;
  private final StringProperty messageProp = new SimpleStringProperty();
  private final StringProperty errorLabel = new SimpleStringProperty("");
  private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
  BooleanProperty isSelected = new SimpleBooleanProperty();
  private DiningRequestDto currentDto;
  private final ObjectProperty<HourValues> hourValuesProp = new SimpleObjectProperty<>();
  private final BooleanProperty disableReserveButton = new SimpleBooleanProperty(
      true);
  private final BooleanProperty updateCancel = new SimpleBooleanProperty(
      true);
  private final ObservableList<DiningRequestDto> dining = FXCollections.observableArrayList();

  public DiningStaffManageVM(DiningClient diningClient)
  {
    isSelected.set(false);
    this.diningClient = diningClient;
    date.addListener(this::updateDiningButtonState);
    isSelected.addListener(this::updateDiningButtonState);
    ClientData.addPropertyChangeListener(this);
    isSelected.addListener(this::enableCancelButtonProp);
  }

  public StringProperty messageProperty()
  {
    return messageProp;
  }

  public BooleanProperty enableReserveButtonProp()
  {
    return disableReserveButton;
  }

  public ObjectProperty<HourValues> hourValuesProperty()
  {
    return hourValuesProp;
  }

  public ObjectProperty<LocalDate> dateProperty()
  {
    return date;
  }

  public ObservableList<DiningRequestDto> getDining()
  {
    return dining;
  }

  public void searchDiningForEmail()
  {
    ArrayList<DiningRequestDto> currentList = new ArrayList<>();
    List<DiningRequestDto> templist = ClientData.getDining();
    for (DiningRequestDto temp : templist)
    {
      if (temp.email().contains(emailProp.get()))
      {
        currentList.add(temp);
      }
    }
    try
    {
      dining.setAll(currentList);
      messageProp.set(null);
    }
    catch (Exception e)
    {
      messageProp.set(e.getMessage());
    }
  }

  private void updateDiningButtonState(Observable observable)
  {
    boolean disable = true;
    if (currentDto != null)
    {
      if (date.get() != null)
      {
        if (LocalDate.now().isAfter(date.get()))
        {
          messageProp.set("Dining can not be made in the past");
        }
        else if (date.get() != null)
        {
          messageProp.set("");
          {
            disable = hourValuesProp.get() == null || !isSelected.getValue();
          }
        }
      }
      disableReserveButton.set(disable);
    }
  }

  public void clearFields()
  {
    date.set(null);
    hourValuesProp.set(null);
  }

  public DiningRequestDto onPressedItem(DiningRequestDto selectedItem)
  {
    currentDto = selectedItem;
    date.set(Utilities.toLocalDate(currentDto.date()));
    hourValuesProp.set(
        HourValues.from(currentDto.hour().hours(), currentDto.hour().minute()));
    isSelected.set(true);
    return selectedItem;
  }

  public void onReserve()
  {
    messageProp.set(""); // clean potential existing message

    // validate all input is present
    if (date.get() == null)
    {
      messageProp.set("The date cannot be empty");
      return;
    }
    if (hourValuesProp.get() == null)
    {
      messageProp.set("Please select what hour you would like to go in");
      return;
    }
    DateDto diningDate = new DateDto(date.get().getYear(),
        date.get().getMonthValue(), date.get().getDayOfMonth());
    DiningRequestDto request = new DiningRequestDto(currentDto.id(), diningDate,
        new HourDto(hourValuesProp.get().getHour(),
            hourValuesProp.get().getMinute()), currentDto.email());
    try
    {
      diningClient.edit(request);
      clearFields();
      messageProp.set("Success");
    }
    catch (Exception e)
    {
      messageProp.set(e.getMessage());
    }
  }

  public void onCancel()
  {
    diningClient.cancelDining(currentDto);
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
            errorLabel.set("");
            List<DiningRequestDto> updatedDining = (List<DiningRequestDto>) newValue;
            List<DiningRequestDto> userDining = new ArrayList<>();
            for (DiningRequestDto dining : updatedDining)
            {
              if (emailProp.get() == null)
                userDining = updatedDining;
              else if (dining.email().contains(emailProp.get()))
              {
                userDining.add(dining);
              }
            }
            if (userDining.isEmpty())
            {
              throw new RuntimeException("ERROR 404 DATA NOT FOUND");
            }
            dining.setAll(userDining);
            messageProp.set("");
          }
          catch (Exception e)
          {
            errorLabel.set(e.getMessage());
            messageProp.set("Failed to update dining: " + e.getMessage());
            dining.clear();
          }
        }
        else
        {
          errorLabel.set("ERROR 404 DATA NOT FOUND");
          messageProp.set("Failed to refresh dining: Invalid data received");
          dining.clear();
        }
      });
    }
  }

  public Property<String> emailProp()
  {
    return emailProp;
  }

  public void loadDining()
  {
    try
    {
      if(ClientData.getDining().isEmpty())
      {
        throw new RuntimeException("ERROR 404 DATA NOT FOUND");
      }
      dining.setAll(ClientData.getDining());

      messageProp.set(null);
    }
    catch (Exception e)
    {
      errorLabel.set(e.getMessage());
      ViewHandler.popupMessage(MessageType.WARNING,e.getMessage());
      messageProp.set(e.getMessage());
    }
  }
  public void enableCancelButtonProp(Observable observable)
  {
    updateCancel.set(!isSelected.get());
  }
  public BooleanProperty updateCancel()
  {
    return updateCancel;
  }

  public StringProperty errorProperty()
  {
    return errorLabel;
  }
}