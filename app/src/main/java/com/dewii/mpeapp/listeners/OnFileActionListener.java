package com.dewii.mpeapp.listeners;

import java.io.File;

public interface OnFileActionListener {
    void onFilePathCreatedForCamera(String imagePath, File imageFile);

    void onFileAddRequest();

    void onFileViewRequest(int position);

    void onFileDelete(int position);
}
