package com.example.menu_editor;

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
import com.example.menu_editor.Utilities.MenuItem;
import com.example.menu_editor.Utilities.ChangeCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Context context;
    private List<MenuItem> menuItems;
    private ChangeCallback changeCallback;

    public MenuAdapter(Context context, List<MenuItem> menuItems, ChangeCallback changeCallback) {
        this.context = context;
        this.menuItems = menuItems;
        this.changeCallback = changeCallback;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);

        holder.TXT_category.setText(menuItem.getCategory());
        holder.TXT_title.setText(menuItem.getName());
        holder.TXT_description.setText(menuItem.getDescription());
        holder.TXT_allergens.setText(menuItem.getAllergens().toString());
        holder.TXT_price.setText(String.format("%.2f", menuItem.getPrice()));
        holder.TXT_imgUrl.setText(menuItem.getImgURL());

        holder.BTN_delete.setOnClickListener(v -> {
            changeCallback.delete(menuItem);
        });
        holder.BTN_update.setOnClickListener(v -> {
            menuItem.setCategory(holder.TXT_category.getText().toString());
            menuItem.setName(holder.TXT_title.getText().toString());
            menuItem.setDescription(holder.TXT_description.getText().toString());
            ArrayList<String> allergens = new ArrayList<>();
            Collections.addAll(allergens, holder.TXT_allergens.getText().toString().split(","));
            menuItem.setAllergens(allergens);
            menuItem.setPrice(Double.valueOf(holder.TXT_price.getText().toString()));
            menuItem.setImgURL(holder.TXT_imgUrl.getText().toString());

            changeCallback.change(menuItem);
        });

        // Load image using Glide library
        Glide.with(context)
                .load(menuItem.getImgURL())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(holder.imageView);

        // Display allergens
        StringBuilder allergensBuilder = new StringBuilder();
        if (menuItem.getAllergens() != null && !menuItem.getAllergens().isEmpty()) {
            for (String allergen : menuItem.getAllergens()) {
                allergensBuilder.append(allergen).append(",");
            }
            // Remove the last comma and space
            allergensBuilder.setLength(allergensBuilder.length() - 1);
        }
        holder.TXT_allergens.setText(allergensBuilder.toString());
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView TXT_title;
        TextView TXT_description;
        TextView TXT_price;
        TextView TXT_allergens;
        TextView TXT_category;
        TextView TXT_imgUrl;
        Button BTN_delete;
        Button BTN_update;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            TXT_title = itemView.findViewById(R.id.TXT_title);
            TXT_description = itemView.findViewById(R.id.TXT_description);
            TXT_price = itemView.findViewById(R.id.TXT_price);
            TXT_allergens = itemView.findViewById(R.id.TXT_allergens);
            TXT_category = itemView.findViewById(R.id.TXT_category);
            TXT_imgUrl = itemView.findViewById(R.id.TXT_imgUrl);
            BTN_delete = itemView.findViewById(R.id.BTN_delete);
            BTN_update = itemView.findViewById(R.id.BTN_update);
        }
    }
}
