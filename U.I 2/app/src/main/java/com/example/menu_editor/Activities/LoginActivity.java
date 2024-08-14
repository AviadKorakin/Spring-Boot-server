package com.example.menu_editor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.menu_editor.Boundaries.UserBoundary;
import com.example.menu_editor.Enums.RoleEnum;
import com.example.menu_editor.R;
import com.example.menu_editor.Service.RetrofitClient;
import com.example.menu_editor.Service.UserService;
import com.example.menu_editor.Utilities.CurrentUser;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText TXT_superapp;
    private TextInputEditText TXT_email;

    private MaterialButton BTN_login;
    private MaterialButton BTN_goRegister;

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
                        if (!response.body().getRole().equals(RoleEnum.MINIAPP_USER)) {
                            CurrentUser.setUserBoundary(response.body());

                            Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else
                            Toast.makeText(LoginActivity.this,
                                    "Permission Denied!",
                                    Toast.LENGTH_LONG).show();
                    }
                    else {
                        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                        startActivity(i);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<UserBoundary> call, Throwable t) {
                    Log.d("dddd", t.toString());
                }
            });
        });
    }
}