package com.algeriatour.utils;

import android.text.TextUtils;

public class FormatValidator {
    public static boolean isValidEmail(String target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
