package via.sep2.persistence.daos;

import dtos.dining.DiningRequestDto;
import via.sep2.model.time.Date;
import via.sep2.model.time.Hour;

import java.sql.SQLException;
import java.util.List;
public interface DiningDAO
{
  public DiningRequestDto create(Date date, Hour hour, String email)throws
      SQLException;
  public void cancel(int orderId)throws SQLException;
  public List<DiningRequestDto> readByEmail(String email)throws SQLException;
  public List<DiningRequestDto> getAllCurrent()throws SQLException;
  public DiningRequestDto readByOrderId(int orderId)throws SQLException;
}
