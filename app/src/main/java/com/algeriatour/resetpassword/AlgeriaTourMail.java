package com.algeriatour.resetpassword;

import android.content.Context;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import java.util.concurrent.atomic.AtomicReference;

import es.dmoral.toasty.Toasty;

public class AlgeriaTourMail {

    private final static String ALGERIA_TOUR_Mail = "algeria.tour2018@gmail.com";
    private final static String ALGERIA_TOUR_PASSWORD = "123456aze";

    public static void sendResetCode(Context context, String destinationMail, String msg) {
        final String mailSubject = " Algeria Tours Reset Password";
        final String bodyMessage = msg;

        AtomicReference<Boolean> emailSend = new AtomicReference<>(false);
        BackgroundMail.newBuilder(context)
                .withUsername(ALGERIA_TOUR_Mail)
                .withPassword(ALGERIA_TOUR_PASSWORD)
                .withMailto(destinationMail)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject(mailSubject)
                .withBody(bodyMessage)
                .withSendingMessageSuccess(null)
                .withSendingMessageError(null)
                .withOnSuccessCallback(() ->{
                        Toasty.success(context, "password send success", Toast.LENGTH_LONG, true)
                                .show();
                        }
                )
                .withOnFailCallback(() ->
                        Toasty.error(context, "password send failed", Toast.LENGTH_LONG, true).show()
                )
                .send();
    }
}
