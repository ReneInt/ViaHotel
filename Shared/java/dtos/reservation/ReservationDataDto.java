package dtos.reservation;

import dtos.time.DateDto;
import types.ReservationType;

import java.io.Serializable;

public record ReservationDataDto(int id,ReservationType type, String email, DateDto startDate, DateDto endDate, int roomNumber,int priceModifier)implements
    Serializable
{


  public ReservationDataDto(int id,ReservationType type, String email, DateDto startDate, DateDto endDate, int roomNumber)
  {
    this(id,type,email,startDate,endDate,roomNumber,0);
  }
  @Override public String toString()
  {
    return "ReservationDataDto  id= " + id + ", type= " + type + ", email= "
        + email + '\'' + ", startDate= " + startDate + ", endDate= " + endDate
        + ", roomNumber= " + roomNumber + "price modifier: "+priceModifier+ '\n';
  }
}
