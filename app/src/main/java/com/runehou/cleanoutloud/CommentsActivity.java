package com.runehou.cleanoutloud;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentsActivity extends Activity {


    Button btn_add_comment;

    ProgressDialog prgDialog;
    TextView tvOriginalMessageInfo, tvOriginalMessageDate, getTvOriginalMessageText;
    EditText etNewComment;

    private ListView listView;
    ArrayList<commentsObject> commentsObjectList = new ArrayList<>();
    private CustomAdapter adapter;
    int messageId;
    SharedPreferences prefs;
    String messageInfo, messageDate, messageText, messageAllText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        btn_add_comment = (Button) findViewById(R.id.button_comments_add_commment);
        tvOriginalMessageDate = (TextView) findViewById(R.id.tv_comments_original_message_date);
        tvOriginalMessageInfo = (TextView) findViewById(R.id.tv_comments_original_message_info);
        getTvOriginalMessageText = (TextView) findViewById(R.id.tv_comments_original_message);
        listView = (ListView) findViewById(R.id.listview_comments);
        etNewComment = (EditText) findViewById(R.id.et_comment);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage(getResources().getString(R.string.please_wait));
        prgDialog.setCancelable(false);

        adapter = new CustomAdapter();

        Bundle b = getIntent().getExtras();
        if (b != null) {
            messageAllText = b.getString("messageText");
            Log.d("Nicki", "messageAlleText " + messageAllText);

            messageInfo = messageAllText.substring(0, messageAllText.indexOf(":"));
            messageText = messageAllText.substring(messageAllText.indexOf(":") + 4);
            messageDate = b.getString("messageDate");
            tvOriginalMessageInfo.setText(messageInfo);
            getTvOriginalMessageText.setText(messageText);
            tvOriginalMessageDate.setText(messageDate);
            messageId = b.getInt("messageId");
            RequestParams params = new RequestParams();
            params.put("messageid", String.valueOf(messageId));
            invokeREST(params);
        } else {
            Toast.makeText(getApplicationContext(), R.string.msg_id_not_found, Toast.LENGTH_SHORT).show();
        }


        btn_add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etNewComment.getText().toString().equals("")) {
                    RequestParams params = new RequestParams();
                    Log.d("Nicki", "etNewComment " + etNewComment.getText());
                    String str = String.valueOf(etNewComment.getText());
                    params.put("comment", str);
                    params.put("messageid", String.valueOf(messageId));
                    params.put("token", prefs.getString("token", "empty_token"));
                    invokeRESTAddComment(params);
                    adapter.notifyDataSetChanged();
                } else {
                 Toast.makeText(getApplicationContext(), R.string.add_comment_error, Toast.LENGTH_LONG).show();
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
        client.get("http://52.43.233.138:8080/CoLWebService/CoL/messages/comments", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    JSONArray commentList = obj.getJSONArray("comment");
                    for (int i = 0; i < commentList.length(); i++) {
                        JSONObject item = commentList.getJSONObject(i);
                        commentsObjectList.add(new commentsObject(item.getString("text"), Utility.dateFormat(item.getString("date"), getResources().getString(R.string.date_error))));
                    }
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
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


    public void invokeRESTAddComment(final RequestParams params) {
        prgDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://52.43.233.138:8080/CoLWebService/CoL/messages/addcomment", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();

                Intent intent = new Intent(getApplicationContext(), CommentsActivity.class);
                Bundle b = new Bundle();
                b.putInt("messageId", messageId);
                b.putString("messageText", messageAllText);
                b.putString("messageDate", messageDate);
                intent.putExtras(b);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

    public class commentsObject {
        String text;
        String date;

        commentsObject(String text, String date) {
            this.text = text;
            this.date = date;
        }

    }


    public class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return commentsObjectList.size();
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
                view = getLayoutInflater().inflate(R.layout.wall_item, null);
            }

            TextView tvInfo = (TextView) view.findViewById(R.id.tv_message_info);
            TextView tvDate = (TextView) view.findViewById(R.id.tv_message_date);
            TextView tvText = (TextView) view.findViewById(R.id.tv_message);

            String str = commentsObjectList.get(position).text;
            tvInfo.setText(str.substring(0, str.indexOf(":")));
            tvText.setText(str.substring(str.indexOf(":") + 4));
            tvDate.setText(commentsObjectList.get(position).date);

            return view;
        }
    }


}
