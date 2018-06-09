package com.algeriatour.contect_nous;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.algeriatour.R;
import com.algeriatour.utils.Networking;
import com.algeriatour.utils.StaticValue;
import com.algeriatour.utils.User;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class ContacterNousActivity extends AppCompatActivity {

    private final String contacFileName = "at_send_message.php";
    private final String web_site = StaticValue.MYSQL_SITE + contacFileName;
    @BindView(R.id.contacter_nous_message)
    EditText messageEditText;
    @BindView(R.id.contacter_nous_subject)
    EditText subjectEditText;
    SpotsDialog spotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacter_nous);
        ButterKnife.bind(this);
        Networking.initAndroidNetworking(this);
    }

    @OnClick(R.id.contacter_nous_back_btn)
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.contacter_nous_cancel_btn)
    public void onCancelClicked() {
        finish();
    }

    @OnClick(R.id.contacter_nous_send_btn)
    public void onSendButtonClicked() {
        if (spotsDialog == null) {
            spotsDialog = new SpotsDialog(this);
            spotsDialog.setCancelable(false);
        }

        if (!checkInput())
            return;

        spotsDialog.show();

        AndroidNetworking.post(web_site)
                .addBodyParameter(StaticValue.PHP_TARGET, StaticValue.PHP_MYSQL_TARGET)
                .addBodyParameter(StaticValue.PHP_USER_ID, User.getMembre().getId() + "")
                .addBodyParameter(StaticValue.PHP_CONTENT, messageEditText.getText().toString())
                .addBodyParameter(StaticValue.PHP_OBJECT, subjectEditText.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        spotsDialog.dismiss();
                        try {
                            if (response.getInt(StaticValue.JSON_NAME_SUCCESS) == 1) {
                                onMessageSendSuccess();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("tixx", "onResponse: catch = " + e.getMessage());
                        }
                        onMessageSendFail("cant send message ... server Error");
                    }

                    @Override
                    public void onError(ANError anError) {
                        spotsDialog.dismiss();
                        onMessageSendFail("can't send the message ... connection problem !");
                    }
                });

    }

    private boolean checkInput() {
        boolean valide = true;
        if (subjectEditText.getText().toString().isEmpty()) {
            subjectEditText.setError("remplire le sujet svp");
            valide = false;
        }
        if (messageEditText.getText().toString().isEmpty()) {
            messageEditText.setError("remplire le message svp");
            valide = false;
        }
        return valide;

    }

    private void onMessageSendSuccess() {
        Toasty.success(this, "message send success", Toast.LENGTH_LONG, true).show();
        finish();
    }

    private void onMessageSendFail(String msg) {
        Toasty.error(this, msg, Toast.LENGTH_LONG, true).show();

    }
}
