package com.runehou.cleanoutloud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        skipBtn = (Button) findViewById(R.id.skipBtn);
        skipBtn.setOnClickListener(this);
    }


    public void invokeREST(final RequestParams params) {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://10.0.2.2:8084/colrest/CoL/login/dologin", params, new AsyncHttpResponseHandler() {
//                        client.get("http://52.43.233.138:8080/CoLWebService/CoL/login/dologin", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getBoolean("success")) {
                        Toast.makeText(getApplicationContext(), "You are successfully logged in!", Toast.LENGTH_LONG).show();
                        // Navigate to Home screen

                        String token = obj.getString("token");
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
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                    error.printStackTrace();

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == loginBtn) {

            String userName = this.userName.getText().toString();
            String password = this.password.getText().toString();

            RequestParams params = new RequestParams();

            if (Utility.isNotNull(userName) && Utility.isNotNull(password)) {
                params.put("username", userName);
                params.put("password", password);
                invokeREST(params);
            } else {
                Toast.makeText(getApplicationContext(), R.string.invalid_user_pass, Toast.LENGTH_SHORT).show();
            }
        } else if (v == skipBtn) {
            RequestParams params = new RequestParams();
            params.put("username", "rune");
            params.put("password", "1234");
            invokeREST(params);
        } else if (v == registerLink) {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        }
    }
}
