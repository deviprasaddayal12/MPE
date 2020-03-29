package com.dewii.mpeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class UserModel extends RootModel {
    private String login;
    private String password;
    private String niceName;
    private String email;
    private String phone;
    private String url;
    private String registered;
    private String activationKey;
    private String status;
    private String displayName;

    private String capKey;
    private String caps;
    private String[] roles;

    public UserModel() {

    }

    protected UserModel(Parcel in) {
        super(in);
        login = in.readString();
        password = in.readString();
        niceName = in.readString();
        email = in.readString();
        phone = in.readString();
        url = in.readString();
        registered = in.readString();
        activationKey = in.readString();
        status = in.readString();
        displayName = in.readString();

        capKey = in.readString();
        caps = in.readString();
        roles = in.createStringArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(login);
        dest.writeString(password);
        dest.writeString(niceName);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(url);
        dest.writeString(registered);
        dest.writeString(activationKey);
        dest.writeString(status);
        dest.writeString(displayName);

        dest.writeString(capKey);
        dest.writeString(caps);
        dest.writeStringArray(roles);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNiceName() {
        return niceName;
    }

    public void setNiceName(String niceName) {
        this.niceName = niceName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCapKey() {
        return capKey;
    }

    public void setCapKey(String capKey) {
        this.capKey = capKey;
    }

    public String getCaps() {
        return caps;
    }

    public void setCaps(String caps) {
        this.caps = caps;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", niceName='" + niceName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", url='" + url + '\'' +
                ", registered='" + registered + '\'' +
                ", activationKey='" + activationKey + '\'' +
                ", status='" + status + '\'' +
                ", displayName='" + displayName + '\'' +
                ", capKey='" + capKey + '\'' +
                ", caps='" + caps + '\'' +
                ", roles=" + Arrays.toString(roles) +
                '}';
    }
}
