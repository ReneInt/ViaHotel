package via.sep2.networking.requesthandlers;

import dtos.auth.LoginRequest;
import dtos.auth.RegisterUserRequest;
import dtos.auth.UpdatePasswordRequest;
import via.sep2.networking.EventManager;
import via.sep2.networking.exceptions.InvalidActionException;
import via.sep2.networking.FavorReaders;
import via.sep2.services.autentification.AuthenticationService;
import via.sep2.services.exceptions.ValidationException;

import java.sql.SQLException;

/**
 * @author Troels,Mario
 */
public class AuthRequestHandler implements RequestHandler
{
  private final AuthenticationService authenticationService;
  private final FavorReaders favorReaders=FavorReaders.getInstance();
  public AuthRequestHandler(AuthenticationService authenticationService)
  {
    this.authenticationService = authenticationService;
  }

  @Override
  public Object handle(String action, Object payload) throws SQLException
  {
    try{
    switch (action)
    {
      case "register" ->
      {
        favorReaders.acquireWrite();
        authenticationService.registerUser((RegisterUserRequest) payload);
        favorReaders.releaseWrite();
        EventManager.fireUpdate("person");
      }
      case "login" ->
      {
        favorReaders.acquireRead();
        Object response=authenticationService.login((LoginRequest) payload);
        favorReaders.releaseRead();
        return response;
      }
      case "update"->{
        favorReaders.acquireWrite();
        authenticationService.updatePassword((UpdatePasswordRequest) payload);
        favorReaders.releaseWrite();
      }
      default -> throw new InvalidActionException("auth", action);
    }
    }
    catch (Exception e)
    {
      favorReaders.releaseWrite();
      favorReaders.releaseRead();
      throw new ValidationException(e.getMessage());
    }
    return null; // just a default return value. Some actions above may return stuff.
  }
}
