package com.example.menu_editor.General;

import android.os.Parcel;
import android.os.Parcelable;

public class UserId implements Parcelable {
    private String superapp;
    private String email;
    private String delimiter = "/";

    public UserId() {
    }

    public UserId(String superapp, String email) {
        this.email = email;
        this.superapp = superapp;
    }

    public UserId(String composedId) {
        String[] composed = composedId.split(delimiter);
        this.superapp = composed[0];
        this.email = composed[1];
    }

    protected UserId(Parcel in) {
        superapp = in.readString();
        email = in.readString();
        delimiter = in.readString();
    }

    public static final Creator<UserId> CREATOR = new Creator<UserId>() {
        @Override
        public UserId createFromParcel(Parcel in) {
            return new UserId(in);
        }

        @Override
        public UserId[] newArray(int size) {
            return new UserId[size];
        }
    };

    public String getSuperapp() {
        return superapp;
    }

    public void setSuperapp(String superapp) {
        this.superapp = superapp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserId userId = (UserId) o;
        return superapp.equals(userId.superapp) && email.equals(userId.email);
    }

    @Override
    public String toString() {
        return superapp + delimiter + email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(superapp);
        dest.writeString(email);
        dest.writeString(delimiter);
    }
}
