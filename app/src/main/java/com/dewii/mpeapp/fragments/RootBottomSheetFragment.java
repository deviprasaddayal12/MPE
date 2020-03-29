package com.dewii.mpeapp.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.dewii.mpeapp.R;
import com.dewii.mpeapp.listeners.OnRootFragmentMethodsListener;
import com.dewii.mpeapp.models.UserModel;
import com.dewii.mpeapp.utils.AppUtils;
import com.dewii.mpeapp.utils.DialogUtils;
import com.dewii.mpeapp.utils.ExecutorUtils;
import com.dewii.mpeapp.utils.InputUtils;
import com.dewii.mpeapp.utils.NetworkUtils;
import com.dewii.mpeapp.utils.PreferenceUtils;
import com.dewii.mpeapp.utils.StringUtils;
import com.dewii.mpeapp.utils.ViewUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;

public abstract class RootBottomSheetFragment extends BottomSheetDialogFragment implements OnRootFragmentMethodsListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private UserModel userModel;
    private ContentLoadingProgressBar progressBar;

    private TextView tvToolbar;
    private ImageView ivToolbarLeft, ivToolbarRight;

    private Snackbar snackbar;
    private boolean fragmentVisibleToUser = false;

    private TextView tvNoData;
    private RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            onAdapterDataChanged();
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvToolbar = view.findViewById(R.id.tv_toolbar);
        ivToolbarLeft = view.findViewById(R.id.iv_toolbar_left);
        ivToolbarRight = view.findViewById(R.id.iv_toolbar_right);

        progressBar = view.findViewById(R.id.pb_loading);
        tvNoData = view.findViewById(R.id.tv_error);

        userModel = PreferenceUtils.getUserModel(getContext());

        setUpToolbar(view);
        initialiseViews(view);
        initialiseListeners(view);

        setUpRecycler(view);
        setUpViewpager(view);

        hideLoader();

        addObservers();

        requestData();
        setData();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_toolbar_left)
            getActivity().onBackPressed();
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

    public boolean isRequestValid() {
        return true;
    }

    public void toggleViews(boolean enable) {

    }

    public void showLoader() {
        showLoader(null);
    }

    public void showLoader(String message) {
        if (!StringUtils.isInvalid(message))
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
            snackbar = Snackbar.make(getActivity().findViewById(R.id.root), message,
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
                DialogUtils.showFailure(getActivity(), message, action);
        } else {
            if (soft)
                showInterrupt(message, true);
            else
                DialogUtils.showFailure(getActivity(), message);
        }
    }

    public void showWarning(boolean soft, String message, Runnable action) {
        if (action != null) {
            if (soft)
                showInterrupt(message, true, "Change", action);
            else
                DialogUtils.showWarning(getActivity(), message, action);
        } else {
            if (soft)
                showInterrupt(message, true);
            else
                DialogUtils.showWarning(getActivity(), message);
        }
    }

    public void showSuccess(boolean soft, String message, Runnable action) {
        if (action != null) {
            if (soft)
                showInterrupt(message, true, "Proceed", action);
            else
                DialogUtils.showSuccess(getActivity(), message, action);
        } else {
            if (soft)
                showInterrupt(message, true);
            else
                DialogUtils.showSuccess(getActivity(), message);
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

    String getResString(int resId) {
        return AppUtils.getString(getContext(), resId);
    }

    public boolean isNetworkAvailable() {
        return NetworkUtils.isNetworkAvailable();
    }

    UserModel getUserModel() {
        return userModel;
    }

    RecyclerView.AdapterDataObserver getAdapterDataObserver() {
        return adapterDataObserver;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        fragmentVisibleToUser = menuVisible;
    }

    public boolean isFragmentVisibleToUser() {
        return fragmentVisibleToUser;
    }

    public void onAdapterDataChanged() {

    }
}
