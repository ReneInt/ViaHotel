package via.sep2.services.reservation;

import dtos.reservation.FinalPriceDto;
import dtos.reservation.ReservationDataDto;
import dtos.reservation.ReservationRoomTypeDto;

import java.sql.SQLException;
import java.util.List;

public interface ReservationService
{
  ReservationDataDto create(ReservationDataDto payload);
  ReservationRoomTypeDto create(ReservationRoomTypeDto payload);
  void edit(ReservationRoomTypeDto payload);
  FinalPriceDto calculateFinalPrice(ReservationRoomTypeDto payload);
  void changePrice(ReservationRoomTypeDto payload);
  List<ReservationRoomTypeDto> getAll();
  void cancelReservation(ReservationRoomTypeDto payload);

}
