package com.example.menu_editor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu_editor.Boundaries.ObjectBoundary;
import com.example.menu_editor.General.ObjectId;
import com.example.menu_editor.MenuAdapter;
import com.example.menu_editor.OrderAdapter;
import com.example.menu_editor.R;
import com.example.menu_editor.Service.ObjectService;
import com.example.menu_editor.Service.RetrofitClient;
import com.example.menu_editor.TableAdapter;
import com.example.menu_editor.Utilities.CurrentUser;
import com.example.menu_editor.Utilities.MenuItem;
import com.example.menu_editor.Utilities.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TablesActivity extends AppCompatActivity {
    ArrayList<Table> tables = new ArrayList<>();

    private AppCompatButton BTN_back, BTN_addNewTable;
    private RecyclerView recyclerViewTables;
    private TableAdapter tableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tables);

        findViews();
        fetchTablesData();
    }

    private void findViews() {
        recyclerViewTables = findViewById(R.id.recyclerViewTables);
        recyclerViewTables.setLayoutManager(new LinearLayoutManager(this));

        BTN_addNewTable = findViewById(R.id.BTN_addNewTable);
        BTN_back = findViewById(R.id.BTN_back);

        BTN_addNewTable.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), NewTableActivity.class);
            startActivity(i);
            finish();
        });
        BTN_back.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void updateUI() {
        tableAdapter = new TableAdapter(TablesActivity.this, tables);
        recyclerViewTables.swapAdapter(tableAdapter, true);
    }

    private void fetchTablesData() {
        Retrofit client = RetrofitClient.getInstance();
        ObjectService objectService = client.create(ObjectService.class);
        Call<List<ObjectBoundary>> call = objectService.getObjectByType(
                "Table", CurrentUser.getSuperapp(), CurrentUser.getEmail(), 5, 0);

        call.enqueue(new Callback<List<ObjectBoundary>>() {
            @Override
            public void onResponse(Call<List<ObjectBoundary>> call,
                                   Response<List<ObjectBoundary>> response) {
                if (response.isSuccessful()) {
                    List<ObjectBoundary> ObjectBoundaries = response.body();
                    Map<String, Object> details;
                    tables.clear();
                    if (ObjectBoundaries == null)
                        return;

                    for (ObjectBoundary table : ObjectBoundaries) {
                        if (table.getActive()) {
                            ObjectId id = table.getObjectId();
                            String capacity = table.getAlias();
                            details = table.getObjectDetails();
                            int tableNumber = Integer.valueOf(details.get("tableNumber").toString().charAt(0))-48;
                            String locationDesc = (String) details.get("locationDesc");

                            tables.add(new Table(
                                    id,
                                    capacity,
                                    tableNumber,
                                    locationDesc));
                        }
                    }
                    Collections.sort(tables);
                    updateUI();
                }
                else {
                    Toast.makeText(TablesActivity.this,
                            response.errorBody().toString(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<ObjectBoundary>> call, Throwable t) {
                Toast.makeText(TablesActivity.this,t.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
}