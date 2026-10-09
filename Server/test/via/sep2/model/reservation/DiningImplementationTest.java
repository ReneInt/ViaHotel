package via.sep2.model.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import types.PersonType;
import via.sep2.model.people.Customer;
import via.sep2.model.time.Date;
import via.sep2.model.time.DateImplementation;
import via.sep2.model.time.Hour;
import via.sep2.model.time.HourImplementation;

import static org.junit.jupiter.api.Assertions.*;

class DiningImplementationTest
{
  private DiningImplementation dining;
  private Customer customer;
  private Date diningDate;
  private Hour diningTime;

  @BeforeEach
  void setUp() {
    customer = new Customer("Jane", "Doe", "jane@example.com");
    diningDate = new DateImplementation(2025, 5, 15);
    diningTime = new HourImplementation(18,30);
    dining = new DiningImplementation(1,diningDate, diningTime, customer);
  }

  @Test void testEquals()
  {
    DiningImplementation same = new DiningImplementation(1,diningDate, diningTime,customer);
    DiningImplementation different = new DiningImplementation(1,new
         DateImplementation(2025, 5, 15),
        new HourImplementation(19, 0),new Customer("Alice", "Smith", "alice@example.com"));

    assertEquals(dining, same);
    assertNotEquals(dining, different);
  }

  @Test void testToString()
  {
    String str = dining.toString();
    assertTrue(str.contains("jane@example.com"));
    assertTrue(str.contains("2025"));
    assertTrue(str.contains("18:30"));
  }

  @Test void getDiningTime()
  {
    assertEquals(diningTime, dining.getDiningTime());
  }

  @Test void setDiningTime()
  {
    Hour newTime = new HourImplementation(19, 0);
    dining.setDiningTime(newTime);
    assertEquals(newTime, dining.getDiningTime());
  }

  @Test void testSetDiningTime()
  {
    dining.setDiningTime(20, 15);
    assertEquals(new HourImplementation(20, 15), dining.getDiningTime());
  }

  @Test void testSetDiningTime1()
  {
    Hour newTime = new HourImplementation(21, 45);
    dining.setDiningTime(newTime);
    assertEquals(newTime, dining.getDiningTime());
  }

  @Test void getDiningDate()
  {
    assertEquals(diningDate, dining.getDiningDate());
  }

  @Test void setDiningDate()
  {
    Date newDate = new DateImplementation(2025, 6, 1);
    dining.setDiningDate(newDate);
    assertEquals(newDate, dining.getDiningDate());
  }

  @Test void setCustomer()
  {
    Customer newCustomer = new Customer("Mark", "Lee", "mark@example.com");
    dining.setCustomer(newCustomer);
    assertEquals(newCustomer, dining.getCustomer());
  }

  @Test void getCustomer()
  {
    assertEquals(customer, dining.getCustomer());
  }
}
