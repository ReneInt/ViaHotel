package via.sep2.model.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import via.sep2.model.people.Customer;
import via.sep2.model.time.Date;
import via.sep2.model.time.DateImplementation;

import static org.junit.jupiter.api.Assertions.*;

class SimpleTest
{
  private Simple reservation;
  private Customer customer;
  private Date startDate;
  private Date endDate;

  @BeforeEach
  void setUp() {
    customer = new Customer("John", "Doe", "john@example.com");
    startDate = new DateImplementation(2025, 5, 1);
    endDate = new DateImplementation(2025, 5, 10);
    reservation = new Simple(1, customer, startDate, endDate, 101);
  }

  @Test void getFinalPrice()
  {
    reservation.setFinalPrice(450.0);
    assertEquals(450.0, reservation.getFinalPrice());
  }

  @Test void setFinalPrice()
  {
    reservation.setFinalPrice(600.0);
    assertEquals(600.0, reservation.getFinalPrice());
  }

  @Test void testToString()
  {
    reservation.setFinalPrice(400.0);
    String output = reservation.toString();
    assertTrue(output.contains("Reservation for a simple room:"));
    assertTrue(output.contains("Final price: 400.0"));
  }

  @Test void setRoom()
  {
    reservation.setRoom(202);
    assertEquals(202, reservation.getRoomNumber());
  }

  @Test void getRoomNumber()
  {
    assertEquals(101, reservation.getRoomNumber());
  }

  @Test void setCustomer()
  {
    Customer newCustomer = new Customer("Alice", "Smith", "alice@example.com");
    reservation.setCustomer(newCustomer);
    assertEquals(newCustomer, reservation.getCustomer());
  }

  @Test void testSetCustomer()
  {
    reservation.setCustomer("Bob", "Brown", "bob@example.com");
    Customer updated = reservation.getCustomer();
    assertEquals("Bob", updated.getFirstName());
    assertEquals("Brown", updated.getLastName());
    assertEquals("bob@example.com", updated.getEmail());
  }

  @Test void setStartDate()
  {
    Date newStart = new DateImplementation(2025, 6, 1);
    reservation.setStartDate(newStart);
    assertEquals(newStart, reservation.getStartDate());
  }

  @Test void testSetStartDate()
  {
    DateImplementation date = new DateImplementation(2025, 6, 2);
    DateImplementation differentDate = new DateImplementation(2025, 6, 2);
    reservation.setStartDate(date);
    assertEquals(differentDate, reservation.getStartDate());
  }

  @Test void setEndDate()
  {
    Date newEnd = new DateImplementation(2025, 6, 10);
    reservation.setEndDate(newEnd);
    assertEquals(newEnd, reservation.getEndDate());
  }

  @Test void testSetEndDate()
  {
    DateImplementation date = new DateImplementation(2025, 6, 2);
    DateImplementation differentDate = new DateImplementation(2025, 6, 2);
    reservation.setEndDate(date);
    assertEquals(differentDate, reservation.getEndDate());
  }

  @Test void getCustomer()
  {
    assertEquals(customer, reservation.getCustomer());
  }

  @Test void getReservation()
  {
    assertNull(reservation.getReservation());
  }

  @Test void getPriceModifier()
  {
    reservation.setPriceModifier(1.2);
    assertEquals(1.2, reservation.getPriceModifier());
  }

  @Test void setPriceModifier()
  {
    reservation.setPriceModifier(1.5);
    assertEquals(1.5, reservation.getPriceModifier());
  }

  @Test void getEndDate()
  {
    assertEquals(endDate, reservation.getEndDate());
  }

  @Test void getStartDate()
  {
    assertEquals(startDate, reservation.getStartDate());
  }

  @Test void getId()
  {
    assertEquals(1, reservation.getId());
  }

  @Test void testEquals()
  {
    Simple same = new Simple(1, customer, startDate, endDate, 101);
    Simple different = new Simple(2, customer, startDate, endDate, 102);
    assertEquals(reservation, same);
    assertNotEquals(reservation, different);
  }
}
