package com.example.menu_editor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu_editor.Utilities.Order;
import com.example.menu_editor.Utilities.Table;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {
    private Context context;
    private List<Table> tables;

    public TableAdapter(Context context, List<Table> tables) {
        this.context = context;
        this.tables = tables;
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

        holder.TXT_capacity.setText("Capacity: "+table.getCapacity());
        holder.TXT_tableNumber.setText("Table number: "+table.getTableNumber());
        holder.TXT_locationDesc.setText("Description: "+table.getLocationDesc());
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    public static class TableViewHolder extends RecyclerView.ViewHolder {
        TextView TXT_capacity;
        TextView TXT_tableNumber;
        TextView TXT_locationDesc;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            TXT_capacity = itemView.findViewById(R.id.TXT_capacity);
            TXT_tableNumber = itemView.findViewById(R.id.TXT_tableNumber);
            TXT_locationDesc = itemView.findViewById(R.id.TXT_locationDesc);
        }
    }
}
