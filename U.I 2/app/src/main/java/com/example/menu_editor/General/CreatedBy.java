package com.example.menu_editor.General;

import android.os.Parcel;
import android.os.Parcelable;

public class CreatedBy implements Parcelable {
    private UserId userId;

    public CreatedBy() {
    }

    public CreatedBy(UserId userId) {
        this.userId = userId;
    }

    protected CreatedBy(Parcel in) {
        userId = in.readParcelable(UserId.class.getClassLoader());
    }

    public static final Creator<CreatedBy> CREATOR = new Creator<CreatedBy>() {
        @Override
        public CreatedBy createFromParcel(Parcel in) {
            return new CreatedBy(in);
        }

        @Override
        public CreatedBy[] newArray(int size) {
            return new CreatedBy[size];
        }
    };

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CreatedBy that = (CreatedBy) o;
        return userId.equals(that.userId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(userId, flags);
    }
}
