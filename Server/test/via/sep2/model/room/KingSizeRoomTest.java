package via.sep2.model.room;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KingSizeRoomTest
{
  private KingSizeRoom room;

  @BeforeEach
  void setUp() {
    room = new KingSizeRoom(201);
    room.setPrice(180.0);
  }

  @Test void getRoomNumber()
  {
    assertEquals(201, room.getRoomNumber());
  }

  @Test void setPrice()
  {
    room.setPrice(220.0);
    assertEquals(220.0, room.getPrice());
  }

  @Test void getPrice()
  {
    assertEquals(180.0, room.getPrice());
  }

  @Test void testToString()
  {
    String output = room.toString();
    assertTrue(output.contains("Room number: 201"));
    assertTrue(output.contains("Price: 180.0"));
    assertTrue(output.contains("Number of beds: 2"));
  }

  @Test void testEquals()
  {
    KingSizeRoom sameRoom = new KingSizeRoom(201);
    sameRoom.setPrice(180.0);

    KingSizeRoom differentRoom = new KingSizeRoom(202);
    differentRoom.setPrice(180.0);

    assertEquals(room, sameRoom);
    assertNotEquals(room, differentRoom);
  }
}
