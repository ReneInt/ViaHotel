package via.sep2.services.autentification;

import dtos.auth.LoginRequest;
import dtos.auth.RegisterUserRequest;
import dtos.auth.UpdatePasswordRequest;
import dtos.person.PersonDataDto;

import java.sql.SQLException;

public interface AuthenticationService
{
  void registerUser(RegisterUserRequest request) throws SQLException;
  PersonDataDto login(LoginRequest request) throws SQLException;
  void updatePassword(UpdatePasswordRequest request) throws SQLException;
}
