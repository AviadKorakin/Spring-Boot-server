package com.restaurant.restaurant.Utilities;

import com.restaurant.restaurant.General.ObjectId;

public interface OrderCallback {
    void addToCart(ObjectId id);
    void removeFromCart(ObjectId id);
}