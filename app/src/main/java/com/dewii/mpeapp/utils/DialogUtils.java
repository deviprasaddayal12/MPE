package com.dewii.mpeapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.dewii.mpeapp.R;
import com.dewii.mpeapp.constants.Type;
import com.dewii.mpeapp.managers.FileManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public final class DialogUtils {
    public static final String TAG = DialogUtils.class.getCanonicalName();
    private static AlertDialog responseInfoDialog;

    private DialogUtils() {

    }

    public static void showFailure(Activity activity, int stringResId) {
        showFailure(activity, AppUtils.getString(activity, stringResId));
    }



    public static void showFailure(Activity activity, String message) {
        createCancelableNoActionDialog(activity, Type.Info.SORRY, message);
    }

    public static void showFailure(Activity activity, String message, Runnable positive) {
        createNonCancelableSingleActionDialog(activity, Type.Info.SORRY, message, "Okay", positive);
    }



    public static void showSuccess(Activity activity, String message) {
        createCancelableNoActionDialog(activity, Type.Info.SMILES, message);
    }

    public static void showSuccess(Activity activity, String message, Runnable positive) {
        createNonCancelableSingleActionDialog(activity, Type.Info.SMILES, message, "Proceed", positive);
    }



    public static void showWarning(Activity activity, String message) {
        createCancelableNoActionDialog(activity, Type.Info.OOPS, message);
    }

    public static void showWarning(Activity activity, String message, Runnable positive) {
        createNonCancelableSingleActionDialog(activity, Type.Info.OOPS, message, "Okay", positive);
    }



    public static void showConfirm(Activity activity, String message) {
        createCancelableNoActionDialog(activity, Type.Info.SURE, message);
    }

    public static void showConfirm(Activity activity, String message, Runnable positive) {
        createNonCancelableSingleActionDialog(activity, Type.Info.SURE, message, "Confirm", positive);
    }



    private static void createCancelableNoActionDialog(Context context, int infoType, String infoMessage) {
        createCancelableDialog(context, infoType, infoMessage, null, null, null, null);
    }

    private static void createCancelableSingleActionDialog(Context context, int infoType, String infoMessage,
                                               String pText, Runnable positive) {
        createCancelableDialog(context, infoType, infoMessage, pText, positive, null, null);
    }

    private static void createCancelableDialog(Context context, int infoType, String infoMessage,
                                     String pText, Runnable positive, String nText, Runnable negative) {
        createDialog(context, true, infoType, infoMessage, pText, positive, nText, negative);
    }



    private static void createNonCancelableNoActionDialog(Context context, int infoType, String infoMessage) {
        createNonCancelableDialog(context, infoType, infoMessage, null, null, null, null);
    }

    private static void createNonCancelableSingleActionDialog(Context context, int infoType, String infoMessage,
                                                           String pText, Runnable positive) {
        createNonCancelableDialog(context, infoType, infoMessage, pText, positive, null, null);
    }

    private static void createNonCancelableDialog(Context context, int infoType, String infoMessage,
                                               String pText, Runnable positive, String nText, Runnable negative) {
        createDialog(context, false, infoType, infoMessage, pText, positive, nText, negative);
    }



    private static void createDialog(Context context, boolean cancelable, int infoType, String infoMessage,
                                     String pText, Runnable positive, String nText, Runnable negative) {
        createDialog(context, cancelable, infoType, infoMessage, R.drawable.ic_launcher_foreground,
                pText, positive, nText, negative);
    }



    private static void createDialog(Context context, boolean cancelable, int infoType, String infoMessage,
                                     int infoDrawable,
                                     String pText, Runnable positive, String nText, Runnable negative) {
        try {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_info, null);
            builder.setView(view);

            ImageView ivInfoIcon = view.findViewById(R.id.iv_info_icon);

            TextView tvTitle = view.findViewById(R.id.tv_info_title);
            TextView tvMessage = view.findViewById(R.id.tv_info_message);

            MaterialButton btnPositive = view.findViewById(R.id.btn_positive);
            MaterialButton btnNegative = view.findViewById(R.id.btn_negative);

            switch (infoType) {
                case Type.Info.OOPS: {
                    tvTitle.setText(context.getString(R.string.oops));
                    pText = context.getString(R.string.done);
                }
                break;
                case Type.Info.SORRY: {
                    tvTitle.setText(context.getString(R.string.sorry));
                    pText = context.getString(R.string.done);
                }
                break;
                case Type.Info.SMILES: {
                    tvTitle.setText(context.getString(R.string.smiles));
                    pText = context.getString(R.string.done);
                }
                break;
                case Type.Info.HEY: {
                    tvTitle.setText(context.getString(R.string.hey));
                    pText = context.getString(R.string.done);
                }
                break;
                case Type.Info.SURE: {
                    tvTitle.setText(context.getString(R.string.hey_you_sure));
                    pText = context.getString(R.string.yes_im);
                    nText = context.getString(R.string.no_cancel);
                }
                break;
                case Type.Info.LOGOUT: {
                    tvTitle.setText(context.getString(R.string.hey));
                    pText = context.getString(R.string.logout);
                    nText = context.getString(R.string.cancel);
                }
                break;
                case Type.Info.UPDATE: {
                    tvTitle.setText(context.getString(R.string.update_available));
                    pText = context.getString(R.string.update);
                }
                break;
                case Type.Info.CUSTOM: {
                    tvTitle.setText(context.getString(R.string.hey));
                }
                break;
            }

            builder.setCancelable(cancelable);

            tvMessage.setText(infoMessage);
            ivInfoIcon.setImageResource(infoDrawable);

            btnPositive.setText(pText);
            btnPositive.setOnClickListener(v -> {
                if (positive != null)
                    positive.run();

                if (responseInfoDialog != null) {
                    responseInfoDialog.cancel();
                    responseInfoDialog = null;
                }
            });

            if (nText != null) {
                btnNegative.setText(nText);
                btnNegative.setOnClickListener(v -> {
                    if (negative != null)
                        negative.run();

                    if (responseInfoDialog != null) {
                        responseInfoDialog.cancel();
                        responseInfoDialog = null;
                    }
                });
            } else {
                ViewUtils.hideViews(btnNegative);
            }

            responseInfoDialog = builder.create();
            responseInfoDialog.show();

            resizeDialogToMatchWindow(context, responseInfoDialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showFileSources(Context context, final FileManager sourceChoiceListener) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_file_source);

        dialog.findViewById(R.id.view_camera).setOnClickListener(view -> {
            if (sourceChoiceListener != null) {
                sourceChoiceListener.onCameraChosen();
            }
            dialog.dismiss();
        });
        dialog.findViewById(R.id.view_gallery).setOnClickListener(view -> {
            if (sourceChoiceListener != null) {
                sourceChoiceListener.onGalleryChosen();
            }
            dialog.dismiss();
        });
        dialog.findViewById(R.id.view_file_manager).setOnClickListener(view -> {
            if (sourceChoiceListener != null) {
                sourceChoiceListener.onFileManagerChosen();
            }
            dialog.dismiss();
        });

        dialog.setCancelable(true);
        dialog.show();

        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        resizeDialogToMatchWindow(context, dialog);
    }

    public static void resizeDialogToMatchWindow(Context context, Dialog dialog) {
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 1.00);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
    }
}
