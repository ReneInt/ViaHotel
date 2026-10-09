package via.sep2.utils;

import dtos.time.DateDto;

import java.time.LocalDate;

public class Utilities
{
  public static LocalDate toLocalDate(DateDto dateDto) {
    if (dateDto == null) {
      return null;
    }
    return LocalDate.of(dateDto.year(), dateDto.month(), dateDto.day());
  }
}
