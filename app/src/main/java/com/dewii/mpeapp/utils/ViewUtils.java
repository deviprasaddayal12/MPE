package com.dewii.mpeapp.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;

import com.dewii.mpeapp.R;
import com.dewii.mpeapp.listeners.OnDateSelectedListener;
import com.dewii.mpeapp.listeners.OnSearchVisibilityChangeListener;
import com.dewii.mpeapp.widgets.RecyclerBottomDecoration;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public final class ViewUtils {
    public static final String TAG = ViewUtils.class.getCanonicalName();

    private ViewUtils() {
    }

    public static View getRootView(Activity activity) {
        return activity.findViewById(R.id.root);
    }

    public static void elevateViewOnClick(final Activity context, final View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.animate().translationZ(10f).start();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    context.runOnUiThread(() -> {
                        try {
                            view.animate().translationZ(0f).start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }, 1000);
        }
    }

    public static boolean toggleSearchViewVisibility(boolean showing, LinearLayout llSearchPanel,
                                                     ImageView ivSearch, OnSearchVisibilityChangeListener onSearchVisibilityChangeListener) {
        if (showing) {
            int cx = ivSearch.getWidth() / 2;
            int[] centre = new int[2];
            ivSearch.getLocationOnScreen(centre);
            hideWithCircularContractAnimation(centre[0] + cx, centre[1], llSearchPanel);

            if (onSearchVisibilityChangeListener != null)
                onSearchVisibilityChangeListener.onSearchVisibilityChanged(false);
        } else {
            int cx = ivSearch.getWidth() / 2;
            int[] centre = new int[2];
            ivSearch.getLocationOnScreen(centre);
            showWithCircularExpandAnimation(centre[0] + cx, centre[1], llSearchPanel);

            if (onSearchVisibilityChangeListener != null)
                onSearchVisibilityChangeListener.onSearchVisibilityChanged(true);
        }

        return !showing;
    }

    public static void showDateDialog(Context context, boolean setNowAsMax,
                                      OnDateSelectedListener onDateSelectedListener) {
        showDateDialog(context, null, null, setNowAsMax, onDateSelectedListener);
    }

    public static void showDateDialog(Context context, String title, String previousDate, boolean setNowAsMax,
                                      OnDateSelectedListener onDateSelectedListener) {
        if (title == null) title = "Please select date";

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (datePicker, i, i1, i2) -> {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.YEAR, i);
            calendar1.set(Calendar.MONTH, i1);
            calendar1.set(Calendar.DAY_OF_MONTH, i2);
            onDateSelectedListener.onDateSelected(DateUtils.getDateInString(calendar1.getTime()), calendar1);
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle(title);
        if (setNowAsMax)
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        else
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public static Snackbar getNetworkSnackbar(Snackbar snackbar, View view, boolean online) {
        String message = online ? "We're back online..." : "Internet unavailable!";
        int duration = online ? Snackbar.LENGTH_SHORT : Snackbar.LENGTH_INDEFINITE;

        if (snackbar != null) {
            snackbar.dismiss();
        }

        snackbar = Snackbar.make(view, message, duration);
        snackbar.show();

        return snackbar;
    }

    public static Snackbar getGpsSnackbar(Snackbar snackbar, View view, boolean turnedOn, final Runnable runnableAction) {
        String message = turnedOn ? "GPS enabled..." : "GPS off!";
        int duration = turnedOn ? Snackbar.LENGTH_SHORT : Snackbar.LENGTH_INDEFINITE;

        if (snackbar != null) {
            snackbar.dismiss();
        }

        snackbar = Snackbar.make(view, message, duration);
        snackbar.show();
        if (!turnedOn) {
            snackbar.setAction("ENABLE", v -> runnableAction.run());
        }

        return snackbar;
    }

    // this callback is used only for different departmental fragments used to create new requisition
    public static void toggleButtonWidthWithLayoutVisibility(boolean expandLayout, MaterialButton button, LinearLayout layout) {
        ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
        layoutParams.width = expandLayout ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        button.setLayoutParams(layoutParams);

        toggleViewVisibility(expandLayout, layout);
    }

    public static void showWithCircularExpandAnimation(int centerX, int centerY, final View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) Math.hypot(centerX, centerY);
            Animator anim = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, 0, finalRadius);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    view.setVisibility(View.VISIBLE);
                }
            });
            anim.start();
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hideWithCircularContractAnimation(int centerX, int centerY, final View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) Math.hypot(centerX, centerY);
            Animator anim = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, finalRadius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.GONE);
                }
            });
            anim.start();
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public static void showFromBottomAnimation(Context context, View view) {
        Animation bottomUp = AnimationUtils.loadAnimation(context, R.anim.rise_from_bottom);
        view.startAnimation(bottomUp);
        view.setVisibility(View.VISIBLE);
    }

    public static void hideToBottomAnimation(Context context, View view) {
        Animation bottomDown = AnimationUtils.loadAnimation(context, R.anim.sink_to_bottom);
        view.startAnimation(bottomDown);
        view.setVisibility(View.GONE);
    }

    public static void showByFadingIn(Context context, final View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        view.setAnimation(animation);
//        Animation animation = view.getAnimation();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.start();
    }

    public static void hideByFadingOut(Context context, final View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        view.setAnimation(animation);
//        Animation animation = view.getAnimation();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.start();
    }

    public static void animateLayoutChanges(View view) {
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        ((ViewGroup) view).setLayoutTransition(layoutTransition);
    }

    public static void makeToast(Context context, String message) {
        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
    }

    public static void makeSnackBar(FloatingActionButton fab, String message) {
        makeSnackBar(fab, message, Snackbar.LENGTH_SHORT, null, null);
    }

    public static Snackbar makeSnackBar(FloatingActionButton fab, String message, String actionTitle, final Runnable action) {
        return makeSnackBar(fab, message, Snackbar.LENGTH_LONG, actionTitle, action);
    }

    public static Snackbar makeSnackBar(FloatingActionButton fab, String message, int duration, String actionTitle, final Runnable action) {
        Snackbar snackbar = Snackbar.make(fab, message, duration);
        if (action != null) {
            snackbar.setAction(actionTitle, view -> action.run());
        }
        snackbar.show();
        return snackbar;
    }

    public static void toggleViewVisibility(boolean visible, View... views) {
        toggleViewVisibility(false, visible, views);
    }

    public static void toggleViewVisibility(boolean makeInvisible, boolean visible, View... views) {
        if (visible)
            showViews(views);
        else {
            if (makeInvisible)
                invisibleViews(views);
            else
                hideViews(views);
        }
    }

    public static void showViews(View... views) {
        for (View v : views)
            v.setVisibility(View.VISIBLE);
    }

    public static void hideViews(View... views) {
        for (View v : views)
            v.setVisibility(View.GONE);
    }

    public static void invisibleViews(View... views) {
        for (View v : views)
            v.setVisibility(View.INVISIBLE);
    }

    public static boolean isVisible(View v) {
        return v.getVisibility() == View.VISIBLE;
    }

    public static void swapViewVisibility(boolean showView1, View view1, View view2) {
        if (showView1) {
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.GONE);
        } else {
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.VISIBLE);
        }
    }

    public static int setAnimation(Context context, View viewToAnimate, int position, int lastPosition) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.rise_up);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }

        return lastPosition;
    }

    public static void toggleViewAbility(boolean enable, View... views) {
        if (enable)
            enableViews(views);
        else
            disableViews(views);
    }

    public static void disableViews(View... views) {
        for (View v : views)
            v.setEnabled(false);
    }

    public static void enableViews(View... views) {
        for (View v : views)
            v.setEnabled(true);
    }

    public static void changeProgressMode(boolean show, ContentLoadingProgressBar progressBar) {
        if (show)
            progressBar.show();
        else
            progressBar.hide();
    }

    public static void makeUneditable(TextInputEditText... textInputEditTexts) {
        for (TextInputEditText textInputEditText : textInputEditTexts) {
            textInputEditText.setInputType(InputType.TYPE_NULL);
            textInputEditText.setFilters(new InputFilter[]{InputUtils.getNoInputFilter()});
            textInputEditText.setFocusableInTouchMode(false);
        }
    }

    public static void makeEditable(InputFilter[] inputFilters, int inputType, TextInputEditText... textInputEditTexts) {
        for (TextInputEditText textInputEditText : textInputEditTexts) {
            textInputEditText.setInputType(inputType);
            textInputEditText.setFilters(inputFilters);
            textInputEditText.setFocusableInTouchMode(true);
        }
    }

    public static int[] getMenuLocation(View menuItemView, View toolbarView) {
        int[] itemWindowLocation = new int[2];
        menuItemView.getLocationInWindow(itemWindowLocation);

        int[] toolbarWindowLocation = new int[2];
        toolbarView.getLocationInWindow(toolbarWindowLocation);

        int itemX = itemWindowLocation[0] - toolbarWindowLocation[0];
        int itemY = itemWindowLocation[1] - toolbarWindowLocation[1];
        int centerX = itemX + menuItemView.getWidth() / 2;
        int centerY = itemY + menuItemView.getHeight() / 2;

        return new int[]{centerX, centerY};
    }

    public static int[] getViewCentre(View view) {
        int[] centre = new int[2];
        view.getLocationOnScreen(centre);
        return centre;
    }

    public static void toggleFabVisibility(boolean visible, FloatingActionButton floatingActionButton) {
        if (visible)
            floatingActionButton.show();
        else
            floatingActionButton.hide();
    }

    public static void toggleEFabVisibility(boolean visible, ExtendedFloatingActionButton floatingActionButton) {
        if (visible)
            floatingActionButton.show();
        else
            floatingActionButton.hide();
    }

    public static void addFilterOffset(Context context, RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new RecyclerBottomDecoration(
                (int) context.getResources().getDimension(R.dimen.spacing_32), true));
    }

    public static void addFabOffset(Context context, RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new RecyclerBottomDecoration(
                (int) context.getResources().getDimension(R.dimen.spacing_64), false));
    }

    public static void addFabAndFilterOffsets(Context context, RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new RecyclerBottomDecoration(
                (int) context.getResources().getDimension(R.dimen.spacing_32),
                (int) context.getResources().getDimension(R.dimen.spacing_64)));
    }

    public static boolean toggleArrowBy180(boolean isMoreVisible, ImageView ivSeeMore, View target) {
        if (isMoreVisible) {
            ivSeeMore.animate().rotation(0).start();
            ViewUtils.hideViews(target);

            return false;
        } else {
            ivSeeMore.animate().rotation(-180).start();
            ViewUtils.showViews(target);

            return true;
        }
    }

    public static int toAlpha(String colorString, float ratio) {
        return toAlpha(Color.parseColor(colorString), ratio);
    }

    public static int toAlpha(int color, float ratio) {
        return Color.argb(Math.round(Color.alpha(color) * ratio),
                Color.red(color),
                Color.green(color),
                Color.blue(color)
        );
    }

    public static void colorBackground(View view, String color) {
        try {
            GradientDrawable gradientDrawable = (GradientDrawable) view.getBackground();
            gradientDrawable.setColor(toAlpha(color, 0.2f));
            view.setBackground(gradientDrawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void colorBackground(View view, int color) {
        try {
            GradientDrawable gradientDrawable = (GradientDrawable) view.getBackground();
            gradientDrawable.setColor(toAlpha(color, 0.2f));
            view.setBackground(gradientDrawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
