package via.sep2.model.time;

import dtos.time.HourDto;

import java.sql.Time;
import java.util.Calendar;

public class HourUtil
{
  public static Time toSqlTime(HourImplementation hour) {
    if (hour == null) {
      throw new IllegalArgumentException("HourImplementation object cannot be null");
    }

    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, hour.getHourAsInt());
    calendar.set(Calendar.MINUTE, hour.getMinutes());
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    return new Time(calendar.getTimeInMillis());
  }

  public static HourImplementation fromSqlTime(Time time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(time);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    return new HourImplementation(hour, minute);
  }
  public static HourDto fromHourToDto (Hour hour)
  {
    return new HourDto(hour.getHourAsInt(),hour.getMinutes());
  }
  public static HourDto fromTimeToDto (Time time)
  {
    Hour hour = fromSqlTime(time);
    return new HourDto(hour.getHourAsInt(),hour.getMinutes());
  }
}
