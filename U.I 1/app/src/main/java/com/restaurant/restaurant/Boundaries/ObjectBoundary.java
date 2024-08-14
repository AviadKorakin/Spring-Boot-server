package com.restaurant.restaurant.Boundaries;

import com.restaurant.restaurant.General.CreatedBy;
import com.restaurant.restaurant.General.Location;
import com.restaurant.restaurant.General.ObjectId;

import java.util.Map;
import java.util.Date;

public class ObjectBoundary {
    private ObjectId objectId;
    private String type;
    private String alias;
    private Location location;
    private Boolean active;
    private Date creationTimestamp;
    private CreatedBy createdBy;
    private Map<String, Object> objectDetails;

    public ObjectBoundary() {

    }


    public ObjectBoundary(ObjectId objectId, String type, String alias, Location location, Boolean active, CreatedBy createdBy, Map<String, Object> objectDetails) {
        this.objectId = objectId;
        this.type = type;
        this.alias = alias;
        this.location = location;
        this.active = active;
        this.createdBy = createdBy;
        this.objectDetails = objectDetails;
    }


    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public Map<String, Object> getObjectDetails() {
        return objectDetails;
    }

    public void setObjectDetails(Map<String, Object> objectDetails) {
        this.objectDetails = objectDetails;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ObjectBoundary that = (ObjectBoundary) o;
        return objectId.equals(that.objectId) && type.equals(that.type) && alias.equals(that.alias)
                && location.equals(that.location) && active.equals(that.active)
                && creationTimestamp.equals(that.creationTimestamp) && createdBy.equals(that.createdBy)
                && objectDetails.entrySet().equals(that.objectDetails.entrySet());
    }

    @Override
    public String toString() {
        return "ObjectBoundary [objectId=" + objectId + ", type=" + type + ", alias=" + alias + ", location=" + location
                + ", active=" + active + ", creationTimestamp=" + creationTimestamp + ", createdBy=" + createdBy
                + ", objectDetails=" + objectDetails + "]";
    }

}
