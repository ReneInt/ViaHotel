package via.sep2.networking.reservation;

import dtos.Request;
import dtos.reservation.FinalPriceDto;
import dtos.reservation.ReservationDataDto;
import dtos.reservation.ReservationRoomTypeDto;
import via.sep2.networking.SocketService;

public class SocketReservationClient implements ReservationClient
{

  @Override public ReservationDataDto create(ReservationDataDto payload)
  {
    Request request = new Request("reservation", "create", payload);
    ReservationDataDto response= (ReservationDataDto) SocketService.sendRequest(request);
    return response;
  }

  @Override public ReservationDataDto create(ReservationRoomTypeDto payload)
  {
    Request request = new Request("reservation", "create", payload);
    ReservationDataDto response= (ReservationDataDto) SocketService.sendRequest(request);
    return response;
  }

  @Override public ReservationRoomTypeDto edit(ReservationRoomTypeDto payload)
  {
    Request request = new Request("reservation", "update", payload);
    ReservationRoomTypeDto response= (ReservationRoomTypeDto) SocketService.sendRequest(request);
    return response;
  }

  @Override public double calculateFinalPrice(
      ReservationRoomTypeDto payload)
  {
    Request request = new Request("reservation", "price", payload);
    FinalPriceDto response= (FinalPriceDto) SocketService.sendRequest(request);
    return response.finalPrice();
  }

  @Override public void cancelReservation(ReservationRoomTypeDto payload)
  {
    Request request = new Request("reservation", "cancel", payload);
    SocketService.sendRequest(request);
  }
  @Override public void updatePrice(ReservationRoomTypeDto payload)
  {
    Request request = new Request("reservation", "updatePrice", payload);
    SocketService.sendRequest(request);
  }
}
