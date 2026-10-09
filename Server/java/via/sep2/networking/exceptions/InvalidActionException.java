package via.sep2.networking.exceptions;

public class InvalidActionException extends RuntimeException
{
    public InvalidActionException(String handler, String action)
    {
        super("Action '" + action + "' not found on handler '" + handler + "'.");
    }
}