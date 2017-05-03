package com.runehou.cleanoutloud;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddMessageActivity extends ListActivity implements View.OnClickListener {


    Button btn_add_message;
    ProgressDialog prgDialog;

    private ListView listView;
    ArrayList<MessageObject> messageObjectList = new ArrayList<>();
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        btn_add_message = (Button) findViewById(R.id.button_wall_add_message);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        adapter = new CustomAdapter();
        invokeREST();
        listView = (ListView) findViewById(R.id.listview_messages);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), CommentsActivity.class);
                Bundle b = new Bundle();
                b.putInt("messageId", messageObjectList.get(i).id); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
                finish();
            }
        });


    }


    public void invokeREST() {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://52.43.233.138:8080/CoLWebService/CoL/objects/wall", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                Log.d("Nicki", ": ON SUCCESS!" );
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true

                        JSONArray messageList = obj.getJSONArray("wall");

                        for (int i = 0; i < messageList.length(); i++) {

                            JSONObject item = messageList.getJSONObject(i);
                            Log.d("Nicki", ": " + item.getString("text"));
                            messageObjectList.add(new MessageObject(item.getString("text"), item.getString("date"), item.getInt("id")));
                        }
                    setListAdapter(adapter);
                    adapter.notifyDataSetChanged();



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
    public void onClick(View view) {
        if (view == btn_add_message) {
            Intent intent = new Intent(this, CommentsActivity.class);
            startActivity(intent);
            finish();
//            startActivity(new Intent(getApplicationContext(), CommentsActivity.class));
        }
    }

    public class MessageObject {
        String text;
        String date;
        int id;

        MessageObject (String text, String date, int id) {
            this.text = text;
            this.date= date;
            this.id = id;
        }

    }


    public class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return messageObjectList.size();
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

            String str = messageObjectList.get(position).text;
            tvInfo.setText(str.substring(0,str.indexOf(":")));
            tvText.setText(str.substring(str.indexOf(":")+1));
            tvDate.setText(messageObjectList.get(position).date);

            return view;
        }
    }



}
