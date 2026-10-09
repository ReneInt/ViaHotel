package via.sep2.model.people;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest
{

  @Test void testToString()
  {
    Manager manager=new Manager("Janusz","zloty","cos@gmail.com");
    assertEquals("Manager Janusz zloty cos@gmail.com",manager.toString());
  }
}