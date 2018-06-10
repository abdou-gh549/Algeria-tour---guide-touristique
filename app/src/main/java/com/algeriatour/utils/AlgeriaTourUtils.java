package com.algeriatour.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONObject;

import java.util.List;

/*
*   class contain static function to validate input format as name , email ...
*/
public class AlgeriaTourUtils {
    public static boolean isValidEmail(String target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static  Bitmap parsImage(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static void gpsRequest(Activity activity, GpsResponsListiner gpsRespons) {

        Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission
                        .ACCESS_FINE_LOCATION).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                Log.d("tixx", "onPermissionGranted: ");
                if(report.areAllPermissionsGranted()){
                    gpsRespons.onPermissionGaranted();
                }else {
                    gpsRespons.onPermissionDenied();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    public static String getString(int stringId){
        return User.getInstance().getString(stringId);
    }
    public interface GpsResponsListiner {
        void onPermissionDenied();
        void onPermissionGaranted();
    }

    public interface NetworkResponseAction {
        void onSuccess(Object response);

        void onFail(String msg);
    }

}
