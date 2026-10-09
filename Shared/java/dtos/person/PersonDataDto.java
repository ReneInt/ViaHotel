package dtos.person;

import types.PersonType;

import java.io.Serializable;
/**
 * @author Mario
 */
public record PersonDataDto(String firstName, String lastName, String email, PersonType position) implements Serializable
{
  @Override public String toString()
  {
    return "PersonDataDto " + "firstName='" + firstName + '\'' + ", lastName='"
        + lastName + '\'' + ", email='" + email + '\'' + ", position="
        + position + '\n';
  }
}
