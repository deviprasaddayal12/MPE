package com.dewii.mpeapp.listeners;

import android.view.View;

public interface OnRootActivityMethodsListener {
    void setUpToolbar();

    void initialiseViews();

    void initialiseListeners();

    void setUpRecycler();

    void setUpViewpager();

    void addObservers();

    void requestData();

    void setData();
}
