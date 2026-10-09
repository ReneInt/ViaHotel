package via.sep2.model.reservation;

import via.sep2.model.people.Person;
import via.sep2.model.time.Date;
import via.sep2.model.time.Hour;
import via.sep2.model.people.Customer;
/**
 * @deprecated This interface and implementations have been replaced by {@link dtos.dining.DiningRequestDto}.
 * <p>
 * The class is kept here to show how the project evolved and the progress the team made.
 * </p>
 * @author Group 3
 * @version 1.1
 */
public interface Dining
{

  Hour getDiningTime();

  void setDiningTime(int hour);
  void setDiningTime(Hour hour);
  void setDiningTime(int hour, int minutes);

  Date getDiningDate();

  void setDiningDate(Date date);

  void setCustomer(Person person);

  Person getCustomer();
  int getId();
  void setId(int id);
}
