package via.sep2.networking.dining;

import dtos.Request;
import dtos.dining.DiningRequestDto;
import dtos.reservation.FinalPriceDto;
import via.sep2.networking.SocketService;

public class SocketDiningClient implements DiningClient
{
  @Override public DiningRequestDto create(DiningRequestDto payload)
  {
    Request request = new Request("dining", "create", payload);
    DiningRequestDto response = (DiningRequestDto) SocketService.sendRequest(request);
    return response;
  }

  @Override public DiningRequestDto edit(DiningRequestDto payload)
  {
    Request request = new Request("dining", "edit", payload);
    DiningRequestDto response = (DiningRequestDto) SocketService.sendRequest(request);
    return response;
  }

  @Override public void cancelDining(DiningRequestDto payload)
  {
    Request request = new Request("dining", "cancel", payload);
    SocketService.sendRequest(request);
  }
}
