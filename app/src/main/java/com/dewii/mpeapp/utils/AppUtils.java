package com.dewii.mpeapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dewii.mpeapp.MyApplication;
import com.dewii.mpeapp.constants.App;
import com.dewii.mpeapp.listeners.OnPermissionChangeListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public final class AppUtils {
    public static final String TAG = AppUtils.class.getCanonicalName();

    private AppUtils() {

    }

    private static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String CAMERA = Manifest.permission.CAMERA;
    private static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String CALL_PHONE = Manifest.permission.CALL_PHONE;

    public static final String[] PERMISSIONS = new String[]{
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION,
            CAMERA,
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE,
            CALL_PHONE
    };

    private static final String[] LOCATION_PERMISSIONS = new String[]{
            ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
    };
    private static final String[] CAMERA_PERMISSIONS = new String[]{CAMERA};
    private static final String[] GALLERY_PERMISSIONS = new String[]{READ_EXTERNAL_STORAGE};
    private static final String[] STORAGE_PERMISSIONS = new String[]{WRITE_EXTERNAL_STORAGE};
    private static final String[] CALL_PERMISSIONS = new String[]{CALL_PHONE};

    private static boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private static boolean isRationale(Activity activity, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    public static boolean checkPermissions(Activity activity, boolean showDialog) {
        boolean allowedLocationAccess = hasPermission(activity, ACCESS_COARSE_LOCATION) || hasPermission(activity, ACCESS_FINE_LOCATION);
        boolean allowedCameraAccess = hasPermission(activity, CAMERA);
        boolean allowedGalleryAccess = hasPermission(activity, READ_EXTERNAL_STORAGE);
        boolean allowedStorageAccess = hasPermission(activity, WRITE_EXTERNAL_STORAGE);
        boolean allowedCallingAccess = hasPermission(activity, CALL_PHONE);

        if (!allowedLocationAccess
                || !allowedCameraAccess || !allowedGalleryAccess || !allowedStorageAccess
                || !allowedCallingAccess) {

            try {
                if (showDialog) {
                    new MaterialAlertDialogBuilder(activity)
                            .setTitle("Usage Needed")
                            .setMessage("App uses these permissions to provide better experience.")
                            .setPositiveButton("Ok, Show Permissions",
                                    (dialogInterface, i) ->
                                            ActivityCompat.requestPermissions(activity, PERMISSIONS, App.Usage.ANY)
                            ).create().show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        } else
            return true;
    }

    public static boolean checkPermission(Activity activity, int usage) {
        return checkPermission(activity, usage, true);
    }

    public static boolean checkPermission(Context context, int usage) {
        return checkPermission(context, usage, false);
    }

    private static boolean checkPermission(Context context, int usage, boolean showDialog) {
        String permission = PERMISSIONS[0];
        String[] permissions = PERMISSIONS;
        Runnable rationale = () -> {
        };

        if (usage == App.Usage.LOCATION) {
            permission = ACCESS_COARSE_LOCATION;
            permissions = LOCATION_PERMISSIONS;
            rationale = () -> showLocationRationaleDialog(context);

        } else if (usage == App.Usage.CAMERA) {
            permission = CAMERA;
            permissions = CAMERA_PERMISSIONS;
            rationale = () -> showCameraRationaleDialog(context);

        } else if (usage == App.Usage.GALLERY) {
            permission = READ_EXTERNAL_STORAGE;
            permissions = GALLERY_PERMISSIONS;
            rationale = () -> showGalleryRationaleDialog(context);

        } else if (usage == App.Usage.STORAGE) {
            permission = WRITE_EXTERNAL_STORAGE;
            permissions = STORAGE_PERMISSIONS;
            rationale = () -> showStorageRationaleDialog(context);

        } else if (usage == App.Usage.CALL) {
            permission = CALL_PHONE;
            permissions = CALL_PERMISSIONS;
            rationale = () -> showCallRationaleDialog(context);

        }

        if (!hasPermission(context, permission)) {
            try {
                if (showDialog) {
                    if (isRationale((Activity) context, permission))
                        rationale.run();
                    else
                        ActivityCompat.requestPermissions((Activity) context, permissions, usage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        } else
            return true;
    }

    private static void showLocationRationaleDialog(Context context) {
        showRationalePermissionDialog(context,
                "Location Permission Needed",
                "App accesses location to help navigate to vehicles on site.",
                LOCATION_PERMISSIONS,
                App.Usage.LOCATION
        );
    }

    private static void showCameraRationaleDialog(Context context) {
        showRationalePermissionDialog(context,
                "Camera Permission Needed",
                "App uses camera feature capture document images.",
                CAMERA_PERMISSIONS,
                App.Usage.CAMERA
        );
    }

    private static void showGalleryRationaleDialog(Context context) {
        showRationalePermissionDialog(context,
                "Gallery Permission Needed",
                "App uses gallery to choose files from.",
                GALLERY_PERMISSIONS,
                App.Usage.GALLERY
        );
    }

    private static void showStorageRationaleDialog(Context context) {
        showRationalePermissionDialog(context,
                "Storage Permission Needed",
                "App uses storage to download files.",
                STORAGE_PERMISSIONS,
                App.Usage.STORAGE
        );
    }

    private static void showCallRationaleDialog(Context context) {
        showRationalePermissionDialog(context,
                "Call Permission Needed",
                "App uses calling feature to call to concerned persons/authorities.",
                CALL_PERMISSIONS,
                App.Usage.CALL
        );
    }

    private static void showRationalePermissionDialog(Context context, String title, String message,
                                                      String[] permissions, int requestCode) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok, Allow", (dialogInterface, i)
                        -> ActivityCompat.requestPermissions((Activity) context, permissions, requestCode))
                .create().show();
    }

    public static void showAllowPermissionDialog(Context context, final String[] permissions,
                                                 final OnPermissionChangeListener listener) {
        try {
            if (context != null) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
                builder.setTitle("Alas! Permission required.");
                builder.setMessage("This permission is required by the application for better performance.");
                builder.setCancelable(false);
                builder.setPositiveButton("Allow", (dialog1, which) -> {
                    dialog1.dismiss();
                    if (listener != null)
                        listener.onAllowPermission(permissions);
                });
                builder.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return MyApplication.getContext();
    }

    public static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    public static int getColor(Context context, int resId) {
        return context.getResources().getColor(resId);
    }

    public static String appName() {
        return "MPE";
    }
}
