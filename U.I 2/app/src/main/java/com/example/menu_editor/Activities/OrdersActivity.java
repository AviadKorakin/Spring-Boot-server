package com.example.menu_editor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu_editor.Boundaries.ObjectBoundary;
import com.example.menu_editor.General.CreatedBy;
import com.example.menu_editor.General.ObjectId;
import com.example.menu_editor.OrderAdapter;
import com.example.menu_editor.R;
import com.example.menu_editor.Service.ObjectService;
import com.example.menu_editor.Service.RetrofitClient;
import com.example.menu_editor.Utilities.CurrentUser;
import com.example.menu_editor.Utilities.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrdersActivity extends AppCompatActivity {
    ArrayList<Order> orders = new ArrayList<>();

    private AppCompatButton BTN_back;
    private RecyclerView recyclerViewOrder;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        findViews();
        fetchOrderData();
    }

    private void findViews() {
        recyclerViewOrder = findViewById(R.id.recyclerViewOrder);
        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(this));

        BTN_back = findViewById(R.id.BTN_back);
        BTN_back.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void updateUI() {
        orderAdapter = new OrderAdapter(OrdersActivity.this, orders);
        recyclerViewOrder.swapAdapter(orderAdapter, true);
    }

    private void fetchOrderData() {
        Retrofit client = RetrofitClient.getInstance();
        ObjectService objectService = client.create(ObjectService.class);
        Call<List<ObjectBoundary>> call = objectService.getObjectByType(
                "Order", CurrentUser.getSuperapp(), CurrentUser.getEmail(), 5, 0);
        ObjectMapper a=new ObjectMapper();
        call.enqueue(new Callback<List<ObjectBoundary>>() {
            @Override
            public void onResponse(Call<List<ObjectBoundary>> call,
                                   Response<List<ObjectBoundary>> response) {
                if (response.isSuccessful()) {
                    List<ObjectBoundary> ObjectBoundaries = response.body();
                    Map<String, Object> details;
                    orders.clear();
                    if (ObjectBoundaries == null)
                        return;
                    for (ObjectBoundary order : ObjectBoundaries) {
                        ObjectId id = order.getObjectId();
                        String type = order.getAlias();
                        CreatedBy createdBy = order.getCreatedBy();

                        details = order.getObjectDetails();
                        ArrayList<ObjectId> objectIds ;
                        String json;
                        try {
                            json=  a.writeValueAsString(details.get("objectIds"));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            objectIds = a.readValue(json, new TypeReference<ArrayList<ObjectId>>() {
                            });
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        //TODO move to another activity with Order details and show them
                        Double totalPrice = (Double) details.get("totalPrice");
                        String notes = (String) details.get("notes");
                        String tableNumber = (String) details.get("tableNumber");
                        String address = (String) details.get("address");
                        String phoneNumber = (String) details.get("phoneNumber");
                        String status = (String) details.get("status");

                        orders.add(new Order(
                                id,
                                type,
                                createdBy,
                                notes,
                                tableNumber,
                                address,
                                phoneNumber,
                                status,
                                totalPrice,
                                objectIds));
                    }
                    updateUI();
                }
                else {
                    Toast.makeText(OrdersActivity.this,
                            response.errorBody().toString(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ObjectBoundary>> call, Throwable t) {
                Toast.makeText(OrdersActivity.this,t.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
}