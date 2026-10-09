package types;
import exceptions.InvalidDataException;
public enum ReservationType
{
  SIMPLE,
  ALL_INCLUSIVE;

  @Override public String toString()
  {
    switch (this){
      case SIMPLE -> {return "Simple";}
      case ALL_INCLUSIVE -> {
        return "All inclusive";
      }
      case null, default -> throw new InvalidDataException("Not a type");
    }
  }
  public String dataBase()
  {
    switch (this){
      case SIMPLE -> {return "simple";}
      case ALL_INCLUSIVE -> {
        return "all_inclusive";
      }
      case null, default -> throw new InvalidDataException("Not a type");
    }
  }
}
