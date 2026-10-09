package via.sep2.networking.requesthandlers;

import dtos.dining.DiningRequestDto;
import dtos.person.PersonDataDto;
import via.sep2.networking.EventManager;
import via.sep2.networking.FavorReaders;
import via.sep2.networking.exceptions.InvalidActionException;
import via.sep2.services.dining.DiningService;
import via.sep2.services.exceptions.ValidationException;
import via.sep2.services.person.PersonService;

import java.sql.SQLException;
import java.util.List;

public class PeopleRequestHandler implements RequestHandler
{
  private final PersonService service;
  public PeopleRequestHandler(PersonService service)
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
          Object response=service.create((PersonDataDto) payload);
          favorReaders.releaseWrite();
          EventManager.fireUpdate("person");
          return response;
        }
        case "update" ->
        {
          favorReaders.acquireWrite();
          Object response=service.update((PersonDataDto) payload);
          favorReaders.releaseWrite();
          EventManager.fireUpdate("person");
          return response;

        }
        case "delete"->
        {
          favorReaders.acquireWrite();
          service.delete((PersonDataDto) payload);
          favorReaders.releaseWrite();
          EventManager.fireUpdate("person");
        }
        case "readByEmail"->{
          favorReaders.acquireRead();
          PersonDataDto response =service.readByEmail((PersonDataDto) payload);
          favorReaders.releaseRead();
          return response;
        }
        case "getAll"->{
          favorReaders.acquireRead();
          List<PersonDataDto> response =service.getPeople();
          favorReaders.releaseRead();
          return response;
        }
        default -> throw new InvalidActionException("person", action);
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
