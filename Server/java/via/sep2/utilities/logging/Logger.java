package via.sep2.utilities.logging;

public interface Logger
{
  void log(String message);
  void log(String message,Formating formating);
  void log(Exception e);//log exceptions
  void logNewUser(String message);
}
