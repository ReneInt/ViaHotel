package via.sep2.persistence.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil
{
  public static Connection getConnection() throws SQLException
  {
    //Create a connection to the DriverManager
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=viahotel",
        "postgres", "nogayshit");
  }
}
