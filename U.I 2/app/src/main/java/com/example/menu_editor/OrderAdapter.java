package com.example.menu_editor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu_editor.Activities.OrderDetailsActivity;
import com.example.menu_editor.Utilities.Order;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orders;

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.orders, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.TXT_createdBy.setText(order.getCreatedBy().getUserId().getEmail());
        holder.TXT_type.setText(order.getType());
        holder.TXT_notes.setText(order.getNotes());
        holder.TXT_tableNumber.setText("" + order.getTableNumber());
        holder.TXT_address.setText(order.getAddress());
        holder.TXT_phoneNumber.setText(order.getPhoneNumber());
        holder.TXT_status.setText(order.getStatus());
        holder.TXT_totalPrice.setText("₪ " + String.format("%.2f", order.getTotalPrice()));

        // Set click listener for the update_icon
        holder.updateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("order", order);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView TXT_createdBy;
        MaterialTextView TXT_type;
        MaterialTextView TXT_notes;
        MaterialTextView TXT_tableNumber;
        MaterialTextView TXT_address;
        MaterialTextView TXT_phoneNumber;
        MaterialTextView TXT_status;
        MaterialTextView TXT_totalPrice;
        AppCompatImageView updateIcon;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            TXT_createdBy = itemView.findViewById(R.id.TXT_createdBy);
            TXT_type = itemView.findViewById(R.id.TXT_type);
            TXT_notes = itemView.findViewById(R.id.TXT_notes);
            TXT_tableNumber = itemView.findViewById(R.id.TXT_tableNumber);
            TXT_address = itemView.findViewById(R.id.TXT_address);
            TXT_phoneNumber = itemView.findViewById(R.id.TXT_phoneNumber);
            TXT_status = itemView.findViewById(R.id.TXT_status);
            TXT_totalPrice = itemView.findViewById(R.id.TXT_totalPrice);
            updateIcon = itemView.findViewById(R.id.update_icon);
        }
    }
}
