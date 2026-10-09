package via.sep2.persistence.daos;

import dtos.reservation.ReservationDataDto;
import dtos.reservation.ReservationRoomTypeDto;
import types.ReservationType;
import types.RoomType;
import via.sep2.model.time.Date;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Mario,Rene
 * @since version 2.0 changed to use {@link dtos.reservation}
 * @version 2.0
 */
public interface ReservationDAO
{
  ReservationDataDto create(ReservationType reservationType, String email,
      Date startDate, Date endDate, int roomNumber) throws SQLException;
  ReservationRoomTypeDto create(ReservationType reservationType,
      String email, Date startDate, Date endDate, RoomType roomType)
      throws SQLException;
  ReservationDataDto update(int id, ReservationType reservationType, String email,
      Date startDate, Date endDate, int roomNumber) throws SQLException;
  void cancelReservation(int id) throws SQLException;
  ReservationDataDto readById(int id) throws SQLException;
  List<ReservationDataDto> readByEmail(String email) throws SQLException;
  double setFinalPriceById(int id)throws SQLException;
  void updatePricing(ReservationType reservationType,double price)throws SQLException;
  List<ReservationRoomTypeDto> getAllActiveReservations() throws SQLException;
  double readPricePerType(ReservationType reservationType)
      throws SQLException;
  RoomType getRoomType(int roomNumber) throws SQLException;
}
