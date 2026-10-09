package via.sep2.networking.authentication;
import dtos.auth.LoginRequest;
import dtos.auth.RegisterUserRequest;
import dtos.auth.UpdatePasswordRequest;
import dtos.person.PersonDataDto;

/**
 * Interface for managing client authentication.
 * <p>
 * Provides methods related to user login, registration, and password updates.
 * </p>
 *
 * @author Troels, Mario
 * @version 1.0
 */
public interface AuthenticationClient
{
    /**
     * Handles user registration by sending a request to create an account using the provided data.
     * If server-side validation fails, appropriate error messages are returned to the user.
     *
     * @param user a {@code RegisterUserRequest} object containing the user's registration data
     */
    void registerUser(RegisterUserRequest user);
    /**
     * Handles user login by validating the provided credentials.
     * <p>
     * Sends a request containing the user's email and password. If the credentials are valid,
     * the user is logged in with the appropriate permission level. Returns an error if the email
     * is not found or the password is incorrect.
     * </p>
     *
     * @param loginRequest a {@code LoginRequest} object containing the user's login data
     * @return a {@code PersonDataDto} representing the authenticated user to be set as the CurrentUser
     * @see via.sep2.data.AppState for managing the CurrentUser
     */
    PersonDataDto login(LoginRequest loginRequest);
    /**
     * Updates the user's password by verifying the provided data with the server.
     * <p>
     * If the verification fails (e.g., incorrect current password or invalid input),
     * an appropriate error message is returned.
     * </p>
     *
     * @param request an {@code UpdatePasswordRequest} containing the user's password update data
     */
    void updatePassword(UpdatePasswordRequest request);
}
