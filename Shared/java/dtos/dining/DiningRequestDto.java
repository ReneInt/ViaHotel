package dtos.dining;

import dtos.time.DateDto;
import dtos.time.HourDto;

import java.io.Serializable;

public record DiningRequestDto(int id,DateDto date, HourDto hour, String email) implements Serializable
{
  public DiningRequestDto(DateDto dateDto, HourDto hourDto, String email)
  {
    this(0,dateDto,hourDto,email);
  }

  @Override public String toString()
  {
    return "DiningRequestDto id"+id +" date= " + date + ", hour= " + hour + ", email= "
        + email + '\n';
  }
}
