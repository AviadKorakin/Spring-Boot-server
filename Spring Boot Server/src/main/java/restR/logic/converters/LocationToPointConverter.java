package restR.logic.converters;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Component;

import restR.general.Location;
import restR.logic.exceptions.RestRBadRequestException;

@Component
public class LocationToPointConverter {

	private String PointWKT = "POINT";

	public Location fromPointToLocation(Point p) {
		return new Location(p.getX(), p.getY());
	}

	public Point fromLocationToPoint(Location loc) {
		try {
			return (Point) (new WKTReader().read(PointWKT + " (" + loc.getLat() + " " + loc.getLng() + ")"));
		} catch (ParseException e) {
			throw new RestRBadRequestException("Error in lat,lng paramaters");
		}
	}

}
