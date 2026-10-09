package via.sep2.networking.exceptions;

public class ServerFailureException extends RuntimeException
{
  public ServerFailureException(String handler, String action)
  {
    super("Action '" + action + "' not found on handler '" + handler + "'.");
  }
}
