package types;

import exceptions.InvalidDataException;

public enum PersonType {
  CUSTOMER,
  EMPLOYEE,
  MANAGER;

  @Override public String toString()
  {
    switch (this)
    {
      case CUSTOMER -> {return "customer";}
      case EMPLOYEE -> {return "employee";}
      case MANAGER -> {return "manager";}
      case null, default -> throw new InvalidDataException("case not found");
    }
  }
}
