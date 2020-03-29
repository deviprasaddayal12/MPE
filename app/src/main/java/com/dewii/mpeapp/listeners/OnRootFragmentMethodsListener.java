package com.dewii.mpeapp.listeners;

import android.view.View;

public interface OnRootFragmentMethodsListener {
    void setUpToolbar(View view);

    void initialiseViews(View view);

    void initialiseListeners(View view);

    void setUpRecycler(View view);

    void setUpViewpager(View view);

    void addObservers();

    void requestData();

    void setData();
}
