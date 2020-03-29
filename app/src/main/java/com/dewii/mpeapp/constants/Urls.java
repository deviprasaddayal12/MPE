package com.dewii.mpeapp.constants;

import com.android.volley.Request;

public enum Urls {
    ;

    private int apiId;
    private String url;
    private int type;

    Urls(int apiId, String url, int type) {
        this.apiId = apiId;
        this.url = url;
        this.type = type;
    }

    public int getApiId() {
        return apiId;
    }

    public String getUrl() {
        return url;
    }

    public int getType() {
        return type;
    }
}
