package restR.logic.converters;

import org.springframework.stereotype.Component;

import restR.boundaries.ObjectBoundary;
import restR.entities.ObjectEntity;
import restR.general.Location;
import restR.general.ObjectId;
import restR.general.UserId;
import restR.general.CreatedBy;

@Component
public class ObjectConverter {
	private LocationToPointConverter ltpConverter;

	public ObjectConverter(LocationToPointConverter ltpConverter) {
		super();
		this.ltpConverter = ltpConverter;
	}

	public ObjectBoundary toBoundary(ObjectEntity entity) {
		ObjectBoundary rv = new ObjectBoundary();
		ObjectId temp = new ObjectId(entity.getObjectId());
		rv.setObjectId(temp);
		rv.setType(entity.getType());
		rv.setAlias(entity.getAlias());
		rv.setLocation(new Location(entity.getGeom().getX(), entity.getGeom().getY()));
		rv.setActive(entity.getActive());
		rv.setCreationTimestamp(entity.getCreationTimestamp());
		rv.setCreatedBy(new CreatedBy(new UserId(entity.getCreatedBy())));
		rv.setObjectDetails(entity.getObjectDetails());

		return rv;
	}

	public ObjectEntity toEntity(ObjectBoundary boundary) {

		ObjectEntity rv = new ObjectEntity();
		rv.setAlias(boundary.getAlias());
		rv.setCreatedBy(boundary.getCreatedBy().getUserId().toString());
		rv.setObjectDetails(boundary.getObjectDetails());
		if (boundary.getLocation() != null &&  boundary.getLocation().getLat() != null && boundary.getLocation().getLng() != null) {
			rv.setGeom(ltpConverter.fromLocationToPoint(boundary.getLocation()));
		} else {
			rv.setGeom(ltpConverter.fromLocationToPoint(new Location(0.0, 0.0)));
		}
		rv.setObjectId(boundary.getObjectId().toString());

		rv.setType(boundary.getType());
		rv.setActive(boundary.getActive());
		rv.setCreationTimestamp(boundary.getCreationTimestamp());

		return rv;

	}

}