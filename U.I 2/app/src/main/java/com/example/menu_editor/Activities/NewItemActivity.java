package com.example.menu_editor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.example.menu_editor.Boundaries.ObjectBoundary;
import com.example.menu_editor.General.CreatedBy;
import com.example.menu_editor.General.Location;
import com.example.menu_editor.General.ObjectId;
import com.example.menu_editor.R;
import com.example.menu_editor.Service.ObjectService;
import com.example.menu_editor.Service.RetrofitClient;
import com.example.menu_editor.Utilities.CurrentUser;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewItemActivity extends AppCompatActivity {
    private AppCompatEditText TXT_title;
    private AppCompatEditText TXT_description;
    private AppCompatEditText TXT_allergens;
    private AppCompatEditText TXT_price;
    private AppCompatEditText TXT_category;
    private AppCompatEditText TXT_imgUrl;

    private Button BTN_load_img;
    private Button BTN_back;
    private Button BTN_save;

    private AppCompatImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        findViews();
    }

    private void findViews() {
        TXT_title = findViewById(R.id.TXT_title);
        TXT_description = findViewById(R.id.TXT_description);
        TXT_allergens = findViewById(R.id.TXT_allergens);
        TXT_price = findViewById(R.id.TXT_price);
        TXT_category = findViewById(R.id.TXT_category);
        TXT_imgUrl = findViewById(R.id.TXT_imgUrl);
        BTN_back = findViewById(R.id.BTN_back);
        BTN_save = findViewById(R.id.BTN_save);
        BTN_load_img = findViewById(R.id.BTN_load_img);
        imageView = findViewById(R.id.imageView);

        BTN_load_img.setOnClickListener(v -> {
            Glide.with(this)
                    .load(TXT_imgUrl.getText().toString())
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .into(imageView);
        });
        BTN_back.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(i);
            finish();
        });
        BTN_save.setOnClickListener(v -> {
            addItem();
        });
    }

    private void addItem() {
        ObjectBoundary objectBoundary = new ObjectBoundary();
        objectBoundary.setObjectId(new ObjectId(
                CurrentUser.getSuperapp(),
                "null"));
        objectBoundary.setType("MenuItem");
        objectBoundary.setAlias(TXT_category.getText().toString());
        objectBoundary.setLocation(new Location());
        objectBoundary.setActive(true);
        objectBoundary.setCreationTimestamp(null);
        objectBoundary.setCreatedBy(new CreatedBy(CurrentUser.getUserId()));

        Map<String, Object> details = new LinkedHashMap<>();
        details.put("title", TXT_title.getText().toString());
        details.put("description", TXT_description.getText().toString());
        details.put("price", Double.valueOf(TXT_price.getText().toString()));
        details.put("imgURL", TXT_imgUrl.getText().toString());
        ArrayList<String> allergens = new ArrayList<>();
        Collections.addAll(allergens, TXT_allergens.getText().toString().split(","));
        details.put("allergens", allergens);

        objectBoundary.setObjectDetails(details);

        Retrofit client = RetrofitClient.getInstance();
        ObjectService objectService = client.create(ObjectService.class);
        Call<ObjectBoundary> call = objectService.storeInDatabase(
                CurrentUser.getSuperapp(), CurrentUser.getEmail(), objectBoundary);

        call.enqueue(new Callback<ObjectBoundary>() {
            @Override
            public void onResponse(Call<ObjectBoundary> call, Response<ObjectBoundary> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(NewItemActivity.this,
                            "Created Successfully",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(NewItemActivity.this,
                            response.errorBody().toString(),
                            Toast.LENGTH_LONG).show();
                    Log.d("dddd", response.toString());
                }
            }

            @Override
            public void onFailure(Call<ObjectBoundary> call, Throwable t) {
                Toast.makeText(NewItemActivity.this,t.toString(),Toast.LENGTH_LONG).show();
                Log.d("dddd", t.toString());
            }
        });
    }
}
