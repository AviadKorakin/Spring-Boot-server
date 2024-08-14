package restR.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import restR.boundaries.MiniAppCommandBoundary;
import restR.boundaries.UserBoundary;
import restR.logic.logicInterfaces.MiniAppCommandLogic;
import restR.logic.logicInterfaces.ObjectLogic;
import restR.logic.logicInterfaces.UserLogic;

@RestController
@RequestMapping(path = { "/superapp/admin" })
@Tag(name = "Admin Controller", description = "API for Admins")
public class AdminController {
	private UserLogic userLogic;
	private ObjectLogic objLogic;
	private MiniAppCommandLogic commandLogic;

	public AdminController(UserLogic usrLogic, ObjectLogic objLogic, MiniAppCommandLogic commandLogic) {
		this.userLogic = usrLogic;
		this.objLogic = objLogic;
		this.commandLogic = commandLogic;
	}

	@Operation(summary = "Delete all users from database")
	@DeleteMapping(path = { "/users" })
	public void delteAllUsers(
			@RequestParam(name = "userSuperapp") String userSuperapp,
			@RequestParam(name = "userEmail") String email
			) {

		this.userLogic.deleteAll(userSuperapp, email);

	}

	@Operation(summary = "Delete all objects from database")
	@DeleteMapping(path = { "/objects" })
	public void delteAllObjects(
			@RequestParam(name = "userSuperapp") String userSuperapp,
			@RequestParam(name = "userEmail") String email
			) {
		this.objLogic.deleteAll(userSuperapp, email);
	}

	@Operation(summary = "Delete all mini app commands from database")
	@DeleteMapping(path = { "/miniapp" })
	public void DeleteAllCommands(
			@RequestParam(name = "userSuperapp") String userSuperapp,
			@RequestParam(name = "userEmail") String email
			) {
		this.commandLogic.deleteAll(userSuperapp, email);
	}

	@Operation(summary = "Export all users")
	@GetMapping(path = { "/users" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] ExportAllUsers(
			@RequestParam(name = "userSuperapp") String userSuperapp,
			@RequestParam(name = "userEmail") String email,
			@RequestParam(name = "size", defaultValue = "5", required = false) int size,
			@RequestParam(name = "page", defaultValue = "0", required = false) int page
			) {
		return this.userLogic.ExportAllUsers(userSuperapp, email, size, page).toArray(new UserBoundary[0]);

	}

	@Operation(summary = "Export all mini app commands")
	@GetMapping(path = { "/miniapp" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public MiniAppCommandBoundary[] ExportAllMiniAppCommands(
			@RequestParam(name = "userSuperapp") String userSuperapp,
			@RequestParam(name = "userEmail") String email,
			@RequestParam(name = "size", defaultValue = "5", required = false) int size,
			@RequestParam(name = "page", defaultValue = "0", required = false) int page
			) {

		return this.commandLogic.ExportAllMiniAppCommands(userSuperapp, email, size, page)
				.toArray(new MiniAppCommandBoundary[0]);
	}

	@Operation(summary = "Export specific mini app commands")
	@GetMapping(path = { "/miniapp/{miniAppName}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public MiniAppCommandBoundary[] searchByminiApp(
			@PathVariable("miniAppName") String miniApp,
			@RequestParam(name = "userSuperapp") String userSuperapp,
			@RequestParam(name = "userEmail") String email,
			@RequestParam(name = "size", defaultValue = "5", required = false) int size,
			@RequestParam(name = "page", defaultValue = "0", required = false) int page
			) {

		return this.commandLogic.ExportSpecificMiniAppCommands(userSuperapp, email, miniApp, size, page)
				.toArray(new MiniAppCommandBoundary[0]);

	}
}
