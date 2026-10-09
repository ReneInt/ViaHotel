package via.sep2.services.dining;

import dtos.dining.DiningRequestDto;
import dtos.time.DateDto;
import dtos.time.HourDto;
import via.sep2.model.reservation.Dining;
import via.sep2.model.time.Date;
import via.sep2.model.time.DateImplementation;
import via.sep2.model.time.Hour;
import via.sep2.model.time.HourImplementation;
import via.sep2.persistence.daos.DiningDAO;
import via.sep2.services.exceptions.ValidationException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DiningServiceImpl implements DiningService
{
  private final DiningDAO diningDao;
  public DiningServiceImpl(DiningDAO diningDao) throws SQLException
  {
    this.diningDao= diningDao;
  }

  @Override public DiningRequestDto create(DiningRequestDto payload)
  {
    Hour hour=new HourImplementation(payload.hour().hours(),payload.hour().minute());
    Date date=new DateImplementation(payload.date().year(),payload.date().month(),payload.date().day());
    try
    {
    return diningDao.create(date,hour,payload.email());
    }
    catch (SQLException e)
    {
      throw new ValidationException(e.getMessage());
    }
  }

  @Override public DiningRequestDto edit(DiningRequestDto payload)
  {
    try
    {
      diningDao.cancel(payload.id());

    Hour hour=new HourImplementation(payload.hour().hours(),payload.hour().minute());
    Date date=new DateImplementation(payload.date().year(),payload.date().month(),payload.date().day());
    try
    {
      return diningDao.create(date,hour,payload.email());
    }
    catch (SQLException e)
    {
      throw new ValidationException(e.getMessage());
    }
  }
    catch (SQLException e)
  {
    throw new ValidationException(e.getMessage());
  }
  }

  @Override public void cancelDining(DiningRequestDto payload)
  {
    try
    {
      diningDao.cancel(payload.id());
    }
    catch (SQLException e)
    {
      throw new ValidationException(e.getMessage());
    }
  }

  @Override public List<DiningRequestDto> getALL()
  {
    try
    {
      return diningDao.getAllCurrent();
    }
    catch (SQLException e)
    {
      throw new ValidationException(e.getMessage());
    }
  }
}
