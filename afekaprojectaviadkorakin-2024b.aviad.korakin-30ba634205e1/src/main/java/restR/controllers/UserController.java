package restR.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import restR.boundaries.NewUserBoundary;
import restR.boundaries.UserBoundary;
import restR.logic.logicInterfaces.UserLogic;

@RestController
@RequestMapping(path = {"/superapp/users"})
@Tag(name = "User Controller", description = "API for user")
public class UserController {
	private UserLogic userLogic;

	public UserController(UserLogic userLogic) {
		this.userLogic = userLogic;
	}

	@Operation(summary = "Store user in database")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary storeInDatabase(@RequestBody NewUserBoundary newuser) {
		return this.userLogic.storeInDatabase(newuser);
	}

	@Operation(summary = "find specific user by email")
	@GetMapping(path = { "login/{superapp}/{email}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary getSpecificUser(@PathVariable("superapp") String eSuperapp,
			@PathVariable("email") String email) {
		return this.userLogic.getSpecificUserFromDatabase(eSuperapp , email);
	}

	@Operation(summary = "update user")
	@PutMapping(path = { "{superapp}/{userEmail}" }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void updateUser(@PathVariable("superapp") String eSuperapp, @PathVariable("userEmail") String email,
			@RequestBody UserBoundary update) {
		this.userLogic.updateById(eSuperapp ,email, update);
	}
	

}
