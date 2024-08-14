package restR.logic.implementations;

import restR.logic.exceptions.RestRBadRequestException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import restR.boundaries.ObjectBoundary;
import restR.crud.ObjectCrud;
import restR.entities.LocationEnum;
import restR.entities.ObjectEntity;
import restR.entities.RoleEnum;
import restR.general.Location;
import restR.general.ObjectId;
import restR.general.UserId;
import restR.logic.converters.LocationToPointConverter;
import restR.logic.converters.ObjectConverter;
import restR.logic.exceptions.RestRNotFoundException;
import restR.logic.exceptions.RestRUnauthorizedException;
import restR.logic.logicInterfaces.ObjectLogic;
import restR.logic.validTests.ValidTestsObject;
import restR.logic.validTests.ValidTestsString;
import restR.logic.validTests.ValidTestsUser;

@Service
public class ObjectLogicImplementation implements ObjectLogic {
	private ObjectCrud objectCrud;
	private ObjectConverter objConverter;
	private LocationToPointConverter ltpConverter;
	private ValidTestsObject validTestsObject;
	private ValidTestsUser validTestsUser;
	private ValidTestsString validTestsString;
	private String superappName;

	public ObjectLogicImplementation(ObjectCrud objectCrud, ObjectConverter objConverter,
			LocationToPointConverter ltpConverter, ValidTestsObject validTestsObject, ValidTestsUser validTestsUser,
			ValidTestsString validTestsString) {
		this.objectCrud = objectCrud;
		this.objConverter = objConverter;
		this.ltpConverter = ltpConverter;
		this.validTestsObject = validTestsObject;
		this.validTestsUser = validTestsUser;
		this.validTestsString = validTestsString;
	}

	@Override
	@Transactional
	public ObjectBoundary storeInDatabase(String eSuperapp, String email, ObjectBoundary newObj) {
		newObj.getCreatedBy().setUserId(new UserId(eSuperapp, email));
		RoleEnum role = validTestsUser.getRole(newObj.getCreatedBy().getUserId());
		if (role != RoleEnum.SUPERAPP_USER) {
			throw new RestRUnauthorizedException("Denied! out of your premmisions");
		} else {
			if (newObj.getObjectId() == null)
				newObj.setObjectId(new ObjectId());
			newObj.getObjectId().setSuperapp(superappName);
			do {
				newObj.getObjectId().setId(UUID.randomUUID().toString());
			} while (validTestsObject.isExist(newObj.getObjectId()));

			validTestsObject.TestNotNullsorBlankParams(newObj);
			if (!validTestsObject.isValidLocation(newObj.getLocation()))
				throw new RestRBadRequestException("Location isn't valid");
			newObj.setCreationTimestamp(new Date());
			ObjectEntity entity = this.objConverter.toEntity(newObj);

			
			entity = this.objectCrud.save(entity);
			return this.objConverter.toBoundary(entity);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ObjectBoundary getSpecificObjectFromDatabase(String userSuperapp, String email, String superapp, String id) {
		RoleEnum role = validTestsUser.getRole(new UserId(userSuperapp, email));
		if (role == RoleEnum.ADMIN) {
			throw new RestRUnauthorizedException("Denied! out of your premmisions");
		} else {
			ObjectBoundary rv = this.objectCrud.findById(new ObjectId(superapp, id).toString())
					.map(entity -> this.objConverter.toBoundary(entity)).orElseThrow(
							() -> new RestRNotFoundException("Couldn't find object named " + id + " in database."));
			if (role == RoleEnum.MINIAPP_USER && rv.getActive() == false) {
				throw new RestRNotFoundException("Object with the id " + id + " not found");
			}

			return rv;
		}
	}

	@Override
	@Transactional
	public void updateById(String userSuperapp, String email, String eSuperapp, String id, ObjectBoundary boundary) {
		if (validTestsUser.isRole(new UserId(userSuperapp, email), RoleEnum.SUPERAPP_USER) == false)
			throw new RestRUnauthorizedException("Denied! out of your premmisions");
		ObjectEntity existing = validTestsObject.ifExistReturn(new ObjectId(eSuperapp, id));
		if (!validTestsString.isNullorBlank(boundary.getAlias()))
			existing.setAlias(boundary.getAlias());
		if (boundary.getActive() != null)
			existing.setActive(boundary.getActive());
		if (boundary.getObjectDetails() != null && boundary.getObjectDetails().isEmpty() == false)
			existing.setObjectDetails(boundary.getObjectDetails());
		if (!validTestsString.isNullorBlank(boundary.getType()))
			existing.setType(boundary.getType());
		if (boundary.getLocation() != null && boundary.getLocation().getLat() != null
				&& boundary.getLocation().getLng() != null
				&& !validTestsObject.isValidLocation(boundary.getLocation())) {
			existing.setGeom(ltpConverter.fromLocationToPoint(
					new Location(boundary.getLocation().getLat(), boundary.getLocation().getLng())));
		}
		this.objectCrud.save(existing);
	}

	@Override
	@Deprecated
	public List<ObjectBoundary> getAll() {
		throw new RestRBadRequestException("deprecated operation");
		/*
		 * List<ObjectEntity> entities = this.objectCrud.findAll();
		 * 
		 * List<ObjectBoundary> rv = new ArrayList<>();
		 * 
		 * for (ObjectEntity entity : entities) {
		 * rv.add(this.objConverter.toBoundary(entity)); } if (rv.isEmpty()) throw new
		 * RestRNotFoundException("NO DATA."); return rv;
		 */
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAll(int size, int page, String userSuperapp, String email) {
		List<ObjectBoundary> Boundaries;
		RoleEnum role = validTestsUser.getRole(new UserId(userSuperapp, email));
		if (role == RoleEnum.ADMIN) {
			throw new RestRUnauthorizedException("Denied! out of your premmisions");
		} else {
			if (role == RoleEnum.MINIAPP_USER) {
				Boundaries = this.objectCrud
						.findAllByActiveTrue(PageRequest.of(page, size, Direction.ASC, "type", "objectId")).stream()
						.parallel().map(this.objConverter::toBoundary).peek(System.err::println).toList();

			} else {
				Boundaries = this.objectCrud.findAll(PageRequest.of(page, size, Direction.ASC, "type", "objectId"))
						.stream().parallel().map(this.objConverter::toBoundary).peek(System.err::println).toList();
			}
			return Boundaries;
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllByType(String type, int size, int page, String userSuperapp, String email) {

		List<ObjectBoundary> Boundaries;
		RoleEnum role = validTestsUser.getRole(new UserId(userSuperapp, email));
		if (role == RoleEnum.ADMIN) {
			throw new RestRUnauthorizedException("Denied! out of your premmisions");
		} else {
			if (role == RoleEnum.MINIAPP_USER) {
				Boundaries = this.objectCrud
						.findAllByTypeAndActiveTrue(type, PageRequest.of(page, size, Direction.ASC, "type", "objectId"))
						.stream().parallel().map(this.objConverter::toBoundary).peek(System.err::println).toList();

			} else {
				Boundaries = this.objectCrud
						.findAllByType(type, PageRequest.of(page, size, Direction.ASC, "type", "objectId")).stream()
						.parallel().map(this.objConverter::toBoundary).peek(System.err::println).toList();
			}
			return Boundaries;
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllByAlias(String alias, int size, int page, String userSuperapp, String email) {
		List<ObjectBoundary> Boundaries;
		RoleEnum role = validTestsUser.getRole(new UserId(userSuperapp, email));
		if (role == RoleEnum.ADMIN) {
			throw new RestRUnauthorizedException("Denied! out of your premmisions");
		} else {
			if (role == RoleEnum.MINIAPP_USER) {
				Boundaries = this.objectCrud
						.findAllByAliasAndActiveTrue(alias,
								PageRequest.of(page, size, Direction.ASC, "alias", "objectId"))
						.stream().parallel().map(this.objConverter::toBoundary).peek(System.err::println).toList();
			} else {
				Boundaries = this.objectCrud
						.findAllByAlias(alias, PageRequest.of(page, size, Direction.ASC, "alias", "objectId")).stream()
						.parallel().map(this.objConverter::toBoundary).peek(System.err::println).toList();
			}
			return Boundaries;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllByPattern(String pattern, int size, int page, String userSuperapp,
			String email) {
		List<ObjectBoundary> Boundaries;
		RoleEnum role = validTestsUser.getRole(new UserId(userSuperapp, email));
		if (role == RoleEnum.ADMIN) {
			throw new RestRUnauthorizedException("Denied! out of your premmisions");
		} else {
			if (role == RoleEnum.MINIAPP_USER) {
				Boundaries = this.objectCrud
						.findAllByAliasContainingAndActiveTrue(pattern,
								PageRequest.of(page, size, Direction.ASC, "alias", "objectId"))
						.stream().parallel().map(this.objConverter::toBoundary).peek(System.err::println).toList();

			} else {
				Boundaries = this.objectCrud
						.findAllByAliasContaining(pattern,
								PageRequest.of(page, size, Direction.ASC, "alias", "objectId"))
						.stream().parallel().map(this.objConverter::toBoundary).peek(System.err::println).toList();
			}
			return Boundaries;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllBySpecificDistance(LocationEnum edistanceUnits, Double lat, Double lng, Double distance,
			int size, int page, String userSuperapp, String email) {
		String distanceUnits = edistanceUnits.toString();
		Location loc = new Location(lat, lng);

		if (!validTestsObject.isValidLocation(loc))
			throw new RestRBadRequestException("Location isn't valid");

		List<ObjectBoundary> Boundaries;
		RoleEnum role = validTestsUser.getRole(new UserId(userSuperapp, email));
		if (role == RoleEnum.ADMIN) {
			throw new RestRUnauthorizedException("Denied! out of your premmisions");
		} else {
			if (role == RoleEnum.MINIAPP_USER) {
				if(distanceUnits.equals("NEUTRAL"))//DEGREES
				{
					Boundaries = this.objectCrud
							.findAllByDistanceAndActiveTrue(ltpConverter.fromLocationToPoint(loc), distance,
									PageRequest.of(page, size, Direction.ASC, "objectId"))
							.stream().parallel().map(this.objConverter::toBoundary).peek(System.err::println).toList();
				}
				else
				{
				Boundaries = this.objectCrud
						.findAllByDistanceAndActiveTrue(distanceUnits, ltpConverter.fromLocationToPoint(loc), distance,
								PageRequest.of(page, size, Direction.ASC, "objectId"))
						.stream().parallel().map(this.objConverter::toBoundary).peek(System.err::println).toList();
				}
			} else {
				if(distanceUnits.equals("NEUTRAL"))//DEGREES
				{
					Boundaries = this.objectCrud
							.findAllByDistance(ltpConverter.fromLocationToPoint(loc), distance,
									PageRequest.of(page, size, Direction.ASC, "objectId"))
							.stream().parallel().map(this.objConverter::toBoundary).peek(System.err::println).toList();
				}
				else
				{
				Boundaries = this.objectCrud
						.findAllByDistance(distanceUnits, ltpConverter.fromLocationToPoint(new Location(lat, lng)),
								distance, PageRequest.of(page, size, Direction.ASC, "objectId"))
						.stream().parallel().map(this.objConverter::toBoundary).peek(System.err::println).toList();
				}
			}
			return Boundaries;
		}
	}

	@Override
	@Transactional
	public void deleteAll(String userSuperapp, String email) {

		if (validTestsUser.isRole(new UserId(userSuperapp, email), RoleEnum.ADMIN) == false)
			throw new RestRUnauthorizedException("Denied! out of your premmisions");
		this.objectCrud.deleteAll();

	}

	@Value("${spring.application.name:supperapp}")
	private void setSuperappName(String superappName) {
		this.superappName = superappName;
	}

}