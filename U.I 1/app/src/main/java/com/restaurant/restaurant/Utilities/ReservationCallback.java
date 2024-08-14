package com.restaurant.restaurant.Utilities;

import com.restaurant.restaurant.General.ObjectId;

public interface ReservationCallback {
    void reserve(ObjectId tableId);
}