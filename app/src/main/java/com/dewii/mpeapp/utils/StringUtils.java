package com.dewii.mpeapp.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dewii.mpeapp.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Locale;

public final class StringUtils {
    public static final String TAG = StringUtils.class.getCanonicalName();

    public static final int RETURN_EMPTY_STRING_IF_NULL = 0;
    public static final int RETURN_HYPHEN_STRING_IF_NULL = 1;
    public static final int REPLACE_UNDERSCORE_WITH_SPACE = 2;
    public static final int REPLACE_UNDERSCORE_WITH_HYPHEN = 3;
    public static final int ADD_SPACE_IF_EMPTY = 4;
    public static final int RETURN_EMPTY_IF_HYPHEN = 5;

    private StringUtils() {

    }

    public static String[] filterString(int type, String... toBeFiltered) {
        ArrayList<String> filteredStrings = new ArrayList<>();
        for (String string : toBeFiltered) {
            filteredStrings.add(filterString(type, string));
        }

        String[] filtered = new String[filteredStrings.size()];
        for (int i = 0; i < filteredStrings.size(); i++)
            filtered[i] = filteredStrings.get(i);

        return filtered;
    }

    public static String filterString(int type, String toBeFiltered) {
        toBeFiltered = (toBeFiltered == null || toBeFiltered.equals("null")) ? "" : toBeFiltered;

        switch (type) {
            case RETURN_EMPTY_STRING_IF_NULL:
                return toBeFiltered;
            case RETURN_HYPHEN_STRING_IF_NULL:
                return toBeFiltered.isEmpty() ? "-" : toBeFiltered;
            case REPLACE_UNDERSCORE_WITH_SPACE:
                return toBeFiltered.replace('_', ' ');
            case REPLACE_UNDERSCORE_WITH_HYPHEN:
                return toBeFiltered.replace('_', '-');
            case ADD_SPACE_IF_EMPTY:
                return toBeFiltered.isEmpty() ? " " : toBeFiltered;
            case RETURN_EMPTY_IF_HYPHEN:
                return toBeFiltered.contains("-") && toBeFiltered.length() == 1 ? "" : toBeFiltered;
            default:
                return toBeFiltered;
        }
    }

    private static String upperCaseWords(String sentence) {
        String[] words = sentence.trim().split(" ");
        String newSentence = "";
        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            word = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();

            newSentence = i == 0 ? newSentence + word : newSentence + " " + word;
        }

        return newSentence;
    }

    public static boolean isInvalid(String s) {
        return TextUtils.isEmpty(s) || s.toLowerCase().equals("null") || s.toLowerCase().contains("n/a");
    }

    public static boolean isInvalid(EditText editText) {
        return isInvalid(editText.getText().toString());
    }

    public static boolean isInvalid(EditText editText, String fallBackError) {
        if (isInvalid(editText.getText().toString())) {
            editText.setError(fallBackError);
            editText.requestFocus();

            return true;
        }
        return false;
    }

    public static void setText(TextView textView, int i) {
        setText(textView, asString(i));
    }

    public static void setText(TextView textView, double i) {
        setText(textView, asString(i));
    }

    public static void setText(TextView textView, float i) {
        setText(textView, asString(i));
    }

    public static void setText(TextView textView, String s) {
        textView.setText(s);
        ViewUtils.toggleViewVisibility(!isInvalid(s), textView);
    }

    public static void setText(TextView textView, String s, @NonNull String fallback) {
        textView.setText(isInvalid(s) ? fallback : s);
    }

    public static void setText(TextView textView, int i, @NonNull String fallback) {
        textView.setText(isInvalid(asString(i)) ? fallback : asString(i));
    }

    public static void appendText(TextView textView, String s, String separator, @NonNull String fallback) {
        if (isInvalid(textView.getText().toString()))
            textView.setText(String.format("%s", isInvalid(s) ? fallback : s));
        else
            textView.setText(String.format("%s %s %s", textView.getText(), separator, isInvalid(s) ? fallback : s));
    }

    public static void setDateTime(TextView textView, long timestamp, @NonNull String fallback) {
        try {
            if (timestamp == 0) {
                textView.setText(fallback);
                return;
            }
            setText(textView, DateUtils.toLocalString(timestamp), fallback);
        } catch (Exception e) {
            e.printStackTrace();
            textView.setText(fallback);
        }
    }

    public static void setDateInPattern(TextView textView, String dateString, String from, String to, @NonNull String fallback) {
        try {
            String inPattern = DateUtils.toPattern(dateString, from, to);
            setText(textView, inPattern, fallback);
        } catch (Exception e) {
            e.printStackTrace();
            textView.setText(fallback);
        }
    }

    public static Spannable colorSearchText(Context context, String searchText, String targetText) {
        Spannable spannable = new SpannableString(targetText);

        int start = targetText.toLowerCase().indexOf(searchText.toLowerCase());
        spannable.setSpan(new ForegroundColorSpan(AppUtils.getColor(context, R.color.colorPrimaryDark)),
                start, start + searchText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }

    public static String asStringWithHeader(String header, String data) {
        return asStringWithHeader(header, "", data);
    }

    public static String asStringWithFooter(String data, String footer) {
        return asStringWithFooter(data, "", footer);
    }

    public static String asStringWithHeader(String header, String separator, String data) {
        return isInvalid(data) ? data : String.format(Locale.getDefault(), "%s %s %s", header, separator, data);
    }

    public static String asStringWithFooter(String data, String separator, String footer) {
        return isInvalid(data) ? data : String.format(Locale.getDefault(), "%s %s %s", data, separator, footer);
    }

    public static String asString(double d) {
        return NumericUtils.isInvalidDouble(d)
                ? null : String.format(Locale.getDefault(), "%.3f", d);
    }

    public static String asStringWithHeader(String header, double d) {
        return asStringWithHeader(header, "-", asString(d));
    }

    public static String asStringWithFooter(double d, String footer) {
        return asStringWithFooter(asString(d), "-", footer);
    }

    public static String asString(int i) {
        return String.format(Locale.getDefault(), "%d", i);
    }

    public static String asStringWithHeader(String header, int i) {
        return asStringWithHeader(header, "-", asString(i));
    }

    public static String asStringWithFooter(int i, String footer) {
        return asStringWithFooter(asString(i), "-", footer);
    }

    public static String asString(long l) {
        return String.format(Locale.getDefault(), "%d", l);
    }

    public static String asStringWithHeader(String header, long l) {
        return asStringWithHeader(header, "-", asString(l));
    }

    public static String asStringWithFooter(long l, String footer) {
        return asStringWithFooter(asString(l), "-", footer);
    }

    public static String toCapWordSentence(String sentence) {
        if (isInvalid(sentence))
            return "N/A";
        String appendChar = " ";
        String[] words;
        if (sentence.contains("_")) {
            words = sentence.split("_");
//            appendChar = " ";
        } else if (sentence.contains(" ")) {
            words = sentence.split(" ");
//            appendChar = " ";
        } else if (sentence.contains("/")) {
            words = sentence.split("/");
            appendChar = "/";
        } else
            words = new String[]{sentence};

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (i != 0)
                builder.append(appendChar);
            builder.append(toCapWord(words[i]));
        }
        return builder.toString();
    }

    public static String toCapWord(String word) {
        if (isInvalid(word))
            return "N/A";
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

    public static void setCountInTab(TabLayout tabLayout, int index, String title, int count, int total) {
        if (count != 0 && total != 0) {
            tabLayout.getTabAt(index).setText(String.format(Locale.getDefault(), "%s(%d/%d)",
                    title, count, total));
        } else {
            tabLayout.getTabAt(index).setText(String.format(Locale.getDefault(), "%s(%d)",
                    title, total));
        }
    }

    public static String arrayToString(String[] array) {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i == 0) {
                value = value.append("[");
            }
            value = value.append("\"").append(array[i]).append("\"");
            if (i == array.length - 1) {
                value = value.append("]");
            } else {
                value = value.append(",");
            }
        }
        return value.toString();
    }

    public static String getSearchMessage(String search, String applyingSearchMessage) {
        return search == null ? "Removing search..."
                : search.isEmpty() ? "Clearing search..."
                : applyingSearchMessage;
    }

    public static void setTextAndBackgroundColor(TextView textView, int color) {
        textView.setTextColor(color);
        ViewUtils.colorBackground(textView, color);
    }

    public static void setTextAndBackgroundColor(TextView textView, String color) {
        setTextAndBackgroundColor(textView, NumericUtils.parseColor(color));
    }

    public static String hideChars(String s) {
        // no of chars correctly visible: if, is phone, length is > 4  is 4 else 3
        // if mail, 2 chars before @
        int l = s.length();
        int end = s.contains("@") ? s.indexOf('@') - 2 : l > 4 ? l - 4 : l - 1;
        for (int j = 0; j < l; j++) {
            if (j != end)
                s = s.replace(s.charAt(j), 'X');
            else
                return s;
        }
        return s;
    }

    public static boolean compareInput(EditText inputField, String comparingValue) {
        if (isInvalid(inputField))
            return false;
        else if (isInvalid(comparingValue))
            return false;
        else
            return !inputField.getText().toString().trim().equals(comparingValue);
    }
}
