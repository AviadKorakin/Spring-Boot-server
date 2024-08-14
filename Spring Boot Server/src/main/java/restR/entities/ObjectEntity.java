package restR.entities;

import java.util.Date;
import java.util.Map;

import org.locationtech.jts.geom.Point;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import restR.logic.converters.MapToJsonConverter;


@Entity
@Table(name = "OBJECTS_TBL")
public class ObjectEntity {
	@Id
	private String objectId;
	
	private String type;

	private String alias;

	private Boolean active;

	private String createdBy;
	
	private Point geom;

	@Convert(converter = MapToJsonConverter.class)
	@Lob
	private Map<String, Object> objectDetails;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTimestamp;

	public ObjectEntity() {
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}


	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Map<String, Object> getObjectDetails() {
		return objectDetails;
	}

	public void setObjectDetails(Map<String, Object> objectDetails) {
		this.objectDetails = objectDetails;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Point getGeom() {
		return geom;
	}

	public void setGeom(Point geom) {
		this.geom = geom;
	}


	


}