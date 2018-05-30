package com.algeriatour.utils;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class Networking {
    public static void initAndroidNetworking(Context context) {
        OkHttpClient httpConfiguration = new OkHttpClient().newBuilder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.initialize(context, httpConfiguration);
    }
}
