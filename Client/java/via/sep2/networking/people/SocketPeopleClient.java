package via.sep2.networking.people;

import dtos.Request;
import dtos.dining.DiningRequestDto;
import dtos.person.PersonDataDto;
import via.sep2.networking.SocketService;

import java.sql.SQLException;
import java.util.List;

public class SocketPeopleClient implements PeopleClient
{
  @Override public PersonDataDto create(PersonDataDto payload)
  {
    Request request = new Request("person", "create", payload);
    PersonDataDto response = (PersonDataDto) SocketService.sendRequest(request);
    return response;
  }

  @Override public PersonDataDto update(PersonDataDto payload)
  {
    Request request = new Request("person", "update", payload);
    PersonDataDto response = (PersonDataDto) SocketService.sendRequest(request);
    return response;
  }

  @Override public void delete(PersonDataDto payload)
  {
    Request request = new Request("person", "delete", payload);
    SocketService.sendRequest(request);
  }

  @Override public PersonDataDto readByEmail(PersonDataDto payload)
  {
    Request request = new Request("person", "readByEmail", payload);
    PersonDataDto response = (PersonDataDto) SocketService.sendRequest(request);
    return response;
  }
}
