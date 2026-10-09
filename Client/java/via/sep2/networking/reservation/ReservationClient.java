package via.sep2.networking.reservation;

import dtos.reservation.FinalPriceDto;
import dtos.reservation.ReservationDataDto;
import dtos.reservation.ReservationRoomTypeDto;

public interface ReservationClient
{
  ReservationDataDto create(ReservationDataDto payload);
  ReservationDataDto create(ReservationRoomTypeDto payload);
  ReservationRoomTypeDto edit(ReservationRoomTypeDto payload);
  double calculateFinalPrice(ReservationRoomTypeDto payload);
  void cancelReservation(ReservationRoomTypeDto payload);
  void updatePrice(ReservationRoomTypeDto payload);
}
