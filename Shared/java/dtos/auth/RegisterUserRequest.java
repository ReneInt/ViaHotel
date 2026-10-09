package dtos.auth;

import types.PersonType;

import java.io.Serializable;
/**
 * @author Troels
 */
public record RegisterUserRequest(String email, String password, String firstName, String lastName, PersonType position) implements Serializable
{
  @Override
  public String toString()
  {
    return "RegisterUserRequest{" +
        "email='" + email + '\'' +
        ", password='" + password + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        "position='"+position+
        '}';
  }
}