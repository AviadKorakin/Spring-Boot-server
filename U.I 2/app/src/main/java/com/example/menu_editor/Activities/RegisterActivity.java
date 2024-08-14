package com.example.menu_editor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.example.menu_editor.Boundaries.NewUserBoundary;
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

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText TXT_email;
    private TextInputEditText TXT_username;
    private AppCompatRadioButton adminRadio;
    private AppCompatRadioButton superRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViews();
    }

    private void findViews() {
        TXT_email = findViewById(R.id.TXT_email);
        TXT_username = findViewById(R.id.TXT_username);
        adminRadio = findViewById(R.id.adminRadio);
        superRadio = findViewById(R.id.superRadio);
        MaterialButton BTN_login = findViewById(R.id.BTN_login);
        MaterialButton BTN_register = findViewById(R.id.BTN_register);

        BTN_login.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        });
        BTN_register.setOnClickListener(v -> {
            CurrentUser.init();
            Retrofit client = RetrofitClient.getInstance();
            UserService userService = client.create(UserService.class);

            RoleEnum role;
            if (adminRadio.isChecked())
                role = RoleEnum.ADMIN;
            else if (superRadio.isChecked())
                role = RoleEnum.SUPERAPP_USER;
            else
                role = RoleEnum.MINIAPP_USER;
            Call<UserBoundary> call = userService.storeInDatabase(
                    new NewUserBoundary(
                            String.valueOf(TXT_email.getText()),
                            role,
                            String.valueOf(TXT_username.getText()),
                            "ang"));
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
                            Toast.makeText(RegisterActivity.this,
                                    "User Created But Permission Denied!",
                                    Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(RegisterActivity.this,
                                "User Already Exist",
                                Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<UserBoundary> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this,
                            "yes li kaki",
                            Toast.LENGTH_LONG).show();
                }
            });

        });
    }
}