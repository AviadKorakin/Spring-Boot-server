package restR.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import restR.boundaries.MiniAppCommandBoundary;
import restR.logic.logicInterfaces.MiniAppCommandLogic;
@RestController
@RequestMapping(path = "superapp/miniapp")
@Tag(name = "Miniapp Controller", description = "API for commands")
public class MiniAppCommandController {
	private MiniAppCommandLogic miniAppCommandLogic;

	public MiniAppCommandController(MiniAppCommandLogic miniAppCommandLogic) {
		this.miniAppCommandLogic = miniAppCommandLogic;
	}

	@Operation(summary = "Invoke a Command")
	@PostMapping(path = "{miniAppName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object[] storeInDatabase(
			@PathVariable("miniAppName") String miniAppName,
			@RequestBody MiniAppCommandBoundary message) {
		return this.miniAppCommandLogic.invokeaCommand(message, miniAppName).toArray(new Object[0]);
	}
}
