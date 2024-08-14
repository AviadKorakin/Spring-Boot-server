package com.example.menu_editor.Utilities;

import com.example.menu_editor.General.InvokedBy;
import com.example.menu_editor.General.ObjectId;

public class Reservation {
    private ObjectId id;
    private String date;
    private InvokedBy invokedBy;
    private String phoneNumber;
    private String notes;

    public Reservation(ObjectId id, String date, InvokedBy invokedBy, String phoneNumber, String notes) {
        this.id = id;
        this.date = date;
        this.invokedBy = invokedBy;
        this.phoneNumber = phoneNumber;
        this.notes = notes;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public InvokedBy getInvokedBy() {
        return invokedBy;
    }

    public void setInvokedBy(InvokedBy invokedBy) {
        this.invokedBy = invokedBy;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}



