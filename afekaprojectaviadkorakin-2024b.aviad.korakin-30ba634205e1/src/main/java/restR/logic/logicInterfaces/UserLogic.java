package restR.logic.logicInterfaces;

import java.util.List;

import restR.boundaries.NewUserBoundary;
import restR.boundaries.UserBoundary;
import restR.entities.UserEntity;
import restR.logic.exceptions.RestRBadRequestException;
import restR.logic.exceptions.RestRNotFoundException;
import restR.logic.exceptions.RestRUnauthorizedException;

public interface UserLogic {
	/**
	 * Stores a new user in the database and sends a registration message.
	 * 
	 * This method validates the provided {@link NewUserBoundary} object, creates a
	 * corresponding {@link UserEntity}, saves it to the database, and attempts to
	 * send a registration message to the user's email.
	 * 
	 * @param newuser      A {@link NewUserBoundary} object containing the new user
	 *                     information.
	 * @param superappName The super application the user belongs to.
	 * @return A {@link UserBoundary} object representing the stored user.
	 * @throws RestRBadRequestException If any validation on the new user fails, the
	 *                                  email is invalid, or the registration
	 *                                  message cannot be sent.
	 */

	public UserBoundary storeInDatabase(NewUserBoundary newuser);

	/**
	 * Retrieves a user by super application and email address.
	 * 
	 * This method fetches a user based on the provided super application and email
	 * address.
	 * 
	 * @param eSuperapp The super application of the user to retrieve.
	 * @param email     The email address of the user to retrieve.
	 * @return A {@link UserBoundary} object representing the retrieved user.
	 * @throws RestRNotFoundException If the user with the specified email address
	 *                                is not found.
	 */
	public UserBoundary getSpecificUserFromDatabase(String eSuperapp, String email);

	/**
	 * Updates a user identified by super application and email address.
	 * 
	 * This method updates specific fields of the user based on the provided
	 * {@link UserBoundary} object. Only username, avatar, and role can be updated.
	 * 
	 * @param eSuperapp The super application of the user to update.
	 * @param email     The email address of the user to update.
	 * @param boundary  A {@link UserBoundary} object containing the updated user
	 *                  information.
	 * @throws RestRBadRequestException If username or avatar in the boundary object
	 *                                  are blank, or if the role is null.
	 */
	public void updateById(String eSuperapp, String email, UserBoundary boundary);

	/**
	 * Deletes all users.
	 * 
	 * **WARNING:** This method deletes all users permanently. Use with caution!
	 *
	 * @param userSuperapp The super application of the requesting user.
	 * @param email        The email address of the requesting user.
	 * @throws RestRUnauthorizedException If the requesting user is not authorized
	 *                                    to delete users (requires ADMIN role).
	 */
	public void deleteAll(String userSuperapp, String email);

	/**
	 * **Deprecated:** Exports all users (without pagination or filtering).
	 * 
	 * This method is deprecated and should not be used in new code. It might be
	 * removed in future versions. Use the
	 * {@link #ExportAllUsers(String, String, int, int)} method instead for
	 * pagination and filtering based on user's role.
	 * 
	 * @throws RestRBadRequestException Always thrown indicating this method is
	 *                                  deprecated.
	 */
	@Deprecated
	public List<UserBoundary> ExportAllUsers();

	/**
	 * Exports all users with pagination and filtering based on user's role.
	 * 
	 * @param userSuperapp The super application of the requesting user.
	 * @param email        The email address of the requesting user.
	 * @param size         The number of users to retrieve per page.
	 * @param page         The current page number (zero-based indexing).
	 * @return A list of {@link UserBoundary} objects representing the exported
	 *         users.
	 * @throws RestRUnauthorizedException If the requesting user is not authorized
	 *                                    to export users (requires ADMIN role).
	 */
	public List<UserBoundary> ExportAllUsers(String userSuperapp, String email, int size, int page);


}
