package via.sep2.data;

import dtos.person.PersonDataDto;

public class AppState
{
  private static PersonDataDto loggedInUser;

  public static PersonDataDto getCurrentUser()
  {
    return loggedInUser;
  }

  public static void setCurrentUser(PersonDataDto user)
  {
    loggedInUser = user;
  }
}