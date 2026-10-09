package dtos.reservation;

import dtos.time.DateDto;
import types.ReservationType;
import types.RoomType;

import java.io.Serializable;

public record ReservationRoomTypeDto(int id,ReservationType type, String email, DateDto startDate, DateDto endDate,RoomType roomType,double finalPrice)implements Serializable
{
  public ReservationRoomTypeDto(ReservationType type, String email,
      DateDto startDate, DateDto endDate, RoomType roomType)
  {
    this(0,type,email,startDate,endDate,roomType,0);
  }
}
