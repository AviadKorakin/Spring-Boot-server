package restR.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import restR.boundaries.ObjectBoundary;
import restR.entities.LocationEnum;
import restR.logic.logicInterfaces.ObjectLogic;
@RestController
@RequestMapping(path = { "/superapp/objects" })
@Tag(name = "Object Controller", description = "API for objects")
public class ObjectController {
	private ObjectLogic objLogic;

	public ObjectController(ObjectLogic objLogic) {
		this.objLogic = objLogic;
	}

	
	
	@Operation(summary = "Store object in database") // TODO check if value in boudary is SUPERAPP_USER
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary storeInDatabase(
			@RequestParam(name = "userSuperapp") String userSuperapp,
			@RequestParam(name = "userEmail") String email,
			@RequestBody ObjectBoundary newuser) {
		
		return this.objLogic.storeInDatabase(userSuperapp,email,newuser);
	}

	@Operation(summary = "find specific object by id")
	@GetMapping(path = { "{superapp}/{id}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary getSpecificObject(
			@RequestParam(name = "userSuperapp") String userSuperapp,
			@RequestParam(name = "userEmail") String email,
			@PathVariable("superapp") String superapp,
			@PathVariable("id") String id) {
		return this.objLogic.getSpecificObjectFromDatabase(userSuperapp, email,superapp, id);

	}

	@Operation(summary = "update object")
	@PutMapping(path = { "{superapp}/{id}" }, consumes = { MediaType.APPLICATION_JSON_VALUE }) 
	public void updateObject(
			@RequestParam(name = "userSuperapp") String userSuperapp,
			@RequestParam(name = "userEmail") String email,
			@RequestBody ObjectBoundary update,
			@PathVariable("superapp") String eSuperapp,
			@PathVariable("id") String id)
			{
		this.objLogic.updateById(userSuperapp, email, eSuperapp, id, update);
	}

	@Operation(summary = "find all objects")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getObjects(
			@RequestParam(name = "userSuperapp") String superapp,
			@RequestParam(name = "userEmail") String email,
			@RequestParam(name = "size", defaultValue = "5", required = false) int size,
			@RequestParam(name = "page", defaultValue = "0", required = false) int page
			) {
			return this.objLogic.getAll(size, page,superapp,email).toArray(new ObjectBoundary[0]);

	}
	
	
	@Operation(summary = "find all objects by type")
	@GetMapping(path = { "/search/byType/{type}" },produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getObjectByType(
			@RequestParam(name = "userSuperapp") String superapp,
			@RequestParam(name = "userEmail") String email,
			@RequestParam(name = "size", defaultValue = "5", required = false) int size,
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@PathVariable("type") String type) {
			return this.objLogic.getAllByType(type, size, page,superapp,email).toArray(new ObjectBoundary[0]);

	}

	@Operation(summary = "find all objects by alias")
	@GetMapping(path = { "/search/byAlias/{alias}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getObjectByAlias(
			@RequestParam(name = "userSuperapp") String superapp,
			@RequestParam(name = "userEmail") String email,
			@RequestParam(name = "size", defaultValue = "5", required = false) int size,
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@PathVariable("alias") String alias) {
		return this.objLogic.getAllByAlias(alias, size, page, superapp, email).toArray(new ObjectBoundary[0]);

	}

	@Operation(summary = "find all objects by pattern") 
	@GetMapping(path = { "/search/byAliasPattern/{pattern}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getObjectByAliasPattern(
			@RequestParam(name = "userSuperapp") String superapp,
			@RequestParam(name = "userEmail") String email,
			@RequestParam(name = "size", defaultValue = "5", required = false) int size,
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@PathVariable("pattern") String pattern) {
		return this.objLogic.getAllByPattern(pattern, size, page, superapp, email)
				.toArray(new ObjectBoundary[0]);

	}

	@Operation(summary = "find all objects in sphere radius")
	@GetMapping(path = { "/search/byLocation/{lat}/{lng}/{distance}"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ObjectBoundary[] getObjectByLocation(
			@RequestParam(name = "units", defaultValue = "NEUTRAL",required = false) LocationEnum distanceUnits,
			@RequestParam(name = "userSuperapp") String superapp,
			@RequestParam(name = "userEmail") String email,
			@RequestParam(name = "size", defaultValue = "5", required = false) int size,
			@RequestParam(name = "page", defaultValue = "0", required = false) int page,
			@PathVariable("lat") Double lat,
			@PathVariable("lng") Double lng,
			@PathVariable("distance") Double distance) {
		return this.objLogic.getAllBySpecificDistance(distanceUnits, lat, lng, distance, size, page, superapp, email)
				.toArray(new ObjectBoundary[0]);

	}

}
