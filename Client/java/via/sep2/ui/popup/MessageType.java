package via.sep2.ui.popup;

import exceptions.InvalidDataException;

public enum MessageType
{
  SUCCESS,
  WARNING,
  NOTIFICATION,
  ERROR;

  @Override public String toString()
  {
    switch (this)
    {
      case SUCCESS -> {
      return "Success";
    }
      case WARNING -> {
        return "Warning";
      }
      case ERROR -> {
        return "Error";
      }
      case NOTIFICATION ->{
        return "Notification";
      }
      case null, default -> throw new InvalidDataException("Unable to get get message text");
    }
  }
}
