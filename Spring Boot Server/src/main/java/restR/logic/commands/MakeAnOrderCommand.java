package restR.logic.commands;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import restR.general.CreatedBy;
import restR.general.Location;
import restR.general.MenuItem;
import restR.general.MyRestaurant;
import restR.general.ObjectId;
import restR.logic.converters.ObjectConverter;
import restR.logic.exceptions.RestRBadRequestException;
import restR.logic.exceptions.RestRNotFoundException;
import restR.logic.logicInterfaces.EmailLogic;

@Component("makeAnOrder")
public class MakeAnOrderCommand implements Command {

	private ObjectCrud objectCrud;
	private ObjectConverter objConverter;
	private EmailLogic emailLogic;
	private ObjectMapper mapper;
	private MyRestaurant rest;


	public MakeAnOrderCommand(ObjectCrud objectCrud, ObjectConverter objConverter, EmailLogic emailLogic,
			ObjectMapper mapper, MyRestaurant rest) {
		super();
		this.objectCrud = objectCrud;
		this.objConverter = objConverter;
		this.emailLogic = emailLogic;
		this.mapper = new ObjectMapper();
		this.rest = rest;
	}



	@Override
	@Transactional
	public List<Object> invoke(MiniAppCommandBoundary Command) throws JsonProcessingException, ParseException {
		String json;

		Map<String, Object> commandAttributes = Command.getCommandAttributes();

		if (commandAttributes == null) {
			throw new RestRBadRequestException("Command attributes cannot be null");
		}

		if (!commandAttributes.containsKey("objectIds")) {
			throw new RestRBadRequestException("objectIds are required in command attributes");
		}

		json = mapper.writeValueAsString(commandAttributes.get("objectIds"));
		String type = (String) commandAttributes.getOrDefault("type", "TAKE_AWAY");
		Double lat;
		Double lng;
		if (type.equals("DELIVERY")) {
			lat = (Double) commandAttributes.getOrDefault("lat", rest.getLoc().getLat());
			lng = (Double) commandAttributes.getOrDefault("lng", rest.getLoc().getLng());
		} else {
			lat = rest.getLoc().getLat();
			lng = rest.getLoc().getLng();
		}
		ArrayList<ObjectId> objectIds = mapper.readValue(json, new TypeReference<ArrayList<ObjectId>>() {
		});

		if (objectIds.isEmpty()) {
			throw new RestRBadRequestException("objectIds cannot be empty");
		}

		ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
		double totalPrice = 0;

		for (ObjectId objectId : objectIds) {
			ObjectBoundary menuItemasObjectBoundary = this.objConverter.toBoundary(
					this.objectCrud.findById(objectId.toString()).orElseThrow(() -> new RestRNotFoundException(
							"Menu Item with id " + objectId.toString() + " doesn't exist")));

			String category = menuItemasObjectBoundary.getType();
			Map<String, Object> Details = menuItemasObjectBoundary.getObjectDetails();

			if (Details == null) {
				throw new RestRBadRequestException(
						"Menu item details cannot be null for item with id " + objectId.toString());
			}

			String title = (String) Details.get("title");
			String description = (String) Details.get("description");
			Double price = (Double) Details.get("price");
			String imgURL = (String) Details.get("imgURL");
			ArrayList<String> allergens = (ArrayList<String>) Details.get("allergens");

			if (title == null || price == null) {
				throw new RestRBadRequestException("Title and price are required for menu items");
			}

			menuItems.add(new MenuItem(category, title, description, price, imgURL, allergens));
			totalPrice += price.doubleValue();
		}

		ObjectBoundary myOrder = new ObjectBoundary();
		myOrder.setObjectId(new ObjectId());
		myOrder.getObjectId().setSuperapp(Command.getCommandId().getSuperapp());
		myOrder.getObjectId().setId(UUID.randomUUID().toString());
		myOrder.setCreatedBy(new CreatedBy(Command.getInvokedBy().getUserId()));
		myOrder.setActive(true);
		myOrder.setLocation(new Location(lat, lng));
		myOrder.setAlias(type);
		myOrder.setCreationTimestamp(new Date());

		Map<String, Object> orderDetails = new LinkedHashMap<String, Object>();
		myOrder.setType("Order");
		orderDetails.put("notes", commandAttributes.get("notes"));
		orderDetails.put("tableNumber", commandAttributes.getOrDefault("tableNumber", null));
		orderDetails.put("phoneNumber", commandAttributes.get("phoneNumber"));
		orderDetails.put("address", commandAttributes.get("address"));
		orderDetails.put("status", "OPEN");
		orderDetails.put("totalPrice", totalPrice);
		orderDetails.put("objectIds", objectIds);

		myOrder.setObjectDetails(orderDetails);
		ObjectEntity entity = this.objConverter.toEntity(myOrder);
		entity = this.objectCrud.save(entity);

		try {
			emailLogic.OrderMessage(myOrder.getCreatedBy().getUserId().getEmail(), menuItems, totalPrice);
		} catch (MessagingException e) {
			throw new RestRBadRequestException(e.getMessage());
		}

		ArrayList<Object> rq = new ArrayList<>();
		rq.add(this.objConverter.toBoundary(entity));

		return rq;
	}

}
