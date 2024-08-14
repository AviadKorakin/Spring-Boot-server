package restR.logic.validTests;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import restR.boundaries.NewUserBoundary;
import restR.crud.UserCrud;
import restR.entities.RoleEnum;
import restR.entities.UserEntity;
import restR.general.UserId;
import restR.logic.converters.RoleConverter;
import restR.logic.exceptions.RestRBadRequestException;
import restR.logic.exceptions.RestRConflictException;
import restR.logic.exceptions.RestRNotFoundException;

@Component
public class ValidTestsUser {
	private UserCrud userCrud;
	private ValidTestsString validTestsString;
	private RoleConverter roleConverter;
	// Regular expression pattern for validating email addresses
	private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@"
			+ "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

	// Compiles the regular expression into a pattern
	private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

	

	public ValidTestsUser(UserCrud userCrud, ValidTestsString validTestsString,RoleConverter roleConverter) {
		this.userCrud = userCrud;
		this.validTestsString = validTestsString;
		this.roleConverter=roleConverter;
	}

	/**
	 * This method validates a `NewUserBoundary` object, throwing an exception if
	 * any required attributes are null.
	 * 
	 * @param NewUserBoundary The `NewUserBoundary` object to validate.
	 * @throws RestRBadRequestException If any of the following attributes in the
	 *                                  boundary object are null.
	 */
	public void TestNotNullsorBlankParams(NewUserBoundary boundary) {
		if (validTestsString.isNullorBlank(boundary.getAvatar()) 
				|| validTestsString.isNullorBlank(boundary.getEmail())
				|| validTestsString.isNullorBlank(boundary.getUsername())
				|| boundary.getRole() == null)
			throw new RestRBadRequestException("Can't enter null arguments.");
	}

	/**
	 * This method checks if a user exists in the database with the provided
	 * `UserId`.
	 * 
	 * It throws a `RestRConflictException` if a user is found, indicating a
	 * conflict because a user with the same ID already exists.
	 * 
	 * @param id The `UserId` object containing the identifier of the user to check
	 *           for.
	 * @throws RestRConflictException If a user with the provided ID already exists
	 *                                in the database.
	 */
	public void ifExistThrows(UserId id)// check if not exists in DB
	{
		if (this.userCrud.existsById(id.toString()))// already exists check
			throw new RestRConflictException("User with email " + id.getEmail() + " already exist in database.");
	}

	/**
	 * This method attempts to retrieve a `UserEntity` from the database based on
	 * the provided `UserId`.
	 * 
	 * It uses the `userCrud` repository to find the user entity by its ID converted
	 * to a string.
	 * 
	 * @param id The `UserId` object containing the identifier of the user to search
	 *           for.
	 * @return The `UserEntity` if found, otherwise throws a
	 *         `RestRNotFoundException`.
	 * @throws RestRNotFoundException If a user with the provided ID is not found in
	 *                                the database.
	 */
	public UserEntity ifExistReturn(UserId id)// check if exists in DB then returns
	{
		return this.userCrud.findById(id.toString())
				.orElseThrow(() -> new RestRNotFoundException("User with email " + id.getEmail() + " doesn't exist"));
	}

	/**
	 * Checks if a user with the provided ID has a specific role.
	 * 
	 * @param id   The user ID to check.
	 * @param role The role to check against.
	 * @return True if the user has the specified role, false otherwise. If the user
	 *         is not found, it returns false (consider throwing an exception if
	 *         needed).
	 * @throws RestRNotFoundException If a user with the provided ID is not found in
	 *                                the database.
	 */

	public boolean isRole(UserId id, RoleEnum role)

	{
		return this.userCrud.findById(id.toString())
				.orElseThrow(() -> new RestRNotFoundException("User with email " + id.getEmail() + " doesn't exist"))
				.getRole() == roleConverter.toEntityEnum(role);
	}

	/**
	 * Checks if a user with the provided ID has a specific role.
	 * 
	 * @param id The user ID to check.
	 * @return the role in Boundary form.
	 * @throws RestRNotFoundException If a user with the provided ID is not found in
	 *                                the database.
	 */
	public RoleEnum getRole(UserId id)

	{
		return roleConverter.toBoundaryEnum(userCrud.findById(id.toString())
				.orElseThrow(() -> new RestRNotFoundException("User with email " + id.getEmail() + " doesn't exist")
				).getRole());
	}

	/**
	 * Validates an email address using a regular expression pattern.
	 * 
	 * @param email the email address to be validated
	 * @return true if the email address is valid, false otherwise
	 */
	public boolean isValidEmail(String email) {
		// Check if the email is null
		if (validTestsString.isNullorBlank(email)) {
			return false;
		}
		// Match the email with the pattern
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

}
