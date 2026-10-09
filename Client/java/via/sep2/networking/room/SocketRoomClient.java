package via.sep2.networking.room;

import dtos.Request;
import dtos.reservation.ReservationDataDto;
import dtos.reservation.ReservationRoomTypeDto;
import via.sep2.networking.SocketService;

public class SocketRoomClient implements RoomClient
{
  @Override public void updatePrice(ReservationRoomTypeDto payload)
  {
    Request request = new Request("room", "updatePrice", payload);
    SocketService.sendRequest(request);
  }
}
