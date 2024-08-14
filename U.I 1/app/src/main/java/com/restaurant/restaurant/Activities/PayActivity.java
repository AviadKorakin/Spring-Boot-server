package com.restaurant.restaurant.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.textfield.TextInputEditText;
import com.restaurant.restaurant.Adapters.CartAdapter;
import com.restaurant.restaurant.Boundaries.MiniAppCommandBoundary;
import com.restaurant.restaurant.Boundaries.ObjectBoundary;
import com.restaurant.restaurant.Enums.RoleEnum;
import com.restaurant.restaurant.General.CommandId;
import com.restaurant.restaurant.General.CreatedBy;
import com.restaurant.restaurant.General.InvokedBy;
import com.restaurant.restaurant.General.Location;
import com.restaurant.restaurant.General.ObjectId;
import com.restaurant.restaurant.General.TargetObject;
import com.restaurant.restaurant.R;
import com.restaurant.restaurant.Service.MiniAppCommandService;
import com.restaurant.restaurant.Service.ObjectService;
import com.restaurant.restaurant.Service.RetrofitClient;
import com.restaurant.restaurant.Utilities.CurrentUser;
import com.restaurant.restaurant.Utilities.MenuItem;
import com.restaurant.restaurant.Utilities.OrderCallback;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PayActivity extends AppCompatActivity {
    private TextView TXT_price;
    private TextInputEditText TXT_notes;
    private TextInputEditText TXT_address;
    private TextInputEditText TXT_phone;


    private LinearLayoutCompat LAY_notes;
    private LinearLayoutCompat LAY_tableNumber;
    private LinearLayoutCompat LAY_address;
    private LinearLayoutCompat LAY_phone;

    private RadioGroup radio_select;
    private AppCompatRadioButton takeaway_radio_button;
    private AppCompatRadioButton delivery_radio_button;

    private AppCompatButton BTN_goToCart;
    private AppCompatButton BTN_pay;
    private  MiniAppCommandBoundary commandBoundary;
    ArrayList<MenuItem> orderItems = new ArrayList<>();
    Double totalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Intent intent = getIntent();
        orderItems = intent.getParcelableArrayListExtra("CART");

        findViews();
        setUI();
    }

    private ArrayList<ObjectId> extractIds(ArrayList<MenuItem> objects) {
        ArrayList<ObjectId> ids = new ArrayList<>();
        for (MenuItem object : objects)
            ids.add(object.getID());
        return ids;
    }

    private void setUI() {
        for (MenuItem orderItem : orderItems) {
            totalPrice += orderItem.getPrice();
        }
        totalPrice = Math.round(totalPrice * 100.0) / 100.0;;

        TXT_price.setText(""+String.format("%.2f",totalPrice));
    }

    private void goToCart() {
        Intent i = new Intent(getApplicationContext(), CartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("CART", orderItems);
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }

    private void postOrderCustomer() {
        commandBoundary = new MiniAppCommandBoundary();
        commandBoundary.setCommand("makeAnOrder");
        commandBoundary.setCommandId(new CommandId(CurrentUser.getSuperapp(), "Restaurant", "1"));
        commandBoundary.setInvokedBy(new InvokedBy(CurrentUser.getUserId()));

        Map<String, Object> details = new LinkedHashMap<>();
        if (takeaway_radio_button.isChecked())
            details.put("type", "TAKE_AWAY");
        else
            details.put("type", "DELIVERY");

        details.put("objectIds", extractIds(orderItems));
        details.put("totalPrice", totalPrice);
        details.put("notes", TXT_notes.getText().toString());
        details.put("tableNumber", null);
        details.put("address", TXT_address.getText().toString());
        details.put("phoneNumber", TXT_phone.getText().toString());
        details.put("status", null);

        commandBoundary.setCommandAttributes(details);

        Retrofit client = RetrofitClient.getInstance();
        MiniAppCommandService commandService = client.create(MiniAppCommandService.class);
        Call<List<Object>> call = commandService.invokeCommand("Restaurant", commandBoundary);

        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if (response.isSuccessful())
                    Toast.makeText(PayActivity.this,
                            "Order Created!",
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(PayActivity.this,
                            "Failed to create order",
                            Toast.LENGTH_SHORT).show();
                Log.d("dddd", response.toString());
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {

            }
        });
    }
    private void postOrderWaiter() {
        ObjectBoundary objectBoundary = new ObjectBoundary();
        objectBoundary.setObjectId(new ObjectId(
                CurrentUser.getSuperapp(),
                "null"));
        objectBoundary.setType("Order");
        objectBoundary.setAlias("SITTING");
        objectBoundary.setLocation(new Location());
        objectBoundary.setActive(true);
        objectBoundary.setCreationTimestamp(null);
        objectBoundary.setCreatedBy(new CreatedBy(CurrentUser.getUserId()));

        Map<String, Object> details = new LinkedHashMap<>();
        details.put("objectIds", extractIds(orderItems));
        details.put("totalPrice", totalPrice);
        details.put("notes", TXT_notes.getText().toString());
        details.put("address", null);
        details.put("tableNumber", null);
        details.put("phoneNumber", null);
        details.put("status", null);
        objectBoundary.setObjectDetails(details);

        Retrofit client = RetrofitClient.getInstance();
        ObjectService objectService = client.create(ObjectService.class);
        Call<ObjectBoundary> call = objectService.storeInDatabase(
                CurrentUser.getSuperapp(), CurrentUser.getEmail(), objectBoundary);

        call.enqueue(new Callback<ObjectBoundary>() {
            @Override
            public void onResponse(Call<ObjectBoundary> call, Response<ObjectBoundary> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PayActivity.this,
                            "Order Created!",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PayActivity.this,
                            response.errorBody().toString(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ObjectBoundary> call, Throwable t) {
                Toast.makeText(PayActivity.this,t.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void findViews() {
        TXT_price = findViewById(R.id.TXT_price);
        TXT_notes = findViewById(R.id.TXT_notes);
        TXT_address = findViewById(R.id.TXT_address);
        TXT_phone = findViewById(R.id.TXT_phone);
        LAY_tableNumber = findViewById(R.id.LAY_tableNumber);
        LAY_address = findViewById(R.id.LAY_address);
        LAY_phone = findViewById(R.id.LAY_phone);
        radio_select = findViewById(R.id.radio_select);
        delivery_radio_button = findViewById(R.id.delivery_radio_button);
        takeaway_radio_button = findViewById(R.id.takeaway_radio_button);
        BTN_goToCart = findViewById(R.id.BTN_goToCart);
        BTN_pay = findViewById(R.id.BTN_pay);

        fixUI();

        BTN_goToCart.setOnClickListener(v -> {
            goToCart();
        });
        BTN_pay.setOnClickListener(v -> {
            if (CurrentUser.getRole().equals(RoleEnum.MINIAPP_USER))
                postOrderCustomer();
            else
                postOrderWaiter();
        });
    }

    private void fixUI() {
        if (CurrentUser.getRole().equals(RoleEnum.MINIAPP_USER)) {
            LAY_address.setVisibility(View.VISIBLE);
            LAY_phone.setVisibility(View.VISIBLE);
            radio_select.setVisibility(View.VISIBLE);
            LAY_tableNumber.setVisibility(View.GONE);
        }
        else {
            LAY_address.setVisibility(View.GONE);
            LAY_phone.setVisibility(View.GONE);
            radio_select.setVisibility(View.GONE);
            LAY_tableNumber.setVisibility(View.VISIBLE);
        }
    }
}
