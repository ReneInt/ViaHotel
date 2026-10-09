package via.sep2.model.time;
import org.junit.jupiter.api.Test;
import via.sep2.model.time.HourImplementation;

import static org.junit.jupiter.api.Assertions.*;
public class HourTest {

  private HourImplementation hour;

  @Test
  public void testConstructorWithOneParameter() {
    hour = new HourImplementation(10);
    assertEquals(new HourImplementation(10, 0), hour);
  }

  @Test
  public void testConstructorWithTwoParameters() {
    hour = new HourImplementation(12, 30);
    assertEquals(new HourImplementation(12, 30), hour);
  }

  @Test
  public void testSetHourOnly() {
    hour = new HourImplementation(0);
    hour.setHour(15);
    assertEquals(new HourImplementation(15, 0), hour);
  }

  @Test
  public void testSetHourWithMinutes() {
    hour = new HourImplementation(0);
    hour.setHour(8, 45);
    assertEquals(new HourImplementation(8, 45), hour);
  }

  @Test
  public void testGetHourReturnsSameObject() {
    hour = new HourImplementation(5, 20);
    assertSame(hour, hour.getHour());
  }

  @Test
  public void testToStringFormat() {
    hour = new HourImplementation(7, 5);
    String expected = "07:05";
    String actual = String.format("%02d:%02d", 7, 5);
    assertEquals(expected, actual);
  }

  @Test
  public void testEqualsSameTime() {
    HourImplementation h1 = new HourImplementation(10, 15);
    HourImplementation h2 = new HourImplementation(10, 15);
    assertEquals(h1, h2);
  }

  @Test
  public void testEqualsDifferentTime() {
    HourImplementation h1 = new HourImplementation(10, 15);
    HourImplementation h2 = new HourImplementation(11, 15);
    assertNotEquals(h1, h2);
  }

  @Test
  public void testEqualsWithNull() {
    hour = new HourImplementation(10, 10);
    assertNotEquals(null, hour);
  }

  @Test
  public void testEqualsWithDifferentClass() {
    hour = new HourImplementation(10, 10);
    assertNotEquals(hour, "10:10");
  }

  @Test
  public void testInvalidHourTooHigh() {
    hour = new HourImplementation(0);
    assertThrows(IllegalArgumentException.class, () -> {
      hour.setHour(25);
    });
  }

  @Test
  public void testInvalidHourTooLow() {
    hour = new HourImplementation(0);
    assertThrows(IllegalArgumentException.class, () -> {
      hour.setHour(-1);
    });
  }

  @Test
  public void testInvalidMinuteTooHigh() {
    hour = new HourImplementation(0);
    assertThrows(IllegalArgumentException.class, () -> {
      hour.setHour(12, 61);
    });
  }

  @Test
  public void testInvalidMinuteTooLow() {
    hour = new HourImplementation(0);
    assertThrows(IllegalArgumentException.class, () -> {
      hour.setHour(12, -5);
    });
  }
}

