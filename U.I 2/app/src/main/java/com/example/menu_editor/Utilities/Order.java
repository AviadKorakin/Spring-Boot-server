package com.example.menu_editor.Utilities;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.menu_editor.General.CreatedBy;
import com.example.menu_editor.General.ObjectId;

import java.util.ArrayList;

public class Order implements Parcelable {
    private ObjectId id;
    private String type;
    private CreatedBy createdBy;
    private String notes;
    private String tableNumber;
    private String address;
    private String phoneNumber;
    private String status;
    private Double totalPrice;
    private ArrayList<ObjectId> menuItemsId;

    public Order(ObjectId id,
                 String type,
                 CreatedBy createdBy,
                 String notes,
                 String tableNumber,
                 String address,
                 String phoneNumber,
                 String status,
                 Double totalPrice,
                 ArrayList<ObjectId> menuItemsId) {
        this.id = id;
        this.type = type;
        this.createdBy = createdBy;
        this.notes = notes;
        this.tableNumber = tableNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.totalPrice = totalPrice;
        this.menuItemsId = menuItemsId;
    }

    protected Order(Parcel in) {
        id = in.readParcelable(ObjectId.class.getClassLoader());
        type = in.readString();
        createdBy = in.readParcelable(CreatedBy.class.getClassLoader());
        notes = in.readString();
        tableNumber = in.readString();
        address = in.readString();
        phoneNumber = in.readString();
        status = in.readString();
        if (in.readByte() == 0) {
            totalPrice = null;
        } else {
            totalPrice = in.readDouble();
        }
        menuItemsId = in.createTypedArrayList(ObjectId.CREATOR);
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(id, flags);
        dest.writeString(type);
        dest.writeParcelable(createdBy, flags);
        dest.writeString(notes);
        dest.writeString(tableNumber);
        dest.writeString(address);
        dest.writeString(phoneNumber);
        dest.writeString(status);
        if (totalPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(totalPrice);
        }
        dest.writeTypedList(menuItemsId);
    }

    // Getters and setters

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<ObjectId> getMenuItems() {
        return menuItemsId;
    }

    public void setMenuItems(ArrayList<ObjectId> menuItemsId) {
        this.menuItemsId = menuItemsId;
    }
}
