package via.sep2.networking.dining;

import dtos.dining.DiningRequestDto;

public interface DiningClient
{
  DiningRequestDto create (DiningRequestDto payload);
  DiningRequestDto edit (DiningRequestDto payload);
  void cancelDining(DiningRequestDto payload);
}
