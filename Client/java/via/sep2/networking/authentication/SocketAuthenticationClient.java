package via.sep2.networking.authentication;

import dtos.Request;
import dtos.auth.LoginRequest;
import dtos.auth.RegisterUserRequest;
import dtos.auth.UpdatePasswordRequest;
import dtos.person.PersonDataDto;
import via.sep2.networking.SocketService;
/**
 * Implementation of {@link AuthenticationClient} using sockets for communication with the server.
 * <p>
 * Handles user registration, login, and password updates by sending appropriate requests
 * via the {@link SocketService}.
 * </p>
 *
 * @author Troels, Mario
 * @version 1.0
 */
public class SocketAuthenticationClient implements AuthenticationClient
{
    /**
     * {@inheritDoc}
     *
     * @param user a {@code RegisterUserRequest} containing the user's registration data
     * @see AuthenticationClient#registerUser(RegisterUserRequest)
     */
    @Override
    public void registerUser(RegisterUserRequest user)
    {
        //Making a request for the server to register
        Request request = new Request("auth", "register", user);
        SocketService.sendRequest(request);
    }
    /**
     * {@inheritDoc}
     * Authenticates a user by verifying their login credentials.
     * Sends a request to the server and returns the corresponding user data if login is successful.
     *
     * @param loginRequest a {@code LoginRequest} containing the user's login credentials
     * @return a {@code PersonDataDto} representing the authenticated user
     * @see AuthenticationClient#login(LoginRequest)
     */
    @Override
    public PersonDataDto login(LoginRequest loginRequest)
    {
        //Making a request to the server to login
        Request request = new Request("auth", "login", loginRequest);
        //we get user data in a dto to return to get a return onto the client side
      return (PersonDataDto) SocketService.sendRequest(request);
    }
    /**
     * {@inheritDoc}
     * Updates the user's password by sending the updated information to the server.
     *
     * @param passwordRequest an {@code UpdatePasswordRequest} with the new password data
     * @see AuthenticationClient#updatePassword(UpdatePasswordRequest)
     */

    @Override public void updatePassword(UpdatePasswordRequest  passwordRequest)
    {
        //Making a request to the server to updatePassword
        Request request = new Request("auth", "update", passwordRequest);
        SocketService.sendRequest(request);
    }
}
