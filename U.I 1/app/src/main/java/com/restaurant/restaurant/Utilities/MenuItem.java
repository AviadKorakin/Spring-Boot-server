package com.restaurant.restaurant.Utilities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.restaurant.restaurant.General.ObjectId;

import java.util.ArrayList;

public class MenuItem implements Comparable<MenuItem>, Parcelable {
    private ObjectId id;
    private String category;
    private String name;
    private String description;
    private Double price;
    private String imgURL;
    private ArrayList<String> allergens;

    public MenuItem(
            ObjectId id,
            String category,
            String name,
            String description,
            Double price,
            String imgURL,
            ArrayList<String> allergens) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgURL = imgURL;
        this.allergens = allergens;
    }

    public ObjectId getID() {
        return id;
    }

    public void setID(ObjectId id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public ArrayList<String> getAllergens() {
        return allergens;
    }

    public void setAllergens(ArrayList<String> allergens) {
        this.allergens = allergens;
    }

    @Override
    public int compareTo(MenuItem o) {
        return this.category.compareTo(o.category);
    }

    // to pass it as bundle
    protected MenuItem(Parcel in) {
        id = (ObjectId) in.readValue(ObjectId.class.getClassLoader());
        category = in.readString();
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
        imgURL = in.readString();
        allergens = new ArrayList<>();
        in.readStringList(allergens);
    }

    public static final Parcelable.Creator<MenuItem> CREATOR = new Parcelable.Creator<MenuItem>() {
        @Override
        public MenuItem createFromParcel(Parcel in) {
            return new MenuItem(in);
        }

        @Override
        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeString(category);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeString(imgURL);
        dest.writeStringList(allergens);
    }
}



