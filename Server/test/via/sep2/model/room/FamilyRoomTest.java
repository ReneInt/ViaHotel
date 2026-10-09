package via.sep2.model.room;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FamilyRoomTest
{
  private FamilyRoom room;

  @BeforeEach
  void setUp() {
    room = new FamilyRoom(101);
    room.setPrice(250.0);
  }

  @Test void getRoomNumber()
  {
    assertEquals(101, room.getRoomNumber());
  }

  @Test void setPrice()
  {
    room.setPrice(300.0);
    assertEquals(300.0, room.getPrice());
  }

  @Test void getPrice()
  {
    assertEquals(250.0, room.getPrice());
  }

  @Test void testToString()
  {
    String output = room.toString();
    assertTrue(output.contains("Room number: 101"));
    assertTrue(output.contains("Price: 250.0"));
    assertTrue(output.contains("Number of beds: 4"));
  }

  @Test void testEquals()
  {
    FamilyRoom sameRoom = new FamilyRoom(101);
    sameRoom.setPrice(250.0);

    FamilyRoom differentRoom = new FamilyRoom(102);
    differentRoom.setPrice(250.0);

    assertEquals(room, sameRoom);
    assertNotEquals(room, differentRoom);
  }
}
