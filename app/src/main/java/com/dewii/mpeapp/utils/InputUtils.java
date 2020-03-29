package com.dewii.mpeapp.utils;

import android.content.Context;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public final class InputUtils {
    private static final String TAG = InputUtils.class.getCanonicalName();

    private static final int digitsBeforeDecimal = 8;
    private static final int digitsAfterDecimal = 3;
    private static final String decimal = ".";

    private InputUtils() {
    }

    public static void hideKeyboard(View view) {
        if (view.hasFocus()) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null)
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null)
                inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static InputFilter getNoInputFilter() {
        return (charSequence, i, i1, spanned, i2, i3) -> "";
    }

    public static InputFilter[] getLengthFilters(int length) {
        return new InputFilter[]{new InputFilter.LengthFilter(length)};
    }
}
