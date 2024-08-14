package restR.logic.validTests;

import org.springframework.stereotype.Component;

import restR.boundaries.ObjectBoundary;
import restR.crud.ObjectCrud;
import restR.entities.ObjectEntity;
import restR.general.Location;
import restR.general.ObjectId;
import restR.logic.exceptions.RestRBadRequestException;
import restR.logic.exceptions.RestRConflictException;
import restR.logic.exceptions.RestRNotFoundException;

@Component
public class ValidTestsObject {
	private ObjectCrud objectCrud;
	private ValidTestsString validTestsString;

	public ValidTestsObject(ObjectCrud objectCrud, ValidTestsString validTestsString) {
		this.objectCrud = objectCrud;
		this.validTestsString = validTestsString;
	}

	/**
	 * This method checks if a provided Location object represents valid geographic
	 * coordinates.
	 * 
	 * @param loc The Location object containing latitude and longitude values.
	 * @return True if both latitude and longitude are valid, False otherwise.
	 */
	public boolean isValidLocation(Location loc) {

		// Check for valid range of latitude (-90 to 90 degrees)
		if (loc.getLat() < -90 || loc.getLat() > 90) {
			return false;
		}

		// Check for valid range of longitude (-180 to 180 degrees)
		if (loc.getLng() < -180 || loc.getLng() > 180) {
			return false;
		}

		return true;
	}

	public void TestNotNullsorBlankParams(ObjectBoundary boundary) {
		if (boundary.getObjectDetails() == null || boundary.getCreatedBy() == null
				|| boundary.getCreatedBy().getUserId() == null || boundary.getLocation() == null
				|| boundary.getLocation().getLat() == null || boundary.getLocation().getLng() == null
				|| boundary.getActive() == null
				|| validTestsString.isNullorBlank(boundary.getType())
				|| validTestsString.isNullorBlank(boundary.getAlias())
				|| validTestsString.isNullorBlank(boundary.getCreatedBy().getUserId().getEmail())
				|| validTestsString.isNullorBlank(boundary.getCreatedBy().getUserId().getSuperapp()))
			throw new RestRBadRequestException("Can't enter null arguments.");
	}

	/**
	 * This method checks if an object exists in the data store based on its ID.
	 *
	 * @param id The unique identifier of the object to check.
	 * @return The ObjectEntity if found, otherwise throws a RestRNotFoundException.
	 * @throws RestRNotFoundException If the object with the provided ID is not
	 *                                found.
	 */
	public ObjectEntity ifExistReturn(ObjectId id) {
		return this.objectCrud.findById(id.toString())
				.orElseThrow(() -> new RestRNotFoundException("Object with id " + id.getId() + " doesn't exist"));
	}

	/**
	 * This method checks if an object exists and active in the data store based on
	 * its ID.
	 *
	 * @param id The unique identifier of the object to check.
	 * @return The ObjectEntity if found, otherwise throws a RestRNotFoundException.
	 * @throws RestRNotFoundException If the object with the provided ID is not
	 *                                found.
	 */
	public ObjectEntity ifExistAndActiveReturn(ObjectId id) {
		return this.objectCrud.findByObjectIdAndActiveTrue(id.toString())
				.orElseThrow(() -> new RestRNotFoundException("Object with id " + id.getId() + " doesn't exist"));

	}

	/**
	 * This method checks if an object with the provided ID already exists in the
	 * data store. If it exists, it throws a RestRConflictException.
	 *
	 * @param id The unique identifier of the object to check.
	 * @throws RestRConflictException If the object with the provided ID already
	 *                                exists.
	 */

	public void ifExistThrows(ObjectId id)// check if not exists in DB
	{
		if (this.objectCrud.existsById(id.toString()))// already exists check
			throw new RestRConflictException("Object with id " + id.getId() + " already exist in database.");
	}
	/**
	 * This method checks if an object with the provided ID already exists in the
	 * data store. If it exists,return true else false.
	 *
	 * @param id The unique identifier of the object to check.
	 * @return true if found , false if not.
	 */
	public boolean isExist(ObjectId id)
	{
		return this.objectCrud.existsById(id.toString());
	}
	

}
