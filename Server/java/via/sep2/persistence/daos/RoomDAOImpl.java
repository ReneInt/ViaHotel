package via.sep2.persistence.daos;

import org.postgresql.Driver;
import types.RoomType;
import via.sep2.model.room.*;
import via.sep2.model.room.KingSizeRoom;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAOImpl implements RoomDAO
{
  private static RoomDAOImpl instance;

  private RoomDAOImpl() throws SQLException
  {
    DriverManager.registerDriver(new Driver());
  }

  public static RoomDAOImpl getInstance() throws SQLException
  {
    //making it a singleton
    if (instance == null)
    {
      synchronized (RoomDAOImpl.class)
      {
        //MultiThread safety
        if (instance == null)
        {
          instance = new RoomDAOImpl();
        }
      }
    }
    return instance;
  }

  /**
   * Handles room creating inside the database
   * @param roomNumber for the primary key representing the number of the room
   * @param roomType represents the type of room
   * @return the room created as an object of type {@link Room}
   */

  @Override public Room create(int roomNumber, RoomType roomType)
      throws SQLException
  {
    //we make the connection is closed by getting the connection in a try block
    try (Connection connection = ConnectionUtil.getConnection())
    {
      //preparing the statement to add a room
      PreparedStatement statement = connection.prepareStatement(
          "INSERT INTO room (room_number, room_type, price) VALUES (?, ? ,?)");
      statement.setInt(1, roomNumber);
      //selecting the existing pricing for rooms
      PreparedStatement selectPrice = connection.prepareStatement(
          "SELECT price FROM room WHERE room_type LIKE ?");
      selectPrice.setString(1,roomType.toString());
      statement.setString(2, roomType.toString());
      // Create Room object to initialize later
      Room room;
      //Initialize the room based on the room type
      switch (roomType)
      {
        case SMALL ->
        {
          room = new SmallRoom(roomNumber);
        }
        case FAMILY ->
        {
          room = new FamilyRoom(roomNumber);
        }
        case KING ->
        {
          room = new KingSizeRoom(roomNumber);
        }
        default ->
            throw new IllegalArgumentException("Invalid RoomType: " + roomType);
      }
      ResultSet resultSet=selectPrice.executeQuery();
      if(resultSet.next())
      {
        statement.setDouble(3,resultSet.getDouble("price"));
       room.setPrice(resultSet.getDouble("price"));
      }
      statement.executeUpdate();
      return room;
    }
  }

  /**
   * @param roomType
   * @param price
   * @return
   */
  @Override public void updatePrice(RoomType roomType, double price)
      throws SQLException
  {
    //we make the connection is closed by getting the connection in a try block
    try (Connection connection = ConnectionUtil.getConnection())
    {
      PreparedStatement select = connection.prepareStatement("SELECT * FROM room WHERE room_type LIKE ?;");
      PreparedStatement update= connection.prepareStatement("UPDATE room SET price=? WHERE room_number=?;");
      update.setDouble(1,price);
      select.setString(1, roomType.toString());
      ResultSet resultSet = select.executeQuery();
      ArrayList<Room> result = new ArrayList<>();
      while (resultSet.next())
      {
        int roomNumber = resultSet.getInt(1);
        result.add(readByRoomNumber(roomNumber));
      }
      for (Room room : result)
      {
        update.setInt(2,room.getRoomNumber());
        room.setPrice(price);
        update.executeUpdate();
      }
    }
  }

  /**
   * @param roomNumber
   */
  @Override public void delete(int roomNumber) throws SQLException
  {
    //we make the connection is closed by getting the connection in a try block
    try (Connection connection = ConnectionUtil.getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "DELETE FROM room WHERE room_number = ?");
      statement.setInt(1, roomNumber);

      // Execute updates
      statement.executeUpdate();
    }
  }

  /**
   * @param roomNumber
   * @return
   */
  @Override
  public Room readByRoomNumber(int roomNumber) throws SQLException {
    final String query = "SELECT room_type FROM Room WHERE room_number = ?";

    try (Connection connection = ConnectionUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {

      statement.setInt(1, roomNumber);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          return switch (resultSet.getString(1)) {
            case "small" -> new SmallRoom(roomNumber);
            case "king" -> new KingSizeRoom(roomNumber);
            case "family" -> new FamilyRoom(roomNumber);
            default -> throw new SQLException("Unknown room type: " + resultSet.getString(1));
          };
        } else {
          throw new SQLException("Room not found: " + roomNumber);
        }
      }
    }
  }

  @Override
  public List<Integer> getRoomsOfType(RoomType roomType) throws SQLException {
    final String query = "SELECT room_number FROM Room WHERE room_type = ?";
    try (Connection connection = ConnectionUtil.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, roomType.toString());
      try (ResultSet resultSet = statement.executeQuery()) {
        List<Integer> idList = new ArrayList<>();
        while (resultSet.next()) {
          idList.add(resultSet.getInt(1));
        }
        return idList;
      }
    }
  }

  public double getRoomPrice(RoomType type) throws SQLException
  {
    try(Connection connection=ConnectionUtil.getConnection())
    {
      PreparedStatement selectRoom = connection.prepareStatement(
          "SELECT price FROM ROOM WHERE room_type = ?");
      selectRoom.setString(1, type.toString());
      ResultSet resultSet=selectRoom.executeQuery();
      if(resultSet.next())
      {
        return resultSet.getDouble("price");
      }
    }
    throw new SQLException("Could not find room price");
  }
}
