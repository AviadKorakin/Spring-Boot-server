package com.restaurant.restaurant.General;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.restaurant.restaurant.Utilities.MenuItem;

public class ObjectId implements Parcelable {
    private String superapp;
    private String id;

    public ObjectId() {

    }

    /**
     * This constructor creates an ObjectId object with the provided SuperApp name
     * and identifier.
     *
     * @param superapp The name of the SuperApp this object belongs.
     * @param id       The unique identifier for this object.
     */
    public ObjectId(String superapp, String id) {
        this.superapp = superapp;
        this.id = id;
    }


    public String getSuperapp() {
        return superapp;
    }

    public void setSuperapp(String superapp) {
        this.superapp = superapp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ObjectId that = (ObjectId) o;
        return superapp.equals(that.superapp) && id.equals(that.id);
    }


    // to pass it as bundle
    protected ObjectId(Parcel in) {
        superapp = in.readString();
        id = in.readString();
    }

    public static final Parcelable.Creator<ObjectId> CREATOR = new Parcelable.Creator<ObjectId>() {
        @Override
        public ObjectId createFromParcel(Parcel in) {
            return new ObjectId(in);
        }

        @Override
        public ObjectId[] newArray(int size) {
            return new ObjectId[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(superapp);
        dest.writeString(id);
    }
}
