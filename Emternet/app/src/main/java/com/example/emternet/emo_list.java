package com.example.emternet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class emo_list extends Activity {
    private EditText sleep;
    private EditText sleept;
    private EditText avgheart;
    private EditText sapp;
    private EditText gapp;
    private EditText fapp;
    private EditText oapp;
    String account;
    Date dt;
    String url = "http://Tkuim3asdemternet-env-1.nhigxjvkwf.us-east-2.elasticbeanstalk.com/api/OriVarDatas/";
    OkHttpClient okHttpClient;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emo_list);
        sleep = (EditText) findViewById(R.id.sleep);
        sleept = (EditText) findViewById(R.id.sleept);
        avgheart = (EditText) findViewById(R.id.avgheart);
        sapp = (EditText) findViewById(R.id.sapp);
        gapp = (EditText) findViewById(R.id.gapp);
        fapp = (EditText) findViewById(R.id.fapp);
        oapp = (EditText) findViewById(R.id.oapp);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginData", MODE_PRIVATE);
        account = sharedPreferences.getString("Account", "");
        String dw = sharedPreferences.getString("Date", "");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        try {
            dt = sdf.parse(dw);
        } catch (ParseException e) {
            Log.i("Er", "Date format");
        }
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 1) {
                    Intent intent = new Intent(emo_list.this, Island.class);
                    startActivity(intent);
                }
            }

        };
    }
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public void uuu(View v){

                   String v1 = sleep.getText().toString();
                   String v4 = sapp.getText().toString();
                   String v5 = gapp.getText().toString();
                   String v6 = fapp.getText().toString();
                   String v7 = oapp.getText().toString();
                    double v2 = Double.parseDouble(sleept.getText().toString());
                    int v3 = Integer.parseInt(avgheart.getText().toString());
                    Gson gson = new Gson();
                    EmoData emoData = new EmoData(v1,v2,v3,v4,v5,v6,v7);
                    Log.i("12",emoData.OtherApp.toString());
                    String json = gson.toJson(emoData);
                    okHttpClient = new OkHttpClient();

                    RequestBody body = RequestBody.create(json,JSON);

                    final Request request = new Request.Builder()
                            .url(url)
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
                                    msg.what = 1;
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

    class EmoData{
        Date dd;
        String Account;
        String Sleep;
        double Sleeptime;
        int AvgHeartRate;
        String SocialApp;
        String GameApp;
        String FunApp;
        String OtherApp;
        EmoData(String Sleep,double Sleeptime,int AvgHeartRate,String SApp,String GApp,String Fapp,String Oapp){
            this.dd = dt;
            this.Account = account;
            this.Sleep = Sleep;
            this.Sleeptime = Sleeptime;
            this.AvgHeartRate = AvgHeartRate;
            this.SocialApp = SApp;
            this.GameApp = GApp;
            this.FunApp = Fapp;
            this.OtherApp = Oapp;
        }
    }
}

