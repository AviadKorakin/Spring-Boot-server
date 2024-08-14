package com.restaurant.restaurant.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.restaurant.restaurant.R;
import com.restaurant.restaurant.Utilities.MenuItem;
import com.restaurant.restaurant.Utilities.OrderCallback;
import com.restaurant.restaurant.Utilities.ReservationCallback;
import com.restaurant.restaurant.Utilities.Table;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    private Context context;
    private List<Table> tables;
    private ReservationCallback reservationCallback;

    public TableAdapter(Context context, List<Table> tables, ReservationCallback reservationCallback) {
        this.context = context;
        this.tables = tables;
        this.reservationCallback = reservationCallback;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.table, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        Table table = tables.get(position);

        holder.TXT_locationDesc.setText("Table Description: " + table.getLocationDesc());
        holder.TXT_tableNumber.setText("Table Number: " + table.getTableNumber());
        holder.TXT_capacity.setText("Table Capacity: " + table.getCapacity());

        holder.BTN_reserve.setOnClickListener(v -> {
            reservationCallback.reserve(table.getId());
        });
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    public static class TableViewHolder extends RecyclerView.ViewHolder {
        TextView TXT_locationDesc;
        TextView TXT_tableNumber;
        TextView TXT_capacity;
        Button BTN_reserve;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            TXT_locationDesc = itemView.findViewById(R.id.TXT_locationDesc);
            TXT_tableNumber = itemView.findViewById(R.id.TXT_tableNumber);
            TXT_capacity = itemView.findViewById(R.id.TXT_capacity);
            BTN_reserve = itemView.findViewById(R.id.BTN_reserve);
        }
    }
}
