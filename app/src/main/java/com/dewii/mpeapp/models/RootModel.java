package com.dewii.mpeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RootModel implements Parcelable {
    private int id;

    public RootModel() {

    }

    protected RootModel(Parcel in) {
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RootModel> CREATOR = new Creator<RootModel>() {
        @Override
        public RootModel createFromParcel(Parcel in) {
            return new RootModel(in);
        }

        @Override
        public RootModel[] newArray(int size) {
            return new RootModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RootModel{" +
                "id=" + id +
                '}';
    }
}
