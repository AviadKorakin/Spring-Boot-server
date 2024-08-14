package restR.logic.logicInterfaces;

import java.util.List;

import restR.boundaries.ObjectBoundary;
import restR.entities.LocationEnum;
import restR.logic.exceptions.RestRBadRequestException;
import restR.logic.exceptions.RestRNotFoundException;
import restR.logic.exceptions.RestRUnauthorizedException;

public interface ObjectLogic {
	
	/**
	 * Stores a new object in the database with access control based on user role.
	 * 
	 * This method validates and stores a new {@link ObjectBoundary} object in the
	 * database. It throws an exception if the user creating the object has ADMIN or
	 * MINIAPP_USER role.
	 * 
	 * @param eSuperapp    The super application of the user creating the object.
	 * @param email        The email address of the user creating the object.
	 * @param newObj       A {@link ObjectBoundary} object containing the new object
	 *                     information.
	 * @param superappName The super application the object belongs to. (assumed to
	 *                     be available in context)
	 * @return An {@link ObjectBoundary} object representing the stored object.
	 * @throws RestRUnauthorizedException If the user creating the object has either
	 *                                    ADMIN or MINIAPP_USER role.
	 * @throws RestRBadRequestException   If any validation on the new object fails
	 *                                    or the object already exists.
	 */
	public ObjectBoundary storeInDatabase(String userSuperapp, String email, ObjectBoundary newObj);
	/**
	 * Retrieves a specific object from the database with access control based on
	 * user role.
	 * 
	 * This method fetches an object based on its super application and ID. It
	 * performs access control checks based on the requesting user's role.
	 * 
	 * - ADMIN users are not allowed to access objects through this method (should
	 * use a separate method). - MINIAPP_USER users can only access active objects.
	 * 
	 * @param userSuperapp The super application of the requesting user.
	 * @param email        The email address of the requesting user.
	 * @param superapp     The super application of the object to retrieve.
	 * @param id           The ID of the object to retrieve.
	 * @return An {@link ObjectBoundary} object representing the retrieved object.
	 * @throws RestRUnauthorizedException If the requesting user is an ADMIN.
	 * @throws RestRNotFoundException     If the object with the specified ID is not
	 *                                    found, or if the requesting user is a
	 *                                    MINIAPP_USER and the object is inactive.
	 */
	public ObjectBoundary getSpecificObjectFromDatabase(String userSuperapp ,String email, String superapp, String id);
	
	/**
	 * Updates a specific object in the database with access control based on user
	 * role.
	 * 
	 * This method updates specific fields of an object identified by super
	 * application and ID. It performs an access control check to ensure only
	 * SUPERAPP_USER can update objects.
	 * 
	 * @param userSuperapp The super application of the requesting user.
	 * @param email        The email address of the requesting user.
	 * @param eSuperapp    The super application of the object to update.
	 * @param id           The ID of the object to update.
	 * @param boundary     An {@link ObjectBoundary} object containing the updated
	 *                     object information.
	 * @throws RestRUnauthorizedException If the requesting user is not a
	 *                                    SUPERAPP_USER.
	 * @throws RestRBadRequestException   If any of the updated fields in the
	 *                                    boundary object are invalid (e.g., empty
	 *                                    alias, invalid location coordinates).
	 */
	public void updateById(String userSuperapp,String email,String eSuperapp,String id, ObjectBoundary boundary);
	@Deprecated
	public List<ObjectBoundary> getAll();
	/**
	 * Retrieves a paginated list of objects from the database with access control
	 * based on user role.
	 * 
	 * This method fetches a list of objects with pagination and filtering based on
	 * the requesting user's role.
	 * 
	 * - ADMIN users are not allowed to access objects through this method (should
	 * use a separate method). - MINIAPP_USER users can only access active objects.
	 * 
	 * @param size         The number of objects to retrieve per page.
	 * @param page         The current page number (zero-based indexing).
	 * @param userSuperapp The super application of the requesting user.
	 * @param email        The email address of the requesting user.
	 * @return A list of {@link ObjectBoundary} objects representing the retrieved
	 *         objects.
	 * @throws RestRUnauthorizedException If the requesting user is an ADMIN.
	 * @throws RestRNotFoundException     If no objects are found based on the
	 *                                    user's role and filtering.
	 */
	public List<ObjectBoundary> getAll(int size, int page, String userSuperapp, String email);

	/**
	 * Deletes all objects in the database (ADMIN access only).
	 * 
	 * This method deletes all objects from the database. It performs an access
	 * control check to ensure only users with the ADMIN role can execute this
	 * operation.
	 * 
	 * @param userSuperapp The super application of the requesting user.
	 * @param email        The email address of the requesting user.
	 * @throws RestRUnauthorizedException If the requesting user does not have the
	 *                                    ADMIN role.
	 */
	public void deleteAll(String userSuperapp, String email);
	/**
	 * Retrieves a paginated list of objects of a specific type from the database
	 * with access control based on user role.
	 * 
	 * This method fetches a list of objects with pagination and filtering based on
	 * the requesting user's role and the provided object type.
	 * 
	 * - ADMIN users are not allowed to access objects through this method (should
	 * use a separate method). - MINIAPP_USER users can only access active objects
	 * of the specified type.
	 * 
	 * @param type         The type of object to retrieve.
	 * @param size         The number of objects to retrieve per page.
	 * @param page         The current page number (zero-based indexing).
	 * @param userSuperapp The super application of the requesting user.
	 * @param email        The email address of the requesting user.
	 * @return A list of {@link ObjectBoundary} objects representing the retrieved
	 *         objects.
	 * @throws RestRUnauthorizedException If the requesting user is an ADMIN.
	 * @throws RestRNotFoundException     If no objects of the specified type are
	 *                                    found based on the user's role and
	 *                                    filtering.
	 */
	public List<ObjectBoundary> getAllByType(String type,int size, int page, String userSuperapp, String email);
	/**
	 * Retrieves a paginated list of objects with a specific alias from the database
	 * with access control based on user role.
	 * 
	 * This method fetches a list of objects with pagination and filtering based on
	 * the requesting user's role and the provided object alias.
	 * 
	 * - ADMIN users are not allowed to access objects through this method (should
	 * use a separate method). - MINIAPP_USER users can only access active objects
	 * with the specified alias.
	 * 
	 * @param alias        The alias of the object to retrieve.
	 * @param size         The number of objects to retrieve per page.
	 * @param page         The current page number (zero-based indexing).
	 * @param userSuperapp The super application of the requesting user.
	 * @param email        The email address of the requesting user.
	 * @return A list of {@link ObjectBoundary} objects representing the retrieved
	 *         objects.
	 * @throws RestRUnauthorizedException If the requesting user is an ADMIN.
	 * @throws RestRNotFoundException     If no objects with the specified alias are
	 *                                    found based on the user's role and
	 *                                    filtering.
	 */
	public List<ObjectBoundary> getAllByAlias(String alias,int size, int page, String userSuperapp, String email);
	/**
	 * Retrieves a paginated list of objects with aliases matching a pattern from
	 * the database with access control based on user role.
	 * 
	 * This method fetches a list of objects with pagination and filtering based on
	 * the requesting user's role and a pattern to match object aliases.
	 * 
	 * - ADMIN users are not allowed to access objects through this method (should
	 * use a separate method). - MINIAPP_USER users can only access active objects
	 * with aliases matching the pattern.
	 * 
	 * @param pattern      The pattern to match object aliases (supports wildcards).
	 * @param size         The number of objects to retrieve per page.
	 * @param page         The current page number (zero-based indexing).
	 * @param userSuperapp The super application of the requesting user.
	 * @param email        The email address of the requesting user.
	 * @return A list of {@link ObjectBoundary} objects representing the retrieved
	 *         objects.
	 * @throws RestRUnauthorizedException If the requesting user is an ADMIN.
	 * @throws RestRNotFoundException     If no objects with aliases matching the
	 *                                    pattern are found based on the user's role
	 *                                    and filtering.
	 */
	public List<ObjectBoundary> getAllByPattern(String pattern,int size, int page, String userSuperapp, String email);
	/**
	 * Retrieves a paginated list of objects within a specific distance from a
	 * location with access control based on user role.
	 * 
	 * This method fetches a list of objects with pagination and filtering based on
	 * the requesting user's role, a provided location (latitude and longitude),
	 * distance unit, and search radius.
	 * 
	 * - ADMIN users are not allowed to access objects through this method (should
	 * use a separate method). - MINIAPP_USER users can only access active objects
	 * within the specified distance from the location.
	 * 
	 * @param distanceUnits The unit of distance used for the search (e.g., "KM",
	 *                      "M").
	 * @param lat           The latitude of the location to search from.
	 * @param lng           The longitude of the location to search from.
	 * @param distance      The radius of the search area in the specified distance
	 *                      unit.
	 * @param size          The number of objects to retrieve per page.
	 * @param page          The current page number (zero-based indexing).
	 * @param userSuperapp  The super application of the requesting user.
	 * @param email         The email address of the requesting user.
	 * @return A list of {@link ObjectBoundary} objects representing the retrieved
	 *         objects within the search area.
	 * @throws RestRUnauthorizedException If the requesting user is an ADMIN.
	 * @throws RestRNotFoundException     If no objects are found within the
	 *                                    specified distance based on the user's
	 *                                    role and filtering.
	 */

	public List<ObjectBoundary> getAllBySpecificDistance(LocationEnum distanceUnits, Double lat, Double lng, Double distance,
			int size, int page, String userSuperapp, String email);


}
