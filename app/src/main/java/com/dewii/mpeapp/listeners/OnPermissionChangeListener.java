package com.dewii.mpeapp.listeners;

public interface OnPermissionChangeListener {
    void onAllowPermission(String[] permissions);

    void onPermissionDenied();
}
