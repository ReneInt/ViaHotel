package via.sep2.persistence.daos;

import dtos.person.PersonDataDto;
import types.PersonType;

import java.sql.*;
public class LoginDAOImpl implements LoginDAO
{
  private static LoginDAOImpl instance;

  private LoginDAOImpl() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  public static LoginDAOImpl getInstance() throws SQLException
  {
    //making it a singleton
    if (instance == null)
    {
      synchronized (LoginDAOImpl.class)
      {
        //MultiThread safety
        if (instance == null)
        {
          instance = new LoginDAOImpl();
        }
      }
    }
    return instance;
  }

  /**
   * @param firstName
   * @param lastName
   * @param email
   * @param password
   * @param personType
   * @return
   * @throws SQLException
   */
  @Override public PersonDataDto register(String firstName, String lastName,
      String email, String password, PersonType personType) throws SQLException
  {
    //we make the connection is closed by getting the connection in a try block
    try (Connection connection = ConnectionUtil.getConnection())
    {
      PreparedStatement seeUserInLogin= connection.prepareStatement("SELECT email FROM Users WHERE email LIKE ?");
      seeUserInLogin.setString(1,email.toLowerCase());
      ResultSet resultSet=seeUserInLogin.executeQuery();
      if (resultSet!=null && resultSet.next())
      {
        String temp = resultSet.getString("email");
        if(temp.toLowerCase().equals(email.toLowerCase()))
        {
        throw new SQLException("Person already register, please log in");
        }
      }
      PersonDataDto person=(PersonDAOImpl.getInstance().readByEmail(email.toLowerCase()));
      if (person==null||!person.email().toLowerCase().equals(email.toLowerCase()))
      {
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO Person (first_name, last_Name, email, position ) VALUES(?,?,?,?)");
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, email.toLowerCase());
        statement.setString(4, personType.toString());
        try
        {
          statement.executeUpdate();
        }
        catch (SQLException e)
        {
          throw new SQLException("Email already exists in the Database");
        }
      }
      PreparedStatement statement1 = connection.prepareStatement(
          "INSERT INTO Users (email,password) VALUES(?,?)");
      statement1.setString(1, email.toLowerCase());
      statement1.setString(2, password);
      statement1.executeUpdate();
      return new PersonDataDto(firstName,lastName,email.toLowerCase(),personType);
    }
  }

  /**
   * @param email
   * @param password
   * @return
   * @throws SQLException
   */
  @Override public PersonDataDto login(String email, String password)
      throws SQLException
  {
    //we make the connection is closed by getting the connection in a try block
    try (Connection connection = ConnectionUtil.getConnection())
    {
      ResultSet resultSetLogin;
      PreparedStatement login = connection.prepareStatement(
          "SELECT * FROM users WHERE email LIKE ?");
      login.setString(1, email.toLowerCase());
      try
      {
        resultSetLogin = login.executeQuery();
      }
      catch (SQLException e)
      {
        throw new SQLException("Email not registered, please register instead");
      }
      if (resultSetLogin == null)
      {
        throw new SQLException("Email not registered, please register instead");
      }
      if (resultSetLogin.next())
      {
        String tempPass = resultSetLogin.getString("password");
        if (!tempPass.equals(password))
        {
          throw new SQLException("Password is incorrect, try again");
        }
        else
        {
          PreparedStatement statement = connection.prepareStatement(
              "SELECT * FROM Person WHERE email=?");
          statement.setString(1, email.toLowerCase());
          ResultSet resultSet = statement.executeQuery();
          if (resultSet.next())
          {
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String personTypeString= resultSet.getString("position");
            PersonType personType = PersonType.valueOf(personTypeString.toUpperCase());
            return new PersonDataDto(firstName,lastName,email,personType);
          }
        }
      }
      throw new SQLException("Email not registered, please register instead");
    }
  }

  /**
   * @param email
   * @param oldPassword
   * @param newPassword
   * @throws SQLException
   */
  @Override public void updatePassword(String email, String oldPassword,
      String newPassword) throws SQLException
  {
    //we make the connection is closed by getting the connection in a try block
    try (Connection connection = ConnectionUtil.getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE Users SET password=? WHERE email=? AND password=?");
      statement.setString(1, newPassword);
      statement.setString(2, email.toLowerCase());
      statement.setString(3, oldPassword);
      statement.executeUpdate();
    }
  }

  /**
   * Deletes the login
   *
   * @param email
   * @throws SQLException
   */
  @Override public void delete(String email, String password)
      throws SQLException
  {
    try (Connection connection = ConnectionUtil.getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "DELETE FROM Users WHERE email = ? AND password=?");
      statement.setString(1, email.toLowerCase());
      statement.setString(2, password);

      int affected = statement.executeUpdate();
      if (affected == 0)
      {
        throw new SQLException(
            "No login found with email: " + email.toLowerCase());
      }
    }
  }
}
