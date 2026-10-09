package via.sep2.ui.dining;

import dtos.dining.DiningRequestDto;
import dtos.time.DateDto;
import dtos.time.HourDto;
import javafx.beans.Observable;
import javafx.beans.property.*;
import types.HourValues;
import via.sep2.networking.dining.DiningClient;
import via.sep2.data.AppState;

import java.time.LocalDate;
/**
 * @author Rodrigo
 */
public class DiningForCustomersVM
{
  private final StringProperty messageProp = new SimpleStringProperty();
  private final ObjectProperty<LocalDate> diningDate = new SimpleObjectProperty<>();
  private final ObjectProperty<DateDto> dateDTO = new SimpleObjectProperty<>();
  private final ObjectProperty<HourValues> hourProp = new SimpleObjectProperty<>();
  private final BooleanProperty disableReserveButton = new SimpleBooleanProperty(
      true);
  private final DiningClient diningClient;

  public DiningForCustomersVM (DiningClient diningClient) {
    this.diningClient = diningClient;
    this.hourProp.addListener(this::updateReserveButtonState);
    this.diningDate.addListener(this::updateReserveButtonState);
  }

  public StringProperty messageProperty()
  {
    return messageProp;
  }

  public ObjectProperty<LocalDate> diningDateProperty() {
    return diningDate;
  }

  public ObjectProperty<HourValues> hourProperty() {
    return hourProp;
  }

  public void onReserve() {
    messageProp.set("");

    if(diningDate.get() == null) {
      messageProp.set("The dining date cannot be empty");
      return;
    }

    if(hourProp == null) {
      messageProp.set("Please select what time you would like to make your reservation");
    }
    DateDto date = new DateDto(diningDate.get().getYear(),diningDate.get().getMonthValue(),diningDate.get().getDayOfMonth());
    DiningRequestDto request = new DiningRequestDto(0,date,
        new HourDto(hourProp.get().getHour(),hourProp.get().getMinute()),
        AppState.getCurrentUser().email());
    try
    {
      diningClient.create(request);
      clearFields();
      messageProp.set("Success");
    }
    catch (Exception e)
    {
      messageProp.set(e.getMessage());
    }
  }

  public void clearFields()
  {
    diningDate.set(null);
    hourProp.set(null);
  }


  private void updateReserveButtonState(Observable observable)
  {
    boolean disable=true;
    if(diningDate.get()!=null){
    if(LocalDate.now().isAfter(diningDate.get()))
    {
      messageProp.set("Dining can not be made in the past");
    }
    else if (diningDate.get() != null)
    {
      messageProp.set("");
       disable= hourProp.get() == null;
    }
  }
    disableReserveButton.set(disable);
  }

  public BooleanProperty enableReserveButtonProp()
  {
    return disableReserveButton;
  }
}
