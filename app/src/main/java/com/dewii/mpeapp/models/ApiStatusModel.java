package com.dewii.mpeapp.models;

import android.os.Parcel;

public class ApiStatusModel extends RootModel {
    private int api;
    private int status;
    private String message;

    public ApiStatusModel() {

    }

    public ApiStatusModel(int api, int status, String message) {
        this.api = api;
        this.status = status;
        this.message = message;
    }


    protected ApiStatusModel(Parcel in) {
        api = in.readInt();
        status = in.readInt();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(api);
        dest.writeInt(status);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ApiStatusModel> CREATOR = new Creator<ApiStatusModel>() {
        @Override
        public ApiStatusModel createFromParcel(Parcel in) {
            return new ApiStatusModel(in);
        }

        @Override
        public ApiStatusModel[] newArray(int size) {
            return new ApiStatusModel[size];
        }
    };

    public int getApi() {
        return api;
    }

    public void setApi(int api) {
        this.api = api;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ApiStatusModel{" +
                "api=" + api +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
