package via.sep2.persistence.daos;

import types.RoomType;
import via.sep2.model.room.Room;

import java.sql.SQLException;
import java.util.List;

public interface RoomDAO
{
  Room create(int roomNumber, RoomType roomType)throws SQLException;
  void updatePrice(RoomType roomType, double price)throws SQLException;
  void delete(int roomNumber)throws SQLException;
  Room readByRoomNumber(int roomNumber)throws SQLException;
  List<Integer> getRoomsOfType(RoomType roomType)throws SQLException;
  double getRoomPrice(RoomType type) throws SQLException;
}
