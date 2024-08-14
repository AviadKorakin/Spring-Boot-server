package restR.logic.logicInterfaces;

import java.util.ArrayList;
import java.util.List;

import restR.boundaries.MiniAppCommandBoundary;
import restR.logic.exceptions.RestRBadRequestException;
import restR.logic.exceptions.RestRUnauthorizedException;

public interface MiniAppCommandLogic {
	/**
	 * Invokes a MiniApp command and stores it in the database with access control.
	 * 
	 * This method processes a MiniApp command boundary object, performs
	 * validations, and stores it in the database. It performs access control checks
	 * to ensure:
	 * 
	 * - The command parameters are not null or blank. - The invoking user has the
	 * MINIAPP_USER role. - The target object exists and is active. - The command ID
	 * is unique.
	 * 
	 * @param newMiniCmd  The MiniApp command boundary object containing the command
	 *                    details.
	 * @param miniAppName The name of the MiniApp associated with the command.
	 * @return An ArrayList containing a single objects
	 *         representing the stored command.
	 * @throws RestRUnauthorizedException If the invoking user does not have the
	 *                                    MINIAPP_USER role.
	 * @throws RestRBadRequestException   If any validation on the command object
	 *                                    fails.
	 */
	public List<Object> invokeaCommand(MiniAppCommandBoundary newObj, String miniAppName);
	/**
	 * Exports all MiniApp commands from the database (deprecated).
	 * 
	 * **This method is deprecated and should not be used.** It was previously used
	 * to export all MiniApp commands, but it might be inefficient or have security
	 * concerns for large datasets.
	 * 
	 * @throws RestRBadRequestException Always thrown to indicate the deprecated
	 *                                  functionality.
	 * 
	 * @deprecated This method is no longer supported and will be removed in future
	 *             versions.
	 */
	@Deprecated	
	public List<MiniAppCommandBoundary> ExportAllMiniAppCommands();
	/**
	 * Deletes all MiniApp commands in the database (ADMIN access only).
	 * 
	 * This method deletes all MiniApp commands from the database. It performs an
	 * access control check to ensure only users with the ADMIN role can execute
	 * this operation.
	 * 
	 * @param userSuperapp The super application of the requesting user.
	 * @param email        The email address of the requesting user.
	 * @throws RestRUnauthorizedException If the requesting user does not have the
	 *                                    ADMIN role.
	 */
	public void deleteAll(String userSuperapp, String email);
	/**
	 * Exports all MiniApp commands for a specific MiniApp (deprecated).
	 * 
	 * **This method is deprecated and should not be used.** It was previously used
	 * to export MiniApp commands for a specific MiniApp, but it might be
	 * inefficient or have security concerns for large datasets.
	 * 
	 * @param miniApp The name of the MiniApp for which to export commands.
	 * @throws RestRBadRequestException Always thrown to indicate the deprecated
	 *                                  functionality.
	 * 
	 * @deprecated This method is no longer supported and will be removed in future
	 *             versions.
	 */
	@Deprecated
	public List<MiniAppCommandBoundary> ExportSpecificMiniAppCommands(String miniApp);
	/**
	 * Retrieves a paginated list of MiniApp commands for a specific MiniApp with
	 * access control.
	 * 
	 * This method fetches a list of MiniApp commands for a specified MiniApp with
	 * pagination. It performs an access control check to ensure only ADMIN users
	 * can access this functionality.
	 * 
	 * @param userSuperapp The super application of the requesting user.
	 * @param email        The email address of the requesting user.
	 * @param miniApp      The name of the MiniApp for which to export commands.
	 * @param size         The number of objects to retrieve per page.
	 * @param page         The current page number (zero-based indexing).
	 * @return A list of {@link MiniAppCommandBoundary} objects representing the
	 *         retrieved commands.
	 * @throws RestRUnauthorizedException If the requesting user is not an ADMIN.
	 */
	
	public List<MiniAppCommandBoundary> ExportSpecificMiniAppCommands(String userSuperapp, String email, String miniApp, int size, int page);

	/**
	 * Retrieves a paginated list of all MiniApp commands from the database with
	 * access control.
	 * 
	 * This method fetches a list of all MiniApp commands with pagination. It
	 * performs an access control check to ensure only ADMIN users can access this
	 * functionality.
	 * 
	 * @param userSuperapp The super application of the requesting user.
	 * @param email        The email address of the requesting user.
	 * @param size         The number of objects to retrieve per page.
	 * @param page         The current page number (zero-based indexing).
	 * @return A list of {@link MiniAppCommandBoundary} objects representing the
	 *         retrieved commands.
	 * @throws RestRUnauthorizedException If the requesting user is not an ADMIN.
	 */

	public List<MiniAppCommandBoundary> ExportAllMiniAppCommands(String userSuperapp, String email, int size, int page);

}
