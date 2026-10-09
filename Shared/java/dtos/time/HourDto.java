package dtos.time;

import java.io.Serializable;

public record HourDto(int hours, int minute) implements Serializable
{
  public static HourDto justHour(int hour)
  {
    return new HourDto(hour, 0);
  }
  @Override
  public String toString() {
    return String.format( "%02d:%02d" , hours , minute);
  }
}
