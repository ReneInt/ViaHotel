package via.sep2.services.reservation;

import dtos.reservation.FinalPriceDto;
import dtos.reservation.ReservationDataDto;
import dtos.reservation.ReservationRoomTypeDto;
import dtos.time.DateDto;
import via.sep2.model.time.Date;
import via.sep2.model.time.DateImplementation;
import via.sep2.model.time.DateUtility;
import via.sep2.persistence.daos.ReservationDAO;
import via.sep2.persistence.daos.RoomDAOImpl;
import via.sep2.services.exceptions.ValidationException;

import java.sql.SQLException;
import java.util.List;

public class ReserveServiceImpl implements ReservationService
{
  private final ReservationDAO reservationDAO;
  public ReserveServiceImpl(ReservationDAO reservationDAO)
  {
    this.reservationDAO=reservationDAO;
  }
  @Override public ReservationDataDto create(ReservationDataDto payload)
  {
    DateDto startDate = payload.startDate();
    DateDto endDate = payload.endDate();
    Date start = new DateImplementation(startDate.year(), startDate.month(),
        startDate.day());
    Date end = new DateImplementation(endDate.year(), endDate.month(),
        endDate.day());
    ReservationDataDto reservation;
    try
    {
      reservation = reservationDAO.create(payload.type(), payload.email(), start,
          end, payload.roomNumber());
    }
    catch (SQLException e)
    {
      throw new ValidationException(e.getMessage());
    }
    return new ReservationDataDto(reservation.id(), payload.type(),
        reservation.email(), startDate, endDate,
        reservation.roomNumber());
  }
  @Override public ReservationRoomTypeDto create(ReservationRoomTypeDto payload)
  {
    {
      DateDto startDate = payload.startDate();
      DateDto endDate = payload.endDate();
      Date start = new DateImplementation(startDate.year(), startDate.month(),
          startDate.day());
      Date end = new DateImplementation(endDate.year(), endDate.month(),
          endDate.day());
      ReservationRoomTypeDto reservation;
      try
      {
        reservation = reservationDAO.create(payload.type(),
            payload.email(), start, end, payload.roomType());
      }
      catch (SQLException e)
      {
        throw new ValidationException(e.getMessage());
      }
      return new ReservationRoomTypeDto(payload.type(),
          reservation.email(), startDate, endDate,
          reservation.roomType());
    }
  }

  @Override public void edit(ReservationRoomTypeDto payload)
  {
    try
    {
      reservationDAO.cancelReservation(payload.id());
      create(payload);
    }
    catch (SQLException e)
    {
      throw new ValidationException(e.getMessage());
    }
  }

  @Override public FinalPriceDto calculateFinalPrice(ReservationRoomTypeDto payload)
  {
    Date startDate=new DateImplementation(payload.startDate().year(),payload.startDate().month(),payload.startDate().day());
    Date endDate=new DateImplementation(payload.endDate().year(),payload.endDate().month(),payload.endDate().day());
    int time;
    if (DateUtility.timeBetween(startDate,endDate)<0){
      throw new ValidationException("EndDate must be before startDate");
    }
    else if(DateUtility.timeBetween(startDate,endDate)==0)
    {
      time=1;
    }
    else {
      time=DateUtility.timeBetween(startDate,endDate);
    }
    double finalPrice;
    try
    {
      double price = reservationDAO.readPricePerType(payload.type());
      double roomPrice = RoomDAOImpl.getInstance().getRoomPrice(payload.roomType());
      finalPrice = price/100* roomPrice * time;
    }
    catch (SQLException e)
    {
      throw new ValidationException(e.getMessage());
    }
    if (finalPrice==0)
    {
      throw new ValidationException("Could not calculate final price");
    }
    return new  FinalPriceDto(finalPrice);
  }

  @Override public void changePrice(ReservationRoomTypeDto payload)
  {
    try
    {
      reservationDAO.updatePricing(payload.type(),payload.finalPrice());
    }
    catch (SQLException e)
    {
      throw new ValidationException(e.getMessage());
    }
  }

  @Override public List<ReservationRoomTypeDto> getAll()
  {
    try
    {
      return reservationDAO.getAllActiveReservations();
    }
    catch (SQLException e)
    {
      throw new ValidationException(e.getMessage());
    }
  }

  @Override public void cancelReservation(ReservationRoomTypeDto payload)
  {
    try
    {
      checkCancel(payload);
      reservationDAO.cancelReservation(payload.id());
    }
    catch (SQLException e)
    {
      throw new ValidationException(e.getMessage());
    }
  }
  private void checkCancel(ReservationRoomTypeDto payload)
  {
    Date start = new DateImplementation(payload.startDate().year(),payload.startDate().month(),payload.startDate().day());
    Date end= new DateImplementation(payload.endDate().year(),payload.endDate().month(),payload.endDate().day());
    Date today= DateUtility.today();
    if (DateUtility.timeBetween(today,end)<=0)
    {
      throw new ValidationException("Cannot cancel a past reservation");
    }
    else if(DateUtility.timeBetween(today,start)<=0)
    {
      throw new ValidationException("Cannot cancel an ongoing reservation");
    }
  }
}
