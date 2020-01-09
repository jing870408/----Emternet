package com.example.emternet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Chating extends AppCompatActivity {
    private EditText editText;
    private TextView textView;
    private Button button;
    private Timer timer;
    Handler handler;
    String _account;
    String Friend;
    //取得房間代碼
    String strUrl = "http://Tkuim3asdemternet-env-1.nhigxjvkwf.us-east-2.elasticbeanstalk.com/api/ChatRoomLists/";
    //房間
    String room ;
    //取得歷史訊息
    //傳送訊息
    String strUrl2 = "http://Tkuim3asdemternet-env-1.nhigxjvkwf.us-east-2.elasticbeanstalk.com/api/ChatRooms/";
    String Content;
    OkHttpClient okHttpClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chating);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        editText = findViewById(R.id.editText6);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        timer = new Timer();

        SharedPreferences pref = getSharedPreferences("ChatWith",MODE_PRIVATE);
        Friend = pref.getString("Friend","");
        SharedPreferences pref2 = getSharedPreferences("LoginData",MODE_PRIVATE);
        _account = pref2.getString("Account","");
        Log.i("Who : ",_account);
        Log.i("Chatwith : ",Friend);

        getHistory();
        setTimerTask();
    }
    private void setTimerTask() {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getHistory();
            }
        }, 1000, 1000/* 表示1000毫秒之後，每隔1000毫秒執行一次 */);
    }

    public void getHistory(){
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if(msg.what == 1) {
                    RequestBody formBody = new FormBody.Builder()
                            .add(_account,Friend)
                            .build();
                    okHttpClient = new OkHttpClient();
                    final Request request = new Request.Builder()
                            .url(strUrl2 + _account + "/" + Friend)
                            .post(formBody)
                            .build();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Response response = null;
                            try {
                                response = okHttpClient.newCall(request).execute();
                                if (response.isSuccessful()) {
                                    Content = response.body().string();
                                    Message msg = new Message();
                                    msg.what = 2;
                                    handler.sendMessage(msg);
                                } else {
                                    throw new IOException("Unexpected code " + response);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }else if(msg.what == 2){
                    ProcessString(Content);
                }else if(msg.what == 3){
                    getHistory();
                    editText.setText("");
                }
            }

        };

        RequestBody formBody = new FormBody.Builder()
                .add(_account,Friend)
                .build();
        okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(strUrl + _account + "/" + Friend)
                .post(formBody)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Message msg = new Message();
                        room = response.body().string();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void ProcessString(String ss){

        String text = "";
        try {
            JSONArray jsArray = new JSONArray(ss);
            for(int i = 0;i < jsArray.length(); i++) {
                JSONObject jsdata = jsArray.getJSONObject(i);
                text = text + jsdata.getString("sender") + ":"
                        +jsdata.getString("sendContent") + "\r\n";
            }
            textView.setMovementMethod(new ScrollingMovementMethod());
            textView.setText(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public void click_to_send(View view) {
        String mess = editText.getText().toString();
        if(!mess.equals("")){
            Gson gson = new Gson();
            Date date =new Date();
            String datetime = date.toString();
            int i = Integer.parseInt(room);
            Messages messages = new Messages(mess,date,_account,i);
            String json = gson.toJson(messages);

            okHttpClient = new OkHttpClient();

            RequestBody body = RequestBody.create(json,JSON);

            final Request request = new Request.Builder()
                    .url(strUrl2)
                    .post(body)
                    .build();


            new Thread(new Runnable() {
                @Override
                public void run() {
                    Response response = null;
                    try {
                        response = okHttpClient.newCall(request).execute();
                        if (response.isSuccessful()) {
                            Message msg = new Message();
                            msg.what = 3;
                            handler.sendMessage(msg);
                            Log.i("state","sucess");
                        } else {
                            Log.i("WY", "ec" + response);
                            throw new IOException("Unexpected code " + response);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    class Messages{
        String SendContent;
        Date Sendtime;
        String Sender;
        int RoomId;
        Messages(String _sendcontent, Date _sendtime, String _sender, int _roomid){
            SendContent = _sendcontent;
            Sendtime = _sendtime;
            Sender = _sender;
            RoomId = _roomid;
        }
    }
}

