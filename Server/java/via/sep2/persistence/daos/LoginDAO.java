package via.sep2.persistence.daos;

import dtos.person.PersonDataDto;
import types.PersonType;

import java.sql.SQLException;
public interface LoginDAO
{
  PersonDataDto register(String firstName, String lastName, String email, String password, PersonType personType)throws SQLException;
  PersonDataDto login (String email, String password)throws SQLException;
  void updatePassword(String email,String oldPassword,String newPassword)throws SQLException;
  void delete (String email, String password) throws SQLException;
}
