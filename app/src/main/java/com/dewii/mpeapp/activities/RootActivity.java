package com.dewii.mpeapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dewii.mpeapp.R;
import com.dewii.mpeapp.listeners.OnRootActivityMethodsListener;
import com.dewii.mpeapp.utils.AppUtils;
import com.dewii.mpeapp.utils.DialogUtils;
import com.dewii.mpeapp.utils.ExecutorUtils;
import com.dewii.mpeapp.utils.InputUtils;
import com.dewii.mpeapp.utils.NetworkUtils;
import com.dewii.mpeapp.utils.StringUtils;
import com.dewii.mpeapp.utils.ViewUtils;
import com.google.android.material.snackbar.Snackbar;

public abstract class RootActivity extends AppCompatActivity implements OnRootActivityMethodsListener,
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private TextView tvToolbar;
    private ImageView ivToolbarLeft, ivToolbarRight;

    private TextView tvNoData;
    private RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            onAdapterDataChanged();
        }
    };

    private ContentLoadingProgressBar progressBar;
    private Snackbar snanckbarNetwork, snackbar;

    private MutableLiveData<Boolean> networkMutable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        networkMutable = new MutableLiveData<>();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        tvToolbar = findViewById(R.id.tv_toolbar);
        ivToolbarLeft = findViewById(R.id.iv_toolbar_left);
        ivToolbarRight = findViewById(R.id.iv_toolbar_right);

        progressBar = findViewById(R.id.pb_loading);
        tvNoData = findViewById(R.id.tv_error);

        setUpToolbar();

        initialiseViews();
        initialiseListeners();

        addObservers();

        requestData();
        setData();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_toolbar_left)
            onBackPressed();
        else if (v.getId() == R.id.iv_toolbar_right)
            onToolbarRightClick();
    }

    @Override
    public void onRefresh() {

    }

    public void setupToolBar(String title) {
        setupToolBar(title, false);
    }

    public void setupBackIconOnly() {
        setupToolBar(null, false);
    }

    public void setupToolBar(String title, boolean showToolbarRight) {
        if (tvToolbar != null)
            StringUtils.setText(tvToolbar, title);

        if (ivToolbarRight != null) {
            ViewUtils.toggleViewVisibility(showToolbarRight, ivToolbarRight);
            ivToolbarRight.setOnClickListener(this);
        }

        if (ivToolbarLeft != null)
            ivToolbarLeft.setOnClickListener(this);
    }

    public void onToolbarRightClick() {

    }

    public MutableLiveData<Boolean> getNetworkMutable() {
        return networkMutable;
    }

    public boolean isRequestValid() {
        return true;
    }

    public void toggleViews(boolean enable) {

    }

    public void showLoader() {
        showLoader(null);
    }

    public void showLoader(String message) {
        if (StringUtils.isInvalid(message))
            showInterrupt(message, false);

        if (progressBar != null)
            progressBar.show();
    }

    public void showKeyboard(View view) {
        InputUtils.showKeyboard(view);
    }

    public void hideKeyboard(View view) {
        InputUtils.hideKeyboard(view);
    }

    public void runOnUiAfterMillis(Runnable runnable, long delayMillis) {
        ExecutorUtils.getInstance().executeOnMainDelayed(runnable, delayMillis);
    }


    public void showInterrupt(String message, boolean definite) {
        showInterrupt(message, definite, null, null);
    }

    public void showInterrupt(String message, boolean definite, String actionText, Runnable action) {
        if (snackbar == null) {
            snackbar = Snackbar.make(findViewById(R.id.root), message,
                    definite ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_INDEFINITE);
        } else {
            snackbar.setText(message);
            snackbar.setDuration(definite ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_INDEFINITE);
        }
        if (action != null) {
            snackbar.setAction(actionText, v -> action.run());
        }
        snackbar.show();
    }

    public void showFailure(String message) {
        showFailure(true, message);
    }

    public void showWarning(String message) {
        showWarning(true, message);
    }

    public void showSuccess(String message) {
        showSuccess(true, message);
    }

    public void showFailure(boolean soft, String message) {
        showFailure(soft, message, null);
    }

    public void showWarning(boolean soft, String message) {
        showWarning(soft, message, null);
    }

    public void showSuccess(boolean soft, String message) {
        showSuccess(soft, message, null);
    }

    public void showFailure(boolean soft, String message, Runnable action) {
        if (action != null) {
            if (soft)
                showInterrupt(message, true, "Okay", action);
            else
                DialogUtils.showFailure(this, message, action);
        } else {
            if (soft)
                showInterrupt(message, true);
            else
                DialogUtils.showFailure(this, message);
        }
    }

    public void showWarning(boolean soft, String message, Runnable action) {
        if (action != null) {
            if (soft)
                showInterrupt(message, true, "Change", action);
            else
                DialogUtils.showWarning(this, message, action);
        } else {
            if (soft)
                showInterrupt(message, true);
            else
                DialogUtils.showWarning(this, message);
        }
    }

    public void showSuccess(boolean soft, String message, Runnable action) {
        if (action != null) {
            if (soft)
                showInterrupt(message, true, "Proceed", action);
            else
                DialogUtils.showSuccess(this, message, action);
        } else {
            if (soft)
                showInterrupt(message, true);
            else
                DialogUtils.showSuccess(this, message);
        }
    }


    public void hideLoader() {
        if (snackbar != null && snackbar.isShown())
            snackbar.dismiss();

        if (progressBar != null && progressBar.isShown())
            progressBar.hide();
    }

    public void gotoNextScreen() {

    }

    public void setError(String message) {
        if (tvNoData != null)
            tvNoData.setText(message);
    }

    void toggleTvNoData(boolean show) {
        if (tvNoData != null)
            ViewUtils.toggleViewVisibility(show, tvNoData);
    }

    RecyclerView.AdapterDataObserver getAdapterDataObserver() {
        return adapterDataObserver;
    }

    public boolean isNetworkAvailable() {
        return NetworkUtils.isNetworkAvailable();
    }

    public String getResString(int resId) {
        return AppUtils.getString(this, resId);
    }

    public void onAdapterDataChanged() {

    }
}
