package via.sep2.services.person;

import dtos.person.PersonDataDto;
import via.sep2.persistence.daos.PersonDAO;
import via.sep2.services.exceptions.ValidationException;

import java.sql.SQLException;
import java.util.List;

public class PersonServiceImpl implements PersonService
{
  private final PersonDAO dao;

  public PersonServiceImpl(PersonDAO dao)
  {
    this.dao=dao;
  }

  @Override public PersonDataDto create(PersonDataDto payload) throws SQLException
  {
    validateName(payload.firstName());
    validateName(payload.lastName());
    validateEmailIsCorrectFormat(payload.email());
    return dao.create(payload.firstName(),
        payload.lastName(),payload.email(), payload.position());
  }

  @Override public PersonDataDto update(PersonDataDto payload) throws SQLException
  {
    validateName(payload.firstName());
    validateName(payload.lastName());
    validateEmailIsCorrectFormat(payload.email());
    return dao.update(payload.email(), payload.firstName(),
        payload.lastName(), payload.position());
  }

  @Override public void delete(PersonDataDto payload) throws SQLException
  {
    dao.delete(payload.email());
  }

  @Override public PersonDataDto readByEmail(PersonDataDto payload) throws SQLException
  {
    return dao.readByEmail(payload.email());
  }

  @Override public List<PersonDataDto> getPeople() throws SQLException
  {
    return dao.getPeople();
  }
  private static void validateName(String name) {
    if (name.matches(".*[^a-zA-Z\\s].*")) {//Checks if the name is valid, we used AI for this one because we didn't know.
      throw new ValidationException("Name must not contain numbers or special characters.");
    }
  }


  private static void validateEmailIsCorrectFormat(String email)
  {
    String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"; //Checks if the email is valid, we used AI for this one because we didn't know.

    if (!email.matches(emailRegex)) {
      throw new ValidationException("Invalid email format.");
    }
  }
}
