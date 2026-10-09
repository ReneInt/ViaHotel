package via.sep2.model.time;

import org.junit.jupiter.api.Test;

import java.sql.Time;

import static org.junit.jupiter.api.Assertions.*;

class HourUtilTest
{

  @Test void toSqlTime()
  {
    HourImplementation hour = new HourImplementation(14, 30); // 2:30 PM
    Time time = HourUtil.toSqlTime(hour);

    assertNotNull(time);
    assertEquals("14:30:00", time.toString());
  }

  @Test void fromSqlTime()
  {
    Time sqlTime = Time.valueOf("08:45:00");
    HourImplementation hour = HourUtil.fromSqlTime(sqlTime);

    assertNotNull(hour);
    assertEquals(8, hour.getHourAsInt());
    assertEquals(45, hour.getMinutes());
  }
}
