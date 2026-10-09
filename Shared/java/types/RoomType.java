package types;

import exceptions.InvalidDataException;

public enum RoomType
{
  SMALL, KING,
  FAMILY;

  @Override public String toString()
  {
    switch (this)
    {
      case SMALL -> {
        return "small";
      }
      case KING ->
      {
        return "king";
      }
      case FAMILY ->
      {
        return "family";
      }
      default ->
      {
        throw new InvalidDataException("Not a type of room");
      }
    }
  }
}
