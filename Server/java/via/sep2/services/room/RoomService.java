package via.sep2.services.room;

import dtos.reservation.ReservationRoomTypeDto;
import types.RoomType;
import via.sep2.model.room.Room;

import java.sql.SQLException;
import java.util.List;

public interface RoomService
{
  void updatePrice(ReservationRoomTypeDto dto);
}
