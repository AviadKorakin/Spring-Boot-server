package restR.logic.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.MessagingException;
import restR.boundaries.MiniAppCommandBoundary;
import restR.boundaries.ObjectBoundary;
import restR.crud.ObjectCrud;
import restR.entities.ObjectEntity;
import restR.general.Reservation;
import restR.logic.converters.ObjectConverter;
import restR.logic.exceptions.RestRBadRequestException;
import restR.logic.exceptions.RestRNotFoundException;
import restR.logic.logicInterfaces.EmailLogic;

@Component("setAReservation")
public class SetAReservationCommand implements Command {

	private ObjectCrud objectCrud;
	private ObjectConverter objConverter;
	private EmailLogic emailLogic;
	private ObjectMapper mapper;

	public SetAReservationCommand(ObjectCrud objectCrud, ObjectConverter objConverter, EmailLogic emailLogic) {
		super();
		this.objectCrud = objectCrud;
		this.objConverter = objConverter;
		this.emailLogic = emailLogic;
		this.mapper = new ObjectMapper();
	}

	@Override
	@Transactional
	public List<Object> invoke(MiniAppCommandBoundary Command) throws JsonProcessingException, ParseException {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	    // Validate Command and its TargetObject
	    if (Command.getTargetObject() == null || Command.getTargetObject().getObjectId() == null) {
	        throw new RestRBadRequestException("Target object and its ID cannot be null");
	    }

	    // Fetch the table
	    ObjectBoundary myTable = this.objConverter.toBoundary(
	            this.objectCrud.findById(Command.getTargetObject().getObjectId().toString())
	                    .orElseThrow(() -> new RestRNotFoundException("Table with id " +
	                            Command.getTargetObject().getObjectId().toString() + " doesn't exist")));

	    Map<String, Object> objectDetails = myTable.getObjectDetails();
	    Map<String, Object> commandAttributes = Command.getCommandAttributes();

	    if (commandAttributes == null) {
	        throw new RestRBadRequestException("Command attributes cannot be null");
	    }

	    // Validate date attribute
	    if (commandAttributes.get("date") == null) {
	        throw new RestRBadRequestException("date is required in command attributes");
	    }

	    Date date;
	    try {
	        date = dateFormat.parse(String.valueOf(commandAttributes.get("date")));
	    } catch (ParseException e) {
	        throw new RestRBadRequestException("date must be in the correct format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	    }

	    // Validate phone number
	    String phoneNumber = (String) commandAttributes.get("phoneNumber");
	    if (phoneNumber == null || !phoneNumber.matches("\\d{2,3}-\\d{7}")) {
	        throw new RestRBadRequestException("phone number must be in the format like");
	    }
	    String notes = (String) commandAttributes.getOrDefault("notes",null);
	    
	    String json = "";
	    ArrayList<Reservation> reservations;

	    json = mapper.writeValueAsString(objectDetails.get("Reservations"));
	    reservations = mapper.readValue(json, new TypeReference<ArrayList<Reservation>>() {});

	    if (reservations == null || reservations.isEmpty()) {
	        reservations = new ArrayList<Reservation>();
	    }

	    Reservation newReservation = new Reservation(date, Command.getInvokedBy(), phoneNumber,notes);
	    reservations.add(newReservation);
	    Map<String, Object> rv = new LinkedHashMap<String, Object>();

	    // Validate table number and location description
	    Integer tableNumber = (Integer) objectDetails.get("tableNumber");
	    String locationDesc = (String) objectDetails.get("locationDesc");

	    if (tableNumber == null) {
	        throw new RestRBadRequestException("table number cannot be null or empty");
	    }

	    if (locationDesc == null || locationDesc.isEmpty()) {
	        throw new RestRBadRequestException("location description cannot be null or empty");
	    }

	    rv.put("tableNumber", tableNumber);
	    rv.put("locationDesc", locationDesc);
	    rv.put("Reservations", reservations);
	    myTable.setObjectDetails(rv);

	    ObjectEntity myTableEntity = this.objectCrud.save(this.objConverter.toEntity(myTable));

	    try {
	        emailLogic.ReservationMessage(Command.getInvokedBy().getUserId().getEmail(),
	                tableNumber.toString(), locationDesc, date.getDay(), date.getHours());
	    } catch (MessagingException e) {
	        throw new RestRBadRequestException(e.getMessage());
	    }

	    ArrayList<Object> rq = new ArrayList<>();
	    rq.add(this.objConverter.toBoundary(myTableEntity));
	    return rq;
	}



}
