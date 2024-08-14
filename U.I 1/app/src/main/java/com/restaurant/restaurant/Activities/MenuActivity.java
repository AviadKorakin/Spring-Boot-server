package com.restaurant.restaurant.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.restaurant.restaurant.Enums.RoleEnum;
import com.restaurant.restaurant.General.ObjectId;
import com.restaurant.restaurant.Service.ObjectService;
import com.restaurant.restaurant.Adapters.MenuAdapter;
import com.restaurant.restaurant.R;
import com.restaurant.restaurant.Utilities.CurrentUser;
import com.restaurant.restaurant.Utilities.MenuItem;
import com.restaurant.restaurant.Boundaries.ObjectBoundary;
import com.restaurant.restaurant.Service.RetrofitClient;
import com.restaurant.restaurant.Utilities.OrderCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MenuActivity extends AppCompatActivity {
    private TextView TXT_username, cartItemCount;
    private AppCompatButton BTN_all;
    private AppCompatButton BTN_starters;
    private AppCompatButton BTN_main;
    private AppCompatButton BTN_deserts;
    private AppCompatButton BTN_drinks;
    private AppCompatButton BTN_goToCart;
    private AppCompatButton BTN_goToReservation;

    private RecyclerView recyclerViewMenu;

    private MenuAdapter menuAdapter;
    private OrderCallback orderCallback;

    ArrayList<MenuItem> orderItems = new ArrayList<>();
    ArrayList<MenuItem> menuItems = new ArrayList<>();
    ArrayList<MenuItem> itemsToShow = new ArrayList<>();
    private String category = "ALL";

    static boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (flag) {
            Intent intent = getIntent();
            orderItems = intent.getParcelableArrayListExtra("CART");
        }
        flag = true;

        createOrderCallback();
        findViews();
        setListeners();
        fetchMenuData();

        cartItemCount.setText(""+orderItems.size());
    }

    private void goToCart() {
        Intent i = new Intent(getApplicationContext(), CartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("CART", orderItems);
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }

    private void goToReservation() {
        Intent i = new Intent(getApplicationContext(), ReservationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("CART", orderItems);
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }

    private void createOrderCallback() {
        orderCallback = new OrderCallback() {
            @Override
            public void addToCart(ObjectId id) {
                for (MenuItem menuItem : menuItems)
                    if (menuItem.getID().equals(id)) {
                        orderItems.add(menuItem);
                        Toast.makeText(MenuActivity.this,
                                menuItem.getName()+" was added to cart",
                                Toast.LENGTH_SHORT).show();
                    }
                cartItemCount.setText(""+orderItems.size());
            }

            @Override
            public void removeFromCart(ObjectId id) {}
        };
    }

    private void updateUI() {
        itemsToShow.clear();
        for (MenuItem menuItem : menuItems)
            if (menuItem.getCategory().equals(category) || category.equals("ALL"))
                itemsToShow.add(menuItem);

        menuAdapter = new MenuAdapter(MenuActivity.this, itemsToShow, orderCallback);
        recyclerViewMenu.swapAdapter(menuAdapter, true);
    }

    private void setListeners() {
        BTN_all.setOnClickListener(v -> { changeCategory("ALL"); });
        BTN_starters.setOnClickListener(v -> { changeCategory("Starters"); });
        BTN_main.setOnClickListener(v -> { changeCategory("Main"); });
        BTN_deserts.setOnClickListener(v -> { changeCategory("Dessert"); });
        BTN_drinks.setOnClickListener(v -> { changeCategory("Drinks"); });

        BTN_goToCart.setOnClickListener(v -> {
            goToCart();
        });
        BTN_goToReservation.setOnClickListener(v -> {
            goToReservation();
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
        cartItemCount = findViewById(R.id.cartItemCount);
        cartItemCount.setText("0");
        recyclerViewMenu = findViewById(R.id.recyclerViewMenu);
        BTN_all = findViewById(R.id.BTN_all);
        BTN_starters = findViewById(R.id.BTN_starters);
        BTN_main = findViewById(R.id.BTN_main);
        BTN_deserts = findViewById(R.id.BTN_deserts);
        BTN_drinks = findViewById(R.id.BTN_drinks);

        recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this));

        BTN_goToCart = findViewById(R.id.BTN_goToCart);
        BTN_goToReservation = findViewById(R.id.BTN_goToReservation);

        if (CurrentUser.getRole().equals(RoleEnum.MINIAPP_USER)) {
            BTN_goToReservation.setVisibility(View.VISIBLE);
        }
        else {
            BTN_goToReservation.setVisibility(View.GONE);
        }

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
}