package via.sep2.networking.room;

import dtos.reservation.ReservationRoomTypeDto;

public interface RoomClient
{
  void updatePrice(ReservationRoomTypeDto payload);
}
