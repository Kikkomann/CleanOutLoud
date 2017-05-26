package com.runehou.cleanoutloud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText userName;
    TextInputLayout email_layout;
    TextInputEditText password;
    TextInputLayout password_layout;
    Button loginBtn, skipBtn;
    ProgressDialog prgDialog;
    TextView errorTxt, registerLink;
    SharedPreferences prefs;
    String strUserName, strPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        errorTxt = (TextView) findViewById(R.id.errorTxt);

        userName = (TextInputEditText) findViewById(R.id.input_email);
        email_layout = (TextInputLayout) findViewById(R.id.input_email_layout);

        password = (TextInputEditText) findViewById(R.id.input_password);
        password_layout = (TextInputLayout) findViewById(R.id.input_password_layout);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);

        registerLink = (TextView) findViewById(R.id.text_register_link);
        registerLink.setOnClickListener(this);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(getResources().getString(R.string.please_wait));
        prgDialog.setCancelable(false);

        skipBtn = (Button) findViewById(R.id.skipBtn);
        skipBtn.setOnClickListener(this);
    }


    public void invokeREST(final RequestParams params) {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
//        client.get("http://10.0.2.2:8084/colrest/CoL/login/dologin", params, new AsyncHttpResponseHandler() {
                        client.get("http://52.43.233.138:8080/CoLWebService/CoL/login/dologin", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("success")) {
                        Toast.makeText(getApplicationContext(), R.string.login_success, Toast.LENGTH_LONG).show();
                        // Navigate to Home screen

                        String token = obj.getString("token");
                        prefs.edit().putString("token", token).commit();
                        Log.d("Nicki", "username: " + strUserName);
                        prefs.edit().putString("username", strUserName).commit();

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("TOKEN", token);
                        startActivity(intent);
                    }
                    // Else display error message
                    else {
                        errorTxt.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), R.string.json_error, Toast.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }


            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), R.string.resource_not_found, Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), R.string.unexpected_error, Toast.LENGTH_LONG).show();
                    error.printStackTrace();

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == loginBtn) {

            strUserName = this.userName.getText().toString();
            strPassword = this.password.getText().toString();

            RequestParams params = new RequestParams();

            if (Utility.isNotNull(strUserName) && Utility.isNotNull(strPassword)) {
                params.put("username", strUserName);
                params.put("password", strPassword);
                invokeREST(params);
            } else {
                Toast.makeText(getApplicationContext(), R.string.invalid_user_pass, Toast.LENGTH_SHORT).show();
            }
        } else if (v == skipBtn) {
            RequestParams params = new RequestParams();
            strUserName = "rune";
            strPassword = "1234";
            params.put("username", strUserName);
            params.put("password", strPassword);
            invokeREST(params);
        } else if (v == registerLink) {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        }
    }
}
