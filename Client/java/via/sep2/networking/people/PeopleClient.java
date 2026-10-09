package via.sep2.networking.people;

import dtos.person.PersonDataDto;

import java.sql.SQLException;
import java.util.List;
public interface PeopleClient {
  PersonDataDto create(PersonDataDto payload);
  PersonDataDto update(PersonDataDto payload);
  void delete(PersonDataDto payload);
  PersonDataDto readByEmail(PersonDataDto payload);
}
