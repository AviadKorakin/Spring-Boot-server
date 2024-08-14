package com.example.menu_editor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.menu_editor.Boundaries.ObjectBoundary;
import com.example.menu_editor.General.CreatedBy;
import com.example.menu_editor.General.Location;
import com.example.menu_editor.General.ObjectId;
import com.example.menu_editor.R;
import com.example.menu_editor.Service.ObjectService;
import com.example.menu_editor.Service.RetrofitClient;
import com.example.menu_editor.Utilities.CurrentUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewTableActivity extends AppCompatActivity {
    private AppCompatEditText TXT_capacity;
    private AppCompatEditText TXT_tableNumber;
    private AppCompatEditText TXT_locationDesc;
    private Button BTN_back;
    private Button BTN_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_table);

        findViews();
    }

    private void findViews() {
        TXT_capacity = findViewById(R.id.TXT_capacity);
        TXT_tableNumber = findViewById(R.id.TXT_tableNumber);
        TXT_locationDesc = findViewById(R.id.TXT_locationDesc);
        BTN_back = findViewById(R.id.BTN_back);
        BTN_save = findViewById(R.id.BTN_save);

        BTN_back.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(i);
            finish();
        });
        BTN_save.setOnClickListener(v -> {
            addTable();
        });
    }

    private void addTable() {
        ObjectBoundary objectBoundary = new ObjectBoundary();
        objectBoundary.setObjectId(new ObjectId(
                CurrentUser.getSuperapp(),
                "null"));
        objectBoundary.setType("Table");
        objectBoundary.setAlias(""+TXT_capacity.getText().toString());
        objectBoundary.setLocation(new Location());
        objectBoundary.setActive(true);
        objectBoundary.setCreationTimestamp(null);
        objectBoundary.setCreatedBy(new CreatedBy(CurrentUser.getUserId()));

        Map<String, Object> details = new LinkedHashMap<>();
        details.put("tableNumber", Integer.valueOf(TXT_tableNumber.getText().toString()));
        details.put("locationDesc", TXT_locationDesc.getText().toString());
        details.put("Reservations", null);

        objectBoundary.setObjectDetails(details);

        Retrofit client = RetrofitClient.getInstance();
        ObjectService objectService = client.create(ObjectService.class);
        Call<ObjectBoundary> call = objectService.storeInDatabase(
                CurrentUser.getSuperapp(), CurrentUser.getEmail(), objectBoundary);

        call.enqueue(new Callback<ObjectBoundary>() {
            @Override
            public void onResponse(Call<ObjectBoundary> call, Response<ObjectBoundary> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(NewTableActivity.this,
                            "Created Successfully",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(NewTableActivity.this,
                            response.errorBody().toString(),
                            Toast.LENGTH_LONG).show();
                    Log.d("dddd", response.toString());
                }
            }

            @Override
            public void onFailure(Call<ObjectBoundary> call, Throwable t) {
                Toast.makeText(NewTableActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                Log.d("dddd", t.toString());
            }
        });
    }
}