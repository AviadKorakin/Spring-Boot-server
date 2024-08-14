package restR.logic.validTests;

import org.springframework.stereotype.Component;

import restR.boundaries.MiniAppCommandBoundary;
import restR.crud.MiniAppCommandCrud;
import restR.entities.MiniAppCommandEntity;
import restR.general.CommandId;
import restR.general.ObjectId;
import restR.logic.exceptions.RestRBadRequestException;
import restR.logic.exceptions.RestRConflictException;
import restR.logic.exceptions.RestRNotFoundException;

@Component
public class ValidTestsMiniAppCommand {
	private MiniAppCommandCrud miniAppCommandCrud;
	private ValidTestsString validTestsString;



	public ValidTestsMiniAppCommand(MiniAppCommandCrud miniAppCommandCrud, ValidTestsString validTestsString) {
		this.miniAppCommandCrud = miniAppCommandCrud;
		this.validTestsString = validTestsString;
	}

	/**
	 * This method checks if any of the parameters in the provided
	 * `MiniAppCommandBoundary` object are null.
	 * 
	 * It throws a `RestRBadRequestException` if any of the following properties are
	 * null
	 * 
	 * @param boundary The `MiniAppCommandBoundary` object containing command
	 *                 details.
	 * @throws RestRBadRequestException If any of the required parameters in the
	 *                                  boundary object are null.
	 */
	public void TestNotNullsorBlankParams(MiniAppCommandBoundary boundary) {
		if (boundary.getCommandAttributes() == null ||  boundary.getInvokedBy() == null 
				//boundary.getTargetObject() == null
				
				//|| validTestsString.isNullorBlank(boundary.getTargetObject().getObjectId().getSuperapp())
				//|| validTestsString.isNullorBlank(boundary.getTargetObject().getObjectId().getId())
				|| validTestsString.isNullorBlank(boundary.getInvokedBy().getUserId().getSuperapp())
				|| validTestsString.isNullorBlank(boundary.getInvokedBy().getUserId().getEmail())
				|| validTestsString.isNullorBlank(boundary.getCommand()))

			throw new RestRBadRequestException("Can't enter null arguments.");
	}

	/**
	 * This method checks if a MiniAppCommandEntity exists in the data store based
	 * on its CommandId.
	 *
	 * @param id The unique CommandId of the MiniAppCommand to check.
	 * @return The MiniAppCommandEntity if found, otherwise throws a
	 *         RestRNotFoundException.
	 * @throws RestRNotFoundException If the MiniAppCommand with the provided ID is
	 *                                not found.
	 */
	public MiniAppCommandEntity ifExistReturn(CommandId id) {
		return this.miniAppCommandCrud.findById(id.toString())
				.orElseThrow(() -> new RestRNotFoundException("Miniapp with id:  " + id.getId() + " doesnt exist"));
	}

	/**
	 * This method checks if a MiniAppCommandEntity with the provided CommandId
	 * already exists in the data store. If it exists, it throws a
	 * RestRConflictException.
	 *
	 * @param id The unique CommandId of the MiniAppCommand to check.
	 * @throws RestRConflictException If the MiniAppCommand with the provided ID
	 *                                already exists.
	 */
	public void ifExistThrows(CommandId id)// check if not exists in DB
	{
		if (this.miniAppCommandCrud.existsById(id.toString()))// already exists
																// check
			throw new RestRConflictException("Miniapp with id " + id.getId() + " already exist in database.");
	}
	
	/**
	 * This method checks if an object with the provided ID already exists in the
	 * data store. If it exists,return true else false.
	 *
	 * @param id The unique identifier of the object to check.
	 * @return true if found , false if not.
	 */
	public boolean isExist(CommandId id)
	{
		return this.miniAppCommandCrud.existsById(id.toString());
	}
	
	

}
