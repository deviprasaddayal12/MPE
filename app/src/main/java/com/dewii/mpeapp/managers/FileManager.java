package com.dewii.mpeapp.managers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraCharacteristics;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.MutableLiveData;

import com.dewii.mpeapp.BuildConfig;
import com.dewii.mpeapp.constants.App;
import com.dewii.mpeapp.listeners.OnFileActionListener;
import com.dewii.mpeapp.listeners.OnFileAttachListener;
import com.dewii.mpeapp.listeners.OnFileSourceChoiceListener;
import com.dewii.mpeapp.models.FileModel;
import com.dewii.mpeapp.utils.AppUtils;
import com.dewii.mpeapp.utils.DialogUtils;
import com.dewii.mpeapp.utils.FileUtils;
import com.dewii.mpeapp.utils.LogUtils;
import com.dewii.mpeapp.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileManager implements OnFileSourceChoiceListener {
    public static final String TAG = FileManager.class.getCanonicalName();

    public interface OnFileDeleteListener {
        void onFileDelete(int position);
    }

    private AppCompatActivity activity;
    private String from;
    private OnFileActionListener onFileActionListener;

    private MutableLiveData<HashMap<String, FileModel>> fileModelsMutable;
    private HashMap<String, FileModel> fileModelsMap;

    private String namePrefix;
    private String imagePathForCamera;
    private File storageDest;
    private boolean changeable;

    public FileManager(AppCompatActivity activity, String from) {
        this(activity, from, null);
    }

    public FileManager(AppCompatActivity activity, OnFileActionListener onFileActionListener) {
        this(activity, "", onFileActionListener);
    }

    public FileManager(AppCompatActivity activity, String from, OnFileActionListener onFileActionListener) {
        this.activity = activity;
        this.from = from;
        this.onFileActionListener = onFileActionListener;
        this.storageDest = null;
        this.changeable = true;

        fileModelsMap = new HashMap<>();
        fileModelsMutable = new MutableLiveData<>();
        fileModelsMutable.postValue(fileModelsMap);
    }

    /**
     * Checks for the file manager permission during the runtime, if already granted, takes to the startFileBrowser()
     */
    private void requestDirectories() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, App.Usage.STORAGE);
            } else {
                makeDirectories();
            }
        } else {
            makeDirectories();
        }
    }

    /**
     * This method is used to create, if unavailable, directories for saving files
     * It checks for the existence of folders for the respective file type and category
     */
    private void makeDirectories() {

    }

    public void showFileSources() {
        DialogUtils.showFileSources(activity, this);
    }

    @Override
    public void onCameraChosen() {
        gotoCamera();
    }

    @Override
    public void onGalleryChosen() {
        gotoGallery();
    }

    @Override
    public void onFileManagerChosen() {
        gotoFileBrowser();
    }

    public void gotoCamera(String namePrefix) {
        this.namePrefix = namePrefix;
        gotoCamera();
    }

    public void gotoCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (AppUtils.checkPermission(activity, App.Usage.CAMERA))
                startCamera();
        } else
            startCamera();
    }

    @SuppressLint("InlinedApi")
    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = FileUtils.createImageFile(activity, storageDest, namePrefix);

                if (onFileActionListener != null)
                    onFileActionListener.onFilePathCreatedForCamera(photoFile.getAbsolutePath(), photoFile);
                else
                    imagePathForCamera = photoFile.getAbsolutePath();
            } catch (IOException ex) {
                // Todo : handle exceptions while creating files for version M and higher
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity,
                        BuildConfig.APPLICATION_ID + ".provider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                intent.putExtra("android.intent.extras.CAMERA_FACING", CameraCharacteristics.LENS_FACING_FRONT);
                activity.startActivityForResult(intent, App.SystemIntent.CAMERA);
            }
        } else {
            Toast.makeText(activity, "No application found to perform the action.", Toast.LENGTH_SHORT).show();
        }
    }

    public void gotoGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (AppUtils.checkPermission(activity, App.Usage.GALLERY))
                startGallery();
        } else
            startGallery();
    }

    private void startGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("image/*");
            activity.startActivityForResult(intent, App.SystemIntent.GALLERY);
        } else {
            Toast.makeText(activity, "No application found to perform the action.", Toast.LENGTH_SHORT).show();
        }
    }

    public void gotoFileBrowser() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (AppUtils.checkPermission(activity, App.Usage.BROWSER))
                startFileBrowser();
        } else
            startFileBrowser();
    }

    private void startFileBrowser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        activity.startActivityForResult(intent, App.SystemIntent.BROWSER);
    }

    public String getFileDataFromIntent(Intent data) {
        try {
            if (data.getData() != null) {
                String imagePath = FileUtils.getInternalStoragePath(activity, data.getData());
                if (imagePath == null) {
                    if (FileUtils.isSDCardAvailable(activity)) {
                        imagePath = FileUtils.getExternalStoragePath(activity, data.getData());
                    }
                    if (imagePath == null) {
                        DialogUtils.showFailure(activity, "Unable to fetch file from SDCard." +
                                "\nMove the file into Device Memory and try again.");
                        return null;
                    }
                }
                return imagePath;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[] getClipFileDataFromIntent(Intent data) {
        try {
            if (data.getClipData() != null) {
                String[] imagePathArray = new String[data.getClipData().getItemCount()];
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    String imagePath = FileUtils.getInternalStoragePath(activity, data.getClipData().getItemAt(i).getUri());
                    if (imagePath == null) {
                        DialogUtils.showFailure(activity, "Unable to fetch file from SDCard." +
                                "\nMove the file into Device Memory and try again.");
                        continue;
                    }
                    imagePathArray[i] = imagePath;
                }
                return imagePathArray;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getImagePathForCamera() {
        return imagePathForCamera;
    }

    public void attachFiles(Intent data) {
        try {
            if (data.getData() != null) {
                String imagePath = getFileDataFromIntent(data);
                if (!StringUtils.isInvalid(imagePath)) {
                    addFile(imagePath, true);
                }

            } else if (data.getClipData() != null) {
                String[] imagePathArray = getClipFileDataFromIntent(data);
                if (imagePathArray != null) {
                    for (String imagePath : imagePathArray) {
                        if (!StringUtils.isInvalid(imagePath)) {
                            addFile(imagePath, false);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFile() {
        addFile(null, true);
    }

    private void addFile(String imagePath, boolean toast) {
        if (imagePath == null) {
            imagePath = imagePathForCamera;
            if (imagePath == null) {
                LogUtils.logError(TAG, "addFile: File path not found!");
                return;
            }
        }

        if (!fileModelsMap.containsKey(imagePath)) {
            fileModelsMap.put(imagePath, FileUtils.toFileModel(imagePath));
            fileModelsMutable.postValue(fileModelsMap);
        } else {
            if (toast)
                Toast.makeText(activity, "File already chosen.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addFile(ArrayList<String> filePaths, String imagePath, boolean toast,
                         OnFileAttachListener onFileAttachListener) {
        if (!filePaths.contains(imagePath)) {
            onFileAttachListener.onFileAttached(imagePath, new File(imagePath));
        } else {
            if (toast)
                Toast.makeText(activity, "File already chosen.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean hasFileModels() {
        return fileModelsMap.size() != 0;
    }

    public ArrayList<FileModel> getFileModels() {
        ArrayList<FileModel> fileModels = new ArrayList<>();
        for (String filePath : fileModelsMap.keySet()) {
            fileModels.add(fileModelsMap.get(filePath));
        }
        return fileModels;
    }

    public ArrayList<String> getFilePaths() {
        return new ArrayList<>(fileModelsMap.keySet());
    }
}
