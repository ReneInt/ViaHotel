package via.sep2.networking.requesthandlers;

import dtos.reservation.ReservationRoomTypeDto;
import via.sep2.networking.FavorReaders;
import via.sep2.networking.exceptions.InvalidActionException;
import via.sep2.services.exceptions.ValidationException;
import via.sep2.services.room.RoomService;

public class RoomRequestHandler implements RequestHandler
{
  private final RoomService roomService;
  private final FavorReaders favorReaders=FavorReaders.getInstance();
  public RoomRequestHandler(RoomService roomService)
  {
    this.roomService = roomService;
  }
  public Object handle(String action, Object payload)
  {
    // Some methods can return something, some do not. Logging in? If no exceptions are thrown, success could be assumed.
    // Here, though, I explicitly return "OK", just as an example. I don't really use it.
    try
    {
      switch (action)
      {
        case "updatePrice"->{
          favorReaders.acquireWrite();
          roomService.updatePrice((ReservationRoomTypeDto) payload);
          favorReaders.releaseWrite();
        }
        default -> throw new InvalidActionException("room", action);
      }
    }
    catch (Exception e)
    {
      favorReaders.releaseWrite();
      favorReaders.releaseRead();
      throw new ValidationException(e.getMessage());
    }
    return null; // just a default return value. Some actions above may return stuff.
  }
}
