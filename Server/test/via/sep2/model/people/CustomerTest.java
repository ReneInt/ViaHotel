package via.sep2.model.people;

import org.junit.jupiter.api.Test;
import via.sep2.model.reservation.Reservation;
import via.sep2.model.room.Room;
import via.sep2.model.room.SmallRoom;
import via.sep2.model.time.Date;
import via.sep2.model.time.DateImplementation;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest
{

  @Test void testToString()
  {
    Customer customer=new Customer("Janusz","zloty","cos@gmail.com");
    assertEquals("Customer Janusz zloty cos@gmail.com",customer.toString());
  }

  @Test void getFirstName()
  {
    Customer customer=new Customer("Janusz","zloty","cos@gmail.com");
    assertEquals("Janusz", customer.getFirstName());
  }

  @Test void setFirstName()
  {
    Customer customer=new Customer(null,null,null);
    customer.setFirstName("Janusz");
    assertEquals("Janusz",customer.getFirstName());
  }

  @Test void getLastName()
  {
    Customer customer=new Customer("Janusz","zloty","cos@gmail.com");
    assertEquals("zloty", customer.getLastName());
  }

  @Test void setLastName()
  {
    Customer customer=new Customer(null,null,null);
    customer.setLastName("zloty");
    assertEquals("zloty",customer.getLastName());
  }

  @Test void getEmail()
  {
    Customer customer=new Customer("Janusz","zloty","cos@gmail.com");
    assertEquals("cos@gmail.com", customer.getEmail());
  }

  @Test void setEmail()
  {
    Customer customer=new Customer(null,null,null);
    customer.setEmail("cos@gmail.com");
    assertEquals("cos@gmail.com",customer.getEmail());
  }
}