package com.runehou.cleanoutloud;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class    AddMessageActivity extends AppCompatActivity {


    Button btn_add_message;
    ProgressDialog prgDialog;
    EditText etMessage;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        btn_add_message = (Button) findViewById(R.id.button_wall_add_message);
        etMessage = (EditText) findViewById(R.id.et_message_new);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(getResources().getString(R.string.please_wait));
        prgDialog.setCancelable(false);


        btn_add_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams params = new RequestParams();
                String newMessage = etMessage.getText().toString();
                if (!newMessage.equals("")) {
                    params.put("message", newMessage);
                    params.put("token", prefs.getString("token", "empty_token"));
                    invokeREST(params);
                }


            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        prgDialog.dismiss();
    }

    public void invokeREST(final RequestParams params) {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://52.43.233.138:8080/CoLWebService/CoL/messages/createmessage", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {

                Intent intent = new Intent(getApplicationContext(), WallActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

//                startActivity(new Intent(getApplicationContext(), WallActivity.class));

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

}
