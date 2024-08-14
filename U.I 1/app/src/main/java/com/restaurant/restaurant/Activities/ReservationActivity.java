package com.restaurant.restaurant.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.restaurant.Adapters.TableAdapter;
import com.restaurant.restaurant.Boundaries.MiniAppCommandBoundary;
import com.restaurant.restaurant.Boundaries.ObjectBoundary;
import com.restaurant.restaurant.General.InvokedBy;
import com.restaurant.restaurant.General.ObjectId;
import com.restaurant.restaurant.General.TargetObject;
import com.restaurant.restaurant.R;
import com.restaurant.restaurant.Service.MiniAppCommandService;
import com.restaurant.restaurant.Service.RetrofitClient;
import com.restaurant.restaurant.Utilities.CurrentUser;
import com.restaurant.restaurant.Utilities.CustomSpinnerAdapter;
import com.restaurant.restaurant.Utilities.MenuItem;
import com.restaurant.restaurant.Utilities.Reservation;
import com.restaurant.restaurant.Utilities.ReservationCallback;
import com.restaurant.restaurant.Utilities.Table;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReservationActivity extends AppCompatActivity {
    private Button BTN_back, BTN_complete, BTN_revert;
    private EditText TXT_people_count;
    private Spinner spinnerDayOfWeek, spinnerTimeOfDay;
    private LinearLayout peopleCountLayout, dayOfWeekLayout, timeOfDayLayout;
    private RecyclerView recyclerViewTables;

    private TableAdapter tableAdapter;
    private ReservationCallback reservationCallback;
    ArrayList<MenuItem> orderItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        Intent intent = getIntent();
        orderItems = intent.getParcelableArrayListExtra("CART");

        findViews();
        setupSpinners();
    }

    private void findViews() {
        peopleCountLayout = findViewById(R.id.peopleCountLayout);
        dayOfWeekLayout = findViewById(R.id.dayOfWeekLayout);
        timeOfDayLayout = findViewById(R.id.timeOfDayLayout);
        BTN_back = findViewById(R.id.BTN_back);
        BTN_complete = findViewById(R.id.BTN_complete);
        BTN_revert = findViewById(R.id.BTN_revert);
        recyclerViewTables = findViewById(R.id.recyclerViewTables);
        recyclerViewTables.setLayoutManager(new LinearLayoutManager(this));
        TXT_people_count = findViewById(R.id.TXT_people_count);
        spinnerDayOfWeek = findViewById(R.id.spinner_day_of_week);
        spinnerTimeOfDay = findViewById(R.id.spinner_time_of_day);

        reservationCallback = new ReservationCallback() {
            @Override
            public void reserve(ObjectId tableId) {
                postReservation(tableId);
                detailsUI();
            }
        };

        BTN_back.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MenuActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("CART", orderItems);
            i.putExtras(bundle);
            startActivity(i);
            finish();
        });
        BTN_complete.setOnClickListener(v -> {
            checkTables();
        });
        BTN_revert.setOnClickListener(v -> {
            detailsUI();
        });
    }

    private void setupSpinners() {
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        int[] dayIcons = {R.drawable.monday, R.drawable.tuesday, R.drawable.wednesday,
                R.drawable.thursday, R.drawable.friday, R.drawable.saturday,
                R.drawable.sunday};
        CustomSpinnerAdapter dayAdapter = new CustomSpinnerAdapter(this, daysOfWeek, dayIcons);
        spinnerDayOfWeek.setAdapter(dayAdapter);

        String[] timesOfDay = new String[24];
        for (int i = 0; i < 24; i++) {
            timesOfDay[i] = String.format("%02d:00", i);
        }
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timesOfDay);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeOfDay.setAdapter(timeAdapter);
    }

    private void detailsUI() {
        BTN_complete.setVisibility(View.VISIBLE);
        peopleCountLayout.setVisibility(View.VISIBLE);
        dayOfWeekLayout.setVisibility(View.VISIBLE);
        timeOfDayLayout.setVisibility(View.VISIBLE);

        recyclerViewTables.setVisibility(View.GONE);
        BTN_complete.setVisibility(View.VISIBLE);
        BTN_revert.setVisibility(View.GONE);
    }

    private void chooseTableUI(ArrayList<Table> tables) {
        BTN_complete.setVisibility(View.GONE);
        peopleCountLayout.setVisibility(View.GONE);
        dayOfWeekLayout.setVisibility(View.GONE);
        timeOfDayLayout.setVisibility(View.GONE);

        recyclerViewTables.setVisibility(View.VISIBLE);
        BTN_complete.setVisibility(View.GONE);
        BTN_revert.setVisibility(View.VISIBLE);

        tableAdapter = new TableAdapter(ReservationActivity.this, tables, reservationCallback);
        recyclerViewTables.swapAdapter(tableAdapter, true);
    }

    private void checkTables() {
        MiniAppCommandBoundary commandBoundary = new MiniAppCommandBoundary();
        commandBoundary.setCommand("returnTables");
        commandBoundary.setTargetObject(null);
        commandBoundary.setInvokedBy(new InvokedBy(CurrentUser.getUserId()));

        Map<String, Object> commandAttributes = new LinkedHashMap<>();
        commandAttributes.put("capacity", Integer.valueOf(TXT_people_count.getText().toString()));
        commandAttributes.put("size", 4);
        commandAttributes.put("page", 0);

        // Get selected day and time from spinners
        String selectedDay = spinnerDayOfWeek.getSelectedItem().toString();
        String selectedTime = spinnerTimeOfDay.getSelectedItem().toString();

        // Get current year and month
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        // Convert day of the week to a date in the current week
        int dayOfMonth = getDayOfMonthFromDayOfWeek(selectedDay);

        // Construct the date and time string
        String dateAndTime = String.format("%04d-%02d-%02dT%s:00.000Z", year, month, dayOfMonth, selectedTime);
        commandAttributes.put("date", dateAndTime);

        commandBoundary.setCommandAttributes(commandAttributes);

        Retrofit client = RetrofitClient.getInstance();
        MiniAppCommandService commandService = client.create(MiniAppCommandService.class);
        Call<List<Object>> call = commandService.invokeCommand("Restaurant", commandBoundary);

        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if (response.isSuccessful()) {
                    if (response.body().size() > 0) {
                        ArrayList<Table> tables = new ArrayList<>();
                        for (Object object : response.body()) {
                            ObjectMapper a = new ObjectMapper();
                            ObjectBoundary objectBoundary;
                            String json;
                            try {
                                json = a.writeValueAsString(object);
                                objectBoundary = a.readValue(json, ObjectBoundary.class);
                                Map<String, Object> details = objectBoundary.getObjectDetails();
                                tables.add(new Table(
                                        objectBoundary.getObjectId(),
                                        objectBoundary.getAlias(),
                                        Integer.valueOf(details.get("tableNumber").toString().charAt(0)) - 48,
                                        (String) details.get("locationDesc"),
                                        (ArrayList<Reservation>) details.get("Reservations")
                                ));
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        chooseTableUI(tables);
                    } else {
                        Toast.makeText(ReservationActivity.this,
                                "No free tables at that time",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ReservationActivity.this,
                            "Error " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
            }
        });
    }

    private void postReservation(ObjectId tableId) {
        MiniAppCommandBoundary commandBoundary = new MiniAppCommandBoundary();
        commandBoundary.setCommand("setAReservation");
        commandBoundary.setTargetObject(new TargetObject(tableId));
        commandBoundary.setInvokedBy(new InvokedBy(CurrentUser.getUserId()));

        Map<String, Object> commandAttributes = new LinkedHashMap<>();
        String selectedDay = spinnerDayOfWeek.getSelectedItem().toString();
        String selectedTime = spinnerTimeOfDay.getSelectedItem().toString();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = getDayOfMonthFromDayOfWeek(selectedDay);

        String dateAndTime = String.format("%04d-%02d-%02dT%s:00.000Z", year, month, dayOfMonth, selectedTime);
        commandAttributes.put("date", dateAndTime);
        commandAttributes.put("phoneNumber", "054-8989898");
        commandAttributes.put("email", dateAndTime);

        commandBoundary.setCommandAttributes(commandAttributes);

        Retrofit client = RetrofitClient.getInstance();
        MiniAppCommandService commandService = client.create(MiniAppCommandService.class);
        Call<List<Object>> call = commandService.invokeCommand("Restaurant", commandBoundary);

        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReservationActivity.this,
                            "Reservation accepted",
                            Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MenuActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("CART", orderItems);
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();
                } else
                    Toast.makeText(ReservationActivity.this,
                            "Failed to reserve",
                            Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
            }
        });
    }

    private int getDayOfMonthFromDayOfWeek(String dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        int targetDay = 0;
        switch (dayOfWeek) {
            case "Sunday":
                targetDay = Calendar.SUNDAY;
                break;
            case "Monday":
                targetDay = Calendar.MONDAY;
                break;
            case "Tuesday":
                targetDay = Calendar.TUESDAY;
                break;
            case "Wednesday":
                targetDay = Calendar.WEDNESDAY;
                break;
            case "Thursday":
                targetDay = Calendar.THURSDAY;
                break;
            case "Friday":
                targetDay = Calendar.FRIDAY;
                break;
            case "Saturday":
                targetDay = Calendar.SATURDAY;
                break;
        }
        int dayDifference = targetDay - today;
        if (dayDifference < 0) {
            dayDifference += 7;
        }
        calendar.add(Calendar.DAY_OF_MONTH, dayDifference);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
