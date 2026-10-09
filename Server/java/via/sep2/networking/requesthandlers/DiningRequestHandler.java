package via.sep2.networking.requesthandlers;

import dtos.dining.DiningRequestDto;
import dtos.reservation.FinalPriceDto;
import dtos.reservation.ReservationRoomTypeDto;
import via.sep2.networking.EventManager;
import via.sep2.networking.FavorReaders;
import via.sep2.networking.exceptions.InvalidActionException;
import via.sep2.services.dining.DiningService;
import via.sep2.services.exceptions.ValidationException;

import java.sql.SQLException;
import java.util.List;

public class DiningRequestHandler implements RequestHandler
{
  private final DiningService service;
  public DiningRequestHandler(DiningService service)
  {
    this.service =service;
  }
  private final FavorReaders favorReaders=FavorReaders.getInstance();

  @Override
  public Object handle(String action, Object payload) throws SQLException
  {
    // Some methods can return something, some do not. Logging in? If no exceptions are thrown, success could be assumed.
    // Here, though, I explicitly return "OK", just as an example. I don't really use it.
    try{
      switch (action)
      {
        case "create" ->
        {
          favorReaders.acquireWrite();
          DiningRequestDto response=service.create((DiningRequestDto) payload);
          favorReaders.releaseWrite();
          EventManager.fireUpdate("dining");
          return response;
        }
        case "edit" ->
        {
          favorReaders.acquireWrite();
         Object response=service.edit((DiningRequestDto) payload);
          favorReaders.releaseWrite();
          EventManager.fireUpdate("dining");
         return response;

        }
        case "cancel"->
        {
          favorReaders.acquireWrite();
          service.cancelDining((DiningRequestDto) payload);
          favorReaders.releaseWrite();
          EventManager.fireUpdate("dining");
        }
        case "getAll"->{
          favorReaders.acquireRead();
         List<DiningRequestDto>  response =service.getALL();
        favorReaders.releaseRead();
        return response;
        }
        default -> throw new InvalidActionException("dining", action);
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
