package via.sep2.networking.exceptions;

public class NotFoundException extends RuntimeException
{
  public NotFoundException(String handler, String action)
  {
    super("Action '" + action + "' not found on handler '" + handler + "'.");
  }
}
