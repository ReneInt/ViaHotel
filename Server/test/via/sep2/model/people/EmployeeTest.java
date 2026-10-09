package via.sep2.model.people;

import org.junit.jupiter.api.Test;
import via.sep2.model.reservation.Reservation;
import via.sep2.model.room.Room;
import via.sep2.model.room.SmallRoom;
import via.sep2.model.time.Date;
import via.sep2.model.time.DateImplementation;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest
{

  @Test void testToString()
  {
    Employee employee=new Employee("Janusz","zloty","cos@gmail.com");
    assertEquals("Employee Janusz zloty cos@gmail.com",employee.toString());
  }

  @Test void getFirstName()
  {
    Employee employee=new Employee("Janusz","zloty","cos@gmail.com");
    assertEquals("Janusz", employee.getFirstName());
  }

  @Test void setFirstName()
  {
    Employee employee=new Employee(null,null,null);
    employee.setFirstName("Janusz");
    assertEquals("Janusz",employee.getFirstName());
  }

  @Test void getLastName()
  {
    Employee employee=new Employee("Janusz","zloty","cos@gmail.com");
    assertEquals("zloty", employee.getLastName());
  }

  @Test void setLastName()
  {
    Employee employee=new Employee(null,null,null);
    employee.setLastName("zloty");
    assertEquals("zloty",employee.getLastName());
  }

  @Test void getEmail()
  {
    Employee employee=new Employee("Janusz","zloty","cos@gmail.com");
    assertEquals("cos@gmail.com", employee.getEmail());
  }

  @Test void setEmail()
  {
    Employee employee=new Employee(null,null,null);
    employee.setEmail("cos@gmail.com");
    assertEquals("cos@gmail.com",employee.getEmail());
  }
}