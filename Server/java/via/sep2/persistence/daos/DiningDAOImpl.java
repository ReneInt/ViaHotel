package via.sep2.persistence.daos;

import dtos.dining.DiningRequestDto;
import dtos.person.PersonDataDto;
import dtos.time.DateDto;
import dtos.time.HourDto;
import via.sep2.model.people.Customer;
import via.sep2.model.people.Person;
import via.sep2.model.reservation.Dining;
import via.sep2.model.reservation.DiningImplementation;
import via.sep2.model.time.*;
import via.sep2.model.time.Date;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiningDAOImpl implements DiningDAO
{
  private static DiningDAOImpl instance;

  private DiningDAOImpl() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  public static DiningDAOImpl getInstance() throws SQLException
  {
    //making it a singleton
    if (instance == null)
    {
      synchronized (DiningDAOImpl.class)
      {
        //MultiThread safety
        if (instance == null)
        {
          instance = new DiningDAOImpl();
        }
      }
    }
    return instance;
  }

  /**
   * @param date
   * @param hour
   * @param email
   * @return
   * @throws SQLException
   */
  @Override public DiningRequestDto create(Date date, Hour hour, String email)
      throws SQLException
  {
    //we make the connection is closed by getting the connection in a try block
    try (Connection connection = ConnectionUtil.getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "INSERT INTO Dining ( date, hour, email) VALUES(?,?,?)",
          PreparedStatement.RETURN_GENERATED_KEYS);
      statement.setDate(1, new java.sql.Date(
          DateUtility.toJavaUtilDate((DateImplementation) date).getTime()));
      statement.setTime(2, HourUtil.toSqlTime((HourImplementation) hour));
      statement.setString(3, email.toLowerCase());
      statement.executeUpdate();
      ResultSet keys = statement.getGeneratedKeys();
      DiningRequestDto dining = null;
      if (keys.next())
      {
        dining = new DiningRequestDto(keys.getInt(1),
            DateUtility.fromDateToDto(date), HourUtil.fromHourToDto(hour),
            email);
      }

      if (dining == null)
      {
        throw new SQLException("Dining was not created in the client");
      }
      return dining;
    }
  }

  /**
   * @param orderId
   * @throws SQLException
   */
  @Override public void cancel(int orderId) throws SQLException
  {
    //we make the connection is closed by getting the connection in a try block
    try (Connection connection = ConnectionUtil.getConnection())
    {
      checkCancel(orderId);
      PreparedStatement statement = connection.prepareStatement(
          "DELETE FROM dining WHERE order_id = ?");
      statement.setInt(1, orderId);
      statement.executeUpdate();
    }

  }

  /**
   * @param email
   * @return
   * @throws SQLException
   */
  @Override public List<DiningRequestDto> readByEmail(String email)
      throws SQLException
  {
    //we make the connection is closed by getting the connection in a try block
    try (Connection connection = ConnectionUtil.getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM dining WHERE email LIKE ?");
      statement.setString(1, "%" + email.toLowerCase() + "%");
      ResultSet resultSet = statement.executeQuery();
      ArrayList<DiningRequestDto> result = new ArrayList<>();
      while (resultSet.next())
      {
        result.add(new DiningRequestDto(resultSet.getInt("order_id"),
            DateUtility.fromJavaUtilDateDto(resultSet.getDate("date")),
            HourUtil.fromTimeToDto(resultSet.getTime("hour")), email));
      }
      return result;
    }
  }

  @Override public List<DiningRequestDto> getAllCurrent() throws SQLException
  {
    List<DiningRequestDto> result = new ArrayList<>();
    try (Connection connection = ConnectionUtil.getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM dining WHERE date>?");
      statement.setDate(1, new java.sql.Date(
          DateUtility.toJavaUtilDate((DateImplementation) DateUtility.today())
              .getTime()));
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next())
      {
        DateDto date = DateUtility.fromJavaUtilDateDto(
            resultSet.getDate("date"));
        HourDto hour = HourUtil.fromTimeToDto(resultSet.getTime("hour"));
        result.add(
            new DiningRequestDto(resultSet.getInt("order_id"), date, hour,
                resultSet.getString("email")));
      } return result;
    }
  }

  /**
   * @param orderId
   * @return
   * @throws SQLException
   */
  @Override public DiningRequestDto readByOrderId(int orderId) throws SQLException
  {
    //we make the connection is closed by getting the connection in a try block
    try (Connection connection = ConnectionUtil.getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM dining WHERE order_id = ?");
      statement.setInt(1, orderId);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        PersonDataDto person = PersonDAOImpl.getInstance()
            .readByEmail(resultSet.getString("email"));
        return new DiningRequestDto(orderId,
            DateUtility.fromJavaUtilDateDto(resultSet.getDate("date")),
            HourUtil.fromTimeToDto(resultSet.getTime("hour")), person.email());
      }
      else
      {
        throw new SQLException("Order not found");
      }
    }
  }

  private void checkCancel(int orderId) throws SQLException
  {
    DiningRequestDto dining = readByOrderId(orderId);
    Date date = new DateImplementation(dining.date().year(),dining.date().month(),dining.date().day());
    if (DateUtility.timeBetween( date, DateUtility.today())
        >= 0)
    {
      throw new SQLException("Cancel window expired");
    }
  }
}