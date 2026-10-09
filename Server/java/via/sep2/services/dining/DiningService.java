package via.sep2.services.dining;

import dtos.dining.DiningRequestDto;

import java.sql.SQLException;
import java.util.List;

public interface DiningService

{
  DiningRequestDto create (DiningRequestDto payload);
  DiningRequestDto edit (DiningRequestDto payload);
  void cancelDining(DiningRequestDto payload);
  List<DiningRequestDto> getALL();
}
