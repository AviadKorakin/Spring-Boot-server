package com.example.menu_editor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu_editor.Boundaries.ObjectBoundary;
import com.example.menu_editor.General.CreatedBy;
import com.example.menu_editor.General.ObjectId;
import com.example.menu_editor.MenuAdapter;
import com.example.menu_editor.R;
import com.example.menu_editor.Service.ObjectService;
import com.example.menu_editor.Service.RetrofitClient;
import com.example.menu_editor.Utilities.CurrentUser;
import com.example.menu_editor.Utilities.MenuItem;
import com.example.menu_editor.Utilities.ChangeCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MenuActivity extends AppCompatActivity {
    private TextView TXT_username;
    private AppCompatButton BTN_all;
    private AppCompatButton BTN_starters;
    private AppCompatButton BTN_main;
    private AppCompatButton BTN_deserts;
    private AppCompatButton BTN_drinks;

    private RecyclerView recyclerViewMenu;

    private MenuAdapter menuAdapter;
    private ChangeCallback changeCallback;

    private AppCompatButton BTN_addNewItem;
    private AppCompatButton BTN_goToOrders;
    private AppCompatButton BTN_goToTables;

    ArrayList<MenuItem> menuItems = new ArrayList<>();
    ArrayList<MenuItem> itemsToShow = new ArrayList<>();
    private String category = "ALL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        createOrderCallback();

        findViews();
        setListeners();
        fetchMenuData();
    }

    private void createOrderCallback() {
        changeCallback = new ChangeCallback() {
            @Override
            public void change(MenuItem menuItem) {
                changeMenuData(menuItem);
                fetchMenuData();
            }

            @Override
            public void delete(MenuItem menuItem) {
                deleteItem(menuItem);
                fetchMenuData();
            }
        };
    }

    private void updateUI() {
        if (menuItems.isEmpty())
            return;
        itemsToShow.clear();
        for (MenuItem menuItem : menuItems)
            if (menuItem.getCategory().equals(category) || category.equals("ALL"))
                itemsToShow.add(menuItem);

        menuAdapter = new MenuAdapter(MenuActivity.this, itemsToShow, changeCallback);
        recyclerViewMenu.swapAdapter(menuAdapter, true);
    }

    private void setListeners() {
        BTN_all.setOnClickListener(v -> { changeCategory("ALL"); });
        BTN_starters.setOnClickListener(v -> { changeCategory("Starters"); });
        BTN_main.setOnClickListener(v -> { changeCategory("Main"); });
        BTN_deserts.setOnClickListener(v -> { changeCategory("Desserts"); });
        BTN_drinks.setOnClickListener(v -> { changeCategory("Drinks"); });

        BTN_addNewItem.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), NewItemActivity.class);
            startActivity(i);
            finish();
        });
        BTN_goToOrders.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), OrdersActivity.class);
            startActivity(i);
            finish();
        });
        BTN_goToTables.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), TablesActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void changeCategory(String newCategory) {
        if (category.equals(newCategory))
            return;
        category = newCategory;
        updateUI();
    }

    private void findViews() {
        TXT_username = findViewById(R.id.TXT_username);
        TXT_username.setText("Welcome back "+CurrentUser.getUsername());
        recyclerViewMenu = findViewById(R.id.recyclerViewMenu);
        BTN_all = findViewById(R.id.BTN_all);
        BTN_starters = findViewById(R.id.BTN_starters);
        BTN_main = findViewById(R.id.BTN_main);
        BTN_deserts = findViewById(R.id.BTN_deserts);
        BTN_drinks = findViewById(R.id.BTN_drinks);

        recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this));

        BTN_addNewItem = findViewById(R.id.BTN_addNewItem);
        BTN_goToOrders = findViewById(R.id.BTN_goToOrders);
        BTN_goToTables = findViewById(R.id.BTN_goToTables);
    }

    private void fetchMenuData() {
        Retrofit client = RetrofitClient.getInstance();
        ObjectService objectService = client.create(ObjectService.class);
        Call<List<ObjectBoundary>> call = objectService.getObjectByType(
                "MenuItem", CurrentUser.getSuperapp(), CurrentUser.getEmail(), 5, 0);

        call.enqueue(new Callback<List<ObjectBoundary>>() {
            @Override
            public void onResponse(Call<List<ObjectBoundary>> call,
                                   Response<List<ObjectBoundary>> response) {
                if (response.isSuccessful()) {
                    List<ObjectBoundary> ObjectBoundaries = response.body();
                    Map<String, Object> Details;
                    menuItems.clear();
                    if (ObjectBoundaries == null)
                        return;

                    for (ObjectBoundary item : ObjectBoundaries) {
                        if (item.getActive()) {
                            ObjectId id = item.getObjectId();
                            String category = item.getAlias();
                            Details = item.getObjectDetails();
                            String title = (String) Details.get("title");
                            String description = (String) Details.get("description");
                            Double price = (Double) Details.get("price");
                            String imgURL = (String) Details.get("imgURL");
                            ArrayList<String> allergens = (ArrayList<String>) Details.get("allergens");

                            menuItems.add(new MenuItem(
                                    id,
                                    category,
                                    title,
                                    description,
                                    price,
                                    imgURL,
                                    allergens));
                        }
                    }
                    Collections.sort(menuItems);
                    updateUI();
                }
                else {
                     Toast.makeText(MenuActivity.this,
                            response.errorBody().toString(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ObjectBoundary>> call, Throwable t) {
                Toast.makeText(MenuActivity.this,t.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void changeMenuData(MenuItem menuItem) {
        Retrofit client = RetrofitClient.getInstance();
        ObjectService objectService = client.create(ObjectService.class);

        Map<String, Object> details = new LinkedHashMap<>();
        details.put("title", menuItem.getName());
        details.put("description", menuItem.getDescription());
        details.put("price", menuItem.getPrice());
        details.put("imgURL", menuItem.getImgURL());
        details.put("allergens", menuItem.getAllergens());

        Call<Void> call = objectService.updateObject(
                CurrentUser.getSuperapp(),
                menuItem.getID().getId(),
                CurrentUser.getSuperapp(),
                CurrentUser.getEmail(),
                new ObjectBoundary(
                        menuItem.getID(),
                        "MenuItem",
                        menuItem.getCategory(),
                        null,
                        true,
                        new CreatedBy(CurrentUser.getUserId()),
                        details
                ));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(MenuActivity.this,
                        "Changed Successfully",
                        Toast.LENGTH_LONG).show();
                fetchMenuData();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void deleteItem(MenuItem menuItem) {
        Retrofit client = RetrofitClient.getInstance();
        ObjectService objectService = client.create(ObjectService.class);

        Map<String, Object> details = new LinkedHashMap<>();
        details.put("title", menuItem.getName());
        details.put("description", menuItem.getDescription());
        details.put("price", menuItem.getPrice());
        details.put("imgURL", menuItem.getImgURL());
        details.put("allergens", menuItem.getAllergens());

        Call<Void> call = objectService.updateObject(
                CurrentUser.getSuperapp(),
                menuItem.getID().getId(),
                CurrentUser.getSuperapp(),
                CurrentUser.getEmail(),
                new ObjectBoundary(
                        menuItem.getID(),
                        "MenuItem",
                        menuItem.getCategory(),
                        null,
                        false,
                        new CreatedBy(CurrentUser.getUserId()),
                        details
                ));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(MenuActivity.this,
                        "Deleted Successfully",
                        Toast.LENGTH_LONG).show();
                updateUI();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}