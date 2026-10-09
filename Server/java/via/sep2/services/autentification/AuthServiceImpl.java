package via.sep2.services.autentification;

import dtos.auth.LoginRequest;
import dtos.auth.RegisterUserRequest;
import dtos.auth.UpdatePasswordRequest;
import dtos.person.PersonDataDto;
import via.sep2.persistence.daos.LoginDAO;
import via.sep2.services.exceptions.ValidationException;

import java.sql.SQLException;

public class AuthServiceImpl implements AuthenticationService
{

  private final LoginDAO loginDAO;


  public AuthServiceImpl(LoginDAO loginDAO)
  {
    this.loginDAO = loginDAO;
  }


  @Override
  public void registerUser(RegisterUserRequest request) throws SQLException
  {

   validateName(request.firstName());
   validateName(request.lastName());
   validateEmailIsCorrectFormat(request.email());
   validatePasswordIsCorrectFormat(request.password());
   //writing to the database
   loginDAO.register(request.firstName(), request.lastName(), request.email(),
       request.password(),request.position());
  }

  private static void validatePasswordIsCorrectFormat(String password)
  {
    // validate password has correct format, e.g. upper case/lower case, symbols, numbers, etc
    if (password.length() < 8)
    {
      throw new ValidationException("Password must be 8 or more characters");
    }
    if (password.length() > 24)
    {
      throw new ValidationException("Password must be 24 or fewer characters");
    }
    // etc..
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

  @Override
  public PersonDataDto login(LoginRequest request) throws SQLException
  {
    return loginDAO.login(request.email(),request.password());
  }

  @Override
  public void updatePassword(UpdatePasswordRequest request) throws SQLException
  {
    validatePasswordIsCorrectFormat(request.newPassword());
    loginDAO.updatePassword(request.email(),request.oldPassword(),request.newPassword());
  }
}
