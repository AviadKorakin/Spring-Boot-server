package com.restaurant.restaurant.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.restaurant.restaurant.Adapters.CartAdapter;
import com.restaurant.restaurant.Adapters.MenuAdapter;
import com.restaurant.restaurant.General.ObjectId;
import com.restaurant.restaurant.R;
import com.restaurant.restaurant.Utilities.MenuItem;
import com.restaurant.restaurant.Utilities.OrderCallback;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerViewCart;
    private AppCompatButton BTN_goToMenu;
    private AppCompatButton BTN_goToPay;

    private CartAdapter cartAdapter;
    private OrderCallback orderCallback;

    ArrayList<MenuItem> orderItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intent intent = getIntent();
        orderItems = intent.getParcelableArrayListExtra("CART");

        createOrderCallback();
        findViews();
    }

    private void updateUI() {
        cartAdapter = new CartAdapter(CartActivity.this, orderItems, orderCallback);
        recyclerViewCart.swapAdapter(cartAdapter, true);
    }

    private void createOrderCallback() {
        orderCallback = new OrderCallback() {
            @Override
            public void addToCart(ObjectId id) {}

            @Override
            public void removeFromCart(ObjectId id) {
                if (orderItems == null)
                    return;
                for (MenuItem orderItem : orderItems) {
                    if (orderItem.getID().equals(id)) {
                        orderItems.remove(orderItem);
                        Toast.makeText(CartActivity.this,
                                orderItem.getName()+" was removed from cart",
                                Toast.LENGTH_SHORT).show();
                        updateUI();
                        return;
                    }
                }
            }
        };
    }

    private void goToMenu() {
        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("CART", orderItems);
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }

    private void goToPay() {
        if (orderItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(getApplicationContext(), PayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("CART", orderItems);
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }


    private void findViews() {
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        BTN_goToMenu = findViewById(R.id.BTN_goToMenu);
        BTN_goToPay = findViewById(R.id.BTN_goToPay);

        BTN_goToMenu.setOnClickListener(v -> {
            goToMenu();
        });
        BTN_goToPay.setOnClickListener(v -> {
            goToPay();
        });

        updateUI();
    }
}
