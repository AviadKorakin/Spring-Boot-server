package com.restaurant.restaurant.Utilities;

import com.restaurant.restaurant.General.ObjectId;

import java.util.ArrayList;

public class Table  {
    private ObjectId id;
    private String capacity;
    private int tableNumber;
    private String locationDesc;
    private ArrayList<Reservation> Reservations;

    public Table(ObjectId id, String capacity, int tableNumber, String locationDesc, ArrayList<Reservation> reservations) {
        this.id = id;
        this.capacity = capacity;
        this.tableNumber = tableNumber;
        this.locationDesc = locationDesc;
        Reservations = reservations;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public void setLocationDesc(String locationDesc) {
        this.locationDesc = locationDesc;
    }

    public void setReservations(ArrayList<Reservation> reservations) {
        Reservations = reservations;
    }

    public ObjectId getId() {
        return id;
    }

    public String getCapacity() {
        return capacity;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public String getLocationDesc() {
        return locationDesc;
    }

    public ArrayList<Reservation> getReservations() {
        return Reservations;
    }
}



