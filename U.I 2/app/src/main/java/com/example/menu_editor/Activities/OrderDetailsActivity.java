package com.example.menu_editor.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu_editor.Boundaries.ObjectBoundary;
import com.example.menu_editor.General.ObjectId;
import com.example.menu_editor.R;
import com.example.menu_editor.Service.ObjectService;
import com.example.menu_editor.Service.RetrofitClient;
import com.example.menu_editor.Utilities.CurrentUser;
import com.example.menu_editor.Utilities.MenuItem;
import com.example.menu_editor.Utilities.MenuItemAdapter;
import com.example.menu_editor.Utilities.Order;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderDetailsActivity extends AppCompatActivity {

    private TextView TXT_createdBy, TXT_type, TXT_notes, TXT_tableNumber, TXT_address, TXT_phoneNumber, TXT_status, TXT_totalPrice;
    private RecyclerView orderItemsRecyclerView;
    private Order order;
    private MenuItemAdapter menuItemAdapter;
    private List<MenuItem> menuItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        findViews();
        order = getIntent().getParcelableExtra("order");
        if (order != null) {
            initOrderDetails();
            fetchMenuItems();
        }
    }

    private void findViews() {
        TXT_createdBy = findViewById(R.id.TXT_createdBy);
        TXT_type = findViewById(R.id.TXT_type);
        TXT_notes = findViewById(R.id.TXT_notes);
        TXT_tableNumber = findViewById(R.id.TXT_tableNumber);
        TXT_address = findViewById(R.id.TXT_address);
        TXT_phoneNumber = findViewById(R.id.TXT_phoneNumber);
        TXT_status = findViewById(R.id.TXT_status);
        TXT_totalPrice = findViewById(R.id.TXT_totalPrice);
        orderItemsRecyclerView = findViewById(R.id.order_items_recycler_view);
    }

    private void initOrderDetails() {
        TXT_createdBy.setText(order.getCreatedBy().getUserId().getEmail());
        TXT_type.setText(order.getType());
        TXT_notes.setText(order.getNotes());
        TXT_tableNumber.setText(order.getTableNumber());
        TXT_address.setText(order.getAddress());
        TXT_phoneNumber.setText(order.getPhoneNumber());
        TXT_status.setText(order.getStatus());
        TXT_totalPrice.setText("₪ " + String.format("%.2f", order.getTotalPrice()));
    }

    private void fetchMenuItems() {
        Retrofit client = RetrofitClient.getInstance();
        ObjectService objectService = client.create(ObjectService.class);

        for (ObjectId menuItemId : order.getMenuItems()) {
            Call<ObjectBoundary> call = objectService.getSpecificObject(
                    "2024b.aviad.korakin",
                    menuItemId.getId(),
                    CurrentUser.getSuperapp(),
                    CurrentUser.getEmail());

            call.enqueue(new Callback<ObjectBoundary>() {
                @Override
                public void onResponse(Call<ObjectBoundary> call, Response<ObjectBoundary> response) {
                    if (response.isSuccessful()) {
                        ObjectBoundary objectBoundary = response.body();
                        if (objectBoundary != null) {
                            Map<String, Object> details = objectBoundary.getObjectDetails();
                            String category = objectBoundary.getAlias();
                            String title = (String) details.get("title");
                            String description = (String) details.get("description");
                            Double price = (Double) details.get("price");
                            String imgURL = (String) details.get("imgURL");
                            ArrayList<String> allergens = (ArrayList<String>) details.get("allergens");

                            MenuItem menuItem = new MenuItem(
                                    menuItemId,
                                    category,
                                    title,
                                    description,
                                    price,
                                    imgURL,
                                    allergens);
                            menuItems.add(menuItem);
                            updateUI();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ObjectBoundary> call, Throwable t) {
                    // Handle failure
                }
            });
        }
    }

    private void updateUI() {
        if (menuItemAdapter == null) {
            menuItemAdapter = new MenuItemAdapter(this, menuItems);
            orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            orderItemsRecyclerView.setAdapter(menuItemAdapter);
        } else {
            menuItemAdapter.notifyDataSetChanged();
        }
    }

    public void onReturnArrowClick(View view) {
        finish();
    }
}
