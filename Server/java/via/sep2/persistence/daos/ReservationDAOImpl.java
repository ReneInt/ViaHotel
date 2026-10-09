package via.sep2.persistence.daos;

import dtos.reservation.ReservationDataDto;
import dtos.reservation.ReservationRoomTypeDto;
import dtos.time.DateDto;
import org.postgresql.Driver;
import types.ReservationType;
import types.RoomType;
import via.sep2.model.room.FamilyRoom;
import via.sep2.model.room.KingSizeRoom;
import via.sep2.model.room.Room;
import via.sep2.model.room.SmallRoom;
import via.sep2.model.time.Date;
import via.sep2.model.time.DateImplementation;
import via.sep2.model.time.DateUtility;
import exceptions.InvalidDataException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mario, Rene
 * @version 2.0
 * @since 2.0 Refactored to use {@link dtos.reservation}
 */
public class ReservationDAOImpl implements ReservationDAO
{
  //IMPORTANT NOTE! tried to change price to price_modifier, it ruined things and I don't know why, I'm not touching it anymore
  //Sorry Allan...
  private static ReservationDAOImpl instance;

  ReservationDAOImpl() throws SQLException
  {
    DriverManager.registerDriver(new Driver());
  }

  public static ReservationDAOImpl getInstance() throws SQLException
  {
    //making it a singleton
    if (instance == null)
    {
      synchronized (ReservationDAOImpl.class)
      {
        //MultiThread safety
        if (instance == null)
        {
          instance = new ReservationDAOImpl();
        }
      }
    }
    return instance;
  }

  private void checkAvailability(int roomNumber, Date startDate)
      throws SQLException, InvalidDataException
  {
    try (Connection connection = ConnectionUtil.getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * from reservation WHERE room=?");
      statement.setInt(1, roomNumber);
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Date> dates = new ArrayList<>();
      while (resultSet.next())
      {
        dates.add(DateUtility.fromJavaUtilDate(resultSet.getDate("end_date")));
        if (DateUtility.timeBetween(startDate, dates.get(dates.size() - 1)) > 0)
        {
          throw new SQLException("Room already booked");
        }
      }
    }
  }

  private void checkUpdateAvailability(int id, int roomNumber, Date startDate)
      throws SQLException
  {
    try (Connection connection = ConnectionUtil.getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * from reservation WHERE room=?");
      statement.setInt(1, roomNumber);
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Date> dates = new ArrayList<>();
      while (resultSet.next())
      {
        if (resultSet.getInt("id") != id)
        {
          dates.add(
              DateUtility.fromJavaUtilDate(resultSet.getDate("end_date")));
          if (DateUtility.timeBetween(startDate, dates.get(dates.size() - 1))
              > 0)
          {
            throw new SQLException("Room already booked");
          }
        }
      }
    }
  }

  /**
   * @param reservationType
   * @param email
   * @param startDate
   * @param endDate
   * @param roomNumber
   * @return
   * @throws SQLException
   */
  @Override public ReservationDataDto create(ReservationType reservationType,
      String email, Date startDate, Date endDate, int roomNumber)
      throws SQLException
  {
    //we make the connection is closed by getting the connection in a try block
    try (Connection connection = ConnectionUtil.getConnection())
    {
      //code here for update
      checkAvailability(roomNumber, startDate);
      ReservationDataDto reservation = null;
      PreparedStatement statement = connection.prepareStatement(
          "INSERT INTO reservation(reservation_type, customer_email, start_date, end_date, room, price) VALUES(?, ?, ?, ?, ?, ?)",
          PreparedStatement.RETURN_GENERATED_KEYS);

      statement.setString(2, email.toLowerCase());

      statement.setDate(3, new java.sql.Date(
          DateUtility.toJavaUtilDate((DateImplementation) startDate)
              .getTime()));
      statement.setDate(4, new java.sql.Date(
          DateUtility.toJavaUtilDate((DateImplementation) endDate).getTime()));
      statement.setInt(5, roomNumber);
      double price = readPricePerType(reservationType);
      statement.setDouble(6, price);
      statement.setString(1, reservationType.dataBase());
      statement.executeUpdate();
      ResultSet keys = statement.getGeneratedKeys();
      if (keys.next())
      {
        setFinalPriceById(keys.getInt(1));
        reservation = new ReservationDataDto(keys.getInt(1), reservationType,
            email.toLowerCase(),
            new DateDto(startDate.getYear(), startDate.getMonth(),
                startDate.getDay()),
            new DateDto(endDate.getYear(), endDate.getMonth(),
                endDate.getDay()), roomNumber);
      }
      if (reservation == null)
      {
        throw new SQLException("Reservation not created");
      }
      return reservation;
    }
  }

  @Override public ReservationDataDto update(int id,
      ReservationType reservationType, String email, Date startDate,
      Date endDate, int roomNumber) throws SQLException
  {
    //we make the connection is closed by getting the connection in a try block
    try (Connection connection = ConnectionUtil.getConnection())
    {
      checkUpdateAvailability(id, roomNumber, startDate);
      //code here for update
      ReservationDataDto reservation = readById(id);
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE reservation SET reservation_type = ?, customer_email =?, start_date = ?, end_date = ?, room = ?,price = ? WHERE id = ?");
      java.util.Date endTempDate = DateUtility.toJavaUtilDate(
          (DateImplementation) endDate);

      statement.setString(1, reservationType.dataBase());
      statement.setString(2, email.toLowerCase());
      statement.setDate(3, new java.sql.Date(
          //setting up startDate
          DateUtility.toJavaUtilDate((DateImplementation) startDate)
              .getTime()));
      //setting up endDate
      statement.setDate(4, new java.sql.Date(
          DateUtility.toJavaUtilDate((DateImplementation) endDate).getTime()));

      statement.setInt(5, roomNumber);
      statement.setInt(7, id);
      statement.setDouble(6, readPricePerType(reservationType));
      statement.executeUpdate();
      setFinalPriceById(id);
    }
    return readById(id);
  }

  /**
   * @param id
   * @throws SQLException
   */

  @Override public void cancelReservation(int id) throws SQLException
  {
    //we make the connection is closed by getting the connection in a try block
    try (Connection connection = ConnectionUtil.getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "DELETE FROM reservation WHERE id = ?");
      statement.setInt(1, id);
      statement.executeUpdate();
    }
  }

  /**
   * @param id
   * @return
   * @throws SQLException
   */
  @Override public ReservationDataDto readById(int id) throws SQLException
  {
    //we make the connection is closed by getting the connection in a try block
    try (Connection connection = ConnectionUtil.getConnection())
    {
      //code here for update
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM reservation WHERE id = ?");
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery();

      PreparedStatement select = connection.prepareStatement(
          "SELECT * FROM PERSON WHERE email LIKE ?");

      if (resultSet.next())
      {
        String reservationString = resultSet.getString("reservation_type");
        String email = resultSet.getString("customer_email");
        java.sql.Date startDate = resultSet.getDate("start_date");
        java.sql.Date endDate = resultSet.getDate("end_date");
        //parsing back to custom Date
        Date start = DateUtility.fromJavaUtilDate(startDate);
        Date end = DateUtility.fromJavaUtilDate(endDate);
        //Still using Date instead of DateDto because it has value checks
        int roomNumber = resultSet.getInt("room");
        ReservationType reservationType = ReservationType.valueOf(
            reservationString.toUpperCase());
        return new ReservationDataDto(id, reservationType, email,
            new DateDto(start.getYear(), start.getMonth(), start.getDay()),
            new DateDto(end.getYear(), end.getMonth(), end.getDay()),
            roomNumber, resultSet.getInt("price"));
      }
      else
      {
        throw new SQLException("Cannot create reservation, try again");
      }
    }
  }

  /**
   * @param email
   * @return
   * @throws SQLException
   */
  @Override public List<ReservationDataDto> readByEmail(String email)
      throws SQLException
  {
    //we make the connection is closed by getting the connection in a try block
    try (Connection connection = ConnectionUtil.getConnection())
    {
      //code here for update
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM reservation WHERE customer_email LIKE ?");
      statement.setString(1, "%" + email.toLowerCase() + "%");
      ResultSet resultSet = statement.executeQuery();
      List<ReservationDataDto> result = new ArrayList<ReservationDataDto>();
      while (resultSet.next())
      {
        int id = resultSet.getInt("id");
        String reservationString = resultSet.getString("reservation_type");
        java.sql.Date startDate = resultSet.getDate("start_date");
        java.sql.Date endDate = resultSet.getDate("end_date");
        Date start = DateUtility.fromJavaUtilDate(startDate);
        Date end = DateUtility.fromJavaUtilDate(endDate);
        int roomNumber = resultSet.getInt("room");
        ReservationType reservationType = ReservationType.valueOf(
            reservationString.toUpperCase());
        result.add(new ReservationDataDto(id, reservationType, email,
            new DateDto(start.getYear(), start.getMonth(), start.getDay()),
            new DateDto(end.getYear(), end.getMonth(), end.getDay()),
            roomNumber));
      }
      if (result.isEmpty())
      {
        throw new SQLException("No reservations found");
      }
      return result;
    }
  }

  /**
   * @param id
   * @return
   */
  @Override public double setFinalPriceById(int id) throws SQLException
  {
    try (Connection connection = ConnectionUtil.getConnection())
    {
      ReservationDataDto reservation = readById(id);
      PreparedStatement selectRoom = connection.prepareStatement(
          "SELECT * FROM ROOM WHERE room_number = ?");
      selectRoom.setInt(1, reservation.roomNumber());
      PreparedStatement addPrice = connection.prepareStatement(
          "UPDATE reservation SET final_price = ? WHERE id = ?");
      addPrice.setInt(2, id);
      ResultSet resultSet = selectRoom.executeQuery();
      if (resultSet.next())
      {
        double reservationTypePrice = reservation.priceModifier();
        double roomPrice = resultSet.getDouble("price");
        Date start = new DateImplementation(reservation.startDate().year(),
            reservation.startDate().month(), reservation.startDate().day());
        Date end = new DateImplementation(reservation.endDate().year(),
            reservation.endDate().month(), reservation.endDate().day());
        double finalPrice =
            reservationTypePrice / 100 * roomPrice * DateUtility.timeBetween(
                start, end);
        addPrice.setDouble(1, finalPrice);
        addPrice.executeUpdate();
        return finalPrice;
      }
      throw new SQLException("Final could not be set");
    }
  }

  public double readPricePerType(ReservationType reservationType)
      throws SQLException
  {
    try (Connection connection = ConnectionUtil.getConnection())
    {
      double price = 0;
      PreparedStatement statement = connection.prepareStatement(
          "SELECT price FROM reservation WHERE reservation_type LIKE ? LIMIT 1");
      statement.setString(1, reservationType.dataBase());
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        price = resultSet.getDouble("price");
      }
      if (price == 0)
      {
        throw new SQLException("Whoops reservation is not free, set a price");
      }
      return price;
    }
  }

  /**
   * @param reservationType
   * @param price
   */
  @Override public void updatePricing(ReservationType reservationType,
      double price) throws SQLException
  {
    try (Connection connection = ConnectionUtil.getConnection())
    {
      PreparedStatement update = connection.prepareStatement(
          "UPDATE reservation SET price = ? WHERE reservation_type = ?");
      update.setDouble(1, price);
      update.setString(2,reservationType.dataBase());
        update.executeUpdate();
    }
  }

  @Override public ReservationRoomTypeDto create(
      ReservationType reservationType, String email, Date startDate,
      Date endDate, RoomType roomType) throws SQLException
  {
    List<Integer> roomList = RoomDAOImpl.getInstance().getRoomsOfType(roomType);
    int number = 0;
    for (int i = 0; i < roomList.size(); i++)
    {
      try
      {
        checkAvailability(roomList.get(i), startDate);
        number = roomList.get(i);
      }
      catch (SQLException e)
      {
      }
      ReservationDataDto reservation = null;
      if (number != 0)
      {
        reservation = create(reservationType, email, startDate, endDate,
            number);
      }
      if (reservation != null)
      {
        return new ReservationRoomTypeDto(reservationType, email,
            new DateDto(startDate.getYear(), startDate.getMonth(),
                startDate.getDay()),
            new DateDto(endDate.getYear(), endDate.getMonth(),
                endDate.getDay()), roomType);
      }
    }
    throw new SQLException(
        "No rooms available of that type, reservation could not be made");//sometimes it returns stuff other times it's null, you get an exception!
  }

  /**
   * Selects all active reservation
   * <p>note from author: It finally works. Made me cry -Mario</p>
   * @since 2.0 optimised to select only once instead of n^2 times
   * @return All active reservations
   * @throws SQLException if the connection is not lost or data is not found
   */
  @Override
  public List<ReservationRoomTypeDto> getAllActiveReservations() throws SQLException {
    List<ReservationRoomTypeDto> result = new ArrayList<>();
    try (Connection connection = ConnectionUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement("""
    SELECT *
    FROM reservation r
    JOIN room rm ON r.room = rm.room_number
    WHERE r.end_date > ?
  """)) {

      statement.setDate(1, new java.sql.Date(
          DateUtility.toJavaUtilDate((DateImplementation) DateUtility.today()).getTime()));

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          int id = resultSet.getInt("id");
          String email = resultSet.getString("customer_email");
          String reservationString = resultSet.getString("reservation_type");
          java.sql.Date startDate = resultSet.getDate("start_date");
          java.sql.Date endDate = resultSet.getDate("end_date");
          String roomTypeString = resultSet.getString("room_type");
          double price = resultSet.getDouble("final_price");

          Date start = DateUtility.fromJavaUtilDate(startDate);
          Date end = DateUtility.fromJavaUtilDate(endDate);
          ReservationType reservationType = ReservationType.valueOf(reservationString.toUpperCase());

          RoomType roomType = RoomType.valueOf(roomTypeString.toUpperCase());

          result.add(new ReservationRoomTypeDto(id, reservationType, email,
              new DateDto(start.getYear(), start.getMonth(), start.getDay()),
              new DateDto(end.getYear(), end.getMonth(), end.getDay()),
              roomType, price));
        }
      }
    }
    if (result.isEmpty()) {
      throw new SQLException("No reservations found");
    }
    //Runs fast!
    return result;
  }

  public RoomType getRoomType(int roomNumber) throws SQLException
  {
    Room room = RoomDAOImpl.getInstance().readByRoomNumber(roomNumber);
    if (room instanceof SmallRoom)
    {
      return RoomType.SMALL;
    }
    else if (room instanceof KingSizeRoom)
    {
      return RoomType.KING;
    }
    else if (room instanceof FamilyRoom)
    {
      return RoomType.FAMILY;
    }
    throw new SQLException("HOw did we get here?");
  }
}