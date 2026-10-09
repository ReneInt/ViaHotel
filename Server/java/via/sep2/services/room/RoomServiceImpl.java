package via.sep2.services.room;

import dtos.reservation.ReservationRoomTypeDto;
import via.sep2.persistence.daos.RoomDAO;
import via.sep2.services.exceptions.ValidationException;

import java.sql.SQLException;

public class RoomServiceImpl implements RoomService
{
  RoomDAO roomDAO;

  public RoomServiceImpl(RoomDAO roomDAO)
  {
    this.roomDAO = roomDAO;
  }

  @Override public void updatePrice(ReservationRoomTypeDto dto)
  {
    try
    {
      roomDAO.updatePrice(dto.roomType(), dto.finalPrice());
    }
    catch (SQLException e)
    {
      throw new ValidationException(e.getMessage());
    }
  }
}
