package via.sep2.persistence.daos;

import dtos.person.PersonDataDto;
import org.postgresql.Driver;
import types.PersonType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAOImpl implements PersonDAO
{
  private static PersonDAOImpl instance;
  //PersonDAOImpl is following the singleton design pattern.

  /**
   * Creates a PersonDAOImpl Object;
   *
   * @throws SQLException in case the driver is missing
   */
  private PersonDAOImpl() throws SQLException
  {
    DriverManager.registerDriver(new Driver());
  }

  /**
   * Singleton for PersonDAOImpl
   *
   * @return PersonDAOImpl
   * @throws SQLException in case something goes wrong
   */
  public static PersonDAOImpl getInstance() throws SQLException
  {
    //making it a singleton
    if (instance == null)
    {
      synchronized (PersonDAOImpl.class)
      {
        //MultiThread safety
        if (instance == null)
        {
          instance = new PersonDAOImpl();
        }
      }
    }
    return instance;
  }

  /***
   * Create a customer and add it to the Database
   * @param firstName String
   * @param lastName String
   * @param email String
   * @param position PersonType that shows which type of person object we are adding to the database
   * @throws SQLException if the data cannot be written in the database
   */

  @Override public PersonDataDto create(String firstName, String lastName,
      String email, PersonType position) throws SQLException
  {
    //we make the connection is closed by getting the connection in a try block
    try (Connection connection = ConnectionUtil.getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "INSERT INTO person (first_name, last_name, email ,position ) VALUES (?, ?, ?, ?) ;");
      statement.setString(1, firstName);
      statement.setString(2, lastName);
      statement.setString(3, email.toLowerCase());
      statement.setString(4, position.toString());
      statement.executeUpdate();
      return new PersonDataDto(firstName, lastName, email, position);
    }
  }

  @Override public PersonDataDto update(String email, String firstName,
      String lastName, PersonType position) throws SQLException
  {
    //we make the connection is closed by getting the connection in a try block
    try (Connection connection = ConnectionUtil.getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE Person SET first_name=?, last_name=?, position=? WHERE email=?");
      statement.setString(1, firstName);
      statement.setString(2, lastName);
      statement.setString(4, email.toLowerCase());
      statement.setString(3, position.toString());
      statement.executeUpdate();
      return new PersonDataDto(firstName, lastName, email, position);
    }
  }

  /**
   * @throws SQLException
   */
  @Override public void delete(String email) throws SQLException
  {
    //we make the connection is closed by getting the connection in a try block

    try (Connection connection = ConnectionUtil.getConnection())
    {
      PreparedStatement statementLog = connection.prepareStatement(
          "DELETE FROM Users WHERE email = ?");
      statementLog.setString(1, email.toLowerCase());
      PreparedStatement statement = connection.prepareStatement(
          "DELETE FROM Person WHERE email=?");
      statement.setString(1, email.toLowerCase());
      statement.executeUpdate();
    }
  }

  /**
   * @param email
   * @return
   */
  @Override public PersonDataDto readByEmail(String email) throws SQLException
  {
    //we make the connection is closed by getting the connection in a try block
    try (Connection connection = ConnectionUtil.getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM Person WHERE email LIKE ?");
      statement.setString(1, email.toLowerCase());
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String position = resultSet.getString("position");
        PersonType personType = PersonType.valueOf(position.toUpperCase());
        return new PersonDataDto(firstName, lastName, email, personType);
      }
    }
    return null;
  }

  @Override public List<PersonDataDto> getPeople() throws SQLException
  {
    List<PersonDataDto> people = new ArrayList<>();
    try (Connection connection = ConnectionUtil.getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM Person");
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next())
      {
        PersonType personType = PersonType.valueOf(
            resultSet.getString("position").toUpperCase());
        people.add(new PersonDataDto(resultSet.getString("first_name"),
            resultSet.getString("last_name"), resultSet.getString("email"),
            personType));
      }
    }
    return people;
  }
}
