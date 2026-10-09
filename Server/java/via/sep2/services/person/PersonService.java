package via.sep2.services.person;

import dtos.person.PersonDataDto;
import types.PersonType;
import via.sep2.model.people.Person;

import java.sql.SQLException;
import java.util.List;

public interface PersonService
{
  PersonDataDto create(PersonDataDto payload) throws
      SQLException;
  PersonDataDto update(PersonDataDto payload) throws SQLException;
  void delete(PersonDataDto payload) throws SQLException;
  PersonDataDto readByEmail(PersonDataDto payload) throws SQLException;
  List<PersonDataDto> getPeople() throws SQLException;
}
