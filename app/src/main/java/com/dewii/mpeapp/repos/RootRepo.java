package com.dewii.mpeapp.repos;

import android.content.Context;

import androidx.annotation.CallSuper;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.VolleyError;
import com.dewii.mpeapp.constants.Status;
import com.dewii.mpeapp.models.ApiStatusModel;
import com.dewii.mpeapp.models.UserModel;
import com.dewii.mpeapp.networks.VolleyProvider;
import com.dewii.mpeapp.utils.AppUtils;
import com.dewii.mpeapp.utils.NetworkUtils;
import com.dewii.mpeapp.utils.PreferenceUtils;

import org.json.JSONObject;

public abstract class RootRepo {
    private UserModel userModel;
    private VolleyProvider volleyProvider;
    private MutableLiveData<ApiStatusModel> apiStatusModelMutable;

    @CallSuper
    public void init(Context context) {
        userModel = PreferenceUtils.getUserModel(context);
        volleyProvider = VolleyProvider.getProvider(context);

        apiStatusModelMutable = new MutableLiveData<>();
    }

    @CallSuper
    public void init() {
        userModel = PreferenceUtils.getUserModel(AppUtils.getContext());
        volleyProvider = VolleyProvider.getProvider(AppUtils.getContext());

        apiStatusModelMutable = new MutableLiveData<>();
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public VolleyProvider getVolleyProvider() {
        return volleyProvider;
    }

    public MutableLiveData<ApiStatusModel> getApiStatusModelMutable() {
        return apiStatusModelMutable;
    }

    public void setApiStatus(ApiStatusModel apiStatusModel) {
        apiStatusModelMutable.postValue(apiStatusModel);
    }

    public void apiRequestHit(int api, String message) {
        setApiStatus(new ApiStatusModel(api, Status.Request.API_HIT, message));
    }

    public void apiRequestSuccess(int api, String message) {
        setApiStatus(new ApiStatusModel(api, Status.Request.API_SUCCESS, message));
    }

    public void apiRequestFailure(int api, String message) {
        setApiStatus(new ApiStatusModel(api, Status.Request.API_ERROR, message));
    }

    public boolean isRequestSuccess(JSONObject jsonObject) {
        return jsonObject.optInt("status") == Status.Response.SUCCESS;
    }

    public boolean hasMessage(JSONObject jsonObject) {
        return jsonObject.has("message");
    }

    public boolean hasErrorDescription(JSONObject jsonObject) {
        return jsonObject.has("error_description");
    }

    public boolean hasConnection(int api) {
        boolean hasConnection = NetworkUtils.isNetworkAvailable();
        if (!hasConnection)
            apiRequestFailure(api, "No internet connection.");
        return hasConnection;
    }

    public String getErrorFromException(Exception exception) {
        return NetworkUtils.getErrorString(exception);
    }

    public String getErrorFromVolley(VolleyError volleyError) {
        return NetworkUtils.getErrorString(volleyError);
    }

    public String getErrorFromVolleyAsJson(VolleyError volleyError) {
        JSONObject jsonObject = NetworkUtils.getErrorJson(volleyError);
        if (jsonObject != null) {
            if (hasMessage(jsonObject))
                return jsonObject.optString("message");
            else if (hasErrorDescription(jsonObject))
                return jsonObject.optString("error_description");
        }
        return getErrorFromVolley(volleyError);
    }

    public void postError(int api, JSONObject jsonObject) {
        if (hasMessage(jsonObject))
            apiRequestFailure(api, jsonObject.optString("message"));
        else if (hasErrorDescription(jsonObject))
            apiRequestFailure(api, jsonObject.optString("error_description"));
        else
            apiRequestFailure(api, "Unknown error occured!");
    }

    public void onException(int api, Exception exception) {
        exception.printStackTrace();
        apiRequestFailure(api, getErrorFromException(exception));
    }
}
