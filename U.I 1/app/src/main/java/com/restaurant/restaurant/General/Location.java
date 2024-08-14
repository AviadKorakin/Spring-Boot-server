package com.restaurant.restaurant.General;

public class Location {
    private Double lat;
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Location(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Location() {
        this(0.0, 0.0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Location location = (Location) o;
        return Double.compare(lat, location.lat) == 0 && Double.compare(lng, location.lng) == 0;
    }

    @Override
    public String toString() {
        return "Location [lat=" + lat + ", lng=" + lng + "]";
    }

}
