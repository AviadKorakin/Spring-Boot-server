package com.restaurant.restaurant.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.restaurant.restaurant.Boundaries.NewUserBoundary;
import com.restaurant.restaurant.Boundaries.ObjectBoundary;
import com.restaurant.restaurant.Boundaries.UserBoundary;
import com.restaurant.restaurant.Enums.RoleEnum;
import com.restaurant.restaurant.General.ObjectId;
import com.restaurant.restaurant.R;
import com.restaurant.restaurant.Service.ObjectService;
import com.restaurant.restaurant.Service.RetrofitClient;
import com.restaurant.restaurant.Service.UserService;
import com.restaurant.restaurant.Utilities.CurrentUser;
import com.restaurant.restaurant.Utilities.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText TXT_superapp;
    private TextInputEditText TXT_email;

    private AppCompatButton BTN_login;
    private AppCompatButton BTN_goRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
    }

    private void findViews() {
        TXT_superapp = findViewById(R.id.TXT_superapp);
        TXT_email = findViewById(R.id.TXT_email);
        BTN_login = findViewById(R.id.BTN_login);

        BTN_goRegister = findViewById(R.id.BTN_goRegister);

        BTN_goRegister.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(i);
            finish();
        });

        BTN_login.setOnClickListener(v -> {
            CurrentUser.init();
            Retrofit client = RetrofitClient.getInstance();
            UserService userService = client.create(UserService.class);

            Call<UserBoundary> call = userService.getSpecificUser(
                    String.valueOf(TXT_superapp.getText()),
                    String.valueOf(TXT_email.getText()));
            call.enqueue(new Callback<UserBoundary>() {
                @Override
                public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
                    if (response.code() == 200) {
                        CurrentUser.setUserBoundary(response.body());

                         Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else {
                        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                        startActivity(i);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<UserBoundary> call, Throwable t) {
                    Toast.makeText(LoginActivity.this,t.toString(),Toast.LENGTH_LONG).show();

                }
            });

        });
    }
}