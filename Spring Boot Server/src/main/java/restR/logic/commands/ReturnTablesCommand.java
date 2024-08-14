package restR.logic.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import restR.boundaries.MiniAppCommandBoundary;
import restR.boundaries.ObjectBoundary;
import restR.crud.ObjectCrud;
import restR.general.MyRestaurant;
import restR.general.Reservation;
import restR.logic.converters.ObjectConverter;
import restR.logic.exceptions.RestRBadRequestException;

@Component("returnTables")
public class ReturnTablesCommand implements Command {
	private ObjectCrud objectCrud;
	private ObjectConverter objConverter;
	private static final String TYPE = "Table";
	private ObjectMapper mapper;
	private MyRestaurant rest;

	public ReturnTablesCommand(ObjectCrud objectCrud, ObjectConverter objConverter, MyRestaurant rest) {
		super();
		this.objectCrud = objectCrud;
		this.objConverter = objConverter;
		this.rest = rest;
		mapper = new ObjectMapper();
	}

	@Override
	@Transactional
	public List<Object> invoke(MiniAppCommandBoundary Command) throws JsonProcessingException, ParseException {
		int count = 0;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		Map<String, Object> commandAttributes = Command.getCommandAttributes();

		if (commandAttributes == null) {
			throw new RestRBadRequestException("Command attributes cannot be null");
		}

		Integer page = (Integer) commandAttributes.getOrDefault("page", 0);
		Integer size = (Integer) commandAttributes.getOrDefault("size", 5);

		if (commandAttributes.get("capacity") == null) {
			throw new RestRBadRequestException("capacity is required in command attributes");
		}

		Integer userCapacity;
		try {
			userCapacity = (Integer)commandAttributes.get("capacity");
		} catch (NumberFormatException e) {
			throw new RestRBadRequestException("capacity must be a valid integer");
		}

		if (commandAttributes.get("date") == null) {
			throw new RestRBadRequestException("date is required in command attributes");
		}

		Date date;
		try {
			date = dateFormat.parse(String.valueOf(commandAttributes.get("date")));
		} catch (ParseException e) {
			throw new RestRBadRequestException("date must be in the correct format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		}

		if (page == null || size == null) {
			throw new RestRBadRequestException("page and size must not be null");
		}

		List<ObjectBoundary> objectBoundaries;
		List<Object> rvObj = new ArrayList<>();
		Map<String, Object> objectDetails;
		ArrayList<Reservation> reservations;
		String json = "";
		do {
			objectBoundaries = this.objectCrud
					.findAllByTypeAndAliasAndActiveTrue(TYPE, String.valueOf(userCapacity + count),
							PageRequest.of(page, size, Direction.ASC, "type", "objectId"))
					.stream().parallel().map(this.objConverter::toBoundary).peek(System.err::println).toList();

			if (!objectBoundaries.isEmpty()) {
				for (ObjectBoundary objectBoundary : objectBoundaries) {
					objectDetails = objectBoundary.getObjectDetails();
					json = mapper.writeValueAsString(objectDetails.get("Reservations"));
					reservations = mapper.readValue(json, new TypeReference<ArrayList<Reservation>>() {
					});

					if (reservations == null || reservations.isEmpty()) {
						rvObj.add(objectBoundary);
					} else {
						int reserved;
						int myReq;
						boolean isOccupied = false;
						for (Reservation res : reservations) {
							reserved = res.getDate().getDay() * 100 + res.getDate().getHours();
							myReq = date.getDay() * 100 + date.getHours();

							if (Math.abs(myReq - reserved) < 2) {
								isOccupied = true;
								break;
							}
						}
						if (!isOccupied)
							rvObj.add(objectBoundary);
					}
				}
			}
			if (!rvObj.isEmpty())
				return rvObj;

			count++;
		} while (userCapacity + count <rest.getTableMaximumCapacity());

		return rvObj;
	}

}
