package via.sep2.model.room;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SmallRoomTest
{
  private SmallRoom room;

  @BeforeEach
  void setUp() {
    room = new SmallRoom(301);
    room.setPrice(90.0);
  }

  @Test void getRoomNumber()
  {
    assertEquals(301, room.getRoomNumber());
  }

  @Test void setPrice()
  {
    room.setPrice(100.0);
    assertEquals(100.0, room.getPrice());
  }

  @Test void getPrice()
  {
    assertEquals(90.0, room.getPrice());
  }

  @Test void testToString()
  {
    String output = room.toString();
    assertTrue(output.contains("Room number: 301"));
    assertTrue(output.contains("Price: 90.0"));
    assertTrue(output.contains("Number of beds: 1"));
  }

  @Test void testEquals()
  {
    SmallRoom sameRoom = new SmallRoom(301);
    sameRoom.setPrice(90.0);

    SmallRoom differentRoom = new SmallRoom(302);
    differentRoom.setPrice(90.0);

    assertEquals(room, sameRoom);
    assertNotEquals(room, differentRoom);
  }
}
