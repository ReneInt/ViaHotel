package via.sep2.startup;


import via.sep2.networking.requesthandlers.*;
import via.sep2.persistence.daos.*;
import via.sep2.services.autentification.AuthServiceImpl;
import via.sep2.services.autentification.AuthenticationService;
import via.sep2.services.dining.DiningService;
import via.sep2.services.dining.DiningServiceImpl;
import via.sep2.services.person.PersonService;
import via.sep2.services.person.PersonServiceImpl;
import via.sep2.services.reservation.ReservationService;
import via.sep2.services.reservation.ReserveServiceImpl;
import via.sep2.services.room.RoomService;
import via.sep2.services.room.RoomServiceImpl;
import via.sep2.utilities.logging.Logger;
import via.sep2.utilities.logging.MarkdownLogger;
import java.sql.SQLException;

public class ServiceProvider
{
  public RequestHandler getAuthenticationRequestHandler() throws SQLException
  {
    return new AuthRequestHandler(getAuthenticationService());
  }
  public RequestHandler getRoomRequestHandler() throws SQLException
  {
    return new RoomRequestHandler(getRoomService());
  }
  public RequestHandler getPeopleRequestHandler() throws SQLException
  {
    return new PeopleRequestHandler(getPersonService());
  }


  public RequestHandler getDiningRequestHandler() throws SQLException
  {
   return new DiningRequestHandler(getDiningService());
  }

  private DiningService getDiningService() throws SQLException
  {
    return new DiningServiceImpl(getDiningDao());
  }

  public Logger getLogger()
  {
    return MarkdownLogger.getInstance();
  }
  private PersonService getPersonService()
      throws SQLException
  {
    return new PersonServiceImpl(getPersonDao());
  }
  private AuthenticationService getAuthenticationService()
      throws SQLException
  {
    return new AuthServiceImpl(getLoginDao());
  }
  private RoomService getRoomService()
      throws SQLException
  {
    return new RoomServiceImpl(getRoomDao());
  }
  /**
   * Handles getting the requestHandler implementation {@link ReservationRequestHandler} for reservations
   * @return {@link RequestHandler} with the Reservation implementation
   * @throws SQLException if the connection cannot be made to the database
   */
  public RequestHandler getReservationRequestHandler() throws SQLException
  {
    return new ReservationRequestHandler(getReservationService());
  }
  /**
   * Handles getting the {@link ReservationService} implementation
   * @return {@code ReservationService} with the given implementation
   * @throws SQLException if the connection cannot be made to the database
   */
  private ReservationService getReservationService() throws SQLException
  {
    return new ReserveServiceImpl(getReservationDao());
  }
  /**
   * Handles getting the {@link ReservationDAO} implementation
   * @return {@code ReservationDAO} with the given implementation
   * @throws SQLException if the connection cannot be made to the database
   */
  private ReservationDAO getReservationDao() throws SQLException
  {
    return ReservationDAOImpl.getInstance();
  }
  private DiningDAO getDiningDao() throws SQLException
  {
    return DiningDAOImpl.getInstance();
  }

  private LoginDAO getLoginDao() throws SQLException
  {
    return LoginDAOImpl.getInstance();
  }
  private RoomDAO getRoomDao() throws SQLException
  {
    return RoomDAOImpl.getInstance();
  }
  private PersonDAO getPersonDao() throws SQLException
  {
    return PersonDAOImpl.getInstance();
  }
}