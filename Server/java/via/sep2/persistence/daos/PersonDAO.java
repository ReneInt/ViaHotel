package via.sep2.persistence.daos;
import dtos.person.PersonDataDto;
import types.PersonType;
import via.sep2.model.people.Person;

import java.sql.SQLException;
import java.util.List;
//TODO REFACTOR TO USE Dto
public interface PersonDAO {
    PersonDataDto create(String firstName, String lastName, String email, PersonType position) throws SQLException;
    PersonDataDto update(String email,String firstName, String lastName, PersonType position) throws SQLException;
    void delete(String email) throws SQLException;
    PersonDataDto readByEmail(String email) throws SQLException;
    List<PersonDataDto> getPeople() throws SQLException;
}
