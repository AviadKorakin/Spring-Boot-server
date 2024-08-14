package com.example.menu_editor.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.0.2.2:8084/";
    //emulator : 10.0.2.2 else go to windows ipconfig and copy ipv4
    // when you copy that assert that u also
    // adjust the network_security_config.xml

    private static Retrofit retrofitInstance;

    private RetrofitClient() {
    }

    public static synchronized Retrofit getInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitInstance;
    }
}