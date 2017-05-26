package com.runehou.cleanoutloud;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GarbageActivity extends Activity {


    Button btnAddGarbage;
    ProgressDialog prgDialog;
    TextView tvOverskrift;

    private ListView listView;
    ArrayList<campObject> campObjectList = new ArrayList<>();
    private CustomAdapter adapter;
    SharedPreferences prefs;
    AlertDialog alert11;
    boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);
        isAdmin = false;

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        btnAddGarbage = (Button) findViewById(R.id.button_wall_add_message);
        btnAddGarbage.setText(R.string.add_trash);
        btnAddGarbage.setVisibility(View.GONE);
        tvOverskrift = (TextView) findViewById(R.id.tv_overskrift_wall);
        tvOverskrift.setText(R.string.trash_point);


        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(getResources().getString(R.string.please_wait));
        prgDialog.setCancelable(false);

        adapter = new CustomAdapter();

        final RequestParams params = new RequestParams();
        params.put("username", prefs.getString("username", "empty_username"));
        params.put("token", prefs.getString("token", "empty_token"));
        Log.d("Nicki", "BUTTON! token: " + prefs.getString("token", "empty_token") + " username: " + prefs.getString("username", "empty_username"));
        invokeRESTToCheckUsertype(params);

        RequestParams params2 = new RequestParams();
        params2.put("sorted", "true");
        invokeREST(params2);


        listView = (ListView) findViewById(R.id.listview_messages);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
          if (isAdmin) {
              final int k = i;
              final EditText inputText = new EditText(GarbageActivity.this);
              inputText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(GarbageActivity.this);
                builder1.setMessage(getString(R.string.add_trash_to) + campObjectList.get(i).name);
                builder1.setCancelable(true);
                builder1.setView(inputText);



                builder1.setPositiveButton(
                        R.string.add,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                RequestParams params = new RequestParams();
                                params.put("campname", campObjectList.get(k).name);
                                String str = String.valueOf(inputText.getText());
                                str = str.replace(",", ".");
                                params.put("weight", str + "f");
                                params.put("token", prefs.getString("token", "empty_token"));
                                invokeRESTAddGarbage(params);
                                dialog.cancel();

                                Intent intent = new Intent(getApplicationContext(), GarbageActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);


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

            }
        });


//        btnAddGarbage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        prgDialog.dismiss();
    }

    public void invokeREST(final RequestParams params) {
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
                        Log.d("Nicki", "getWeight: " + Float.valueOf(item.getString("weight")));
                        campObjectList.add(new campObject(item.getString("name"), item.getString("location"), Float.valueOf(item.getString("weight"))));
                    }
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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


    public void invokeRESTToCheckUsertype(final RequestParams params) {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://52.43.233.138:8080/CoLWebService/CoL/user", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if (obj.getString("usertype").equals("admin")) {
//                        btnAddGarbage.setVisibility(View.VISIBLE);
                        isAdmin = true;
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



    public void invokeRESTAddGarbage(final RequestParams params) {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://52.43.233.138:8080/CoLWebService/CoL/camps/addgarbage", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
             Toast.makeText(getApplicationContext(), R.string.trash_added, Toast.LENGTH_SHORT).show();

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



    public class campObject {
        String name;
        String location;
        Float weight;

        campObject(String name, String location, Float weight) {
            this.name = name;
            this.location = location;
            this.weight = weight;
        }

    }


    public class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return campObjectList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.recycle_item_wall, null);
            }

            TextView tvInfo = (TextView) view.findViewById(R.id.tv_message_info);
            TextView tvDate = (TextView) view.findViewById(R.id.tv_message_date);
            TextView tvText = (TextView) view.findViewById(R.id.tv_message);

            tvInfo.setText(campObjectList.get(position).name);
            tvDate.setText(String.valueOf(campObjectList.get(position).weight) + getString(R.string.kg));
            tvText.setText(campObjectList.get(position).location);

            return view;
        }
    }


}
