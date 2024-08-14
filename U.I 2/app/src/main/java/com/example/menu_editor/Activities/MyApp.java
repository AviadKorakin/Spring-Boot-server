package com.example.menu_editor.Activities;

import android.app.Application;

import com.example.menu_editor.Service.RetrofitClient;


public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitClient.getInstance();
    }
}
