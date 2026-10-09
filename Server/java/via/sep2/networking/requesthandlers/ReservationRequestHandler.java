package via.sep2.networking.requesthandlers;

import dtos.reservation.FinalPriceDto;
import dtos.reservation.ReservationDataDto;
import dtos.reservation.ReservationRoomTypeDto;
import via.sep2.networking.EventManager;
import via.sep2.networking.exceptions.InvalidActionException;
import via.sep2.networking.FavorReaders;
import via.sep2.services.exceptions.ValidationException;
import via.sep2.services.reservation.ReservationService;

import java.util.ArrayList;

/**
 * @author Troels,Mario
 */
public class ReservationRequestHandler implements RequestHandler
{
  private final ReservationService reservationService;
  private final FavorReaders favorReaders=FavorReaders.getInstance();
  public ReservationRequestHandler(ReservationService reservationService)
  {
    this.reservationService = reservationService;
  }

  @Override
  public Object handle(String action, Object payload)
  {
    // Some methods can return something, some do not. Logging in? If no exceptions are thrown, success could be assumed.
    // Here, though, I explicitly return "OK", just as an example. I don't really use it.
    try
    {
      switch (action)
      {
        case "create" ->
        {
          ReservationDataDto reservationDataDto = null;
          ReservationRoomTypeDto reservationRoomTypeDto = null;
          if (payload instanceof ReservationRoomTypeDto)
          {
            favorReaders.acquireWrite();
            reservationRoomTypeDto=reservationService.create((ReservationRoomTypeDto)payload);
            favorReaders.releaseWrite();
          }
          if (payload instanceof ReservationDataDto)
          {
            favorReaders.acquireWrite();
            reservationDataDto=reservationService.create((ReservationDataDto) payload);
            favorReaders.releaseWrite();
          }
          EventManager.fireUpdate("reservations");
          if (reservationRoomTypeDto==null)
          {
          return reservationDataDto;
          }
          else
          {
            return reservationRoomTypeDto;
          }
        }
        case "price" ->
        { FinalPriceDto response=null;
          favorReaders.acquireRead();
          response=reservationService.calculateFinalPrice(
              (ReservationRoomTypeDto) payload);
          favorReaders.releaseRead();
          return response;
        }
        case "update"->{
          favorReaders.acquireWrite();
          reservationService.edit((ReservationRoomTypeDto) payload);
          favorReaders.releaseWrite();
          EventManager.fireUpdate("reservations");
        }
        case "getAll"->{
          favorReaders.acquireRead();
          Object response=reservationService.getAll();
          favorReaders.releaseRead();
          return response;
        }
        case "cancel"->{
          favorReaders.acquireWrite();
          reservationService.cancelReservation((ReservationRoomTypeDto) payload);
          favorReaders.releaseWrite();
          EventManager.fireUpdate("reservations");
        }
        case "updatePrice"->{
          favorReaders.acquireWrite();
          reservationService.changePrice((ReservationRoomTypeDto) payload);
          favorReaders.releaseWrite();
        }
        default -> throw new InvalidActionException("reservation", action);
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
