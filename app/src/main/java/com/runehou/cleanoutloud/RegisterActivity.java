package com.runehou.cleanoutloud;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Register Activity Class
 */
public class RegisterActivity extends Activity implements View.OnClickListener {
    ProgressDialog prgDialog;
    TextView errorMsg, loginLink;
    EditText etUserName, etPassword, etPasswordConfirm;
    Button registerBtn, btnChooseCamp;
    String selectedCamp;
    AlertDialog alert11;


    private List<String> camps = new ArrayList<String>();

    String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(getResources().getString(R.string.please_wait));
        prgDialog.setCancelable(false);

        selectedCamp = "";

        // Find Error Msg Text View control by ID
        errorMsg = (TextView) findViewById(R.id.register_error);

        loginLink = (TextView) findViewById(R.id.text_login_link);
        loginLink.setOnClickListener(this);

        etUserName = (EditText) findViewById(R.id.input_username);

        etPasswordConfirm = (EditText) findViewById(R.id.input_confirm);
        etPassword = (EditText) findViewById(R.id.input_password);


        RequestParams params = new RequestParams();
        params.put("sorted", "true");
        invokeRESTCamps(params);


//        campsSpinner = (Spinner) findViewById(R.id.camps_spinner);
        btnChooseCamp = (Button) findViewById(R.id.btn_register_choose_camp);
        btnChooseCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NumberPicker CampPicker = new NumberPicker(RegisterActivity.this);
                final String[] stringsCamps = camps.toArray(new String[camps.size()]);
                CampPicker.setMinValue(0);
                CampPicker.setMaxValue(stringsCamps.length-1);
                CampPicker.setDisplayedValues(stringsCamps);


                AlertDialog.Builder builder1 = new AlertDialog.Builder(RegisterActivity.this);
                builder1.setMessage(R.string.choose_your_camp);
                builder1.setCancelable(true);
                builder1.setView(CampPicker);



                builder1.setPositiveButton(
                        getResources().getString(R.string.choose_camp),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                selectedCamp = stringsCamps[CampPicker.getValue()];
                                btnChooseCamp.setText(selectedCamp);
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                alert11 = builder1.create();
                alert11.show();








            }
        });
        registerBtn = (Button) findViewById(R.id.btn_register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                RequestParams params1 = new RequestParams();
//                params1.put("username", etUserName);
//                params1.put("password", etPassword);
//                params1.put("camp", selectedCamp);
//                invokeWS(params1);

                registerUser();

            }
        });

    }

    /**
     * Method gets triggered when Register button is clicked
     *
     * @param
     */
    public void registerUser() {
        // Get NAme ET control value
        String name = etUserName.getText().toString();
        // Get Email ET control value
        String password = etPassword.getText().toString();
        // Get Password ET control value
        String passwordConFirm = etPasswordConfirm.getText().toString();
        // Instantiate Http Request Param Object
        RequestParams params = new RequestParams();
        if (Utility.isNotNull(name) && Utility.isNotNull(passwordConFirm) && Utility.isNotNull(password)) {
            if (password.equals(passwordConFirm)) {

                if (!selectedCamp.isEmpty()) {
                    params.put("username", name);
                    params.put("password", password);
                    params.put("camp", selectedCamp);
                    // Invoke RESTful Web Service with Http parameters
                    invokeWS(params);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.choose_a_camp, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.password_match, Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), R.string.blank_form, Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Method that performs RESTful webservice invocations
     *
     * @param params
     */
    public void invokeWS(RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://52.43.233.138:8080/CoLWebService/CoL/login/createuser", params, new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {


                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getString("success").equals("true")){
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.user_not_created, Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Hide Progress Dialog
                prgDialog.hide();

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
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Toast.makeText(getApplicationContext(), R.string.unexpected_error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Method which navigates from Register Activity to Login Activity
     */
    public void navigatetoLoginActivity(View view) {
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        // Clears History of Activity
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }

    /**
     * Set degault values for Edit View controls
     */
    public void setDefaultValues() {
        etUserName.setText("");
        etPassword.setText("");
    }

    public void invokeRESTCamps(final RequestParams params) {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://52.43.233.138:8080/CoLWebService/CoL/camps", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Log.d("Nicki", ": ON SUCCESS!");
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    JSONArray campList = obj.getJSONArray("camps");
                    for (int i = 0; i < campList.length(); i++) {
                        JSONObject item = campList.getJSONObject(i);
                        camps.add(item.getString("name"));
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
    protected void onDestroy() {
        super.onDestroy();
        prgDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        if (v == loginLink) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
    }
}