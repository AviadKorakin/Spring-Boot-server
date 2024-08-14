package com.example.menu_editor.Utilities;

import com.example.menu_editor.General.InvokedBy;
import com.example.menu_editor.General.ObjectId;

public class Table implements Comparable<Table>{
    private ObjectId id;
    private String capacity;
    private int tableNumber;
    private String locationDesc;

    public Table(ObjectId id, String capacity, int tableNumber, String locationDesc) {
        this.id = id;
        this.capacity = capacity;
        this.tableNumber = tableNumber;
        this.locationDesc = locationDesc;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getLocationDesc() {
        return locationDesc;
    }

    public void setLocationDesc(String locationDesc) {
        this.locationDesc = locationDesc;
    }

    @Override
    public int compareTo(Table o) {
        return tableNumber - o.getTableNumber();
    }
}



