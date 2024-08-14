package com.restaurant.restaurant.Activities;

import android.app.Application;

import com.restaurant.restaurant.Service.RetrofitClient;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitClient.getInstance();
    }
}
