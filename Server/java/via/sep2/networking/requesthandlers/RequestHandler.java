package via.sep2.networking.requesthandlers;

import java.sql.SQLException;

/**
 * @author Troels
 */
// This interface serves little purpose other than ensuring consistency across different specific SocketHandlers.
// Consistency and uniformitet is nice.
public interface RequestHandler
{
  Object handle(String action, Object payload) throws SQLException;
}
