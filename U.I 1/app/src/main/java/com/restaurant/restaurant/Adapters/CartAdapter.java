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

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MenuViewHolder> {

    private Context context;
    private List<MenuItem> cartItems;
    private OrderCallback orderCallback;

    public CartAdapter(Context context, List<MenuItem> menuItems, OrderCallback orderCallback) {
        this.context = context;
        this.cartItems = menuItems;
        this.orderCallback = orderCallback;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem menuItem = cartItems.get(position);

        holder.nameTextView.setText(menuItem.getName());
        holder.descriptionTextView.setText(menuItem.getDescription());
        holder.priceTextView.setText(String.format("₪ %.2f", menuItem.getPrice()));
        holder.BTN_addToOrder.setOnClickListener(v -> {
            orderCallback.removeFromCart(menuItem.getID());
        });

        // Load image using Glide library
        Glide.with(context)
                .load(menuItem.getImgURL())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(holder.imageView);

        // Display allergens
        StringBuilder allergensBuilder = new StringBuilder();
        allergensBuilder.append("Allergens: \n \t");
        if (menuItem.getAllergens() != null && !menuItem.getAllergens().isEmpty()) {
            for (String allergen : menuItem.getAllergens()) {
                allergensBuilder.append(allergen).append(", ");
            }
            // Remove the last comma and space
            allergensBuilder.setLength(allergensBuilder.length() - 2);
        }
        holder.allergensTextView.setText(allergensBuilder.toString());
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView descriptionTextView;
        TextView priceTextView;
        TextView allergensTextView;
        Button BTN_addToOrder;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            allergensTextView = itemView.findViewById(R.id.allergensTextView);
            BTN_addToOrder = itemView.findViewById(R.id.BTN_addToOrder);
            BTN_addToOrder.setText("Remove");
        }
    }
}
